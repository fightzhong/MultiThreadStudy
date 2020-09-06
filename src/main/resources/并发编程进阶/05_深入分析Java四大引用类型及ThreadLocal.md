### 引入
```
在Java中有四大引用类型, 强引用、软引用、弱引用、虚引用, 其中弱引用在JDK中的一个典型引用场景就是
ThreadLocal, 本章内容将会从四大引用类型开始讲起, 之后再以ThreadLocal的源码来分析弱引用是如何在
ThreadLocal中得到使用的, 以及弱引用是如何解决在ThreadLocal中的内存泄漏问题的
```

### 如何创建四大引用类型
```java
// 创建一个强引用
Object obj = new Object();

// 创建一个软引用, new Object()对象就会被当成一个软引用
SoftReference<Object> softReference = new SoftReference<>( new Object() );

// 创建一个弱引用
WeakReference<Object> weakReference = new WeakReference<>( new Object() );

// 创建一个虚引用(虚引用必须有一个引用队列, 在该引用中的对象被回收的时候会被放入到引用队列中)
PhantomReference<Object> weakReference
        = new PhantomReference<>( new Object(), new ReferenceQueue<>() );
```

### 四大引用类型及其GC特点描述
```
强引用: 我们在代码中直接指向的引用即为强引用, 比如: Object obj = new Object()中, obj到
        new Object()这个对象的引用就是强引用, 在垃圾回收的时候, 基于GCRoot的垃圾判断算法就是指由
        GCRoot出发, 一直查找下去直到没有一个强引用出现为止, 查找过程中出现的对象就是存活的对象, 即
        不会被GC的对象, 换句话说, 强引用指向的对象在GC的过程中是一定不会被回收的

软引用: 软引用其实指的是Java中的一个类SoftReference, 在该类中有一个referent指针, 该指针会指向一个
        对象(由我们自己定义), 在GC中, 通常情况下不会回收软引用指向的对象, 但是当堆内存在GC之后还是
        不够的时候, JVM就会将SoftReference中的对象进行回收, 根据SoftReference中的JavaDoc描述,
        JVM会在OOM之前一定会将软引用进行回收, 那么根据这个特点, 软引用就特别适合用于缓存相关的场景,
        当内存不够的时候JVM会将软引用指向的内存空间进行回收, 对于缓存来说, 即使回收了也是没有任何
        影响的

弱引用: 弱引用是比软引用的引用强度还要弱的, 软引用是在发生OOM之前会被JVM进行回收, 但是弱引用就不同
        了, 在GC中, 弱引用是一定会被回收的, 弱引用仍然也是Java中的一个类WeakReference, 如果我们
        期望将一个对象变成弱引用, 只需要将该对象放在WeakReference实例中的referent指针中, 这样GC
        一旦发生, 该对象就会被直接回收

虚引用: 虚引用即没有引用, 该引用的使用场景可以说是基本遇不到的, 其仍然是Java中的一个类
        [PhantomReference], 如果想将一个对象变为虚引用, 只需要将其放入PhantomReference实例中的
        referent指针上就好了, 需要注意的是, 在软引用和弱引用中的对象是可以通过get方法来获取的, 但
        是虚引用中的对象是不能获取的, 调用get方法会返回一个null, 也就是说我们Java代码是直接无法获
        取的, 当一个对象处于虚引用状态的时候, 一旦被GC, 那么就会向实例化该虚引用对象时传入的队列中
        发送一个通知, 告知该对象被回收了, 该引用的使用场景就是操作堆外内存(非JVM管理的内存, 而是由
        OS管理的内存)的时候, 在该内存被回收时可以在Java中获取到, 所以通常情况下我们是用不到这个虚
        引用的

小小的总结:
    在软引用、弱引用以及虚引用中的对象, 一旦有一个强引用指向它, 那么该对象在GC时是不会进行回收的,
    当一个对象仅仅处于这三种引用状态的时候, GC才会对其做不同的处理, 软引用一般用于缓存, 在发生OOM
    之前GC会将所有的软引用进行回收, 其他情况是不会进行回收的, 弱引用在GC中一定会被回收, 但是当一个
    处于弱引用状态下的对象仍然有强引用指向它的时候, 弱引用也是不会被回收的, 弱引用通常的应用场景是
    映射, 典型的应用场景是ThreadLocal, 之后我们会对ThreadLocal进行详细的讲解, 虚引用的应用场景是
    操作堆外内存的时候, 期望通过Java代码能够检测到内存的回收
```

### 验证软引用在OOM发生前才会被回收
```java
// 测试代码
SoftReference<byte[]> softReference = new SoftReference<>( new byte[1024 * 1024 * 10] );
System.out.println( softReference.get() );

byte[] data = new byte[1024 * 1024 * 10];
System.out.println( softReference.get() );

分析一: 在上述代码中, 首先放入了一个10M的数组在软引用中, 然后我们进行输出, 之后又创建了一个10M的数
        组, 我们直接将其放入一个main函数执行, 会发现两次的打印都打印出了了内存地址, 因为正常情况下
        堆内存肯定是能够存放下20M的数据的

测试二: 在JVM的启动参数中加入两个参数[ -Xms20m -Xmx20m ], 指定堆内存的大小固定为20M

分析二: 当我们再去执行上述代码的时候, 由于堆内存固定成了20M, 我们放入了一个10M的数组到软引用后, 由
        于堆中肯定是还存在其它的对象的, 所以堆空间占据的大小肯定是大于这个10M的, 换句话说, 堆空间
        剩余容量肯定是小于10M的, 当我们再一次放入10M的数组后, 由于堆空间不够了, 此时JVM会回收所有
        的软引用, 从而使得当我们第二次去获取软引用中的对象的时候, 得到的结果为null
```

