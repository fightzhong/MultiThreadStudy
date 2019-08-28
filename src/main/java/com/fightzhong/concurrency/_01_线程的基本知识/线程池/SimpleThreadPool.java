package com.fightzhong.concurrency._01_线程的基本知识.线程池;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SimpleThreadPool {
	// 线程池中线程的个数
	private final int size;

	// 默认的线程池中线程的个数
	private static final int DEFAULT_SIZE = 10;

	// 将所有的线程归属于一个线程组
	private static final ThreadGroup GROUP = new ThreadGroup( "Simple_Thread_Pool_Group" );

	// 任务列表, 为链表, 采用先进先出的方式来选择一个任务
	private static final LinkedList<Runnable> TASK_QUEUE = new LinkedList<>();

	// 线程队列
	private static final ArrayList<WorkThread> THREAD_QUEUE = new ArrayList<>();

	/*********************Constructor************************/

	public SimpleThreadPool () {
		this( DEFAULT_SIZE );
	}

	public SimpleThreadPool (int size) {
		this.size = size;
		init();
	}

	/*********************Constructor************************/

	/**
	 * inti方法主要用于初始化线程池, 如创建指定个数的线程
	 */
	private void init () {
		for ( int i = 0; i < size; i ++ ) {
			WorkThread workThread = new WorkThread( "Thread[ " + i + " ]" );
			workThread.start();
			THREAD_QUEUE.add( workThread );
		}
	}

	/**
	 * 递交任务, 将任务放入任务列表, 因为是往TASK_QUEUE链表中放入
	 * 数据, 所以仍然会有多线程安全问题, 所以这里需要用同步锁, 这个
	 * 同步锁必须要用TASK_QUEUE, 因为我们线程没有任务的时候处于阻塞
	 * 状态是由于TASK_QUEUE.wait()引起的, 所以我们在往任务列表中增加
	 * 任务的同时要唤醒这些线程去接收任务
	 * @param runnable
	 */
	public void submit (Runnable runnable) {
		synchronized ( TASK_QUEUE ) {
			TASK_QUEUE.addLast( runnable );
			// 当任务列表中增加了任务后, 我们需要调用notify方法唤醒线程
			TASK_QUEUE.notifyAll();
		}
	}

	/**
	 * 我们自定义的四种线程状态
	 * Free: 表示当前线程可以接收任务
	 * Running: 表示当前线程正在执行任务
	 * Block: 表示当前线程由于没有任务执行而进入等待状态
	 * Dead: 表示当前线程即将死亡, 即run方法执行结束
	 *
	 * Note: Block状态可以被用于判断线程是否活跃, 因为处于Block状态的话可以表示当前线程是没有任务可执行的
	 *       , 并且处于wait中, 那么在关闭线程池的时候去销毁线程就可以通过interrupt的方式来使其退出等待
	 */
	private enum WorkThreadState {
		FREE, RUNNING, BLOCK, DEAD;
	}

	/**
	 * 对线程Thread类进一步封装, 我们所有的线程归属于一个线程组, 并且拥有不同的线程名字
	 */
	static private class WorkThread extends Thread {
		private WorkThreadState state = WorkThreadState.FREE; // 一开始都是Free状态

		public WorkThread (String threadName) {
			super( GROUP, threadName );
		}

		@Override
		public void run () {
			// 当线程的状态不是DEAD的时候使其一直处于循环状态, 即活跃, 从而不会使得run方法结束而导致线程终止
			while ( this.state != WorkThreadState.DEAD ) {
				/**
				 * 接下来应该从任务队列中取出一个任务来执行：
				 *    Runnable runnable = TASK_QUEUE.removeFirst();
				 * 	runnable.run();
				 * 但是因为从TASK_QUEUE取出任务可能会导致多线程问题, 即多个线程同时取的情况,
				 * 所以这里需要用同步代码块
 				 */
				Runnable runnable;
				synchronized ( TASK_QUEUE ) {
					// 如果此时发现了这个任务队列为空了, 那么就需要让当前线程进行等待,
					// 直到任务队列中出现了任务的时候才唤醒线程
					while ( TASK_QUEUE.isEmpty() ) {
						try {
							// 设置当前线程为阻塞状态
							this.state = WorkThreadState.BLOCK;
							TASK_QUEUE.wait();
						} catch (InterruptedException e) {
							// 如果被中断了等待状态, 则直接退出run方法使得线程结束
							return;
						}
					}

					// 如果不为空, 那么就分配一个任务给runnable
					runnable = TASK_QUEUE.removeFirst();
				}

				// 到了这一步, 当前线程是一定取得了任务的, 直接调用run方法即可, 并设置运行状态
				this.state = WorkThreadState.RUNNING;
				runnable.run();
			}
		}
	}
}
