package com.fightzhong.concurrency._01_线程的基本知识;

public class TestClass1 {
	public static void main (String[] args) {
		Thread t1 = new Thread("...线程一...") {
			@Override
			public void run () {
				System.out.println( Thread.currentThread().getName() );
			}
		};
		t1.start();
	}
}
