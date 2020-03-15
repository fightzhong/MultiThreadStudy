package com.fightzhong.concurrentcy_2.cache_line;

public class CacheLine1 {
	private static volatile long[] arr1 = new long[]{ 6666L, 6666L, 6666L, 6666L, 6666L, 6666L, 6666L, 6666L };
	private static volatile long[] arr2 = new long[]{ 6666L, 6666L, 6666L, 6666L, 6666L, 6666L, 6666L, 6666L };
	public static void main (String[] args) throws InterruptedException {

		long start = System.currentTimeMillis();


		Thread t1 = new Thread( () -> {
			for ( int i = 0; i <= 100000_0000; i ++ ) {
				arr1[0] = i;
			}
		} );

		Thread t2 = new Thread( () -> {
			for ( int i = 0; i <= 100000_0000; i ++ ) {
				arr2[0] = i;
			}
		} );

		t1.start();
		t2.start();

		t1.join();
		t2.join();

		long end = System.currentTimeMillis();
		System.out.println( "运行了: " + ( end - start ) + "毫秒" );

	}
}
