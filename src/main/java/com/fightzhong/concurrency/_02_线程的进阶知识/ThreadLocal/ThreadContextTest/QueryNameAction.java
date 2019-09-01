package com.fightzhong.concurrency._02_线程的进阶知识.ThreadLocal.ThreadContextTest;

import java.util.Random;

public class QueryNameAction {
	Random ran = new Random();
	public void execute () {
		try {
			Thread.sleep( 2000 );
		} catch (InterruptedException e) {}
		Context context = ActionContext.getContext();
		context.setName( "张三[" + ran.nextInt( 100 ) + "]" );
	}
}
