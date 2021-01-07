import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.*;

public class TestCh2Sec12 {
   public static String PREFIX = "\t// ";

   public static void display(String title, IntStream stream) {
      final int SIZE = 5;
      int[] firstElements = stream.limit(SIZE + 1).toArray();
      System.out.print(PREFIX + title + ": [");
      int i;
      for (i = 0; i < SIZE && i < firstElements.length; i++) {
         System.out.print(firstElements[i]);
         if (i < firstElements.length - 1) {
            System.out.print(", ");
         }
      }
      if (i < firstElements.length) {
         System.out.print("...");
      }
      System.out.println("]");
   }


   public static void main(String[] args) throws IOException {
      // 输入数据
      Path path = Paths.get("code/ch2/alice.txt");
      String contents = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
      Stream<String> strList = Stream.of(contents.split("[\\P{L}]+"));

      String sentence = "\uD835\uDD46 is the set of octonions.";
      System.out.println(sentence);


      // 从数组创建IntStream
      IntStream isFromOfOperator = IntStream.of(1,2,3,4,5);
      display("IntStream.of(1,2,3,4,5)", isFromOfOperator);
      // IntStream.of(1,2,3,4,5): [1, 2, 3, 4, 5]

      IntStream isFromArray = Arrays.stream(new int[] {1,2,3,4,5}, 1, 3);
      display("Arrays.stream(new int[] {1,2,3,4,5}, 1, 3)", isFromArray);
      // Arrays.stream(new int[] {1,2,3,4,5}, 1, 3): [2, 3]

      IntStream is1 = IntStream.generate(() -> (int)(Math.random() * 100));
      display("IntStream.generate(() -> (int)(Math.random() * 100))", is1);
      // IntStream.generate(() -> (int)(Math.random() * 100)): [42, 0, 98, 5, 81, ...]

      IntStream is2 = IntStream.range(5, 9);
      display("IntStream.range(5, 9)", is2);
      // IntStream.range(5, 10): [5, 6, 7, 8]

      IntStream is3 = IntStream.rangeClosed(5, 9);
      display("IntStream.rangeClosed(5, 9)", is3);
      // IntStream.rangeClosed(5, 10): [5, 6, 7, 8, 9]

      // List<T>.mapToInt(ToIntFunction)
      IntStream is4 = strList.mapToInt(String::length);
      display("strList.mapToInt(String::length)", is4);
      // strList.mapToInt(String::length): [0, 7, 9, 1, 5, ...]

      IntStream is5 = new Random().ints();
      display("new Random().ints()", is5);
      // new Random().ints(): [-328531074, -978755995, -120081503, 559623620, -1461156362, ...]

      // String.codePoints()
      IntStream codes = sentence.codePoints(); // UTF-16编码单元组成的IntStream
      String codePoints = codes.mapToObj(c -> String.format("%X ", c)).collect(Collectors.joining());
      System.out.println();
      System.out.println(PREFIX + "sentence.codePoints().mapToObj(c -> String.format(\"%X \", c)).collect(Collectors.joining())");
      System.out.println(PREFIX + codePoints);
      System.out.println();
      // sentence.codePoints().mapToObj(c -> String.format("%X ", c)).collect(Collectors.joining())
      // 1D546 20 69 73 20 74 68 65 20 73 65 74 20 6F 66 20 6F 63 74 6F 6E 69 6F 6E 73 2E

      // IntStream.boxed()
      Stream<Integer> integers = IntStream.range(0, 100).boxed();
      IntStream is6 = integers.mapToInt(Integer::intValue);
      display("IntStream.range(0, 100).boxed().mapToInt(Integer::intValue)", is6);
      // IntStream.range(0, 100).boxed().mapToInt(Integer::intValue): [0, 1, 2, 3, 4, ...]
   }
}


