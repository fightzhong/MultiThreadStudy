package com.fightzhong.concurrency._02_线程的进阶知识.ThreadPerMessage;

public class Server {
	public static void main (String[] args) {
	    // 通过循环的方式模拟客户端发来多条消息
		for ( int i = 0; i < 10; i ++ ) {
			RequestMessage message = new RequestMessage( "消息[ "+ i +" ]" );
			RequestMessageHandler.handle( message );
		}
	}
}
