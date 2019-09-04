package com.fightzhong.concurrency._03_JUC并发包学习._01_Atomic原子变量;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class TestClass8 {
	public static void main (String[] args) {
		T8 t = new T8();
		t.update( 50 );
		System.out.println( t.getNum() );
		t.increment();
		System.out.println( t.getNum() );
	}
}

class T8 {
	private volatile int num = 10;

	// 本类中实现原子性的构造
	private AtomicIntegerFieldUpdater updater = AtomicIntegerFieldUpdater.newUpdater( T8.class, "num" );

	// 实现修改操作
	public void update ( int newValue ) {
		updater.getAndSet( this, newValue );
	}

	// 实现加加操作
	public void increment () {
		updater.getAndIncrement( this );
	}

	public int getNum () {
		return num;
	}
}
