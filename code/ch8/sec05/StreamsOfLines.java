import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

public class StreamsOfLines {
    public static final String BASE_DIR = "./code/ch8/sec05/";

    public static void main(String[] args) throws IOException {
        // 8.5.1.(1) 读取文件行的流：`Files.lines`方法
        // 调用Files.lines(Path)的到文件行的流(Stream)
        // 把Stream放在java 7的try-with-resource语句中，可以保证其close()方法在离开try块时被调用，从而不需要手动调用
        try (Stream<String> lines = Files.lines(Paths.get(BASE_DIR,"StreamsOfLines.java"))) {
            // 可以像使用Stream一样传入lambda表达式
            Optional<String> passwordEntry = lines
                    .filter(s -> s.contains("password"))
                    .findFirst();
            passwordEntry.ifPresent(System.out::println);
        }
        // 输出：
        // Optional<String> passwordEntry = lines.filter(s -> s.contains("password")).findFirst();

        // 调用Files.lines(Path)的到文件行的流(Stream)
        // 把Stream放在java 7的try-with-resource语句中，可以保证其close()方法在离开try块时被调用，从而不需要手动调用
        // 可以通过onClose方法，设置在close()被调用时执行的操作
        try (Stream<String> filteredLines = Files
                        .lines(Paths.get(BASE_DIR,"StreamsOfLines.java"))
                        .onClose(() -> System.out.println("Closing"))
                        .filter(s -> s.contains("password"))) {
            Optional<String> passwordEntry = filteredLines.findFirst();
            passwordEntry.ifPresent(System.out::println);
        }
        // 输出
        // Optional<String> passwordEntry = lines.filter(s -> s.contains("password")).findFirst();
        // Closing

        // 8.5.1.(2) 按行读取其他数据：`BufferedReader.lines()`
        URL url = new URL("http://horstmann.com");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            // BufferedReader.lines()：从BufferedReader生成Stream
            Stream<String> lines = reader.lines();
            Optional<String> imgEntry = lines
                    .filter(s -> s.contains("<img "))
                    .findFirst();
            imgEntry.ifPresent(System.out::println);
        }


        try (BufferedReader reader = new BufferedReader(
                // 构造一个Reader，会在读取文件到第10行时抛出IOException
                new Reader() {
                    private int count;
                    public void close() {}
                    public int read(char[] cbuf, int off, int len) throws IOException {
                        if (++count == 10) {
                            throw new IOException("Simulated exception");
                        }
                        return len;
                    }
                }
        )) {
            Stream<String> lines = reader.lines();
            Optional<String> imgEntry = lines.filter(s -> s.contains("<img ")).findFirst();
            imgEntry.ifPresent(System.out::println);
        }
        // 输出：流操作已经将IOException转化为UncheckedIOException抛出了
        // Exception in thread "main" java.io.UncheckedIOException: java.io.IOException: Simulated exception
        // at java.base/java.io.BufferedReader$1.
        // ...
    }
}
