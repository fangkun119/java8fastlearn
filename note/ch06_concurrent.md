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
> ```

## 6.2 ConcurrentHashMap改进

> ConcurrentHashMap线程安全、同时容许多个线程并发更新哈希表的不同部分，且不会相互阻塞

### 6.2.0 设计更新

> * `mappingCount()方法`：返回long解决超大型哈希量导致size()方法32位整形数溢出的问题
>
> * `块状hash拉链设计`：对于相同的hash值、可以以`log(n)`的时间复杂度搜索拉链中的数据块（需要实现Comparable接口），用来处理分布不均的情况

### 6.2.1 更新值

> 完整代码：[../code/ch6/sec02/UpdatingValues.java](../code/ch6/sec02/UpdatingValues.java)

#### (1) 不能保证原子性的方法组合：`put`和`get`

> ```java
> // 虽然ConcurrentHashMap保证了get和put不会因为并发操作而破坏HashMap的数据结构
> // 但是get和put并没有放到一个原子操作中
> Long oldValue = concurrentHashMap.get(word);
> Long newValue = oldValue == null ? 1 : oldValue + 1;    
> concurrentHashMap.put(word, newValue); // Error—might not replace oldValue
> ```

#### (2) 使用基于乐观锁的方法`replace`来更新

> ```java
> do {
>    oldValue = concurrentHashMap.get(word);
>    newValue = oldValue == null ? 1 : oldValue + 1;
> } while (!concurrentHashMap.replace(word, oldValue, newValue));
> ```

#### (3) 用`LongAdder`的原子属性来确保线程安全

> ```java
> ConcurrentHashMap<String, LongAdder> map2 = new ConcurrentHashMap<>();
> map2.putIfAbsent(word, new LongAdder()); // 保证已经存在了一个LongAdder
> map2.get(word).increment();              // 调用LongAdder的原子自增
> // 上面两行代码可以合并成一个
> map2.putIfAbsent(word, new LongAdder()).increment();
> ```

#### (4) 传入lambda表达式并原子执行：`compute`

> ```java
> // lambda表达式封装操作的内容、及value初始值设置以及更新
> // compute、computeIfAbsent, computeIfPresent方法保证该操作被原子执行
> concurrentHashMap.compute(word, (k, v) -> v == null ? 1 : v + 1);
> map2.computeIfAbsent(word, k -> new LongAdder()).increment();
> map2.computeIfPresent(word, (k,v) -> v).increment();
> ```

#### (5) 传入lambda表达式并原子执行：merge

> ```java
> // 与compute类似，但是lambda表达式只需要提供更新操作，而value初始值可以通过另一个参数传入
> concurrentHashMap.merge(
>     word,  // key
>     1L,    // value缺失时的hash初始值
>     (existingValue, newValue2) -> existingValue + newValue2 // value更新操作
> );
> concurrentHashMap.merge(word, 1L, Long::sum);
> ```

### 6.2.2 批量数据操作

#### 6.2.2.1 介绍

批量数据操作

> * 可以去其他线程的操作同时执行
> * 不需要将Map冻结成一个快照
> * 在此期间读线程读出来的value应当被看做是一个近似值（即有可能已被修改、也有可能未被修改的值）

三类批量数据操作

> * `search`：对所有满足条件的key或value应用一个函数
> * `reduce`：应用accumulator函数将所有key和value组合起来
> * `forEach`：对所有key和（或）value应用一个函数

每个操作都有三个版本，共12种组合

> |            | search        | reduce        | forEach      |
> | ---------- | ------------- | ------------- | ------------ |
> | key        | searchKeys    | reduceKeys    | forEachKey   |
> | value      | searchValues  | reduceValues  | forEachValue |
> | key, value | search        | reduce        | forEach      |
> | entries    | searchEntries | reduceEntries | forEachEntry |

并行阈值：

> 只有当Map的`元素数量`超过`并行阈值`时，批量操作才会以并行的方式执行

例子

> ```java
> // 容量超过1就并行，保证始终以并行方式运行
> long threshold = 1; 
> 
> // search
> String result = word2cntConcurrentHashmap.search(threshold, (k, v) -> v > 1000 ? k : null);
> 		// the
> 
> // forEach
> word2cntConcurrentHashmap.forEach(
>         threshold,
>         (k, v) -> System.out.print(k + " -> " + v + ", "));
> 		// govern -> 1, numerous -> 1, England -> 1, ......
> 
> word2cntConcurrentHashmap.forEach(
>         threshold,                       // key
>         (k, v) -> k + " -> " + v + ", ", // Transformer
>         System.out::print);              // Consumer
> 		// govern -> 1, numerous -> 1, England -> 1, ......
> 
> word2cntConcurrentHashmap.forEach(threshold,
>         (k, v) -> v > 300 ? k + " -> " + v : null, // Filter and transformer
>         System.out::println); // The nulls are not passed to the consumer
>         // I -> 545
>         // a -> 672
>         // ...
> 
> // reduceKeys
> Integer maxlength = word2cntConcurrentHashmap.reduceKeys(threshold,
>         String::length, // Transformer
>         Integer::max);  // Accumulator
> 		// maxlength: 16
> 
> // reduceValues
> Long sum = word2cntConcurrentHashmap.reduceValues(threshold, Long::sum);
> 		// sum: 30420
> 
> 
> Long count = word2cntConcurrentHashmap.reduceValues(threshold,
>         v -> v > 300 ? 1L : null,
>         Long::sum);
> System.out.println("count: " + count);
> // count: 13
> 
> long sum2 = word2cntConcurrentHashmap.reduceValuesToLong(threshold,
>         Long::longValue, // Transformer to primitive type
>         0, // Default value for empty concurrentHashMap
>         Long::sum); // Primitive type accumulator
> 		// sum2: 30420
> ```
>
> 完整代码：[../code/ch6/sec02/BulkOperations.java](../code/ch6/sec02/BulkOperations.java)

### 6.2.3 Set视图

> java没有提供ConcurrentHashSet这样的类，java 8用以下两种方式提供支持

#### (1) 用静态方法`ConcurrentHashMap.<K>newKeySet()`新建一个支持并发的`Set<T>`

> 其实这个Set的底层是用`ConcurrentHashMap<K, Boolean>`实现的，但是提供的方法是`Set<K>`的方法
>
> ```java
> Set<String> words = ConcurrentHashMap.<String>newKeySet();
> ```

#### (2) 用方法`keySet(V)`将已有的`ConcurrentHashMap<K,V>`映射成`Set<K>`并共享底层数据

> * 当从`Set<K>`中删除一个Key时，对应的`<K, V>`也会从`ConcurrentHashMap<K,V>`中删除
> * 向这个`Set<K>`中添加元素时，会以调用`keySet(V defaultValue)`时传入的默认值来向底层的`ConcurrentHashMap<K,V>`中添加数据
>
> ```java
> ConcurrentHashMap<String, Long> map = new ConcurrentHashMap<>();
> words = map.keySet(1L); //默认值 1
> words.add("Java");
> System.out.println(map.get("Java"));
> // 输出: 1
> ```

## 6.3 并行数组操作

(1) `Arrays.parallelSort(T[])`

> 以多线程的方式对数组进行排序，还可以传入Comparator，可以指定对数组的哪一段进行排序
>
> ```java
> Arrays.parallelSort(strArray);
> // [, , , , ...... , youth, youth, youth, zigzag, zip]
> ```

(2) `Arrays.parallelSetAll(T[], UnaryOperator)`

> 用传入的lambda表达式，根据数组下标计算出一个值填充到数组中
>
> ```java
> int[] values = new int[20];
> Arrays.parallelSetAll(values, i -> i % 10);
> System.out.println(Arrays.toString(values));
> // [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
> ```

(3) `Arrays.parallelPrefix(T[], BinaryOperator)`

> 用数组从下标0到当前下标i的累积运算（由传入的lambda表达式指定）计算出来的值填充数组
>
> ```java
> // 用数组从下标0到当前下标i的累积运算（由传入的lambda表达式指定）计算出来的值填充数组
> Arrays.parallelSetAll(values, i -> i + 1);
> System.out.println(Arrays.toString(values));
> // [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]
> Arrays.parallelPrefix(values, (x, y) -> x * y);
> System.out.println(Arrays.toString(values));
> // [1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800, 39916800, 479001600, 1932053504, 1278945280, 2004310016, 2004189184, -288522240, -898433024, 109641728, -2102132736]
> ```

## 6.4 `CompletableFuture<T>`

### 6.4.1 Java 6中的`Future`

> 只能通过调用`get()`方法一直阻塞直至结果可用，不能做到完全异步

### 6.4.2 `CompletableFuture`

> 可以设置一个回调函数给`CompletableFuture`，然后进行其他操作。当CompletableFuture可用时，会调用回调函数，做到完全异步
>
> ```java
> class Util {
>     ...
>     
>     // action: 执行的慢速阻塞操作
>     // until：判断操作完成的条件，如果完成则返回，否则重试
>     public static <T> CompletableFuture<T> repeat(Supplier<T> action,  Predicate<T> until) {
>         return CompletableFuture.supplyAsync(action).thenComposeAsync((T t) -> {
>             return until.test(t) ? CompletableFuture.completedFuture(t) : repeat(action, until);
>         });
>     }
> }
> ```
>
> 



