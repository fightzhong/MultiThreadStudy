package com.fightzhong.concurrency._03_JUC并发包学习._10_Executor._01_ThreadPoolExecutor;

import java.util.concurrent.*;

public class TestClass7 {
	public static void main (String[] args) throws InterruptedException {
		ThreadPoolExecutor pool = new ThreadPoolExecutor( 1, 3, 10, TimeUnit.SECONDS,
																		new ArrayBlockingQueue<>(1), (r) -> new Thread( r ),
																		new ThreadPoolExecutor.DiscardOldestPolicy() );
		pool.allowCoreThreadTimeOut( true );
		for ( int i = 0; i < 5; i ++ )
			pool.execute( () -> sleeping( 5 ) );
		System.out.println( "================" );

	}

	public static void sleeping (int second) {
		try {
			TimeUnit.SECONDS.sleep( second );
			System.out.println( "finished...." );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
