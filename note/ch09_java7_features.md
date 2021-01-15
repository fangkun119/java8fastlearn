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

### 9.2.1 取代`File`类的`Path`接口

> Path：(1)可能是含有多个目录名称的序列；(2)也可能带着一个文件名

#### (1) 绝对路径：`Paths.get()`

> ```java
> // [1] 绝对路径，Paths.get()
> // 对于无效路径会抛出InvalidPathException异常
> Path absolute = Paths.get("/", "home", "cay");
> System.out.println(LOG_PREFIX + absolute);
> // 输出：/home/cay
> 
> Path relative = Paths.get("myprog", "conf", "user.properties");
> System.out.println(LOG_PREFIX + relative);
> // 输出：myprog/conf/user.properties
> 
> Path homeDirectory = Paths.get("/home/cay");
> System.out.println(LOG_PREFIX + homeDirectory);
> // 输出：/home/cay
> ```

#### (2) 路径解析：`p.resolve(q)`，`p.resolveSibling(q)`，`p.relativize(q)`，`p.normalize()`

> ```java
> // p.resolve(q)，规则如下：
> // * 如果q是绝对路径，返回${q}
> // * 如果q是相对路劲，返回${p}/${q}
> Path configPath = homeDirectory.resolve("myprog/conf/user.properties");
> System.out.println(LOG_PREFIX + configPath);
> // 输出：/home/cay/myprog/conf/user.properties
> 
> // p.resolveSibling(q)，针对p的父目录进行解析
> Path workPath = Paths.get("/home/cay/myprog/work");
> Path tempPath = workPath.resolveSibling("temp");
> System.out.println(LOG_PREFIX + tempPath);
> // 输出：/home/cay/myprog/temp
> 
> // p.relativize(q)：从路径p到路径q的相对路径
> Path relPath = Paths.get("/home/cay").relativize(Paths.get("/home/fred/myprog"));
> System.out.println(LOG_PREFIX + relPath);
> // 输出：../fred/myprog
> 
> // p.normalize()：去掉冗余成分之后的路径
> Path normalizedPath = Paths.get("/home/cay/../fred/./myprog").normalize();
> System.out.println(normalizedPath);
> // 输出：/home/fred/myprog
> ```

#### (3) 路径拆分：`p.getParent()`，`p.getFileName()`，`p.getRoot()`

> ```java
> // p.getParent()：
> // 如果p是目录，返回的p的上一级目录
> // 如果p是文件，返回的p所在的目录
> Path parent1 = Paths.get("/home/cay/myfile.txt").getParent();
> System.out.println(LOG_PREFIX + parent1);
> // 输出：/home/cay
> Path parent2 = Paths.get("/home/cay/mydir/").getParent();
> System.out.println(LOG_PREFIX + parent2);
> // 输出：/home/cay
> 
> // p.getFileName()：
> // 如果p是文件，返回文件名
> // 如果p是目录，返回最后一级目录的目录名
> Path file1 =  Paths.get("/home/cay/myfile.txt").getFileName(); // the path myprog.properties
> System.out.println(LOG_PREFIX + file1);
> // 输出：myfile.txt
> Path file2 =  Paths.get("/home/cay/mydir/").getFileName(); // the path myprog.properties
> System.out.println(LOG_PREFIX + file2);
> // 输出：myDir
> 
> // p.getRoot()
> // 如果是绝对路径，返回该路径的根目录
> // 如果是相对路径，返回null
> Path root1 = Paths.get("/home/cay/myfile.txt").getRoot(); // the path /
> System.out.println(LOG_PREFIX + root1);
> // 输出：/
> Path root2 = Paths.get("/home/cay/myfile.txt").getFileName().getRoot();
> System.out.println(LOG_PREFIX + root2);
> // 输出：null （单独的文件名部分没有root）
> Path root3 = Paths.get("./code/next/").getRoot();
> System.out.println(LOG_PREFIX + root3);
> // 输出：null （相对路径没有root）
> ```

