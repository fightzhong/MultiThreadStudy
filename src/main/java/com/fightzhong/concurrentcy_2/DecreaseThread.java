package com.fightzhong.concurrentcy_2;

public class DecreaseThread extends Thread {
	@Override
	public void run () {
		for ( int i = 0; i < 20; i ++ ) {
			try {
				Thread.sleep( 30 );
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Counter.decrease();

		}
	}
}
