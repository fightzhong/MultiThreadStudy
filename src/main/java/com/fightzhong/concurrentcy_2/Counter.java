package com.fightzhong.concurrentcy_2;

public class Counter {
	public static int i = 1;

	static synchronized void increase () {
		while ( i == 1 ) {
			try {
				Counter.class.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		i ++;
		Counter.class.notify();
		System.out.println( Thread.currentThread().getName() + ": " + Counter.i );
	}

	static synchronized void decrease () {
		while ( i == 0 ) {
			try {
				Counter.class.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		i --;
		Counter.class.notify();
		System.out.println( Thread.currentThread().getName() + ": " + Counter.i );
	}
}
