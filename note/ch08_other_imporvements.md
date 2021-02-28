<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
<!--**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*-->

- [CH08 其他改进](#ch08-%E5%85%B6%E4%BB%96%E6%94%B9%E8%BF%9B)
  - [8.1 类似python的字符串拼接](#81-%E7%B1%BB%E4%BC%BCpython%E7%9A%84%E5%AD%97%E7%AC%A6%E4%B8%B2%E6%8B%BC%E6%8E%A5)
  - [8.2 数字类](#82-%E6%95%B0%E5%AD%97%E7%B1%BB)
    - [8.2.1 包装类的`BYTES`字段](#821-%E5%8C%85%E8%A3%85%E7%B1%BB%E7%9A%84bytes%E5%AD%97%E6%AE%B5)
    - [8.2.2 包装类`hashCode`静态方法](#822-%E5%8C%85%E8%A3%85%E7%B1%BBhashcode%E9%9D%99%E6%80%81%E6%96%B9%E6%B3%95)
    - [8.2.3 用于Stream聚合函数的静态方法](#823-%E7%94%A8%E4%BA%8Estream%E8%81%9A%E5%90%88%E5%87%BD%E6%95%B0%E7%9A%84%E9%9D%99%E6%80%81%E6%96%B9%E6%B3%95)
      - [(1) `sum`，`max`、`min`](#1-summaxmin)
      - [(2) `logicalAnd`, `logicalOr`, `logicalXor`](#2-logicaland-logicalor-logicalxor)
    - [8.2.4 无符号运算支持](#824-%E6%97%A0%E7%AC%A6%E5%8F%B7%E8%BF%90%E7%AE%97%E6%94%AF%E6%8C%81)
      - [(1) Long和Integer的`compareUnsigned`,` divideUnsigned`, `remainderUnsigned`静态方法](#1-long%E5%92%8Cinteger%E7%9A%84compareunsigned-divideunsigned-remainderunsigned%E9%9D%99%E6%80%81%E6%96%B9%E6%B3%95)
      - [(2) Byte，Short，Integer的`toUnsignedInt`静态方法](#2-byteshortinteger%E7%9A%84tounsignedint%E9%9D%99%E6%80%81%E6%96%B9%E6%B3%95)
      - [(3) Integer的`toUnsignedLong`静态方法](#3-integer%E7%9A%84tounsignedlong%E9%9D%99%E6%80%81%E6%96%B9%E6%B3%95)
      - [(4) 浮点数`isFinite`检查](#4-%E6%B5%AE%E7%82%B9%E6%95%B0isfinite%E6%A3%80%E6%9F%A5)
      - [(5) 从BigInteger提取原始类型：`intValueExact`、`longValueExact`、`shortValueExact`、`byteValueExact`](#5-%E4%BB%8Ebiginteger%E6%8F%90%E5%8F%96%E5%8E%9F%E5%A7%8B%E7%B1%BB%E5%9E%8Bintvalueexactlongvalueexactshortvalueexactbytevalueexact)
  - [8.3 新的数学函数](#83-%E6%96%B0%E7%9A%84%E6%95%B0%E5%AD%A6%E5%87%BD%E6%95%B0)
    - [8.3.1 带有溢出检查的算数运算：`Math.*Exact`](#831-%E5%B8%A6%E6%9C%89%E6%BA%A2%E5%87%BA%E6%A3%80%E6%9F%A5%E7%9A%84%E7%AE%97%E6%95%B0%E8%BF%90%E7%AE%97mathexact)
    - [8.3.2 带有溢出检查的数值转换：`Math.to*Exact`](#832-%E5%B8%A6%E6%9C%89%E6%BA%A2%E5%87%BA%E6%A3%80%E6%9F%A5%E7%9A%84%E6%95%B0%E5%80%BC%E8%BD%AC%E6%8D%A2mathtoexact)
    - [8.3.3 避免结果为负问题的求模函数：`Math.floorMod`](#833-%E9%81%BF%E5%85%8D%E7%BB%93%E6%9E%9C%E4%B8%BA%E8%B4%9F%E9%97%AE%E9%A2%98%E7%9A%84%E6%B1%82%E6%A8%A1%E5%87%BD%E6%95%B0mathfloormod)
    - [8.3.4 返回比指定值小但是最接近的浮点数: `Math.nextDown`](#834-%E8%BF%94%E5%9B%9E%E6%AF%94%E6%8C%87%E5%AE%9A%E5%80%BC%E5%B0%8F%E4%BD%86%E6%98%AF%E6%9C%80%E6%8E%A5%E8%BF%91%E7%9A%84%E6%B5%AE%E7%82%B9%E6%95%B0-mathnextdown)
  - [8.4 集合](#84-%E9%9B%86%E5%90%88)
    - [8.4.1 集合类中新增的方法](#841-%E9%9B%86%E5%90%88%E7%B1%BB%E4%B8%AD%E6%96%B0%E5%A2%9E%E7%9A%84%E6%96%B9%E6%B3%95)
    - [8.4.2 比较器](#842-%E6%AF%94%E8%BE%83%E5%99%A8)
      - [(1) 基于字段的比较器：`Comparator.comparing(keyExtractor)`](#1-%E5%9F%BA%E4%BA%8E%E5%AD%97%E6%AE%B5%E7%9A%84%E6%AF%94%E8%BE%83%E5%99%A8comparatorcomparingkeyextractor)
      - [(2) 多级比较器：`Comparator.thenComparing(keyExtractor)`](#2-%E5%A4%9A%E7%BA%A7%E6%AF%94%E8%BE%83%E5%99%A8comparatorthencomparingkeyextractor)
      - [(3) 基于字段及排序规则来比较：`Comparator.comparing(keyExtractor,keyComparator)`](#3-%E5%9F%BA%E4%BA%8E%E5%AD%97%E6%AE%B5%E5%8F%8A%E6%8E%92%E5%BA%8F%E8%A7%84%E5%88%99%E6%9D%A5%E6%AF%94%E8%BE%83comparatorcomparingkeyextractorkeycomparator)
      - [(4) 避免int/long/double的装箱拆箱的比较器：`Comparator.comparingInt/Long/Double(keyExtractor)`](#4-%E9%81%BF%E5%85%8Dintlongdouble%E7%9A%84%E8%A3%85%E7%AE%B1%E6%8B%86%E7%AE%B1%E7%9A%84%E6%AF%94%E8%BE%83%E5%99%A8comparatorcomparingintlongdoublekeyextractor)
      - [(5) 兼容null值的比较器：`Comparator.nullsFirst(comparator)`](#5-%E5%85%BC%E5%AE%B9null%E5%80%BC%E7%9A%84%E6%AF%94%E8%BE%83%E5%99%A8comparatornullsfirstcomparator)
      - [(6) 倒序比较器：`Comparator.reversed()`](#6-%E5%80%92%E5%BA%8F%E6%AF%94%E8%BE%83%E5%99%A8comparatorreversed)
  - [8.5 使用文件](#85-%E4%BD%BF%E7%94%A8%E6%96%87%E4%BB%B6)
    - [8.5.1 读取文件行的流：`Files.lines`方法](#851-%E8%AF%BB%E5%8F%96%E6%96%87%E4%BB%B6%E8%A1%8C%E7%9A%84%E6%B5%81fileslines%E6%96%B9%E6%B3%95)
      - [(1) 按行读取文件：`Files.lines(Path)`](#1-%E6%8C%89%E8%A1%8C%E8%AF%BB%E5%8F%96%E6%96%87%E4%BB%B6fileslinespath)
      - [(2) 按行读取其他数据：`BufferedReader.lines()`](#2-%E6%8C%89%E8%A1%8C%E8%AF%BB%E5%8F%96%E5%85%B6%E4%BB%96%E6%95%B0%E6%8D%AEbufferedreaderlines)
      - [(3) 抛出的异常：IOException变为UncheckedIOException](#3-%E6%8A%9B%E5%87%BA%E7%9A%84%E5%BC%82%E5%B8%B8ioexception%E5%8F%98%E4%B8%BAuncheckedioexception)
    - [8.5.2 遍历目录项的流（`Stream<Path>`）](#852-%E9%81%8D%E5%8E%86%E7%9B%AE%E5%BD%95%E9%A1%B9%E7%9A%84%E6%B5%81streampath)
      - [(1) `Files.list(Path)`](#1-fileslistpath)
      - [(2) ` Files.walk(Path)`](#2--fileswalkpath)
      - [(3) `Files.find(Path, maxDepth, FileVisitOption...)`](#3-filesfindpath-maxdepth-filevisitoption)
    - [8.5.3 Base64](#853-base64)
      - [(1) 字符串编码/解码](#1-%E5%AD%97%E7%AC%A6%E4%B8%B2%E7%BC%96%E7%A0%81%E8%A7%A3%E7%A0%81)
      - [(2) 自动编码/解码的流](#2-%E8%87%AA%E5%8A%A8%E7%BC%96%E7%A0%81%E8%A7%A3%E7%A0%81%E7%9A%84%E6%B5%81)
  - [8.6 注解](#86-%E6%B3%A8%E8%A7%A3)
    - [8.6.1 可重复的注解](#861-%E5%8F%AF%E9%87%8D%E5%A4%8D%E7%9A%84%E6%B3%A8%E8%A7%A3)
      - [(1) 使用可重复的注解](#1-%E4%BD%BF%E7%94%A8%E5%8F%AF%E9%87%8D%E5%A4%8D%E7%9A%84%E6%B3%A8%E8%A7%A3)
      - [(2) 编写可重复的注解](#2-%E7%BC%96%E5%86%99%E5%8F%AF%E9%87%8D%E5%A4%8D%E7%9A%84%E6%B3%A8%E8%A7%A3)
      - [(3) 例子](#3-%E4%BE%8B%E5%AD%90)
    - [8.6.2 可用于类型的注解](#862-%E5%8F%AF%E7%94%A8%E4%BA%8E%E7%B1%BB%E5%9E%8B%E7%9A%84%E6%B3%A8%E8%A7%A3)
    - [8.6.3 方法参数反射](#863-%E6%96%B9%E6%B3%95%E5%8F%82%E6%95%B0%E5%8F%8D%E5%B0%84)
  - [8.7 其他改进](#87-%E5%85%B6%E4%BB%96%E6%94%B9%E8%BF%9B)
    - [8.7.1 Null检查：`Objects.isNull`和`Objects.nonNull`](#871-null%E6%A3%80%E6%9F%A5objectsisnull%E5%92%8Cobjectsnonnull)
    - [8.7.2 延迟消息](#872-%E5%BB%B6%E8%BF%9F%E6%B6%88%E6%81%AF)
      - [(1) `java.util.Logger`对延迟操作的支持](#1-javautillogger%E5%AF%B9%E5%BB%B6%E8%BF%9F%E6%93%8D%E4%BD%9C%E7%9A%84%E6%94%AF%E6%8C%81)
      - [(2) `Object.requireNonNull`的lambda版本](#2-objectrequirenonnull%E7%9A%84lambda%E7%89%88%E6%9C%AC)
    - [8.7.3 正则表达式](#873-%E6%AD%A3%E5%88%99%E8%A1%A8%E8%BE%BE%E5%BC%8F)
      - [(1) 用`组名`提取`命名捕获组`匹配的内容](#1-%E7%94%A8%E7%BB%84%E5%90%8D%E6%8F%90%E5%8F%96%E5%91%BD%E5%90%8D%E6%8D%95%E8%8E%B7%E7%BB%84%E5%8C%B9%E9%85%8D%E7%9A%84%E5%86%85%E5%AE%B9)
      - [(2) 用`正则表达式`生成`Stream<String>`](#2-%E7%94%A8%E6%AD%A3%E5%88%99%E8%A1%A8%E8%BE%BE%E5%BC%8F%E7%94%9F%E6%88%90streamstring)
    - [8.7.4 语言环境](#874-%E8%AF%AD%E8%A8%80%E7%8E%AF%E5%A2%83)
      - [(1) 语言环境](#1-%E8%AF%AD%E8%A8%80%E7%8E%AF%E5%A2%83)
      - [(2) 语言范围查找](#2-%E8%AF%AD%E8%A8%80%E8%8C%83%E5%9B%B4%E6%9F%A5%E6%89%BE)
    - [8.7.5 JDBC](#875-jdbc)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# CH08 其他改进

## 8.1 类似python的字符串拼接

字符串拼接更加方便，字符串可以来自`数组`或者`Iterable<? extends CharSequence>`对象 

> ```java
> // 拼接可变参数列表中的字符串
> String joined = String.join("/", "usr", "local", "bin"); // "usr/local/bin"
> System.out.println(joined);
> // 输出：usr/local/bin
> 
> // 拼接Set<String>中的字符串
> String ids = String.join(", ", ZoneId.getAvailableZoneIds());
> System.out.println(ids);
> // 输出：Asia/Aden, America/Cuiaba, Etc/GMT+9, ......
> ```

完整代码：[../code/ch8/sec01/Strings.java](../code/ch8/sec01/Strings.java)

## 8.2 数字类

完整代码：[../code/ch8/sec02/Numbers.java](../code/ch8/sec02/Numbers.java)

### 8.2.1 包装类的`BYTES`字段 

7种包装类（不包括Boolean）都提供了一个BYTES字段，以byte为单位表示该类型的长度

> ```java
> System.out.printf("Integer   SIZE: %d BYTES: %d\n", Integer.SIZE  , Integer.BYTES  );
> System.out.printf("Long      SIZE: %d BYTES: %d\n", Long.SIZE     , Long.BYTES     );
> System.out.printf("Double    SIZE: %d BYTES: %d\n", Double.SIZE   , Double.BYTES   );
> System.out.printf("Float     SIZE: %d BYTES: %d\n", Float.SIZE    , Float.BYTES    );
> System.out.printf("Short     SIZE: %d BYTES: %d\n", Short.SIZE    , Short.BYTES    );
> System.out.printf("Byte      SIZE: %d BYTES: %d\n", Byte.SIZE     , Byte.BYTES     );
> System.out.printf("Character SIZE: %d BYTES: %d\n", Character.SIZE, Character.BYTES);
> // Integer   SIZE: 32 BYTES: 4
> // Long      SIZE: 64 BYTES: 8
> // Double    SIZE: 64 BYTES: 8
> // Float     SIZE: 32 BYTES: 4
> // Short     SIZE: 16 BYTES: 2
> // Byte      SIZE: 8  BYTES: 1
> // Character SIZE: 16 BYTES: 2
> ```

### 8.2.2 包装类`hashCode`静态方法

8种包装类，都提供了一个静态函数版本的hashCode方法，用于省去拆箱装箱的过程

> ```java
> System.out.println(Integer.hashCode(1));     // 1
> System.out.println(Long.hashCode(1));        // 1
> System.out.println(Double.hashCode(0.25));   // 1070596096
> System.out.println(Float.hashCode(0.25f));   // 1048576000
> System.out.println(Short.hashCode((short)1));      // 1
> System.out.println(Byte.hashCode((byte)1));        // 1
> System.out.println(Character.hashCode('c')); // 99
> System.out.println(Boolean.hashCode(true));  // 1231
> ```

### 8.2.3 用于Stream聚合函数的静态方法

#### (1) `sum`，`max`、`min`

> ```java
> // java8 为Short, Integer, Long, Float, Double这5种类型提供了静态方法sum、max、min
> Random generator = new Random();
> OptionalInt sum = generator.ints().limit(100).reduce(Integer::sum);
> OptionalInt min = generator.ints().limit(100).reduce(Integer::min);
> System.out.println(sum); // OptionalInt[-15423225]
> System.out.println(min); // OptionalInt[-2125555369]
> ```

#### (2) `logicalAnd`, `logicalOr`, `logicalXor`

> ```java
> // java8 为Boolean提供了logicalAnd, logicalOr, logicalXor方法
> Optional<Boolean> xor = generator.ints().limit(100).mapToObj(i -> i % 2 == 0).reduce(Boolean::logicalXor);
> System.out.println(xor); // Optional[false]
> ```

### 8.2.4 无符号运算支持 

#### (1) Long和Integer的`compareUnsigned`,` divideUnsigned`, `remainderUnsigned`静态方法

> ```java
> // 对于Long和Integer，compareUnsigned, divideUnsigned, remainderUnsigned都可以把他们当做无符号数来运算
> // 例如下面两个int
> int maxValue            = Integer.MAX_VALUE;  							// 2147483647
> int nextValue           = maxValue + 1;       							// -2147483648 (整形数溢出）
> // 运算结果如下
> int maxCmpNext          = Integer.compareUnsigned(maxValue, nextValue); // -1
> int nextCmpMax          = Integer.compareUnsigned(nextValue,maxValue ); // 1
> int nextDivide65536     = Integer.divideUnsigned(nextValue, 65536);     // 32768
> int nextRemainder65536  = Integer.remainderUnsigned(nextValue,  65536); // 0
> ```

#### (2) Byte，Short，Integer的`toUnsignedInt`静态方法

> ```java
> // Byte，Short，Integer新增了toUnsignedInt方法
> byte b = -127; //byte是8位
> int bi = Byte.toUnsignedInt(b); //  129
> ```

#### (3) Integer的`toUnsignedLong`静态方法

> ```java
> // 而Integer还新增了toUnsignedLong方法
> long l = Integer.toUnsignedLong(Integer.MAX_VALUE + 1);  //2147483648
> ```

#### (4) 浮点数`isFinite`检查

> ~~~java
> // Float, Double新增了静态方法isFinite：表示不是无穷大、无穷小、也不是Nan
> System.out.println(Double.isFinite(1.0 / 0.0));    		// false
> System.out.println(Double.isFinite(Math.sqrt(-1.0))); 	// false
> ~~~

#### (5) 从BigInteger提取原始类型：`intValueExact`、`longValueExact`、`shortValueExact`、`byteValueExact`

> ```java
> // 从BigInteger中提取原始类型，超出范围时抛ArithmeticException异常
> int intExact = new BigInteger("129").intValueExact();       // 129
> long longExact = new BigInteger("129").longValueExact();    // 129
> short shortExact = new BigInteger("129").shortValueExact(); // 129
> byte byteExact = new BigInteger("129").byteValueExact();
> // Exception in thread "main" java.lang.ArithmeticException: BigInteger out of byte range
> // at java.base/java.math.BigInteger.byteValueExact(BigInteger.java:4846)
> // at Numbers.main(Numbers.java:70)
> ```

## 8.3 新的数学函数 

完整代码：[../code/ch8/sec03/MathematicalFunctions.java](../code/ch8/sec03/MathematicalFunctions.java)

### 8.3.1 带有溢出检查的算数运算：`Math.*Exact`

> `Math.addExact(...)`, `Math.substractExact(...)`, `Math.multiplyExact(...)`, `Math.incrementExact(...)`, `Math.decrementExact(...)`, `Math.negateExact(...)`

直接做算数运算，不会进行溢出检查，当溢出发生时会得到一个错误的结果

> ```java
> int overflowedInt = 1000000 * 1000000;
> System.out.println(overflowedInt);
> // -727379968
> ```

如果使用上面的`Math.*Exact`方法，则会在异常发生时抛出一个`ArithmeticException`

> 而方法名后缀`Exact`则暗示该方法比较严格、会检查溢出
>
> ```java
> // 方法名后缀`Exact`则暗示该方法比较严格、会检查溢出
> try {
>    	System.out.println(Math.multiplyExact(1000000, 1000000));
> } catch (ArithmeticException ex) {
>    	// 溢出时会抛出ArithmeticException
>    	ex.printStackTrace();
> }
> // 输出
> // java.lang.ArithmeticException: integer overflow
> //	  at java.base/java.lang.Math.multiplyExact(Math.java:909)
> //	  at MathematicalFunctions.main(MathematicalFunctions.java:23)
> ```

### 8.3.2 带有溢出检查的数值转换：`Math.to*Exact`

> ```java
> // Java 8提供的带“Exact”标识的类型转换函数，例如toIntExact, 同样会检查类型转换后是否发生异常
> long largerThanIntMax = (long)Integer.MAX_VALUE + 10;
> System.out.println("largerThanIntMax: " + largerThanIntMax); // 2147483657
> try {
>    	int n = Math.toIntExact(largerThanIntMax);
> } catch (ArithmeticException ex) {
>    	ex.printStackTrace();
> }
> // java.lang.ArithmeticException: integer overflow
> //      at java.base/java.lang.Math.toIntExact(Math.java:1080)
> //      at MathematicalFunctions.main(MathematicalFunctions.java:33)
> ```

### 8.3.3 避免结果为负问题的求模函数：`Math.floorMod`

> ```java
> // Java 8提供的floorMod能够解决取模时，当除数为负数时，得到的模为负数的问题（严格讲，floorMod仍然存在返回负数的可能，但是实际中很少发生）
> // * 模（即余数）应当永远大于0，
> // * 但是计算机诞生时，先驱们并不知道这条数学规则，导致了余数正负的不确定性
> System.out.println(Math.floorMod(6 + 10, 12)); // Ten hours later
> System.out.println(Math.floorMod(6 - 10, 12)); // Ten hours earlier
> System.out.println(Math.floorMod(6 + 10, -12));
> System.out.println(Math.floorMod(6 + 10, -12));
> ```

### 8.3.4 返回比指定值小但是最接近的浮点数: `Math.nextDown`

> 与Java 6的Math.nextUp相对应
>
> ```java
> Random generator = new Random(164311266871034L);
> // Also try new Random(881498)
> for (int tries = 1; tries < 1000000000; tries++) {
>     double r = 1.0 - generator.nextDouble();
>     if (r == 1.0) {
>         System.out.println("It happened at try " + tries);
>         r = Math.nextDown(r);
>         System.out.println(r);
>     }
> }
> ```

##  8.4 集合

### 8.4.1 集合类中新增的方法

完整代码：[../code/ch8/sec04/CollectionMethods.java](../code/ch8/sec04/CollectionMethods.java)

除了`stream`，`parallelStream`，`spliterator`方法以外，Java 8还向集合类及接口中添加如下方法

> | 集合类     | 新增方法                                                     |
> | ---------- | ------------------------------------------------------------ |
> | Iterable   | forEach                                                      |
> | Collection | removeIf                                                     |
> | List       | replaceAll, sort                                             |
> | Map        | forEach,  replace,  replaceAll,  remove(key,value) (仅在<key,value>存在时才删除),  putIfAbsent,  compute,  computeIf |
> | Iterator   | forEachRemaining (将迭代器剩余元素都传递给一个函数)          |
> | BitSet     | stream (生成集合中的所有元素，返回一个由int组成的stream）    |

使用例子如下

> ```java
> List<String> ids = new ArrayList<>(ZoneId.getAvailableZoneIds());
> 
> // Collection.removeIf
> ids.removeIf(s -> !s.startsWith("America"));
> 
> // Iterable.forEach
> ids.forEach(System.out::println);
> 
> // List.replaceAll
> ids.replaceAll(s -> s.replace("America/", ""));
> 
> // Iterable.forEach
> ids.forEach(System.out::println);
> BitSet bits = new BitSet();
> ids.forEach(s -> bits.set(s.length()));
> 
> // BitSet.stream
> System.out.println(Arrays.toString(bits.stream().toArray()));
> ```

### 8.4.2 比较器

完整代码：[../code/ch8/sec04/ComparatorsTest.java](../code/ch8/sec04/ComparatorsTest.java)

#### (1) 基于字段的比较器：`Comparator.comparing(keyExtractor)`

> ```java
> // 基于字段比较
> // Comparator.comparing(Function<? super T, ? extends U> keyExtractor)
> Arrays.sort(
>         TestData.personArray,
>         Comparator.comparing(Person::getName)
> );
> display(TestData.personArray, 5);
> // [Abraham Lincoln,Andrew Jackson,Andrew Johnson,Barack Hussein Obama,Benjamin Harrison...]
> ```

#### (2) 多级比较器：`Comparator.thenComparing(keyExtractor)`

> ```java
> // 多级比较
> // Comparator.comparing(Function<? super T, ? extends U> keyExtractor)
> // Comparator.thenComparing(Function<? super T, ? extends U> keyExtractor)
> Arrays.sort(
>         TestData.personArray,
>         Comparator
>             .comparing(Person::getLastName)
>             .thenComparing(Person::getFirstName)
> );
> display(TestData.personArray, 5);
> // [John Adams,John Quincy Adams,James Buchanan,George Herbert Walker Bush,George Walker Bush...]
> ```

#### (3) 基于字段及排序规则来比较：`Comparator.comparing(keyExtractor,keyComparator)`

> ```java
> // 基于字段及排序规则来比较
> // Comparator.comparing(
> //            Function<? super T, ? extends U> keyExtractor,
> //            Comparator<? super U> keyComparator)
> Arrays.sort(
>         TestData.personArray,
>         Comparator.comparing(
>             Person::getName,
>             (s, t) -> Integer.compare(s.length(), t.length())
>         )
> );
> display(TestData.personArray, 5);
> // [John Adams,John Tyler,Gerald Ford,James Monroe,James Madison...]
> ```

#### (4) 避免int/long/double的装箱拆箱的比较器：`Comparator.comparingInt/Long/Double(keyExtractor)`

> ```java
> // 用来避免int/long/double的装箱拆箱的比较器
> // Comparator.comparingInt(ToIntFunction<? super T> keyExtractor)
> // Comparator.comparingLong(ToIntFunction<? super T> keyExtractor)
> // Comparator.comparingDouble(ToIntFunction<? super T> keyExtractor)
> Arrays.sort(
>         TestData.personArray,
>         Comparator.comparingInt(p -> p.getName().length())
> );
> display(TestData.personArray, 5);
> // [John Adams,John Tyler,Gerald Ford,James Monroe,James Madison...]
> ```

#### (5) 兼容null值的比较器：`Comparator.nullsFirst(comparator)`

> ```java
> // 兼容null值的比较，让null值排在最前或最后，而不是抛异常
> // Comparator.nullsFirst(Comparator<? super T> comparator)
> // nullsLast(Comparator<? super T> comparator)
> //
> // Comparator.自然顺序
> // Comparator.naturalOrder()
> Arrays.sort(
>         TestData.personArray,
>         Comparator.comparing(
>                 Person::getMiddleName,
>                 nullsFirst(naturalOrder())
>         )
> );
> display(TestData.personArray, 5);
> // [John Adams,John Tyler,Gerald Ford,James Monroe,James Madison...]
> ```

#### (6) 倒序比较器：`Comparator.reversed()`

```java
// 倒序排序
// Comparator.reversed()
Arrays.sort(
        TestData.personArray,
        Comparator.comparing(
            Person::getMiddleName,
            // Comparator<T>.natrualOrder().reversed()等价于Comparator<T>.reverseOrder()
            nullsFirst(Comparator.<String>naturalOrder().reversed())
        )
);
display(TestData.personArray, 5);
// [John Adams,John Tyler,Gerald Ford,James Monroe,James Madison...]
```

## 8.5 使用文件

### 8.5.1 读取文件行的流：`Files.lines`方法

完整代码：[../code/ch8/sec05/StreamsOfLines.java](../code/ch8/sec05/StreamsOfLines.java)

#### (1) 按行读取文件：`Files.lines(Path)`

> ```java
> // 调用Files.lines(Path)的到文件行的流(Stream)
> // 把Stream放在java 7的try-with-resource语句中，可以保证其close()方法在离开try块时被调用，从而不需要手动调用
> // 可以通过onClose方法，设置在close()被调用时执行的操作
> try (Stream<String> filteredLines = Files
>                 .lines(Paths.get(BASE_DIR,"StreamsOfLines.java"))
>                 .onClose(() -> System.out.println("Closing"))
>                 .filter(s -> s.contains("password"))) {
>    	Optional<String> passwordEntry = filteredLines.findFirst();
>    	passwordEntry.ifPresent(System.out::println);
> }
> // 输出
> // Optional<String> passwordEntry = lines.filter(s -> s.contains("password")).findFirst();
> // Closing
> ```

#### (2) 按行读取其他数据：`BufferedReader.lines()`

> ```java
> URL url = new URL("http://horstmann.com");
> try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
>    	// BufferedReader.lines()：从BufferedReader生成Stream
>    	Stream<String> lines = reader.lines();
>    	Optional<String> imgEntry = lines
>    		.filter(s -> s.contains("<img "))
>    		.findFirst();
>    	imgEntry.ifPresent(System.out::println);
> }
> ```

#### (3) 抛出的异常：IOException变为UncheckedIOException

> 读取文件出错时原本会抛出IOException，但是流操作的方法并没有声明会抛出任何异常（导致IOException无法抛出），因此该异常会被转化为`UncheckedIOException`抛出（unchecked exception不受方法声明影响）
>
> ```java
> try (BufferedReader reader = new BufferedReader(
>         // 构造一个Reader，会在读取文件到第10行时抛出IOException
>         new Reader() {
>             private int count;
>             public void close() {}
>             public int read(char[] cbuf, int off, int len) throws IOException {
>                 if (++count == 10) {
>                     throw new IOException("Simulated exception");
>                 }
>                 return len;
>             }
>         }
> )) {
>    	Stream<String> lines = reader.lines();
>    	Optional<String> imgEntry = lines.filter(s -> s.contains("<img ")).findFirst();
>    	imgEntry.ifPresent(System.out::println);
> }
> // 输出：流操作已经将IOException转化为UncheckedIOException抛出了
> // Exception in thread "main" java.io.UncheckedIOException: java.io.IOException: Simulated exception
> // at java.base/java.io.BufferedReader$1.
> // ...
> ```

### 8.5.2 遍历目录项的流（`Stream<Path>`）

完整代码：[../code/ch8/sec05/StreamsOfDirectoryEntries.java](../code/ch8/sec05/StreamsOfDirectoryEntries.java)

#### (1) `Files.list(Path)`

> ```java
> // Files.list(Path)
> System.out.println("non-hidden directories and files in current directory");
> try (Stream<Path> entries = Files.list(Paths.get("./"))) {
>      entries
>     .filter(p -> !p.getFileName().toString().startsWith("."))
>     .limit(5)
>     .forEach(System.out::println);
> }
> // non-hidden directories and files in current directory
> // ./note
> // ./out
> // ./code
> // ./README.md
> ```

#### (2) ` Files.walk(Path)`

> ```java
> System.out.println("hidden directories and files including those in all sub-directories");
> try (Stream<Path> entries = Files.walk(Paths.get("./"))) {
>      entries
>     .filter(p -> p.getFileName().toString().startsWith("."))
>     .limit(5)
>     .forEach(System.out::println);
> }
> // hidden directories and files including those in all sub-directories
> // .
> // ./.DS_Store
> // ./code/ch7/sec01/.Rhistory
> // ./.gitignore
> // ./.git
> ```

#### (3) `Files.find(Path, maxDepth, FileVisitOption...)`

> ```java
> // Files.find(Path, maxDepth, FileVisitOption...)
> System.out.println("recent files in directories with directory-max-depth 5");
> int depth = 5;
> Instant oneMonthAgo = Instant.now().minus(30, ChronoUnit.DAYS);
> try (Stream<Path> entries = Files.find(Paths.get("./"), depth,
>         (path, attrs) -> attrs.creationTime().toInstant().compareTo(oneMonthAgo) >= 0)) {
>    	entries.limit(5).forEach(System.out::println);
> }
> // recent files in directories with directory-max-depth 5
> // .
> // ./note
> // ./note/ch09_java7_features.md
> // ./note/ch02_stream.md
> // ./note/ch07_js_engine_noshorn.md
> ```

### 8.5.3 Base64

> Base64编码将字节序列编码成一个（更长的）可打印的ASCII序列，在二进制数据传输、“基本“的HTTP认证等都被使用过。JDK之前提供的支持非常弱、直到Java 8才提供了标准编码器和解码器。
>
> Base64有如下要求
>
> * 每6 bit信息使用一些字符来编码，字符选择范围包括：`A-Za-z0-9`以及`+`和`-`
> * 通常编码后的字符串没有换行符，但是电子邮件使用的MIME标准要求每76个字符要使用一个"\r\n"换行符

#### (1) 字符串编码/解码

> ```java
> // 编码
> Base64.Encoder encoder = Base64.getEncoder();
> String encoded = encoder.encodeToString(someString.getBytes(StandardCharsets.UTF_8));
> System.out.println(encoded);
> 
> // 解码
> String decoded = new String(Base64.getDecoder().decode(encoded.getBytes()));
> System.out.println(decoded);
> ```

#### (2) 自动编码/解码的流

> ```java
> // 编码流：
> Path originalPath = Paths.get("code/ch8/sec05/", "Base64Demo.java");
> Path encodedPath  = Paths.get("code/ch8/sec05/", "Base64Demo.java.base64");
> encoder = Base64.getMimeEncoder();
> try (OutputStream output = Files.newOutputStream(encodedPath)) {
>    	Files.copy(originalPath, encoder.wrap(output));
> }
> 
> // 解码流：
> Path decodedPath = Paths.get("code/ch8/sec05/", "Base64Demo.java.decoded");
> Base64.Decoder decoder = Base64.getMimeDecoder();
> try (InputStream input = Files.newInputStream(encodedPath)) {
>    	Files.copy(decoder.wrap(input), decodedPath, StandardCopyOption.REPLACE_EXISTING);
> }
> ```

完整代码：[../code/ch8/sec05/Base64Demo.java](../code/ch8/sec05/Base64Demo.java)

##  8.6 注解

> Java 8 对注解的改进主要体现在三方面
>
> * 可重复的注解：容许同一个注解应用多次
> * 可用于类型的注解
> * 反射增强：Java 8能够得到方法参数的名称，进而可以简化标注在参数上的注解

### 8.6.1 可重复的注解

#### (1) 使用可重复的注解

> 在Java8之前，同一个注解不能重复使用，因此不得不将它们包装到一个父注解中，代码比较丑陋，例如
>
> ~~~java
> @Entity
> @PrimaryKeyJoinColumns({
> 	@PrimaryKeyJoinColumn(name = "ID"),
> 	@PrimaryKeyJoinColumn(name = "REGION")
> })
> public class Item { ... }
> ~~~
>
> Java 8容许注解被注解重复使用，一个框架可以会提供如下的注解，对于使用人员来说会更加方便
>
> ~~~java
> @Entity
> @PrimaryKeyJoinColumn(name = "ID"),
> @PrimaryKeyJoinColumn(name = "REGION")
> public class Item { ... }
> ~~~

#### (2) 编写可重复的注解

> 要将注解`标注为@Repeatable`，并提供一个`“容器注解”`，这样
>
> * 注解处理器调用`public <T extends Anntation> T getAnnotation(Class<T> annotationClass)`时，就可以通过返回的"容器注解"、获得封装在里面的多个可重复注解（Java 8为了兼容历史版本）
> * Java 8同时提供了另一个方法`getAnnotationByType`，可以返回一个注解数组

#### (3) 例子

> 注解
>
> * 可重复注解：[../code/ch8/sec06/TestCase.java](../code/ch8/sec06/TestCase.java)
>
> * “容器”注解：[../code/ch8/sec06/TestCases.java](../code/ch8/sec06/TestCases.java)
>
> 注解处理器：[../code/ch8/sec06/TestCaseProcessor.java](../code/ch8/sec06/TestCaseProcessor.java)
>
> 使用注解的代码：[../code/ch8/sec06/TestCaseDemo.java](../code/ch8/sec06/TestCaseDemo.java)

### 8.6.2 可用于类型的注解

Java 8之前的注解都是`声明注解`，只能被标注在一个`声明`（用于定义某个新名称的代码）上

> 例如下面代码的`Person`和`people`
>
> ~~~java
> @Entithy
> public class Person {
>    	...
> }
> ~~~
>
> ~~~java
> @SuppressWarnings("unchecked")
> List<Person> people = query.getResultList();
> ~~~

Java 8新增了`类型注解`，可以标注在任何`类型`上，包括

> * 泛型类型参数：`List<@NotNull String>`，`Comparator.<@NotNullString> reverseOrder()`
> * 数组、多维数组：
>     * `String[] @NotNull [] words`：`words[i][j]`不为null
>     * `String @NotNull [][] words`：`words`不为null
>     * ``String[] @NotNull [] words`：`words[i]`不为null
> * 父类和接口：`class Image implements @Rectangular Shape`
> * 调用构造函数：`new @Path String("/usr/bin")`
> * 强制类型转换和`instanceof`检查：`(@Path String) input`，`if (input instanceof @Path String)`，这些注解只用于外部工具，不会对类型转换或者instanceof的检查带来任何影响
> * 定义抛出的异常：`public Person read() throws @Localized IOException`
> * 通配符和类型绑定：`List<@ReadOnly ? Extends Person>`，`List<? Extends @ReadOnly> Person`
> * 方法和构造函数引用：`@Immutable Person::getName`

当然`类型注解`不能用于`类型`以外的地方，例如class或者import等，也不能用在其他注解上

> ~~~java
> @NotNull String.class //非法，不能用来标注class
> import java.long.@NotNull String; //非法，不能用来标注import
> ~~~

使用例子之一：`Checker Framework`

> 官网及文档：
>
> * [http://types.cs.washington.edu/checker-framework](http://types.cs.washington.edu/checker-framework)
> * [http://types.cs.washington.edu/checker-framework/tutorial](http://types.cs.washington.edu/checker-framework/tutorial)
>
> 它会为程序中所有的`非局部变量`（如方法返回值、成员变量等）都隐式地标注了`@NotNull`注解，这些变量不容许出现null值，除非在代码中显示地标注`@Nullable`

### 8.6.3 方法参数反射

> Java 8 提供的类 `java.lang.reflect.Parameter`使注解处理器可以获得参数的名称（但是需要以`javac -parameters SourceFile.java`的方式来编译

在没有方法参数反射之前，需要通过注解来提供参数名称，例如

> ~~~java
> Person getEmployee(@PathParam("dept") Long dept, @QueryParam("id") Long id)
> ~~~

使用方法参数反射之后，代码可以简化为

> ~~~java
> Person getEmployee(@PathParam Long dept, @QueryParam Long id)
> ~~~

## 8.7 其他改进 

### 8.7.1 Null检查：`Objects.isNull`和`Objects.nonNull`

新增量两个静态方法：`Objects.isNull`和`Objects.nonNull`，用在Stream中进行过滤，例如：

> 检查一个流是否有null值
>
> ~~~java
> stream.anyMatch(Object::isNull)
> ~~~
>
> 从一个流中获取所有非null对象
>
> ~~~java
> stream.filter(Object::nonNull)
> ~~~

### 8.7.2 延迟消息

#### (1) `java.util.Logger`对延迟操作的支持

方法：`log`、`logp`、`severe`、`warning`、`info`、`config`、`fine`、`finer`、`finest`

传入lambda表达式

> * 表达式的计算操作内容被延迟到日志打印时才执行
> * 如果不满足日志级别要求不用打印日志，这些操作就不需要执行

例如

> 在java7的时候，下面的操作不论是否打印日志都会执行
>
> ~~~java
> logger.finest("x: " + x + ", y:" + y);
> ~~~
>
> 用java8，可以通过lambda来传入要执行的操作，并延迟按需执行
>
> ~~~java
> logger.finest(() -> "x: " + x + ", y:" + y);
> ~~~

#### (2) `Object.requireNonNull`的lambda版本

> ~~~java
> this.directions = Objects.requireNotNull(
>     inputParameter,
> 	() -> "inputParameter for " + this.goal + "must not be null"
> )
> ~~~
>
> * 当`inputParameter`不为null时，赋给`this.directions`
>
> * 当`inputParameter`为null时，执行lambda表达式中的延迟操作、生成一个字符串，填充在NullPointerException中抛出

### 8.7.3 正则表达式

#### (1) 用`组名`提取`命名捕获组`匹配的内容

Java 7引入了`命名捕获组`，例如下面的`(?<city>)`和`(?<state>)`就包裹了两个命名捕获组的正则表达式

> ~~~java
> Pattern pattern = Pattern.compile("(?<city>[\p{L} ]+),\s*(?<state>[A-Z]{2})")
> ~~~

Java 8中， `Matcher`类的`start`、`end`和`group`方法可以使用`命名捕获组`的`名称`来获得匹配的字符串

> ~~~java
> Matcher matcher = pattern.matcher(input);
> if (matcher.matches()) {
>    	String city = matcher.group("city"); //"city"是写在正则表达式中的命名组名称
>    	...
> }
> ~~~

#### (2) 用`正则表达式`生成`Stream<String>`

> 正则表达式用作分隔符
>
> ~~~java
> Stream<String> strStream = Pattern.compile("[\\P{L}]+").splitAsStream(inputStr);
> ~~~
>
> 过滤stream，值保留能匹配正则表达式的item
>
> ~~~java
> Stream<String> afterFilter = word.filter(Pattern.compile("[A-Z]{2,}")).asPredicate());
> ~~~

### 8.7.4 语言环境

#### (1) 语言环境

> 不同地域的人习惯于不同的语言环境（`语种`、`日期格式`等），一套语言环境由以下5部分组成（根据IETF BCP47)：
>
> * 语言：由2-3个小写字母指定，例如`en`，`de`
> * 脚本：由4个字母组成，例如`Latn`（拉丁文），`Cuyrl`（西里尔文），`Hant`（繁体中文）
> * 国家：由2-3个大写字母组成，例如`US`，`CH`
> * 变量（可选）：目前有些变量被语言代码替换
> * 扩展（可选）：用于描述日历、数字等本地偏好，例如`u-nu-thai`表示使用泰国数字
> * 其他扩展：以`x-`开头，例如`x-java`
>
> 这些会体现在Locale的方法参数中

#### (2) 语言范围查找

> ```java
> // List中包含两个LanguageRange，分别表示"说德语"，以及"在瑞士"
> List<Locale.LanguageRange> priorityRangeList
>         = Stream.of("de", "*-CH")
>                 .map(Locale.LanguageRange::new)
>                 .collect(Collectors.toList());
> 
> // 用两个LanguageRange进行筛选，返回所有匹配的Local
> List<Locale> matches
>         = Locale.filter(
>                 priorityRangeList,
>                 Arrays.asList(Locale.getAvailableLocales()));
> System.out.println(matches);
> // 输出
> // [de_IT, de_CH, de_BE, de, de_LU, de_DE, de_LI, de_AT, pt_CH, gsw_CH, fr_CH, rm_CH, it_CH, wae_CH, en_CH]
> 
> // 用两个LanguageRange进行筛选，返回匹配度最高的Local
> Locale bestMatch = Locale.lookup(
>         priorityRangeList,
>         Arrays.asList(Locale.getAvailableLocales()));
> System.out.println(bestMatch);
> // 输出
> // de
> ```
>
> 代码链接：[../code/ch8/sec07/Locales.java](../code/ch8/sec07/Locales.java) 

### 8.7.5 JDBC

> (1) `java.sql.Date`、`Time`、`Timestamp`增加支持`LocalDate`、`LocalTime`和`LocalDateTime`的方法
>
> (2) `Statement`增加`,`方法，支持修改行数超过Integer.MAX_VALUE的更新操作
>
> (3) `Statement`和`ResultSet`增加泛型方法`getObject(columnStr, class)`方法以及对应的`setObject`方法，例如：
>
> ~~~java
> URL url = result.getObject("link", URL.class);
> ~~~
>
> 准确说`getObject`方法是java 7新增的，只有`setObject`方法才是java 8新增的

