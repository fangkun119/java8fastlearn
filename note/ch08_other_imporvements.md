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



##  8.4 集合

完整代码：[../code/ch8/sec04/CollectionMethods.java](../code/ch8/sec04/CollectionMethods.java)

完整代码：[../code/ch8/sec04/Comparators.java](../code/ch8/sec04/Comparators.java)



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

