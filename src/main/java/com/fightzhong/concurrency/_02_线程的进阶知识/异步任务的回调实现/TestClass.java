package com.fightzhong.concurrency._02_线程的进阶知识.异步任务的回调实现;

public class TestClass {
	public static void main (String[] args) throws InterruptedException {
		Executor executor = new Executor();
		executor.execute( () -> {
			// 异步任务, 利用sleep方法来模拟异步任务需要执行一段时间
			try {
				Thread.sleep( 5000 );
			} catch (InterruptedException e) {}

			// 返回结果
			return "Result...";
		}, (result) -> {
			System.out.println( result );
		} );

		// 在调用future之前是不会收到阻塞的, 因为future是立马返回的
		// 而异步任务是用另外一个线程来执行的, 这里用sleep来模拟主线程在继续执行
		System.out.println( "===========主线程在执行任务=========" );
		Thread.sleep( 3000 );
		System.out.println( "===========主线程执行任务完毕=========" );
	}
}
