## ScheduledThreadPoolExecutor
```java
ScheduledThreadPoolExecutor继承于ThreadPoolExecutor, 在其基础上实现了定时任务的效果, 并且该类
实现了ScheduledExecutorService接口, 对于该接口来说, 仅仅定义了四个方法, 下面对这四个方法进行描述

ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit)
ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit):
  这两个方法用于实现延迟任务的执行, delay表示延迟的时间, 在该时间到达后才去执行callable或者runnable
  任务, 并且返回一个Future, 对于runnable任务来说, 返回的future是没有用的

scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit)
  重复执行任务, initialDelay表示初始的延迟时间即第一次执行任务前的延迟initialDelay的时间, period
  表示周期时间即每隔period的时间执行一次该任务, unit即前面两个时间的单位

scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit)
  仍然是重复的执行任务, 不同的是上面那个方法表示一个任务执行的时间是period, 如果在规定的时间内执行
  完毕仍然需要等待到period才能继续下一次执行, 而当前这个方法表示每次执行任务前都需要等待delay的时间,
  等待完成后才执行任务

对于ScheduleExecutorService来说, 存在一个问题, 那就是这个定时性不稳定, 如果规定一个任务的执行周期
即period为5秒, 此时若任务执行的时间超过了5秒如10秒, 那么会等到该任务执行完毕才开启下一个任务, 而不是
严格的遵守每隔5秒就开启一个任务的执行, 这个跟JDK自带的Timer是类似的, 都存在这个缺点
```
