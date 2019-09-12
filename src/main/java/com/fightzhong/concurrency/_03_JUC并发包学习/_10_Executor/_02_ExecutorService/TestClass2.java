package com.fightzhong.concurrency._03_JUC并发包学习._10_Executor._02_ExecutorService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.*;

public class TestClass2 {
	public static void main (String[] args) throws InterruptedException, ExecutionException, TimeoutException {
		ExecutorService pool = Executors.newFixedThreadPool( 2 );
		Collection<Callable<Integer>> callbales = new ArrayList<>();

		callbales.add( () -> {
			sleeping( 10 );
			System.out.println( "======================" );
			int i = 1 / 0;
			return i;
		} );

		callbales.add( () -> {
			sleeping( 15 );
			System.out.println( "6666" );
			return 2;
		} );

		Integer result = pool.invokeAny( callbales );
		System.out.println( result );
	}

	public static void sleeping (int rangeSeconds) {
		try {
			TimeUnit.SECONDS.sleep( rangeSeconds );
			System.out.println( "sleeping finished..." );
		} catch (InterruptedException e) {}
	}
}
