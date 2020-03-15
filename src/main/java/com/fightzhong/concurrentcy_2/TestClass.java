package com.fightzhong.concurrentcy_2;

import java.util.concurrent.TimeUnit;

public class TestClass {
	private static int num = 0;

	public static void main (String[] args) {
		new Thread( () -> {
			System.out.println( "开始执行死循环" );
			while ( num == 0 );
			System.out.println( "结束执行死循环" );
		} ).start();

		new Thread( () -> {
			System.out.println( "开始休眠" );
			try {
				Thread.sleep( 3000 );
				num = 1;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println( "结束休眠" );
		} ).start();
	}
}
