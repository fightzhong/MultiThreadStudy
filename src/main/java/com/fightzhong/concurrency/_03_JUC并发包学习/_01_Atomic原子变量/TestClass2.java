package com.fightzhong.concurrency._03_JUC并发包学习._01_Atomic原子变量;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class TestClass2 {
	@Test
	public void constructor () {
		AtomicInteger i1 = new AtomicInteger( 1 );
		AtomicInteger i2 = new AtomicInteger();

		System.out.println( i1.get() );
		System.out.println( i2.get() );
	}

	@Test
	public void addAndGet () {
		AtomicInteger i = new AtomicInteger( 30 );
		int result = i.addAndGet(20);
		System.out.println( i.get() );
		System.out.println( result );
	}

	@Test
	public void  set () {
		AtomicInteger i = new AtomicInteger( 20 );
		System.out.println( i.get() );
		i.set( 60 );
		System.out.println( i.get() );
	}

	// @Test
	// public void compareAndExchange () {
	// 	AtomicInteger i = new AtomicInteger( 60 );
	// 	System.out.println( i.get() );
	// 	int result = i.compareAndExchange(60, 80);
	// 	System.out.println( i.get() );
	// 	System.out.println( result );
	// }
	//
	// @Test
	// public void compareAndExchangeAcquire () {
	// 	AtomicInteger i = new AtomicInteger( 80 );
	// 	System.out.println( i );
	// 	int result = i.compareAndExchangeAcquire(80, 90);
	// 	System.out.println( i );
	// 	System.out.println( result );
	// }

	@Test
	public void compareAndSet () {
		AtomicInteger i = new AtomicInteger( 50 );
		boolean result = i.compareAndSet(50, 80);
	}

	@Test
	public void decrementAndGet () {
		AtomicInteger i = new AtomicInteger( 5 );
		System.out.println( i );
		int result = i.decrementAndGet();
		System.out.println( result );
		System.out.println( i );
	}

	@Test
	public void doubleValue () {
		AtomicInteger i = new AtomicInteger( 5 );
		double result = i.doubleValue();
		System.out.println( result );
	}

	@Test
	public void getAndUpdate () {
		AtomicInteger i = new AtomicInteger( 5 );
		int result = i.getAndUpdate( (a) -> {
			return 1;
		} );
		System.out.println( i );
		System.out.println( result );
	}

}
