package com.fightzhong.concurrency._03_JUC并发包学习._09_Phaser;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

public class TestClass7 {
	public static void main (String[] args) throws InterruptedException {
		final Phaser phaser = new Phaser( 4 );
		// 创建四个线程去执行任务
		new Thread( () -> {
			System.out.println( Thread.currentThread().getName() + ": 开始执行任务" );
			try {
				TimeUnit.SECONDS.sleep( 3 );
			} catch (InterruptedException e) {}
			System.out.println( Thread.currentThread().getName() + ": 执行任务结束" );
			phaser.arriveAndAwaitAdvance();
			System.out.println( "continue/......" );
		} ).start();

		// 创建四个线程去执行任务
		new Thread( () -> {
			System.out.println( Thread.currentThread().getName() + ": 开始执行任务" );
			try {
				TimeUnit.SECONDS.sleep( 10 );
			} catch (InterruptedException e) {}
			System.out.println( Thread.currentThread().getName() + ": 执行任务结束" );
			phaser.arriveAndAwaitAdvance();
			System.out.println( "continue/......" );
		} ).start();

		Thread.sleep( 2000 );
		System.out.println( phaser.isTerminated() );
		phaser.forceTermination();
		System.out.println( phaser.isTerminated() );
	}
}
