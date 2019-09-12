package com.fightzhong.concurrency._03_JUC并发包学习._10_Executor._01_ThreadPoolExecutor;

import com.fightzhong.concurrency._02_线程的进阶知识.异步任务的回调实现.Executor;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class TestClass5 {
	public static void main (String[] args) {
		final AtomicInteger count = new AtomicInteger( 1 );
		MyThreadPoolExecutors pool = new MyThreadPoolExecutors( 1, 3, 10,
																				TimeUnit.SECONDS, new ArrayBlockingQueue<>( 1 ),
																				(r) -> {
																					Thread t = new Thread( r, "T => " + count.getAndIncrement() );
																					return t;
																				}, new ThreadPoolExecutor.AbortPolicy() );
		IntStream.range( 0, 3 ).forEach( (i) -> {
			pool.execute( () -> {
				sleeping( 5 );
			} );
		} );

	}

	static class MyThreadPoolExecutors extends ThreadPoolExecutor {

		public MyThreadPoolExecutors (int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
			super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
		}

		@Override
		protected void beforeExecute (Thread t, Runnable r) {
			System.out.println( Thread.currentThread().getName() + "start......" );
		}

		@Override
		protected void afterExecute (Runnable r, Throwable t) {
			System.out.println( Thread.currentThread().getName() + "end......" );
		}
	}

	public static void sleeping (int second) {
		try {
			TimeUnit.SECONDS.sleep( second );
			System.out.println( "finished...." );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void look (ThreadPoolExecutor pool) {
		int poolSize = pool.getPoolSize();
		int queueSize = pool.getQueue().size();
		int activeCount = pool.getActiveCount();
		System.out.println( "poolSize: " + pool.getPoolSize() );
		System.out.println( "queueSize: " + pool.getQueue().size() );
		System.out.println( "activeCount: " + pool.getActiveCount() );
		System.out.println( "========================================" );
		while ( true ) {
			if ( activeCount != pool.getActiveCount() || poolSize != pool.getPoolSize() || queueSize != pool.getQueue().size() ) {
				System.out.println( "poolSize: " + pool.getPoolSize() );
				System.out.println( "queueSize: " + pool.getQueue().size() );
				System.out.println( "activeCount: " + pool.getActiveCount() );
				System.out.println( "========================================" );
				poolSize = pool.getPoolSize();
				queueSize = pool.getQueue().size();
				activeCount = pool.getActiveCount();
			}
		}
	}
}
