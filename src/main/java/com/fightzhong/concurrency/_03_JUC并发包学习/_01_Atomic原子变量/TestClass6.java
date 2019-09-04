package com.fightzhong.concurrency._03_JUC并发包学习._01_Atomic原子变量;

import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class TestClass6 {
	public static void main (String[] args) {
		AtomicReferenceArray<MyTest> arr = new AtomicReferenceArray<MyTest>( 10 );
		arr.set( 5, new MyTest( 15, "张三" ) );
		MyTest myTest = arr.get(5);
		System.out.println( myTest );

	}
}

class MyTest {
	private int age;
	private String name;

	public MyTest (int age, String name) {
		this.age = age;
		this.name = name;
	}

	public int getAge () {
		return age;
	}

	public void setAge (int age) {
		this.age = age;
	}

	public String getName () {
		return name;
	}

	public void setName (String name) {
		this.name = name;
	}

	@Override
	public String toString () {
		return "MyTest{" +
		"age=" + age +
		", name='" + name + '\'' +
		'}';
	}
}