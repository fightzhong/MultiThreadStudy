## 引入
```
ReentrantReadWriteLock可重入的读写锁, 在线程的设计模式中我自己实现了一个读写锁的设计模式, 跟这个
可重入的读写锁是类似的, 在ReentrantReadWriteLock中, 将读锁和写锁进行了分离, 即在其里面会有两个静
态内部类ReadLock, WriteLock, 这两个内部类都是继承于Lock接口的, 下面我们就看一下两个读写锁的例子就
可以了
```
## read && read
```java
// 下面是一段很简单的代码......
public static void main (String[] args) throws InterruptedException {
  ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
  // 数据保存区
  ArrayList<Integer> data = new ArrayList<>();
  // 获取读锁和写锁
  Lock readLock = readWriteLock.readLock();
  Lock writeLock = readWriteLock.writeLock();

  // 写线程
  new Thread( () -> {
    writeLock.lock();
    System.out.println( Thread.currentThread().getName() + ": 正在写入数据" );
    data.add( 66666 );
    // 通过sleep来模拟写操作需要一段时间
    try {
      Thread.sleep( 3000 );
    } catch (InterruptedException e) {}
    System.out.println( Thread.currentThread().getName() + ": 写入数据完成" );
    writeLock.unlock();
  }, "写线程" ).start();

  // 我们确保写线程先获取到锁, 因为读操作只有一个, 所以我们需要确保里面有数据
  Thread.sleep( 50 );

  // 读线程一
  new Thread( () -> {
    readLock.lock();
    System.out.println( Thread.currentThread().getName() + ": 读取数据 => " + data );
    try {
      // 通过sleep来模拟读操作需要一段时间
      Thread.sleep( 3000 );
    } catch (InterruptedException e) {}
    System.out.println( Thread.currentThread().getName() + "读取完成" );
    readLock.unlock();
  }, "读线程一" ).start();

  // 读线程二
  new Thread( () -> {
    readLock.lock();
    System.out.println( Thread.currentThread().getName() + ": 读取数据 => " + data );
    try {
      Thread.sleep( 3000 );
    } catch (InterruptedException e) {}
    System.out.println( Thread.currentThread().getName() + "读取完成" );
    readLock.unlock();
  }, "读线程二" ).start();
}

结果分析:
  写线程: 正在写入数据
  写线程: 写入数据完成
  读线程一: 读取数据 => [66666]
  读线程二: 读取数据 => [66666]
  读线程一读取完成
  读线程二读取完成

我们发现在写进程在运行的过程中, 读进程没有输出任何一句话, 当写进程完成时, 两个读进程同时输出了读取
数据这句话, 即读进程可以同时进行, 读写互斥
```
