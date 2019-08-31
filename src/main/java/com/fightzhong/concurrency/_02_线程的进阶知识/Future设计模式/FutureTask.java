package com.fightzhong.concurrency._02_线程的进阶知识.Future设计模式;

@FunctionalInterface
public interface FutureTask<T> {
	// FutureTask, 将异步任务放入call方法中
	T call();
}
