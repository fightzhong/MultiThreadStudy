package com.fightzhong.concurrency._01_线程的基本知识;

public class TestClass4 {
	public static void main (String[] args) {
		new Thread( null, new Runnable() {
			private int count = 0;
			@Override
			public void run () {
				try {
					test();
				} catch ( StackOverflowError e ) {
					System.out.println( count );
				} catch ( Throwable t ) {
					t.printStackTrace();
				}
			}

			private void test () {
				count++;
				test();
			}
		}, "MyThread..", 1 ).start();
	}
}
