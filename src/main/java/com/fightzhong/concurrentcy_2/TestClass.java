package com.fightzhong.concurrentcy_2;

import com.fightzhong.concurrentcy_2.queue.ArrayBlockingQueue;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TestClass {
	private static Random random = new Random();
	public static void main (String[] args) {
		ArrayBlockingQueue queue = new ArrayBlockingQueue();
		for ( int i = 0; i < 28; i ++ ) {
			new Thread( () -> {
				// while ( true ) {
					queue.put();
					try {
						Thread.sleep( random.nextInt( 1000 ) );
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				// }
			} ).start();
		}

		for ( int i = 0; i < 30; i ++ ) {
			new Thread( () -> {
				// while ( true ) {
					queue.take();
					try {
						Thread.sleep( random.nextInt( 1000 ) );
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				// }
			} ).start();
		}


	}
}
