package com.fightzhong.concurrency._03_JUC并发包学习._09_Phaser;

import java.util.concurrent.Phaser;

public class TestClass2 {
	public static void main (String[] args) throws InterruptedException {
		Phaser phaser = new Phaser( 1 );

		new Thread( () -> {
			System.out.println( Thread.currentThread().getName() + "start ...." );
			try {
				Thread.sleep( 2000 );
			} catch (InterruptedException e) {}
			phaser.arriveAndAwaitAdvance();
			System.out.println( Thread.currentThread().getName() + "end ...." );
		} ).start();

		System.out.println( Thread.currentThread().getName() + "start ...." );
		Thread.sleep( 3000 );
		System.out.println( phaser.getRegisteredParties() );
		System.out.println( phaser.getUnarrivedParties() );
		System.out.println( phaser.getArrivedParties() );
		System.out.println( phaser.getPhase() );
		System.out.println( phaser.isTerminated() );
		phaser.arriveAndAwaitAdvance();

		System.out.println( phaser.getRegisteredParties() );
		System.out.println( phaser.getUnarrivedParties() );
		System.out.println( phaser.getArrivedParties() );
		System.out.println( phaser.getPhase() );
		System.out.println( phaser.isTerminated() );
		System.out.println( Thread.currentThread().getName() + "end ...." );
	}
}
