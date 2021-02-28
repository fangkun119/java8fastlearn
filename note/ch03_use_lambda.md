<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
<!--**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*-->

- [CH03 Lambda的应用](#ch03-lambda%E7%9A%84%E5%BA%94%E7%94%A8)
  - [3.1 延迟执行](#31-%E5%BB%B6%E8%BF%9F%E6%89%A7%E8%A1%8C)
    - [3.1.1 内容](#311-%E5%86%85%E5%AE%B9)
    - [3.2.2 API](#322-api)
      - [(1) `Supplier<T>`](#1-suppliert)
  - [3.3 理解lambda作为参数时的传参过程](#33-%E7%90%86%E8%A7%A3lambda%E4%BD%9C%E4%B8%BA%E5%8F%82%E6%95%B0%E6%97%B6%E7%9A%84%E4%BC%A0%E5%8F%82%E8%BF%87%E7%A8%8B)
    - [3.3.1 内容](#331-%E5%86%85%E5%AE%B9)
    - [3.3.2 API](#332-api)
      - [(1) `IntConsumer`](#1-intconsumer)
      - [(2) `Runnable`](#2-runnable)
  - [3.3 选择函数式接口（Functional Interface）](#33-%E9%80%89%E6%8B%A9%E5%87%BD%E6%95%B0%E5%BC%8F%E6%8E%A5%E5%8F%A3functional-interface)
    - [3.3.1 使用函数式接口的好处](#331-%E4%BD%BF%E7%94%A8%E5%87%BD%E6%95%B0%E5%BC%8F%E6%8E%A5%E5%8F%A3%E7%9A%84%E5%A5%BD%E5%A4%84)
    - [3.3.2 实例：编写并使用自定义的函数式接口](#332-%E5%AE%9E%E4%BE%8B%E7%BC%96%E5%86%99%E5%B9%B6%E4%BD%BF%E7%94%A8%E8%87%AA%E5%AE%9A%E4%B9%89%E7%9A%84%E5%87%BD%E6%95%B0%E5%BC%8F%E6%8E%A5%E5%8F%A3)
    - [3.3.3 生成组合函数](#333-%E7%94%9F%E6%88%90%E7%BB%84%E5%90%88%E5%87%BD%E6%95%B0)
    - [3.3.4 Java 8为普通类型提供的函数式接口](#334-java-8%E4%B8%BA%E6%99%AE%E9%80%9A%E7%B1%BB%E5%9E%8B%E6%8F%90%E4%BE%9B%E7%9A%84%E5%87%BD%E6%95%B0%E5%BC%8F%E6%8E%A5%E5%8F%A3)
    - [3.3.5 Java 8为原始类型提供的函数式接口](#335-java-8%E4%B8%BA%E5%8E%9F%E5%A7%8B%E7%B1%BB%E5%9E%8B%E6%8F%90%E4%BE%9B%E7%9A%84%E5%87%BD%E6%95%B0%E5%BC%8F%E6%8E%A5%E5%8F%A3)
  - [3.4 把`函数`用作返回值](#34-%E6%8A%8A%E5%87%BD%E6%95%B0%E7%94%A8%E4%BD%9C%E8%BF%94%E5%9B%9E%E5%80%BC)
  - [3.5 `函数`组合](#35-%E5%87%BD%E6%95%B0%E7%BB%84%E5%90%88)
  - [3.6 按需将一组`函数`组合在一起，延迟到执行点一起执行](#36-%E6%8C%89%E9%9C%80%E5%B0%86%E4%B8%80%E7%BB%84%E5%87%BD%E6%95%B0%E7%BB%84%E5%90%88%E5%9C%A8%E4%B8%80%E8%B5%B7%E5%BB%B6%E8%BF%9F%E5%88%B0%E6%89%A7%E8%A1%8C%E7%82%B9%E4%B8%80%E8%B5%B7%E6%89%A7%E8%A1%8C)
  - [3.7 让传入的`函数`并行执行](#37-%E8%AE%A9%E4%BC%A0%E5%85%A5%E7%9A%84%E5%87%BD%E6%95%B0%E5%B9%B6%E8%A1%8C%E6%89%A7%E8%A1%8C)
  - [3.8 处理lambda执行过程中抛出的异常](#38-%E5%A4%84%E7%90%86lambda%E6%89%A7%E8%A1%8C%E8%BF%87%E7%A8%8B%E4%B8%AD%E6%8A%9B%E5%87%BA%E7%9A%84%E5%BC%82%E5%B8%B8)
    - [3.8.1 同步执行场景](#381-%E5%90%8C%E6%AD%A5%E6%89%A7%E8%A1%8C%E5%9C%BA%E6%99%AF)
    - [3.8.2 异步执行场景](#382-%E5%BC%82%E6%AD%A5%E6%89%A7%E8%A1%8C%E5%9C%BA%E6%99%AF)
      - [(1) 跨线程传递异常处理代码](#1-%E8%B7%A8%E7%BA%BF%E7%A8%8B%E4%BC%A0%E9%80%92%E5%BC%82%E5%B8%B8%E5%A4%84%E7%90%86%E4%BB%A3%E7%A0%81)
        - [例子1：通过lambda传入异常处理函数](#%E4%BE%8B%E5%AD%901%E9%80%9A%E8%BF%87lambda%E4%BC%A0%E5%85%A5%E5%BC%82%E5%B8%B8%E5%A4%84%E7%90%86%E5%87%BD%E6%95%B0)
        - [例子2：通过lambda传入异常处理函数，并且两个任务函数间有数据依赖](#%E4%BE%8B%E5%AD%902%E9%80%9A%E8%BF%87lambda%E4%BC%A0%E5%85%A5%E5%BC%82%E5%B8%B8%E5%A4%84%E7%90%86%E5%87%BD%E6%95%B0%E5%B9%B6%E4%B8%94%E4%B8%A4%E4%B8%AA%E4%BB%BB%E5%8A%A1%E5%87%BD%E6%95%B0%E9%97%B4%E6%9C%89%E6%95%B0%E6%8D%AE%E4%BE%9D%E8%B5%96)
        - [例子3：任务处理函数既收集返回结果、又收集遇到的异常，并返回给主线程](#%E4%BE%8B%E5%AD%903%E4%BB%BB%E5%8A%A1%E5%A4%84%E7%90%86%E5%87%BD%E6%95%B0%E6%97%A2%E6%94%B6%E9%9B%86%E8%BF%94%E5%9B%9E%E7%BB%93%E6%9E%9C%E5%8F%88%E6%94%B6%E9%9B%86%E9%81%87%E5%88%B0%E7%9A%84%E5%BC%82%E5%B8%B8%E5%B9%B6%E8%BF%94%E5%9B%9E%E7%BB%99%E4%B8%BB%E7%BA%BF%E7%A8%8B)
      - [(2) 处理`函数式接口`的异常抛出声明与lambda表达式不一致的问题](#2-%E5%A4%84%E7%90%86%E5%87%BD%E6%95%B0%E5%BC%8F%E6%8E%A5%E5%8F%A3%E7%9A%84%E5%BC%82%E5%B8%B8%E6%8A%9B%E5%87%BA%E5%A3%B0%E6%98%8E%E4%B8%8Elambda%E8%A1%A8%E8%BE%BE%E5%BC%8F%E4%B8%8D%E4%B8%80%E8%87%B4%E7%9A%84%E9%97%AE%E9%A2%98)
  - [3.9 使用lambda表达式时用到的泛型问题](#39-%E4%BD%BF%E7%94%A8lambda%E8%A1%A8%E8%BE%BE%E5%BC%8F%E6%97%B6%E7%94%A8%E5%88%B0%E7%9A%84%E6%B3%9B%E5%9E%8B%E9%97%AE%E9%A2%98)
    - [3.9.1 解决泛型擦除问题](#391-%E8%A7%A3%E5%86%B3%E6%B3%9B%E5%9E%8B%E6%93%A6%E9%99%A4%E9%97%AE%E9%A2%98)
      - [(1) java 6的解决办法](#1-java-6%E7%9A%84%E8%A7%A3%E5%86%B3%E5%8A%9E%E6%B3%95)
      - [(2) 使用lambda的解决办法](#2-%E4%BD%BF%E7%94%A8lambda%E7%9A%84%E8%A7%A3%E5%86%B3%E5%8A%9E%E6%B3%95)
    - [3.9.2 协变、逆变问题](#392-%E5%8D%8F%E5%8F%98%E9%80%86%E5%8F%98%E9%97%AE%E9%A2%98)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# CH03 Lambda的应用

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
>     // 不传入String而是通过Supplier传入lambda表达式
> 	public static void info(Logger logger, Supplier<String> message) {
> 		if (logger.isLoggable(Level.INFO)) {
> 			logger.info(message.get());
> 		}
> 	}
> 
> 	public static void main(String[] args) {
> 		double x = 3;
> 		double y = 4;
> 		info(Logger.getGlobal(), () -> "x: " + x + ", y: " + y);
> 		// 信息: x: 3.0, y: 4.0
> 	}
> }
> ```
> 
>完整代码：[../code/ch3/sec01/Logging.java](../code/ch3/sec01/Logging.java)

### 3.1.2 API 

#### (1) `Supplier<T>`

> ```java
> package java.util.function;
> @FunctionalInterface
> public interface Supplier<T> {
>        T get();
> }
> ```

## 3.2 理解lambda作为参数时的传参过程

### 3.2.1 内容

例子：[../code/ch3/sec02/Parameters.java](../code/ch3/sec02/Parameters.java)

> 参数类型是`函数式接口`，编译器根据 `lambda表达式`的输入输出，生成能匹配上的`函数式接口`实现类的类对象，进行调用。更多信息可以参考《章节1.3-函数式接口》
>
> ```java
> public class Parameters {
>    	public static void main(String[] args) {
>    		// IntConsumer
>    		repeat(10, i  -> System.out.println("Countdown: " + (9 - i)));
>    		// Runnable
>    		repeat(10, () -> System.out.println("Hello, World!"));
>    	}
> 
>    	public static void repeat(int n, IntConsumer action) {
>    		for (int i = 0; i < n; i++) {
>    			action.accept(i);
>    		}
>    	}
> 
>    	public static void repeat(int n, Runnable action) {
>    		for (int i = 0; i < n; i++) {
>    			action.run();
>    		}
>    	}
> }
> ```

### 3.2.2 API

#### (1) `IntConsumer`

> ```java
> package java.util.function;
> import java.util.Objects;
> 
> @FunctionalInterface
> public interface IntConsumer {
>    	void accept(int value);
>     	default IntConsumer andThen(IntConsumer after) {
>    		Objects.requireNonNull(after);
>    		return (int t) -> { accept(t); after.accept(t); };
>    	}
>    }
> ```

#### (2) `Runnable`

> ```java
> package java.lang;
> 
> @FunctionalInterface
> public interface Runnable {
>    	public abstract void run();
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
> 	Color apply(int x, int y, Color colorAtXY);
> }
> ```
>
> 以`函数式接口`作为参数的代码
>
> ```java
> public static Image transform(Image in, ColorTransformer f /*自定义函数式接口*/ ) {
>    	int width = (int) in.getWidth();
>    	int height = (int) in.getHeight();
>    	WritableImage out = new WritableImage(width, height);
>    	for (int x = 0; x < width; x++) {
>    		for (int y = 0; y < height; y++) {
>    			// 调用函数式接口的apply方法
>    			out.getPixelWriter().setColor(x, y, f.apply(x, y, in.getPixelReader().getColor(x, y)));
>    		}
>    	}
>    	return out;
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
>    	boolean test(T t);
> 
>    	default Predicate<T> and(Predicate<? super T> other) {
>    		Objects.requireNonNull(other);
>    		return (t) -> test(t) && other.test(t);
>    	}
> 
>    	default Predicate<T> negate() {
>    		return (t) -> !test(t);
>    	}
> 
>    	default Predicate<T> or(Predicate<? super T> other) {
>    		Objects.requireNonNull(other);
>    		return (t) -> test(t) || other.test(t);
>    	}
> 
>    	static <T> Predicate<T> isEqual(Object targetRef) {
>    		return (null == targetRef)
>    			? Objects::isNull : object -> targetRef.equals(object);
>    	}
>    }
> ```
> 
>比如如下的代码
> 
>~~~java
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
>    	// lambda表达式会编译成一个UnaryOperator<Color>对象返回
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
>    	int width = (int) in.getWidth();
>    	int height = (int) in.getHeight();
>    	WritableImage out = new WritableImage(width, height);
>    	for (int x = 0; x < width; x++) {
>    		for (int y = 0; y < height; y++) {
>    			// 调用函数式接口的apply方法
>    			out.getPixelWriter().setColor(x, y, f.apply(in.getPixelReader().getColor(x, y)));
>    		}
>    	}
>    	return out;
> }
> ```
>
> 将两个`函数`（函数式接口实现对象、lambda表达式）组合成一个的代码
>
> ```java
> public static <T> UnaryOperator<T> compose(UnaryOperator<T> op1, UnaryOperator<T> op2) {
>    	return t -> op2.apply(op1.apply(t));
>    }
> ```
> 
>调用该`compose`方法的代码
> 
>```java
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
>    	private Image in;
>    	// 存储加入的函数
>    	private List<UnaryOperator<Color>> pendingOperations;
> 
>    	public static LatentImage from(Image in) {
>    		LatentImage result = new LatentImage();
>    		result.in = in;
>    		result.pendingOperations = new ArrayList<>();
>    		return result;
>    	}
> 
>    	// 加入函数
>    	LatentImage transform(UnaryOperator<Color> f) {
>    		pendingOperations.add(f);
>    		return this;
>    	}
> 
>    	public Image toImage() {
>    		int width = (int) in.getWidth();
>    		int height = (int) in.getHeight();
>    		WritableImage out = new WritableImage(width, height);
>    		for (int x = 0; x < width; x++) {
>    			for (int y = 0; y < height; y++) {
>    				Color c = in.getPixelReader().getColor(x, y);
>    				// 依次调用先前加入的所有函数
>    				for (UnaryOperator<Color> f : pendingOperations) {
>    					c = f.apply(c);
>    				}
>    				out.getPixelWriter().setColor(x, y, c);
>    			}
>    		}
>    		return out;
>    	}
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
>    	int n = Runtime.getRuntime().availableProcessors();
>    	int height = in.length;
>    	int width = in[0].length;
>    	Color[][] out = new Color[height][width];
>    	try {
>    		// 初始化线程池
>    		ExecutorService pool = Executors.newCachedThreadPool();
>    		for (int i = 0; i < n; i++) {
>    			int fromY = i * height / n;
>    			int toY   = (i + 1) * height / n;
>    			// 线程池
>    			pool.submit(() -> {
>    				System.out.printf("%s %d...%d\n", Thread.currentThread(), fromY, toY - 1);
>    				for (int x = 0; x < width; x++) {
>    					for (int y = fromY; y < toY; y++) {
>    						// 执行 UnaryOperator<Color> f
>    						out[y][x] = f.apply(in[y][x]);
>    					}
>    				}
>    			});
>    		}
>    		// 停止新的线程提交，等待已有线程和运行完毕（超时时间1小时）
>    		pool.shutdown();
>    		pool.awaitTermination(1, TimeUnit.HOURS);
>    	} catch (InterruptedException ex) {
>    		ex.printStackTrace();
>    	}
>    	return out;
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
>
> ~~~java
> public static void doInOrder(Runnable first, Runnable second) {
>    	first.run();
>    	second.run();
> }
> public static void main() {
>    	try {
>    		doInOrder(
>    			() -> System.out.println(args[0]),   // 会抛异常
>    			() -> System.out.println("won't be executed")
>    		);
>    	} catch (Exception e) {
>    		System.out.println("exception: " + e);
>    	}
>    	// 输出： exception: java.lang.ArrayIndexOutOfBoundsException: 0
> }
> ~~~

### 3.8.2 异步执行场景

#### (1) 跨线程传递异常处理代码

> 上面的代码，可以交给一个异步运行的API（形如下面的`doInOrderAsync`方法），但有个问题
>
> * `try`块的代码交给了`新创建的线程B`来异步运行
> * `catch`块的代码仍然留在了`主线程A`
> * 当异常抛出时，抛在了线程B（线程B发现异常后退出），而捕捉线程的代码留在线程A什么也捕捉不到

解决方法是：把处理线程的逻辑、也用lambda表达式传给API

下面解决方法的代码，完整版本见“[../code/ch3/sec08/ExceptionDemo.java](../code/ch3/sec08/ExceptionDemo.java)”

##### 例子1：通过lambda传入异常处理函数

> 异步API
>
> ```java
> public static void doInOrderAsync1(Runnable first, Runnable second, Consumer<Throwable> handler) {
>    	Thread t = new Thread() {
>    		public void run() {
>    			try {
>    				first.run();
>    				second.run();
>    			} catch (Throwable t) {
>    				handler.accept(t); // 调用异常处理函数
>    			}
>    		}
>    	};
>    	t.start();
>    }
> ```
> 
>使用异步API的代码
> 
>```java
> doInOrderAsync1(
> 	()  -> System.out.println(args[0]),          // 会抛异常
>    	()  -> System.out.println("won't be executed") ,
>    	(e) -> System.out.println("exception: " + e) // Exception Handler
>    );
> // 输出：exception: java.lang.ArrayIndexOutOfBoundsException: 0
> ```

##### 例子2：通过lambda传入异常处理函数，并且两个任务函数间有数据依赖

> 异步API
>
> ```java
> public static <T> void doInOrderAsync2(Supplier<T> first, Consumer<T> second, Consumer<Throwable> handler) {
>    	Thread t = new Thread() {
>    		public void run() {
>    			try {
>    				// first的输出作为second的输入
>    				T firstRet = first.get(); 
>    				second.accept(firstRet); 
>    			} catch (Throwable e) {
>    				handler.accept(e); // 调用异常处理函数
>    			}
>    		}
>    	};
>    	t.start();
>    }
> ```
> 
>使用异步API的代码
> 
>```java
> doInOrderAsync2(
> 	()  -> args[0],                              // 会抛异常
>    	(r) -> System.out.println(r) ,
>    	(e) -> System.out.println("exception: " + e) // Exception Handler
>    );
> // 输出：exception: java.lang.ArrayIndexOutOfBoundsException: 0
> ```

##### 例子3：任务处理函数既收集返回结果、又收集遇到的异常，并返回给主线程

> 异步API
>
> ```java
> public static <T> void doInOrderAsync3 (Supplier<T> first, BiConsumer<T, Throwable> second) {
>    	Thread t = new Thread() {
>    		public void run() {
>    			T firstRet = null;
>    			Throwable exception = null;
>    			try {
>    				firstRet = first.get();
>    			} catch (Throwable e) {
>    				exception = e;
>    			} finally {
>    				second.accept(firstRet, exception);
>    			}
>    		}
>    	};
>    	t.start();
>    }
> ```
> 
>使用异步API的代码
> 
>```java
> doInOrderAsync3(
>      () -> args[0],                           // 会抛异常
>         (result, exception) -> System.out.println(String.format("result %s; exception: %s", result, exception))
>    );
>    // 输出：result null; exception: java.lang.ArrayIndexOutOfBoundsException: 0
> ```

#### (2) 处理`函数式接口`的异常抛出声明与lambda表达式不一致的问题

问题：

> Java 8 提供的`Supplier<T>`，`Consumer<T>`等，抽象方法声明中都没有`throws Exception`
>
> 如果
>
> * API使用的`函数式接口`没有`throws Exception`的函数
> * 而传入的lambda表达式，如果抛出的异常是`checked exception`，作为参数的`函数式接口`必须有`throws Exception`方法声明
>
> 那么就造成了不一致，无法通过编译检查

解决方法1：考虑让API使用`带有throws Exception的函数式接口`（类库提供的、或自己写的），

> 例如`Supplier<T>`可以替换为`Callable<T>`
>
> ```java
> package java.util.function;
> 
> @FunctionalInterface
> public interface Supplier<T> {
>    	T get(); // 没有异常抛出声明
> }
> ```
>
> ~~~java
> package java.util.concurrent;
> 
> @FunctionalInterface
> public interface Callable<V> {
>    	V call() throws Exception;  // 有异常抛出声明
> }
> ~~~

解决方法2：更多情况下API不可以更改，解决方法是做一层封装、将`checked exception`转换为`unchecked exception`

> ```java
> // 将会抛出checked exception的Callable<T>
> // 转换为不会抛checked exception的Supplier<T>，它只会抛un-checked exception
> public static <T> Supplier<T> unchecked(Callable<T> f) {
>    	return () -> {
>    		try {
>    			// 调用传入的会抛checked Exception的函数
>    			return f.call();
>    		} catch (Exception e) {
>    			// 将checked exception转换成un-checked exception后抛出
>    			throw new RuntimeException(e);
>    		} catch (Throwable t) {
>    			throw t;
>    		}
>    	};
> }
> ```
>
> ```java
> // 不能兼容API的lambda表达式
> Callable<String> withCheckedException = () -> new String(Files.readAllBytes(
>         Paths.get("/etc/not_existed")), StandardCharsets.UTF_8);
> 
> // 用上面的unchecked方法将其转换成兼容API的lambda
> Consumer<Throwable> unCheckedExceptionHandler = e -> System.out.println(e);
> api(unchecked(withCheckedException), unCheckedExceptionHandler);
> // 输出：java.lang.RuntimeException: java.nio.file.NoSuchFileException: /etc/not_existed
> ```

完整代码：[../code/ch3/sec08/ExceptionDemo2.java](../code/ch3/sec08/ExceptionDemo2.java)

## 3.9 使用lambda表达式时用到的泛型问题

### 3.9.1 解决泛型擦除问题

> 泛型擦除问题：形如`Collection<T>.toArray()`之类的方法只能返回`Object[]`
>
> * 因为泛型擦除，形如`Collection<T>`的泛型类 ，存入容器类型为`T`的对象，其类型在运行期就丢失了，只能把它们当成`Object`类型。调用其API，也只能返回`Object`或者`Object[]`类型的数据 。
>     * 对于返回`Object`的情况，虽然运行期类型`T`被擦除了，但是编译期仍然知道该`Object`的真实类型，可以通过类型转换的方法将其转回`T`
>     * 对于返回`Object[]`的情况，Java不容许将它转型为`T[]`
> * `Parent[]`不容许转型为`Children1[]`，是因为`Parent[]`内除了可以存储`Children`对象、还有可能存储`Parent`对象或者`Children2`对象

#### (1) java 6的解决办法

> ` T[] Collection<T>.toArray(T[] a)`
>
> 传入`T[]`作为参数，在方法内部使用反射，得到类型`T`来创建`T[]`，例如
>
> ~~~java
> String[] strArray = strList.toArray(new String[0]);
> ~~~

#### (2) 使用lambda的解决办法

> `T[] Collection<T>.toArray(T[]::new)`
>
> 用lambda表达式传入数组的构造函数给toArray方法，例如
>
> ~~~java
> String[] strArray = strList.toArray(String[]::new);
> ~~~

### 3.9.2 协变、逆变问题

问题：编写一个泛型函数接口、其参数该使用`协变`还是`逆变`？

协变（covariant）

> 例如`void readFrom(List<? Extends Preson> list)`，这个方法会从`list`中`以Person为类型`读出出入，`list`的泛型参数必须是Person的子类，例如
>
> ~~~java
> readFrom(new ArrayList<Employee>());
> readFrom(new ArrayList<Student>());
> ~~~

逆变（contravariant）

> 例如`void writeTo(List<? Super Person> list)`，这个方法会向`list`中`以Person为类型`来写入数据，`list`的泛型参数必须是`Person`的基类、否则list装不下，例如
>
> ~~~java
> writeTo(new ArrayList<Person>);
> writeTo(new ArrayList<Object>);
> ~~~

函数接口使用协变逆变：参数总是指定为`逆变`、返回值指定为`协变`

> 参数使用了逆变`? super T`，返回值使用协变`? extends R`
>
> 参考Stream用到的泛型函数接口Function和Consumer
>
> ~~~java
> public interface Stream<T> extends BaseStream<T, Stream<T>> {
> 	// 返回值”? extends R”：后面的代码可以以类型R读取该返回值，返回的都是R或者它的子类
> 	// 参数“? super T”：对处理T的函数的要求，如果它能处理任何基类，那么它也能处理T
> 	<R> Stream<R> map(Function<? super T, ? extends R> mapper);
> 
> 	// 参数"? super T"：
> 	// * 对处理T的函数的要求，如果它能处理任何基类，那么它也能处理T
> 	// * 例如可以传Consumer<Object>，这个函数连Object都可以处理，那么它一定可以处理类型T
> 	void forEach(Consumer<? super T> action);
> 	...
> }
> ~~~
>
> 下面是一个实例
>
> (1) 未使用泛型通配符
>
> ~~~java
> public static <T> void doInOrderAsync(
> 	Supplier<T> first, Consumer<T> second, Consumer<Throwable> handler
> )
> ~~~
>
> (2) 改为使用泛型通配符
>
> ~~~java
> public static <T> void doInOrderAsync(
> 	Supplier<? extends T> first, 		// 输出使用协变，输出的对象当做类型T来给下游
> 	Consumer<? super T> second,  		// 输入使用逆变，这个函数要能够处理输入类型为T的对象
> 	Consumer<? super Throwable> handler // 输入使用逆变，这个函数要能处理类型为Throwable的异常
> ) {
>     ...
> }
> ~~~

不能使用泛型通配符的例外情况：输入输出类型相依赖、逆变协变相互抵消

> 例如
>
> ~~~java
> T reduce(T identity, BinaryOperator<T> accumulator)
> ~~~





