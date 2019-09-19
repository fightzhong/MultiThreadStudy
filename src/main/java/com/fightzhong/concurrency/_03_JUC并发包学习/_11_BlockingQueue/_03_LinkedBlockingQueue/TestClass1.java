package com.fightzhong.concurrency._03_JUC并发包学习._11_BlockingQueue._03_LinkedBlockingQueue;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TestClass1 {
	public static void main (String[] args) throws InterruptedException {
		testAdd();
	}

	public static void testAdd () throws InterruptedException {
		LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<>( 3 );
		queue.offer( 10 );
		queue.offer( 20 );
		queue.offer( 30 );
		queue.add( 30 );




	}
}
