package com.fightzhong.concurrency._03_JUC并发包学习._01_Atomic原子变量;

import org.junit.Test;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class TestClass3 {
	@Test
	public void test () {
		AtomicReference<MyObj> atomic = new AtomicReference<>( new MyObj( 15, "张三" ) );
		boolean b = atomic.compareAndSet( new MyObj( 15, "张三" ), new MyObj( 14, "张三" ) );
		System.out.println( b );
	}
}

class MyObj{
	private int age;
	private String name;

	public MyObj (int age, String name) {
		this.age = age;
		this.name = name;
	}
}