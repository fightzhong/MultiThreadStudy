package com.fightzhong.concurrency._02_线程的进阶知识.ThreadLocal.ThreadLocalTest;

import java.util.HashMap;

public class MyThreadLocal<T> {
	private HashMap<Thread, T> data = new HashMap<>();

	public void set (T t) {
		Thread thread = Thread.currentThread();
		data.put( thread, t );
	}

	public T get () {
		Thread thread = Thread.currentThread();
		T result = data.get( thread );

		if ( result == null ) {
			return initValue();
		}

		return result;
	}

	protected T initValue () {
		return null;
	}
}
