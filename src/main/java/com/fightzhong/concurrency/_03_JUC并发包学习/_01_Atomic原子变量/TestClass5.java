package com.fightzhong.concurrency._03_JUC并发包学习._01_Atomic原子变量;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

public class TestClass5 {
	public static void main (String[] args) {
		AtomicStampedReference<Integer> i = new AtomicStampedReference<>( 10, 0 );
		new Thread( "线程一" ){
			@Override
			public void run () {
				int expect = i.getReference();
				int expectStamp = i.getStamp();
				try {
					sleep( 2000 );
				} catch (InterruptedException e) {}

				boolean success = i.compareAndSet( expect, 15, expectStamp, expectStamp + 1 );
				System.out.println( "线程一: " + success );
			}
		}.start();

		new Thread( "线程二" ){
			@Override
			public void run () {
				int expect = i.getReference();
				int expectStamp = i.getStamp();
				boolean success = i.compareAndSet( expect, expect, expectStamp, expectStamp );
				System.out.println( "线程二: " + success );

				// expect = i.getReference();
				// expectStamp = i.getStamp();
				// success = i.compareAndSet( expect, 10, expectStamp, expectStamp + 1 );
				// System.out.println( "线程二: " + success );
			}
		}.start();
	}
}
