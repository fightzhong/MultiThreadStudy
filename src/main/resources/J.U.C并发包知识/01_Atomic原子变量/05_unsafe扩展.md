## Unsafe
```java
在学习Atomic包的这些类中, 我们发现几乎里面所有的原子操作的实现都是依赖于unsafe这个对象的, unsafe
这个类表面的意思是不安全的, 不是说类本身是不安全的, 而是因为这个类过于强大, 能够使得开发者直接操控
内存(类似于C), 因为其里面的方法大部分是native方法, 而对于大部分开发者来说, 直接对内存进行操控存在
许多的不安全性, 所以这个类才叫不安全的, 我们跟进AtomicInteger的源码发现, 它通过调用Unsafe.getUnsafe
方法来获取Unsafe对象的, 然而我们自己调用这个方法的时候会报安全异常, 因为在getUnsafe方法中有以下代码:

@CallerSensitive
public static Unsafe getUnsafe() {
  Class var0 = Reflection.getCallerClass();
  if (!VM.isSystemDomainLoader(var0.getClassLoader())) {
      throw new SecurityException("Unsafe");
  } else {
      return theUnsafe;
  }
}

所以对于不是系统类加载器加载的类, 是会报异常的, 为了能够获得这个对象, 我们通过反射的方式来获取:
Class<?> c = Class.forName("sun.misc.Unsafe");
Constructor con = c.getDeclaredConstructor();
con.setAccessible( true );
unsafe = (Unsafe)con.newInstance();

在Unsafe中提供了许多强大的功能, 下面我们会选择几个来玩一下
```
## monitorEnter&&monitorExit
```java
了解synchronized在JVM中的实现的可能知道, synchronized关键字在字节码层面就是通过这两个指令来保证
临界区的互斥操作的, 也就是说我们可以通过unsafe的这两个方法来达到synchronized的效果, 接下来我们上
一段代码

public class TestClass1 {
	private static int a = 1;  // 定义一个静态成员变量a
	public static void main (String[] args) throws Exception {
		// 通过反射获取Unsafe对象
		Class<?> c = Class.forName("sun.misc.Unsafe");
		Constructor con = c.getDeclaredConstructor();
		con.setAccessible( true );
		Unsafe unsafe = (Unsafe)con.newInstance();

		// 利用CountDownLatch来实现在输出a这个变量前这些线程都已经执行完毕
		CountDownLatch coutDown = new CountDownLatch( 5 );
		for ( int i = 0; i < 5; i ++ ) {
			new Thread(){
				@Override
				public void run () {
					// monitorEnter
					unsafe.monitorEnter( TestClass1.class );
					for ( int i = 0; i < 500; i ++ ) {
						try {
							// 小小的休眠一毫秒来使得线程安全问题更加的明显
							Thread.sleep( 1 );
						} catch (InterruptedException e) {}
						a++;
					}
					coutDown.countDown();
					// monitorExit
					unsafe.monitorExit( TestClass1.class );
				}
			}.start();
		}

		// 在上面这些线程都执行完毕前会阻塞在这里
		coutDown.await();
		System.out.println( a );
	}
}

分析: 最后我们发现, 如果去掉了monitorEnter和monitorExit两个, 线程安全问题会非常的明显, 而加上之后
      不管执行多少次都没有出现安全问题
```

## putInt
```java
// 首先我们定义了一个Test类
class Test {
	private int a = 10; // 私有的成员属且没有提供set方法, 意味着在外部是不能改变的
	public int getA () {
		return a;
	}
}

public class TestClass2 {
	public static void main (String[] args) throws Exception {
		// 通过反射获取Unsafe对象
		Class<?> c = Class.forName("sun.misc.Unsafe");
		Constructor con = c.getDeclaredConstructor();
		con.setAccessible( true );
		Unsafe unsafe = (Unsafe)con.newInstance();

    // 构建Test对象
		Test obj = new Test();
		System.out.println( obj.getA() );
		unsafe.putInt( obj, unsafe.objectFieldOffset( Test.class.getDeclaredField( "a" ) ), 66 );
		System.out.println( obj.getA() );
	}
}

结果分析: 我们发现结果是先打印了10, 然后再打印了66, 也就是说unsafe.putInt方法实现了修改私有属性的
          功能, 在开头有提到, unsafe对象能够直接操控内存, 这就是一个典型的体现, 下面我们来说说这个
          putInt方法

putInt(Object obj, long offset, int newValue):
      Object表示要操控的对象, offset表示要操控的对象的属性的内存偏移量, newValue表示新的值, 通过
      unsafe.objectFieldOffset( Field field )方法能够获取一个对象的属性的偏移量
```
