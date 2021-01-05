import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

public class TestCh2Sec07 {
   public static void main(String[] args) throws IOException {
      // 输入数据
      String contents = new String(
              Files.readAllBytes(
                      Paths.get("code/ch2/alice.txt")), StandardCharsets.UTF_8);
      List<String> strList = Arrays.asList(contents.split("[\\P{L}]+"));

      // Optional<T>.ifPresent(Consumer<? super T>)
      Optional<String> optionalValue = strList.stream().filter(s -> s.contains("red")).findFirst();
      optionalValue.ifPresent(s -> System.out.println("\n\t// " + s + " contains red"));
      // tired contains red

      Set<String> results = new HashSet<>();
      optionalValue.ifPresent(results::add);
      results.stream().forEach(e -> System.out.println("\n\t// " + e));
      // tired

      // Optional<U> elem = Optional<T>.map(Function<? super T, ? extends U>)
      Optional<Boolean> added = optionalValue.map(results::add);
      System.out.println("\n\t// " + added);
      // Optional[false]

      // T Optional<T>.orElse(T other)
      System.out.println("\n\t// " +
              strList.stream()
                      .filter(s -> s.contains("fred"))
                      .findFirst()
                      .orElse("No word") + " contains fred");
      // No word contains fred

      String result = Optional.<String>empty().orElse("N/A");
      System.out.println("\n\t// result: " + result);
      // result: N/A

      result = Optional.<String>empty().orElseGet(() -> System.getProperty("user.dir"));
      System.out.println("\n\t// result: " + result);
      // result: /Users/fangkun/Dev/git/java8fastlearn

      try {
         result = Optional.<String>empty().orElseThrow(NoSuchElementException::new);
         System.out.println("\n\t// result: " + result);
      } catch (Throwable t) {
         t.printStackTrace();
      }
      // java.util.NoSuchElementException
      //	at java.util.Optional.orElseThrow(Optional.java:290)
      //	at TestCh2Sec07.main(TestCh2Sec07.java:44)

      // Optional<U> Optional<T>.flatMap(Function<? super T, Optional<U>>)
      System.out.println("\n\t// " +
              inverse(4.0).flatMap(TestCh2Sec07::squareRoot));
      // Optional[0.5]

      System.out.println("\n\t// " +
              inverse(-1.0).flatMap(TestCh2Sec07::squareRoot));
      // Optional.empty

      System.out.println("\n\t// " +
              inverse(0.0).flatMap(TestCh2Sec07::squareRoot));
      // Optional.empty

      System.out.println("\n\t// " +
              Optional.of(-4.0).flatMap(TestCh2Sec07::inverse).flatMap(TestCh2Sec07::squareRoot));
      // Optional.empty
   }

   public static Optional<Double> inverse(Double x) {
      return x == 0 ? Optional.empty() : Optional.of(1 / x);
   }

   public static Optional<Double> squareRoot(Double x) {
      return x < 0 ? Optional.empty() : Optional.of(Math.sqrt(x));
   }
}


