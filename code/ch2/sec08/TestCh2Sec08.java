import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class TestCh2Sec08 {
   // 打印label和content
   private static final String LOG_PREFIX = "\t// ";
   public static <T> void display(String label, T content) {
      System.out.println("");
      System.out.println(LOG_PREFIX + label);
      System.out.println(LOG_PREFIX + content.toString());
   }

   public static void main(String[] args) throws IOException {
      // 测试数据
      Integer[] intArray = {
         3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5, 8, 9, 7, 9, 3, 2, 3, 8, 4, 6, 2, 6, 4, 3, 3, 8, 3, 2, 7, 9, 5, 0, 2, 8, 8, 4, 1, 9, 7, 1, 6, 9, 3, 9, 9, 3, 7, 5, 1, 0, 5, 8, 2, 0, 9, 7, 4, 9, 4, 4, 5, 9, 2, 3, 0, 7, 8, 1, 6, 4, 0, 6, 2, 8, 6 };

      // reduce as Optional<T>
      Optional<Integer> sum1 = Stream.of(intArray).reduce((x, y) -> x + y);
      display("Stream.of(intArray).reduce((x, y) -> x + y) /* as Optional<Integer> */", sum1);
      // Stream.of(intArray).reduce((x, y) -> x + y) /* as Optional<Integer> */
      // Optional[366]

      sum1 = Stream.of(intArray).reduce(Integer::sum);
      display("Stream.of(intArray).reduce(Integer::sum) /* as Optional<Integer*/", sum1);
      // Stream.of(intArray).reduce(Integer::sum) /* as Optional<Integer*/
      // Optional[366]

      Optional<Integer> sum2 = Stream.<Integer>empty().reduce((x, y) -> x + y); // Or values.reduce(Integer::sum);
      display("Stream.<Integer>empty().reduce((x, y) -> x + y) /* as Optional<Integer> */", sum2);
      // Stream.<Integer>empty().reduce((x, y) -> x + y) /* as Optional<Integer> */
      // Optional.empty

      sum2 = Stream.<Integer>empty().reduce(Integer::sum);
      display("Stream.<Integer>empty().reduce(Integer::sum) /* as Optional<Integer>", sum2);
      // Stream.<Integer>empty().reduce(Integer::sum) /* as Optional<Integer>
      // Optional.empty

      // reduce as T
      Integer sum3 = Stream.of(intArray).reduce(0, (x, y) -> x + y);
      display("Stream.of(intArray).reduce(0, (x, y) -> x + y) /* as Integer */", sum3);
      // Stream.of(intArray).reduce(0, (x, y) -> x + y) /* as Integer */
      // 366

      Integer sum4 = Stream.<Integer>empty().reduce(0, (x, y) -> x + y);
      display("Stream.<Integer>empty().reduce(0, (x, y) -> x + y) /* as Integer */", sum4);
      // Stream.<Integer>empty().reduce(0, (x, y) -> x + y) /* as Integer */
      // 0


      // 测试数据
      String contents = new String(Files.readAllBytes(
            Paths.get("code/ch2/alice.txt")), StandardCharsets.UTF_8);
      List<String> strList = Arrays.asList(contents.split("[\\P{L}]+"));

      // U reduce(identity, accumulator, combiner)
      int result = strList.stream().reduce(
              0, (s, w) -> s + w.length(), (s1, s2) -> s1 + s2);
      display("strList.stream().reduce(0, (s, w) -> s + w.length(), (s1, s2) -> s1 + s2)", result);
      // strList.stream().reduce(0, (s, w) -> s + w.length(), (s1, s2) -> s1 + s2)
      // 122988

      result = strList.parallelStream().reduce(
              0, (s, w) -> s + w.length(), (s1, s2) -> s1 + s2);
      display("strList.parallelStream().reduce(0, (s, w) -> s + w.length(), (s1, s2) -> s1 + s2)", result);
      // strList.parallelStream().reduce(0, (s, w) -> s + w.length(), (s1, s2) -> s1 + s2)
      // 122988
   }
}


