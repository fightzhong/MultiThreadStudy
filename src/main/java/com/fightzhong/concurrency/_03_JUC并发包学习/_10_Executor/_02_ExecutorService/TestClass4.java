package com.fightzhong.concurrency._03_JUC并发包学习._10_Executor._02_ExecutorService;

import java.util.concurrent.*;

public class TestClass4 {
	public static void main (String[] args) throws ExecutionException, InterruptedException {
		ExecutorService pool = Executors.newFixedThreadPool( 3 );
		Future<String> future = pool.submit( () -> {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}, "aaa" );

		try {
			String s = future.get(3, TimeUnit.SECONDS);
			System.out.println( s );
		} catch ( TimeoutException e ){}
		
		System.out.println( "超时.........." );
		Thread.sleep( 10000 );
		String s = future.get();
		System.out.println( s );

	}
}
