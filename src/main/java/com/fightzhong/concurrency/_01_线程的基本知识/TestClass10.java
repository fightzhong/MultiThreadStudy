package com.fightzhong.concurrency._01_线程的基本知识;

public class TestClass10 {
	public static void main (String[] args) {
		// 获取当前的main线程
		Thread mainThread = Thread.currentThread();

		Thread t1 = new Thread( () -> {
			for ( int i = 0; i < 10; i ++ ) {
				// 当i == 5的时候, 就不让t1线程继续阻塞main线程了, 而是让两者并发执行
				// 此时需要调用main线程的interrupt方法
				if ( i == 5 ) {
					mainThread.interrupt();
				}

				System.out.println( Thread.currentThread().getName() + " : " + i );
				try {
					Thread.sleep( 1000 );
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} );

		t1.start();
		try {
			// t1.join阻塞的是main线程, 所以需要用main线程来进行打断
			t1.join();
		} catch (InterruptedException e) {
		}
		
		for ( int i = 0; i < 10; i ++ ) {
			System.out.println( Thread.currentThread().getName() + " : " + i );
		}
	}
}
