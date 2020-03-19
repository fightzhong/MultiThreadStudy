## 分析objectMonitor.hpp(头文件)中的内容
- ObjectWaiter类
```C++
当一个线程在synchronized代码块中调用了wait方法后, 会被放入该锁对应的Monitor对象中的waitset等待集
合中, ObjectWaiter就是将该线程进行了一下封装, 并提供了其它的属性而已, waitset是一个双向循环链表

class ObjectWaiter : public StackObj {
 public:
  enum TStates { TS_UNDEF, TS_READY, TS_RUN, TS_WAIT, TS_ENTER, TS_CXQ } ;
  enum Sorted  { PREPEND, APPEND, SORTED } ;
  ObjectWaiter * volatile _next; // 指向前一个等待的ObjectWaiter
  ObjectWaiter * volatile _prev; // 指向后一个等待的ObjectWaiter
  Thread*       _thread; // 当前线程
  jlong         _notifier_tid;
  ParkEvent *   _event;
  volatile int  _notified ;
  volatile TStates TState ; // 线程对应的状态
  Sorted        _Sorted ;           // List placement disposition
  bool          _active ;           // Contention monitoring is enabled
 public:
  ObjectWaiter(Thread* thread);

  void wait_reenter_begin(ObjectMonitor *mon);
  void wait_reenter_end(ObjectMonitor *mon);
};
```

- ObjectMonitor监视器对象(仅仅分析其中一小部分的定义)
```C++
class ObjectMonitor {
  // 线程用到的一些信息用枚举表示
 public:
  enum {
    OM_OK,                    // no error
    OM_SYSTEM_ERROR,          // operating system error
    OM_ILLEGAL_MONITOR_STATE, // IllegalMonitorStateException
    OM_INTERRUPTED,           // Thread.interrupt()
    OM_TIMED_OUT              // Object.wait() timed out
  };

  void*     owner() const; // 拥有该Monitor对象的线程
  void      set_owner(void* owner); // 设置拥有该Monitor对象的线程

  // JVM/DI GetMonitorInfo() needs this
  ObjectWaiter* first_waiter()                { return _WaitSet; } // 前一个等待的线程
  ObjectWaiter* next_waiter(ObjectWaiter* o)  { return o->_next; } // 后一个等待的线程
  Thread* thread_of_waiter(ObjectWaiter* o)   { return o->_thread; } // 获取ObjectWaiter中的线程

  // initialize the monitor, exception the semaphore, all other fields
  // are simple integers or pointers
  ObjectMonitor() {
    _WaitSet      = NULL;  // 当一个线程调用了wait方法后, 放入等待集合中
    _EntryList    = NULL ; // 当一个线程处于等待锁的状态的时候, 则被置于EntryList中
    _SpinClock    = 0 ; // 自旋锁
  }

public:
  bool      try_enter (TRAPS) ;
  void      enter(TRAPS);
  void      exit(bool not_suspended, TRAPS);
  void      wait(jlong millis, bool interruptable, TRAPS); // 调用Object的wait方法对应的native方法
  void      notify(TRAPS); // 调用Object的notify方法对应的native方法
  void      notifyAll(TRAPS); // 调用Object的notifyAll方法对应的native方法

 private:
  void      AddWaiter (ObjectWaiter * waiter) ; // 添加一个线程到waitset等待集合中
  ObjectWaiter * DequeueWaiter () ; // 从等待集合中取走一个线程

 protected:
  ObjectWaiter * volatile _EntryList ;     // Threads blocked on entry or reentry.

 protected:
  volatile intptr_t  _waiters;      // number of waiting threads

 protected:
  ObjectWaiter * volatile _WaitSet; // LL of threads wait()ing on the monitor

 private:
  volatile int _WaitSetLock;        // protects Wait Queue - simple spinlock
};
```

### 分析objectMonitor.cpp文件(对头文件定义的方法进行实现)
- objectMonitor.wait()
```C++
当我们在synchronized方法中调用wait方法的时候, 其实调用的就是objectMonitor.wait()方法, 其里面有
这几行代码, 我就不进行翻译了, 有时候英文比中文更好理解:

// create a node to be put into the queue
// Critically, after we reset() the event but prior to park(), we must check
// for a pending interrupt.
ObjectWaiter node(Self);
node.TState = ObjectWaiter::TS_WAIT ;
Self->_ParkEvent->reset() ;
OrderAccess::fence();          // ST into Event; membar ; LD interrupted-flag

// Enter the waiting queue, which is a circular doubly linked list in this case
// but it could be a priority queue or any data structure.
// _WaitSetLock protects the wait queue.  Normally the wait queue is accessed only
// by the the owner of the monitor *except* in the case where park()
// returns because of a timeout of interrupt.  Contention is exceptionally rare
// so we use a simple spin-lock instead of a heavier-weight blocking lock.

Thread::SpinAcquire (&_WaitSetLock, "WaitSet - add") ;
AddWaiter (&node) ;
Thread::SpinRelease (&_WaitSetLock) ;
```

- AddWaiter方法
```C++
objectMonitor中的wait方法中调用了一个AddWaiter方法, 如下, 其实我们很容易看懂, 就是对一个双向循环
队列中插入一个ObjectWaiter而已, 而这个双向循环队列就是waitset:

inline void ObjectMonitor::AddWaiter(ObjectWaiter* node) {
  assert(node != NULL, "should not dequeue NULL node");
  assert(node->_prev == NULL, "node already in list");
  assert(node->_next == NULL, "node already in list");
  // put node at end of queue (circular doubly linked list)
  if (_WaitSet == NULL) {
    _WaitSet = node;
    node->_prev = node;
    node->_next = node;
  } else {
    ObjectWaiter* head = _WaitSet ;
    ObjectWaiter* tail = head->_prev;
    assert(tail->_next == head, "invariant check");
    tail->_next = node;
    head->_prev = node;
    node->_next = head;
    node->_prev = tail;
  }
}
```

## 总结
```
java中在synchronized代码块中调用对象的wait方法时, 最终会调用到该对象的Monitor对象即ObjectMonitor
的wait方法, 在该wait方法中, 会将该线程封装成一个ObjectWaiter, 并放入waitset这个双向循环链表中,
notify方法就不进行分析了, 其实就是从waitset中取出一个线程(有多种策略, 表示取出的哪个位置的ObjectWaiter),
然后放入EntryList中, 根据头文件中的定义, EntryList: Threads blocked on entry or reentry.
```
