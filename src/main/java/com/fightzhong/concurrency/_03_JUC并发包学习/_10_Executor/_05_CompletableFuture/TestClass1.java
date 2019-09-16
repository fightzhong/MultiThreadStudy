package com.fightzhong.concurrency._03_JUC并发包学习._10_Executor._05_CompletableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TestClass1 {
	public static void main (String[] args) throws InterruptedException {
		CompletableFuture.supplyAsync( () -> {
			System.out.println( "start the query" );
			sleep( 5 );
			System.out.println( "finish the query" );
			return "hahaha";
		} ).whenComplete( ( v, t ) -> {
			System.out.println( "display the query-" + v );
			sleep( 5 );
			System.out.println( "finish display the query-" + v );
		} );

		System.out.println( "main thread is running ...." );
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
