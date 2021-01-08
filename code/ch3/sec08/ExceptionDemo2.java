import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class ExceptionDemo2 {

    public static <T> Supplier<T> unchecked(Callable<T> f) {
        return () -> {
            try {
                // 调用传入的函数，如果它抛出异常，直接交给调用它的代码来处理
                return f.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            } catch (Throwable t) {
                throw t;
            }
        };
    }

    public static void main(String[] args) {
        // 不会抛出异常的代码
        Supplier<String> s1 = unchecked(
                () -> new String(Files.readAllBytes(Paths.get("/etc/passwd")), StandardCharsets.UTF_8));
        System.out.println(s1.get());

        // 会抛出异常的代码：
        // 异常抛出路径：lambda表达式 -> unchecked方法 -> main方法
        Supplier<String> s2 = unchecked(
                () -> new String(Files.readAllBytes(Paths.get("/etc/not_existed")), StandardCharsets.UTF_8));
        System.out.println(s2.get());
    }
}

