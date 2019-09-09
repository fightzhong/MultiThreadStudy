package com.fightzhong.concurrency._03_JUC并发包学习._09_Phaser;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Phaser;

public class TestClass1 {

	public static void main (String[] args) throws InterruptedException, BrokenBarrierException {
		testAthleteCompetitionByPhaser();
	}

	public static void testAthleteCompetitionByPhaser () {
		final Random random = new Random();
					final Phaser phaser = new Phaser();

					for ( int i = 0; i < 5; i ++ ) {
						new Thread( () -> {
							phaser.register();
							try {
								// running
								System.out.println( Thread.currentThread().getName() + "..start..running" );
								Thread.sleep( random.nextInt( 3000 ) + 000 );
								System.out.println( Thread.currentThread().getName() + "..end..running" );
								phaser.arriveAndAwaitAdvance();

					// jumping
					System.out.println( Thread.currentThread().getName() + "..start..jumping" );
					Thread.sleep( random.nextInt( 3000 ) + 000 );
					System.out.println( Thread.currentThread().getName() + "..end..jumping" );
					phaser.arriveAndAwaitAdvance();

					// swimming
					System.out.println( Thread.currentThread().getName() + "..start..swimming" );
					Thread.sleep( random.nextInt( 3000 ) + 000 );
					System.out.println( Thread.currentThread().getName() + "..end..swimming" );
					phaser.arriveAndAwaitAdvance();
				} catch (InterruptedException e) {
				}
			} ).start();
		}


	}

	public static void testAthleteCompetition () {
		final Random random = new Random();
		final CyclicBarrier cyclicBarrier = new CyclicBarrier( 5 );

		for ( int i = 0; i < 5; i ++ ) {
			new Thread( () -> {
				try {
					// running
					System.out.println( Thread.currentThread().getName() + "..start..running" );
					Thread.sleep( random.nextInt( 3000 ) + 000 );
					System.out.println( Thread.currentThread().getName() + "..end..running" );
					cyclicBarrier.await();
					
					System.out.println( "===============================================" );
					// jumping
					System.out.println( Thread.currentThread().getName() + "..start..jumping" );
					Thread.sleep( random.nextInt( 3000 ) + 000 );
					System.out.println( Thread.currentThread().getName() + "..end..jumping" );
					cyclicBarrier.await();

					System.out.println( "===============================================" );

					// swimming
					System.out.println( Thread.currentThread().getName() + "..start..swimming" );
					Thread.sleep( random.nextInt( 3000 ) + 000 );
					System.out.println( Thread.currentThread().getName() + "..end..swimming" );
					cyclicBarrier.await();
				} catch (InterruptedException e) {
				} catch (BrokenBarrierException e) {
				}
			} ).start();
		}


	}

	public static void testCyclicBarrier () throws BrokenBarrierException, InterruptedException {
		final Random random = new Random();
		final CyclicBarrier cyclicBarrier = new CyclicBarrier( 6 );

		for ( int i = 0; i < 5; i ++ ) {
			new Thread( () -> {
				System.out.println( Thread.currentThread().getName() + "..start.." );
				try {
					Thread.sleep( random.nextInt( 3000 ) + 000 );
					System.out.println( Thread.currentThread().getName() + "..end.." );
					cyclicBarrier.await();
				} catch (InterruptedException e) {
				} catch (BrokenBarrierException e) {
				}
			} ).start();
		}

		cyclicBarrier.await();
		System.out.println( "all tasks have finished..." );
	}

	public static void testCountDownLatch () throws InterruptedException {
		final Random random = new Random();
		final CountDownLatch countDownLatch = new CountDownLatch( 5 );
		for ( int i = 0; i < 5; i ++ ) {
			new Thread( () -> {
				System.out.println( Thread.currentThread().getName() + "..start.." );
				try {
					Thread.sleep( random.nextInt( 3000 ) + 1000 );
				} catch (InterruptedException e) {}
				System.out.println( Thread.currentThread().getName() + "..end.." );
				countDownLatch.countDown();
			} ).start();
		}

		countDownLatch.await();
		System.out.println( "all the task have finished..." );
	}
}
