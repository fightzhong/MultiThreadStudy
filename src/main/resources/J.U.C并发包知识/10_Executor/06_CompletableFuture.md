## 引入
```
在Future的使用中, 我们需要通过get方法来主动的获取结果, 如果任务还没执行完毕就会进入阻塞状态, 通过
这种主动获取结果并对结果进行处理的方式是比较麻烦的, 如果对其进行优化, 使得在任务结束时自动的进行结
果的处理, 即采用回调的方式, 除了提供一个任务Runnable之外还提供一个回调函数, 使得在任务执行结束时自
动的调用回调函数, 这样就更加的方便了, 而CompleableFuture即提供了该功能

并且, 在使用Future的过程中, 我们一次提交多个任务的时候, 无法得知哪个任务会优先执行完毕, 假设存在这
么一种情况, 我们需要同时查询多次数据库, 将查询的结果进行展示输出, 如果采用Future模式, 我们将任务提
交上去之后, 由线程池进行执行, 那么执行的结果即查询数据库得到的数据放入了Future对象中, 并将这个对象
放入ArrayList中, 然后通过循环遍历的方式一个个的获取结果即get方法并对结果进行处理, 那么一旦我们第一
个处理的Future对象是所有查询中最慢的, 调用get方法如果还没查询完毕, 那么就会使得其它已经查询完毕的
Future对象没有及时得到处理, 这也是Future模式的一个问题, 但是该问题可以通过CompletionService来进
行解决, 对于CompletionService来说, 其内部维护了一个completionQueue, 即完成队列, 先完成的任务会先
进入该队列, 通过take方法来获得依次完成的任务, 但是还是上面的那个问题, 如果没有完成的任务则take方法
会进入阻塞
```

## 入门案例[CompletableFuture回调解决Future主动获取结果的问题]
```java
// 利用sleep来模拟执行任务需要一段时间, 这里对sleep进行了一下简单的封装, 为了防止多次调用需要try
// catch而造成查看代码较为混乱
public static void sleep (int seconds) {
  try {
    TimeUnit.SECONDS.sleep( seconds );
  } catch (InterruptedException e) {
    e.printStackTrace();
  }
}

public static void main (String[] args) throws InterruptedException {
  // 利用CompletableFuture的静态方法来构造一个CompletableFuture对象
  // suppleAsync是带有返回值的任务执行, 之后会对这些方法进行介绍
  CompletableFuture.supplyAsync( () -> {
    // 查询任务, 利用sleep来模拟查询任务需要5秒钟
    System.out.println( "start the query" );
    sleep( 5 );
    System.out.println( "finish the query" );
    // 查询的结果
    return "hahaha";
  } ).whenComplete( ( v, t ) -> {
    // suppleAsync结果是返回一个CompletableFuture对象, 利用whenComplete方法来使得上一个任务执行
    // 完毕后会自动调用回调, 即下面的这个任务, 对查询的结果进行展示
    System.out.println( "display the query-" + v );
    sleep( 5 );
    System.out.println( "finish display the query-" + v );
  } );

  // 主线程进行自己任务的执行, 不会因为上面的代码而陷入阻塞
  System.out.println( "main thread is running ...." );
  // CompletableFuture执行任务利用的是守护线程, 如果主线程终止了会造成CompletableFuture任务的
  // 终止, 所以我们通过join的方式让主线程一直执行
  Thread.currentThread().join();
}

结果:
  start the query
  main thread is running ....
  finish the query
  display the query-hahaha
  finish display the query-hahaha

结果分析:
  我们发现当调用了查询之后, 主线程仍然在继续执行, 而查询结束后会自动执行对结果进行处理的代码, 即回调
```

