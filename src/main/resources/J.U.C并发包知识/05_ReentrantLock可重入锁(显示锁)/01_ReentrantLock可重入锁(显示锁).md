## 引入
```
ReentrantLock可重入锁是一个显示锁, 而我们在基础知识实现的显示锁就跟这个锁是类似的, ReentrantLock
继承于Lock接口, 其lock和unlock方法起到的作用是跟synchronized关键字是一样的, 所以其弊端也是一样的,
那就是一旦在lock锁定的临界区中执行了耗时的代码的话, 那么其它因为抢夺锁失败的线程就会进入阻塞状态,
并且在抢夺到锁之前无法退出该状态, 为了解决这个问题, Lock接口还提供了一个lockInterruptibly的方法来
加锁, 该方法加的锁可以通过产生中断的方式来使得等待的线程退出等待状态, 下面主要描述一下Lock接口提供
的几个方法, 对于ReentrantLock内部自带的一些监视的方法(如hasQueuedThreads判断是否有等待的线程)就
不进行描述了, 一般这些方法是用于系统监控状态的, 即通过这些方法获取的信息有可能存在不准确性, 因为线程
一直在运行
```

## lock && unlock && synchronized
```java
public static void main (String[] args) {
  // 获取一个显示锁
  Lock lock = new ReentrantLock();
  // 开启两个线程
  for ( int i = 0; i < 2; i ++ ) {
    new Thread( () -> {
      try {
        lock.lock();
        System.out.println( Thread.currentThread().getName() + "获取到锁" );
        while ( true );
      } finally {
        lock.unlock();
        System.out.println( Thread.currentThread().getName() + "释放锁" );
      }
    } ).start();
  }
}

// 上述代码执行的结果是一个线程一直处于死循环的状态, 并且霸占了该锁, 另一个线程会一直处于等待状态,
// 这就是跟下面的synchronized关键字是一样的效果的, 同时也显示了synchronized关键字的弊端, 等待的
// 线程无法被中断

public static void main (String[] args) {
  for ( int i = 0; i < 2; i ++ ) {
    new Thread( () -> {

      synchronized (TestClass2.class) {
        System.out.println( Thread.currentThread().getName() + "获取到锁" );
        while ( true );
      }

    } ).start();
  }
}
```

## lockInterruptibly可中断锁
```java
public static void main (String[] args) throws InterruptedException {
  // 获取一个显示锁
  Lock lock = new ReentrantLock();

  Thread t1 = new Thread( () -> {
    try {
      // 加锁, 可中断锁
      lock.lockInterruptibly();
      System.out.println( Thread.currentThread().getName() + ": 获取到锁" );
      // 使得当前线程一直在执行, 这里没有用sleep方法的原因是为了防止interrupt的时候混淆
      while ( true );
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      lock.unlock();
    }
  } );

  // 跟上面的代码一模一样
  Thread t2 = new Thread( () -> {
    try {
      lock.lockInterruptibly();
      System.out.println( Thread.currentThread().getName() + ": 获取到锁" );
      while ( true );
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      lock.unlock();
    }
  } );

  // 先启动第一个线程
  t1.start();
  // 通过睡眠的方式使得第二个线程晚于第一个线程启动, 这样第二个线程就会因为抢夺锁失败而阻塞
  Thread.sleep( 1000 );
  t2.start();

  // 休眠一会, 确保第二个线程完全启动, 然后中断第二个线程的等待状态
  Thread.sleep( 3000 );
  t2.interrupt();
}

结果分析: 会先输出"Thread-0: 获取到锁", 然后输出一个InterruptedException异常信息, 因为第二个线程
        被中断了, 然后还会输出一个IllegalMonitorStateException异常, 因为在线程的finally语句中
        调用了unlock方法, 而第二个线程并不是锁的拥有者
```

## tryLock && tryLock(long, TimeUnit)
```
tryLock和tryLock(long, TimeUnit)都是用于尝试获取锁, 对于第一个方法来说, 如果尝试获取锁失败了, 该
线程不会进入等待状态, 而是执行tryLock方法后面的代码, 而tryLock(long, TimeUnit)方法虽然也是尝试获
取锁, 如果在规定的时间内获取到了锁则该线程会持有该锁继续执行, 如果在规定的时间内没有获取到锁, 仍然
也是继续执行, 同时该方法还会提供一个中断操作, 即提供catch语句, 在规定的时间内如果收到了其它线程调用
该线程的interrupt方法, 则该线程会跳出等待, 进入catch语句
```






