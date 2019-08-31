package com.fightzhong.concurrency._02_线程的进阶知识.异步任务的回调实现;

@FunctionalInterface
public interface FutureTask<T> {
	T call();
}
