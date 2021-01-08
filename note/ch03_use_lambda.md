# CH03 Lambda的应用

[TOC]

## 3.1 延迟执行

### 3.1.1 内容

所有Lambda表达式都是延迟执行的，可以使用在需要延迟执行、按需执行的场景中，例如：

> * 在另一个线程中运行的代码
> * 多次运行的代码
> * 在算法的执行节点、某种状态被触发时上才需要运行的代码

例子：日志打印

> 日志打印时的`字符串合并逻辑`被封装在`() -> "x: " + x + ", y: " + y`这样一个lambda表达式中
>
> * lambda表达式是按需执行、延迟执行的
> * 因此只有当`logger.isLoggable(Level.INFO)`为true时，`字符串合并`才会被执行
>
> ```java
> import java.util.function.*;
> import java.util.logging.*;
> 
> public class Logging {
>    public static void info(
>            Logger logger, 
>        	   Supplier<String> message /*不传入String而是传入lambda表达式*/
>    ) {
>       if (logger.isLoggable(Level.INFO))
>          logger.info(message.get());
>    }
> 
>    public static void main(String[] args) {
>       double x = 3;
>       double y = 4;
>       info(Logger.getGlobal(), () -> "x: " + x + ", y: " + y);
>       // 信息: x: 3.0, y: 4.0
>    }
> }
> ```
>
> 完整代码：[../code/ch3/sec01/Logging.java](../code/ch3/sec01/Logging.java)

### 3.2.2 API：

#### (1) `Supplier<T>`

> ```java
> package java.util.function;
> @FunctionalInterface
> public interface Supplier<T> {
>        T get();
> }
> ```

## 3.3 理解lambda作为参数时的传参过程

### 3.3.1 内容

例子：[../code/ch3/sec02/Parameters.java](../code/ch3/sec02/Parameters.java)

> 参数类型是`函数式接口`，编译器根据 `lambda表达式`的输入输出，生成能匹配上的`函数式接口`实现类的类对象，进行调用。更多信息可以参考《章节1.3-函数式接口》
>
> ```java
> public class Parameters {
>    public static void main(String[] args) {
>       // IntConsumer
>       repeat(10, i  -> System.out.println("Countdown: " + (9 - i)));
>       // Runnable
>       repeat(10, () -> System.out.println("Hello, World!"));
>    }
> 
>    public static void repeat(int n, IntConsumer action) {
>       for (int i = 0; i < n; i++) {
>          action.accept(i);
>       }
>    }
> 
>    public static void repeat(int n, Runnable action) {
>       for (int i = 0; i < n; i++) {
>          action.run();
>       }
>    }
> }
> ```

### 3.3.2 API

#### (1) `IntConsumer`

> ```java
> package java.util.function;
> import java.util.Objects;
> 
> @FunctionalInterface
> public interface IntConsumer {
>        void accept(int value);
>     
>        default IntConsumer andThen(IntConsumer after) {
>            Objects.requireNonNull(after);
>            return (int t) -> { accept(t); after.accept(t); };
>        }
> }
> ```

#### (2) `Runnable`

> ```java
> package java.lang;
> 
> @FunctionalInterface
> public interface Runnable {
>        public abstract void run();
> }
> ```

## 3.3 选择函数式接口（Functional Interface）

### 3.3.1 使用函数式接口的好处

> 例如，项目的运行步骤中需要`文件检测环节`，即执行某些匹配规则，来确定文件是否与其相符合。
>
> 接口是`Predicate<File>`好呢？还是`File`好呢？
>
> 显然是`Predicate<File>`更好，这样将更加灵活和简洁，除非项目中已经大量依赖`File`接口不方便更改

### 3.3.2 实例：编写并使用自定义的函数式接口

