import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

/**
* 变量作用域：
* 1. Lambda表达式可以捕获闭合作用域的变量值，并且使用这些值
* 2. Lambda表达式强制约束，不可以修改这些所使用的局部变量
*    是因为Lambda访问这些变量的原理、类似于变量值拷贝、为了避免两个值不一致问题的发生（特别是多线程环境），增加了这个约束
* 3. 即便是增加了不可修改约束，仍然不能完全避免“不一致”问题的发生
*    (1) 类成员变量、静态变量不受约束（只约束局部变量）
*    (2) 如果变量是一个对象，不可修改约束只能保证这个变量不指向其他对象，不能阻挡对象内部的修改
*    彻底的解决方法、需要使用stream
* 4. Lambda表达式的方法体、与嵌套代码块有相同的作用域，适用同样的命名和屏蔽规则
* 5. Lambda表达式中使用this关键字时，会引用创建该表达式的方法的this参数（而不是执行该表达式的方法的this参数）
*/

public class VariableScope {
   public static void main(String[] args) {
      repeatMessage("Hello", 100);
   }

   public static void repeatMessage(String text, int count) {
      // lambda表达式引用作用域中的变量text和count
      Runnable r = () -> {
         for (int i = 0; i < count; i++) {
            System.out.println(text);
            Thread.yield();
         }
      };
      new Thread(r).start();                  
   }

   public static void repeatMessage2(String text, int count) {
      // lambda表达式访问局部变量时，不可以修改这些变量
      Runnable r = () -> {
         while (count > 0) {
            // count--; // Error: Can't mutate captured variable
            System.out.println(text);
            Thread.yield();
         }
      };
      new Thread(r).start();                  
   }

   public static void countMatches(Path dir, String word) throws IOException {
      Path[] files = getDescendants(dir);
      // lambda表达式访问局部变量时，不可以修改这些变量
      int matches = 0;
      for (Path p : files) 
         new Thread(() -> { if (contains(p, word)) {
                  // matches++; 
                  // ERROR: Illegal to mutate matches
               }}).start();
   }
   */

   private static int matches;
   
   public static void countMatches2(Path dir, String word) {
      Path[] files = getDescendants(dir);
      for (Path p : files) 
         // lambda表达式访问类成员变量、静态变量时，编译器不会阻止对这些变量的修改，但是要小心带来的线程安全问题
         new Thread(() -> { if (contains(p, word)) {
                  matches++; 
                  // CAUTION: Legal to mutate matches, but not threadsafe      
               }}).start();
   }

   // Warning: Bad code ahead
   public static List<Path> collectMatches(Path dir, String word) {
      Path[] files = getDescendants(dir);
      // 当局部变量是对象时，lambda表达式仅仅不能修改这些对象的引用，但是仍然可以修改对象的数据，要小心带来的安全问题
      List<Path> matches = new ArrayList<>();
      for (Path p : files) 
         new Thread(() -> { if (contains(p, word)) {
                  matches.add(p);
                  // CAUTION: Legal to mutate matches, but not threadsafe
               }}).start();
      return matches;
   }

   public static Path[] getDescendants(Path dir) {
      try {
         try (Stream<Path> entries = Files.walk(dir)) {
            return entries.toArray(Path[]::new);
         }
      } catch (IOException ex) {
         return new Path[0];
      }
   }

   public static boolean contains(Path p, String word) {
      try {
         return new String(Files.readAllBytes(p), 
            StandardCharsets.UTF_8).contains(word);
      } catch (IOException ex) {
         return false;
      }
   }
}
