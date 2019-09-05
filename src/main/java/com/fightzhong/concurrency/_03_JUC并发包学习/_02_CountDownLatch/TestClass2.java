package com.fightzhong.concurrency._03_JUC并发包学习._02_CountDownLatch;

import sun.misc.Unsafe;

import java.lang.reflect.Constructor;

public class TestClass2 {
	public static void main (String[] args) throws Exception {
		// 通过反射获取Unsafe对象
		Class<?> c = Class.forName("sun.misc.Unsafe");
		Constructor con = c.getDeclaredConstructor();
		con.setAccessible( true );
		Unsafe unsafe = (Unsafe)con.newInstance();

		Test obj = new Test();
		System.out.println( obj.getA() );
		unsafe.putInt( obj, unsafe.objectFieldOffset( Test.class.getDeclaredField( "a" ) ), 66 );
		System.out.println( obj.getA() );
	}
}

class Test {
	private int a = 10;

	public int getA () {
		return a;
	}
}
