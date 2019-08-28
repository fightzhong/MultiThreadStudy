package com.fightzhong.concurrency._01_线程的基本知识.显示锁;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class MyLock implements Lock {
	private boolean isLock = false;  // 判断当前锁是否已经被一个线程持有, true则表示已经被持有
	private Collection<Thread> blockedThreadQueue = new HashSet<>();  // 阻塞队列
	private Thread currentThread;

	@Override
	public synchronized void lock () throws InterruptedException {

		// 如果当前锁已经被一个线程持有了, 那么当前调用该方法的线程就必须进入等待状态
		// 同时, 由于可能会有多个线程依次执行到这里从而进入等待状态, 那么等待状态唤醒后是
		// 执行之后的代码的, 此时不应该直接走下一步, 而是应该再一次判断锁是否存在, 所以用while
		while ( isLock ) {
			// 如果已经有线程持有锁, 那么当前线程就应该被放入阻塞队列, 同时进入等待状态
			// 这里必须用Set集合, 防止阻塞队列中出现多个相同的线程
			blockedThreadQueue.add( Thread.currentThread() );
			this.wait();
		}

		// 如果为false, 那么就说明当前线程可以持有该锁, 同时将锁置为true, 并将当前线程从Set集合中
		// 移除(前提是Set集合中存在该线程)
		currentThread = Thread.currentThread(); // 持有锁的线程
		this.isLock = true;
		blockedThreadQueue.remove( currentThread );
	}

	@Override
	public synchronized void lock (long millis) throws TimeOutException, InterruptedException {
		if ( millis < 0 ) {
			throw new IllegalArgumentException( "millis is not allowed to lower than zero" );
		}

		long startTime = System.currentTimeMillis(); // 用户调用lock方法获取锁的时间
		long endTime; // 用户休眠后的时间
		// 如果锁已经被其它线程持有, 则当前线程进入等待状态, 等待时间为millis
		while ( isLock ) {
			blockedThreadQueue.add( Thread.currentThread() );
			this.wait( millis );
			endTime = System.currentTimeMillis(); // 当前线程等待的结束时间
			// 如果等待的时间超过了用户规定的时间, 那么就抛出一个超时异常异常
			// 从而用户可以捕捉该异常而选择下一步的执行逻辑, 如果没超时, 则
			// 当前线程会继续判断锁是否空闲, 如果空闲则直接跳过循环获取锁
			// 如果不空闲则继续等待, 等待时间过后再继续判断是否超时等
			if ( endTime - startTime > millis ) {
				throw new TimeOutException( "Time Out" );
			}
		}

		currentThread = Thread.currentThread();
		isLock = true;
		blockedThreadQueue.remove( currentThread ); // 将当前线程从等待队列中移除
	}

	/**
	 * 一: 解锁的过程只需要将isLock置为false就好了, 但是存在一个问题, 那就是这个解锁的调用
	 *     不是在当前线程被调用的, 举个例子:
	 *    final MyLock lock = new MyLock();
	 *    new Thread() {
	 *       lock.lock();
	 *          // 一系列同步逻辑
	 *       lock.unlock();
	 *    }.start();
	 *
	 *    lock.unlock() // invalid invocation
	 *
	 * 二: 当调用unlock方法的线程不是锁的持有线程的时候, 选择跳过该方法, 而不是抛出异常,
	 *      因为我们关锁一般放在finally语句中, 那么在上面的lock(millis)方法中, 由于我们
	 *      可能会抛出TimeOutException异常, 这时该线程是不持有锁的, 那么就会导致在finally
	 *      语句中调用unlock从而触发这个在unlock中定义的异常, 如下:
	 *      new Thread( () -> {
	 * 		   try {
 	 *				   // 获取锁, 如果超时, 则锁没获取到,会转入超时异常catch语句, 并执行finally语句
	 *             lock.lock( 1000 );
    *          } catch (InterruptedException e) {
    * 				e.printStackTrace();
    *          } catch (Lock.TimeOutException e) {
    * 				System.out.println( Thread.currentThread().getName() + ": 等待超时, 结束运行..." );
    *          } finally {
    * 				lock.unlock();
	 *				} ).start();
	 *
	 */
	@Override
	public synchronized void unlock () {
		if ( currentThread == Thread.currentThread() ) {
			this.isLock = false;
			this.notifyAll(); // 唤醒阻塞队列中的所有线程, 使得它们去争夺这个锁
		}
	}

	/**
	 * 返回一个不可更改的阻塞队列(如果用户尝试去更改它则会抛出异常)
	 * @return
	 */
	@Override
	public Collection<Thread> getBlockedThreadQueue () {
		return Collections.unmodifiableCollection( blockedThreadQueue );
	}

	/**
	 * 获取阻塞队列中线程的个数
	 */
	@Override
	public int getSize () {
		return blockedThreadQueue.size();
	}
}
