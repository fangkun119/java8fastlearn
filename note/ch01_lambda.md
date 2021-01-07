# CH01 Lambda表达式

[TOC]

## 1.1 为什么要使用Lambda表达式

Lambda表达式是一段可以被传递的代码，例如

1. 由另一个线程运行的代码
2. 用于排序的自定义比较器
3. 由回调函数延迟执行的代码

如果不使用lambda，使用老式的方法，那么将需要不得不构建一个类，由类对象来封装所要传递的代码

例子：[../code/ch1/sec01/OldStyle.java](../code/ch1/sec01/OldStyle.java)

> 其的`LengthComparator`、`Worker`、`EventHandler<ActionEvent>的内部匿名子类`的代码都可以替换成lambda表达式

## 1.2 Lambda表达式的语法

> 上一小节1.1中的三个例子，可以用Lamdda进行如下替换
>
> 类型参数可以进行类型推导
>
> ```java
> // 以1.1中的`LengthComparator`为例，可以替换成如下的lambda表达式
> Comparator<String> comp = null;
> comp = (String first, String second) -> Integer.compare(first.length(), second.length());
> comp = (first, second) -> Integer.compare(first.length(), second.length());
> comp = (String first, String second) -> {
>             if (first.length() < second.length()) return -1;
>             else if (first.length() > second.length()) return 1;
>             else return 0;
>         };
> 
> // 以1.1中的Worker类为例，可以替换成形式如下的lambda表达式
> Runnable runner = () -> {
>     for (int i = 0; i < 1000; i++) doWork();
> };
> 
> // 以1.1中的`EventHandler<ActionEvent>的内部匿名子类`为例，可以替换成如下lambda表达式
> EventHandler<ActionEvent> listener = null;
> listener = e -> System.out.println(e.getTarget());
> listener = (e) -> System.out.println(e.getTarget());
> listener = (ActionEvent e) -> System.out.println(e.getTarget());
> ```

完整代码：[../code/ch1/sec02/Lambdas.java](../code/ch1/sec02/Lambdas.java)

## 1.3 函数式接口

很多API的函数参数是一个`只有一个抽象方法`的`Interface`。那么在使用这些API的时候，可以直接传入一个`lambda表达式`。java会用lambda表达式生成Interface的实现类，并创建对象传入

例如：`java.util.Arrays`的`sort`方法的参数只有一个方法`compare`

> ~~~java
> @FunctionalInterface
> public interface Comparator<T> {
>     int compare(T o1, T o2);
> }
> ~~~
>
> ```java
> public class Arrays {
> 	public static <T> void sort(T[] a, Comparator<? super T> c) {...}
> }
> ```

使用时可以直接传入lambda表达式

> ```java
> Arrays.sort(strings, 
>    (first, second) -> Integer.compare(first.length(), second.length()));
> ```

更多的例子：

> ```java
> Button button = new Button("Click me!");
> button.setOnAction(
>    event -> System.out.println("Thanks for clicking!"));
> ```
>
> ```java
> Runnable sleeper2 = () -> { 
>    System.out.println("Zzz"); 
>    try {
>       Thread.sleep(1000);
>    } catch (InterruptedException ex) {
>       Thread.currentThread().interrupt();
>    }
> };
> Callable<Void> sleeper3 = () -> { System.out.println("Zzz"); Thread.sleep(1000); return null; };
> ```

使用时要注意：

1. 参数类型、返回值、抛出异常的类型要能够匹配
2. `lambda`表达式会生成类对象，会占用内存空间，代码编写不当同样会照成内存浪费或溢出
3. 自己写接口时，最好加上`@FunctionalInterface`注解（生成`javadoc`文档是会标注出这是一个函数式接口，同时编译器也会检查它是否只包含一个方法）

完整代码：[../code/ch1/sec03/FunctionalInterfaces.java](../code/ch1/sec03/FunctionalInterfaces.java)

## 1.4 方法引用

把一个已有的方法，传给某个API：相当于一个lambda表达式的简写

### 1.4.1 三种形式

#### (1) `类::静态方法`

> ```java
> Arrays.sort(strings, MethodReferences::cmp);
> ```
>
> 等价于
>
> ```java
> Arrays.sort(strings, (x,y) -> MethodReferences.cmp(x, y)); 
> ```
>
> 他们都为下面的接口生成了实现类以及对象
>
> ```java
> @FunctionalInterface
> public interface Comparator<T> {
>     int compare(T o1, T o2);
> }
> ```

