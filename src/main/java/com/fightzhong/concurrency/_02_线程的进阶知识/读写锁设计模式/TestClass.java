package com.fightzhong.concurrency._02_线程的进阶知识.读写锁设计模式;

import java.util.Random;

public class TestClass {
	public static void main (String[] args) {
		ReadWriteLock lock = new ReadWriteLock(); // 读写锁
		String[] data = new String[]{"a"};  // 共享资源(临界资源)
		// 模拟许多读者进程和少量写者进程的情况
		new ReaderThread( data, "读者进程一", lock ).start();
		new ReaderThread( data, "读者进程二", lock ).start();
		new ReaderThread( data, "读者进程三", lock ).start();
		new ReaderThread( data, "读者进程四", lock ).start();
		new ReaderThread( data, "读者进程五", lock ).start();

		new WriterThread( data, "写者进程一", lock ).start();
	}
}

class ReaderThread extends Thread {
	private String[] data;        // 读者写者共享资源
	private ReadWriteLock lock;   // 读写锁(所有的读者进程和写者进程必须共享一把锁)
	public ReaderThread (String[] data, String name, ReadWriteLock lock) {
		super(name);
		this.data = data;
		this.lock = lock;
	}

	@Override
	public void run () {
		while ( true ) {
			try {
				// 获取读锁
				lock.readLock();

				/********************临界区操作**********************/
				System.out.println( Thread.currentThread().getName() + ": " + data[0] );
				Thread.sleep( 1000 );
				/********************临界区操作**********************/
			} catch (InterruptedException e) {} finally {
				// 释放读锁
				lock.readUnlock();
			}

		}
	}
}

class WriterThread extends Thread {
	// 写者进程随机从这些字符中选择一个写入到字符串中
	private static char[] chars = new char[]{ 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
	'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',  'z' };

	private String[] data;       // 读者写者共享资源
	private ReadWriteLock lock;  // 读写锁(所有的读者进程和写者进程必须共享一把锁)

	public WriterThread (String[] data, String name, ReadWriteLock lock) {
		super(name);
		this.data = data;
		this.lock = lock;
	}

	@Override
	public void run () {
		// 一直写
		while ( true ) {
			try {
				// 获取写锁
				lock.writeLock();

				/********************临界区操作**********************/
				System.out.println( "================================正在写入数据================================" );
				char c = chars[ new Random().nextInt( 26 ) ];

				System.out.println( Thread.currentThread().getName() + ": 写入数据[ " + c + " ]" );
				data[0] += c;
				System.out.println( "================================写入数据完毕================================" );
				Thread.sleep( 3000 );
				/********************临界区操作**********************/
			} catch (InterruptedException e) {} finally {
				// 释放写锁
				lock.writeUnlock();
			}
		}
	}
}
