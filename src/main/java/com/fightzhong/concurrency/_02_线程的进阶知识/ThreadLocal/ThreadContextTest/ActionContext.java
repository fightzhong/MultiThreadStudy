package com.fightzhong.concurrency._02_线程的进阶知识.ThreadLocal.ThreadContextTest;

public class ActionContext {
	private static ThreadLocal<Context> threadLocal = new ThreadLocal<Context>() {
		@Override
		protected Context initialValue () {
			return new Context();
		}
	};

	public static Context getContext () {
		return threadLocal.get();
	}
}
