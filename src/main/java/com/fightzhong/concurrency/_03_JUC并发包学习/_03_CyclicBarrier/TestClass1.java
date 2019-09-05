package com.fightzhong.concurrency._03_JUC并发包学习._03_CyclicBarrier;

import java.util.concurrent.*;

public class TestClass1 {
	public static void main (String[] args) throws InterruptedException {
		CyclicBarrier cyclicBarrier = new CyclicBarrier( 3, () -> {
			System.out.println( "所有线程均到达目的地..............." );
		} );

		Thread t1 = new Thread(){
			@Override
			public void run () {
				try {
					Thread.sleep( 3000 );
				} catch (InterruptedException e) {}
				
				System.out.println( "线程一执行完毕, 到达目的地" );

				// 要求所有线程在执行完上述任务后从这里统一开始执行
				try {
					cyclicBarrier.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (BrokenBarrierException e) {
					e.printStackTrace();
				}
				System.out.println( "线程一继续执行" );
			}
		};

		Thread t2 = new Thread(){
			@Override
			public void run () {
				try {
					Thread.sleep( 5000 );
				} catch (InterruptedException e) {}
				System.out.println( "线程二执行完毕, 到达目的地" );
				// 要求所有线程在执行完上述任务后从这里统一开始执行
				try {
					cyclicBarrier.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (BrokenBarrierException e) {
					e.printStackTrace();
				}
				System.out.println( "线程二继续执行" );
			}
		};

		Thread t3 = new Thread(){
			@Override
			public void run () {
				try {
					Thread.sleep( 7000 );
				} catch (InterruptedException e) {}
				System.out.println( "线程三执行完毕, 到达目的地" );
				// 要求所有线程在执行完上述任务后从这里统一开始执行
				try {
					cyclicBarrier.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (BrokenBarrierException e) {
					e.printStackTrace();
				}
				System.out.println( "线程三继续执行" );

			}
		};

		t1.start();
		t2.start();
		t3.start();
	}
}
