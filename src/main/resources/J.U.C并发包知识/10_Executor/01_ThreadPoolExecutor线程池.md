## ThreadPoolExecutor参数描述
```
corePoolSize: 可以认为是最少个数的线程
maximumPoolSize: 最大的线程个数
keepAliveTime: 对于超过了corePoolSize大小的那部分线程, 如果其闲置时间查过了这个keepAliveTime, 则
                该线程会被销毁掉
TimeUnit: 修饰keepAliveTime的时间基数, 即用于表示keepAliveTime这个值代表的是时分秒
BlockingQueue: 阻塞队列, 当超过了线程个数的任务进来的时候, 会被放入到阻塞队列中, 当阻塞队列中的任
               务满的时候仍然有任务进来, 则会触发拒绝策略
RejectedExecutionHandler: 拒绝策略, 对于超过指定个数的任务将会引发这个拒绝策略, 对于ThreadPoolExecutor
                          提供的拒绝策略是抛出一个异常, 我们也可以自定义拒绝策略
ThreadFactory: 表示如何创建线程, 可以通过实现ThreadFactory(函数式接口)的newThread方法来自定义我
              们创建一个线程的过程, 可以在该方法中定义自己想要创建的线程的名字, 线程是否是守护线程
              线程的优先级, 线程组等特性, 当我们重写newThread方法的时候, 线程池就会通过我们重写的
              方法来创建线程了
```

## 扩容策略
```
当阻塞队列满的时候, 则会对corePoolSize进行增大, 直到增大到最大的线程个数, 如果已经增大到了最大的
线程个数后还有新的线程进来, 那么就会触发拒绝策略, 举个例子, 如果corePoolSize为1, maximumPoolSize
为3, 阻塞队列的长度为5, 如果此时提交了7个任务, 首先我们在提交第一个任务时, 因为corePoolSize足够,
所以第一个任务不会进入阻塞队列, 而从第二个到第六个任务, 一共5个任务, 此时会被依次放入阻塞队列, 到
了第七个任务的时候, 由于阻塞队列已经满了, 而此时线程池中的线程个数仍然为1个, 所以动态扩容到2个,
此时会从阻塞队列中取出一个任务执行, 并将第七个任务就可以被放入阻塞队列, 总的来说, 就是当阻塞队列
满的时候, 新来几个任务就扩容几个线程, 直到扩容到最大的线程个数, 如果线程个数达到了最大, 并且阻塞
队列满了的话, 此时新的的任务就会触发拒绝策略
```

## 缩容策略
```
当阻塞队列为空时, 对于超过了corePoolSize大小的那部分线程,如果其闲置时间查过了这个keepAliveTime,
则该线程会被销毁掉, 直到缩小到corePoolSize的大小
```

## shutdown方法
```
调用线程池的该方法的时候, 线程池会对所有的任务发出一个停止信号, 但是只有等正在执行的任务及阻塞队列
中等待执行的任务完全执行完后才会关闭线程池, 并且该方法是一个异步的方法, 当其配合着awaitTermination
方法一起使用时, 在awaitTermination方法中传入一个时间, 如果在该时间内所有的线程都执行完毕(包括
阻塞的线程), awaitTermination方法后面的代码会进行执行, 而如果在该规定的时间内这些线程都没有执行
完毕, 则awaitTermination方法后面的代码也会进行执行, 换句话说, awaitTermination方法最多能等待其
传入的时间
```
## shutdownNow方法
```
调用线程池的该方法的时候, 线程池会对所有的任务发出一个停止信号, 对于正在执行的线程任务, 当执行完
毕后不会再从阻塞队列中取任务, 而阻塞队列中的任务会被排干并作为返回结果返回, 总的来说, 就是等待线程
池将当前线程正在执行的任务执行完毕, 并将阻塞队列中的任务返回(即不执行), 最后才会退出线程池
```