#### (2) `对象::静态方法`

> ```java
> button1.setOnAction(System.out::println); // str -> System.out.println(str)
> ```

####  (3) `类::实例方法`

> 会把第一个参数当做执行方法的对象
>
> ```java
> Arrays.sort(strings, String::compareToIgnoreCase); // (x,y) -> x.compareToIgnoreCase(y)
> ```

完整代码：[../code/ch1/sec04/MethodReferences.java](../code/ch1/sec04/MethodReferences.java)

### 1.4.2 通过`this`和`super`进行实例方法引用

#### (1) `this::实例方法`

> ```java
> Thread t = new Thread(super::greet); // x -> super.great(x)
> t.start();
> ```

#### (2) `super::实例方法`

> ```java
> Thread t = new Thread(super::greet); // x -> super.great(x)
> t.start();
> ```

完整代码：[../code/ch1/sec04/SuperTest.java](../code/ch1/sec04/SuperTest.java)

## 1.5 构造器引用

可以使用`构造器引用`来创建对象，形式如下：

### 1.5.1 `Class::new`

> ```java
> List<String> labels = Arrays.asList("Ok", "Cancel", "Yes", "No", "Maybe");
> Stream<Button> stream = labels.stream().map(Button::new); //等价于x -> new Button
> ```

### 1.5.2 Class[]::new

> ```java
> Button[] buttons = stream.toArray(Button[]::new);
> ```
>
> 等价于
>
> ```java
> Button[] buttons = stream.toArray(n -> new Button[n]);
> ```

### 1.5.3 附录

#### 附录1：解决泛型擦除带来的类型丢失问题

> ```java
> Stream<Button> stream = Arrays.asList("A", "B", "C").stream().map(Button::new);
> ```
>
> 被调用时，因为泛型擦除，存入Stream的Button对象类型被擦除为Object，而调用下面普通版本的toArray只能得到Object[]
>
> ```java
> Object[] buttons = stream.toArray();
> // Button[] buttons = (Button[]) stream.toArray(); //强转成Button[]会抛ClassCastException
> ```
>
> 使用`Class[]::new`则可以提供额外的信息，帮助Stream返回Button[]类型，解决泛型擦除的问题
>
> ~~~java
> Button[] buttons = stream.toArray(Button[]::new);
> ~~~

#### 附录2：理解`Stream<Button> stream = labels.stream().map(Button::new); //等价于x -> new Button`

