# CH06 并发增强

[TOC]

> 要点包括
>
> * 原子变量操作：`updateAndGet`、`accumulateAndGet`
> * `LongAccumulator`和`DoubleAccumulator`（在高并发场景下比AtomicLong和AtomicDouble更高效）
> * `ConcurrentHashMap`的新增方法
>     * `ConcurrentHashMap.compute`
>     * `ConcurrentHashMap.merge`
>     * `ConcurrentHashMap.search`
>     * `ConcurrentHashMap.reduce`
>     * `ConcurrentHashMap.forEach`
> * `集合视图`：将ConcurrentHashMap当做一个Set来使用
> * `Arrays`的新增方法：排序、填充、前缀操作的并行版本
> * `Future`类的更新及异步操作的创建

## 6.1 原子值和乐观锁

### 6.1.1 Java 5的乐观锁（CAS）

#### 错误用法

> ```java
> // 定义原子变量
> public static AtomicLong largest = new AtomicLong();
> ```
>
> ```java
> // 在多线程中操作AtomicLong
> // Max.max不是原子操作，因此会导致"Error—race condition" !
> largest.set(Math.max(largest.get(), observed));
> ```
>
> 完整代码：[../code/ch6/sec01/AtomicUpdates.java](../code/ch6/sec01/AtomicUpdates.java)

#### 正确用法

> ```java
> // 在多线程中操作AtomicLong
> // 正确的使用方法，使用CAS来进行并发检查
> long oldValue, newValue;
> do {
>     oldValue = largest.get();
>     newValue = Math.max(oldValue, observed);
>     // 如果执行CAS时largest的值变了导致结果会不准确，CAS会返回false并在下一轮循环中重试
> } while (!largest.compareAndSet(oldValue, newValue));
> ```
>
> 完整代码：[../code/ch6/sec01/AtomicUpdates2.java](../code/ch6/sec01/AtomicUpdates2.java)

### 6.1.2 Java 8提供的操作

> 借助lambda表达式，Java8提供了API可以实现上面的while循环，而使用者只需要传入相应的lambda表达式

#### (1) 乐观锁（CAS）封装

##### `updateAndGet和accumulateAndGet`：返回`updated value`

> * `long updateAndGet(LongUnaryOperator updateFunction) `
>
> * `long accumulateAndGet(long x, LongBinaryOperator accumulatorFunction)`

例子

> ```java
> // 更新原子变量
> // 方法1： largest.updateAndGet(x -> Math.max(x, observed));
> // 方法2： largest.accumulateAndGet(observed, Math::max);
> largest.accumulateAndGet(observed, Math::max);
> ```
>
> 完整代码：[../code/ch6/sec01/AtomicUpdates3.java](../code/ch6/sec01/AtomicUpdates3.java)

##### `getAndUpdate`和`getAndAccumulate`：返回`previous value`

##### 更多的原子类

> * `AtomicInteger`、`AtomicIntegerArray`、`AtomicIntegerFieldUpdater`
> * `AtomicLong`、`AtomicLongArray`、`AtomicLongFieldUpdater`
> * `AtomicReference`、`AtomicReferenceArray`、`AtomicReferenceFieldUpdater`

使用乐观锁的问题是，当大量线程访问同一个原子变量时，太多重试导致性能严重下降，而下面的`LongAdder`和`LongAccumulator`就是用来解决这个问题的

#### (2) 乐观锁分组封装

##### `LongAdder` / `DoubleAdder`

> LongAdder / DoubleAdder 也是累加器，但是内部封装了多个原子变量
>
> * 多个线程累加（调用`increment`方法）时，会作用在不同的原子变量上、从而减少互斥和重试
> * 获取累加值（调用`sum`方法）时，再把这些原子变量的值加总
>
> 从而提高计算速度
>
> 例子如下
>
> ```java
> // 3. 使用LongAdder的版本
> System.out.println(LOG_SEP + "LongAdder");
> // (1) LongAdder
> LongAdder adder = new LongAdder();
> // (2) 启动100个线程，并返回计算耗时
> elapsedTime = run(THREADS, ITERATIONS, () -> {
>       adder.increment(); // 让LongAdder执行1000000次increase加操作
>    });
> // (3) 输出结果
> System.out.println(adder.sum());              // 累加和
> System.out.println(elapsedTime + " seconds"); // 计算耗时
> // ---------------------
> // LongAdder
> // 100000000
> // 0.45605228800000003 seconds
> ```
>
> 完整代码：[../code/ch6/sec01/LongAdderDemo.java](../code/ch6/sec01/LongAdderDemo.java)

##### `LongAccumulator` / `DoubleAccumulator`

> 原理与LongAdder / DoubleAccumulator 相同，但是API更加灵活，可以通过lambda表达式传入不同的`LongBinaryOperator`
>
> ```java
> // (1) LongAccumulator
> LongAccumulator accumulator = new LongAccumulator(Long::sum, 0);
> // (2) 启动100个线程，并返回计算耗时
> elapsedTime = run(THREADS, ITERATIONS, () -> {
>    accumulator.accumulate(1); // 让LongAdder执行1000000次加1操作
> });
> // (3) 输出结果
> System.out.println(accumulator.get());        // 累加和
> System.out.println(elapsedTime + " seconds"); // 计算耗时
> ```
>
> ```java
> package java.util.function;
> 
> @FunctionalInterface
> public interface LongBinaryOperator {
>     /**
>      * Applies this operator to the given operands.
>      *
>      * @param left the first operand
>      * @param right the second operand
>      * @return the operator result
>      */
>     long applyAsLong(long left, long right);
> }
> ```

#### (3) 更加底层的乐观锁API：`StampedLock`

原理：与AtomicInt / AtomicLong / AtomicDouble 的CAS相同

读数据的线程

> 先尝试乐观度、调用`tryOptimisticRead`方法并读取数据、该方法会反回一个`stamp`来告知读取是否有效
>
> * 如果无效：说明读数据期间有一个线程（获得写锁并）更新了数据，需要再获取一把读锁重新读取数据
> * 如果有效：使用读到的数据

例子：使用乐观锁封装的Vector

> 完整代码：[../code/ch6/sec01/StampedLockDemo.java](../code/ch6/sec01/StampedLockDemo.java)
>
> 读数据
>
> ```java
> public int size() {
>     // 先尝试乐观读
>     long stamp = lock.tryOptimisticRead();
>     int currentSize = size;
>     // 如果乐观读失败（此时有个线程获得了写锁并更新了数据），则只能拿一把读锁并重新读取数据
>     if (!lock.validate(stamp)) { // Someone else had a write lock
>         // 获取读锁
>         stamp = lock.readLock(); // Get a pessimistic lock
>         // 读数据
>         currentSize = size;
>         // 解锁
>         lock.unlockRead(stamp);
>     }
>     return currentSize;
> }
> ```
>
> 写数据
>
> ```java
> public void add(Object obj) {
>     // 获取写锁并更新数据
>     long stamp = lock.writeLock();
>     try {
>         if (size == elements.length)
>             elements = Arrays.copyOf(elements, 2 * size);
>         elements[size] = obj;
>         size++;
>     } finally {
>         // 解锁
>         lock.unlockWrite(stamp);
>     }
> }
> ```