## shutdown VS shutdownNow
```
对于shutdown和shutdownNow方法来说, 都不会直接把任务终止, 两者相同之处在于都会等待线程正在执行的任
务执行完毕, 而对于阻塞队列中的任务的处理却不同, shutdown则会将阻塞队列中的也执行完毕后才关闭线程池,
而shutdownNow则会将阻塞队列中任务返回并不再执行, 如果要实现强制停止所有任务的执行, 可以通过将线程
设置为守护线程的方式来达到效果
```

## 七参数创建线程池
```java
// 定义一个原子整型变量, 用于定义第几个线程的创建
final AtomicInteger count = new AtomicInteger( 1 );
// 最小的线程个数
int corePoolSize = 1;
// 最大的线程个数
int maximumPoolSize = 3;
// 对于多余corePoolSize个数的线程, 其最大的空闲时间为60
long keepAliveTime = 60L;
// 描述上面的60基数是秒
TimeUnit unit = TimeUnit.SECONDS;
// 阻塞队列
BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<>();
// 工厂方式创建线程的过程(规定每个线程的名字以Thread...开头,数值1-n结尾)
ThreadFactory threadFactory = (runnable) -> {
  // 线程名字的前缀
  String namePrefix = "Thread...";
  // 通过提供的Runnable接口创建一个线程
  Thread t = new Thread( runnable, namePrefix + count.getAndIncrement() );
  // 还可以将线程设置为守护线程( t.setDaemon( true ) )
  // 将创建的线程返回
  return t;
};
// 拒绝策略, 采用线程池类自带的拒绝策略, 也可以自定义(同ThreadFactory一样是函数式接口)
RejectedExecutionHa ndler handler = new ThreadPoolExecutor.AbortPolicy();

// 创建一个线程池
ThreadPoolExecutor pool = new ThreadPoolExecutor( corePoolSize, maximumPoolSize,
																	keepAliveTime, unit, workQueue, threadFactory, handler );
```

## 通过Executors工厂类创建线程池
```java
newCachedThreadPool:
  // 创建一个线程池(还有一个构造方式是传入一个ThreadFactory)
  ThreadPoolExecutor pool = ( ThreadPoolExecutor )Executors.newCachedThreadPool();
  // newCachedThreadPool()方法的源码
  new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS,
                                      new SynchronousQueue<Runnable>());
分析:
  对于SynchronousQueue阻塞队列来说, 其特点是队列中只能放一个线程任务, 只有该线程任务被线程取走了
  才能放入下一个线程任务, 根据源码可以看到, 其起始的线程个数是0个的, 而最大能达到整型的最大值, 一个
  线程如果在60内是闲置的, 那么就会从线程池中移除, 对于这个方式创建的线程, 一般用于执行短暂的异步任
  务, 并且由于线程池的corePoolSize为0, 所以当里面的线程执行完毕后, 并且都是空闲的情况下, 则由于缩
  容策略的存在, 缩容到0的时候就会使得线程池自动销毁, 所以通过该方式创建的线程一般用于执行大量的生命
  周期短的任务

newFixedThreadPool:
  // 创建一个固定10个线程的线程池(还有一个构造方式是传入一个线程的个数和ThreadFactory)
  ThreadPoolExecutor pool = ( ThreadPoolExecutor )Executors.newFixedThreadPool( 10 );
  // newFixedThreadPool()方法的源码
  new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS,
                            new LinkedBlockingQueue<Runnable>());
分析:
  corePoolSize和maximumPoolSize均为我们传入的线程的大小, 所以该方法不会扩容和缩容, 并且设置了
  keepAliveTime为0毫秒, 这个时间是没有意义的, 因为并不会扩容和缩容, 然后传入了一个链表类型的阻塞
  队列

newSingleThreadExecutor:
  // 创建一个固定大小为1的线程池(还有一个构造方式是传入一个ThreadFactory)
  ThreadPoolExecutor pool = ( ThreadPoolE xecutor )Executors.newSingleThreadExecutor();
  // newSingleThreadExecutor()方法的源码
  public static ExecutorService newSingleThreadExecutor() {
        return new FinalizableDelegatedExecutorService
            (new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
                                    new LinkedBlockingQueue<Runnable>()));
  }

分析:
  其实就是等价于Executors.newFixedThreadPool( 1 ), 并且将创建出来的线程池用一个代理类包装了一下
  而已FinalizableDelegatedExecutorService, 对于这个单个线程的线程池, 其和直接创建一个线程不同的
  是, 这种方法创建的线程可以进行复用, 而单个线程执行完任务后就直接关闭了
```

