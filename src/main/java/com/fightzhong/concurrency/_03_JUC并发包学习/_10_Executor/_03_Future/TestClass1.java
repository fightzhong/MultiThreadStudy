package com.fightzhong.concurrency._03_JUC并发包学习._10_Executor._03_Future;

import java.util.concurrent.*;

public class TestClass1 {
	public static void main (String[] args) throws InterruptedException, ExecutionException {
		ExecutorService pool = Executors.newFixedThreadPool( 2 );
		Future<Integer> r1 = pool.submit(() -> {
			sleeping( 10 );
			System.out.println( "11111111111111111111" );
			return 1;
		});

		Future<Integer> r2 = pool.submit(() -> {
			sleeping( 10 );
			System.out.println( "2222222222222222222" );
			return 2;
		});

		Thread.sleep( 100 );

		boolean b  = r1.cancel( true );
		System.out.println( b );
		System.out.println( "main....................................." );
		System.out.println( "========================" );
	}
	
	public static void sleeping (int second) {
		try {
			TimeUnit.SECONDS.sleep( second );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
