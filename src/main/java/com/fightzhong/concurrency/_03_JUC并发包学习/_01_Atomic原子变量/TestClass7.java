package com.fightzhong.concurrency._03_JUC并发包学习._01_Atomic原子变量;

import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class TestClass7 {
	public static void main (String[] args) {
		AtomicIntegerFieldUpdater fieldUpdater = AtomicIntegerFieldUpdater.newUpdater( Test.class, "num" );
		Test t = new Test();
		int result = fieldUpdater.getAndIncrement( t );
		System.out.println( result );
		System.out.println( t.num );
	}
}

class Test {
	protected volatile int num = 6;
}