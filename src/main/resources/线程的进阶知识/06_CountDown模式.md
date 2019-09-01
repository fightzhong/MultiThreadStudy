## CountDown模式
```java
假设有这么几种场景, 一共有A, B, C, D, Main线程共5个, 要求Main线程必须在A-D四个线程都执行完毕后才
能执行, 那么我们首先想到的就是在Main线程中把A-D四个线程join进去, 这样四个线程如果没有执行完毕则Main
线程就不会执行了, 如下:

public static void main (String[] args) throws InterruptedException {
  // 将四个线程均放入list中, 在线程都被启动后通过遍历的方式使得这些线程join进主线程
  ArrayList<Thread> threads = new ArrayList<>();
  for ( int i = 0; i < 4; i ++ ) {
    Thread t = new Thread( () -> {
      System.out.println( Thread.currentThread().getName() + "开始执行任务" );

      // 模拟线程在执行
      try {
        Thread.sleep( 2000 );
      } catch (InterruptedException e) {}

      System.out.println( Thread.currentThread().getName() + "执行任务完毕" );

    }, "Thread-" + i );
    threads.add( t );
    t.start();
  }

  for ( Thread t: threads )
    t.join();

  System.out.println( "线程任务执行完毕................................" );
  System.out.println( "主线程退出阻塞" );
}

利用CountDown模式, 在CountDown对象中维护一个线程的个数, 每个线程执行完毕后就使得个数减一, 主线程
在执行的时候就会在个数大于0时进入等待, 改造后的代码如下:

public static void main (String[] args) throws InterruptedException {
  CountDownLatch countDown = new CountDownLatch( 4 );

  for ( int i = 0; i < 4; i ++ ) {
    new Thread( () -> {
      System.out.println( Thread.currentThread().getName() + "开始执行任务" );

      try {
        Thread.sleep( 2000 );
      } catch (InterruptedException e) {}

      System.out.println( Thread.currentThread().getName() + "执行任务完毕" );

      countDown.countDown();
    }, "Thread-" + i ).start();
  }

  // 在里面的计数器大于0时会进入等待
  countDown.await();

  System.out.println( "线程任务执行完毕................................" );
  System.out.println( "主线程退出阻塞" );
}
```

## 实现一个简易的CountDown
```java
public class MyCountDown {
	private int count;

	// 用户传进来一个count计数器
	public MyCountDown (int count) {
		this.count = count;
	}

  // 每次count减减的时候必须是同步的, 同时减减后需要唤醒等待的线程
  // (即调用await方法后发现count大于0而进入等待的线程)
	public synchronized void countDown () {
		count --;
		this.notifyAll();
	}

	public synchronized void await () {
		while ( count > 0 ) {
			try {
				this.wait();
			} catch (InterruptedException e) {}
		}
	}
}
```

















