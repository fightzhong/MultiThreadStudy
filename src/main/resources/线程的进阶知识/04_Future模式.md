> 由于这个Future模式还是相对有点复杂的, 下面我不会直接把该模式的实现代码写出来, 因为这样不利于他人的理解, 我会一步步进行分析, 由每一步的问题进行不停的优化, 直到最后实现该模式, 所以可能会有多个版本

## Version One
```java
下面会涉及三个类, Executor, Future, FutureTask, 其中Future就是我们将希望执行异步任务得到的结果放
入这个类中, 而作为使用者的我们直接调用Future类的get方法就可以拿到这个结果了, FutureTask是一个函数
式接口, 我们通过实现该接口的call方法来将我们需要执行的异步任务放入其中, 最后这个方法的结果就是我们
希望放入Future类中的结果, 总的来说, FutureTask是用来定义我们的异步任务的, Future是用来封装我们异步
任务的结果的, 为了能够将FutureTask的结果放入Future中, 我们需要增加一个Executor来将它们结合起来

// FutureTask, 将异步任务放入call方法中, 由于异步任务的结果类型不确定, 所以用泛型表示
@FunctionalInterface
public interface FutureTask<T> {
	T call();
}

// Future类(保存异步任务的结果, 同样需要泛型表示)
public class Future<T> {
	private T result; // 异步任务的结果

	// 外部将结果放入Future类中
	public void setResult (T result) {
		this.result = result;
	}

	// 用户获取结果
	public T get() {
		return result;
	};
}

// Executor
public class Executor {
	/**
	 * 通过Executor类的execute方法, 用户将异步任务传入进来, 然后该方法先创建一个Future类,
	 * 并把这个future类实例返回, 开启一个线程去执行异步任务, 这样主线程中
	 * new Executor().execute(task)这样的, 调用就不会收到阻塞, 同时也能拿到future类实例,
	 * 只不过类实例中的result还是null, 当这个开启的线程执行完毕后, 会调用future.setResult()
	 * 方法将结果设置进去, 从而更新future实例中result
	 * @param task
	 * @param <T>
	 * @return
	 */
	public <T> Future<T> execute (FutureTask<T> task) {
		Future future = new Future();

		new Thread( () -> {
			T result = task.call();
			future.setResult( result );
		} ).start();

		return future;
	}
}

分析: 上面的三个类是不符合面向对象编程的, 这个之后我们再讨论, 上面三个粗糙的类的实现基本可以表明
      Future设计模式是怎么样的了:
      用户通过调用execute方法, 传入一个异步任务, 在execute方法中, 创建了一个Future类来保存异步任
      务的结果, 该异步任务利用新的线程去执行, 当执行完毕后能够通过setResult方法将任务的结果设置到
      返回的future对象中, 从而使得用户在未来的某一段时间内获取到该结果

存在的问题:
  加入用户提前去拿结果, 即调用完execute后就调用future.get()方法去拿结果, 那么由于执行异步任务
  的线程可能还没结束, 所以这个结果是null, 即拿不到正确的结果, 所以我们需要对这个拿结果的方法进行
  一定的限定, 当结果存在的时候才能拿, 当结果不存在的时候, 调用future.get()方法的线程应该进入等待
  状态, 直到结果存在才能拿
```

## Version Two
```java
// (未改变)FutureTask, 将异步任务放入call方法中, 由于异步任务的结果类型不确定, 所以用泛型表示
@FunctionalInterface
public interface FutureTask<T> {
	T call();
}

// Future
public class Future<T> {
	private T result; // 异步任务的结果
	// 初始的时候结果肯定是不存在的, 当异步任务执行完毕调用setResult方法后才能将finish设置为true
	private boolean finish = false;

	// 外部将结果放入Future类中
	public void setResult (T result) {
		// 为了能够在结果更新后去唤醒那些提前获取结果的线程, 即为了能
		// 够调用this.notifyAll, 我们需要将这些代码放入this对象锁中
		synchronized ( this ) {
			this.result = result;
			// 设置为true, 同时唤醒那些提前去拿结果的线程
			this.finish = true;
			this.notifyAll();
		}
	}

	// 用户获取结果
	public T get() {
		synchronized ( this ) {
			// 当结果不存在的时候, 调用get方法的线程应该进入等待状态
			// 直到异步任务结束更新了结果才能被唤醒
			while ( !finish ) {
				try {
					this.wait();
				} catch (InterruptedException e) {}
			}
		}

		return result;
	};
}

// (未变)Executor
public class Executor {
	/**
	 * 通过Executor类的execute方法, 用户将异步任务传入进来, 然后该方法先创建一个Future类,
	 * 并把这个future类实例返回, 开启一个线程去执行异步任务, 这样主线程中
	 * new Executor().execute(task)这样的, 调用就不会收到阻塞, 同时也能拿到future类实例,
	 * 只不过类实例中的result还是null, 当这个开启的线程执行完毕后, 会调用future.setResult()
	 * 方法将结果设置进去, 从而更新future实例中result
	 * @param task
	 * @param <T>
	 * @return
	 */
	public <T> Future<T> execute (FutureTask<T> task) {
		Future future = new Future();

		new Thread( () -> {
			T result = task.call();
			future.setResult( result );
		} ).start();

		return future;
	}
}

分析: 我们仅仅改变了Future类的实现, 即对get方法增加了限制, 当结果不存在的时候, 提前获取结果的线程
      会进入阻塞状态, 直到setResult方法更新结果并唤醒等待线程
```

