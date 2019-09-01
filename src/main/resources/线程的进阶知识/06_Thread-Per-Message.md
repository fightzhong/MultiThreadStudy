## Thread-Per-Message
```java
Thread-Per-Message模式, 思想是每收到一条消息就开启一个线程去单独处理这条消息, 即一条请求一个线程,
一般用在Socket通信上, 线程可以用线程池来给予, 这样就不会消耗太多, 下面是简单的模拟这个模式的大概情
况

/********客户端发来的消息*********/
public class RequestMessage {
	private String message;

	public RequestMessage (String message) {
		this.message = message;
	}

	public String getMessage () {
		return message;
	}
}

/**
 * 处理消息的类, 收到一条消息则开启一个线程去处理
 */
public class RequestMessageHandler {
	public static void handle (RequestMessage message) {
		new Thread( () -> {
			System.out.println( Thread.currentThread().getName() + "  收到消息: " + message.getMessage() );

			// 模拟消息处理一段时间
			try {
				Thread.sleep( 2000 );
			} catch (InterruptedException e) {}

			System.out.println( Thread.currentThread().getName() + "  处理消息完毕...." );
		} ).start();
	}
}

// 服务端模拟收到客户端的消息
public class Server {
	public static void main (String[] args) {
	    // 通过循环的方式模拟客户端发来多条消息
		for ( int i = 0; i < 10; i ++ ) {
			RequestMessage message = new RequestMessage( "消息[ "+ i +" ]" );
			RequestMessageHandler.handle( message );
		}
	}
}
```


