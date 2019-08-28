package com.fightzhong.concurrency._01_线程的基本知识.线程组;

import java.util.Arrays;

public class TestClass {
	public static void main (String[] args) throws InterruptedException {
		ThreadGroup group1 = new ThreadGroup( "group1" );
		ThreadGroup group2 = new ThreadGroup( group1, "group1" );
		ThreadGroup group3 = new ThreadGroup( group2, "group1" );

		Thread t1 = new Thread( group1, () -> {
			try {
				Thread.sleep( 5000 );
			} catch (InterruptedException e) {
				System.out.println( Thread.currentThread().getName() + "被中断了" );
			}
			System.out.println( Thread.currentThread().getName() );
		}, "线程一" );
		t1.setDaemon( true );
		t1.start();

		Thread t2 = new Thread( group2, () -> {
			System.out.println( Thread.currentThread().getName() + "" );
		}, "线程二" );
		t2.start();

		Thread t3 = new Thread( group3, () -> {
			System.out.println( Thread.currentThread().getName() + " " );
		}, "线程三" );
		t3.start();

		Thread.sleep( 1000 );

	}
}
