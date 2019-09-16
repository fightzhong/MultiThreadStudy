package com.fightzhong.concurrency._03_JUC并发包学习._10_Executor._05_CompletableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class TestClass7 {
	public static void main (String[] args) throws InterruptedException {
		// CompletableFuture.supplyAsync( () -> "a" )
		// 	.acceptEither( CompletableFuture.supplyAsync( () -> "b" ),
		// 	(v) -> System.out.println( Thread.currentThread().getName() + " ==> " + v ) );

		// CompletableFuture.supplyAsync( () -> {
		// 	System.out.println( "start--1" );
		// 	sleep( 5 );
		// 	System.out.println( "end--1" );
		// 	return "a";
		// } ).applyToEitherAsync( CompletableFuture.supplyAsync( () -> {
		// 	System.out.println( "start--2" );
		// 	sleep( 10 );
		// 	System.out.println( "end--2" );
		// 	return "b";
		// } ), ( v ) -> {
		// 	System.out.println( v );
		// 	return v + "--Done--";
		// } ).whenCompleteAsync( (v, t) -> System.out.println( v ) );

		CompletableFuture.runAsync( () -> {
			System.out.println( "start--1" );
			sleep( 5 );
			System.out.println( "end--1" );
		} ).runAfterEither( CompletableFuture.supplyAsync( () -> {
			System.out.println( "start--2" );
			sleep( 10 );
			System.out.println( "end--2" );
			return "a";
		} ), () -> System.out.println( "Done..." ) );

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
