package com.fightzhong.concurrency._03_JUC并发包学习._04_Exchanger;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

public class TestClass {
	public static void main (String[] args) {
		Exchanger<Object> exchanger = new Exchanger<>();

		Thread t1 = new Thread( () -> {
			try {
				Object mes = new Object();
				System.out.println( "线程一发送的信息: " + mes );
				Object result = exchanger.exchange( mes );
				System.out.println( "线程一收到线程二的信息: " + result );
			} catch (InterruptedException e) {}
		} );

		Thread t2 = new Thread( () -> {
			try {
				TimeUnit.SECONDS.sleep( 3 );
				Object mes = new Object();
				System.out.println( "线程二发送的信息: " + mes );
				Object result = exchanger.exchange( mes );
				System.out.println( "线程二收到线程一的信息: " + result );
			} catch (InterruptedException e) {}
		} );

		t1.start();
		t2.start();
	}
}
