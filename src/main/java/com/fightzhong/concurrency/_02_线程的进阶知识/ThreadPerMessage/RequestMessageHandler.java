package com.fightzhong.concurrency._02_线程的进阶知识.ThreadPerMessage;

/**
 * 处理消息的类, 收到一条消息则开启一个线程去处理
 */
public class RequestMessageHandler {
	public static void handle (RequestMessage message) {
		new Thread( () -> {
			System.out.println( Thread.currentThread().getName() + "  收到消息: " + message.getMessage() );

			// 模拟消息处理一段时间
			try {
				Thread.sleep( 2000 );
			} catch (InterruptedException e) {}

			System.out.println( Thread.currentThread().getName() + "  处理消息完毕...." );
		} ).start();
	}
}
