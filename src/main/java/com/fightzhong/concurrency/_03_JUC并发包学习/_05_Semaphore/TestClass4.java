package com.fightzhong.concurrency._03_JUC并发包学习._05_Semaphore;

import java.util.concurrent.Semaphore;

public class TestClass4 {
	public static void main (String[] args) throws InterruptedException {
		Semaphore semaphore = new Semaphore( 1 );
		Thread t1 = new Thread( () -> {
			System.out.println( Thread.currentThread().getName() + "开始执行" );
			try {
				semaphore.acquireUninterruptibly( 2 );
				System.out.println(  Thread.currentThread().getName() + "获得2个信号量" );
			} finally {
				semaphore.release( 2 );
				System.out.println(  Thread.currentThread().getName() + "释放2个信号量" );
			}
			System.out.println( Thread.currentThread().getName() + "结束执行" );
		} );

		t1.start();
		Thread.sleep( 2000 );
		t1.interrupt();

	}
}
