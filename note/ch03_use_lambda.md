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
>     T get();
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
>     void accept(int value);
> 
>     default IntConsumer andThen(IntConsumer after) {
>         Objects.requireNonNull(after);
>         return (int t) -> { accept(t); after.accept(t); };
>     }
> }
> ```

#### (2) `Runnable`

> ```java
> package java.lang;
> 
> @FunctionalInterface
> public interface Runnable {
>     public abstract void run();
> }
> ```

## 3.3 选择函数式接口（Functional Interface）

### 3.3.1 Java 8提供的函数式接口

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

### 3.3.2 生成组合函数

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
>                 ? Objects::isNull
>                 : object -> targetRef.equals(object);
>     }
> }
> ```
>
> 比如如下的代码
>
> ~~~java
> Predicate.isEqual(a).or(Predicate.isEqual(b))
> ~~~

### 3.3.3 使用函数式接口的好处

> 例如，项目的运行步骤中需要`文件检测环节`，即执行某些匹配规则，来确定文件是否与其相符合。
>
> 接口是`Predicate<File>`好呢？还是`File`好呢？
>
> 显然是`Predicate<File>`更好，这样将更加灵活和简洁，除非项目中已经大量依赖`File`接口不方便更改

### 3.3.4 实例：编写并使用自定义的函数式接口

> 完整代码：[../code/ch3/sec03/ImageDemo.java](../code/ch3/sec03/ImageDemo.java)
>
> 编写`函数式接口`
>
> ```java
> @FunctionalInterface
> interface ColorTransformer {
>    Color apply(int x, int y, Color colorAtXY);
> }
> ```
>
> 调用`函数式接口`的代码
>
> ```java
> public static Image transform(Image in, ColorTransformer f /*自定义函数式接口*/ ) {
>    int width = (int) in.getWidth();
>    int height = (int) in.getHeight();
>    WritableImage out = new WritableImage(width, height);
>    for (int x = 0; x < width; x++) {
>       for (int y = 0; y < height; y++) {
>          // 调用函数式接口的apply方法
>          out.getPixelWriter().setColor(x, y, f.apply(x, y, in.getPixelReader().getColor(x, y)));
>       }
>    }
>    return out;
> }
> ```
>
> 提供符合`函数式接口`的lambda表达式的代码
>
> ```java
> // 传入符合函数式接口 ColorTransformer 的 lambda表达式
> Image image2 = transform(image, (x, y, c) -> (x / 10) % 2 == (y / 10) % 2 ? Color.GRAY : c);
> ```

## 3.4 把函数用作返回值

经常会遇到需要把函数用作返回值的场景，例如返回一个Comparator交给Arrays.sort等

关于如何把函数用作返回值，例子如下

> 完整代码：[../code/ch3/sec04/ImageDemoCh3Sec04.java](../code/ch3/sec04/ImageDemoCh3Sec04.java)
>
> 调用`函数式接口`的代码
>
> ```java
> public static Image transform(Image in, UnaryOperator<Color> f /*java自带的函数式接口*/) {
>    int width = (int) in.getWidth();
>    int height = (int) in.getHeight();
>    WritableImage out = new WritableImage(width, height);
>    for (int x = 0; x < width; x++) {
>       for (int y = 0; y < height; y++) {
>          // 调用函数式接口的apply方法
>          out.getPixelWriter().setColor(x, y, f.apply(in.getPixelReader().getColor(x, y)));
>       }
>    }
>    return out;
> }
> ```
>
> 把函数用作返回值
>
> ```java
> // 把函数用作返回值
> public static UnaryOperator<Color> brighten(double factor) {
>    // lambda表达式会编译成一个UnaryOperator<Color>对象返回
>    return c -> c.deriveColor(0, 1, factor, 1); 
> }
> ```
>
> 把上述两处代码连接起来
>
> ```java
> // 把brighten返回的"函数"、传给transform
> Image brightenedImage = transform(image, brighten(1.2));
> ```

