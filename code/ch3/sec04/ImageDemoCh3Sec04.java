import java.util.function.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.stage.*;

public class ImageDemoCh3Sec04 extends Application {
   // 使用java8自带的函数式接口
   public static Image transform(Image in, UnaryOperator<Color> f /*java自带的函数式接口*/) {
      int width = (int) in.getWidth();
      int height = (int) in.getHeight();
      WritableImage out = new WritableImage(width, height);
      for (int x = 0; x < width; x++) {
         for (int y = 0; y < height; y++) {
            // 调用函数式接口的apply方法
            out.getPixelWriter().setColor(x, y, f.apply(in.getPixelReader().getColor(x, y)));
         }
      }
      return out;
   }

   // 把函数用作返回值
   public static UnaryOperator<Color> brighten(double factor) {
      // lambda表达式会编译成一个UnaryOperator<Color>对象返回
      return c -> c.deriveColor(0, 1, factor, 1); 
   }

   public void start(Stage stage) {
      Image image = new Image("queen-mary.png");
      // 把brighten返回的"函数"、传给transform
      Image brightenedImage = transform(image, brighten(1.2));
      stage.setScene(new Scene(new HBox(new ImageView(image), new ImageView(brightenedImage))));
      stage.show();
   }
}
