package com.fightzhong.concurrency._03_JUC并发包学习._10_Executor._01_ThreadPoolExecutor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TestClass2 {
	public static void main (String[] args) throws InterruptedException {
		ThreadPoolExecutor pool = new ThreadPoolExecutor( 1, 3, 30,
											TimeUnit.SECONDS, new ArrayBlockingQueue<>(1), (r) -> {
												Thread thread = new Thread( r );
												// thread.setDaemon( true );
												return thread;
											} );

		for ( int i = 0; i < 2; i ++ )
			pool.submit( () -> {
				while( true );
			} );

		new Thread( () -> look( pool ), "look" ).start();

		Thread.sleep( 3000 );
		pool.shutdownNow();


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

	private static AtomicInteger count = new AtomicInteger();
	public static void sleeping (int seconds) {
		try {
			TimeUnit.SECONDS.sleep( seconds );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println( "sleeping over... " + count.incrementAndGet() );
	}
}
