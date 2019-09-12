## ExecutorService
```java
T invokeAny(Collection<Callable<T>>): 传入一个Callable集合, 利用线程池依次从集合中取走指定个数
                                      的Callable任务, 如果线程池中线程个数是多个, 那么同一时间
                                      就会有多个Callable任务同时执行, 那么这些任务中哪个任务先
                                      执行完毕就将哪个任务的结果返回, 但是其它任务还有在执行, 只
                                      不过是将首先完成的这个任务的结果给返回了而已, 该方法是一个
                                      阻塞的方法, 即调用了该方法后, 直到接收到了结果才能执行之
                                      后的代码
T invokeAny(Collection<Callable<T>>, long, TimeUnit): 该方法同上面的类似, 同样是阻塞的方法, 并
                                                      且线程池会依次执行该集合的线程任务, 如果
                                                      在规定的时间内还没有获取到一个结果, 则抛
                                                      出一个超时异常, 并且不会再获取结果, main
                                                      线程如果不对该异常进行捕获的话则会终止了
List<Future<T>> invokeAll(Collection<Callable>): 传入一个callable集合, 会调用所有的callable任
                                                 务, 并且将所有的结果封装程一个Future对象放入
                                                 集合中, 是一个阻塞的方法, 在所有的任务的结果
                                                 没有获取到之前会一直阻塞住
List<Future<T>> invokeAll(Collection<Callable>, long, TimeUnit): 同上面的类似, 不过增加了超时
                                                                 机制, 如果指定的时间内没有获
                                                                 取到所有结果则不会不能再获取
                                                                 结果
Future<T> submit(Callable<T> callable),
Future<?> submit(Runnable<T> runnable),
Future<T> submit(Runnable<T> runnable, T t):
  submit方法如果传入Callable任务则可以有返回值, 如果传入runnable接口则没有返回值, 对于第三个submit
  重载方法来说, 在调用的时候自己定义返回值, 则调用结束后将该值封装程Future对象返回
```

## invokeAny
```java
// 一个自定义的休眠, 只是对异常进行了捕获而已, 并且不输出异常信息
public static void sleeping (int rangeSeconds) {
  try {
    TimeUnit.SECONDS.sleep( rangeSeconds );
    System.out.println( "sleeping finished..." );
  } catch (InterruptedException e) {}
}

public static void main (String[] args) throws InterruptedException, ExecutionException {
  // 定义一个固定线程大小为1的线程池
  ExecutorService pool = Executors.newFixedThreadPool( 2 );
  // 定义一个Callable集合
  Collection<Callable<Integer>> callbales = new ArrayList<>();
  // 往集合中添加了一个执行10秒的任务, 并且返回值为1
  callbales.add( () -> {
    sleeping( 10 );
    return 1;
  } );

  // 往集合中添加了一个一直执行的任务
  callbales.add( () -> {
    while ( true ) {
      sleeping( 3 );
      System.out.println( ".................." );
    }
  } );

  // 调用invokeAny
  Integer result = pool.invokeAny( callbales );
  System.out.println( result ); // 获得结果为1
}

结果分析: 通过上面的代码我们可以看到, 添加了一个10秒的任务以及一个死循环的任务, 我们的线程大小为2个,
          那么这两个任务就会同时执行, 由于第一个任务在10秒后结束, 所以我们的invokeAny方法就只会阻
          塞10秒钟, 然后获取结果继续执行后面的代码, 而由于死循环的任务不会停止, 所以其会占据线程池
          中的一个线程一直执行
```
