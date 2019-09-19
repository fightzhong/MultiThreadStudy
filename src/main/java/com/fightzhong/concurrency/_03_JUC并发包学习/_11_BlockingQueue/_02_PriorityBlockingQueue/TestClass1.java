package com.fightzhong.concurrency._03_JUC并发包学习._11_BlockingQueue._02_PriorityBlockingQueue;

import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TestClass1 {
	public static void main (String[] args) throws InterruptedException {
		// testAdd();
		testPoll();
	}

	public static void testPoll () throws InterruptedException {
		PriorityBlockingQueue<MyObject> queue = new PriorityBlockingQueue<>(2);
		queue.add( new MyObject( 55 ) );
		queue.add( new MyObject( 66 ) );
		queue.add( new MyObject( 77 ) );
		System.out.println( queue.poll() );
		System.out.println( queue.poll() );
		System.out.println( queue.poll() );
		Executors.newSingleThreadScheduledExecutor().schedule( () -> {
			System.out.println( "准备添加数据" );
			queue.offer( new MyObject( 88 ) );
			System.out.println( "添加数据完成" );
		}, 1, TimeUnit.SECONDS );
		System.out.println( queue.poll(5, TimeUnit.SECONDS) );
		// System.out.println( queue.remove() );
		// System.out.println( queue.remove() );
		// System.out.println( queue.remove() );
		// System.out.println( queue.remove() );

		// System.out.println( queue.take() );
		// System.out.println( queue.take() );
		// System.out.println( queue.take() );
		// Executors.newSingleThreadScheduledExecutor().schedule( () -> {
		// 	System.out.println( "准备添加数据" );
		// 	queue.offer( new MyObject( 88 ) );
		// 	System.out.println( "添加数据完成" );
		// }, 10, TimeUnit.SECONDS );
		// System.out.println( queue.take() );
		// System.out.println( queue.poll() );
		// System.out.println( queue.poll() );
		// System.out.println( queue.poll() );
		// System.out.println( queue.poll() );
	}

	public static void testAdd () {
		PriorityBlockingQueue<MyObject> queue = new PriorityBlockingQueue<>(2);

		queue.add( new MyObject( 88 ) );
		queue.add( new MyObject( 99 ) );
	}

	static class MyObject implements Comparable {
		private int age;

		public MyObject (int age) {
			this.age = age;
		}

		public int getAge () {
			return age;
		}

		@Override
		public String toString () {
			return "MyObject{" +
			"age=" + age +
			'}';
		}

		@Override
		public int compareTo (Object o) {
			if ( o == null ) throw new IllegalArgumentException( "o is not allow be null" );
			return ((MyObject)o).getAge() - this.age;
		}
	}
}
