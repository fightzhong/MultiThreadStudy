package com.fightzhong.concurrency._03_JUC并发包学习._10_Executor._01_ThreadPoolExecutor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TestClass1 {
	public static void main (String[] args) {
		ThreadPoolExecutor pool = new ThreadPoolExecutor( 10, 20,
			5, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(5),
		(r) -> new Thread( r ), new ThreadPoolExecutor.AbortPolicy() );

		for ( int i = 0; i < 15; i ++ )
			pool.submit( () -> sleeping( 10 ) );

		pool.shutdown();
		try {
			pool.awaitTermination( 30, TimeUnit.SECONDS );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println( "======================" );
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