### 验证弱引用在GC的时候一定会回收
```java
private static void testWeakReference () {
   Date date = new Date();

   WeakReference<Date> weakReference = new WeakReference<>( date );
   System.out.println( weakReference.get() );

   System.gc();

   System.out.println( weakReference.get() );
}

分析一: 执行上面的代码, 会发现我们两次都能输出日期值, 这貌似和我们所说的弱引用在GC时一定会回收是不
         相同的, 其实不然, 因为new Date()这个对象, 在该方法执行的时候, 仍然有一个强引用date指向
         它, 根据上面的描述我们可以知道, 强引用引向的对象是不会被回收的, 改进一下代码, 在System.gc()
         这行代码前面加一个date = null, 再次执行代码会发现, 第二次的输出已经变成了null了
```


### 以ThreadLocal源码来看看弱引用的使用
```java
// 以set方法为例子
public void set(T value) {
   Thread t = Thread.currentThread();
   ThreadLocalMap map = getMap(t);
   if (map != null)
      map.set(this, value);
   else
      createMap(t, value);
}

分析:
   <1> 获取当前线程对象
   <2> getMap中的代码等价于ThreadLocalMap map = t.threadLocals
   <3> 由此可见, 在每个Thread线程中, 都会维护一个ThreadLocalMap对象
   <4> map.set(this, value), 根据这行代码, 我们可以猜测到, 会往当前线程的ThreadLocalMap对象中放
      入一个键值对, 键为this, this为当前ThreadLocal对象, value为调用set方法时的value
   <5> 由此可见, 我们调用TheadLocal的set方法, 其实就是往线程的本地变量threadLocals这个map中放入
         了一个以当前ThreadLocal对象为键, 以我们传入的值为value的键值对

如果有一定的数据结构基础的话, 其实可以很容易猜到, map中是如何存放一个键值对的呢, 就是利用一个Entry
对象, 在这个对象中存放着key ,value两个属性, ThreadLocalMap也是如此, 接下来我们看一下ThreadLocalMap
中的Entry类的定义:

static class Entry extends WeakReference<ThreadLocal<?>> {
   /** The value associated with this ThreadLocal. */
   Object value;

   Entry(ThreadLocal<?> k, Object v) {
         super(k);
         value = v;
   }
}

分析:
   <1> Entry是一个虚引用类的子类, 继承于WeakReference
   <2> WeakReference继承于Reference, Reference中有一个属性referent。
   <3> 以referent作为key, 以value作为值, 这就是Entry保存键值对的实现
   <4> key是ThreadLocal对象, value是我们传入的值
```

### 为什么key要用弱引用?
```java
我们通常是在一个类中这样定义一个ThreadLocal:
   private static ThreadLocal<String> threadLocal = new ThreadLocal<String>()

之后我们会通过调用set方法往ThreadLocal中放入一个值, 通过get方法获取这个值, 根据上面的源码的简单分
析, 我们知道放入ThreadLocal中的值其实就是放入到了当前线程对象中的threadLocals这个ThreadLocalMap
中, 并且以threadLocal作为key, 以传入的value作为值, 换句话说, 就是在当前线程中会有一个指针指向这个
threadLocal对象, 在线程不被销毁的情况下, 这个指向是永远不会消失的, 假设我们采用强引用, 那么一旦在
上述的代码中, 我们主动的调用threadLocal = null, 我们期望断开threadLocal这个指向, 从而使得GC能够
回收这个ThreadLocal, 但是由于线程本地变量threadLocals这个map中采用的是强引用, 所以该threadLocal
对象不会被回收, 但是我们却以为其已经被回收了, 这就是典型的内存泄露

当我们Entry中的key采用了弱引用后, 换句话说, Entry中指向ThreadLocal对象的指针是一个弱引用的情况下,
当我们真正的ThreadLocal被置为null的时候, 由于没有强引用再指向该对象, 下一次GC的时候, 线程中保留的
threadLocal指针就会被回收了
```

### ThreadLocal仍然存在内存泄露?
```
通过上面的描述可以知道, ThreadLocalMap中的Entry通过弱引用指向threadLocal对象, 解决了key的内存泄
露问题, 但是ThreadLocal中仍然还是存在内存泄露的, 当我们手动的将threadLocal = null的时候, 下次GC
的时候, 自然就会将Entry中的key给回收了, 但是value却仍然是一个强引用指向原来的对象, 由于key已经变为
了null, 我们就没法定位到这个value了, 所以value也出现了内存泄露的问题, 那么如何解决呢? 只需要我们
在使用完ThreadLocal的时候, 手动的调用其remove方法即可, 在该方法中对key为null的情况进行了处理
```

### 扩展
```
ThreadLocalMap是通过数组来存储Entry的, 有点类似于HashMap, 但是ThreadLocalMap是通过再哈希的方式
来解决哈希冲突, 而HashMap是通过链表或者红黑树来解决冲突情况的, 两者计算元素索引位置的方式是一样的,
都是通过与运算来实现( hashCode & (table.length - 1) ), 即与Entry数组的长度 - 1进行&运算, 但是
这个长度必须是2的幂, 大家可以自己探讨下为啥会这样吼, 这里就不进行进一步的扩展了, 本次对ThreadLocal
及Java的四大引用类型的分析就到此为止了=,=
```
