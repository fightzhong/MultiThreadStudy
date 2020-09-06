package com.fightzhong.concurrentcy_2.thread;

import sun.misc.Unsafe;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class TestClass1 {
	private static volatile int count = 0;

	private static CountDownLatch countDownLatch = new CountDownLatch( 3 );

	public static void main (String[] args) throws BrokenBarrierException, InterruptedException {
		// ReentrantLock reentrantLock = new ReentrantLock();
		// Thread thread = Thread.currentThread();
		// new Thread( () -> {
		// 	try {
		// 		Thread.sleep( 25000 );
		// 	} catch (InterruptedException e) {
		// 		e.printStackTrace();
		// 	}
		//
		// 	thread.interrupt();
		// } ).start();
		//
		// new Thread( () -> {
		// 	reentrantLock.lock();
		// 	try {
		// 		System.out.println( "锁开始持有60秒" );
		// 		Thread.sleep( 6000000 );
		// 	} catch (InterruptedException e) {
		// 		e.printStackTrace();
		// 	}
		//
		// 	reentrantLock.unlock();
		// 	System.out.println( "解锁开始" );
		//
		// } ).start();
		//
		// Thread.sleep( 30 );
		// reentrantLock.lock();
		// System.out.println( "主线程被打断了" );


	}
}
