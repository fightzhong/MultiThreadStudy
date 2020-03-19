package com.fightzhong.concurrentcy_2.ObjectTest;

import com.fightzhong.MyJavaAgent;
import sun.instrument.InstrumentationImpl;

public class TestClass {
	private byte b;

	private short s;

	private int i;

	private long l;

	private float f;

	private double d;

	public static void main (String[] args) {
		System.out.println( MyJavaAgent.sizeOf( new Object() ) );
		System.out.println( MyJavaAgent.sizeOf( new int[]{} ) );
		System.out.println( MyJavaAgent.sizeOf( new TestClass() ) );


		//
		// InstrumentationImpl instrumentation = new InstrumentationImpl(  );
	}
}
