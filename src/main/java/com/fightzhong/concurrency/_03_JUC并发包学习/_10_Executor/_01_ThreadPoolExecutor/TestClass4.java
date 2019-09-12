package com.fightzhong.concurrency._03_JUC并发包学习._10_Executor._01_ThreadPoolExecutor;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestClass4 {
	public static void main (String[] args) {
		// ThreadPoolExecutor pool = ( ThreadPoolExecutor )Executors.newCachedThreadPool();
		// ThreadPoolExecutor pool = ( ThreadPoolExecutor )Executors.newFixedThreadPool( 10 );
		ThreadPoolExecutor pool = ( ThreadPoolExecutor )Executors.newSingleThreadExecutor();
		for ( int i = 0; i < 100 ; i ++ )
			pool.submit( () -> sleeping( 10 ) );
	}

	public static void sleeping (int seconds) {
		try {
			TimeUnit.SECONDS.sleep( seconds );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println( "sleeping over... " + Thread.currentThread().getName() );
	}
}
