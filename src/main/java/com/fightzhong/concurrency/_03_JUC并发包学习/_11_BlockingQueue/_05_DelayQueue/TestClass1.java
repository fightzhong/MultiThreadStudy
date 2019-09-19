package com.fightzhong.concurrency._03_JUC并发包学习._11_BlockingQueue._05_DelayQueue;

import java.util.concurrent.*;

public class TestClass1 {
	public static void main (String[] args) throws InterruptedException {
		// 创建一个DelayElement元素, 设置延迟时间为5秒, 即5秒内如果线程去获取则会陷入阻塞
		DelayElement<String> ele1 = new DelayElement<>( "ele1", 5000 );

		DelayQueue<DelayElement<String>> queue = new DelayQueue<>();
		queue.add( ele1 );

		// 会陷入阻塞5秒钟, 之后输出
		queue.take();
	}

	static class DelayElement<T> implements Delayed {
		private T data;   // 存储的数据
		private long expiredTime; // 过期的时间, 即在什么时候会过期

		public DelayElement (T data, long delayMillisTime) {
			this.data = data;
			/**
			 * delayMillisTime就是用户希望延迟的时间, 单位是毫秒
			 * 例如, 用户希望延迟30毫秒, 那么当前时间加上30毫秒就是过期的时间
			 * 即在30毫秒之后该DelayElement元素就是过期的元素了
 			 */
			this.expiredTime = delayMillisTime + System.currentTimeMillis();
		}

		// 获取到过期时间还剩余多久
		@Override
		public long getDelay (TimeUnit unit) {
			// 获取调用该方法的时候的时间
			long cur = System.currentTimeMillis();
			/**
			 * expiredTime - cur是剩余的过期时间, 在我们这个类的定义中
			 * 我们将其定义成了毫秒级别, 所以需要将其转换为该方法需要的对应
			 * 的时间的格式
			 * unit.convert(expiredTime - cur, TimeUnit.MILLISECONDS)：
			 * 将毫秒时间expiredTime - cur转换为unit对应的时间格式
			 */
			return unit.convert(expiredTime - cur, TimeUnit.MILLISECONDS);
		}

		@Override
		public int compareTo (Delayed o) {
			// 两个时间的对比就是将两个时间进行相减即可
			return (int)( expiredTime - ( (DelayElement)o ).expiredTime );
		}

 		@Override
		public String toString () {
			return "DelayElement{" +
			"data=" + data +
			", expiredTime=" + expiredTime +
			'}';
		}
	}
}
