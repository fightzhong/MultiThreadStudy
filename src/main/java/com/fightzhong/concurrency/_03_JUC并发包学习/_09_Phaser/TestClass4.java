package com.fightzhong.concurrency._03_JUC并发包学习._09_Phaser;

import java.util.concurrent.Phaser;

public class TestClass4 {
	public static void main (String[] args) {
		final Phaser phaser =  new Phaser( 2 );
		
		new Thread( () -> {
			System.out.println( Thread.currentThread().getName() + "  start  ..." );

			try {
				Thread.sleep( 2000 );
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			phaser.arriveAndAwaitAdvance();

			System.out.println( Thread.currentThread().getName() + "  end  ..." );
		} ).start();

		new Thread( () -> {
			System.out.println( Thread.currentThread().getName() + "  start  ..." );

			try {
				Thread.sleep( 10000 );
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			phaser.arriveAndAwaitAdvance();

			System.out.println( Thread.currentThread().getName() + "  end  ..." );
		} ).start();

		System.out.println( "getPhase: " + phaser.getPhase() );
		phaser.awaitAdvance( phaser.getPhase() );
		System.out.println( "getPhase: " + phaser.getPhase() );
	}
}
