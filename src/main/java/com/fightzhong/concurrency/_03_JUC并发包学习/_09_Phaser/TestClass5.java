package com.fightzhong.concurrency._03_JUC并发包学习._09_Phaser;

import java.util.concurrent.Phaser;

public class TestClass5 {
	public static void main (String[] args) {
		final Phaser phaser = new Phaser( 1 );
		System.out.println( phaser.getPhase() );
		phaser.arriveAndAwaitAdvance();

		System.out.println( phaser.getPhase() );
		phaser.arriveAndAwaitAdvance();

		System.out.println( phaser.getPhase() );
		phaser.arriveAndAwaitAdvance();
	}
}
