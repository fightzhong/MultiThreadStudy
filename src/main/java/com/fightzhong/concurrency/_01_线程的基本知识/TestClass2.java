package com.fightzhong.concurrency._01_线程的基本知识;

public class TestClass2 {
	public static void main (String[] args) {
		Thread t1 = new Thread( () -> {
			System.out.println( Thread.currentThread().getName() );
		} );

		t1.start();
	}
}
