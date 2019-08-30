package com.fightzhong.concurrency._02_线程的进阶知识;

public class TestClass2 {
	private static volatile boolean flag = false;
	public static void main (String[] args) {
	   new Thread(){
		   @Override
		   public void run () {
			   System.out.println( "线程1执行了" );
			   try {
				   Thread.sleep( 5000 );
			   } catch (InterruptedException e) {}

			   flag = true;
		   }
	   }.start();

	   new Thread(){
		   @Override
		   public void run () {
		   	while ( !flag ){};
		   	System.out.println( "线程2执行了" );
		   }
	   }.start();

	}
}
