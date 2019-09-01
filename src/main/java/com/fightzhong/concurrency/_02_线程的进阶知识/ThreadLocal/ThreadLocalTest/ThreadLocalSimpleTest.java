package com.fightzhong.concurrency._02_线程的进阶知识.ThreadLocal.ThreadLocalTest;

public class ThreadLocalSimpleTest {
	private static ThreadLocal threadLocal = new ThreadLocal();

	public static void main (String[] args) throws InterruptedException {
		threadLocal.set( "Hello" );

		Thread.sleep( 1000 );

		System.out.println( threadLocal.get() );
	}
}