## 获取CompletableFuture对象的方式
```java
通过CompletableFuture的静态方法来构建CompletableFuture对象

CompletableFuture.supplyAsync(Supple<?> supple),
CompletableFuture.supplyAsync(Supple<?> supple, Executor executor):
  supple中即真正的任务, 执行该任务需要得到一个返回值, 类似于Callable接口, suppleAsync即表示异步的
  任务, 该静态方法返回一个CompletableFuture, 之后可以利用该CompletableFuture对象的异步回调来对任
  务的结果进行处理, 重载方法中可以自己指定一个线程池, 使得传入的任务能够通过自己定义的线程池来进行
  执行, 否则使用ForkJoinPool.commonPool()线程池执行

CompletableFuture.runAsync(Runnable runnable),
CompletableFuture.runAsync(Runnable runnable, Executor executor):
  提供一个Runnable任务, 同上面的方法不同的是, 该方式是没有返回值的, 即可以理解为上一个方式是Callable
  的实现, 而本次这个是Runnable的实现, 该静态方法返回一个CompletableFuture对象, 之后可以利用该对象
  的异步回调来对任务的结果进行处理, 重载方法中可以自己指定一个线程池, 使得传入的任务能够通过自己定
  义的线程池来进行执行, 否则使用ForkJoinPool.commonPool()线程池执行

CompletableFuture.completedFuture(U value):
  创建一个CompletableFuture, 并且可以认为是已经执行了一个任务得到了一个value结果, 利用文档上的描
  述: "Returns a new CompletableFuture that is already completed with the given value."

CompletableFuture.allOf( CompletableFuture<?>... cfs ),
CompletableFuture.anyOf(CompletableFuture<?>... cfs):
  执行多个CompletableFuture, allOf则是执行所有的, 执行完毕后返回一个Void类型的CompletableFuture,
  而anyOf则是执行所有的, 但是第一个执行完毕的则返回对应类型的CompletableFuture
```

## 转换(转换到任意非Void类型的CompleteFuture)
```java
先总结一下:
  <1> whenComplete采用的是BuConsumer接口, 函数式接口为void accept(T v, U t), 即接收一个结果v,
      以及一个异常类t, 没有返回值, 获得跟调用该方法的CompleteFuture一样的对象
  <2> thenApply采用的Function接口, 函数式接口为 U apply(T v), 即接收一个结果v, 返回一个其它类型
      的结果, 所以调用thenApply方法的CompleteFuture对象类型和该方法返回的CompleteFuture的类型可
      以有所不同
  <3> 对于thenApple来说, 没有提供异常类对象, 即如果supplyAsync提交的异步任务出现了异常, 那么就没
      法对异常进行处理, 而handle方法就是为了解决这个问题, 采用BiFunction接口, 其函数式接口中方法
      为R apply(T v, U t), 即接收一个结果v,以及一个异常类t, 返回一个其它类型的结果

whenComplete(BiConsumer action),
whenCompleteAsync(BiConsumer action),
whenCompleteAsync(BiConsumer action, Executor executor):
    当异步任务完成得时候执行该action内的回调, 带有Async的是异步回调, 不带的是同步回调, 同步回调的
    意思是说, 在定义这个回调方法的代码前, 如果supplyAsync或者runAsync已经执行完毕了, 那么就直接在
    当前线程中执行该回调代码, 举个例子:
    // 定义一个CompletableFuture执行异步任务
    CompletableFuture<Void> future = CompletableFuture.runAsync( () -> {
			System.out.println( "a" );
		} );

    // 假设上面的异步任务在执行到这一行的时候已经执行完毕了, 那么下面这个回调就会在main线程中直接
    // 执行, 如果还没有执行完毕, 则会在上面异步任务的线程中进行该回调的执行
		future.whenComplete((v, t) -> {
			System.out.println("async....start + " + Thread.currentThread().getName());
			sleep(5);
			System.out.println("async....end");
		});
    // 经过验证, 如果我们仅仅这样写的话, 可能会出现两种情况, 第一种是上面的runAsync在定义whenComplete
    // 前就已经执行完毕了, 那么whenComplete的代码就会在main线程中进行执行, 因为是同步的, 第二种
    // 情况就是上面的代码在异步线程中即ForkJoinPool.commonPool还没执行完毕, 那么whenComplete定义
    // 的异步回调就会在上面代码所在的异步线程中执行

thenApply(Function<? super T,? extends U>),
thenApplyAsync(Function<? super T,? extends U>),
thenApplyAsync(Function<? super T,? extends U>, Executor executor):
    同上面的whenComplete类似, 不过上面的whenComplete是无返回值的, 因为利用的是BiConsumer接口, 即
    传入两个参数进行消费, 而thenApply使用的是Function接口, 接收一个参数, 返回其它类型的结果, 即可
    以对结果进行更换, 所以可以使得调用supplyAsync获得的CompletableFuture对象和调用thenApply方法
    获得的CompletableFuture对象的类型不同, 下面举个例子:
    // 调用supplyAsync返回的是字符串结果类型的CompletableFuture
    CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
			return "a";
    });

    // 调用future.thenApply传入的是字符串类型的值, 返回的是整型的CompletableFuture
		CompletableFuture<Integer> future2 = future.thenApply((v) -> {
			return 1;
    });

handle(BiFunction<? super T, Throwable, ? extends U>),
handleAsync(BiFunction<? super T, Throwable, ? extends U>),
handleAsync(BiFunction<? super T, Throwable, ? extends U> fn, Executor executor):
    同thenApply类似, 在thenApply的基础上增加了一个throwable类型的参数, 即接收一个结果和一个异常
    类, 返回一个其它类型的结果, 举个例子:
    // 调用supplyAsync返回的是字符串结果类型的CompletableFuture
    CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "A");

    // 调用future.handle传入的是字符串类型的值和一个异常类t, 返回的是整型的CompletableFuture
		CompletableFuture<Integer> handleFuture = future.handle((v, t) -> {
			return 1;
		});
```

