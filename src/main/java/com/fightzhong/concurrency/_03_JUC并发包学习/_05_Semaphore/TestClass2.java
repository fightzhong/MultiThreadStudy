package com.fightzhong.concurrency._03_JUC并发包学习._05_Semaphore;

import java.util.concurrent.Semaphore;

public class TestClass2 {
	public static void main (String[] args) throws InterruptedException {
		Semaphore semaphore = new Semaphore( 2 );
		for (int i = 0 ; i < 3 ; i++) {
			new Thread( () -> {
				System.out.println( Thread.currentThread().getName() + "开始执行" );
				try {
					semaphore.acquire( 1 );
					System.out.println(  Thread.currentThread().getName() + "获得1个信号量" );
					Thread.sleep( 5000 );
				} catch (InterruptedException e) {

				} finally {
					semaphore.release( 1 );
					System.out.println(  Thread.currentThread().getName() + "释放1个信号量" );
				}

				System.out.println( Thread.currentThread().getName() + "结束执行" );
			} ).start();
		}

		while ( true ) {
			Thread.sleep( 1000 );
			System.out.println( semaphore.availablePermits() );
			System.out.println( semaphore.getQueueLength() );
			System.out.println( "==================================" );
		}
	}
}
