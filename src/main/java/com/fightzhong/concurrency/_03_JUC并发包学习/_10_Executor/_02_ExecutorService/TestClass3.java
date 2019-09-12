package com.fightzhong.concurrency._03_JUC并发包学习._10_Executor._02_ExecutorService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

public class TestClass3 {
	public static void main (String[] args) throws InterruptedException, ExecutionException {
		ExecutorService pool = Executors.newFixedThreadPool( 2 );
		Collection<Callable<Integer>> callbales = new ArrayList<>();

		callbales.add( () -> {
			sleeping( 10 );
			System.out.println( "======================" );
			return 1;
		} );

		callbales.add( () -> {
			sleeping( 15 );
			System.out.println( "6666" );
			return 2;
		} );

		List<Future<Integer>> futures = pool.invokeAll(callbales, 11, TimeUnit.SECONDS);
		// for ( Future f: futures )
		System.out.println( "zzzzzz" );
		// 	System.out.println( f.get() );
	}

	public static void sleeping (int rangeSeconds) {
		try {
			TimeUnit.SECONDS.sleep( rangeSeconds );
			System.out.println( "sleeping finished..." );
		} catch (InterruptedException e) {}
	}
}
