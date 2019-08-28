package com.fightzhong.concurrency._01_线程的基本知识.捕获线程异常;

public class ThreadExceptionCapture {
	public static void main (String[] args) throws InterruptedException {
		Thread.currentThread().setUncaughtExceptionHandler( ( thread, ex ) -> {
			System.out.println( "================Error Messsage Start===============" );
			System.out.println( "Error Thread: " + thread.getName() );

			// 获取错误的信息元素, 可以通过该元素的方法获取一个个信息
			StackTraceElement[] stackTrace = ex.getStackTrace();
			for ( StackTraceElement e: stackTrace ) {
				System.out.println( "File: " + e.getFileName() );
				System.out.println( "Method: " + e.getMethodName() );
				System.out.println( "LineNumber: " + e.getLineNumber() );
			}

			System.out.println( "================Error Messsage End===============" );
		} );

		try {
			Thread.sleep( 3000 );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		int a = 10 / 0;

		while ( true ) {
			System.out.println( "main线程在执行..........." );
			Thread.sleep( 1000 );
		}
	}
}
