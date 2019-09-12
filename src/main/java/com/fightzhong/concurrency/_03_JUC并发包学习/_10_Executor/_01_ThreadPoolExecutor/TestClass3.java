package com.fightzhong.concurrency._03_JUC并发包学习._10_Executor._01_ThreadPoolExecutor;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TestClass3 {
	public static void main (String[] args) {
		// 定义一个原子整型变量, 用于定义第几个线程的创建
		final AtomicInteger count = new AtomicInteger( 1 );
		// 最小的线程个数
		int corePoolSize = 1;
		// 最大的线程个数
		int maximumPoolSize = 3;
		// 对于多余corePoolSize个数的线程, 其最大的空闲时间为60
		long keepAliveTime = 60L;
		// 描述上面的60基数是秒
		TimeUnit unit = TimeUnit.SECONDS;
		// 阻塞队列
		BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<>();
		// 工厂方式创建线程的过程
		ThreadFactory threadFactory = (runnable) -> {
			// 线程名字的前缀
			String namePrefix = "Thread...";
			// 通过提供的Runnable接口创建一个线程
			Thread t = new Thread( runnable, namePrefix + count.getAndIncrement() );
			t.setDaemon( true );
			// 将创建的线程返回
			return t;
		};
		// 拒绝策略, 采用线程池类自带的拒绝策略, 也可以自定义(同ThreadFactory一样是函数式接口)
		RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();

		ThreadPoolExecutor pool = new ThreadPoolExecutor( corePoolSize, maximumPoolSize, keepAliveTime, unit,
																		workQueue, threadFactory, handler );

	}

	public static void sleeping (int seconds) {
		try {
			TimeUnit.SECONDS.sleep( seconds );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println( "sleeping over... " + Thread.currentThread().getName() );
	}

	public static void look (ThreadPoolExecutor pool) {
		int poolSize = pool.getPoolSize();
		int queueSize = pool.getQueue().size();
		int activeCount = pool.getActiveCount();
		System.out.println( "poolSize: " + pool.getPoolSize() );
		System.out.println( "queueSize: " + pool.getQueue().size() );
		System.out.println( "activeCount: " + pool.getActiveCount() );
		System.out.println( "========================================" );
		while ( true ) {
			if ( activeCount != pool.getActiveCount() || poolSize != pool.getPoolSize() || queueSize != pool.getQueue().size() ) {
				System.out.println( "poolSize: " + pool.getPoolSize() );
				System.out.println( "queueSize: " + pool.getQueue().size() );
				System.out.println( "activeCount: " + pool.getActiveCount() );
				System.out.println( "========================================" );
				poolSize = pool.getPoolSize();
				queueSize = pool.getQueue().size();
				activeCount = pool.getActiveCount();
			}
		}
	}
}
