package com.fightzhong.concurrency._02_线程的进阶知识.CountDown模式;

public class MyCountDown {
	private int count;

	// 用户传进来一个count计数器
	public MyCountDown (int count) {
		this.count = count;
	}

	// 每次count减减的时候必须是同步的, 同时减减后需要唤醒等待的线程(调用await方法后发现count大于0而进入等待的线程)
	public synchronized void countDown () {
		count --;
		this.notifyAll();
	}

	public synchronized void await () {
		while ( count > 0 ) {
			try {
				this.wait();
			} catch (InterruptedException e) {}
		}
	}
}
