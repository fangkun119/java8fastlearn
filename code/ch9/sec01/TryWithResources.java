import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class TryWithResources {
    private static final String LOG_PREFIX = "\t// ";
    public static void main(String[] args) {
        // 9.1.1 使用`try-with-resources`自动释放资源
        try {
            // Scanner类实现了AutoClosable接口
            // 当抛出异常，或者try块内的代码运行完毕后，都会调用scanner.close()方法来释放资源
            try (Scanner scanner = new Scanner(Paths.get("/usr/share/dict/words"))) {
                int count = 0;
                while (scanner.hasNext() && ++count < 4) {
                   System.out.println(LOG_PREFIX + scanner.next().toLowerCase());
                }
                // a
                // a
                // aa
            }

            // 可以指定多个资源，如下面的in和out
            try (
                  Scanner in = new Scanner(Paths.get("not_existed.txt"));
                  PrintWriter out = new PrintWriter("/tmp/out.txt")
            ) {
               while (in.hasNext()) {
                  out.println(in.next().toLowerCase());
               }
            }
        } catch (IOException ex) { // Separate try-with-resources from try/catch
            System.out.println(LOG_PREFIX + "exception from try-with-resources clause: " + ex);
            // exception from try-with-resources clause: java.nio.file.NoSuchFileException: not_existed.txt
        }

        // 9.1.2 应对`“处理异常时抛出的新异常”`场景
        // (1) 异常没有被破坏的场景
        // 处理业务逻辑时的异常（read failed）会被`try-with-resource`重新抛出
        // 关闭资源时的异常（close failed）则由`try-with-resource`类库捕获并标记为”suppressed“
        System.out.println("");
        try {
            // 测试场景构造
            // 构造一个内部类，传给Scanner，来让Scanner处理业务逻辑，以及关闭时都会抛出异常
            try (InputStream in = new InputStream() {
                public int read() throws IOException {
                    throw new IOException("read failed");
                }
                public void close() throws IOException {
                    throw new IOException("close failed");
                }
            }) {
                System.out.println(in.read());
            }
        } catch (Exception ex) {
            System.out.println(LOG_PREFIX + "exception thrown by try-with-resources: " + ex);
            // 输出
            // exception thrown by try-with-resources: java.io.IOException: read failed

            Throwable[] secondaryExceptions = ex.getSuppressed();
            System.out.println(LOG_PREFIX + "exception suppressed: " + Arrays.toString(secondaryExceptions));
            // 输出
            // exception suppressed: [java.io.IOException: close failed]
        }

        // (2) 异常被破坏的场景
        // 例如Scanner，会拦截并捕获两个异常，然后重新抛出一个新异常。例如下面的例子2中，只能捕获Scanner抛出的新异常
        System.out.println("");
        try {
            // This doesn't since, depressingly, the Scanner, changes the
            // exception type without also carrying along the suppressed
            // exceptions
            try (Scanner in = new Scanner(new InputStream() {
                // 测试场景构造
                // 构造一个内部类，传给Scanner，来让Scanner处理业务逻辑，以及关闭时都会抛出异常
                public int read() throws IOException {
                    System.out.println(LOG_PREFIX + "reading");
                    // 输出：reading
                    throw new IOException("read failed");
                }
                public void close() throws IOException {
                    System.out.println(LOG_PREFIX + "closing");
                    // 输出：closing
                    throw new IOException("close failed");
                }
            })) {
                System.out.println(LOG_PREFIX + in.next());
            }
        } catch (Exception ex) {
            // Scanner拦截并捕获了上面的两个IOException，并重新抛出一个NoSuchElementException
            // 因此这里只能捕获到Scanner重新抛出的NoSuchElementException

            System.out.println(LOG_PREFIX + "exception thrown by try-with-resources: " + ex);
            // 输出
            // exception thrown by try-with-resources: java.util.NoSuchElementException

            Throwable[] secondaryExceptions = ex.getSuppressed();
            System.out.println(LOG_PREFIX + "exception suppressed: " + Arrays.toString(secondaryExceptions));
            // 输出
            // exception suppressed: []
        }

        // As you can see, the read and close method got called, and both
        // threw exceptions, but both the read and the close exception
        // are still lost. The scanner takes it upon itself to catch
        // exceptions from the underlying stream without linking them,
        // and then it throws a different exception
    }
}
