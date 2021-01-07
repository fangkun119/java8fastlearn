import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;
import static java.util.stream.Collectors.*;

public class TestCh2Sec13 {
    private static final String LOG_PREFIX = "\t// ";

    public static void main(String[] args) throws IOException {
        // 输入数据
        String contents = new String(Files.readAllBytes(
                Paths.get("./code/ch2/alice.txt")), StandardCharsets.UTF_8);
        List<String> strList = Arrays.asList(contents.split("[\\P{L}]+"));
        // 共享数据
        int[] shortWords = new int[10];


        // 流操作应当是无状态的，意味着他们以任意顺序执行都不影响最终的计算结果，计算结果都与串行流相同
        // 反例如下，它把一个数组共享给了多个线程
        Arrays.fill(shortWords, 0);
        strList.stream().parallel().forEach(
            s -> {
                if (s.length() < 10) {
                    shortWords[s.length()]++;
                }
            });
        System.out.println(LOG_PREFIX + Arrays.toString(shortWords));
        // 三次运行结果都不同，并且没有一次是对的
        // [1, 1771, 4676, 6996, 5743, 3418, 2135, 1809, 823, 688]
        // [1, 1765, 4546, 6634, 5494, 3357, 2129, 1794, 821, 688]
        // [1, 1778, 4714, 6984, 5728, 3410, 2137, 1816, 823, 687]


        // 但如果用串行（单线程）的stream，输出结果是稳定的
        Arrays.fill(shortWords, 0);
        strList.stream().forEach(
            s -> {
                if (s.length() < 10) {
                    shortWords[s.length()]++;
                }
            });
        System.out.println(LOG_PREFIX + Arrays.toString(shortWords));
        // 三次运行结果：
        // [1, 1826, 4999, 7637, 6166, 3589, 2203, 1867, 831, 697]
        // [1, 1826, 4999, 7637, 6166, 3589, 2203, 1867, 831, 697]
        // [1, 1826, 4999, 7637, 6166, 3589, 2203, 1867, 831, 697]


        // Atomic integers 
        AtomicInteger[] shortWordCounters = new AtomicInteger[10];
        for (int i = 0; i < shortWordCounters.length; i++) {
            shortWordCounters[i] = new AtomicInteger();
        }
        strList.stream().forEach(
            s -> {
                if (s.length() < 10) {
                    shortWordCounters[s.length()].getAndIncrement();
                }
            });
        System.out.println(LOG_PREFIX + Arrays.toString(shortWordCounters));
        // 三次运行结果：
        // [1, 1826, 4999, 7637, 6166, 3589, 2203, 1867, 831, 697]
        // [1, 1826, 4999, 7637, 6166, 3589, 2203, 1867, 831, 697]
        // [1, 1826, 4999, 7637, 6166, 3589, 2203, 1867, 831, 697]

        // Grouping works in parallel
        System.out.println(
            LOG_PREFIX +
            strList.stream().parallel().filter(s -> s.length() < 10).collect(
               groupingBy(
                  String::length,
                  counting())));
        // 三次运行结果：
        // {0=1, 1=1826, 2=4999, 3=7637, 4=6166, 5=3589, 6=2203, 7=1867, 8=831, 9=697}
        // {0=1, 1=1826, 2=4999, 3=7637, 4=6166, 5=3589, 6=2203, 7=1867, 8=831, 9=697}
        // {0=1, 1=1826, 2=4999, 3=7637, 4=6166, 5=3589, 6=2203, 7=1867, 8=831, 9=697}
   }
}


