import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ExceptionDemo {
    public static void doInOrder(Runnable first, Runnable second) {
        first.run();
        second.run();
    }

    public static void doInOrderAsync1(
            Runnable first, Runnable second, Consumer<Throwable> handler) {
        Thread t = new Thread() {
            public void run() {
                try {
                    first.run();
                    second.run();
                } catch (Throwable t) {
                    handler.accept(t);
                }
            }
        };
        t.start();
    }

    public static <T> void doInOrderAsync2(
            Supplier<T> first, Consumer<T> second, Consumer<Throwable> handler) {
        Thread t = new Thread() {
            public void run() {
                try {
                    T firstRet = first.get();
                    second.accept(firstRet);
                } catch (Throwable e) {
                    handler.accept(e);
                }
            }
        };
        t.start();
    }

    public static <T> void doInOrderAsync3 (
            Supplier<T> first, BiConsumer<T, Throwable> second) {
        Thread t = new Thread() {
            public void run() {
                T firstRet = null;
                Throwable exception = null;
                try {
                    firstRet = first.get();
                } catch (Throwable e) {
                    exception = e;
                } finally {
                    second.accept(firstRet, exception);
                }
            }
        };
        t.start();
    }

    public static void main(String[] args) {
        try {
            doInOrder(
                    () -> System.out.println(args[0]),   // 会抛异常
                    () -> System.out.println("won't be executed")
            );
        } catch (Exception e) {
            System.out.println("exception: " + e);
        }
        // 输出： exception: java.lang.ArrayIndexOutOfBoundsException: 0

        doInOrderAsync1(
            ()  -> System.out.println(args[0]),          // 会抛异常
            ()  -> System.out.println("won't be executed") ,
            (e) -> System.out.println("exception: " + e) // Exception Handler
        );
        // 输出：exception: java.lang.ArrayIndexOutOfBoundsException: 0

        doInOrderAsync2(
            ()  -> args[0],                              // 会抛异常
            (r) -> System.out.println(r) ,
            (e) -> System.out.println("exception: " + e) // Exception Handler
        );
        // 输出：exception: java.lang.ArrayIndexOutOfBoundsException: 0

        doInOrderAsync3(
                () -> args[0],                           // 会抛异常
                (result, exception) -> System.out.println(
                        String.format("result %s; exception: %s", result, exception))
        );
        // 输出：result null; exception: java.lang.ArrayIndexOutOfBoundsException: 0



    }
}