> 以下是`Stream<T>.toArray`方法对参数的要求
>
> * 它需要一个`IntFunction<R>`对象，这个对象的`apply`方法根据传入的`int n`返回一个类型为R的对象，在这个例子里`R`就是`Button[]`
> * 随后`toArray`方法会自己向Button[]`中填充元素
>
> ```java
> public interface Stream<T> extends BaseStream<T, Stream<T>> {
> 	...
> 	<A> A[] toArray(IntFunction<A[]> generator);
> 	...
> }
> ```
>
> ```java
> package java.util.function;
> @FunctionalInterface
> public interface IntFunction<R> {
>     R apply(int value);
> }
> ```

完整代码：[../code/ch1/sec05/ConstructorReferences.java](../code/ch1/sec05/ConstructorReferences.java)

## 1.6 变量访问权限

除了传给它的参数以外，Lambda表达式能访问那些变量，有哪些权限

(1) 访问范围：与`lambda表达式所在位置`的普通代码相同、也有着相同的命名屏蔽规则

> * 局部变量
> * 对象的成员变量
> * 类的静态变量

(2) 权限

> * 局部变量："一定程度"的只读
>     * Lambda以捕获值（类似方法的参数传递）的方式来访问这些变量，为了防止不一致问题发生，在语法上增加了强制“只读”约束
>     * `原生类型`（例如`int`）：完全只读
>     * `对象`：不能修改对象的引用，但是可以修改对象内的数据，并且线程不安全（彻底的只读需要用`stream`来实现）
>
> * 类成员变量、静态变量：无约束、线程不安全

(3) Lambda表达式中的`this`关键字

> * 会引用创建该表达式的方法的`this`参数，而不是执行该表达式的方法的`this`参数

例子：

> * [../code/ch1/sec06/ThisInLambda.java](../code/ch1/sec06/ThisInLambda.java)
> * [../code/ch1/sec06/VariableScope.java](../code/ch1/sec06/VariableScope.java)

## 1.7 接口的默认方法

### 1.7.1 接口的默认方法（`default`）

(1) `默认接口方法`

> 下面用`default`修饰的方法`getName()`就是接口`Person`的默认方法
>
> ```java
> interface Person {
>    long getId();
>    // 接口默认函数getName
>    default String getName() { return "Person " + getId(); }
> }
> ```

(2) 用途：默认方法与普通的方法声明不同，它可以在接口声明的时候提供方法实现

(3) 历史由来：

这是java 8为了让集合之类支持函数表达式而设计的

> 例如下面的代码、需要为所有的集合类都添加forEach方法，而这显然不可行
>
> ~~~java
> list.forEach(System.out::println);
> ~~~
>
> 为了解决这类问题，java 8增加了`接口默认方法`。以上面的代码为例，java 8为`Iterable`接口增加了`forEach`默认方法，一劳永逸地解决了这个问题
>
> ```java
> package java.lang;
> 
> import ...
> 
> public interface Iterable<T> {
>     Iterator<T> iterator();
> 
>     default void forEach(Consumer<? super T> action) {
>         Objects.requireNonNull(action);
>         for (T t : this) {
>             action.accept(t);
>         }
>     }
> 
>     default Spliterator<T> spliterator() {
>         return Spliterators.spliteratorUnknownSize(iterator(), 0);
>     }
> }
> ```

### 1.7.2 命名冲突

#### `父类、接口`冲突：使用父类

> 父类、接口都提供了同一个方法（方法名、参数相同）的实现代码
>
> 使用父类的版本，而接口提供的版本被忽略

#### `接口、接口`冲突：必须手动指定

> 接口A、B提供同一个方法（方法名、参数相同），其中一个是`默认方法`
>
> 那么不论另一个是不是默认方法，出于安全考虑，实现类必须手动指定使用哪个方法
>
> ```java
> class Student implements Person, Persistent {
>    // 接口Person, Persistent都声明了`String getName()`方法，且其中一个是默认方法
>    public String getName() {
>       // 必须手动指定使用哪个版本，
>    	  // 例如下面代码，使用接口Person的版本
>       return Person.super.getName();
>    }
>    ...
> }
> ```

代码：[../code/ch1/sec07/DefaultMethods.java](../code/ch1/sec07/DefaultMethods.java)

## 1.8 接口静态方法

Java 8容许为接口添加静态方法

> 例如下面`java.util.Comparator<>`接口中提供的一组方法
>
> ![](https://raw.githubusercontent.com/kenfang119/pics/main/java8fastlearn/java8_interface_static_method.jpg)
>
> ```java
> package java.util;
> 
> @FunctionalInterface
> public interface Comparator<T> {
>     ...
> 
>     public static <T> Comparator<T> comparingInt(ToIntFunction<? super T> keyExtractor) {
>         Objects.requireNonNull(keyExtractor);
>         return (Comparator<T> & Serializable)
>             (c1, c2) -> Integer.compare(keyExtractor.applyAsInt(c1), keyExtractor.applyAsInt(c2));
>     }
>     
>     ...
> }
> ```

这样的好处是，

(1) 之前（`接口`、`辅助类`）编写模式就不再需要了，可以将辅助类中的静态方法直接写在接口中

> 例如（`Path`、`Paths`）、（`Collection`、`Collections`）这样的设计

(2) 与lambda表达式结合使用更加方便

> 例如：`java.util.Comparator`提供了接口静态方法`comparing`
>
> ```java
> public static <T, U extends Comparable<? super U>> Comparator<T> comparing(
>         Function<? super T, ? extends U> keyExtractor)
> {
>     Objects.requireNonNull(keyExtractor);
>     return (Comparator<T> & Serializable)
>         (c1, c2) -> keyExtractor.apply(c1).compareTo(keyExtractor.apply(c2));
> }
> ```
>
> ```java
> @FunctionalInterface
> public interface Function<T, R> {
>     R apply(T t);
>     ...
> }
> ```
>
> 可以直接使用下面的代码来生成比较器（用lambda表达式生成接口Function<T,R>的对象）
>
> ~~~java
> Comparator.comparing(String::length)
> ~~~
>
> 比下面的写法更加简洁（用lambda表达式生成接口Comparator<T>的对象）
>
> ~~~java
> (first, second) -> Integer.compare(first.length(), second.length())
> ~~~
>
> 具体原理结合1.3函数式接口来理解

例子：[../code/ch1/sec08/StaticInterfaceMethods.java](../code/ch1/sec08/StaticInterfaceMethods.java)

