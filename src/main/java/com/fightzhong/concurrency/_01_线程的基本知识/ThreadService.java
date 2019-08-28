package com.fightzhong.concurrency._01_线程的基本知识;

public class ThreadService {
	private Runnable runnable;    // 用户要执行的Runnable接口实现类线程
	private Thread outThread;     // 外部线程
	private boolean runnableThreadFinish = false; // 守护线程是否执行完毕

	// 对用户传进来的Runnable接口创建一个守护线程去执行
	public void executeThread (Runnable ra) {
		if ( ra == null )
			throw new NullPointerException( "ra不允许为空" );

		// 如果runnable不是空的, 那么就不能再传入一个线程,
		// 否则之前的线程就可能会因为没有shutdown而继续执行下去
		if ( runnable != null )
			throw new UnsupportedOperationException( "已经存在一个线程在执行...." );

		runnable = ra;

		// 初始化一个外部线程, 然后将用户要执行的Runnable接口的线程作为外部线程的守护线程来执行
		// 之后将守护线程join到外部线程中, 从而可以达到通过终止外部线程的方式来使得守护线程终止
		outThread = new Thread( () -> {
			Thread daemonThread = new Thread( ra );
			daemonThread.setDaemon( true );
			daemonThread.start();

			// 我们需要将这个守护线程放入到outThread中去同步执行, 如果没有放入的话
			// outThread会很快执行完毕的, 然后就会导致守护线程的终止
 			try {
				daemonThread.join();
			   runnableThreadFinish = true;
			} catch (InterruptedException e) {
				System.out.println( "线程执行完毕" );
			}
		} );

		outThread.start();
	}

	// 规定再millis秒后就终止线程(通过终止外部线程来终止守护线程)
	public void shutdownThread (long millis) {
		if ( runnable == null )
			throw new NullPointerException( "未传入一个Runnable接口实现类" );

		long startTime = System.currentTimeMillis();
		long endTime = System.currentTimeMillis();
		// 守护线程没有执行完毕就要终止
		while ( !runnableThreadFinish ) {
			if ( endTime - startTime >= millis ) {
				// 通过终止外部线程的join状态来终止守护线程
				outThread.interrupt();
				runnableThreadFinish = true;
			} else {
				endTime = System.currentTimeMillis();
			}
		}

		// 线程执行完毕后设置runable为空
		runnable = null;
	}
}
