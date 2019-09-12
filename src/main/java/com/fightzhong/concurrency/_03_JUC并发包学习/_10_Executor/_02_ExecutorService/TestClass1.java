package com.fightzhong.concurrency._03_JUC并发包学习._10_Executor._02_ExecutorService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.*;

public class TestClass1 {
	public static void main (String[] args) throws ExecutionException, InterruptedException {
		ExecutorService pool = Executors.newFixedThreadPool( 2 );
		Collection<Callable<Integer>> callbales = new ArrayList<>();
		for ( int i = 0; i < 5; i ++ ) {
			final int index = i;
			callbales.add( () -> {
				sleeping( 10, true );
				return index;
			} );
		}

		Integer integer = pool.invokeAny(callbales);
		System.out.println( "=======================================" );
		System.out.println( integer );
	}

	public static void sleeping (int rangeSeconds, boolean flag) {
		Random ran = new Random();
		try {
			if ( flag )
				TimeUnit.SECONDS.sleep( ran.nextInt( rangeSeconds ) );
			else
				TimeUnit.SECONDS.sleep( rangeSeconds );
			System.out.println( "sleeping finished..." );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