代码位置：[../code/ch9/sec02/PathDemo.java](../code/ch9/sec02/PathDemo.java)

### 9.2.2 文件操作

#### (1) 读取和写入小文件

> 对于小文件，可以将文件内容当做字符串或字符串列表来处理
>
> ```java
> // 读取全部内容：Files.readAllBytes(Path)
> byte[] bytes = Files.readAllBytes(filePath("FilesDemo.java"));
> // 将byte[]转换为String（可指定编码）：new String(bytes, Charset)
> String content = new String(bytes, StandardCharsets.UTF_8);
> System.out.println(NEW_LOG_PREFIX + content.substring(0, 100) + "...");
> // 输出
> // import java.io.ByteArrayOutputStream;
> // import java.io.IOException;
> // import java.net.URL;
> // import java.n...
> 
> // 按行读取内容
> List<String> lines = Files.readAllLines(filePath("FilesDemo.java"));
> System.out.println(NEW_LOG_PREFIX + "Last line: " + lines.get(lines.size() - 1));
> // 输出
> // Last line: }
> 
> // 以二进制方式覆盖写入（可指定编码）：Files.write(Path, byte[])
> // 按行覆盖写入：Files.write(Path, List<String>)
> content = content.replaceAll("e", "x");
> Files.write(filePath("FilesDemo1.out"), content.getBytes(StandardCharsets.UTF_8));
> Files.write(filePath("FilesDemo1.out"), lines);
> 
> // 以追加的方式写入：File.write(Path, List<String>, StandardOpenOption.APPEND)
> Collections.reverse(lines);
> Files.write(filePath("FilesDemo2.out"), lines, StandardOpenOption.APPEND);
> 
> // 如果存在则删除文件：Files.deleteIfExists(Path)
> boolean deleted = Files.deleteIfExists(filePath("FilesDemo3.out"));
> System.out.println(NEW_LOG_PREFIX + deleted);
> ```

#### (2) 读取和写入大文件

> 对于大文件，或二进制文件，可以考虑使用`InputStream`/`OutputStream`或者`Reader`/`Writer`
> *   `InputStream in   = Files.newInputStream(path);`
> *   `OutputStream out = Files.newOutputStream(path);`
> *   `Reader reader    = Files.newBufferedReader(path);`
> *   `Writer writer    = Files.newBufferedWriter(path);`
>
> ```java
> // 从InputStream拷贝到Path：Files.copy(InputStream, Path)
> URL url = new URL("http://horstmann.com");
> Files.copy(url.openStream(), filePath("FilesDemo3.out"));
> 
> // 从Path拷贝到OutputStream：Files.copy(Path, OutputStream)
> ByteArrayOutputStream out = new ByteArrayOutputStream();
> Files.copy(filePath("FilesDemo3.out"), out);
> System.out.println(NEW_LOG_PREFIX + out.toString("UTF-8").substring(0, 100) + "...");
> // <!DOCTYPE HTML PUBLIC "-//IETF//DTD HTML 2.0//EN">
> // <html><head>
> // <title>302 Found</title>
> // </head><bod...
> ```

#### (3) 创建文件和目录

