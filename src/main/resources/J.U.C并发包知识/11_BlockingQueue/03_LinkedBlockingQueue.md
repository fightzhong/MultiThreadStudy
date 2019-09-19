## LinkedBlockingQueue
```
总结: LinkedBlockingQueue可以有边界也可以无边界, 如果在构造这个对象的时候传入了capacity参数, 那么
      就变成了一个有边界的LinkedBlockingQueue, 如果没有传入, 则是一个无边界的LinkedBlockingQueue,
      有边界的LinkedBlockingQueue跟ArrayBlockingQueue类似

增加方法:
  offer(T t): 往阻塞队列中添加元素, 添加成功则返回true, 如果队列已经满了则返回false
  offer(T t, long time, TimeUnit): 往阻塞队列中添加元素, 如果队列已经满了, 则等待time时间再添加
  add(T t): 往阻塞队列中添加元素, 如果队列已经满了则报错
  put(T t): 往阻塞队列中添加元素, 如果队列已经满了则阻塞

删除方法:
  T poll(): 从阻塞队列的队头中提取一条元素, 如果队列中没有元素了, 则返回null
  T take(): 从阻塞队列的队头中提取一条元素, 如果队列中没有元素了, 则调用该方法的线程进入阻塞状态
  T remove(): 从阻塞队列的队头中提取一条元素, 如果队列中没有元素了, 则抛出一个NoSuchElementException
              异常, 其实其内部调用的就是poll方法, 当poll方法返回null的时候则抛出异常
  boolean remove(Objcet o): 从阻塞队列中提取元素o, 如果提取成功则返回true, 否则返回false
  peek(): 查看队头的元素, 即获取队头的元素, 如果阻塞队列是空的, 则返回null
  element():查看队头的元素, 即获取队头的元素, 如果阻塞队列是空的, 则抛出NoSuchElementException异常

其它方法:
  remainingCapacity(): 查看阻塞队列剩余的容量
  contains(Object o): 查看阻塞队列中是否存在元素o
  drainTo(Collection<T> collection): 将阻塞队列中的元素全部放入到一个集合中
  drainTo(Collection<T> collection, int count): 将阻塞队列中的元素放入count个到一个集合中
```



















