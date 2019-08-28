package com.fightzhong.concurrency._01_线程的基本知识.生产者消费者;

public class Producer {
	// 用于存放生产的数据, 消费者同样也需要从这个里面取数据, 所以这个buffer必须从外面传进来
	// 以至于生产者和消费者操作的是同一个buffer
	private int[] buffer;
	private Object LOCK; // 消费者和生产者共同持有的锁

	public Producer (int[] buffer, Object LOCK) {
		this.buffer = buffer;
		this.LOCK = LOCK;
	}

	public void produce () {
		while ( true ) {
			synchronized ( LOCK ) {
				while ( buffer[0] >= 1 ) {
					try {
						LOCK.wait();
					} catch (InterruptedException e) {}
				}

				buffer[0]++;
				System.out.println( Thread.currentThread().getName() + " : 生产了一个产品 : " + buffer[0] );
				LOCK.notify();
			}
		}
	}
}
