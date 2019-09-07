package com.fightzhong.concurrency._03_JUC并发包学习._05_Semaphore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class TestClass5 {
	public static void main (String[] args) {
		Semaphore semaphore = new Semaphore( 1 );

		for ( int i = 0; i < 2; i ++ )
			new Thread( () -> {
				System.out.println( Thread.currentThread().getName() + "开始执行" );
				if ( semaphore.tryAcquire() ) {
					System.out.println( Thread.currentThread().getName() + "获取到一个信号量" );

					try {
						TimeUnit.SECONDS.sleep( 4 );
					} catch (InterruptedException e) {

					} finally {
						semaphore.release();
						System.out.println( Thread.currentThread().getName() + "释放一个信号量" );
					}

				} else {
					System.out.println( Thread.currentThread().getName() + "获取信号量失败" );
				}

				System.out.println( Thread.currentThread().getName() + "结束执行" );
			} ).start();

	}
}
