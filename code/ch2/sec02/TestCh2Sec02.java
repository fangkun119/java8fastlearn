import java.io.*;
import java.math.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.*;

public class TestCh2Sec02 {

   public static void main(String[] args) throws IOException {
      // 打印当前目录
      System.out.println("current directory: " + System.getProperty("user.dir") +  "\n");

      // contentsString: 用String的类型读入整篇文档
      Path path = Paths.get("code/ch2/alice.txt");
      String contentsString = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);

      // 成员函数：Collection.stream
      Stream<String> collectionAsStream = Arrays.asList("str1", "str2", "str3").stream();
      show("Arrays.asList(\"str1\", \"str2\", \"str3\").stream()", collectionAsStream);
      // Arrays.asList("str1", "str2", "str3").stream():
      // [str1, str2, str3]

      // 静态方法 Stream.of(T[])
      Stream<String> words = Stream.of(contentsString.split("[\\P{L}]+"));
      show("Stream.of(String[])", words);
      // Stream.of(String[]):
      // [, Project, Gutenberg, s, Alice, s, Adventures, in, Wonderland, by, ...]

      Stream<String> song = Stream.of("gently", "down", "the", "stream");
      show("Stream.of(\"gently\", \"down\", \"the\", \"stream\")", song);
      // Stream.of("gently", "down", "the", "stream"):
      // [gently, down, the, stream]

      // 静态方法Arrays.stream
      Stream<String> fromArray = Arrays.stream(new String[]{"idx0", "idx1", "idx2", "idx3"}, 1,  3);
      show("Arrays.stream(new String[]{\"idx0\", \"idx1\", \"idx2\", \"idx3\"}, 1,  3)", fromArray);
      // Arrays.stream(new String[]{"idx0", "idx1", "idx2", "idx3"}, 1,  3):
      // [idx1, idx2]

      // 静态方法 Stream.<String>empty()
      Stream<String> silence = Stream.empty();
      silence = Stream.<String>empty(); // Explicit type specification
      show("Stream.<String>empty()", silence);
      // Stream.<String>empty():
      // []

      // 成员函数 myPattern.splitAsStream(String)
      Stream<String> wordsAnotherWay = Pattern.compile("[\\P{L}]+").splitAsStream(contentsString);
      show("Pattern.compile(\"[\\\\P{L}]+\").splitAsStream(contentsString)", wordsAnotherWay);
      // Pattern.compile("[\\P{L}]+").splitAsStream(contentsString):
      // [, Project, Gutenberg, s, Alice, s, Adventures, in, Wonderland, by, ...]

      // 静态方法 File.lines
      try (Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8)) {
         show ("Files.lines(path, StandardCharsets.UTF_8)", lines);
      }
      // Files.lines(path, StandardCharsets.UTF_8):
      // [﻿Project Gutenberg's Alice's Adventures in Wonderland, by Lewis Carroll, , This eBook is for the use of anyone anywhere at no cost and with, almost no restrictions whatsoever.  You may copy it, give it away or, re-use it under the terms of the Project Gutenberg License included, with this eBook or online at www.gutenberg.org, , , Title: Alice's Adventures in Wonderland, , ...]
   }

   // 工具函数：用来输出Stream中的数据，如果数据量超过10个，只输出先10个
   public static <T> void show(String title, Stream<T> stream) {
      final int SIZE = 10;

      // Stream操作的按需执行的，
      // 下面的`.limit(SIZE + 1)`使得Stream只使用前SIZE个元素
      // 因此即使底层数据有无限的数据量（例如使用Generator），也只会取前SIZE个
      List<T> firstElements = stream.limit(SIZE + 1).collect(Collectors.toList());
      System.out.println("\t// " + title + ": ");

      if (firstElements.size() <= SIZE)
         System.out.println("\t// " + firstElements);
      else {
         firstElements.remove(SIZE);
         String out = firstElements.toString();
         System.out.println("\t// " + out.substring(0, out.length() - 1) + ", ...]");
      }

      System.out.println("");
   }
}