## 测试代码
```java
public class TestClass {
	public static void main (String[] args) throws InterruptedException {
		Executor executor = new Executor();
		Future future = executor.execute( () -> {
			// 异步任务, 利用sleep方法来模拟异步任务需要执行一段时间
			try {
				Thread.sleep( 5000 );
			} catch (InterruptedException e) {}

			// 返回结果
			return "Result...";
		} );

		// 在调用future之前是不会收到阻塞的, 因为future对象是立马返回的,
		// 而异步任务是用另外一个线程来执行的, 这里用sleep来模拟主线程在继续执行
		System.out.println( "===========主线程在执行任务=========" );
		Thread.sleep( 3000 );
		System.out.println( "===========主线程执行任务完毕=========" );

		// 获取结果, 因为主线程只执行了3秒, 而异步任务执行了5秒, 所以如果此时获取结果会进入阻塞
		System.out.println( future.get() );
	}
}
```

## 分析
```
上面其实已经初步实现了Future设计模式了, 为了能够非常直观的看出Future设计模式的情况, 我们在实现
Future类的时候没有考虑其扩展性, 如果为了考虑这个扩展性的话, 应该将Future类定义为一个接口的, 这样
用户就可以选择Executor执行的时候对结果进行不同的封装了, 不过也没关系, 重点是这个模式的思想, 即将异步
任务放入一个线程执行, 然后在线程开启之前创建一个结果类, 之后把这个结果类返回, 用户可以直接拿到这个
结果类对象future, 而异步任务线程执行完毕之后也可以通过future.setResult方法来更新结果, 用户主线程
在调用异步任务之后能够继续往后执行, 在未来的某一时刻能够获取这个结果的值(如果异步任务没有执行完则需
要等待)

不足之处, 用户需要考虑在何时对这个future的结果进行处理, 也就是说在main方法中用户必须在一个地方调用
future.get()方法来获取结果并处理, 这个情况是会导致阻塞的(异步任务没执行完), 所以在这个方法后面的代
码仍然会得不到执行, 可以这样来优化, 我们对这个结果的处理比如输出结果, 将这个操作封装成一个方法, 当
异步任务执行完毕后会自动调用这个方法, 这也叫回调
```

## Version Three
```java
一般来说, 异步任务执行完毕之后都需要调用一个回调函数来对任务的结果进行处理的, 这里我们就抛弃Future
设计模式来对用回调实现异步任务

// FutureTask, 将异步任务放入call方法中, 由于异步任务的结果类型不确定, 所以用泛型表示
@FunctionalInterface
public interface FutureTask<T> {
	T call();
}

// 回调接口, 用法是, 在异步任务执行完毕后调用callback方法, 同时将结果作为泛型传进去
@FunctionalInterface
public interface Callback<T> {
	void callback(T t);
}

// Executor
public class Executor {
  // 用户传进来一个异步任务和一个回调函数, 当异步任务执行完毕后调用回调函数
	public <T> void execute (FutureTask<T> task, Callback<T> callback) {
		// 利用一个新的线程来执行用户的异步任务
		new Thread( () -> {
			// 执行异步任务并获取结果
			T result = task.call();
			callback.callback( result );
		} ).start();

	}
}

// 测试类
public class TestClass {
	public static void main (String[] args) throws InterruptedException {
    Executor executor = new Executor();
    // 传入一个异步任务和回调函数
		executor.execute( () -> {
			// 异步任务, 利用sleep方法来模拟异步任务需要执行一段时间
			try {
				Thread.sleep( 5000 );
			} catch (InterruptedException e) {}

			// 返回结果
			return "Result...";
		}, (result) -> {
			System.out.println( result );
		} );

		// 上面的代码回立马执行, 而上面的异步任务是其它线程执行的, 回调函数是用来处理异步任务结果的,
		//这里用sleep来模拟主线程在继续执行, 不会收到阻塞
		System.out.println( "===========主线程在执行任务=========" );
		Thread.sleep( 3000 );
		System.out.println( "===========主线程执行任务完毕=========" );
	}
}
```
