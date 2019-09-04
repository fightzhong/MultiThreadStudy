package com.fightzhong.concurrency._03_JUC并发包学习._01_Atomic原子变量;

import java.util.concurrent.atomic.AtomicInteger;

public class TestClass4 {
	public static void main (String[] args) {
		AtomicInteger i = new AtomicInteger( 10 );
		new Thread( "线程一" ){
			@Override
			public void run () {
				int expect = i.get();
				try {
					sleep( 2000 );
				} catch (InterruptedException e) {}

				boolean success = i.compareAndSet( expect, 15);
				System.out.println( "线程一: " + success );

			}
		}.start();

		new Thread( "线程二" ){
			@Override
			public void run () {
				int expect = i.get();
				boolean success = i.compareAndSet( expect, 15);
				System.out.println( "线程二: " + success );

				expect = i.get();
				success = i.compareAndSet( expect, 10 );
				System.out.println( "线程二: " + success );
			}
		}.start();
	}
}
