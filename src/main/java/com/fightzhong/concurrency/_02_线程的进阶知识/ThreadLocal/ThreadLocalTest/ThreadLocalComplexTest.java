package com.fightzhong.concurrency._02_线程的进阶知识.ThreadLocal.ThreadLocalTest;

public class ThreadLocalComplexTest {
	public static void main (String[] args) throws InterruptedException {
		// ThreadLocal threadLocal = new ThreadLocal() {
		// 	@Override
		// 	protected Object initialValue () {
		// 		return Thread.currentThread().getName();
		// 	}
		// };
		MyThreadLocal threadLocal = new MyThreadLocal() {
			@Override
			protected Object initValue () {
				return Thread.currentThread().getName();
			}
		};

		Thread t1 = new Thread( () -> {
			threadLocal.set( Thread.currentThread().getName() + "====" );

			try {
				Thread.sleep( 3000 );
			} catch (InterruptedException e) {}

			System.out.println( Thread.currentThread().getName() + ": " + threadLocal.get() );;
		}, "线程一" );

		Thread t2 = new Thread( () -> {
			threadLocal.set( Thread.currentThread().getName() + "----" );

			try {
				Thread.sleep( 6000 );
			} catch (InterruptedException e) {}

			System.out.println( Thread.currentThread().getName() + ": " + threadLocal.get() );;
		}, "线程二" );

		t1.start();
		t2.start();
		t1.join();
		System.out.println( "==================" );
		t2.join();

		System.out.println( Thread.currentThread().getName() + ": " + threadLocal.get() );
	}
}
