package com.fightzhong.concurrency._01_线程的基本知识;

public class MyThread extends Thread {
	public void run () {
		System.out.println( Thread.currentThread().getName() );
	}
}
