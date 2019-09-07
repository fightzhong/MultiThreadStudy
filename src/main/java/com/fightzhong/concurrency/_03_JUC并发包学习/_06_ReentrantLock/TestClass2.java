package com.fightzhong.concurrency._03_JUC并发包学习._06_ReentrantLock;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

public class TestClass2 {
	public static void main (String[] args) {
		Lock lock = new ReentrantLock();
		for ( int i = 0; i < 2; i ++ ) {
			new Thread( () -> {
				try {
					lock.lock();
					System.out.println( Thread.currentThread().getName() + "获取到锁" );
					while ( true );
				} finally {
					lock.unlock();
					System.out.println( Thread.currentThread().getName() + "释放锁" );
				}
			} ).start();
		}

		for ( int i = 0; i < 2; i ++ ) {
			new Thread( () -> {
				synchronized (TestClass2.class) {
					System.out.println( Thread.currentThread().getName() + "获取到锁" );
					while ( true );
				}
			} ).start();
		}
	}
}
