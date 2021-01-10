import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExceptionDemo2 {
    // 如Supplier<T>的接口代码所示，API要求传入的参数不会抛出checked exception
    // @FunctionalInterface public interface Supplier<T> { T get();}
    public static <T> void api(Supplier<T> runnable, Consumer<Throwable> handler) {
        try {
            System.out.println(runnable.get());
        } catch (Throwable e) {
            handler.accept(e);
        }
    }

    // 将会抛出checked exception的Callable<T>
    // 转换为不会抛checked exception的Supplier<T>，它只会抛un-checked exception
    public static <T> Supplier<T> unchecked(Callable<T> f) {
        return () -> {
            try {
                // 调用传入的会抛checked Exception的函数
                return f.call();
            } catch (Exception e) {
                // 将checked exception转换成un-checked exception后抛出
                throw new RuntimeException(e);
            } catch (Throwable t) {
                throw t;
            }
        };
    }

    public static void main(String[] args) {
        // 不能兼容API的lambda表达式
        Callable<String> withCheckedException = () -> new String(Files.readAllBytes(
            Paths.get("/etc/not_existed")), StandardCharsets.UTF_8);

        // 用上面写好的方法转换成兼容API的lambda
        Consumer<Throwable> unCheckedExceptionHandler = e -> System.out.println(e);
        api(unchecked(withCheckedException), unCheckedExceptionHandler);
        // 输出：java.lang.RuntimeException: java.nio.file.NoSuchFileException: /etc/not_existed
    }
}

