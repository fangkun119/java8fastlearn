import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class TestCh2Sec03 {
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
      // filter
      String contents = new String(Files.readAllBytes(
            Paths.get("code/ch2/alice.txt")), StandardCharsets.UTF_8);
      List<String> strList1 = Arrays.asList(contents.split("[\\P{L}]+"));
      Stream<String> longWords = strList1.stream().filter(w -> w.length() > 12);
      show("strList1.stream().filter(w -> w.length() > 12)", longWords);
      // strList1.stream().filter(w -> w.length() > 12)
      // [conversations, disappointment, Multiplication, inquisitively, uncomfortable, uncomfortable, circumstances, contemptuously, extraordinary, straightening, ...]

      //  map
      Stream<String> lowercaseWords = strList1.stream().map(String::toLowerCase);
      show("strList1.stream().map(String::toLowerCase)", lowercaseWords);
      // strList1.stream().map(String::toLowerCase)
      // [, project, gutenberg, s, alice, s, adventures, in, wonderland, by, ...]

      // filter(...).map(...)
      List<String> strList2 = Arrays.asList("row", "row", "row", "your", "boat", "gently", "down", "the", "stream");
      Stream<Character> firstChars = strList2.stream().filter(w -> w.length() > 0).map(s -> s.charAt(0));
      show("strList2.stream().filter(w -> w.length() > 0).map(s -> s.charAt(0))", firstChars);
      // strList2.stream().filter(w -> w.length() > 0).map(s -> s.charAt(0))
      // [r, r, r, y, b, g, d, t, s]

      // flatmap
      List<String> strList3 = Arrays.asList("row", "row", "row", "your", "boat", "gently", "down", "the", "stream");
      Stream<Character> letters = strList3.stream().flatMap(w -> characterStream(w));
      show("strList3.stream().flatMap(w -> characterStream(w))", letters);
      // strList3.stream().flatMap(w -> characterStream(w))
      // [r, o, w, r, o, w, r, o, w, y, ...]
   }
}