## 转换(转换到Void类型的CompleteFuture)
```java
thenAccept(Consumer<? super T> action),
thenAcceptAsync(Consumer<? super T> action),
thenAcceptAsync(Consumer<? super T> action, Executor executor):
  对传入的结果进行处理, 采用Consumer接口, 函数式接口为void accept(T t), 即接收一个接口, 举个例子:
  CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "a");
  CompletableFuture<Void> voidFuture = future.thenAccept((v) -> {
    System.out.println(v);
  });

thenRun(Runnable action),
thenRunAsync(Runnable action),
thenRunAsync(Runnable action, Executor executor):
  再执行一个异步任务, 即上一个异步任务执行完毕后再执行thenRun中的异步任务, 举个例子:
  // 执行一个异步任务
  CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
			System.out.println( "start ..." );
			sleep( 5 );
			System.out.println( "end ..." );
			return "aa";
    } );

  // 再上一个异步任务执行完毕后调用该回调执行第二个Runnable接口的异步任务
  CompletableFuture<Void> future2 = future.thenRun(() -> {
    System.out.println("start2 ...");
    sleep(5);
    System.out.println("end2 ...");
  });
```

## 组合
```java
runAfterBoth(CompletionStage<?> other, Runnable action),
runAfterBothAsync(CompletionStage<?> other, Runnable action),
runAfterBothAsync(CompletionStage<?> other, Runnable action, Executor executor):
  当两个CompletableFuture任务执行完毕之后会执行Runnable任务, 并且两个CompletableFuture任务可以
  是不同类型的, 比如一个是有返回值一个没返回值, 或者一个返回值为字符串一个为整型, 并且可以提供一个
  自定义的线程池去执行, 如果没有提供则使用ForkJoinPool.commonPool()线程池, 举个例子:
  CompletableFuture.runAsync( () -> System.out.println( "无返回值" ) )
    .runAfterBothAsync( CompletableFuture.supplyAsync( () -> 1 ),
    () -> System.out.println( "Done.." ) );
  可以看到提供了一个是无返回值的runAsync和一个有返回值的supplyAsync, action就是在两个给定的
  CompletableFuture都执行完毕的时候输出"Done"

runAfterEither(CompletionStage<?> other, Runnable action),
runAfterEitherAsync(CompletionStage<?> other, Runnable action),
runAfterEitherAsync(CompletionStage<?> other, Runnable action, Executor executor):
  提供两个CompletableFuture, 先完成的那个会触发action的执行, 两个CompletableFuture都会执行完毕

thenAcceptBoth(CompletionStage<? extends U> other, BiConsumer<? super T, ? super U> action),
thenAcceptBothAsync(CompletionStage<? extends U> other, BiConsumer<? super T, ? super U> action),
thenAcceptBoth(CompletionStage<? extends U> other,
  BiConsumer<? super T, ? super U> action, Executor executor):
  接收两个结果, 然后对两个结果进行消费, 两个CompletableFuture的返回值类型可以不一致, 通过BiConsumer
  来接收两个结果

acceptEither(CompletionStage<? extends T> other, Consumer<? super T> action),
acceptEitherAsync(CompletionStage<? extends T> other, Consumer<? super T> action),
acceptEitherAsync(CompletionStage<? extends T> other,
  Consumer<? super T> action, Executor executor):
  接收两个结果中先完成的那个结果, 然后交给Consumer消费, 需要注意的是两个CompletableFuture的返回值
  类型必须是一致的, 否则Consumer不知道消费的是什么类型, 举个例子:
  CompletableFuture.supplyAsync( () -> "a" )
    .acceptEither( CompletableFuture.supplyAsync( () -> "b" ),
    (v) -> System.out.println( Thread.currentThread().getName() + " ==> " + v ) );
  一个CompletableFuture的返回值是字符串a, 一个CompletableFuture返回值是字符串b, 先执行完毕的会
  被Comsumer消费, 即输出线程名加上值, 两个CompletableFuture都会执行完毕, 只不过消费的时候只会消
  费一个数据而已

applyToEither(CompletionStage<? extends T> other, Function<? super T, U> fn)
applyToEitherAsync(CompletionStage<? extends T> other, Function<? super T, U> fn)
applyToEitherAsync(CompletionStage<? extends T> other,
  Function<? super T, U> fn, Executor executor):
  跟acceptEither是类似的, 都要求两个CompletableFuture是同一类型的, 只不过对结果的处理采用了不同
  的函数式接口, acceptEither采用的Consumer, 即对结果进行消费, 而applyToEither采用的是Function
  接口, 即接收一个结果, 返回一个任意类型的结果

thenCombine(CompletionStage<? extends U> other,
  BiFunction<? super T,? super U,? extends V> fn),
thenCombineAsync(CompletionStage<? extends U> other,
  BiFunction<? super T,? super U,? extends V> fn),
thenCombineAsync(CompletionStage<? extends U> other,
  BiFunction<? super T,? super U,? extends V> fn, Executor executor):
  提供两个CompletableFuture, 对两个CompletableFuture的结果可以进行统一处理, 即通过BiFunction函
  数式接口, 传入两个参数, 返回一个任意类型的值, 即组合两个结果, 举个例子:
  // 第一个CompletableFuture
  CompletableFuture.supplyAsync( () -> {
			System.out.println( "start--1" );
			sleep( 5 );
			System.out.println( "end--1" );
			return "aaa";
		} ).thenCombine( CompletableFuture.supplyAsync( () -> { // 第二个CompletableFuture
			System.out.println( "start--2" );
			sleep( 10 );
			System.out.println( "end--2" );
			return 111;
		} ), ( v1, v2 ) -> { // 传入第一个和第二个CompletableFuture的结果
      System.out.println( v1 + "=====" + v2 );
      // 返回一个结果
			return v1 + v2;
    } );

thenCompose(Function<? super T, ? extends CompletionStage<U>> fn),
thenComposeAsync(Function<? super T, ? extends CompletionStage<U>> fn),
thenComposeAsync(Function<? super T, ? extends CompletionStage<U>> fn, Executor executor):
  传入一个任意类型的参数, 返回一个任意类型的CompletableFuture, 举个例子:
  // 通过CompletableFuture.supplyAsync来获取一个CompletableFuture, 并且返回值为字符串aaa
  CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
			System.out.println("start--1");
			sleep(5);
			System.out.println("end--1");
			return "aaa";
		}).thenCompose((v) -> {
      // 调用thenCompose方法, 上一个CompletableFuture的返回值作为参数传入
      System.out.println(v);
      // 返回一个整型的CompletableFuture
			return CompletableFuture.supplyAsync(() -> 1);
		});
```

