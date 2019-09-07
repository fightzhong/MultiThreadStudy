package com.fightzhong.concurrency._03_JUC并发包学习._07_ReentrantReadWriteLock;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TestClass1 {
	public static void main (String[] args) throws InterruptedException {
		ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
		ArrayList<Integer> data = new ArrayList<>();
		// 获取读锁和写锁
		Lock readLock = readWriteLock.readLock();
		Lock writeLock = readWriteLock.writeLock();

		// 写线程
		new Thread( () -> {
			writeLock.lock();
			System.out.println( Thread.currentThread().getName() + ": 正在写入数据" );
			data.add( 66666 );
			// 通过sleep来模拟写操作需要一段时间
			try {
				Thread.sleep( 3000 );
			} catch (InterruptedException e) {}
			System.out.println( Thread.currentThread().getName() + ": 写入数据完成" );
			writeLock.unlock();
		}, "写线程" ).start();

		// 我们确保写线程先获取到锁, 因为读操作只有一个, 所以我们需要确保里面有数据
		Thread.sleep( 50 );

		// 读线程一
		new Thread( () -> {
			readLock.lock();
			System.out.println( Thread.currentThread().getName() + ": 读取数据 => " + data );
			try {
				// 通过sleep来模拟读操作需要一段时间
				Thread.sleep( 3000 );
			} catch (InterruptedException e) {}
			System.out.println( Thread.currentThread().getName() + "读取完成" );
			readLock.unlock();
		}, "读线程一" ).start();

		// 读线程二
		new Thread( () -> {
			readLock.lock();
			System.out.println( Thread.currentThread().getName() + ": 读取数据 => " + data );
			try {
				Thread.sleep( 3000 );
			} catch (InterruptedException e) {}
			System.out.println( Thread.currentThread().getName() + "读取完成" );
			readLock.unlock();
		}, "读线程二" ).start();
	}
}
