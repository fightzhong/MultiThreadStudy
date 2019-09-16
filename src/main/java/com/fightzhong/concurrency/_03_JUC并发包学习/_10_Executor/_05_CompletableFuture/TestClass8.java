package com.fightzhong.concurrency._03_JUC并发包学习._10_Executor._05_CompletableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class TestClass8 {
	public static void main (String[] args) throws InterruptedException {
		// CompletableFuture.supplyAsync( () -> {
		// 	System.out.println( "start--1" );
		// 	sleep( 5 );
		// 	System.out.println( "end--1" );
		// 	return "aaa";
		// } ).thenCombine( CompletableFuture.supplyAsync( () -> {
		// 	System.out.println( "start--2" );
		// 	sleep( 10 );
		// 	System.out.println( "end--2" );
		// 	return 111;
		// } ), ( v1, v2 ) -> {
		// 	System.out.println( v1 + "=====" + v2 );
		// 	return v1 + v2;
		// } );
		CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
			System.out.println("start--1");
			sleep(5);
			System.out.println("end--1");
			return "aaa";
		}).thenCompose((v) -> {
			System.out.println(v);
			return CompletableFuture.supplyAsync(() -> 1);
		});

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
