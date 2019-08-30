package com.fightzhong.concurrency._02_线程的进阶知识.观察者模式观察线程的状态;

/**
 * 真正的观察者对象
 */
public class ConcreteObserver implements ObserverInterface {
	/**
	 * 对线程状态发生改变后的信息进行处理
	 * @param mes
	 */
	@Override
	public void threadMessageHandler (ThreadMessage mes) {
		System.out.println( "线程 [ " + mes.getThread().getName() + " ] " + "状态改变为 [ " + mes.getState() + " ] " );
		// 如果线程发生了错误, 那么就将错误信息打印出来
		if ( mes.getError() != null )
			mes.getError().printStackTrace();
	}
}
