package com.fightzhong.concurrency._03_JUC并发包学习._10_Executor._04_ScheduleExecutorService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class TestClass2 {
	public static void main (String[] args) {
		ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(3 );
		AtomicLong start = new AtomicLong( System.currentTimeMillis() );
		scheduledExecutorService.scheduleAtFixedRate( () -> {
			try {
				Thread.sleep( 10000 );
			} catch (InterruptedException e) {}
			long end = System.currentTimeMillis();
			long time = ( end - start.get() ) / 1000;
			System.out.println( "Hello: " + Thread.currentThread().getName() + "   => " + time );
			start.set( end );
		}, 0, 3, TimeUnit.SECONDS );
	}
}
