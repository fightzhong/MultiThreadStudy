package com.fightzhong.concurrency._03_JUC并发包学习._06_ReentrantLock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TestClass {
	public static void main (String[] args) throws InterruptedException {
		ReentrantLock lock = new ReentrantLock();

		Thread t1 = new Thread( () -> {
			try {
				boolean b = lock.tryLock( 1, TimeUnit.SECONDS );
				System.out.println( Thread.currentThread().getName() + ": " + b );
				System.out.println( Thread.currentThread().getName() + "获取到锁" );
				while ( true ) {

				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
				System.out.println( Thread.currentThread().getName() + "释放锁" );
			}
		}, "线程一" );

		Thread t2 = new Thread( () -> {
			try {
				boolean b = lock.tryLock( 10, TimeUnit.SECONDS );
				System.out.println( Thread.currentThread().getName() + ": " + b );
				System.out.println( Thread.currentThread().getName() + "获取到锁" );
				while ( true ) {

				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
				System.out.println( Thread.currentThread().getName() + "释放锁" );
			}
		}, "线程二" );

		t1.start();
		Thread.sleep( 1000 );
		t2.start();

		Thread.sleep( 3000 );
		t2.interrupt();

	}
}
