package com.fightzhong.concurrency._03_JUC并发包学习._02_CountDownLatch;

import sun.misc.Unsafe;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CountDownLatch;

public class TestClass1 {
	private static int a = 1;  // 定义一个静态成员变量a
	public static void main (String[] args) throws Exception {
		// 通过反射获取Unsafe对象
		Class<?> c = Class.forName("sun.misc.Unsafe");
		Constructor con = c.getDeclaredConstructor();
		con.setAccessible( true );
		Unsafe unsafe = (Unsafe)con.newInstance();

		// 利用CountDownLatch来实现在输出a这个变量前这些线程都已经执行完毕
		CountDownLatch coutDown = new CountDownLatch( 5 );
		for ( int i = 0; i < 5; i ++ ) {
			new Thread(){
				@Override
				public void run () {
					// monitorEnter
					unsafe.monitorEnter( TestClass1.class );
					for ( int i = 0; i < 500; i ++ ) {
						try {
							// 小小的休眠一毫秒来使得线程安全问题更加的明显
							Thread.sleep( 1 );
						} catch (InterruptedException e) {}
						a++;
					}
					coutDown.countDown();
					// monitorExit
					unsafe.monitorExit( TestClass1.class );
				}
			}.start();
		}

		// 在上面这些线程都执行完毕前会阻塞在这里
		coutDown.await();
		System.out.println( a );
	}
}