> 编写`函数式接口`
>
> ```java
> @FunctionalInterface
> interface ColorTransformer {
> Color apply(int x, int y, Color colorAtXY);
> }
> ```
>
> 以`函数式接口`作为参数的代码
>
> ```java
> public static Image transform(Image in, ColorTransformer f /*自定义函数式接口*/ ) {
>     int width = (int) in.getWidth();
>     int height = (int) in.getHeight();
>     WritableImage out = new WritableImage(width, height);
>     for (int x = 0; x < width; x++) {
>        for (int y = 0; y < height; y++) {
>           // 调用函数式接口的apply方法
>           out.getPixelWriter().setColor(x, y, f.apply(x, y, in.getPixelReader().getColor(x, y)));
>        }
>     }
>     return out;
> }
> ```
>
> 提供符合`函数式接口`的lambda表达式的代码
>
> ```java
> // 传入符合函数式接口 ColorTransformer 的 lambda表达式
> Image image2 = transform(image, (x, y, c) -> (x / 10) % 2 == (y / 10) % 2 ? Color.GRAY : c);
> ```

完整代码：[../code/ch3/sec03/ImageDemo.java](../code/ch3/sec03/ImageDemo.java)

### 3.3.3 生成组合函数

> 以`Predict<T>`为例，除了抽象方法`test`以外，它还提供了`and`, `or`,` negate`,` isEqual`四个`default`函数用来生成组合函数
>
> ```java
> package java.util.function;
> 
> import java.util.Objects;
> 
> @FunctionalInterface
> public interface Predicate<T> {
>     boolean test(T t);
> 
>     default Predicate<T> and(Predicate<? super T> other) {
>         Objects.requireNonNull(other);
>         return (t) -> test(t) && other.test(t);
>     }
> 
>     default Predicate<T> negate() {
>         return (t) -> !test(t);
>     }
> 
>     default Predicate<T> or(Predicate<? super T> other) {
>         Objects.requireNonNull(other);
>         return (t) -> test(t) || other.test(t);
>     }
> 
>     static <T> Predicate<T> isEqual(Object targetRef) {
>         return (null == targetRef)
>              ? Objects::isNull
>              : object -> targetRef.equals(object);
>     }
> }
> ```
>
> 比如如下的代码
>
> ~~~java
> Predicate.isEqual(a).or(Predicate.isEqual(b))
> ~~~

### 3.3.4 Java 8为普通类型提供的函数式接口

> | 函数式接口        | 参数类型 | 返回类型 | 抽象方法名 | 描述                 | 其他方法                   |
> | ----------------- | -------- | -------- | ---------- | -------------------- | -------------------------- |
> | Runnable          | 无       | void     | run        | 无参数无返回值的操作 |                            |
> | Supplier<T>       | 无       | T        | get        | 返回T                |                            |
> | Consumer<T>       | T        | void     | accept     | 处理T                | chain                      |
> | BiConsumer<T,U>   | T, U     | void     | accept     | 处理T、U             | chain                      |
> | Function<T,R>     | T        | R        | apply      | 输入T，返回R         | compose, andThen, identity |
> | BiFunction<T,R,U> | T,U      | R        | apply      | 输入T和U，返回R      | andThen                    |
> | UnaryOperator<T>  | T        | T        | apply      | 输入T，返回T         | compose, andThen, identity |
> | BinaryOperator<T> | T,T      | T        | apply      | 输入两个T，返回1个T  | andThen                    |
> | Predicate<T>      | T        | boolean  | test       | 输入T，返回boolean   | and, or, negate, isEqual   |
> | BiPredicate<T, U> | T,U      | boolean  | test       | 输入T、U返回boolean  | and, or, negate            |

### 3.3.5 Java 8为原始类型提供的函数式接口

