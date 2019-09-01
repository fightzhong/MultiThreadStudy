package com.fightzhong.concurrency._02_线程的进阶知识.ThreadPerMessage;

/********客户端发来的消息*********/
public class RequestMessage {
	private String message;

	public RequestMessage (String message) {
		this.message = message;
	}

	public String getMessage () {
		return message;
	}
}
