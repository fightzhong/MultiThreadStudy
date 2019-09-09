package com.fightzhong.concurrency._03_JUC并发包学习._09_Phaser;

import java.util.concurrent.Phaser;

public class TestClass3 {
	public static void main (String[] args) {
		Phaser phaser = new Phaser(4);

		for ( int i = 0; i < 3; i ++ )
			new Thread( () -> {
				System.out.println( Thread.currentThread().getName() + "  start..." );
				try {
					Thread.sleep( 3000 );
				} catch (InterruptedException e) {}
				phaser.arrive();
				System.out.println( Thread.currentThread().getName() + "继续执行第二阶段内容" );
				try {
					Thread.sleep( 3000 );
				} catch (InterruptedException e) {}
				System.out.println( Thread.currentThread().getName() + "  end..." );
			} ).start();

		phaser.arriveAndAwaitAdvance();
		System.out.println( "所有线程第一阶段任务执行完毕" );

	}
}
