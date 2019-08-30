package com.fightzhong.concurrency._02_线程的进阶知识.观察者模式观察线程的状态;

/**
 * 线程的状态信息类
 */
public class ThreadMessage {
	private Thread thread;     // 线程
	private ThreadState state; // 线程的状态
	private Throwable error;   // 线程发生的错误

	public ThreadMessage (Thread thread, ThreadState state, Throwable error) {
		this.thread = thread;
		this.state = state;
		this.error = error;
	}

	public Thread getThread () {
		return thread;
	}

	public ThreadState getState () {
		return state;
	}

	public Throwable getError () {
		return error;
	}

	/**
	 * RUNNING: 表示线程正在运行
	 * BLOCK: 表现线程调用了wait方法或者争夺同步锁失败进入阻塞
	 * EXCEPTION: 表示线程中出现了异常或者错误
	 * DONE: 表示线程正常结束
	 */
	enum ThreadState {
		RUNNING, BLOCK, EXCEPTION, DONE;
	}
}
