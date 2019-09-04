package com.fightzhong.concurrency._03_JUC并发包学习._01_Atomic原子变量;

import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * volatile关键字存在的问题:
 *    不能保证原子性, 假设存在这么一种情况, 线程A和线程B均对一个共享变量进行加1操作, 然后输出这个共享变量的值, 代码如下:
 *    int a = 1;
 *    new Thread(){
 *       a ++;
 *       System.out.println( a );
 *    }
 *
 *    可以把a++分解成以下三个动作:
 *       <1> 从内存中取出a的值
 *       <2> 执行加法操作获得结果2
 *       <3> 将结果2写回内存
 *    假设线程A执行了1、2步, 然后线程A也执行了1、2步, 最后线程A执行第三步, 线程B执行第三步
 *    从而使得两次加法操作后值却是2
 *
 */
public class TestClass1 {
	static AtomicInteger a = new AtomicInteger( 1 );

	public static void main (String[] args) throws InterruptedException {
		HashSet<AtomicInteger> set = new HashSet<>();

		Thread t1 = new Thread( () -> {
			for ( int i = 0; i < 1000; i ++ ) {
				set.add( a );
				a.getAndIncrement();
			}
		} );

		Thread t2 = new Thread( () -> {
			for ( int i = 0; i < 1000; i ++ ) {
				set.add( a );
				a.getAndIncrement();
			}
		} );

		Thread t3 = new Thread( () -> {
			for ( int i = 0; i < 1000; i ++ ) {
				set.add( a );
				a.getAndIncrement();
			}
		} );

		t1.start();
		t2.start();
		t3.start();
		t1.join();
		t2.join();
		t3.join();

		System.out.println( set );
	}
}
