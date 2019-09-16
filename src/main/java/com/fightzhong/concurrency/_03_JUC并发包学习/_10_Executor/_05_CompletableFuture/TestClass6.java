package com.fightzhong.concurrency._03_JUC并发包学习._10_Executor._05_CompletableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class TestClass6 {
	public static void main (String[] args) throws InterruptedException {
		CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
			System.out.println( "start ..." );
			sleep( 5 );
			System.out.println( "end ..." );
			return "aa";
		} );

		CompletableFuture<Void> future2 = future.thenRun(() -> {
			System.out.println("start2 ...");
			sleep(5);
			System.out.println("end2 ...");
		});

		System.out.println( "===============" );
		Thread.currentThread().join();
	}

	public static void sleep (int seconds) {
		try {
			TimeUnit.SECONDS.sleep( seconds );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
