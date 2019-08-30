package com.fightzhong.concurrency._02_线程的进阶知识.观察者模式观察线程的状态;

public class TestClass {
	public static void main (String[] args) {
		// 定义一个观察者, 用于处理线程状态发生改变的信息
		ObserverInterface observer = new ConcreteObserver();

		// 开启一个线程, 线程的执行代码由ObservedRunnable实现类提供
		new Thread(new ObservedRunnable( observer ) {
			@Override
			public void run () {
				// 通知观察者为运行状态
				notifyAllOfObserver( new ThreadMessage( Thread.currentThread(), ThreadMessage.ThreadState.RUNNING, null ) );
				try {
					Thread.sleep( 3000 );
				} catch (InterruptedException e) {
					// 如果线程的休眠状态出现了中断则通知观察者为异常处理状态
					notifyAllOfObserver( new ThreadMessage( Thread.currentThread(), ThreadMessage.ThreadState.EXCEPTION, e ) );
				}

				// 出现了错误, 通知观察者为Exception状态
				try {
					int i = 1 / 0;
				} catch ( Throwable e ) {
					notifyAllOfObserver( new ThreadMessage( Thread.currentThread(), ThreadMessage.ThreadState.EXCEPTION, e ) );
				}

				// 线程执行结束, 通知观察者为终止状态
				notifyAllOfObserver( new ThreadMessage( Thread.currentThread(), ThreadMessage.ThreadState.DONE, null ) );
			}
		}).start();
	}
}