> 小写字母*`p`、`q`*所代表的字面为int、long、double
>
> 大写字母**`P`、`Q`**所代表的字面为Int、Long、Double
>
> | 函数式接口             | 参数类型     | 返回类型 | 抽象方法名     |
> | ---------------------- | ------------ | -------- | -------------- |
> | BooleanSupplier        | none         | boolean  | getAsBoolean   |
> | **`P`**Suppier         | none         | *`p`*    | getAs**`P`**   |
> | **`P`**Consumer        | **`P`**      | void     | accept         |
> | Obj**`P`**Consumer     | T, *`p`*     | void     | accept         |
> | **`P`**Function<T>     | *`p`*        | T        | apply          |
> | PTo**`Q`**Function     | *`p`*        | *`q`*    | applyAs**`Q`** |
> | To**`P`**Function<T>   | T            | *`p`*    | applyAs**`P`** |
> | To**`P`**BiFunction<T> | T, U         | **`P`**  | applyAs**`P`** |
> | **`P`**UnaryOperator   | *`p`*        | **`P`**  | applyAs**`P`** |
> | **`P`**BinaryOperator  | *`p`*, *`p`* | *`p`*    | applyAs**`P`** |
> | **`P`**Predicate       | *`p`*        | boolean  | test           |

## 3.4 把`函数`用作返回值

经常会遇到需要把函数用作返回值的场景，例如返回一个Comparator交给Arrays.sort等

关于如何把函数（`lambda表达式`,`函数式接口的实现对象`）用作返回值，例子如下

> 完整代码：[../code/ch3/sec04/ImageDemoCh3Sec04.java](../code/ch3/sec04/ImageDemoCh3Sec04.java)
>
> 以`函数式接口`作为参数的代码
>
> ```java
> public static Image transform(Image in, UnaryOperator<Color> f /*java自带的函数式接口*/) {
>        int width = (int) in.getWidth();
>        int height = (int) in.getHeight();
>        WritableImage out = new WritableImage(width, height);
>        for (int x = 0; x < width; x++) {
>           for (int y = 0; y < height; y++) {
>              // 调用函数式接口的apply方法
>              out.getPixelWriter().setColor(x, y, f.apply(in.getPixelReader().getColor(x, y)));
>           }
>        }
>        return out;
> }
> ```
>
> 把函数用作返回值
>
> ```java
> public static UnaryOperator<Color> brighten(double factor) {
>     // lambda表达式会编译成一个UnaryOperator<Color>对象返回
>        return c -> c.deriveColor(0, 1, factor, 1); 
>    }
> ```
> 
>把上述两处代码连接起来
> 
>```java
> // 把brighten返回的"函数"、传给transform
> Image brightenedImage = transform(image, brighten(1.2));
> ```

## 3.5 `函数`组合

> API把`函数式接口`作为参数类型，但向该API传入的`参数`是由多个`函数`（`函数式接口的实现对象`，`lambda表达式`）组合而成

例子：

> 以`函数式接口`作为参数的代码
>
> ```java
> public static Image transform(Image in, UnaryOperator<Color> f /*java自带的函数式接口*/) {
>     int width = (int) in.getWidth();
>     int height = (int) in.getHeight();
>     WritableImage out = new WritableImage(width, height);
>     for (int x = 0; x < width; x++) {
>         for (int y = 0; y < height; y++) {
>            // 调用函数式接口的apply方法
>            out.getPixelWriter().setColor(x, y, f.apply(in.getPixelReader().getColor(x, y)));
>         }
>     }
>     return out;
> }
> ```
>
> 将两个`函数`（函数式接口实现对象、lambda表达式）组合成一个的代码
>
> ```java
> public static <T> UnaryOperator<T> compose(
>         UnaryOperator<T> op1, UnaryOperator<T> op2) {
>     return t -> op2.apply(op1.apply(t));
> }
> ```
>
> 调用该`compose`方法的代码
>
> ```java
> Image image3 = transform(image, compose(Color::brighter, Color::grayscale));
> ```

完整代码：[../code/ch3/sec05/ImageDemoCh3Sec05.java](../code/ch3/sec05/ImageDemoCh3Sec05.java)

## 3.6 按需将一组`函数`组合在一起，延迟到执行点一起执行

> 相比两次调用`3.4`节的`transform`方法，`3.5`节的`函数组合`的一个好处是，可以避免中间对象（`Image`）的产生，从而节省内存。

相比`3.5节`，更进一步，按需将一组`函数`组合在一起，延迟到使用他们时一起执行

例子如下：

