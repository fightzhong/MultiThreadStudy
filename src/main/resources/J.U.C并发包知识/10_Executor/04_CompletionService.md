## CompletionService
```java
CompletionService类一般用于执行一批任务, 然后需要根据任务执行的时间, 先获取执行完的任务, 并对先
执行完的任务进行处理, 由于ExectorService在执行批量任务的时候无法判断哪个任务先执行完毕, 假如调用
invokeAll又因为该方法是阻塞的方法, 所以在所有任务执行完毕前会一直阻塞, 利用CompletionService, 对
一批任务进行执行的时候, 会维持一个队列, 每一个任务在执行完毕后的结果Future都会依次放入该队列中, 从
而使得能够依次获取先执行完毕的任务

private class QueueingFuture extends FutureTask<Void> {
    QueueingFuture(RunnableFuture<V> task) {
        super(task, null);
        this.task = task;
    }
    protected void done() { completionQueue.add(task); }
    private final Future<V> task;
}

该内部类继承了FutureTask, 对FutureTask进行了一下封装而已, 其实就是重写了done方法, 使得在任务执行
完毕后会自动的将任务的Future放入到完成队列中

RunnableFuture<V> newTaskFor(Callable<V> task),
RunnableFuture<V> newTaskFor(Runnable task, V result): 这两个方法用于将传进来的Runnable或者
                                                       Callable任务封装成RunnableFuture, 对
                                                       于RunnableFuture来说, 其就是实现了
                                                       Runnable和Future接口

ExecutorCompletionService(Executor executor),
ExecutorCompletionService(Executor executor, BlockingQueue<Future<V>> completionQueue)
构造方法, 需要传入一个Executor用于执行任务, 同时可以传入一个完成队列用于保存完成的任务的Future

submit(Callable<V> task),
submit(Runnable task, V result): 这两个方法用于提交任务, 跟ThreadPoolExecutor类似

take: 获取完成队列中的第一个完成的任务, 如果还没有完成的任务则当前线程进入阻塞状态

poll(),
poll(long timeout, TimeUnit unit): 获取完成队列中的第一个完成的任务, 如果还没有完成的任务则返回
                                    null, 不会进入阻塞, 重载方法中增加了超时机制
```
