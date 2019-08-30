package com.fightzhong.concurrency._02_线程的进阶知识.观察者模式观察线程的状态;

/**
 * 可被观察的Runnable抽象类, 对Runnable接口进行加强, 增加一个观察者对象,
 * 这里尽量用抽象类, 而不是接口, 原因是在里面要定义变量ObserverInterface实例
 * (接口中定义的变量必须是初始化的, 而我们这个实例可能有多种可能)
 */
public abstract class ObservedRunnable implements Runnable {
	private final ObserverInterface observer;

	public ObservedRunnable (ObserverInterface observer) {
		this.observer = observer;
	}

	/**
	 * 通知所有的观察者, 当前线程的状态发生了改变, 如果有多个观察者, 则需要用for循环来进行通知,
	 * 由于该通知的方法是在run方法调用的, 并且是子类调用, 所以用protected修饰最合适
	 * @param mes
	 */
	protected void notifyAllOfObserver (ThreadMessage mes) {
		observer.threadMessageHandler( mes );
	}
}
