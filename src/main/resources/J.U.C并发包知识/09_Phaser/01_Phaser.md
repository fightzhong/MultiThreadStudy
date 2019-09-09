## Phaser之API
```java
Phaser跟CountDownLatch以及CyclicBarrier类似, 并且Phaser可以实现它们的功能, 不同的是Phaser可以
动态的改变parties的个数, 通过注册register和取消注册unregister的方式, 并且Phaser是可以重用的, 即
等待的Phaser达到了指定时, 线程会继续往前推进, 之后再重新计算到达的phaser的时候, 又会从0个开始增加

Constructor:
  Phaser(): 创建一个Phaser对象, 此时其里面的parties为0个
  Phaser(int): 创建指定个数parties的Phaser对象

Method:
  register(): 注册一个parties
  bulkRegister(int): 批量的注册parties, 即可以根据传入的int值来注册对应个数的parties

  arriveAndAwaitAdvance(): 到达并等待前进, 当线程调用该方法的时候, 会使得parties加一, 当到达了注
                           册的parties个数时这些线程才会同时往前执行
  arriveAndDeregister(): 到达并撤销一个parties, 此方法不会等待, 即当线程执行了这个方法后, 会使得
                         parties的个数减一, 同时该线程会继续执行, 跟arrive方法效果类似, 只不过
                         arrive方法不会撤销parties
  arrive(): 表示当前线程已经到达了, 会增加一个parties, 并且不会进行等待, 该线程会继续执行
  awaitAdvance(int phase): 如果当前phaser对象的phase即所处的阶段跟传入方法的phase相同的话, 那么
                           调用该方法的线程就会进入阻塞状态, 直到不相等的时候才会退出阻塞
  awaitAdvanceInterruptibly(int phase): 跟上一个方法类似, 不同的是上一个方法当phaser对象的阶段
                                        phase和传入的值相等的时候会一直等待, 不能被唤醒, 而这个
                                        方法则提供了异常捕获机制, 可以由其它线程调用当前线程的
                                        interrupt方法来触发中断
  awaitAdvanceInterruptibly(int phase, long timeout, TimeUnit unit): 提供超时机制和中断机制

  protected boolean onAdvance：该方法在正常执行是返回false, 当其返回false的时候, isTerminated
                              方法会返回false, 即未终止状态, 当调用了arriveAndDeregister方法
                              来使得注册的parties减少到0的时候, isTerminated方法会返回true, 我
                              们可以通过重写onAdvance方法, 使其一直返回false, 这样isTerminated
                              方法就不会返回true了, 即不是终止状态

  getArrivedParties(): 获取已经到达的parties个数
  getUnarrivedParties(): 获取还未到达的parties个数
  getRegisteredParties(): 获取已经注册的parties个数
  getPhase(): 获取当前是第几阶段, 通过调用arriveAndAwaitAdvance这样的方法使得多个线程到达并等待
               前进时, 当等待结束后, 即指定个数的线程到达后就会使得阶段加1
  isTerminated(): 表示是否是终止状态, 如果是终止状态, 那么parties = 0 arrived = 0, 则下一次如果
                  还继续调用arriveAndAwaitAdvance方法则会报错了
  forceTermination(): 终止Phaser, 但是调用该方法时如果还有处于arriveAndAwaitAdvance而等待的线程,
                      那么它们还是生效的, 只是下一阶段就不会生效了
```

## getPhase方法演示
```java
// 这里我们仅仅使用一个main线程来模拟, 调用arriveAndAwaitAdvance方法表示到达并等待其它线程到达后
// 才往前运行, 由于我们创建phaser的时候只指定了一个parties, 所以当调用arriveAndAwaitAdvance方法
// 时即可继续进行下去
public static void main (String[] args) {
  final Phaser phaser = new Phaser( 1 );
  System.out.println( phaser.getPhase() );
  phaser.arriveAndAwaitAdvance();

  System.out.println( phaser.getPhase() );
  phaser.arriveAndAwaitAdvance();

  System.out.println( phaser.getPhase() );
  phaser.arriveAndAwaitAdvance();
}

结果:依次输出0, 1, 2三个数字, 说明当满足条件的线程进入了到达并等待状态后, 此时它们才可以继续往前推
     进, 同时会使得getPhase的值加一, 即表示又增加了一个阶段
```

## awaitAdvance方法演示
```java
awaitAdvance(int phase): 如果当前phaser对象的phase即所处的阶段跟传入方法的phase相同的话, 那么调
                         用该方法的线程就会进入阻塞状态, 直到不相等的时候才会退出阻塞, 并且该方法
                         不会造成parties值的增加
// 要求四个线程在执行任务, 当这些任务在执行时主线程对任务的结果进行整合后主线程才能继续往下执行
// 下面我们利用该方法来模拟这种情况(其实通过arriveAndAwaitAdvance是更方便的, 不过这里主要是为了
// 演示方法的作用)
public static void main (String[] args) {
  final Phaser phaser = new Phaser( 4 );
  // 创建四个线程去执行任务
  for ( int i = 0; i < 4; i ++ ) {
    new Thread( () -> {
      System.out.println( Thread.currentThread().getName() + ": 开始执行任务" );
      // 利用sleep来模拟执行任务需要一点时间
      try {
        TimeUnit.SECONDS.sleep( 3 );
      } catch (InterruptedException e) {}
      System.out.println( Thread.currentThread().getName() + ": 执行任务结束" );
      phaser.arriveAndAwaitAdvance();
    } ).start();
  }

  // 在主线程执行这行代码的时候, 由于还没有通过phaser.arriveAndAwaitAdvance();的方式
  // 使得parties增加到创建phaser对象时的4个, 所以此时getPhase方法获得的是0, 即第0阶段
  // 由于当前phaser对象所处的正好是第0阶段, 所以主线程会在这里进入阻塞, 直到因为当前线程
  // 进入下一个阶段后即1, 由于1和传入的0不相等, 主线程才会退出等待状态
  phaser.awaitAdvance( phaser.getPhase() );
  System.out.println( "四个线程的任务都执行完毕, 主线程对任务结果进行整合..." );
}
```
