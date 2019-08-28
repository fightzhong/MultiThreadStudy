package com.fightzhong.concurrency._01_线程的基本知识;

public class TestClass8 {
	public static void main (String[] args) throws InterruptedException {
		System.out.println( "main 线程执行开始" );

		Thread t = new Thread() {
			@Override
			public void run () {
				try {
					Thread.sleep( 10_000 );
				} catch (InterruptedException e) {
					System.out.println( Thread.currentThread().getName() + "  接收到中断信号"  );
					e.printStackTrace();
				}
				
				for ( int i = 0; i < 10; i ++ ) {
					System.out.println( i );
				}
			}
		};

		t.start();
		
		Thread.sleep( 3000 );
		t.interrupt();
		System.out.println( "main 线程执行结束" );

	}
}
