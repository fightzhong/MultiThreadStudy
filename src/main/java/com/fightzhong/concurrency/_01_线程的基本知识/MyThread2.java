package com.fightzhong.concurrency._01_线程的基本知识;

public class MyThread2 implements Runnable {
	public static void main (String[] args) {
	    Thread t1 = new Thread( new MyThread2() );
	    t1.start();
	}
	public void run () {
		System.out.println( Thread.currentThread().getName() );
	}
}
