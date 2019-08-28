package com.fightzhong.concurrency._01_线程的基本知识.显示锁;

import java.util.Collection;

/**
 * 显示锁的接口
 */
public interface Lock {
	/**
	 * 用户调用的锁
	 */
	void lock () throws InterruptedException;

	/**
	 * 带延迟的锁, 如果超时了则抛出一个TimeOutException, 使得用户可以选择下一步怎么执行
	 * @param millis
	 */
	void lock (long millis) throws TimeOutException, InterruptedException;

	/**
	 * 解锁
	 */
	void unlock ();

	/**
	 * 获取阻塞线程队列
	 * @return
	 */
	Collection<Thread> getBlockedThreadQueue ();

	/**
	 * 获取阻塞的线程的个数
	 * @return
	 */
	int getSize ();

	/**
	 * 自定义的TimeOutException异常
	 */
	class TimeOutException extends Exception {
		public TimeOutException (String message) {
			super( message );
		}
	}
}
