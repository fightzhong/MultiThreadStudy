package com.fightzhong.concurrency._03_JUC并发包学习._09_Phaser;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

public class TestClass6 {
	public static void main (String[] args) {
		final Phaser phaser = new Phaser( 4 );
		// 创建四个线程去执行任务
		for ( int i = 0; i < 4; i ++ ) {
			new Thread( () -> {
				System.out.println( Thread.currentThread().getName() + ": 开始执行任务" );
				// 利用sleep来模拟执行任务需要一点时间
				try {
					TimeUnit.SECONDS.sleep( 3 );
				} catch (InterruptedException e) {}
				System.out.println( Thread.currentThread().getName() + ": 执行任务结束" );
				phaser.arriveAndAwaitAdvance();
			} ).start();
		}

		// 在主线程执行这行代码的时候, 由于还没有通过phaser.arriveAndAwaitAdvance();的方式
		// 使得parties增加到创建phaser对象时的4个, 所以此时getPhase方法获得的是0, 即第0阶段
		// 由于当前phaser对象所处的正好是第0阶段, 所以主线程会在这里进入阻塞, 直到因为当前线程
		// 进入下一个阶段后即1, 由于1和传入的0不相等, 主线程才会退出等待状态
		phaser.awaitAdvance( phaser.getPhase() );
		System.out.println( "四个线程的任务都执行完毕, 主线程对任务结果进行整合..." );
	}
}
