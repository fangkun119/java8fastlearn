<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
<!--**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*-->

- [CH02 Stream](#ch02-stream)
  - [2.1  从迭代器到Stream](#21--%E4%BB%8E%E8%BF%AD%E4%BB%A3%E5%99%A8%E5%88%B0stream)
    - [(1) 代码对比](#1-%E4%BB%A3%E7%A0%81%E5%AF%B9%E6%AF%94)
    - [(2) Stream与Collection的区别](#2-stream%E4%B8%8Ecollection%E7%9A%84%E5%8C%BA%E5%88%AB)
    - [(3) 创建Stream流水线](#3-%E5%88%9B%E5%BB%BAstream%E6%B5%81%E6%B0%B4%E7%BA%BF)
      - [阶段一：创建Stream](#%E9%98%B6%E6%AE%B5%E4%B8%80%E5%88%9B%E5%BB%BAstream)
      - [阶段二：1至多个中间操作](#%E9%98%B6%E6%AE%B5%E4%BA%8C1%E8%87%B3%E5%A4%9A%E4%B8%AA%E4%B8%AD%E9%97%B4%E6%93%8D%E4%BD%9C)
      - [阶段三：终止操作](#%E9%98%B6%E6%AE%B5%E4%B8%89%E7%BB%88%E6%AD%A2%E6%93%8D%E4%BD%9C)
    - [(4) 运行原理](#4-%E8%BF%90%E8%A1%8C%E5%8E%9F%E7%90%86)
  - [2.2  阶段1：创建Stream](#22--%E9%98%B6%E6%AE%B51%E5%88%9B%E5%BB%BAstream)
    - [(1) 将**Collection**转化为Stream](#1-%E5%B0%86collection%E8%BD%AC%E5%8C%96%E4%B8%BAstream)
    - [(2) 使用静态方法Stream.of或Arrays.stream将**数组**或**变长参数列表**转化为Stream](#2-%E4%BD%BF%E7%94%A8%E9%9D%99%E6%80%81%E6%96%B9%E6%B3%95streamof%E6%88%96arraysstream%E5%B0%86%E6%95%B0%E7%BB%84%E6%88%96%E5%8F%98%E9%95%BF%E5%8F%82%E6%95%B0%E5%88%97%E8%A1%A8%E8%BD%AC%E5%8C%96%E4%B8%BAstream)
    - [(3) 用静态方法Stream.empty创建**空的Stream**](#3-%E7%94%A8%E9%9D%99%E6%80%81%E6%96%B9%E6%B3%95streamempty%E5%88%9B%E5%BB%BA%E7%A9%BA%E7%9A%84stream)
    - [(4) 用静态方法Stream.generate或Stream.Iterate创建**无限Stream**](#4-%E7%94%A8%E9%9D%99%E6%80%81%E6%96%B9%E6%B3%95streamgenerate%E6%88%96streamiterate%E5%88%9B%E5%BB%BA%E6%97%A0%E9%99%90stream)
    - [(5) 各种类自带的方法也可以产生Stream](#5-%E5%90%84%E7%A7%8D%E7%B1%BB%E8%87%AA%E5%B8%A6%E7%9A%84%E6%96%B9%E6%B3%95%E4%B9%9F%E5%8F%AF%E4%BB%A5%E4%BA%A7%E7%94%9Fstream)
  - [2.3 阶段2：`filter`、`map`、`flatMap`方法](#23-%E9%98%B6%E6%AE%B52filtermapflatmap%E6%96%B9%E6%B3%95)
  - [2.4 阶段2：子流（`limit`，` skip`)、组合流（`concat`）及`peek`](#24-%E9%98%B6%E6%AE%B52%E5%AD%90%E6%B5%81limit-skip%E7%BB%84%E5%90%88%E6%B5%81concat%E5%8F%8Apeek)
  - [2.5 阶段2：有状态转换（`distinct`，`sorted`）](#25-%E9%98%B6%E6%AE%B52%E6%9C%89%E7%8A%B6%E6%80%81%E8%BD%AC%E6%8D%A2distinctsorted)
  - [2.6 阶段3：简单聚合（`count`，`max`，`anyMatch`，`findAny`，`findFirst`，……）](#26-%E9%98%B6%E6%AE%B53%E7%AE%80%E5%8D%95%E8%81%9A%E5%90%88countmaxanymatchfindanyfindfirst)
  - [2.7 工具：`Optional<T>`类型](#27-%E5%B7%A5%E5%85%B7optionalt%E7%B1%BB%E5%9E%8B)
    - [(1) `Optional.ifPresent(Consumer)`](#1-optionalifpresentconsumer)
    - [(2) `Optional.map`](#2-optionalmap)
    - [(3) `Optional.orElse`](#3-optionalorelse)
    - [(4) `Optional.flatMap`](#4-optionalflatmap)
    - [(5) 创建Optional：`Optional.empty`，`Optional.of`，`Optional.ofNullable`](#5-%E5%88%9B%E5%BB%BAoptionaloptionalemptyoptionalofoptionalofnullable)
  - [2.8 阶段3：聚合操作（`reduce`）](#28-%E9%98%B6%E6%AE%B53%E8%81%9A%E5%90%88%E6%93%8D%E4%BD%9Creduce)
    - [2.8.1 以类型`Optional<T>`的返回](#281-%E4%BB%A5%E7%B1%BB%E5%9E%8Boptionalt%E7%9A%84%E8%BF%94%E5%9B%9E)
    - [2.8.2 以类型`T`返回，需要提供初始值](#282-%E4%BB%A5%E7%B1%BB%E5%9E%8Bt%E8%BF%94%E5%9B%9E%E9%9C%80%E8%A6%81%E6%8F%90%E4%BE%9B%E5%88%9D%E5%A7%8B%E5%80%BC)
    - [2.8.3 提取属性并累加（`identity`，`accumulator`，`combiner`）](#283-%E6%8F%90%E5%8F%96%E5%B1%9E%E6%80%A7%E5%B9%B6%E7%B4%AF%E5%8A%A0identityaccumulatorcombiner)
  - [2.9 阶段3：收集结果](#29-%E9%98%B6%E6%AE%B53%E6%94%B6%E9%9B%86%E7%BB%93%E6%9E%9C)
    - [2.9.1 `iterator`](#291-iterator)
    - [2.9.2 `toArray`](#292-toarray)
    - [2.9.3 `forEach`](#293-foreach)
    - [2.9.4 collect](#294-collect)
      - [(1) `collect(supplier, accumulator, combiner)`](#1-collectsupplier-accumulator-combiner)
      - [(2) `collect(Collector)`](#2-collectcollector)
  - [2.10 阶段3：将结果收集到Map中](#210-%E9%98%B6%E6%AE%B53%E5%B0%86%E7%BB%93%E6%9E%9C%E6%94%B6%E9%9B%86%E5%88%B0map%E4%B8%AD)
    - [2.10.1 `Collectors.toMap(keyMapper, valueMapper)`](#2101-collectorstomapkeymapper-valuemapper)
    - [2.10.2 `Collection.toMap(keyMapper, valueMapper, mergeFunction)`](#2102-collectiontomapkeymapper-valuemapper-mergefunction)
    - [2.10.3 `Collection.toMap(keyMapper, valueMapper, mergeFunction, mapSupplier)`](#2103-collectiontomapkeymapper-valuemapper-mergefunction-mapsupplier)
  - [2.11 阶段3：分组（groupingBy）和分片（partitioningBy）](#211-%E9%98%B6%E6%AE%B53%E5%88%86%E7%BB%84groupingby%E5%92%8C%E5%88%86%E7%89%87partitioningby)
    - [2.11.1 设置classifier以指定如何分类](#2111-%E8%AE%BE%E7%BD%AEclassifier%E4%BB%A5%E6%8C%87%E5%AE%9A%E5%A6%82%E4%BD%95%E5%88%86%E7%B1%BB)
      - [(1) `Collectors.groupingBy(classifier)`](#1-collectorsgroupingbyclassifier)
      - [(2) `Collectors.partitioningBy(predicate)`](#2-collectorspartitioningbypredicate)
    - [2.11.2 设置downstream指定如何收集分类结果](#2112-%E8%AE%BE%E7%BD%AEdownstream%E6%8C%87%E5%AE%9A%E5%A6%82%E4%BD%95%E6%94%B6%E9%9B%86%E5%88%86%E7%B1%BB%E7%BB%93%E6%9E%9C)
      - [(1) 收集item到指定的数据结构中](#1-%E6%94%B6%E9%9B%86item%E5%88%B0%E6%8C%87%E5%AE%9A%E7%9A%84%E6%95%B0%E6%8D%AE%E7%BB%93%E6%9E%84%E4%B8%AD)
        - [`Collectors.toSet()`/`toList()`/`toMap()`/`toConcurrentMap()`](#collectorstosettolisttomaptoconcurrentmap)
      - [(2) 把每个分组的统计值作为收集内容](#2-%E6%8A%8A%E6%AF%8F%E4%B8%AA%E5%88%86%E7%BB%84%E7%9A%84%E7%BB%9F%E8%AE%A1%E5%80%BC%E4%BD%9C%E4%B8%BA%E6%94%B6%E9%9B%86%E5%86%85%E5%AE%B9)
        - [`Collectors.counting()`](#collectorscounting)
        - [`Collectors.summingInt()/summingLong()/summingDouble()`](#collectorssummingintsumminglongsummingdouble)
        - [`Collectors.maxBy(Comparator)`/`minBy(Comparator)`](#collectorsmaxbycomparatorminbycomparator)
        - [`Collectors.summarizingInt(ToIntFunction)`/`summarizingLong(ToLongFunction)`/`summarizingDouble(ToDoubleFunction)`](#collectorssummarizinginttointfunctionsummarizinglongtolongfunctionsummarizingdoubletodoublefunction)
      - [(3) 将分组内item的`指定计算值`用`指定方法`进行`收集`](#3-%E5%B0%86%E5%88%86%E7%BB%84%E5%86%85item%E7%9A%84%E6%8C%87%E5%AE%9A%E8%AE%A1%E7%AE%97%E5%80%BC%E7%94%A8%E6%8C%87%E5%AE%9A%E6%96%B9%E6%B3%95%E8%BF%9B%E8%A1%8C%E6%94%B6%E9%9B%86)
        - [`Collectors.mapping`(`指定计算值的计算方法`，`收集方法`)](#collectorsmapping%E6%8C%87%E5%AE%9A%E8%AE%A1%E7%AE%97%E5%80%BC%E7%9A%84%E8%AE%A1%E7%AE%97%E6%96%B9%E6%B3%95%E6%94%B6%E9%9B%86%E6%96%B9%E6%B3%95)
      - [(4) 将分组内item的`指定计算值`用`指定方法`进行`聚合`](#4-%E5%B0%86%E5%88%86%E7%BB%84%E5%86%85item%E7%9A%84%E6%8C%87%E5%AE%9A%E8%AE%A1%E7%AE%97%E5%80%BC%E7%94%A8%E6%8C%87%E5%AE%9A%E6%96%B9%E6%B3%95%E8%BF%9B%E8%A1%8C%E8%81%9A%E5%90%88)
        - [`Collectors.reducing`(`指定计算值的计算方法`，`聚合方法`)](#collectorsreducing%E6%8C%87%E5%AE%9A%E8%AE%A1%E7%AE%97%E5%80%BC%E7%9A%84%E8%AE%A1%E7%AE%97%E6%96%B9%E6%B3%95%E8%81%9A%E5%90%88%E6%96%B9%E6%B3%95)
  - [2.12 原始类型流](#212-%E5%8E%9F%E5%A7%8B%E7%B1%BB%E5%9E%8B%E6%B5%81)
    - [2.12.1 `IntStream`，`LongStream`，`DoubleStream`](#2121-intstreamlongstreamdoublestream)
    - [2.12.2 创建原始类型流](#2122-%E5%88%9B%E5%BB%BA%E5%8E%9F%E5%A7%8B%E7%B1%BB%E5%9E%8B%E6%B5%81)
      - [(1) 从数组或可变参数列表创建](#1-%E4%BB%8E%E6%95%B0%E7%BB%84%E6%88%96%E5%8F%AF%E5%8F%98%E5%8F%82%E6%95%B0%E5%88%97%E8%A1%A8%E5%88%9B%E5%BB%BA)
      - [(2) 生成原始类型流](#2-%E7%94%9F%E6%88%90%E5%8E%9F%E5%A7%8B%E7%B1%BB%E5%9E%8B%E6%B5%81)
      - [(3) 使用`mapToXXX()`方法计算得到原始类型流](#3-%E4%BD%BF%E7%94%A8maptoxxx%E6%96%B9%E6%B3%95%E8%AE%A1%E7%AE%97%E5%BE%97%E5%88%B0%E5%8E%9F%E5%A7%8B%E7%B1%BB%E5%9E%8B%E6%B5%81)
    - [2.12.3 装箱，转换成普通的对象流](#2123-%E8%A3%85%E7%AE%B1%E8%BD%AC%E6%8D%A2%E6%88%90%E6%99%AE%E9%80%9A%E7%9A%84%E5%AF%B9%E8%B1%A1%E6%B5%81)
    - [2.12.4 使用方法（与对象流的差别）](#2124-%E4%BD%BF%E7%94%A8%E6%96%B9%E6%B3%95%E4%B8%8E%E5%AF%B9%E8%B1%A1%E6%B5%81%E7%9A%84%E5%B7%AE%E5%88%AB)
  - [2.13 并行流及使用要领](#213-%E5%B9%B6%E8%A1%8C%E6%B5%81%E5%8F%8A%E4%BD%BF%E7%94%A8%E8%A6%81%E9%A2%86)
    - [2.13.1 创建并行流](#2131-%E5%88%9B%E5%BB%BA%E5%B9%B6%E8%A1%8C%E6%B5%81)
      - [`parallelStream()`或`parallel()`](#parallelstream%E6%88%96parallel)
    - [2.13.2 保障计算正确](#2132-%E4%BF%9D%E9%9A%9C%E8%AE%A1%E7%AE%97%E6%AD%A3%E7%A1%AE)
      - [(1) 流操作应当是无状态的](#1-%E6%B5%81%E6%93%8D%E4%BD%9C%E5%BA%94%E5%BD%93%E6%98%AF%E6%97%A0%E7%8A%B6%E6%80%81%E7%9A%84)
      - [(2) 给共享的数据增加原子保护](#2-%E7%BB%99%E5%85%B1%E4%BA%AB%E7%9A%84%E6%95%B0%E6%8D%AE%E5%A2%9E%E5%8A%A0%E5%8E%9F%E5%AD%90%E4%BF%9D%E6%8A%A4)
      - [(3) 分组并行](#3-%E5%88%86%E7%BB%84%E5%B9%B6%E8%A1%8C)
    - [2.13.3 `有序`与`并行流性能`的关系：`parallel`,`unordered`](#2133-%E6%9C%89%E5%BA%8F%E4%B8%8E%E5%B9%B6%E8%A1%8C%E6%B5%81%E6%80%A7%E8%83%BD%E7%9A%84%E5%85%B3%E7%B3%BBparallelunordered)
    - [2.13.4 流不应当改变底层集合](#2134-%E6%B5%81%E4%B8%8D%E5%BA%94%E5%BD%93%E6%94%B9%E5%8F%98%E5%BA%95%E5%B1%82%E9%9B%86%E5%90%88)
  - [2.14 函数式接口](#214-%E5%87%BD%E6%95%B0%E5%BC%8F%E6%8E%A5%E5%8F%A3)
    - [2.14.1 Stream API用到的函数式接口](#2141-stream-api%E7%94%A8%E5%88%B0%E7%9A%84%E5%87%BD%E6%95%B0%E5%BC%8F%E6%8E%A5%E5%8F%A3)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# CH02 Stream

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
>    	show ("Files.lines(path, StandardCharsets.UTF_8)", lines);
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
>    	List<Character> result = new ArrayList<>();
>    	for (char c : s.toCharArray()) {
>    		result.add(c);
>    	}
>    	return result.stream();
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
>    	.peek(e -> System.out.println("\t// Fetching " + e))
>    	.limit(5).toArray();
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
>    	System.out.println("\n\t// largest: \n\t// " + largest.get());
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
>    	System.out.println("\n\t// startsWithQ1.findFirst: \n\t// " + startsWithQ1.get());
> } else {
>    	System.out.println("\n\t// No word starts with Q");
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
>    	System.out.println("\n\t// startsWithQ2.findAny: \n\t// " + startsWithQ2.get());
> } else {
>    	System.out.println("\n\t// No word starts with Q");
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
> result = Optional.<String>empty().orElseGet(() -> System.getProperty("user.dir"));
>    System.out.println("\n\t// result: " + result);
> // result: /Users/fangkun/Dev/git/java8fastlearn
> ~~~
> 
>(3) 以抛出异常作为替代方案 
> 
>~~~java
> try {
> 	result = Optional.<String>empty().orElseThrow(NoSuchElementException::new);
>    	System.out.println("\n\t// result: " + result);
>    } catch (Throwable t) {
> 	t.printStackTrace();
>    }
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
> 	return x == 0 ? Optional.empty() : Optional.of(1 / x);
>}
>public static Optional<Double> squareRoot(Double x) {
> 	return x < 0 ? Optional.empty() : Optional.of(Math.sqrt(x));
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
> 把`Optional`理解成大小为0或1的`Stream`，`Optional.flatMap`就可以与`Stream.flatMap`对照起来看
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

## 2.8 阶段3：聚合操作（`reduce`）

> 使用元素顺序无关的二元操作符（如`相加`、`相乘`、`求最大值`、`求最小值`、`求并集`、`求交集`、……）将Stream中的元素组合成一个值

### 2.8.1 以类型`Optional<T>`的返回

> ```java
> Optional<Integer> sum1 = Stream.of(intArray).reduce((x, y) -> x + y);
> // Optional[366]
> Optional<Integer> sum2 = Stream.<Integer>empty().reduce((x, y) -> x + y);
> // Optional.empty
> ```
>
> 其中的`(x, y) -> x + y`也可以替换成`Integer::sum`，满足`BiFunction<T, U, R>`接口的要求即可

### 2.8.2 以类型`T`返回，需要提供初始值

> ```java
> Integer sum3 = Stream.of(intArray).reduce(0, (x, y) -> x + y);
> // 366
> Integer sum4 = Stream.<Integer>empty().reduce(0, (x, y) -> x + y);
> // 0
> ```

### 2.8.3 提取属性并累加（`identity`，`accumulator`，`combiner`）

> `identity`：初始值
>
> `accumulator`：`累加值`与某个`待提取属性值的item`进行累加
>
> `combiner`：两个`累加值`进行合并
>
> ```java
> // U reduce(identity, accumulator, combiner)
> int result = strList.stream().reduce(
>         0, (s, w) -> s + w.length(), (s1, s2) -> s1 + s2);
> // 122988
> 
> result = strList.parallelStream().reduce(
>         0, (s, w) -> s + w.length(), (s1, s2) -> s1 + s2);
> // 122988
> ```

## 2.9 阶段3：收集结果

> 以各种方法收集流中的Item，而不是将它们聚合成一个值

### 2.9.1 `iterator`

> ```java
> // iterator()
> Iterator<Integer> iter = Stream.iterate(0, n -> n + 1).limit(5).iterator();
> while (iter.hasNext()) {
>    	System.out.println(LOG_PREFIX + iter.next());
> }
> // 0
> // 1
> // 2
> // 3
> // 4
> ```

### 2.9.2 `toArray`

> 需要使用`T[]::new`来解决泛型擦除对数组的影响，否则只能拿到`Object[]`类型的返回值
>
> ```java
> // toArray(T[]:new)：解决泛型擦除的影响，可以返回T[]
> Integer[] numbers3  = Stream.iterate(0, n -> n + 1).limit(5).toArray(Integer[]::new);
>    // [Ljava.lang.Integer;@52cc8049
> ```

### 2.9.3 `forEach`

> ```java
> noVowelsStream().limit(5).forEach(System.out::println);
> // Prjct
> // Gtnbrg
> // s
> // lc
> ```

### 2.9.4 collect

#### (1) `collect(supplier, accumulator, combiner)`

> `supplier`：根据目标类型创建对象的方法（解决泛型擦除问题）
>
> `accumulator`：将一个item天添加到`收纳器`中的方法
>
> `combiner`：将两个`收纳器`合并为一个的方法
>
> ~~~java
> // collect(Supplier<R>，BiConsumer<R, ? super T>, BiConsumer<R, R>)
> HashSet<String> noVowelHashSet
>    	= noVowelsStream().collect(HashSet::new, HashSet::add, HashSet::addAll);
> // [, Pppr, ccssd, rsrc, tdy, srprsd, kss, rsrch, gssd, vwng]
> ~~~

#### (2) `collect(Collector)`

> <div align="left"><img src="https://raw.githubusercontent.com/kenfang119/pics/main/java8fastlearn/java8_stream_collector_interface.jpg" width="800" /></div>
>
> `Collector`接口中已经包含了用于生成`supplier`、`accumulator`、`combiner`的方法。而工具类`Collectors`也提供了许多工厂方法来生成这些`Collector`。
>
> 例子如下：
>
> (1) 收集item到容器中，例如`Set`、`List`、……
>
> ```java
> Set<String> noVowelSet = noVowelsStream().collect(Collectors.toSet());
> // [, Pppr, ccssd, rsrc, tdy, srprsd, kss, rsrch, gssd, vwng]
> 
> TreeSet<String> noVowelTreeSet
>    	= noVowelsStream().collect(Collectors.toCollection(TreeSet::new));
> // [, B, BFR, BG, BK, BRCH, BST, BSY, BT, BTS]
> ```
>
> (2) 收集String并拼接在一起
>
> ~~~java
> String result1 = noVowelsStream().limit(5).collect(Collectors.joining());
> // PrjctGtnbrgslc
> 
> String result2 = noVowelsStream().limit(5).collect(Collectors.joining(", "));
> // , Prjct, Gtnbrg, s, lc
> ~~~
>
> (3) 对流的结果进行汇总
>
> ~~~java
> IntSummaryStatistics summary
>         = noVowelsStream().collect(Collectors.summarizingInt(String::length));
> double averageWordLength = summary.getAverage();      
> double maxWordLength = summary.getMax();
> // 2.488593030900723
> // 10.0
> ~~~

`Collectors`工具类提供的静态工厂方法如下

> <div align="left"><img src="https://raw.githubusercontent.com/kenfang119/pics/main/java8fastlearn/java8_stream_collectors.jpg" width="800" /></div>

## 2.10 阶段3：将结果收集到Map中

> 将stream的结果收集到Map中，对于每一个item，需要解决如下问题：
>
> (1) 如何生成Map的key
>
> (2) 如何生成Map的value
>
> (3) 当两个item的key相同时、该如何处理

### 2.10.1 `Collectors.toMap(keyMapper, valueMapper)`

> 设置`keyMapper`和`valueMapper`可以指定如何得到Map的`key`和`value`
>
> (1) key和value都是item的一个方法的返回值
>
> ```java
> Map<Integer, String> idToName = personStream().collect(Collectors.toMap(Person::getId, Person::getName));
> // {1001=Peter, 1002=Paul, 1003=Mary}
> ```
>
> (2) value是item本身
>
> ```java
> Map<Integer, Person> idToPerson1 = personStream().collect(Collectors.toMap(Person::getId, Function.identity()));
> // {1001=Person[id=1001,name=Peter], 1002=Person[id=1002,name=Paul], 1003=Person[id=1003,name=Mary]}
> ```
>
> (3) 相同key时默认抛异常
>
> 上述两块代码没有指定key相同时的处理方法，当遇到具有相同key的item时，会抛出`IllegalStateException`

### 2.10.2 `Collectors.toMap(keyMapper, valueMapper, mergeFunction)`

> 设置`mergeFunction`可以指定当遇到相同`key`时该如何处理
>
> 例子如下：
>
> (1) value替换，只保留一个（返回的Map的value类型是T）
>
> ```java
> Map<String, String> languageNames = Stream.of(Locale.getAvailableLocales()).collect(
>    Collectors.toMap(
>       Locale::getDisplayLanguage, 
>       Locale::getDisplayLanguage, 
>       (existingValue, newValue) -> existingValue));
> // {土耳其文=土耳其文, =, 意大利文=意大利文, 冰岛文=冰岛文, 印地文=印地文, ...}
> ```
>
> (2) value去重存储在一个Set中（返回的Map的value类型是Set<T>）
>
> ```java
> Map<String, Set<String>> countryLanguageSets = Stream.of(Locale.getAvailableLocales()).collect(
>    Collectors.toMap(
>       Locale::getDisplayCountry,
>       l -> Collections.singleton(l.getDisplayLanguage()),
>       (a, b) -> { // union of a and b
>          Set<String> r = new HashSet<>(a); 
>          r.addAll(b);
>          return r; }));
> // {新加坡=[中文, 英文], 澳大利亚=[英文], 印度尼西亚=[印度尼西亚文], 科威特=[阿拉伯文], 菲律宾=[英文], ..., =[, 土耳其文, 意大利文, 冰岛文, 印地文, ...]}
> ```
>
> 备注：上面的功能也可以使用`2.11`的`grouping`来实现

### 2.10.3 `Collectors.toMap(keyMapper, valueMapper, mergeFunction, mapSupplier)`

> `mapSupplier`用来指定Map的具体实现
>
> 例如下面的例子、把默认的`HashMap`改成`TreeMap`
>
> ```java
> // Collection.toMap(keyMapper, valueMapper, mergeFunction, mapSupplier)
> Map<Integer, Person> idToPerson2  = personStream().collect(
>         Collectors.toMap(
>                 Person::getId,
>                 Function.identity(),
>                 (existingValue, newValue) -> { throw new IllegalStateException(); },
>                 TreeMap::new));
> // {1001=Person[id=1001,name=Peter], 1002=Person[id=1002,name=Paul], 1003=Person[id=1003,name=Mary]}
> ```

## 2.11 阶段3：分组（groupingBy）和分片（partitioningBy）

> 使用`groupingBy`可以对streaming内的数据进行分组，可通过参数指定
>
> * 如何分类（`classifier`）
> * 如何收集分类结果（`downstream`）

### 2.11.1 设置classifier以指定如何分类

#### (1) `Collectors.groupingBy(classifier)`

> 设置`classifier`为`Locale::getCountry`，而`downstream`使用了默认的`List::new`
>
> ```java
> Map<String, List<Locale>> countryToLocales
>         = localeStream().collect(Collectors.groupingBy(Locale::getCountry));
> // [DE=[de_DE], PR=[es_PR], CH=[fr_CH, de_CH, it_CH], TW=[zh_TW], ...]
> ```

#### (2) `Collectors.partitioningBy(predicate)`

> 当`classifier`的返回类型是`boolean`时，也可以使用`partioningBy`
>
> ```java
> Map<Boolean, List<Locale>> englishAndOtherLocales
>         = localeStream().collect(
>                 Collectors.partitioningBy(l -> l.getLanguage().equals("en")));
> display("English locales: ", englishAndOtherLocales.get(true));
> // [en_US, en_SG, en_MT, en, en_PH, en_NZ, en_ZA, en_AU, en_IE, en_CA, en_IN, en_GB]
> ```

### 2.11.2 设置downstream指定如何收集分类结果

> 不设置downstream时，默认值的收集器为Collector::toMap，即收集到Map中

#### (1) 收集item到指定的数据结构中 

##### `Collectors.toSet()`/`toList()`/`toMap()`/`toConcurrentMap()`

> 把分组后每一组的item收集到Set/List/Map/ConcurrentMap中
>
> ```java
> Map<String, Set<String>> countryToLanguages
>         = localeStream().collect(
>                 groupingBy(
>                         Locale::getDisplayCountry,
>                         mapping(Locale::getDisplayLanguage, toSet())
>                 ));
> // [泰国=[泰文], 巴西=[葡萄牙文], 塞尔维亚及黑山=[塞尔维亚文], 丹麦=[丹麦文], 塞尔维亚=[塞尔维亚文], ...]
> ```
>
> 说明：`Collectors.toXXX()`是一组工厂方法，用来返回不同的类，这些类都符合`downstream`泛型参数的要求
>
> ```
> Collector<? super T, A, D> downstream
> ```
>
> <div align="left"><img src="https://raw.githubusercontent.com/kenfang119/pics/main/java8fastlearn/java8_stream_collectors_toXXX.jpg" width="800" /></div>

#### (2) 把每个分组的统计值作为收集内容 

##### `Collectors.counting()`

> 每个分组的item数量作为收集内容
>
> ```
> Map<String, Long> countryToLocaleCounts
>         = localeStream().collect(
>                 groupingBy(Locale::getCountry, counting()));
> // [DE=1, PR=1, HK=1, TW=1, PT=1, ...]
> ```

##### `Collectors.summingInt()/summingLong()/summingDouble()`

> 对每个分组下item的`指定计算值`进行`求和`，作为收集内容
>
> ```java
> Map<String, Integer> stateToCityPopulation
>         = cityStream().collect(
>                 groupingBy(City::getState, summingInt(City::getPopulation)));
> // [DE=71292, HI=345610, TX=13748465, MA=2403297, MD=869891, ...]
> ```

##### `Collectors.maxBy(Comparator)`/`minBy(Comparator)`

> 对每个分组下item的`指定计算值`、按照`指定规则`比较的得到的`最大值`/`最小值`，作为收集内容
>
> ```java
> Map<String, Set<String>> countryToLanguages
>         = localeStream().collect(
>                 groupingBy(
>                         Locale::getDisplayCountry,
>                         mapping(Locale::getDisplayLanguage, toSet())
>                 ));
> // [泰国=[泰文], 巴西=[葡萄牙文], 塞尔维亚及黑山=[塞尔维亚文], 丹麦=[丹麦文], 塞尔维亚=[塞尔维亚文], ...]
> ```

##### `Collectors.summarizingInt(ToIntFunction)`/`summarizingLong(ToLongFunction)`/`summarizingDouble(ToDoubleFunction)`

> 对每个分组下item类型为`int`/`long`/`double`的`指定计算值`，计算出一组统计量（包括`count`，`sum`，`min`，`average`，`max`），作为收集内容
>
> ```java
> Map<String, IntSummaryStatistics> stateToCityPopulationSummary
>         = cityStream().collect(
>                 groupingBy(
>                         City::getState,
>                         summarizingInt(City::getPopulation)
>                 ));
> System.out.println(stateToCityPopulationSummary.get("NY"));
> // IntSummaryStatistics{count=14, sum=9733274, min=49722, average=695233.857143, max=8336697}
> ```

#### (3) 将分组内item的`指定计算值`用`指定方法`进行`收集`

##### `Collectors.mapping`(`指定计算值的计算方法`，`收集方法`)

> ```java
> Map<String, Set<String>> countryToLanguages
>         = localeStream().collect(
>         groupingBy(
>                 Locale::getDisplayCountry,
>                 mapping(Locale::getDisplayLanguage, toSet())
>         ));
> // [泰国=[泰文], 巴西=[葡萄牙文], 塞尔维亚及黑山=[塞尔维亚文], 丹麦=[丹麦文], 塞尔维亚=[塞尔维亚文], ...]
> 
> Map<String, String> stateToCityNames1
>         = cityStream().collect(
>         groupingBy(
>                 City::getState,
>                 mapping(City::getName, joining(", "))
>         ));
> // Baltimore, Frederick, Rockville, Gaithersburg, Bowie
> ```

#### (4) 将分组内item的`指定计算值`用`指定方法`进行`聚合`

##### `Collectors.reducing`(`指定计算值的计算方法`，`聚合方法`)

> ```java
> Map<String, String> stateToCityNames2
>         = cityStream().collect(
>                 groupingBy(
>                     	// 指定值的计算方法为item的getState()方法
>                         City::getState,
>                     	// 聚合方法为','为分割符的字符串拼接
>                         reducing(
>                                 "",
>                                 City::getName,
>                                 (s, t) -> s.length() == 0 ? t : s + ", " + t
>                         )
>                 ));
> System.out.println(stateToCityNames2.get("MD")); // by example of MD
> // Baltimore, Frederick, Rockville, Gaithersburg, Bowie
> ```

## 2.12 原始类型流

> 存储原始类型（`boolean`/`byte`/`char`/`short`/`int`/`long`/`float`/`double`）的流，它们不存储`Integer`之类的对象，而是直接存储原始类型，因此更加高效

### 2.12.1 `IntStream`，`LongStream`，`DoubleStream`

> * `IntStream`：可以用来存储`shart`、`char`、`byte`、`boolean`、`int`
> * `LongStream`：可以用来存储`long`
> * `DoubleStream`：可以用来存储`float`、`double`

以下均以IntStream为例演示

### 2.12.2 创建原始类型流

#### (1) 从数组或可变参数列表创建

> ```java
> IntStream is1 = IntStream.of(1,2,3,4,5);
> // [1, 2, 3, 4, 5]
> 
> IntStream is2 = Arrays.stream(new int[] {1,2,3,4,5}, 1, 3);
> // [2, 3]
> ```

#### (2) 生成原始类型流

> ```java
> IntStream is3 = IntStream.generate(() -> (int)(Math.random() * 100));
> // [42, 0, 98, 5, 81, ...]
> 
> IntStream is4 = IntStream.range(5, 9);
> // [5, 6, 7, 8]
> 
> IntStream is5 = IntStream.rangeClosed(5, 9);
> // [5, 6, 7, 8, 9]
> 
> IntStream is6 new Random().ints();
> // [-328531074, -978755995, -120081503, 559623620, -1461156362, ...]
> ```

#### (3) 使用`mapToXXX()`方法计算得到原始类型流

> ```java
> IntStream  is7 = strList.mapToInt(String::length);
> // [0, 7, 9, 1, 5, ...]
> 
> IntStream codes = someString
>    	.codePoints() // UTF-16编码单元组成的IntStream
>    	.mapToObj(c -> String.format("%X ", c)) 
>    	.collect(Collectors.joining());
> // 1D546 20 69 73 20 74 68 65 20 73 65 74 20 6F 66 20 6F 63 74 6F 6E 69 6F 6E 73 2E
> ```

### 2.12.3 装箱，转换成普通的对象流

> ```java
> Stream<Integer> integerStream = IntStream.range(0, 100).boxed();
> IntStream is8 = integerStream.mapToInt(Integer::intValue);
> // [0, 1, 2, 3, 4, ...]
> ```

### 2.12.4 使用方法（与对象流的差别）

> `原始类型流`与`对象流`的使用方法类似，但是有以下差别：
>
> * `toArray`方法会返回一个原始类型的数组
> * 产生`Optional`结果的方法会返回一个`OpitonalInt`、`OptionalLong`或`OptionalDouble`类型，这些类型没有`get()`方法，而是用`getAsInt()`、`getAsLong()`、`getAsDouble()`来替代
> * 拥有`sum`、`average()`、`max()`、`min()`方法，这些在对象流中不提供
> * `summaryStatistics()`方法返回`IntSummaryStatistics`、`LongSummaryStatistics`、`DoubleSummaryStatistics`对象

## 2.13 并行流及使用要领

### 2.13.1 创建并行流

#### `parallelStream()`或`parallel()`

> `Collection.parallelStream()`可以创建一个并行流
>
> `Stream.parallel()`可以将一个串行流改变为并行流

并行流创建后、整个stream处于并行状态，当阶段3的终止方法被执行时，所有被延迟执行的流操作都会并行执行

### 2.13.2 保障计算正确

#### (1) 流操作应当是无状态的

> 流操作应当是无状态的，意味着他们以任意顺序执行都不影响最终的计算结果，计算结果都与串行流相同
>
> 下面是一个反例，它给多个线程共享了一个数组
>
> ```java
> int[] shortWords = new int[10];
> strList.stream().parallel().forEach(
>    	s -> {
>    		if (s.length() < 10) {
>    			shortWords[s.length()]++;
>    		}
>    	});
> System.out.println(LOG_PREFIX + Arrays.toString(shortWords));
> // 三次运行结果都不同、并且没有一次是对的
> // [1, 1771, 4676, 6996, 5743, 3418, 2135, 1809, 823, 688]
> // [1, 1765, 4546, 6634, 5494, 3357, 2129, 1794, 821, 688]
> // [1, 1778, 4714, 6984, 5728, 3410, 2137, 1816, 823, 687]
> ```
>
> 解决方法如下：

#### (2) 给共享的数据增加原子保护

> ```java
> // Atomic integers 
> AtomicInteger[] shortWordCounters = new AtomicInteger[10];
> for (int i = 0; i < shortWordCounters.length; i++) {
>    	shortWordCounters[i] = new AtomicInteger();
> }
> strList.stream().forEach(
>    	s -> {
>    		if (s.length() < 10) {
>    			shortWordCounters[s.length()].getAndIncrement();
>    	}
>    });
> System.out.println(LOG_PREFIX + Arrays.toString(shortWordCounters));
> // 三次运行结果：
> // [1, 1826, 4999, 7637, 6166, 3589, 2203, 1867, 831, 697]
> // [1, 1826, 4999, 7637, 6166, 3589, 2203, 1867, 831, 697]
> // [1, 1826, 4999, 7637, 6166, 3589, 2203, 1867, 831, 697]
> ```

#### (3) 分组并行

> ```java
> System.out.println(
>    	LOG_PREFIX +
>    	strList.stream().parallel().filter(s -> s.length() < 10).collect(
>    		groupingBy(
>    			String::length,
>    			counting())));
> // 三次运行结果：
> // {0=1, 1=1826, 2=4999, 3=7637, 4=6166, 5=3589, 6=2203, 7=1867, 8=831, 9=697}
> // {0=1, 1=1826, 2=4999, 3=7637, 4=6166, 5=3589, 6=2203, 7=1867, 8=831, 9=697}
> // {0=1, 1=1826, 2=4999, 3=7637, 4=6166, 5=3589, 6=2203, 7=1867, 8=831, 9=697}
> ```

### 2.13.3 `有序`与`并行流性能`的关系：`parallel`,`unordered`

有序流（例如`Stream.sorted`方法产生的流）

> * 有时不会影响性能，例如执行`stream.map`操作
> * 有时会影响性能，例如执行`stream.limit`操作

对于影响性能的情况，可以通过`unordered`方法将其转换为无序流

> 例如：
>
> ~~~java
> Stream<T> sample = stream.parallel().unordered().limit(n);
> ~~~

另一个例子是`groupBy`

> * group By用在`并行流`时，每个线程groupBy得到一个Map，然而合并这些Map的开销非常大
>
> * java8提供了`goupingByConcurrent`方法
>     * 它把一个`ConcurrentMap`共享给多个线程来降低开销
>     * 但代价是将不再保证数据有序
>
> 例子：
>
> ~~~java
> Map<String, List<String>> result = cities.parallel().collect(
> 	Collectors.groupingByConcurrent(City::getState); //无法按照流中的顺序收集
> )
> ~~~

### 2.13.4 流不应当改变底层集合

> 流的设计是`不修改流底层的集合`，下面是<span style="color:red">错误</span>的代码
>
> ~~~java
> Stream<String> strStream = strList.stream();
> strStream.forEach(s -> if (s.length() < 12) {
>    	strList.remove(s);
> })
> ~~~

## 2.14 函数式接口

> 上面的各个例子中都看到把`lambda表达式`作为函数参数传入的做法，理解这些代码，需要参考第一章的1.3小节，函数式接口

### 2.14.1 Stream API用到的函数式接口

> ![](https://raw.githubusercontent.com/kenfang119/pics/main/java8fastlearn/java8_stream_functional_interface.jpg)

