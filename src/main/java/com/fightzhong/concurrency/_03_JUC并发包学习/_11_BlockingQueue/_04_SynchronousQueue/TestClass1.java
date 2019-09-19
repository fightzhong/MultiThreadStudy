package com.fightzhong.concurrency._03_JUC并发包学习._11_BlockingQueue._04_SynchronousQueue;

import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public class TestClass1 {
	public static void main (String[] args) throws InterruptedException {
		SynchronousQueue<Integer> queue = new SynchronousQueue<>();
		// System.out.println( queue.offer( 10 ) );
		// System.out.println( queue.add( 10 ) );
		// System.out.println( queue.offer( 10 ) );
		// System.out.println( queue.poll() );
		// Executors.newScheduledThreadPool(1).schedule( () -> {
		// 	try {
		// 		System.out.println( "提取数据: " + queue.take() );
		// 	} catch (InterruptedException e) {
		// 		e.printStackTrace();
		// 	}
		// }, 3, TimeUnit.SECONDS );
		// queue.add( 10 );
		System.out.println( queue.size() );
	}
}