> 编写一个类，它提供的方法`transform`用来按需组合`函数`，方法`toImage`用来执行这些函数
>
> ```java
> class LatentImage {
>     private Image in;
>     // 存储加入的函数
>     private List<UnaryOperator<Color>> pendingOperations;
> 
>     public static LatentImage from(Image in) {
>         LatentImage result = new LatentImage();
>         result.in = in;
>         result.pendingOperations = new ArrayList<>();
>         return result;
>     }
> 
>     // 加入函数
>     LatentImage transform(UnaryOperator<Color> f) {
>         pendingOperations.add(f);
>         return this;
>     }
> 
>     public Image toImage() {
>         int width = (int) in.getWidth();
>         int height = (int) in.getHeight();
>         WritableImage out = new WritableImage(width, height);
>         for (int x = 0; x < width; x++) {
>             for (int y = 0; y < height; y++) {
>                 Color c = in.getPixelReader().getColor(x, y);
>                 // 依次调用先前加入的所有函数
>                 for (UnaryOperator<Color> f : pendingOperations) {
>                    c = f.apply(c);
>                 }
>                 out.getPixelWriter().setColor(x, y, c);
>             }
>         }
>         return out;
>     }
> }
> ```
>
> 使用这个类的代码
>
> ```java
> Image finalImage = LatentImage.from(image)
>          .transform(Color::brighter)  // 加入函数
>          .transform(Color::grayscale) // 加入函数
>          .toImage();                  // 一起执行这些函数
> ```

完整代码：[../code/ch3/sec06/ImageDemoCh3Sec06.java](../code/ch3/sec06/ImageDemoCh3Sec06.java)

## 3.7 让传入的`函数`并行执行

> 对于`传入的函数`，把他们放在线程池中并行执行

例子如下：

> 编写让`函数`并行执行的方法`parallelTransform`
>
> 它对一个像素矩阵进行分块，为每一块调用传入的`函数`，这些函数放入线程池中并行执行
>
> ```java
> public static Color[][] parallelTransform(Color[][] in, UnaryOperator<Color> f) {
>     int n = Runtime.getRuntime().availableProcessors();
>     int height = in.length;
>     int width = in[0].length;
>     Color[][] out = new Color[height][width];
>     try {
>         // 初始化线程池
>         ExecutorService pool = Executors.newCachedThreadPool();
>         for (int i = 0; i < n; i++) {
>             int fromY = i * height / n;
>             int toY   = (i + 1) * height / n;
>             // 线程池
>             pool.submit(() -> {
>                 System.out.printf("%s %d...%d\n", Thread.currentThread(), fromY, toY - 1);
>                 for (int x = 0; x < width; x++) {
>                     for (int y = fromY; y < toY; y++) {
>                         // 执行 UnaryOperator<Color> f
>                         out[y][x] = f.apply(in[y][x]);
>                     }
>                 }
>             });
>         }
>         // 停止新的线程提交，等待已有线程和运行完毕（超时时间1小时）
>         pool.shutdown();
>         pool.awaitTermination(1, TimeUnit.HOURS);
>     } catch (InterruptedException ex) {
>         ex.printStackTrace();
>     }
>     return out;
> }
> ```
>
> 使用这个方法的代码
>
> ```java
> // 让Collor::grayscaleb并行处理像素矩阵
> pixels = parallelTransform(pixels, Color::grayscale);
> ```

完整代码：[../code/ch3/sec07/ImageDemoCh3Sec07.java](../code/ch3/sec07/ImageDemoCh3Sec07.java)

## 3.8 处理lambda执行过程中抛出的异常

> 如果lambda表达式在执行时抛出异常，该如何处理

### 3.8.1 同步执行场景

> lambda表达式抛出异常，传给调用它的代码，被捕获或者一层层向上传递，与普通代码的异常处理相同
>
> 如果代码中有多个lambda表达式要执行，其中之一抛出异常，则后面的lambda表达式不会在执行

例子：[../code/ch3/sec08/ExceptionDemo2.java](../code/ch3/sec08/ExceptionDemo2.java)

### 3.8.2 异步执行场景

