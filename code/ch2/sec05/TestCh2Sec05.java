import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class TestCh2Sec05 {
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

   public static void main(String[] args) throws IOException {
      // distinct
      Stream<String> uniqueWords = Stream.of("merrily", "merrily", "merrily", "gently").distinct();
      show("Stream.of(\"merrily\", \"merrily\", \"merrily\", \"gently\").distinct()", uniqueWords);
      // Stream.of("merrily", "merrily", "merrily", "gently").distinct()
      // [merrily, gently]

      // 一篇文章的所有词
      String contents = new String(Files.readAllBytes(
            Paths.get("code/ch2/alice.txt")), StandardCharsets.UTF_8);
      List<String> strList = Arrays.asList(contents.split("[\\P{L}]+"));

      // 所有的word
      show("strList.stream()", strList.stream());
      // strList.stream()
      // [, Project, Gutenberg, s, Alice, s, Adventures, in, Wonderland, by, ...]

      // distinct
      Stream<String> distinct = strList.stream().distinct();
      show("strList.stream().distinct()", distinct);
      // strList.stream().distinct()
      // [, Project, Gutenberg, s, Alice, Adventures, in, Wonderland, by, Lewis, ...]

      // sorted
      Stream<String> sorted = strList.stream().sorted();
      show("strList.stream().sorted()", sorted);
      // strList.stream().sorted()
      // [, A, A, A, A, A, A, A, A, A, ...]

      // distinct().sorted()
      Stream<String> distinctSorted = strList.stream().distinct().sorted();
      show("distinctSorted", distinctSorted);
      // distinctSorted
      // [, A, ACTUAL, ADVENTURES, AGREE, AGREEMENT, AK, ALICE, ALL, AND, ...]

      // sorted(Comparator.comparing(String::length).reversed()
      Stream<String> longestFirst = strList.stream().sorted(Comparator.comparing(String::length).reversed());
      show("strList.stream().sorted(Comparator.comparing(String::length).reversed()", longestFirst);
      // strList.stream().sorted(Comparator.comparing(String::length).reversed()
      // [unenforceability, representations, MERCHANTIBILITY, disappointment, Multiplication, contemptuously, contemptuously, affectionately, Redistribution, redistribution, ...]
   }
}


