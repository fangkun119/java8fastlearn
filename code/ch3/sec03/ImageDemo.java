import java.util.function.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.stage.*;


public class ImageDemo extends Application {
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

   // 自定义函数式接口
   @FunctionalInterface
   interface ColorTransformer {
      Color apply(int x, int y, Color colorAtXY);
   }

   // 使用自定义的函数式接口
   public static Image transform(Image in, ColorTransformer f /*自定义函数式接口*/ ) {
      int width = (int) in.getWidth();
      int height = (int) in.getHeight();
      WritableImage out = new WritableImage(width, height);
      for (int x = 0; x < width; x++) {
         for (int y = 0; y < height; y++) {
            // 调用函数式接口的apply方法
            out.getPixelWriter().setColor(x, y, f.apply(x, y, in.getPixelReader().getColor(x, y)));
         }
      }
      return out;
   }

   public void start(Stage stage) {
      Image image = new Image("queen-mary.png");

      // 传入符合函数式接口 UnaryOperator<Color> 的 lambda表达式
      Image brightenedImage = transform(image, Color::brighter);
      // 传入符合函数式接口 ColorTransformer 的 lambda表达式
      Image image2 = transform(image, (x, y, c) -> (x / 10) % 2 == (y / 10) % 2 ? Color.GRAY : c);
      
      stage.setScene(new Scene(new HBox(new ImageView(image), new ImageView(brightenedImage), new ImageView(image2))));
      stage.show();
   }
}
