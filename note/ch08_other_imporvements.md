# CH08 其他改进

[TOC]

## 8.1 字符串拼接

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
> // Byte      SIZE: 8 BYTES: 1
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
> // 5. BigInteger增加了实例方法(long|int|short|byte)ValueExact四个方法，
> // 用来返回long、int、short、byte，并且当值不在目标范围内时会抛出一个ArithmeticException异常
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
>     System.out.println(Math.multiplyExact(1000000, 1000000));
> } catch (ArithmeticException ex) {
>     // 溢出时会抛出ArithmeticException
>     ex.printStackTrace();
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
>     int n = Math.toIntExact(largerThanIntMax);
> } catch (ArithmeticException ex) {
>     ex.printStackTrace();
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





## 8.5 使用文件

完整代码：[../code/ch8/sec05/Base64Demo.java](../code/ch8/sec05/Base64Demo.java)

完整代码：[../code/ch8/sec05/StreamsOfDirectoryEntries.java](../code/ch8/sec05/StreamsOfDirectoryEntries.java)

完整代码：[../code/ch8/sec05/StreamsOfLines.java](../code/ch8/sec05/StreamsOfLines.java)



##  8.6 注解

完整代码：[../code/ch8/sec06/TestCase.java](../code/ch8/sec06/TestCase.java)
完整代码：[../code/ch8/sec06/TestCaseDemo.java](../code/ch8/sec06/TestCaseDemo.java)
完整代码：[../code/ch8/sec06/TestCaseProcessor.java](../code/ch8/sec06/TestCaseProcessor.java)
完整代码：[../code/ch8/sec06/TestCases.java](../code/ch8/sec06/TestCases.java)



## 8.7 其他 

完整代码：[../code/ch8/sec07/Locales.java](../code/ch8/sec07/Locales.java) 

