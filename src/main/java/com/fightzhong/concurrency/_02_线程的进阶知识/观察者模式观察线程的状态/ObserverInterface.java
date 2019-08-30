package com.fightzhong.concurrency._02_线程的进阶知识.观察者模式观察线程的状态;

/**
 * 观察者: 当线程的状态发生改变的时候, 需要将当时线程的信息(线程实例, 线程的状态, 线程出错的Exception对象)封装成一个信息类发送给
 *         观察者, 具体的观察者来决定对信息怎么处理
 * threadMessageHandler方法就是用来处理信息
 */
public interface ObserverInterface {
	void threadMessageHandler (ThreadMessage mes);
}
