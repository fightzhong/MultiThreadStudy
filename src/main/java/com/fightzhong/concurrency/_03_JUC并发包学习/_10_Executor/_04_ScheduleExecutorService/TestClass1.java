package com.fightzhong.concurrency._03_JUC并发包学习._10_Executor._04_ScheduleExecutorService;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TestClass1 {
	public static void main (String[] args) throws ExecutionException, InterruptedException {
		final AtomicInteger count = new AtomicInteger( 1 );
		ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor( 0, (r) -> {
			Thread t = new Thread( r, "POOL-THRED-" + count.getAndIncrement() );
			return t;
		}, new ScheduledThreadPoolExecutor.AbortPolicy() );
		
		Future<Integer> result = scheduledExecutorService.schedule( () -> {
			System.out.println( "running..." );
			return 1;
		}, 5, TimeUnit.SECONDS );

		Integer integer = result.get();
		System.out.println( integer );
		System.out.println( "==============" );

	}

}
