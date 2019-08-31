package com.fightzhong.concurrency._02_线程的进阶知识.Future设计模式;

public class Executor {
	/**
	 * 通过Executor类的excutor方法, 用户将异步任务传入进来, 然后该方法先创建一个Future类,
	 * 并把这个future类实例返回, 开启一个线程去执行异步任务, 这样主线程中
	 * new Executor().excutor(task)这样的, 调用就不会收到阻塞, 同时也能拿到future类实例,
	 * 只不过类实例中的result还是null, 当这个开启的线程执行完毕后, 会调用future.setResult()
	 * 方法将结果设置进去, 从而更新future实例中result
	 * @param task
	 * @param <T>
	 * @return
	 */
	public <T> Future<T> execute (FutureTask<T> task) {
		Future future = new Future();

		new Thread( () -> {
			T result = task.call();
			future.setResult( result );
		} ).start();

		return future;
	}
}
