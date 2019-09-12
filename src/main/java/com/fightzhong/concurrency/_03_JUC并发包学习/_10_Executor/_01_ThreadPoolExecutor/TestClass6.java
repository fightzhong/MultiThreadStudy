package com.fightzhong.concurrency._03_JUC并发包学习._10_Executor._01_ThreadPoolExecutor;

import com.fightzhong.concurrency._02_线程的进阶知识.异步任务的回调实现.Executor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestClass6 {
	public static void main (String[] args) {
		ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool( 1 );
		pool.setKeepAliveTime( 10, TimeUnit.SECONDS );
		pool.allowCoreThreadTimeOut( true );
		pool.execute( () -> {
			sleeping( 5 );
		} );

	}

	public static void sleeping (int second) {
		try {
			TimeUnit.SECONDS.sleep( second );
			System.out.println( "finished...." );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
