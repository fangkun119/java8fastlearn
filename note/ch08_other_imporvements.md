# CH08 其他改进

[TOC]

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

> ```
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
>     Optional<String> passwordEntry = filteredLines.findFirst();
>     passwordEntry.ifPresent(System.out::println);
> }
> // 输出
> // Optional<String> passwordEntry = lines.filter(s -> s.contains("password")).findFirst();
> // Closing
> ```

#### (2) 按行读取其他数据：`BufferedReader.lines()`

> ```java
> URL url = new URL("http://horstmann.com");
> try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
>     // BufferedReader.lines()：从BufferedReader生成Stream
>     Stream<String> lines = reader.lines();
>     Optional<String> imgEntry = lines
>             .filter(s -> s.contains("<img "))
>             .findFirst();
>     imgEntry.ifPresent(System.out::println);
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
>     Stream<String> lines = reader.lines();
>     Optional<String> imgEntry = lines.filter(s -> s.contains("<img ")).findFirst();
>     imgEntry.ifPresent(System.out::println);
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
>     entries.limit(5).forEach(System.out::println);
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
>     Files.copy(originalPath, encoder.wrap(output));
> }
> 
> // 解码流：
> Path decodedPath = Paths.get("code/ch8/sec05/", "Base64Demo.java.decoded");
> Base64.Decoder decoder = Base64.getMimeDecoder();
> try (InputStream input = Files.newInputStream(encodedPath)) {
>     Files.copy(decoder.wrap(input), decodedPath, StandardCopyOption.REPLACE_EXISTING);
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

#### (2)  编写可重复的注解

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

#### (1) 可用于类型的注解

Java 8之前的注解都是`声明注解`，只能被标注在一个`声明`（用于定义某个新名称的代码）上

> 例如下面代码的`Person`和`people`
>
> ~~~java
> @Entithy
> public class Person {
>     ...
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

#### (1) 方法参数反射

> Java 8 提供的类 `java.lang.reflect.Parameter`使注解处理器可以获得参数的名称（但是需要以`javac -parameters SourceFile.java`的方式来编译

在没有方法参数反射之前，需要通过注解来提供参数名称，例如

> ~~~java
> Person getEmployee(@PathParam("dept") Long dept, @QueryParam("id") Long id)
> ~~~

使用方法参数反射之后，代码可以简化为

> ~~~java
> Person getEmployee(@PathParam Long dept, @QueryParam Long id)
> ~~~

## 8.7 其他 

完整代码：[../code/ch8/sec07/Locales.java](../code/ch8/sec07/Locales.java) 

