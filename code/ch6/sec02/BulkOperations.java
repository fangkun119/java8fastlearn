import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class BulkOperations {

    public static void main(String[] args) throws IOException {
        // 测试数据，一本书中所有单词的词频
        ConcurrentHashMap<String, Long> word2cntConcurrentHashmap = new ConcurrentHashMap<>();

        // 填充测试数据
        Path path = Paths.get("./code/ch2/alice.txt");
        String contents = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        Stream<String> wordStream = Stream.of(contents.split("[\\P{L}]+"));
        wordStream.parallel().forEach(w -> word2cntConcurrentHashmap.merge(w, 1L, Long::sum));

        // search
        long threshold = 1; //容量超过1就并行，保证始终以并行方式运行
        String result = word2cntConcurrentHashmap.search(threshold, (k, v) -> v > 1000 ? k : null);
        System.out.println("search result: " + result);
        // search result: the


        // forEach
        System.out.println("\n---\n");
        word2cntConcurrentHashmap.forEach(
                threshold,
                (k, v) -> System.out.print(k + " -> " + v + ", "));
        // govern -> 1, numerous -> 1, England -> 1, ......


        System.out.println("\n---\n");
        word2cntConcurrentHashmap.forEach(
                threshold,                       // key
                (k, v) -> k + " -> " + v + ", ", // Transformer
                System.out::print);              // Consumer
        // govern -> 1, numerous -> 1, England -> 1, ......


        System.out.println("\n---\n");
        word2cntConcurrentHashmap.forEach(threshold,
                (k, v) -> v > 300 ? k + " -> " + v : null, // Filter and transformer
                System.out::println); // The nulls are not passed to the consumer
        // I -> 545
        // a -> 672
        // ...


        // reduceKeys
        Integer maxlength = word2cntConcurrentHashmap.reduceKeys(threshold,
                String::length, // Transformer
                Integer::max);  // Accumulator
        System.out.println("maxlength: " + maxlength);
        // maxlength: 16


        // reduceValues
        Long sum = word2cntConcurrentHashmap.reduceValues(threshold, Long::sum);
        System.out.println("\nsum: " + sum);
        // sum: 30420


        Long count = word2cntConcurrentHashmap.reduceValues(threshold,
                v -> v > 300 ? 1L : null,
                Long::sum);
        System.out.println("count: " + count);
        // count: 13


        long sum2 = word2cntConcurrentHashmap.reduceValuesToLong(threshold,
                Long::longValue, // Transformer to primitive type
                0, // Default value for empty concurrentHashMap
                Long::sum); // Primitive type accumulator
        System.out.println("sum2: " + sum2);
        // sum2: 30420
    }
}
