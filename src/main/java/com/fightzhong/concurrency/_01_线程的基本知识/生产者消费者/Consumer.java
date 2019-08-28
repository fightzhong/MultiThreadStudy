package com.fightzhong.concurrency._01_线程的基本知识.生产者消费者;

public class Consumer {
	private int[] buffer;
	private Object LOCK;

	public Consumer (int[] buffer, Object LOCK) {
		this.buffer = buffer;
		this.LOCK = LOCK;
	}
	
	public void consume () {
		while ( true ) {
			synchronized ( LOCK ) {
				while ( buffer[0] <= 0 ) {
					try {
						LOCK.wait();
					} catch (InterruptedException e) {}
				}

				System.out.println( Thread.currentThread().getName() + " : 消费了一个产品 : " + buffer[0] );
				buffer[0]--;
				LOCK.notify();
			}

		}
	}
}
