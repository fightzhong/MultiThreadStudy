package com.fightzhong.concurrency._03_JUC并发包学习._10_Executor._05_CompletableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TestClass2 {
	public static void main (String[] args) throws InterruptedException {
		CompletableFuture.supplyAsync( () -> {
			return 100;
		} ).whenComplete( (v,t) -> {
			System.out.println( "start..." + Thread.currentThread().getName() );
			sleep( v );
			System.out.println( "end..." + Thread.currentThread().getName() );
		} );

		System.out.println( "aaa" );
		while( true ) {
			sleep( 1 );
			System.out.println( "main ....." );
		}
	}

	public static void sleep (int seconds) {
		try {
			TimeUnit.SECONDS.sleep( seconds );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
