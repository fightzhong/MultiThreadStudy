package com.fightzhong.concurrentcy_2.cache_line;

public class CacheLine4 {
	public static void main (String[] args) {
		long start = System.currentTimeMillis();

		long[][] arr = new long[1000000][8];
		for ( int i = 0; i < 8; i ++ ) {
			for ( int j = 0; j < 1000000; j ++ ) {
				arr[j][i] = i;
			}
		}

		long end = System.currentTimeMillis();

		System.out.println( "运行了: " + ( end - start ) + "毫秒" );
	}
}
