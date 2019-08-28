package com.fightzhong.concurrency._01_线程的基本知识;

public class TestClass5 {
	public static void main (String[] args) throws InterruptedException {
		System.out.println( "main线程开始执行" );

		Thread t = new Thread() {
			@Override
			public void run () {
				
				Thread tt = new Thread( () -> {
					
					for ( int i = 0; i < 10; i ++ ) {
						System.out.println( Thread.currentThread().getName() + ": " + i );
						try {
							sleep( 1000 );
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
				}, "AAAAAA" );
				tt.setDaemon( false );
				tt.start();
				
				
				System.out.println( Thread.currentThread().getName() + ": 开始执行" );
				for ( int i = 0; i < 1000; i ++ ) {
					System.out.println( Thread.currentThread().getName() + " : " + i );
					try {
						sleep( 1000 );
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println( Thread.currentThread().getName() + ": 执行完毕" );
			}
		};

		t.setDaemon( true );
		t.start();
		Thread.sleep( 1000 );
		System.out.println( "main线程执行完毕" );
	}
}
