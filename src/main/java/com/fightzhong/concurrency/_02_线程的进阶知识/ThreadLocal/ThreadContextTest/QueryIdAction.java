package com.fightzhong.concurrency._02_线程的进阶知识.ThreadLocal.ThreadContextTest;

import java.util.Random;

public class QueryIdAction {
	Random ran = new Random();
	public void execute () {
		Context context = ActionContext.getContext();
		String name = context.getName();
		String id = queryIdByName( name );
		context.setId( id );
	}

	private String queryIdByName (String name) {
		try {
			Thread.sleep( 2000 );
		} catch (InterruptedException e) {}

		return "id["+ ran.nextInt( 100 ) + "]";
	}
}
