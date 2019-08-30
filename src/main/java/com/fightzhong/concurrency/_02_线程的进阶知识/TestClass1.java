package com.fightzhong.concurrency._02_线程的进阶知识;

public class TestClass1 {
	private volatile static int a = 0;
	public static void main (String[] args) {
		new Thread( () -> {
			while ( TestClass1.a < 10 ) {
				System.out.println( "Writer: " + TestClass1.a );
				TestClass1.a++;
				try {
					Thread.sleep( 500 );
				} catch (InterruptedException e) {

				}
			}
		}, "Writer" ).start();

		new Thread( () -> {
			int curVal = TestClass1.a;
			while ( curVal < 5 ) {
				if ( curVal != TestClass1.a ) {
					System.out.println( "RAEDER: " + TestClass1.a );
					curVal = TestClass1.a;
				}
			}
		}, "READER" ).start();

	}
}
