package com.fightzhong.concurrency._02_线程的进阶知识.Future设计模式;

public class Future<T> {
	private T result; // 异步任务的结果
	// 初始的时候结果肯定是不存在的, 当异步任务执行完毕调用setResult方法后才能将finish设置为true
	private boolean finish = false;

	// 外部将结果放入Future类中
	public void setResult (T result) {
		// 为了能够在结果更新后去唤醒那些提前获取结果的线程, 即为了能
		// 够调用this.notifyAll, 我们需要将这些代码放入this对象锁中
		synchronized ( this ) {
			this.result = result;
			// 设置为true, 同时唤醒那些提前去拿结果的线程
			this.finish = true;
			this.notifyAll();
		}
	}

	// 用户获取结果
	public T get() {
		synchronized ( this ) {
			// 当结果不存在的时候, 调用get方法的线程应该进入等待状态
			// 直到异步任务结束更新了结果才能被唤醒
			while ( !finish ) {
				try {
					this.wait();
				} catch (InterruptedException e) {}
			}
		}

		return result;
	};
}
