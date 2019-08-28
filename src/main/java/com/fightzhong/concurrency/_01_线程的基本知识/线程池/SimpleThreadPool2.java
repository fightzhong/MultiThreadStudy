package com.fightzhong.concurrency._01_线程的基本知识.线程池;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.SimpleFormatter;

public class SimpleThreadPool2 {
	// 线程池中线程的个数
	private final int size;

	// 线程池中最多的任务个数
	private final int maxTaskCount;

	// 线程池中超出了任务个数的情况下的拒绝策略
	private final RefuseStrategy refuseStrategy;

	// 线程池是否被停止
	private boolean destroy = false;

	// 将所有的线程归属于一个线程组
	private static final ThreadGroup GROUP = new ThreadGroup( "Simple_Thread_Pool_Group" );

	// 任务列表, 为链表, 采用先进先出的方式来选择一个任务
	private static final LinkedList<Runnable> TASK_QUEUE = new LinkedList<>();

	// 线程队列
	private static final ArrayList<WorkThread> THREAD_QUEUE = new ArrayList<>();

	// 默认的线程池中线程的个数
	private static final int DEFAULT_SIZE = 10;

	// 默认的最多的执行任务
	private static final int DEFAULT_MAX_TASK_COUNT = 200;

	// 默认的拒绝策略, 输出当前的信息后直接返回, 不执行后面的代码
	public static RefuseStrategy DEFAULT_REFUSE_STRATEGY = () -> {
		System.out.println( "CurThreadCount: "+ THREAD_QUEUE.size() + ", CurTaskCount: "+ THREAD_QUEUE.size() +", MaxTaskCount: " + DEFAULT_MAX_TASK_COUNT );
		throw new RefuseException( "任务达到最大的个数, 不允许再提交任务" );
	};

	/*********************Constructor************************/

	public SimpleThreadPool2 () {
		this( DEFAULT_SIZE, DEFAULT_MAX_TASK_COUNT, DEFAULT_REFUSE_STRATEGY );
	}

	// 用户传入线程的个数, 最多的执行任务, 当任务达到峰值时再传入的任务而采取的拒绝策略
	public SimpleThreadPool2 (int size, int maxTaskCount, RefuseStrategy refuseStrategy) {
		this.size = size;
		this.maxTaskCount = maxTaskCount;
		this.refuseStrategy = refuseStrategy;
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
			// 如果发现线程池已经被销毁了, 则不能再提交任务
			if ( destroy ) {
				throw new IllegalStateException( "线程池已经被终止, 不允许添加新的任务" );
			}
			// 对于提交的任务, 如果达到达到阈值, 则执行拒绝策略
			if ( TASK_QUEUE.size() >= maxTaskCount ) {
				// refuseStrategy.refuse();
				return;
			}

			// 没达到阈值, 则将任务添加进去
			TASK_QUEUE.addLast( runnable );
			// 当任务列表中增加了任务后, 我们需要调用notify方法唤醒线程
			TASK_QUEUE.notifyAll();
		}
	}

	/**
	 * 终止线程池:
	 *    1、终止线程之前我们必须确保没有任务了, 所以第一步必须是先将任务执行完毕, 并同时不允许新的任务进来,
	 *       为了防止新的任务不进来, 我们需要为线程池设置一个destroy变量, 当这个变量为true的时候, submit失败
	 *    2、对于处于等待的线程(无任务, 空闲), 即wait(), 调用interrupt方法发送中断信号,
	 *       收到中断信号后, 会直接调用return而使得run方法结束
	 *    3、对于处于运行状态的线程, 我们需要等到其任务执行完毕才能终止, 而终止该线程的方法就
	 *       是将线程的状态设置为Dead, 这样其就不会继续循环下去了
	 */
	public void shutdown () {
		// 等待所有的任务被分配完成
		while ( TASK_QUEUE.size() != 0 ) {
			try {
				Thread.sleep( 10 );
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// 依次遍历每一个线程, 将等待的线程打断, 将运行的线程设置状态为DEAD
		for ( WorkThread thread: THREAD_QUEUE ) {
			thread.interrupt();
			thread.state = WorkThreadState.DEAD;
		}

		// 防止新的线程被创建
		destroy = true;
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
							// 线程等待状态由于线程池要被shutdown而被打断, 并终止线程的执行
							return;
						}
					}

					// 任务队列不为空, 选择一个任务
					runnable = TASK_QUEUE.removeFirst();
					// 设置运行状态
					this.state = WorkThreadState.RUNNING;
				}

				// 到了这一步, 当前线程是一定取得了任务的, 直接调用run方法即可
				runnable.run();
			}
		}
	}

	/**
	 * 拒绝策略接口, 如果拒绝任务的具体实现由用户指定, 函数式接口
	 */
	@FunctionalInterface
	private interface RefuseStrategy {
		void refuse ();
	}

	/**
	 * 默认拒绝策略中需要抛出的异常
	 */
	static private class RefuseException extends RuntimeException {
		public RefuseException (String mes) {
			super( mes );
		}
	}
}
