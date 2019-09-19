## PriorityBlockingQueue
```
总结: PriorityBlockingQueue是一个动态数组构成的队列, 可以动态的扩容, 无边界, 但是初始的时候可以指
      定数组的初始大小, 当达到数组大小的时候会进行扩容

构造方法:
  PriorityBlockingQueue(),
  PriorityBlockingQueue(int initialCapacity),
  PriorityBlockingQueue(int initialCapacity, Comparator<? super E> comparator):
    创建一个优先阻塞队列, 可以指定队列的初始大小, 如果队列中存放的元素没有实现Comparable接口, 则
    需要传入一个comparator函数式接口并实现compare方法

增加方法:
  add(T t), offer(T t), put(T t): 三者均调用的是offer方法, 由于是一个无边界阻塞队列, 所以在增加
                                  元素的时候需要被阻塞住

删除方法:
  poll(): 从阻塞优先队列中提取队头的元素, 如果没有元素则返回null
  poll(long timeout, TimeUnit unit): 从阻塞优先队列中提取队头的元素, 如果没有元素则等待一段时间,
                                    如果这段时间内有元素增加进去了, 则会被唤醒, 并拿到元素
  take(): 从阻塞优先队列中提取队头的元素, 如果没有元素则陷入等待
  remove(): 从阻塞优先队列中提取队头的元素, 如果没有元素则报错
  peek(): 查看队头的元素, 如果没有元素则返回null
  element(): 查看队头的元素, 如果没有元素则报错

其它方法:
  remainingCapacity(): 查看阻塞队列剩余的容量
  contains(Object o): 查看阻塞队列中是否存在元素o
  drainTo(Collection<T> collection): 将阻塞队列中的元素全部放入到一个集合中
  drainTo(Collection<T> collection, int count): 将阻塞队列中的元素放入count个到一个集合中
```











