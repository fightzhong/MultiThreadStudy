package com.fightzhong.concurrency._03_JUC并发包学习._06_ReentrantLock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TestClass3 {
	public static void main (String[] args) throws InterruptedException {
		Lock lock = new ReentrantLock();
		
		Thread t1 = new Thread( () -> {
			try {
				lock.lockInterruptibly();
				System.out.println( Thread.currentThread().getName() + ": 获取到锁" );
				while ( true );
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		} );

		Thread t2 = new Thread( () -> {
			try {
				lock.lockInterruptibly();
				System.out.println( Thread.currentThread().getName() + ": 获取到锁" );
				while ( true );
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		} );

		t1.start();
		Thread.sleep( 1000 );
		t2.start();

		Thread.sleep( 3000 );
		t2.interrupt();
	}
}
