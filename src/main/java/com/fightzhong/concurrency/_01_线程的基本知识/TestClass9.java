package com.fightzhong.concurrency._01_线程的基本知识;

public class TestClass9 {
	public static void main (String[] args) throws InterruptedException {
		Thread t1 = new Thread( () -> {
			// 一系列逻辑........................

			try {
				Thread.sleep( 10000 );
			} catch (InterruptedException e) {
				// 捕捉到中断信号之后, 会退出睡眠状态, 此时如果下面还有逻辑则可以选择继续执行下面的逻辑
				// 如果下面没用逻辑了, 那么在这里可以直接return来结束线程的执行
				e.printStackTrace();
			}

			// 一系列逻辑........................
		} );
		t1.start();

		// 先让main线程休眠3秒钟, 这样就可以让t1线程执行3秒钟, 然后调用t1的interrupt方法来中断t1线程的wait或者sleep的状态
		Thread.sleep( 3000 );
		t1.interrupt();
	}
}
