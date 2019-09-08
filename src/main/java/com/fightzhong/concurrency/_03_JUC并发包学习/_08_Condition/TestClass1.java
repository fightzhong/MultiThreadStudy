package com.fightzhong.concurrency._03_JUC并发包学习._08_Condition;

import java.util.concurrent.TimeUnit;

public class TestClass1 {
	private static int data = 0;
	private static final Object MONITOR = new Object();
	private static int min = 0;
	private static int max = 100;


	public static void main (String[] args) {
		for ( int i = 0; i < 10; i ++ ){
			new Thread( () -> {
				while ( true ) {
					try {
						produce();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}, "Producer-" + i ).start();
		}

		for ( int i = 0; i < 6; i ++ ){
			new Thread( () -> {
				while ( true ) {
					try {
						consume();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}, "Consumer-" + i ).start();
		}

	}

	private static void produce () throws InterruptedException {
		synchronized ( MONITOR ) {
			while ( data >= max ) {
				MONITOR.wait();
			}

			Thread.sleep( 200 );

			data ++;
			System.out.println( "生产者["+ Thread.currentThread().getName() +"]生产了的数据为: " + data );
			MONITOR.notifyAll();
		}
	}

	private static void consume () throws InterruptedException {
		synchronized ( MONITOR ) {
			while ( data <= min ) {
				MONITOR.wait();
			}

			Thread.sleep( 200 );

			System.out.println( "消费者["+ Thread.currentThread().getName() +"]消费了的数据为: " + data-- );
			MONITOR.notifyAll();
		}
	}
}
