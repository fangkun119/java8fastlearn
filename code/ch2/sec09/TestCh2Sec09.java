import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class TestCh2Sec09 {
   // 文章的文件路径
   private static final String FILE_NAME = "code/ch2/alice.txt";
   private static final String LOG_PREFIX = "\t// ";

   // 读取一篇文章，提取出word stream，其中所有word的元音字母都已经被删除
   public static Stream<String> noVowelsStream() throws IOException {
      String contents = new String(
              Files.readAllBytes(Paths.get(FILE_NAME)), StandardCharsets.UTF_8);
      List<String> wordList
              = Arrays.asList(contents.split("[\\P{L}]+"));
      Stream<String> words = wordList.stream();
      return words.map(s -> s.replaceAll("[aeiouAEIOU]", "")); 
   } 

   // 打印Set中前10个Object、用","分隔
   public static <T> void display(String label, Set<T> set) {
      System.out.println("");
      System.out.println(
         LOG_PREFIX + label + ": " + set.getClass().getName());
      System.out.println(
         LOG_PREFIX + "[" +
         set.stream().limit(10).map(Object::toString).collect(Collectors.joining(", "))
         + "]");
   }

   // 打印label和content
   public static <T> void display(String label, T content) {
      System.out.println("");
      System.out.println(LOG_PREFIX + label);
      System.out.println(LOG_PREFIX + content.toString());
   }

   public static void main(String[] args) throws IOException {

      // iterator()
      Iterator<Integer> iter = Stream.iterate(0, n -> n + 1).limit(5).iterator();
      while (iter.hasNext()) {
         System.out.println(LOG_PREFIX + iter.next());
      }
      // 0
      // 1
      // 2
      // 3
      // 4


      // toArray() 只能返回 Object[]
      Object[] numbers = Stream.iterate(0, n -> n + 1).limit(5).toArray();
      Integer number = (Integer) numbers[0]; // OK
      System.out.println(LOG_PREFIX + "numbers    : " + numbers); // Object[]
      System.out.println(LOG_PREFIX + "numbers[0] : " + number );
      // numbers    : [Ljava.lang.Object;@34a245ab
      // numbers[0] : 0


      // 泛型擦除对数组的影响
      try {
         Integer[] numbers2 = (Integer[]) numbers; // Throws exception
      } catch (ClassCastException ex) {
         ex.printStackTrace();
      }
      // java.lang.ClassCastException: [Ljava.lang.Object; cannot be cast to [Ljava.lang.Integer;
      //    at TestCh2Sec09.main(TestCh2Sec09.java:51)


      // toArray(T[]:new)：解决泛型擦除的影响，可以返回T[]
      Integer[] numbers3 = Stream.iterate(0, n -> n + 1).limit(5).toArray(Integer[]::new);
      display("Stream.iterate(0, n -> n + 1).limit(10).toArray(Integer[]::new);", numbers3);
      // Stream.iterate(0, n -> n + 1).limit(10).toArray(Integer[]::new);
      // [Ljava.lang.Integer;@52cc8049


      // collect(Supplier<R>，BiConsumer<R, ? super T>, BiConsumer<R, R>)
      HashSet<String> noVowelHashSet
         = noVowelsStream().collect(HashSet::new, HashSet::add, HashSet::addAll);
      display("noVowelsStream().collect(HashSet::new, HashSet::add, HashSet::addAll)", noVowelHashSet);
      // noVowelsStream().collect(HashSet::new, HashSet::add, HashSet::addAll): java.util.HashSet
      // [, Pppr, ccssd, rsrc, tdy, srprsd, kss, rsrch, gssd, vwng]


      // collect(Collector);
      Set<String> noVowelSet = noVowelsStream().collect(Collectors.toSet());
      display("noVowelsStream().collect(Collectors.toSet())", noVowelSet);
      // noVowelsStream().collect(Collectors.toSet()): java.util.HashSet
      // [, Pppr, ccssd, rsrc, tdy, srprsd, kss, rsrch, gssd, vwng]

      TreeSet<String> noVowelTreeSet
         = noVowelsStream().collect(Collectors.toCollection(TreeSet::new));
      display("noVowelsStream().collect(Collectors.toCollection(TreeSet::new))", noVowelTreeSet);
      // noVowelsStream().collect(Collectors.toCollection(TreeSet::new)): java.util.TreeSet
      // [, B, BFR, BG, BK, BRCH, BST, BSY, BT, BTS]

      String result = noVowelsStream().limit(5).collect(Collectors.joining());
      display("noVowelsStream().limit(10).collect(Collectors.joining())", result);
      // noVowelsStream().limit(10).collect(Collectors.joining())
      // PrjctGtnbrgslc

      result = noVowelsStream().limit(5).collect(Collectors.joining(", "));
      display("noVowelsStream().limit(10).collect(Collectors.joining(\", \")", result);
      // noVowelsStream().limit(10).collect(Collectors.joining(", ")
      // , Prjct, Gtnbrg, s, lc

      IntSummaryStatistics summary
              = noVowelsStream().collect(Collectors.summarizingInt(String::length));
      double averageWordLength = summary.getAverage();      
      double maxWordLength = summary.getMax();
      display("noVowelsStream().collect(Collectors.summarizingInt(String::length)).getAverage()", averageWordLength);
      display("noVowelsStream().collect(Collectors.summarizingInt(String::length)).getMax()", maxWordLength);
      // noVowelsStream().collect(Collectors.summarizingInt(String::length)).getAverage()
      // 2.488593030900723
      // noVowelsStream().collect(Collectors.summarizingInt(String::length)).getMax()
      // 10.0

      // forEach(...)
      noVowelsStream().limit(5).forEach(System.out::println);
      // Prjct
      // Gtnbrg
      // s
      // lc
   }
}


