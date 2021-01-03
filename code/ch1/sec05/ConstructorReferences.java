import java.util.*;
import java.util.stream.*;

import javafx.application.*;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.*;

/**
* 构造器引用：与方法引用相同，只是构造器的方法名称为new；当有多个构造器时，使用哪个取决于上下文
* 例如：下面的Button::new等价于 x -> new Button(x)
* 
* 使用数组类型编写构造器引用: 
* 例如: int[]::new 等价于 n -> new int[n]
* 
* 数组类型构造器引用，可以解决Java 6的泛型数组问题
* Java 6中，Button[]无法用通过泛型new T[]来生成，因为会被擦除为Object[]
* Java 8中，使用Button[]::new可以生成Buttong[]数组，避开被擦除的问题
*/

public class ConstructorReferences extends Application {
   public static void main(String[] args) {
      Application.launch(args);
   }

   public void start(Stage stage) {
      List<String> labels = Arrays.asList("Ok", "Cancel", "Yes", "No", "Maybe");
      
      // Button::new 等价于 labelString -> new Button(labelString)
      // 调用了Button(String)构造函数
      Stream<Button> stream = labels.stream().map(Button::new);
      List<Button> buttons = stream.collect(Collectors.toList());
      System.out.println(buttons);

      // Button::new 等价于 streamObj -> new Button(streamObj)
      // 调用了Button(Object)构造函数（labels.stream()返回的是Object[]，然后map函数将数组中的每一个Object传给Button::new)
      stream = labels.stream().map(Button::new);
      Object[] buttons2 = stream.toArray();
      System.out.println(buttons2.getClass());

      // The following generates a ClassCastException
      // stream = labels.stream().map(Button::new);
      // Button[] buttons3 = (Button[]) stream.toArray();
      
      // stream存储的是Button[]
      //    如果向上面那样调用stream.toArray()、返回的是Object[]
      //    如果调用另一个方法A[] toArray(IntFunction<A[]> generator)，可以返回泛型数组A[]
      stream = labels.stream().map(Button::new);
      // Button[]::new 等价于 n -> new Button[n]
      // 可以提供给Stream的A[] toArray(IntFunction<A[]> generator)方法
      // 参考：https://kapeli.com/dash_share?docset_file=Java&docset_name=Java%20SE8&path=java/util/stream/Stream.html%23toArray-java.util.function.IntFunction-&platform=java&repo=Main&version=SE8 
      Button[] buttons4 = stream.toArray(Button[]::new); // 等价于n -> new Button[n]

      final double rem = Font.getDefault().getSize();
      HBox box = new HBox(0.8 * rem); 
      box.getChildren().addAll(buttons4);
      stage.setScene(new Scene(box));
      stage.show();      
   }
}
