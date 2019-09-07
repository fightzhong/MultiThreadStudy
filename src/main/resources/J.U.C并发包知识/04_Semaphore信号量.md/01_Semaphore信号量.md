## Semaphore入门
```java
Semaphore信号量机制, 其实就是OS中的PV操作了, 下面我们利用信号量机制来实现一个显示的锁作为入门的案
例, 将信号量置为1

class Lock {
  // 每次创建一个锁对象都需要一个信号量对象, 并且将信号量设置为1
	private Semaphore semaphore = new Semaphore( 1 );

  // 加锁其实就是获取一个信号量的值
	public void lock () throws InterruptedException {
		semaphore.acquire();
	}

  // 解锁其实就是释放一个信号量的值
	public void unlock () {
		semaphore.release();
	}
}
```

## API描述
```
Constructor:
  Semaphore(int): 创建一个信号量, 并指定一个信号量的值
  Semaphore(int, boolean): 创建一个信号量, 并指定一个信号量的值以及是否公平

Function:
  acquire(): 取走一个信号量
  acquire(int): 取走指定个数的信号量, 如果信号量不足, 则该线程会进入阻塞状态
  acquireUninterruptibly(): 取走一个信号量, 该方法是一个不可中断的方法, 即没有中断机制, 在信号量
                            不足的时候会一直等待下去
  acquireUninterruptibly(int): 取走指定个数的信号量, 该方法是一个不可中断的方法, 即没有中断机制,
                               在信号量不足的时候会一直等待下去
  drainPermits(): 获取剩余的所有信号量

  tryAcquire(): 尝试获取一个信号量, 如果信号量不足则直接跳过
  tryAcquire(int): 尝试获取指定个数的信号量, 如果信号量不足则直接跳过
  tryAcquire(long, TimeUnit): 尝试获取一个信号量, 在指定的时间下如果没有获取到, 则直接跳过
  tryAcquire(int, long, TimeUnit): 尝试获取指定个数的信号量, 在指定的时间下如果没有获取到, 则直
                                   接跳过

  release(): 释放一个信号量
  release(int): 释放指定个数的信号量
  reducePermits(int): 减少指定个数的信号量(即永久的减少, 不会因为release而释放一个)

  availablePermits(): 获取当时可获得的信号量的个数
  getQueueLength(): 获取当时因信号量不足而进入等待的线程的个数
  hasQueuedThreads(): 判断当时是否有因信号量不足而进入等待的线程
```


