package com.fightzhong.concurrency._03_JUC并发包学习._10_Executor._01_ThreadPoolExecutor;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestClass8 {
	public static void main (String[] args) {
		ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool( 1 );
		Runnable r = () -> sleeping( 5 );
		pool.execute( r );
		sleeping(1);
		System.out.println( "============" );
		pool.getQueue().add( r );
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
