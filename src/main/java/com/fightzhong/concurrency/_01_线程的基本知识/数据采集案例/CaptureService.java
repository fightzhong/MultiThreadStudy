package com.fightzhong.concurrency._01_线程的基本知识.数据采集案例;

import java.util.ArrayList;
import java.util.List;

public class CaptureService {
	private static int TOTAL_THREAD_COUNT = 10;
	private static int RUN_THREAD_COUNT = 0;
	private static int MAX_RUN_THREAD_COUNT = 5;
	private static final Object LOCK = new Object();

	public static void main (String[] args) {
		List<Thread> allThread = new ArrayList<>();

		for ( int i = 0; i < TOTAL_THREAD_COUNT; i ++ ) {
			Thread t = createCaptureThread( "线程" + (i + 1) );
			t.start();
			allThread.add( t );
		}

		for ( Thread t: allThread ) {
			try {
				System.out.println( "===================" );
				t.join();
			} catch (InterruptedException e) {}
		}
		
		System.out.println( "main线程执行结束............" );
	}

	private static Thread createCaptureThread (String threadName) {
		return new Thread( () -> {
			synchronized ( LOCK ) {
				while ( RUN_THREAD_COUNT >= MAX_RUN_THREAD_COUNT ) {
					try {
						LOCK.wait();
					} catch (InterruptedException e) {}
				}
				RUN_THREAD_COUNT ++;
			}

			System.out.println( Thread.currentThread().getName() + ": 正在执行任务" );

			try {
				Thread.sleep( 10000 );
			} catch (InterruptedException e) {}

			System.out.println( Thread.currentThread().getName() + ": 任务执行完毕" );

			synchronized ( LOCK ) {
				RUN_THREAD_COUNT --;
				LOCK.notifyAll();
			}

		}, threadName );
	}


}
