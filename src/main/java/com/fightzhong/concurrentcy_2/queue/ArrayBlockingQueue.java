package com.fightzhong.concurrentcy_2.queue;

import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ArrayBlockingQueue {
	private String[] container;

	private int putPosition;

	private int takePosition;

	private static final int DEFAULT_SIZE = 10;

	private static final String STRING_PLACEHOLDER = "a";

	private Lock lock;

	private Condition fullCondition;

	private Condition emptyCondition;

	public ArrayBlockingQueue () {
		this( DEFAULT_SIZE );
	}

	public ArrayBlockingQueue (int size) {
		this.container = new String[size];
		lock = new ReentrantLock();
		fullCondition = lock.newCondition();
		emptyCondition = lock.newCondition();
	}

	public void put () {
		lock.lock();
		try {
			while ( container[putPosition] != null ) {
				try {
					fullCondition.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			container[putPosition] = STRING_PLACEHOLDER;
			putPosition = ++putPosition == container.length ? 0 : putPosition;
			System.out.println( "put: " + Arrays.toString( container ) );;

			emptyCondition.signalAll();
		} finally {
			lock.unlock();
		}
	}

	public void take () {
		lock.lock();
		try {
			while ( container[takePosition] == null ) {
				try {
					emptyCondition.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			container[takePosition] = null;
			takePosition = ++takePosition == container.length ? 0 : takePosition;
			System.out.println( "take: " + Arrays.toString( container ) );;

			fullCondition.signalAll();
		} finally {
			lock.unlock();
		}
	}


}
