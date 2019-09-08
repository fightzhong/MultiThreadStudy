## ForkJoin
```
ForkJoin利用fork, join的模式, 分而治之, 将大任务拆分为一个个的小任务去执行, 主要涉及到下面的几个
类:
  RecursiveTask: 抽象任务类, 有返回值, 通过将任务的内容定义在compute方法中, 并且将任务分割的逻辑
                 也要定义在里面, 其实compute方法的逻辑就跟其名字一样, 递归的任务, 当达到了最小的
                 任务时则自己执行, 否则继续创建两个RecursiveTask任务, 然后调用它们的fork方法进行
                 分治, 对于该类来说, 存在返回值, 所以需要最后调用join方法将结果获取并配以合并的逻
                 辑从而得到一个总的结果后再返回

  RecursiveAction:  抽象任务类, 无返回值, 同RecursiveTask一样, 不同的是在将任务fork之后不需要
                    join, 并且不需要返回结果, 其实可以通过定义一个全局的变量来进行结果的汇总从而
                    达到有结果的情况

 以上的两个类都是继承于ForkJoinTask, 而ForkJoinTask继承于Future, 也就是说在最后获取结果的时候调
 用get方法是同步的

 ForkJoinPool: ForkJoinTask任务的执行对象,通过调用该类实例的submit方法来提交任务, 最后返回的是
              一个ForkJoinTask对象, 然后调用结果对象的get()方法获取结果
```

## RecursiveTask带返回值的实现1-1000的累加
```java
// 首先我们继承RecursiveTask, 并重写compute方法, 定义我们的任务分治的逻辑
class CaculateForkJoinTask extends RecursiveTask<Long> {
  private final int MIN_FORK = 50;    // 最小的任务, 即类加的个数大于50个时需要分治
  private final int start;            // 类加的开始值
  private final int end;              // 类加的结束值

  public CaculateForkJoinTask (int start, int end) {
    this.start = start;
    this.end = end;
  }

  // 具体的任务
  @Override
  protected Long compute () {
    // 类加的结果
    long result = 0;
    // 如果start 到 end的个数小于了最小的类加个数, 那么就自己进行类加, 并返回结果
    if ( end - start <= MIN_FORK ) {
      for ( int i = start; i <= end; i ++ ) {
        result += i;
      }
      return result;
    }

    // 否则就从中间位置分割成两份, 并开启两个任务去执行
    int mid = ( end - start ) / 2 + start;
    // 开启两个任务
    CaculateForkJoinTask task1 = new CaculateForkJoinTask(start, mid);
    CaculateForkJoinTask task2 = new CaculateForkJoinTask(mid + 1, end);
    // 对这两个任务也进行分治
    task1.fork();
    task2.fork();

    // 最后将这些任务执行的结果拼凑成一个总的结果
    return task1.join() + task2.join();
  }
}

public static void main (String[] args) throws ExecutionException, InterruptedException {
  // 创建一个ForkJoinPool来执行任务
  ForkJoinPool pool = new ForkJoinPool();
  // 提交任务
  ForkJoinTask result = pool.submit( new CaculateForkJoinTask( 1, 1000 ) );
  // 由于ForkJoinTask是一个Future的子类, 所以需要通过get来阻塞的获取结果
  System.out.println( "利用ForkJoin的结果: " + result.get() );
}
```

## RecursiveAction实现类加
```java
public class TestClass2 {
	// 定义一个原子变量
	private final static AtomicLong result = new AtomicLong( 0 );
	// 首先我们继承RecursiveAction, 并重写compute方法, 定义我们的任务分治的逻辑
	private static class CaculateForkJoinTask extends RecursiveAction {
		private final int MIN_FORK = 50;    // 最小的任务, 即类加的个数大于50个时需要分治
		private final int start;            // 类加的开始值
		private final int end;              // 类加的结束值

		public CaculateForkJoinTask (int start, int end) {
			this.start = start;
			this.end = end;
		}

		// 具体的任务
		@Override
		protected void compute () {
			// 如果start 到 end的个数小于了最小的类加个数, 那么就将值类加到原子变量中
			if ( end - start <= MIN_FORK ) {
				for ( int i = start; i <= end; i ++ ) {
					result.getAndAdd( i );
				}
			} else {
				// 否则就从中间位置分割成两份, 并开启两个任务去执行
				int mid = ( end - start ) / 2 + start;
				// 开启两个任务
				CaculateForkJoinTask task1 = new CaculateForkJoinTask( start, mid );
				CaculateForkJoinTask task2 = new CaculateForkJoinTask( mid + 1, end );
				// 对这两个任务也进行分治
				task1.fork();
				task2.fork();
			}
		}
	}

	public static void main (String[] args) throws InterruptedException {
		// 创建一个ForkJoinPool来执行任务
		ForkJoinPool pool = new ForkJoinPool();
		// 提交任务, 由于没有返回值, 并且这个任务是异步任务, 所以我们不必进行等待
		ForkJoinTask task = pool.submit( new CaculateForkJoinTask( 1, 1000000000) );
		// 如果5秒内任务没有结束, 则执行之后的代码, 即在此停留一段时间
		pool.awaitTermination( 5, TimeUnit.SECONDS );
		System.out.println( result );
	}
}
```