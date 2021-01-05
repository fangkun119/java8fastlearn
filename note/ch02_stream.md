# CH02 Stream

[TOC]

> 用来处理集合，可以指定对集合的 操作，而这些操作的时间则交给具体实现来决定，相比固定了特定遍历策略的迭代器，Stream更加灵活，并且可以高效地并发执行。

本章内容要点如下：

> * 从`Collection`、`数组`、`Generator`、`Iterator`创建迭代器
> * 用`filer`选择元素、用`map`改变元素 
> * 用`limit`、`distinct`、`sorted`来改变Stream
> * 用`count`、`max`、`min`、`findFirst`、`findAny`等从Stream中获得结果（部分方法返回`Optional`类型的结果）
> * `Optional`：替代null、更加安全，结合`ifPresent`和`orElse`使用 
> * 收集`Collection`、数组 、字符串、`map`中的 Stream结果
> * 用`Collectors.groupingBy`和`patitioningBy`对Stream分组
> * 针对原始类型`int`、`long`、`double`的Stream
> * `并行Stream`以及如何确保使用时不带有副作用、并考虑放弃排序约束
> * 与Stream API相关的`函数式接口

## 2.1  从迭代器到Stream

### (1) 代码对比 

相比迭代器，用Stream编写的代码将简洁很多，并且还可以并行操作，例如：

> ```java
> // 输入类型是List<String>
> List<String> words = Arrays.asList(contents.split("[\\P{L}]+"));
> 
> // 使用stream
> count = words.stream().filter(w -> w.length() > 12).count();
> System.out.println(count);
> 
> // 使用并行Stream
> count = words.parallelStream().filter(w -> w.length() > 12).count();
> System.out.println(count);
> ```

完整代码：[../code/ch2/sec01/TestCh2Sec01.java](../code/ch2/sec01/TestCh2Sec01.java)

### (2) Stream与Collection的区别

> 1. 不存储元素、元素仍然在底层集合中或按需产生
> 2. `Stream操作`不改变源对象，而是返回持有结果的新的Stream
> 3. `Stream操作`有可能延迟执行，按需执行（例如返回前5个单词，它只会统计前5个词，之后的将停止执行）
> 4. `Stream`只需要告诉“做什么”（如上面的`.filter(w -> w.length()).count()`），而“怎么做”可以通过`.stream()`和 `.parallelStream()`来切换，有更高灵活性和优化机会

###  (3) 创建Stream流水线

#### 阶段一：创建Stream

例如上面的`words.stream()`

> Collection.stream、Stream.of、Arrays.stream、Stream.empty、Stream.generate、Stream.Iterate、Pattern.splitAsStream、Files.lines、……

#### 阶段二：1至多个中间操作

将初始Stream转换为另外的操作，

例如上面的`.filter(w -> w.length() > 12)`

> map (mapToInt, flatMap 等)、 filter、distinct、sorted、peek、limit、skip、parallel、sequential、unordered、……

#### 阶段三：终止操作

它会让之前被延迟的操作立即执行 

例如上面的`.count()`

> forEach、forEachOrdered、toArray、reduce、collect、min、max、count、anyMatch、allMatch、noneMatch、findFirst、findAny、iterator、……

文档：https://www.ibm.com/developerworks/cn/java/j-lo-java8streamapi/

### (4) 运行原理 

> 以`words.stream().filter(w -> w.length() > 12).count()`为例 ，只有在`.count()`被调用时，stream才会执行：
>
> * `.count()`要求`.filter(w -> w.length() > 12)`返回元素给它
> * `.filter(w -> w.length()) > 12)`

## 2.2  阶段1：创建Stream

### (1) 将**Collection**转化为Stream

> ```java
> // 成员函数：Collection.stream
> Stream<String> collectionAsStream = Arrays.asList("str1", "str2", "str3").stream();
> show("Arrays.asList(\"str1\", \"str2\", \"str3\").stream()", collectionAsStream);
> // Arrays.asList("str1", "str2", "str3").stream():
> // [str1, str2, str3]
> ```

### (2) 使用静态方法Stream.of或Arrays.stream将**数组**或**变长参数列表**转化为Stream

> ```java
> // 静态方法 Stream.of(T[])
> Stream<String> words = Stream.of(contentsString.split("[\\P{L}]+"));
> show("Stream.of(String[])", words);
> // Stream.of(String[]):
> // [, Project, Gutenberg, s, Alice, s, Adventures, in, Wonderland, by, ...]
> 
> // 静态方法 Stream.of(T, ...)
> Stream<String> song = Stream.of("gently", "down", "the", "stream");
> show("Stream.of(\"gently\", \"down\", \"the\", \"stream\")", song);
> // Stream.of("gently", "down", "the", "stream"):
> // [gently, down, the, stream]
> 
> // 静态方法Arrays.stream(T[], int, int)
> Stream<String> fromArray = Arrays.stream(new String[]{"idx0", "idx1", "idx2", "idx3"}, 1,  3);
> show("Arrays.stream(new String[]{\"idx0\", \"idx1\", \"idx2\", \"idx3\"}, 1,  3)", fromArray);
> // Arrays.stream(new String[]{"idx0", "idx1", "idx2", "idx3"}, 1,  3):
> // [idx1, idx2]
> ```

### (3) 用静态方法Stream.empty创建**空的Stream**

> ```java
> // 静态方法 Stream.<T>empty()
> Stream<String> silence = Stream.empty();
> show("Stream.<String>empty()", silence);
> // Stream.empty():
> // []
> ```

### (4) 用静态方法Stream.generate或Stream.Iterate创建**无限Stream**

> ```java
> // 静态方法 Stream.generate(Supplier<T> s)
> Stream<String> echos = Stream.generate(() -> "Echo");
> show("Stream.generate(() -> \"Echo\")", echos);
> // Stream.generate(() -> "Echo"):
> // [Echo, Echo, Echo, Echo, Echo, Echo, Echo, Echo, Echo, Echo, ...]
> 
> Stream<Double> randoms = Stream.generate(Math::random);
> show("Stream.generate(Math::random)", randoms);
> // Stream.generate(Math::random):
> // [0.5725437318656325, 0.8921997368683414, 0.28808530234016305, 0.7396191535892872, 0.3667115276892434, 0.3816496115856255, 0.7664167086776515, 0.21315782850833787, 0.20236357124908455, 0.4188788807246814, ...]
> // 静态方法 Stream.iterate(final T seed, final UnaryOperator<T> f)
> 
> Stream<BigInteger> integers = Stream.iterate(BigInteger.ONE, n -> n.add(BigInteger.ONE));
> show("Stream.iterate(BigInteger.ONE, n -> n.add(BigInteger.ONE)", integers);
> // Stream.iterate(BigInteger.ONE, n -> n.add(BigInteger.ONE):
> // [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, ...]
> ```

### (5) 各种类自带的方法也可以产生Stream

> ```java
> // 成员函数 myPattern.splitAsStream(String)
> Stream<String> wordsAnotherWay = Pattern.compile("[\\P{L}]+").splitAsStream(contentsString);
> show("Pattern.compile(\"[\\\\P{L}]+\").splitAsStream(contentsString)", wordsAnotherWay);
> // Pattern.compile("[\\P{L}]+").splitAsStream(contentsString):
> // [, Project, Gutenberg, s, Alice, s, Adventures, in, Wonderland, by, ...]
> 
> // 静态方法 File.lines
> try (Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8)) {
>    show ("Files.lines(path, StandardCharsets.UTF_8)", lines);
> }
> // Files.lines(path, StandardCharsets.UTF_8):
> // [﻿Project Gutenberg's Alice's Adventures in Wonderland, by Lewis Carroll, , This eBook is for the use of anyone anywhere at no cost and with, almost no restrictions whatsoever.  You may copy it, give it away or, re-use it under the terms of the Project Gutenberg License included, with this eBook or online at www.gutenberg.org, , , Title: Alice's Adventures in Wonderland, , ...]
> ```

完整代码：[../code/ch2/sec02/TestCh2Sec02.java](../code/ch2/sec02/TestCh2Sec02.java)

## 2.3 阶段2：`filter`、`map`、`flatMap`方法 

> 阶段2用于将原始流转换成个新的流

完整代码：[../code/ch2/sec03/TestCh2Sec03.java](../code/ch2/sec03/TestCh2Sec03.java)

`filter`：元素挑选

> ```java
> Stream<String> longWords = strList1.stream().filter(w -> w.length() > 12);
> // [conversations, disappointment, Multiplication, inquisitively, uncomfortable, uncomfortable, circumstances, contemptuously, extraordinary, straightening, ...]
> ```

`map`：元素转换

> ```java
> Stream<String> lowercaseWords = strList1.stream().map(String::toLowerCase);
> // [, project, gutenberg, s, alice, s, adventures, in, wonderland, by, ...]
> ```

`flatMap`：将包含多个`流`的`流`、展开成一个`流`

> ```java
> public static Stream<Character> characterStream(String s) {
>    List<Character> result = new ArrayList<>();
>    for (char c : s.toCharArray()) {
>       result.add(c);
>    }
>    return result.stream();
> }
> ```
>
> ```java
> List<String> strList3 = Arrays.asList("row", "row", "row", "your", "boat", "gently", "down", "the", "stream");
> Stream<Character> letters = strList3.stream().flatMap(w -> characterStream(w));
> // [r, o, w, r, o, w, r, o, w, y, ...]
> ```

## 2.4 阶段2：子流（`limit`，` skip`)、组合流（`concat`）及`peek`

完整代码：[../code/ch2/sec04/TestCh2Sec04.java](../code/ch2/sec04/TestCh2Sec04.java)

`limit`：用前k个元素生成子流

> ```java
> Stream<Integer> firstFive = Stream.iterate(0, n -> n + 1).limit(5);
> show("Stream.iterate(0, n -> n + 1).limit(5)", firstFive);
> // Stream.iterate(0, n -> n + 1).limit(5)
> // [0, 1, 2, 3, 4]
> ```

`skip`：跳过前k个元素、用剩余的元素生成子流

> ```java
> Stream<Integer> notTheFirst = Stream.iterate(0, n -> n + 1).skip(1);
> show("Stream.iterate(0, n -> n + 1).skip(1)", notTheFirst);
> // Stream.iterate(0, n -> n + 1).skip(1)
> // [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, ...]
> ```

`concat`：将2个流合并成1个，第1个流不可以是`无限流`

> ```java
> Stream<Character> combined = Stream.concat(characterStream("Hello"), characterStream("World"));
> show("Stream.concat(characterStream(\"Hello\"), characterStream(\"World\"))", combined);
> // Stream.concat(characterStream("Hello"), characterStream("World"))
> // [H, e, l, l, o, W, o, r, l, d]
> ```

`peek`：产生一个与原始流相同的流，但是每返回一个元素时，都可以调用一个函数，便于调试

> ```java
> // peek
> Object[] powers = Stream.iterate(1.0, p -> p * 2)
>    .peek(e -> System.out.println("\t// Fetching " + e))
>    .limit(5).toArray();
> System.out.println("\t// " + Arrays.asList(powers));
> // Fetching 1.0
> // Fetching 2.0
> // Fetching 4.0
> // Fetching 8.0
> // Fetching 16.0
> // [1.0, 2.0, 4.0, 8.0, 16.0]
> ```

##  2.5 阶段2：有状态转换（`distinct`，`sorted`）

完整代码：[../code/ch2/sec05/TestCh2Sec05.java](../code/ch2/sec05/TestCh2Sec05.java)

`distinct`

> ```java
> Stream<String> distinct = strList.stream().distinct();
> // [, Project, Gutenberg, s, Alice, Adventures, in, Wonderland, by, Lewis, ...]
> ```

`sorted`、`sorted(Comparator)`

> ```java
> Stream<String> sorted = strList.stream().sorted();
> show("strList.stream().sorted()", sorted);
> // [, A, A, A, A, A, A, A, A, A, ...]
> ```
>
> ```java
> sorted(Comparator.comparing(String::length).reversed()
> Stream<String> longestFirst = 
>        strList.stream().sorted(
>            Comparator.comparing(String::length).reversed());
> // [unenforceability, representations, MERCHANTIBILITY, disappointment, Multiplication, contemptuously, contemptuously, affectionately, Redistribution, redistribution, ...]
> ```

`distinct().sorted()`

> ```java
> // distinct().sorted()
> Stream<String> distinctSorted = strList.stream().distinct().sorted();
> // [, A, ACTUAL, ADVENTURES, AGREE, AGREEMENT, AK, ALICE, ALL, AND, ...]
> ```

## 2.6 阶段3：简单聚合（`count`，`max`，`anyMatch`，`findAny`，`findFirst`，……）

> 阶段3用来从流数据中找到答案。
>
> `聚合方法`
>
> *  会将流聚合成`一个值`
> * 返回类型是`Optional<T>`，有数据时它封装返回值，没数据时它封装一个null，需要与`Optional<T>.isPresent()`结合使用
>
> 本节介绍简单的聚合方法
>
> 完整代码：[../code/ch2/sec06/TestCh2Sec06.java](../code/ch2/sec06/TestCh2Sec06.java)

`max`：类似的还有`count`、`min`、……

> 类似的还有`count`、`min` ……
>
> ```java
> Optional<String> largest = strList.stream().max(String::compareToIgnoreCase);
> if (largest.isPresent()) {
>    System.out.println("\n\t// largest: \n\t// " + largest.get());
> }
> // largest:
> // zip
> ```

`anyMatch` ：

> 返回一个boolean表示是否有满足条件的元素存在
>
> ```java
> boolean aWordStartsWithQ = strList.stream().anyMatch(s -> s.startsWith("Q"));
> System.out.println("\n\t// strList.stream().anyMatch(s -> s.startsWith(\"Q\"): \n\t// " + aWordStartsWithQ);
> // strList.stream().anyMatch(s -> s.startsWith("Q"):
> // true
> ```

`findFirst`

> 返回第一个满足条件的元素
>
> ```java
> Optional<String> startsWithQ1 = strList.stream().filter(s -> s.startsWith("Q")).findFirst();
> if (startsWithQ1.isPresent()) {
>    System.out.println("\n\t// startsWithQ1.findFirst: \n\t// " + startsWithQ1.get());
> } else {
>    System.out.println("\n\t// No word starts with Q");
> }
> // startsWithQ1.findFirst:
> // Quick
> ```

`findAny`

> 返回任何一个满足条件的元素，适合与`parallel()`一起使用，并发执行 ，当任何一个片段中发现匹配元素时，都会介绍整个计算
>
> ```java
> Optional<String> startsWithQ2 = strList.stream().parallel().filter(s -> s.startsWith("Q")).findAny();
> if (startsWithQ2.isPresent()) {
>    System.out.println("\n\t// startsWithQ2.findAny: \n\t// " + startsWithQ2.get());
> } else {
>    System.out.println("\n\t// No word starts with Q");
> }
> // startsWithQ1:
> // Queen
> ```

## 2.7 工具：`Optional<T>`类型 

> 封装类型为T的Object
>
> * 有对象时封装该对象
>
> * 无对象时封装null
>
> 除了上一节用到的`.get()`，它还提供其他API，可以被null更高效地使用
>
> 完整代码：[../code/ch2/sec07/TestCh2Sec07.java](../code/ch2/sec07/TestCh2Sec07.java)

### (1) `Optional.ifPresent(Consumer)`

> 值存在时执行某个方法
>
> ```java
> // Optional<T>.ifPresent(Consumer<? super T>)
> optionalValue.ifPresent(System.out::println);
> 
> Set<String> results = new HashSet<>();
> optionalValue.ifPresent(results::add); 
> // 相当于 v -> results.add(v) 
> // 但是使用该方法调用 Set.add 方法、无法拿到该方法的返回值
> // public interface Set<E> extends Collection<E> {
> //		// Returns:
> //		// true if this set did not already contain the specified element
> // 	    boolean add(E e);
> //		...
> // }
> ```

### (2) `Optional.map`

> 如果想拿到被调用方法的返回值，需要使用Optional.map的方法
>
> 把Optional理解为一个大小为0或1的Stream，就可以将它于Stream.map方法对照起来看
>
> * Stream.map为Stream中0到N个元素做转换操作 ，通过另一个Stream返回
> * Optional.map为Optional中0或1个元素做转换操作，通过另一个Optional返回 
>
> ~~~java
> // Optional<U> elem = Optional<T>.map(Function<? super T, ? extends U>)
> Optional<Boolean> added = optionalValue.map(results::add);
> // Optional[false]
> ~~~

### (3) `Optional.orElse`

> 在Optional为空（没有封装任何对象）时提供一个替代方案
>
> (1) 提供替代值
>
> ```java
> System.out.println("\n\t// " +
>         strList.stream()
>                 .filter(s -> s.contains("fred"))
>                 .findFirst()
>                 .orElse("No word") + " contains fred");
> // No word contains fred
> 
> String result = Optional.<String>empty().orElse("N/A");
> System.out.println("\n\t// result: " + result);
> // result: N/A
> ```
>
> (2) 提供一个方法用来生成替代值
>
> ~~~java
> result = Optional.<String>empty().orElseGet(
>     () -> System.getProperty("user.dir"));
> System.out.println("\n\t// result: " + result);
> // result: /Users/fangkun/Dev/git/java8fastlearn
> ~~~
>
> (3) 以抛出异常作为替代方案 
>
> ~~~java
> try {
>    result = Optional.<String>empty().orElseThrow(NoSuchElementException::new);
>    System.out.println("\n\t// result: " + result);
> } catch (Throwable t) {
>    t.printStackTrace();
> }
> // java.util.NoSuchElementException
> // at java.util.Optional.orElseThrow(Optional.java:290)
> // at TestCh2Sec07.main(TestCh2Sec07.java:44)
> ~~~

### (4) `Optional.flatMap`

>用来把两个都会返回Optional的函数组合起来串行调用，比如下面两个方法`f`和`g`
>
>* `Optional<T> f()`
>* `Class  X { static Optional<U> g(Optional<T>); }`
>
>可以以`Optional<U> u = f().flatMap(X::g)`的方式调用 
>
>下面是例子：
>
>```java
>public static Optional<Double> inverse(Double x) {
>   return x == 0 ? Optional.empty() : Optional.of(1 / x);
>}
>public static Optional<Double> squareRoot(Double x) {
>   return x < 0 ? Optional.empty() : Optional.of(Math.sqrt(x));
>}
>```
>
>```java
>System.out.println("\n\t// " +
>        inverse(4.0).flatMap(TestCh2Sec07::squareRoot));
>// Optional[0.5]
>
>System.out.println("\n\t// " +
>        inverse(-1.0).flatMap(TestCh2Sec07::squareRoot));
>// Optional.empty
>
>System.out.println("\n\t// " +
>        inverse(0.0).flatMap(TestCh2Sec07::squareRoot));
>// Optional.empty
>```
>
>多个flatMap也可以串起来调用
>
>~~~java
>System.out.println("\n\t// " +
>        		Optional.of(-4.0)
>               .flatMap(TestCh2Sec07::inverse)
>               .flatMap(TestCh2Sec07::squareRoot)
>            );
>// Optional.empty
>~~~
>
>把`Optional`理解成大小为0或1的`Stream`，`Optional.flatMap`就可以与`Stream.flatMap`对照起来看
>
>* `Stream.flatMap`把方法返回的`Stream`展开，进而使得两个方法可以组合起来串行调用 
>* `Optional.flatMap`把方法返回的`Optional`展开 ，同样使得两个方法可以组合起来串行调用 

### (5) 创建Optional：`Optional.empty`，`Optional.of`，`Optional.ofNullable`

例子：

> ~~~java
> Optional<T> optionalT = (objT == null) ? Optional.empty() : Optional.of(objT);
> ~~~
>
> 等价于
>
> ~~~java
> Optional<T> optionalT = Optional.ofNullable(objT);
> ~~~

说明：

> `Optional.empty()`：空的Optional
>
> `Optional.of(objT)`：封装了objT的Optional
>
> `Optional.ofNullable(objT)`：objT不为null则封装objT，为null则返回Optional.empty()

## 2.8 阶段3：聚合操作

> 使用元素顺序无关的二元操作符（如`相加`、`相乘`、`求最大值`、`求最小值`、`求并集`、`求交集`、……）将Stream中的元素组合成一个值