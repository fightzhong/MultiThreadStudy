package com.fightzhong.concurrency._01_线程的基本知识.线程池;

public class TestClass {
	public static void main (String[] args) throws InterruptedException {
		SimpleThreadPool2 pool = new SimpleThreadPool2( 6, 10, SimpleThreadPool2.DEFAULT_REFUSE_STRATEGY );
		for ( int i = 0; i < 20; i ++ ) {
			final int j = i;

			Runnable runnable = () -> {
				System.out.println( Thread.currentThread().getName() + " : 开始执行第 " + j + "个任务" );

				try {
					Thread.sleep( 5000 );
				} catch (InterruptedException e) {}

				System.out.println( Thread.currentThread().getName() + " : 结束执行第 " + j + "个任务" );
			};

			pool.submit( runnable );
		}

		System.out.println( "==========================" );
		Thread.sleep( 10000 );
		pool.shutdown();
	}
}
