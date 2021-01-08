import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.UnaryOperator;

public class ImageDemoCh3Sec07 extends Application {
    public static Color[][] parallelTransform(Color[][] in, UnaryOperator<Color> f) {
        int n = Runtime.getRuntime().availableProcessors();
        int height = in.length;
        int width = in[0].length;
        Color[][] out = new Color[height][width];
        try {
            // 初始化线程池
            ExecutorService pool = Executors.newCachedThreadPool();
            for (int i = 0; i < n; i++) {
                int fromY = i * height / n;
                int toY   = (i + 1) * height / n;
                // 线程池
                pool.submit(() -> {
                    System.out.printf("%s %d...%d\n", Thread.currentThread(), fromY, toY - 1);
                    for (int x = 0; x < width; x++) {
                        for (int y = fromY; y < toY; y++) {
                            // 执行 UnaryOperator<Color> f
                            out[y][x] = f.apply(in[y][x]);
                        }
                    }
                });
            }
            // 停止新的线程提交，等待已有线程和运行完毕（超时时间1小时）
            pool.shutdown();
            pool.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return out;
    }

    public void start(Stage stage) {
        // 读取图片
        Image image = new Image("eiffel-tower.jpg");

        // 因为JavaFX的PixelWriter不是线程安全的，将Image转换成像素矩阵
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        Color[][] pixels = new Color[height][width];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                pixels[y][x] = image.getPixelReader().getColor(x, y);
            }
        }

        // 让Collor::grayscaleb并行处理像素矩阵
        pixels = parallelTransform(pixels, Color::grayscale);

        // 将像素矩阵转换回Image
        WritableImage result = new WritableImage(width, height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                result.getPixelWriter().setColor(x, y, pixels[y][x]);
            }
        }

        // 显示图片
        stage.setScene(new Scene(new HBox(new ImageView(image), new ImageView(result))));
        stage.show();
    }
}
