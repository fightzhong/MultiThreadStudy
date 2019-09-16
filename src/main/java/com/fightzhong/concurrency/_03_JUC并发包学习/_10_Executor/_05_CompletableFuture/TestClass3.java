package com.fightzhong.concurrency._03_JUC并发包学习._10_Executor._05_CompletableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class TestClass3 {
	public static void main (String[] args) throws InterruptedException {
		CompletableFuture<Void> future = CompletableFuture.runAsync( () -> {
			System.out.println( "a" );
		} );
		sleep( 1 );
		future.whenComplete((v, t) -> {
			System.out.println("async....start + " + Thread.currentThread().getName());
			sleep(5);
			System.out.println("async....end");
		});

		System.out.println( "========================" );
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
