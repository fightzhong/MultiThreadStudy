package com.fightzhong.concurrency._01_线程的基本知识;

public class TestClass7 {
	public static void main (String[] args) {
		System.out.println( "main 线程执行开始" );

		Thread t = new Thread( () -> {
			System.out.println( Thread.currentThread().getName() + ": 执行开始" );

			try {
				Thread.sleep( 5000 );
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			System.out.println( Thread.currentThread().getName() + ": 执行结束" );
		} );
		t.start();

		try {
			t.join( 2000 );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println( "main 线程执行结束" );



	}
}
