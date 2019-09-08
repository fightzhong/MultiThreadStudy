package com.fightzhong.concurrency._03_JUC并发包学习._08_ForkJoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class TestClass1 {
	public static void main (String[] args) throws ExecutionException, InterruptedException {
		ForkJoinPool pool = new ForkJoinPool();
		ForkJoinTask result = pool.submit( new CaculateForkJoinTask( 1, 1000000000) );
		System.out.println( "利用ForkJoin的结果: " + result.get() );
	}

	private static class CaculateForkJoinTask extends RecursiveTask<Long> {
		private final int MIN_FORK = 50;
		private final int start;
		private final int end;

		public CaculateForkJoinTask (int start, int end) {
			this.start = start;
			this.end = end;
		}

		@Override
		protected Long compute () {
			long result = 0;
			if ( end - start <= MIN_FORK ) {
				for ( int i = start; i <= end; i ++ ) {
					result += i;
				}
				return result;
			}

			int mid = ( end - start ) / 2 + start;
			CaculateForkJoinTask task1 = new CaculateForkJoinTask( start, mid );
			CaculateForkJoinTask task2 = new CaculateForkJoinTask(mid + 1, end );
			task1.fork();
			task2.fork();

			return task1.join() + task2.join();
		}
	}
}
