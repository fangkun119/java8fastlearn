# CH09 Java 7特性

[TOC]

> 除了`字符串switch语句`、`二进制数字表示`、`下划线数字表示`、`改进的类型推断等`，还包括如下非常有用的改进

## 9.1 异常处理改进

### 9.1.1 使用`try-with-resources`自动释放资源

该语句可以自动释放资源，并且可以指定多个资源，例如

> ```java
> try {
>     // Scanner类实现了AutoClosable接口
>     // 当抛出异常，或者try块内的代码运行完毕后，都会调用scanner.close()方法来释放资源
>     try (Scanner scanner = new Scanner(Paths.get("/usr/share/dict/words"))) {
>         int count = 0;
>         while (scanner.hasNext() && ++count < 4) {
>            System.out.println(LOG_PREFIX + scanner.next().toLowerCase());
>         }
>         // a
>         // a
>         // aa
>     }
> 
>     // 可以指定多个资源，如下面的in和out
>     try (
>             Scanner in = new Scanner(Paths.get("not_existed.txt"));
>             PrintWriter out = new PrintWriter("/tmp/out.txt")
>     ) {
>        while (in.hasNext()) {
>           out.println(in.next().toLowerCase());
>        }
>     }
> } catch (IOException ex) { // Separate try-with-resources from try/catch
>     System.out.println(LOG_PREFIX + "exception from try-with-resources clause: " + ex);
>     // exception from try-with-resources clause: java.nio.file.NoSuchFileException: not_existed.txt
> }
> ```

### 9.1.2 应对`“处理异常时抛出的新异常”`场景

问题：在下面场景中，应当捕捉异常A？还是捕捉异常B呢？

> (1) 处理业务逻辑时抛出异常A，导致`try-with-resources`要关闭资源
>
> (2) 在关闭资源时，又抛出新的异常B

`try-with-resources`的处理方法：

> 会重新抛出异常A，而异常B则由`try-with-resource`类库来捕获并将其标记为”suppressed“，例如下面的例子1

但是要注意这种机制、只有在异常没有被破坏的前提下才可用

> 例如Scanner，会拦截并捕获两个异常，然后重新抛出一个新异常。例如下面的例子2中，只能捕获Scanner抛出的新异常。例子可以参考下面的”完整代码“

完整代码：[../code/ch9/sec01/TryWithResources.java](../code/ch9/sec01/TryWithResources.java)

例子1

> ```java
> try {
>     // 测试场景构造
>     // 构造一个内部类，传给Scanner，来让Scanner处理业务逻辑，以及关闭时都会抛出异常
>     try (InputStream in = new InputStream() {
>         public int read() throws IOException {
>             throw new IOException("read failed");
>         }
>         public void close() throws IOException {
>             throw new IOException("close failed");
>         }
>     }) {
>         System.out.println(in.read());
>     }
> } catch (Exception ex) {
>     System.out.println(LOG_PREFIX + "exception thrown by try-with-resources: " + ex);
>     // 输出
>     // exception thrown by try-with-resources: java.io.IOException: read failed
> 
>     Throwable[] secondaryExceptions = ex.getSuppressed();
>     System.out.println(LOG_PREFIX + "exception suppressed: " + Arrays.toString(secondaryExceptions));
>     // 输出
>     // exception suppressed: [java.io.IOException: close failed]
> }
> ```

### 9.1.3 捕获多个异常：`catch(ExceptionA|ExceptionB e)`

> 可以编写如下代码，让多个异常共享同一个catch分治，来避免代码冗余
>
> ~~~java
> try {
>     ...
> } catch (FileNotFoundException | UnknownHostException ex) {
>     ...
> }
> ~~~

### 9.1.4 简化处理反射方法时的异常：`catch(ReflectiveOperationException e)`

Java 6的时候，调用反射方法要处理多种异常

> 例如`ClassNotFoundException`、`NoSuchMethodException`、`IllegalAccessException`、`InvocationTargetException`

Java 7引入了一个新的父类异常`ReflectiveOperationException`，只需捕捉这一个就可以

> `catch (ReflectiveOperationException e) {...}`

## 9.2 使用文件