## ThreadPoolExecutor自带的四大拒绝策略
```
拒绝策略触发情况: 当poolSize线程池的大小达到了maximumPoolSize的时候, 并且此时阻塞队列已经满的时候
                就会触发该策略
ThreadPoolExecutor.AbortPolicy: 终止策略, 如果触发了该策略, 则会抛出一个拒绝异常, 那么调用execute
                                或者submit方法的线程就会被终止了
ThreadPoolExecutor.DiscardPolicy: 忽视策略, 如果出发了该策略, 那么新增的任务就会被忽视掉
ThreadPoolExecutor.CallerRunsPolicy: run策略, 如果主线程中调用了execute或者submit任务, 并且触发
                                     了该拒绝策略的话, 就会在主线程中调用该任务的run方法使得其
                                     在主线程中执行
ThreadPoolExecutor.DiscardOldestPolicy: 忽视最老任务策略, 触发该策略的时候, 会调用阻塞队列的poll
                                        方法将队头的任务移除, 然后将当前任务添加到队尾
```

## ThreadPoolExecutor之API解析
```java
protected void beforeExecute (Thread t, Runnable r),
protected void afterExecute (Runnable r, Throwable t):
  这两个方法用于在任务启动之前被执行, 子类可以复写这两个方法, 两个方法的参数不同, 对于后者来说, 如
  果线程中抛出了异常, 那么就可以通过afterExecute方法来进行异常的处理, 因为在方法中传入了异常对象,
  如果这个异常对象为空的话则表示没有异常了
protected void terminated(): 在线程池被终止的时候调用该方法, 子类可以重写该方法

allowCoreThreadTimeOut(bool): 传入true则表示对于core线程来说, 如果空闲时间超过了keepAliveTime
                              的话同样也会被撤销, 从而可以导致线程池终止
allowsCoreThreadTimeOut(): 如果返回true则标明core线程的空闲时间超过了keepAliveTime则会被撤销

prestartAllCoreThreads(): 预先启动所有的core线程, 使得它们进入等待状态等待接收任务
prestartCoreThread(): 预先启动一个core线程, 使得它们进入等待状态等待接收任务, 可以启动多个直到coreSize

remove​(Runnable task): 将该任务从blockingQueue中移除

getActiveCount(): 获取正在执行任务的线程, 有些线程可能启动了, 但是却没有执行任务, 那么这个count就
                  不会加上那个线程
getCompletedTaskCount(): 获取完成的任务个数
getCorePoolSize(): 获取Core线程的个数
getKeepAliveTime​(TimeUnit unit): 获取keepAliveTime的值
getLargestPoolSize(): 获取线程池中此时最大的线程个数, 不是maximumPoolSize的值, 这个值是当前情况
                      下最大的线程个数, 可能会改变
getMaximumPoolSize(): 获取可以达到的最大的线程个数, 即参数中的maximumPoolSize的值
getPoolSize(): 获取此时线程池的线程个数
getQueue(): 获取该线程池的阻塞队列(可以直接往阻塞队列中添加任务来使得该任务执行, 前提是有活跃的线程)
getRejectedExecutionHandler(): 获取该线程池的拒绝策略
getThreadFactory(): 获取创建线程的工厂对象
isShutdown(): 线程池是否调用了shutdown方法进行终止
isTerminated(): 线程池是否已经终止
isTerminating(): 线程池是否在终止的过程中
```