## 其它API描述
```java
getNow(T t): CompletableFuture仍然是Future类型的, 所以调用get的时候仍然可能陷入阻塞, getNow中传
             入一个值, 如果CompletableFuture已经得到了结果则返回对应的结果, 否则返回方法中传入的
             值

complete(T t): 如果CompletableFuture提交的任务还没启动, 则该任务设置为已经执行完毕, 之后该
               Future获得的值则是complete方法中传入的值, 如果任务正在执行, 那么该任务可以继续执行,
               但是之后获取的值仍然是complete方法中传入的值, 文档的描述是:
               "If not already completed, sets the value returned by get() and related
               methods to the given value."总的来说, complete方法中的参数就是该任务的结果

completeExceptionally(Throwable t): 通过该方式传入一个异常对象, 如果在任务未完成之前调用get方法
                                    去获取值, 则会抛出该异常, 文档描述:
                                    "If not already completed, causes invocations of get()
                                    and related methods to throw the given exception."

exceptionally(Function<Throwable, ? extends T> fn): CompletableFuture绑定了该方法后, 如果在
                                                    执行任务的时候出现了异常, 那么就会被该方法
                                                    捕捉到, 然后再该方法中对发生异常时结果应该
                                                    怎么处理, 举个例子:
  // 创建一个CompletableFuture, 并传入一个任务, 任务中会出现除0操作, 则会报错
  // 当发生错误的时候, t.getMessage()作为结果返回从而替代原来的结果aaa
  CompletableFuture.supplyAsync( () -> {
			System.out.println("start ...");
			sleep(5);
			System.out.println("end ...");
			int a = 10 / 0;
			return "abc";
    } ).exceptionally( (t) -> t.getMessage() )
    // 结束的时候输出结果, 因为出现了异常, 所以这个结果时异常信息
		.whenCompleteAsync( (v, t) -> System.out.println(v) );
```
