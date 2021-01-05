import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class TestCh2Sec04 {
   // 工具函数：用来输出Stream中的数据，如果数据量超过10个，只输出先10个
   public static <T> void show(String title, Stream<T> stream) {
      System.out.println("\t// " + title);

      final int SIZE = 10;
      List<T> firstElements = stream.limit(SIZE + 1).collect(Collectors.toList());
      if (firstElements.size() <= SIZE)
         System.out.println("\t// " + firstElements + "\n");
      else {
         firstElements.remove(SIZE);
         String out = firstElements.toString();
         System.out.println("\t// " + out.substring(0, out.length() - 1) + ", ...]\n");
      }
   }

   // 工具函数：把String转化成Stream<Character>
   public static Stream<Character> characterStream(String s) {
      List<Character> result = new ArrayList<>();
      for (char c : s.toCharArray()) {
         result.add(c);
      }
      return result.stream();
   }

   public static void main(String[] args) throws IOException {
      // limit
      Stream<Double> randoms = Stream.generate(Math::random).limit(5);
      show("Stream.generate(Math::random).limit(5)", randoms);
      // Stream.generate(Math::random).limit(5)
      // [0.4123903170705443, 0.8416544780394619, 0.5815384213377721, 0.7926383668929101, 0.32102829116358]

      Stream<Integer> firstFive = Stream.iterate(0, n -> n + 1).limit(5);
      show("Stream.iterate(0, n -> n + 1).limit(5)", firstFive);
      // Stream.iterate(0, n -> n + 1).limit(5)
      // [0, 1, 2, 3, 4]

      // skip
      Stream<Integer> notTheFirst = Stream.iterate(0, n -> n + 1).skip(1);
      show("Stream.iterate(0, n -> n + 1).skip(1)", notTheFirst);
      // Stream.iterate(0, n -> n + 1).skip(1)
      // [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, ...]

      // concat
      Stream<Character> combined = Stream.concat(characterStream("Hello"), characterStream("World"));
      show("Stream.concat(characterStream(\"Hello\"), characterStream(\"World\"))", combined);
      // Stream.concat(characterStream("Hello"), characterStream("World"))
      // [H, e, l, l, o, W, o, r, l, d]

      // peek
      Object[] powers = Stream.iterate(1.0, p -> p * 2)
         .peek(e -> System.out.println("\t// Fetching " + e))
         .limit(5).toArray();
      System.out.println("\t// " + Arrays.asList(powers));
      // Fetching 1.0
      // Fetching 2.0
      // Fetching 4.0
      // Fetching 8.0
      // Fetching 16.0
      // [1.0, 2.0, 4.0, 8.0, 16.0]
   }
}


