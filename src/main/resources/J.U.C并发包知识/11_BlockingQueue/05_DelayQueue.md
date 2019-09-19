## DelayQueue
```
DelayQueue延迟队列, 里面是通过内置一个PriorityBlockingQueue来实现的, 在延迟队列中, 需要提供每个
元素的延迟时间, 举个例子, 元素A延迟时间为5秒, 那么在将该元素放入该队列后, 5秒钟内是不可以将其取出的,
利用PriorityBlockingQueue无边界及按照一定规则排列的特点, DelayQueue通过元素的延迟时间来对元素进行
排列, 对于DelayQueue来说, 需要实现两个方法, 一个是getDelay方法(Delayed接口), 该方法是获取该元素
期望的延迟时间与当前时间的差, 即获取剩余的延迟时间, 如上面的例子, 如果在第3秒的时候调用该元素的
getDelay方法, 那么获取的剩余延迟时间就是2秒, 第二个是compareTo方法, 用于对比两个元素的大小, 因为
PriorityBlockingQueue需要通过该方法来对元素进行排序, 下面我们先对DelayQueue的插入和取出的方法进行
描述:

插入方法:
  offer(E e): 往DelayQueue中插入一个元素, 其实就是往该队列中的PriorityBlockingQueue中插入一个元
              素, 由于PriorityBlockingQueue是一个无边界的队列, 所以插入操作不会受到阻塞
  add(E e), put(E e): 这两个方法在内部是仅仅调用了offer方法而已, 所以等价于offer方法

取出方法:
  poll(): 从延迟队列中取出一个元素, 是一个不阻塞的方法, 如果队首的元素的延迟时间还没到或者队列中没
          有元素则该方法返回null
  remove(): 内部调用了poll方法, 对poll方法的返回值进行了判断, 如果为null则报错, 如果不为null则将
            结果返回
  take(): 从延迟队列中取出一个元素, 是一个阻塞的方法, 如果队首的元素的延迟时间还没到或者队列中没有
          元素则当前线程回进入阻塞

```

## DelayQueue的实践
```java
/******************实现了Delayed接口的类*****************/
class DelayElement<T> implements Delayed {
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

/******************测试代码*****************/
public static void main (String[] args) throws InterruptedException {
  // 创建一个DelayElement元素, 设置延迟时间为5秒, 即5秒内如果线程去获取则会陷入阻塞
  DelayElement<String> ele1 = new DelayElement<>( "ele1", 5000 );

  DelayQueue<DelayElement<String>> queue = new DelayQueue<>();
  queue.add( ele1 );

  // 会陷入阻塞5秒钟, 之后输出
  queue.take();
}
```
