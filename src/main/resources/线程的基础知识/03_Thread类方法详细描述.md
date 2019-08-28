## 构造方法
```
new Thread(): 创建一个线程
new Thread(Runnable target): 创建一个线程, 并指定线程的执行策略方法
new Thread(Runnable target, String name): 创建一个线程并指定线程的执行策略方法和线程名
new Thread(String name): 创建一个线程并指定线程的名字
new Thread(ThreadGroup group, Runnable Target): 创建一个线程并指定线程所在的线程组和执行策略
new Thread(ThreadGroup group, Runnable Target, String name): 
  创建一个线程并指定线程所在的线程组、执行策略以及线程的名字
new Thread(ThreadGroup group, Runnable Target, String name, long stackSize):
  创建一个线程并指定线程所在的线程组、执行策略、线程的名字以及线程独占的虚拟机栈大小
new Thread(ThreadGroup group, String name): 创建一个线程并指定线程所在的线程组、线程的名字
Thread​(ThreadGroup group, Runnable target, String name, long stackSize, boolean inheritThreadLocals)
```

## 实例方法
```
守护线程: 当一系列线程同属于一个线程组的时候, 如果所有非守护线程执行完毕, 那么守护线程不管在执行什
         么都会退出, 根据文档描述, 虚拟机将会退出当仅仅只有守护线程在运行时

setDaemon(boolean b): 用于设置该线程是否是属于守护线程, 如果不主动设置的话, daemon成员变量的值会
                      默认继承其父线程的值

getId/getName/getPriority: 获取线程的ID(类似于进程的PID)、线程的名字、线程的优先级

join: 表示将该调用该方法的线程A加入到原来的执行线程B中去执行, 只有等到A执行完毕才能执行B线程中
      A.join()方法后面的代码, 相当于对B线程进行了阻塞
  join(millis): 表示A线程最多在B线程中阻塞B线程执行millis毫秒, 如果传入1000则A线程在B线程中执行
                1秒钟, 之后B线程就会继续执行, 如果此时A线程没有结束也会继续执行
  join(millis, nano): 跟上面的方法一样, 不过这个方法可以精确到纳秒
  join(): 不传参数则会一直阻塞到A线程执行完毕

interrupt: A.interrupt()则会向A线程发出一个中断信号, 则A线程中处于因调用wait, sleep, join方法而
            进入阻塞时可以通过该方法进行唤醒

Thread.interrupted(): 判断当前线程是否有中断信号(static)
A.isInterrupted(): 判断当前线程是否有中断信号(前提是A必须是Thread的子类)

Thread.currentThread().setUncaughtExceptionHandler( (thread, ex) -> {
  // 对发生错误的线程thread和错误信息对象ex进行信息获取并处理
} ): 用于捕捉线程的错误信息(防止其输出到控制台)
```

## wait和sleep方法的区别
```
<1> 可以通过调用wait(millis)来达到等待一段时间的效果, 但是wait是采用wait()方法的形式来使用的, 而
    通过该方式就是让线程一直等待下去, 直到其它线程能够唤醒它
<2> wait方法必须要有锁的支持, 即必须在同步代码块或者同步方法内才行, 并且调用该方法必须是该同步锁来
    调用, 否则会出现IllegalMonitorStateException异常, 而sleep是Thread类的静态方法, 所以其可以
    直接调用, 不需要锁的参与
<3> wait方法在调用时会释放自己持有的对应的锁, 比如LOCK.wait()释放的就是LOCK锁, 而sleep方法是不会
    释放锁的, 在其休眠结束前会一直持有该锁, 并且wait()方法必须由其它线程执行LOCK.notify()或者
    LOCK.notifyAll()方法来唤醒它
```

## 线程组ThreadGroup
```
构造方法:
  ThreadGroup(String name): 创建一个线程组, 并指定名字(默认该线程组的父亲是执行该代码所在线程的线程组)
  ThreadGroup(ThreadGroup parent, String name): 创建一个线程组, 并指定名字和其父线程组

实例方法:
int activeCount(): 返回当前线程组及其子孙线程组中总共的活跃的线程个数

int activeGroupCount(): 返回当前线程组及其子孙线程组中总共的活跃的线程组的个数(线程组可以被销毁)

void destroy(): 销毁当前线程组及其子孙线程组(前提是线程组中必须没有活跃的线程, 否则会抛异常)

int enumerate(Thread[] list): 将当前线程组及其子孙线程组中的活跃线程给放入到list数组中, list数组
                              需要提前创建好, 可以通过group.activeCount()方法来获取个数

int enumerate(Thread[] list, boolean recurse): 同上一个方法类似, 不同的是, 如果recurse为false,
                                               则其子孙进程组中的活跃进程就不会放入list中, 即
                                               不进行递归

int	getMaxPriority(): 获取当前线程组中优先级最大的线程

String	getName(): 获取当前线程组的名字

ThreadGroup	getParent(): 获取当前线程组的父亲线程组

void	interrupt(): 向当前线程组及其子孙线程组的所有线程发出中断信号

void setDaemon(boolean b): 设置当前线程组下的所有线程为守护线程(不包括子孙线程组中的线程)

boolean	isDaemon(): 判断当前线程组是否是守护线程组
```



