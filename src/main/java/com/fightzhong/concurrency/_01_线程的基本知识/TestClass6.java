package com.fightzhong.concurrency._01_线程的基本知识;

public class TestClass6 {
	public static void main (String[] args) throws InterruptedException {
		System.out.println( Thread.currentThread().getId() );
		Thread t = new Thread( () -> {
			System.out.println( "Name: " + Thread.currentThread().getName() );
			System.out.println( "ID : " + Thread.currentThread().getId() );
			System.out.println( "priority : " + Thread.currentThread().getPriority() );
			try {
				Thread.sleep( 1000 );
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} );

		t.start();

	}
}
