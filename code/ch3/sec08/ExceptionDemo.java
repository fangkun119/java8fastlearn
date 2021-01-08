import java.util.function.Consumer;

public class ExceptionDemo {
    public static void doInOrder(
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

    public static void notInOrder(Runnable first, Runnable second) {
        Thread t = new Thread() {
            public void run() {
                first.run();
                second.run();
            }
        };
        t.start();
    }

    public static void main(String[] args) {
        notInOrder(
            ()  -> System.out.println(args[0]), // 会抛ArrayIndexOutOfBoundsException
            ()  -> System.out.println("Goodbye")
        );

        /*
        doInOrder(
            ()  -> System.out.println(args[0]), //会抛ArrayIndexOutOfBoundsException
            ()  -> System.out.println("Goodbye") ,
            (e) -> System.out.println("Yikes: " + e)
        );
        */
    }
}
