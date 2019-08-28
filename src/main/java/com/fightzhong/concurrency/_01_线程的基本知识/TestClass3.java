package com.fightzhong.concurrency._01_线程的基本知识;

public class TestClass3 {
	public static void main (String[] args) {
		new Thread(() -> {
			System.out.println( "Runnable" );
		}){
			@Override
			public void run () {
				System.out.println( "Thread: " );
			}
		}.start();
		
		
	}
}
