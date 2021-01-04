import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;

/**
* 函数式接口（Functional Interfaces）：只包含一个抽象方法的接口，这样的接口可以使用lambda表达式来创建接口对象
*   重新声明的来自Object类中的方法（如toString,clone）不是抽象方法，另外Java8开始接口可以声明非抽象方法
*   Java 8不提供其他语言所支持的函数类型，因此lambda只能存储在函数式接口中
* 使用：
*   Lambda为函数式接口创建对象；而这个对象的使用方，函数参数类型也应当是这个函数式接口
*   例如：
*      Arrays.sort(strings, (first, second) -> Integer.compare(first.length(), second.length()));
*      等价于
*      Comparator<String> cmp = (first, second) -> Integer.compare(first.length(), second.length());
*      Arrays.sort(strings, cmp)
* 异常处理：
*   从lambda代码块抛出的异常，处理方法与普通方法抛出的异常一样，也需要处理，处理方法相同
* Java 8提供的内置函数式接口：
*   java.util.function包中，例如BiFunction<T, U, R>
*   Java 8以及未来更多的代码会调用这些接口，但是老的代码却不会调用，例如Arrays.sort调用的是Comparator
* 自己编写函数式接口：
*   可选 @FunctionalInterface 注解来增强安全检查和JDoc文档生成
*/

public class FunctionalInterfaces extends ThisInLambda {
   public void start(Stage stage) {
      String[] strings = "Mary had a little lamb".split(" ");

      // 用lambda表达式生成函数式接口Comparator<String>的对象
      Arrays.sort(strings, 
         (first, second) -> Integer.compare(first.length(), second.length()));
      System.out.println(Arrays.toString(strings));

      // 用lambda表达式生成函数式接口EventHandler<ActionEvent>的对象
      Button button = new Button("Click me!");
      button.setOnAction(
         event -> System.out.println("Thanks for clicking!"));
      stage.setScene(new Scene(button));
      stage.show();

      // lambda表达式生成的对象，必须与调用方的参数类型匹配
      // lambda生成对象的类型是 BiFunction<String, String, Integer>
      // Arrays.sort参数类型是 Comparator<String>
      BiFunction<String, String, Integer> comp
         = (first, second) -> Integer.compare(first.length(), second.length());
      // Arrays.sort(strings, comp); 
      // Error: Arrays.sort doesn't want a BiFunction

      // lambda表达式抛出的类型必须被处理，要么在lambda表达式中的代码块中被捕捉，要么调用方可以捕捉或向外抛出这个exception
      // Runnable sleeper = () -> { System.out.println("Zzz"); Thread.sleep(1000); };
      // Error: Thread.sleep can throw a checked InterruptedException
      Runnable sleeper2 = () -> { 
         System.out.println("Zzz"); 
         try {
            Thread.sleep(1000);
         } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
         }
      };
      Callable<Void> sleeper3 = () -> { System.out.println("Zzz"); Thread.sleep(1000); return null; };
   }
}
