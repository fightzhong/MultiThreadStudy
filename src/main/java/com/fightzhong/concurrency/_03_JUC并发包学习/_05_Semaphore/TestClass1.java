package com.fightzhong.concurrency._03_JUC并发包学习._05_Semaphore;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class TestClass1 {
	static int a = 0;
	public static void main (String[] args) throws InterruptedException {
		Lock lock = new Lock();
		CountDownLatch latch = new CountDownLatch( 2 );

		for ( int i = 0; i < 2; i ++ ) {
			new Thread( () -> {
				for ( int j = 0; j < 500; j ++ ) {
					try {
						lock.lock();
						a ++;
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						lock.unlock();
					}
					try {
						TimeUnit.MILLISECONDS.sleep( 1 );
					} catch (InterruptedException e) {}
				}
				latch.countDown();
			} ).start();
		}
		
		latch.await();
		System.out.println( a );
	}

}

class Lock {
	private Semaphore semaphore = new Semaphore( 1 );

	public void lock () throws InterruptedException {
		semaphore.acquire();
	}

	public void unlock () {
		semaphore.release();
	}
}
