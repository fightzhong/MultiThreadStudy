package com.fightzhong.concurrency._01_线程的基本知识;

public class TestClass12 {
	public static void main (String[] args) {
		ThreadService threadService = new ThreadService();
		threadService.executeThread( () -> {
			try {
				Thread.sleep( 10000 );
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} );

		threadService.shutdownThread( 5000 );



	}
}