> ```java
> // 创建新的目录: 
> // 上一级目录必须存在，目标目录已经存在时会抛异常： Files.createDirectory(Path)
> // 将缺失的上一级目录一起创建，目标目录已经存在时会抛异常： File.createDirectories(Path)
> Path tmpDir = Files.createDirectory(Paths.get("tmp"));
> System.out.println(NEW_LOG_PREFIX + tmpDir.toAbsolutePath().toString());
> // /Users/fangkun/Dev/git/java8note/tmp
> 
> // 创建空文件：Files.createFile(Path)
> // 文件已经存在时会抛异常
> Path tmpFile = Files.createFile(Paths.get("tmp.txt"));
> System.out.println(NEW_LOG_PREFIX + tmpFile.toAbsolutePath().toString());
> // /Users/fangkun/Dev/git/java8note/tmp.txt
> 
> // 检查文件/目录是否存在: Files.exists(Path)
> System.out.println(NEW_LOG_PREFIX + Files.exists(tmpDir));
> System.out.println(NEW_LOG_PREFIX + Files.exists(tmpFile));
> // true
> // true
> 
> // 删除文件或目录: Files.deleteIfExists(Path)
> Files.deleteIfExists(tmpDir);
> Files.deleteIfExists(tmpFile);
> System.out.println(NEW_LOG_PREFIX + Files.exists(tmpDir));
> System.out.println(NEW_LOG_PREFIX + Files.exists(tmpFile));
> // false
> // false
> ```

#### (4) 创建临时文件和目录

> ```java
> // 创建临时文件
> // Files.createTempFile(dir, prefix, suffix);
> // Files.createTempFile(     prefix, suffix);
> // Files.createTempFile(dir, prefix        );
> // Files.createTempFile(     prefix        );
> 
> Path newPath = Files.createTempFile(null, ".txt");
> System.out.println(NEW_LOG_PREFIX + newPath);
> // /var/folders/nb/n2wl0lms2g57q00_t5qd2nsc0000gn/T/14595181751598492480.txt
> 
> // 创建临时目录
> // Files.createTempDirectory(dir, prefix, suffix);
> // Files.createTempDirectory(     prefix, suffix);
> // Files.createTempDirectory(dir, prefix        );
> // Files.createTempDirectory(     prefix        );
> 
> newPath = Files.createTempDirectory("fred");
> System.out.println(NEW_LOG_PREFIX + newPath);
> // /var/folders/nb/n2wl0lms2g57q00_t5qd2nsc0000gn/T/fred16143100884822648692
> ```

#### (5) 复制、移动、和删除文件

> ```java
> // 从Path拷贝到Path: Files.copy(Path, Path)
> Files.copy(
>         filePath("FilesDemo3.out"), filePath("FilesDemo4.out"));
> 
> // 从Path移动到Path：Files.move(Path, Path)
> Files.move(
>         filePath("FilesDemo4.out"), filePath("FilesDemo5.out"));
> 
> // 拷贝设置（1）覆盖已存在文件StandardCopyOption.REPLACE_EXISTING (2) 拷贝文件属性StandardCopyOption.COPY_ATTRIBUTES
> Files.copy(
>         filePath("FilesDemo3.out"), filePath("FilesDemo5.out"),
>         StandardCopyOption.REPLACE_EXISTING,
>         StandardCopyOption.COPY_ATTRIBUTES);
> 
> // 原子移动文件、要么移动完成、要么源文件仍然存在：StandardCopyOption.ATOMIC_MOVE
> Files.move(
>         filePath("FilesDemo5.out"), filePath("FilesDemo6.out"),
>         StandardCopyOption.ATOMIC_MOVE);
> 
> // 删除文件或空目录：Files.delete(Path)
> Files.delete(filePath("FilesDemo6.out"));
> 
> // 仅在文件或空目录存在时删除：Files.deleteIfExists(Path)
> boolean isDeleted = Files.deleteIfExists(filePath("FilesDemo6.out"));
> System.out.println(NEW_LOG_PREFIX + isDeleted);
> 
> // 对于非空目录的删除，需要参考FileVisitor接口的API文档
> ```

## 9.3 实现`equals`、`hashCode`和`CompareTo`方法

### 9.3.1 安全的null值相等测试



### 9.3.2 计算哈希码



### 9.3.3 比较数值类型对象



## 9.4 安全需要



## 9.5 其他改动

### 9.5.1 字符串转换为数字



### 9.5.2 全局Logger



### 9.5.3 null检查



### 9.5.4 ProcessBuilder



### 9.5.5 URLClassLoader



### 9.5.6 BitSet



