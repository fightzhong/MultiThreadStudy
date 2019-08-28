package com.fightzhong.concurrency._01_线程的基本知识.显示锁;

public class VersionTwoTestClass {
	public static void main (String[] args) {
		Lock lock = new MyLock();
		for ( int i = 0; i < 10; i ++ ) {
			new Thread( () -> {
				try {
					// 开锁
					lock.lock( 1000 );

					System.out.println( Thread.currentThread().getName() + ": 开始睡眠" );
					// 一系列逻辑
					Thread.sleep( 5_000 );

					System.out.println( Thread.currentThread().getName() + ": 结束睡眠" );
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (Lock.TimeOutException e) {
					System.out.println( Thread.currentThread().getName() + ": 等待超时, 结束运行..." );
				} finally {
					// 解锁
					lock.unlock();
				}
			} ).start();
		}
	}
}
