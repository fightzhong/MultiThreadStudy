## Exchanger
```
Exchanger使用的场景是两个线程之间进行信息的交换, 必须是两个线程之间, 举个例子, 两个商家互相交换物品,
商家A先到达了交换物品的地方, 而商家B还没到达, 所以商家A必须在交换物品的地方进行等待, 直到商家二到达
了目的地, 两者进行物品的交换, Exchanger就实现了这样的功能, 在构造Exchanger对象的时候通过泛型的方
式来执行交换的物品类型, 然后通过exchange方法来进行交换物品, 如果一个线程执行到了该方法而另一个线程
还没有执行到该方法则第一个线程会进入等待状态
```

## 例一
```java
public static void main (String[] args) {
  Exchanger<Object> exchanger = new Exchanger<>();

  Thread t1 = new Thread( () -> {
    try {
      Object mes = new Object();
      System.out.println( "线程一发送的信息: " + mes );
      Object result = exchanger.exchange( mes );
      System.out.println( "线程一收到线程二的信息: " + result );
    } catch (InterruptedException e) {}
  } );

  Thread t2 = new Thread( () -> {
    try {
      TimeUnit.SECONDS.sleep( 3 );
      Object mes = new Object();
      System.out.println( "线程二发送的信息: " + mes );
      Object result = exchanger.exchange( mes );
      System.out.println( "线程二收到线程一的信息: " + result );
    } catch (InterruptedException e) {}
  } );

  t1.start();
  t2.start();
}

输出结果:
  线程一发送的信息: java.lang.Object@76ee89dd
  线程二发送的信息: java.lang.Object@2d46c162
  线程二收到线程一的信息: java.lang.Object@76ee89dd
  线程一收到线程二的信息: java.lang.Object@2d46c162

分析:
  <1> 如果上面出现了三个线程调用exchanger.exchange方法, 那么必定会有一个线程一直处于阻塞状态, 因
      为没有一个线程会来跟他交换信息, 所以Exchanger是基于两个线程之间的, 必须成对出现
  <2> 线程二在开始执行的时候休眠了3秒钟, 而在这三秒内线程一已经执行到了exchange方法了, 所以线程一
      此时会进入等待状态
  <3> 我们发现线程二收到的对象的地址跟线程一发送的对象的地址是一致的, 所以说对象的交换可能会存在安
      全问题, 因为接收方和发送方的对象引用一致
  <4> Exchanger对象可以被多次复用, 也就是说每两个线程交换完后还可以继续之后到达了目的地两个线程继
      续交换信息
```
