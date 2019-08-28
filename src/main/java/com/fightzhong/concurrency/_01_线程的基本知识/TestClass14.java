package com.fightzhong.concurrency._01_线程的基本知识;

public class TestClass14 {
	public static void main (String[] args) {
		Thread t1 = new Thread( "f1" ){
			@Override
			public void run () {
				f1();
			}
		};

		t1.start();

		Thread t2 = new Thread( "f2" ){
			@Override
			public void run () {
				f2();
			}
		};

		t2.start();
	}

	public synchronized static void f1 () {
		System.out.println( Thread.currentThread().getName() + ": 持有锁" );
		try {
			TestClass14.class.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public synchronized static void f2 () {
		System.out.println( Thread.currentThread().getName() + ": 持有锁" );
		try {
			TestClass14.class.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
