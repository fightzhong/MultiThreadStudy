package com.fightzhong.concurrency._01_线程的基本知识;

/*
	public static void main (String[] args) {
		ProducerAndConsumerService service = new ProducerAndConsumerService();

		Thread producer1 = new Thread( () -> service.produce(), "生产者1" );
		Thread producer2 = new Thread( () -> service.produce(), "生产者2" );
		Thread consumer1 = new Thread( () -> service.consume(), "消费者1" );
		Thread consumer2 = new Thread( () -> service.consume(), "消费者2" );

		producer1.start();
		producer2.start();
		consumer1.start();
		consumer2.start();
	}
 */
public class ProducerAndConsumerService {
	public static void main (String[] args) {
		ProducerAndConsumerService service = new ProducerAndConsumerService();

		Thread producer1 = new Thread( () -> service.produce(), "生产者1" );
		Thread producer2 = new Thread( () -> service.produce(), "生产者2" );
		Thread consumer1 = new Thread( () -> service.consume(), "消费者1" );
		Thread consumer2 = new Thread( () -> service.consume(), "消费者2" );

		producer1.start();
		producer2.start();
		consumer1.start();
		consumer2.start();
	}
	private int buffer = 0;
	private final Object LOCK = new Object();

	private void produce () {
		while ( true ) {
			synchronized ( LOCK ) {
				while ( buffer >= 1 ) {
					try {
						LOCK.wait();
					} catch (InterruptedException e) {}
				}
				buffer ++;
				LOCK.notify();
				System.out.println( Thread.currentThread().getName() + ": buffer -> " + buffer );
			}
		}
	}

	private void consume () {
		while ( true ) {
			synchronized ( LOCK ) {
				while ( buffer <= 0 ) {
					try {
						LOCK.wait();
					} catch (InterruptedException e) {}
				}

				System.out.println( Thread.currentThread().getName() + ": buffer -> " + buffer );
				buffer --;
				LOCK.notify();
			}
		}
	}
}





