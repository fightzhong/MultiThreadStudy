package com.fightzhong.concurrency._03_JUC并发包学习._08_ForkJoin;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

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
