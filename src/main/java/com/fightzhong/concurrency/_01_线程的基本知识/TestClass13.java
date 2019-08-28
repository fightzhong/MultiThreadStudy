package com.fightzhong.concurrency._01_线程的基本知识;

public class TestClass13 {
	public static void main (String[] args) {

		Thread t1 = new Thread( () -> {
			f1();
		}, "f1" );
		t1.start();

		Thread t2 = new Thread( () -> {
			f2();
		}, "f2" );
		t2.start();
	}

	public synchronized static void f1 () {
		System.out.println( Thread.currentThread().getName() + ": 持有锁" );
		try {
			Thread.sleep( 5_000 );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public synchronized static void f2 () {
		System.out.println( Thread.currentThread().getName() + ": 持有锁" );
	}
}
