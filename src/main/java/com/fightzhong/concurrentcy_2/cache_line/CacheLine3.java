package com.fightzhong.concurrentcy_2.cache_line;

import java.util.Arrays;

public class CacheLine3 {
	public static void main (String[] args) {
		long start = System.currentTimeMillis();

		long[][] arr = new long[1000000][8];
		for ( int i = 0; i < 1000000; i ++ ) {
			for ( int j = 0; j < 8; j ++ ) {
				arr[i][j] = i;
			}
		}

		long end = System.currentTimeMillis();

		System.out.println( "运行了: " + ( end - start ) + "毫秒" );
	}
}
