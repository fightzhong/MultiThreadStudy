## Condition
```
对于Condition对象来说, 需要通过Lock接口的实现类的newCondition()方法来获取, 对于Lock实现类来说,
其就相当于synchronized关键字, 而Condition对象就相当于sychronized关键字对应的Monitor对象的wait和
notify以及notifyAll的实现, 即在lock()与unlock()中, 需要用condition对象的await, signal, signalAll
方法来代替wait, notify, notifyAll的实现, 并且不同的是, Condition对象可以有多个, Condition也叫
条件, 即对于线程来说, 可以将它们放入不同的条件等待队列, 比如生产者消费者模型中, 可以创建两个Condition
对象, 一个为ConsumerCondition, 一个为ProducerCondition, 当共享资源区满的时候, 将生产者进程放入
ProducerCondition, 并且由消费者消费后调用该Condition对象的signalAll方法唤醒生产者进程, 当共享资
源区空的时候, 将消费者进程放入ConsumerCondition, 并且由生产者生产了数据后再调用该对象的signalAll
方法唤醒消费者进程, 下面我们利用ReentrantLock来模拟生产者消费者模型
```

## Coding
```java
public class TestClass2 {
	private static int data = 0;                    // 临界区资源个数
	private static final int min = 0;               // 临界区资源最小个数
	private static final int max = 20;              // 临界区资源最大个数
	private static final Lock lock = new ReentrantLock(); // 可重入锁
	private static final Condition CONSUMER_CONDITON = lock.newCondition(); // 消费者Condition
	private static final Condition PRODUCER_CONDITON = lock.newCondition(); // 生产者Condition

	public static void produce () throws InterruptedException {
		try {
			lock.lock();
			// 当资源个数大于等于最大允许的个数时, 此时进来的生产者进程就应该被
			// 放入生产者等待队列进行等待
			while ( data >= max ) {
				PRODUCER_CONDITON.await();
			}

			System.out.println( "生产者[" + Thread.currentThread().getName() + "]生产的数据为: " + ++data );
			// 当生产者生产了数据时, 此时临界区就不为空了, 需要唤醒因临界区为空而进入等待的消费者进程
			// 即唤醒在消费者等待队列中等待的消费者进程
			CONSUMER_CONDITON.signalAll();
			// 利用sleep来模拟生产者生产一个数据需要时间
			TimeUnit.MILLISECONDS.sleep( 200 );
		} finally {
			lock.unlock();
		}
	}

	public static void consume () throws InterruptedException {
		try {
			lock.lock();
			// 当资源个数小于等于最小允许的个数时, 此时进来的消费者进程就应该被
			// 放入消费者等待队列进行等待
			while ( data <= min ) {
				CONSUMER_CONDITON.await();
			}

			System.out.println( "消费者[" + Thread.currentThread().getName() + "]消费的数据为: " + data-- );
			// 当消费者消费了数据时, 此时临界区就不会是满的了, 需要唤醒因临界区为满而进入等待的生产者进程
			// 即唤醒在生产者等待队列中等待的生产者进程
			PRODUCER_CONDITON.signalAll();
			// 利用sleep来模拟生产者生产一个数据需要时间
			TimeUnit.MILLISECONDS.sleep( 200 );
		} finally {
			lock.unlock();
		}
	}

	// 测试代码
	public static void main (String[] args) {
		// 创建10个生产者进程一直生产数据
		for ( int i = 0; i < 10; i ++ ){
			new Thread( () -> {
				while ( true ) {
					try {
						produce();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}, "Producer-" + i ).start();
		}

		// 创建6个消费者进程一直消费数据
		for ( int i = 0; i < 6; i ++ ){
			new Thread( () -> {
				while ( true ) {
					try {
						consume();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}, "Consumer-" + i ).start();
		}
	}
}
```











