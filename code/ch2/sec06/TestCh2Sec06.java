import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class TestCh2Sec06 {
   public static void main(String[] args) throws IOException {
      String contents = new String(Files.readAllBytes(Paths.get("code/ch2/alice.txt")), StandardCharsets.UTF_8);
      List<String> strList = Arrays.asList(contents.split("[\\P{L}]+"));

      // max
      Optional<String> largest = strList.stream().max(String::compareToIgnoreCase);
      if (largest.isPresent()) {
         System.out.println("\n\t// largest: \n\t// " + largest.get());
      }
      // largest:
      // zip

      // anyMatch
      boolean aWordStartsWithQ = strList.stream().anyMatch(s -> s.startsWith("Q"));
      System.out.println("\n\t// strList.stream().anyMatch(s -> s.startsWith(\"Q\"): \n\t// " + aWordStartsWithQ);
      // strList.stream().anyMatch(s -> s.startsWith("Q"):
      // true

      // findFirst
      Optional<String> startsWithQ1 = strList.stream().filter(s -> s.startsWith("Q")).findFirst();
      if (startsWithQ1.isPresent()) {
         System.out.println("\n\t// startsWithQ1.findFirst: \n\t// " + startsWithQ1.get());
      } else {
         System.out.println("\n\t// No word starts with Q");
      }
      // startsWithQ1.findFirst:
      // Quick

      // findAny
      Optional<String> startsWithQ2 = strList.stream().parallel().filter(s -> s.startsWith("Q")).findAny();
      if (startsWithQ2.isPresent()) {
         System.out.println("\n\t// startsWithQ2.findAny: \n\t// " + startsWithQ2.get());
      } else {
         System.out.println("\n\t// No word starts with Q");
      }
      // startsWithQ1:
      // Queen
   }
}


