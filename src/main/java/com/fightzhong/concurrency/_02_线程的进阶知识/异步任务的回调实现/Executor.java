package com.fightzhong.concurrency._02_线程的进阶知识.异步任务的回调实现;

public class Executor {
	public <T> void execute (FutureTask<T> task, Callback<T> callback) {
		// 利用一个新的线程来执行用户的异步任务
		new Thread( () -> {
			// 执行异步任务并获取结果
			T result = task.call();
			callback.callback( result );
		} ).start();

	}
}
