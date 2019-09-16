package com.fightzhong.concurrency._03_JUC并发包学习._10_Executor._05_CompletableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class TestClass9 {
	public static void main (String[] args) throws ExecutionException, InterruptedException {
		// getNow();
		// complete();
		// completeExceptionally();
		// testExceptionally();
		// testJoin();
		testAllOf();
		Thread.currentThread().join();
	}

	public static void sleep (int seconds) {
		try {
			TimeUnit.SECONDS.sleep( seconds );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void testAllOf () {
		CompletableFuture<Object> future = CompletableFuture.anyOf(
			CompletableFuture.runAsync(() -> {
				System.out.println("Start1");
				sleep(3);
				System.out.println("End1");
			}),
			CompletableFuture.supplyAsync(() -> {
				System.out.println("Start2");
				sleep(5);
				System.out.println("End2");
				return 1;
			})
		);
		future.whenCompleteAsync( (v, t) -> {
			System.out.println( "Done" );
		} );
	}

	public static void testJoin () throws ExecutionException, InterruptedException {
		CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
			System.out.println("start ...");
			sleep(5);
			System.out.println("end ...");
			if (true) {
				int a = 1 / 0;
			}
			return "abc";
		});

		String result = future.join();
		// String result = future.get();
		System.out.println( result );
	}

	public static void testExceptionally () {
		CompletableFuture.supplyAsync( () -> {
			System.out.println("start ...");
			sleep(5);
			System.out.println("end ...");
			int a = 10 / 0;
			return "abc";
		} ).exceptionally( (t) -> t.getMessage() )
		.whenCompleteAsync( (v, t) -> System.out.println(v) );
	}

	public static void completeExceptionally () throws ExecutionException, InterruptedException {
		CompletableFuture<Integer> future = CompletableFuture.supplyAsync( () -> {
			System.out.println("start ...");
			sleep(5);
			System.out.println("end ...");
			return 1;
		} );

		sleep( 3 );
		future.completeExceptionally( new IllegalArgumentException("aaa") );
		System.out.println( "=============" );
		System.out.println( future.get() );


	}

	public static void complete () throws ExecutionException, InterruptedException {
		CompletableFuture<Integer> future = CompletableFuture.supplyAsync( () -> {
			System.out.println("start ...");
			sleep(5);
			System.out.println("end ...");
			return 1;
		} );

		future.thenAcceptAsync( System.out::println );

		boolean complete = future.complete(10);
	}

	public static void getNow () throws ExecutionException, InterruptedException {
		CompletableFuture<Integer> future = CompletableFuture.supplyAsync( () -> {
			System.out.println("start ...");
			sleep(5);
			System.out.println("end ...");
			return 1;
		} );
		sleep( 10 );
		System.out.println( "main running" );
		Integer num = future.getNow(10);
		System.out.println( num );
		sleep( 10 );
		System.out.println( future.get() );
	}
}
