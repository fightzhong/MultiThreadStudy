package com.fightzhong.concurrency._01_线程的基本知识;

public class TestClass11 {
	public static void main (String[] args) throws InterruptedException {
		Thread t1 = new Thread( () -> {
			System.out.println( ">>>t1线程的interrupted: " + Thread.interrupted() );   // false
			while( !Thread.interrupted() ) {
				// 在未接收到中断信号之前执行一系列的逻辑
			}
			System.out.println( ">>>t1线程的interrupted: " + Thread.interrupted() );   // false
		} );
		t1.start();

		// 先让main线程休眠3秒钟, 然后再去中断t1线程
		System.out.println( "t1线程的interrupted: " + t1.isInterrupted() );  // false
		Thread.sleep( 3000 );
		t1.interrupt();
		System.out.println( "t1线程的interrupted: " + t1.isInterrupted() );  // true
	}
}
