package com.fightzhong.concurrency._02_线程的进阶知识.读写锁设计模式;

public class ReadWriteLock {
	private int readers = 0;   // 读者的数量
	private int writers = 0;   // 写者的数量
	// 为了能够让写者优先, 我们需要设置写者的等待数
	// 当有写者在等待的时候, 之后来的读者进程都必须进入等待状态
	private int waitingWriter = 0;

	// 读锁
	public synchronized void readLock () throws InterruptedException {
		// 如果有写者在写着或者有写者进程在等待(写者优先), 那么当前读者进程就应该进入等待状态
		while ( writers != 0 || waitingWriter > 0 ) {
			this.wait();
		}

		// 没有写者进程在写, 则读者的个数加一
		readers ++;
	}

	// 解读锁
	public synchronized void readUnlock () {
		// 当前读者读完之后, 应该将读者减去一个
		readers--;
		// 这里可能读者减一之后就已经没有读者进程了, 但是有可能有写者进程正在等待, 所以需要唤醒一下
		// (即使没有写者进程在等待, 此次唤醒也不会有什么影响)
		this.notifyAll();
	}

	// 写锁
	public synchronized void writeLock () throws InterruptedException {
		waitingWriter++;
		// 当一个写者进程要写数据的时候, 如果已经有写者在写或者有读者在读,
		// 那么当前读者进程就应该进入阻塞状态
		while ( writers != 0 || readers != 0 ) {
			this.wait();
		}

		// 抢到了锁后, 当前线程能够进行写操作了, 此时应该将写者进程加1, 等待的写者进程减一
		waitingWriter--;
		writers ++;
	}

	// 解写锁
	public synchronized void writeUnlock () {
		// 当前写者进程写完之后, 应该减1使得写者进程为0个
		writers --;
		// 同时应该唤醒哪些阻塞的读者进程和写者进程去争夺锁
		this.notifyAll();
	}
}
