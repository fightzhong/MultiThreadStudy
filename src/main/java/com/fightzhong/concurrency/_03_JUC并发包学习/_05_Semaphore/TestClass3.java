package com.fightzhong.concurrency._03_JUC并发包学习._05_Semaphore;

import java.util.Collection;
import java.util.concurrent.Semaphore;

public class TestClass3 {
	public static void main (String[] args) throws InterruptedException {
		MySemaphore semaphore = new MySemaphore( 2 );
		for ( int i = 0; i < 2; i ++ )
			new Thread( () -> {
				System.out.println( Thread.currentThread().getName() + "开始执行" );
				try {
					semaphore.acquire( 2 );
					System.out.println(  Thread.currentThread().getName() + "获得2个信号量" );
					Thread.sleep( 5000 );
				} catch (InterruptedException e) {

				} finally {
					semaphore.release( 1 );
					System.out.println(  Thread.currentThread().getName() + "释放1个信号量" );
				}
				System.out.println( Thread.currentThread().getName() + "结束执行" );
			} ).start();

		Thread.sleep( 6000 );
		System.out.println( semaphore.availablePermits() );

	}

	static class MySemaphore extends Semaphore {
		public MySemaphore (int permits) {
			super(permits);
		}

		public MySemaphore (int permits, boolean fair) {
			super(permits, fair);
		}

		public Collection<Thread> getWatingThread () {
			return this.getQueuedThreads();
		}
	}
}
