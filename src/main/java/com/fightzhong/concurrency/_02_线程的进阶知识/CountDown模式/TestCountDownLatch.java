package com.fightzhong.concurrency._02_线程的进阶知识.CountDown模式;

import java.util.concurrent.CountDownLatch;

public class TestCountDownLatch {
	public static void main (String[] args) throws InterruptedException {
		CountDownLatch countDown = new CountDownLatch( 4 );

		for ( int i = 0; i < 4; i ++ ) {
			new Thread( () -> {
				System.out.println( Thread.currentThread().getName() + "开始执行任务" );

				try {
					Thread.sleep( 2000 );
				} catch (InterruptedException e) {}

				System.out.println( Thread.currentThread().getName() + "执行任务完毕" );

				countDown.countDown();
			}, "Thread-" + i ).start();
		}

		// 在里面的计数器大于0时会进入等待
		countDown.await();

		System.out.println( "线程任务执行完毕................................" );
		System.out.println( "主线程退出阻塞" );
	}
}
