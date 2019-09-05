## 引入
```
CyclicBarrier别名叫同步屏障, 跟CountDownLatch类似, 我们先来回顾以下CountDownLatch的场景, 维护一
个数值, 每个线程执行完毕调用其countDown()方法使得数值减一, 主线程中调用CountDownLatch的await方法,
在数值不为0的情况下会一直等待, 其实就是一个线程A在等待其它线程执行完毕, 线程A才会继续执行, 而对于
CyclicBarrire来说, 可以认为是多个线程在互相等待对方是否执行完毕, 只有所有线程都执行完毕后这些线程
才会执行下一步的操作, 举个例子, 把学生宿营为例, 学生就是一个个的线程, 当出发的时候, 必须全部学生都
到齐了才能一起出发, 调用CyclicBarrier的await()方法就会使得一个线程进入等待, 只有规定个数的线程都
调用了该方法后, 这些线程才会同时退出等待状态, 下面以任务执行为例, 三个任务同时执行, 有的执行时间长
有的短, 要求所有线程执行到一个点的时候都进入等待, 只有所有的线程都到达了对应的点才能继续执行下去
```

## 代码
```java
public static void main (String[] args) throws InterruptedException {
  // 可以增加一个Runnable接口的回调, 当所有线程都到达规定的点, 即所有线程都执行了
  // cyclicBarrier.await()方法的时候就会调用这个方法
  CyclicBarrier cyclicBarrier = new CyclicBarrier( 3, () -> {
    System.out.println( "所有线程均执行完毕..............." );
  } );

  // 依次创建线程, 并启动线程, 为了更好的理解把捕获异常的省略了
  Thread t1 = new Thread(){
    @Override
    public void run () {
      // 利用sleep来模拟线程在执行任务
      Thread.sleep( 3000 );
      System.out.println( "线程一执行完毕, 到达目的地" );
      // 要求所有线程从这里点统一开始执行
      cyclicBarrier.await();
      System.out.println( "线程一继续执行" );
    }
  };

  Thread t2 = new Thread(){
    @Override
    public void run () {
      // 利用sleep来模拟线程在执行任务
      Thread.sleep( 3000 );
      System.out.println( "线程二执行完毕, 到达目的地" );
      // 要求所有线程从这里点统一开始执行
      cyclicBarrier.await();
      System.out.println( "线程二继续执行" );
    }
  };

  Thread t3 = new Thread(){
    @Override
    public void run () {
      // 利用sleep来模拟线程在执行任务
      Thread.sleep( 3000 );
      System.out.println( "线程三执行完毕, 到达目的地" );
      // 要求所有线程从这里点统一开始执行
      cyclicBarrier.await();
      System.out.println( "线程三继续执行" );

    }
  };

  t1.start();
  t2.start();
  t3.start();
}

输出的结果:
  线程一执行完毕, 到达目的地
  线程二执行完毕, 到达目的地
  线程三执行完毕, 到达目的地
  所有线程均到达目的地...............
  线程三继续执行
  线程二继续执行
  线程一继续执行
分析:
  其实上面的执行流程也可以用CountDownLatch来模拟, 那就是在目标点先调用CountDownLatch的countDown
  方法, 然后再调用await方法, 这样就会使得所有线程执行到目标点的时候将值减一, 并且还会进入等待状态,
  只有所有线程都执行到目标点后将值减为0后才继续执行下一步
```

## 需要注意的地方
```
<1> 构造方法中可以提供一个Runnable接口的回调, 在规定个数的线程执行到await方法后就会执行该方法, 前
    提是期间await方法没有被打断
<2> 如果调用await(long time, TimeUnit tu)方法, 那么在规定的时间后, 该线程就会退出等待状态, 继续
    执行, 并且抛出TimeoutException异常, 如果此时还有线程没有到达目的地, 那么后续到达的线程就不再
    会进入阻塞状态, 因为已经有线程退出了阻塞了, 并且抛出BrokenBarrierException异常, 即屏障被打破
    异常
<3> reset方法会使得该对象恢复到初始状态, 即初始的时候的数值是多少, 之后仍然需要多少个线程进入等待
    才能执行下一步
```
