## 分析
```java
ThreadLocal threadLocal = new ThreadLocal();
ThreadLocal类对象, 通俗的讲就是保存线程的一个上下文数据, 该对象的set(T t)方法以一个任意类型的T作
为参数, 对象里面采用Map键值对的方式来存储数据, 键为线程对象即Thread.currentThread(), 值为我们传
进去的值, 所以键是不会重复的, 因为线程对象一定是唯一的, get()方法用于获取当前线程对象对应的值, 即
在不同的线程中, threadLocal.get()方法获取的值是不一样的, 因为在get方法中会获取当前线程对象, 下面
是一个简易版本的ThreadLocal:

public class MyThreadLocal<T> {
	private HashMap<Thread, T> data = new HashMap<>();

	public void set (T t) {
		Thread thread = Thread.currentThread();
		data.put( thread, t );
	}

	public T get () {
		Thread thread = Thread.currentThread();
		T result = data.get( thread );

		if ( result == null ) {
			return initValue();
		}

		return result;
	}

	protected T initValue () {
		return null;
	}
}

需要注意的是:
  <1> 当使用ThreadLocal的时候应该要使得ThreadLocal对象唯一
  <2> 在使用ThreadLocal的时候, 由于键为线程对象, 所以在使用线程池的时候需要注意多个线程任务能获得
      同一个线程对象的情况
```
















