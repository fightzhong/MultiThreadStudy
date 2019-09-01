package com.fightzhong.concurrency._02_线程的进阶知识.ThreadLocal.ThreadContextTest;

public class TestClass {
	private static QueryNameAction queryNameAction = new QueryNameAction();
	private static QueryIdAction queryIdAction = new QueryIdAction();
	public static void main (String[] args) throws InterruptedException {
		for ( int i = 0; i < 5; i ++ ) {
			Thread t = new Thread() {
				@Override
				public void run () {
					System.out.println( "==============" + Thread.currentThread().getName() + "==============" );
					queryNameAction.execute();
					System.out.println( "====查询名字结束====" );
					queryIdAction.execute();
					System.out.println( "====查询id结束====" );
					// Context context = ActionContext.getContext();
					ThreadLocal<Context> threadLocal = new ThreadLocal<>();
					Context context = threadLocal.get();
					System.out.println( "查询结果: " + context );
					System.out.println( "==============" + Thread.currentThread().getName() + "==============" );
					System.out.println();
					System.out.println();
				}
			};

			t.start();
			t.join();
		}

	}
}
