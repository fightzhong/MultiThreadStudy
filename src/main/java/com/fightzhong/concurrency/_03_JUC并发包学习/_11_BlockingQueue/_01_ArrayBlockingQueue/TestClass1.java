package com.fightzhong.concurrency._03_JUC并发包学习._11_BlockingQueue._01_ArrayBlockingQueue;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TestClass1 {
	static ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<>( 2 );
	public static void main (String[] args) throws InterruptedException {
		testOther();
 	}

 	public static void testOther () {
		// System.out.println( queue.remainingCapacity() );
	   // queue.offer( 10 );
	   // System.out.println( queue.remainingCapacity() );
	   // queue.offer( 20 );
	   // System.out.println( queue.remainingCapacity() );

	   // queue.offer( 10 );
	   // queue.offer( 20 );
	   // System.out.println( queue.contains( 20 ) );;
	   // System.out.println( queue.contains( 201 ) );;

	   Collection<Integer> list = new LinkedList<>();
	   queue.add( 10 );
	   queue.add( 20 );
	   queue.drainTo( list, 1 );
	   System.out.println( list );
   }

 	public static void testRemove () throws InterruptedException {
		// queue.add( 66 );
		// queue.add( 77 );

		// poll
	   // System.out.println( queue.poll() );
	   // System.out.println( queue.poll() );
	   // System.out.println( queue.poll() );
	   // System.out.println( queue.poll() );
	   // System.out.println( queue.size() );


	   // take
	   // System.out.println( queue.take() );
	   // System.out.println( queue.take() );
	   // System.out.println( queue.take() );

	   // remove
	   // System.out.println( queue.remove(77) );
		// System.out.println( queue.remove() );

	   // System.out.println( queue.peek() );
	   // System.out.println( queue.peek() );
	   // System.out.println( queue.peek() );
	   // System.out.println( queue.peek() );

	   // System.out.println( queue.element() );
	   // System.out.println( queue.element() );
	   // System.out.println( queue.element() );
	   // System.out.println( queue.element() );

   }

 	public static void testAdd () {
	   // add
	   // queue.add( 66 );
	   // queue.add( 77 );
	   // queue.add( 88 );
	   //
	   // System.out.println( "=====================" );

	   // offer
	   // queue.offer( 66 );
	   // queue.offer( 77 );
	   // queue.offer( 88 );
	   //
	   // System.out.println( "=====================" );

	   // put
	   // queue.put( 66 );
	   // queue.put( 77 );
	   // queue.put( 88 );

	   // queue.offer( 66 );
	   // queue.offer( 77 );
	   // queue.offer( 88, 5, TimeUnit.SECONDS );
	   // System.out.println( "=====================" );
   }
}
