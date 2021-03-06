import java.util.*;
import javafx.application.*;
import javafx.application.Application;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;

/**
* CH 1.1：为什么要使用lambda表达式
* 
* Lambda表达式是一段可以被传递的代码，例如
* 1. 由另一个线程运行的代码
* 2. 用于排序的自定义比较器
* 3. 由回调函数延迟执行的代码
*
* 如果不使用lambda，使用老式的方法，那么将需要不得不构建一个类，由类对象来封装所要传递的代码
*/

public class OldStyle extends Application {
   public static void main(String[] args) {      
      // Workder类的实现代码可以替换成lambda表达式
      Worker w = new Worker();      
      new Thread(w).start();

      // LenghtComparator的实现代码可以替换成lambda表达式
      class LengthComparator implements Comparator<String> {
         public int compare(String first, String second) {
            return Integer.compare(first.length(), second.length());
         }
      }
      
      String[] strings = "Mary had a little lamb".split(" ");
      Arrays.sort(strings, new LengthComparator());
      System.out.println(Arrays.toString(strings));

      Application.launch(args);
   }

   public void start(Stage stage) {
      Button button = new Button("Click me!");

      // EventHandler<ActionEvent>的实现代码可以替换成lambda表达式
      button.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
               System.out.println("Thanks for clicking!");
            }
         });

      stage.setScene(new Scene(button));
      stage.show();
   }
}

class Worker implements Runnable {
   public void run() {
      for (int i = 0; i < 10; i++)
         doWork();
   }

   public void doWork() {
      System.out.println("Doing work...");
      try {
         Thread.sleep(100);
      } catch (InterruptedException ex) {
         Thread.currentThread().interrupt();
      }
   }
}

