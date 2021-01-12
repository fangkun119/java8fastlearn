import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class AtomicUpdates2 {
    // 1000个异步运行的Task提交给线程池，每个Task执行1000轮循环
    public static int ntasks = 1000;
    public static int iterations = 1000;

    // 2个AtomicLong
    // * nextNumber是一个递增计数器，
    public static AtomicLong nextNumber = new AtomicLong(ntasks * iterations / 2);
    public static AtomicLong largest = new AtomicLong();

    public static void main(String[] args) throws InterruptedException {
        // 线程池
        ExecutorService pool = Executors.newCachedThreadPool();
        // 提交1000个Task给线程池
        for (int t = 0; t < ntasks; t++)
            pool.submit(() -> {
                // 1000个Task的起始计数值依次是 0 → 999
                long start = nextNumber.incrementAndGet();
                // 循环1000次
                for (int i = 0; i < iterations; i++) {
                    // 只看循环终止时各变量的值
                    // * i = 999
                    // * iteration = 1000
                    // * start 依次是 0 → 999
                    // 所以此时 observed 的值依次是 999001、999002、→、999999
                    long observed = (start + ntasks * i) % (ntasks * iterations);
                    // 正确的使用方法，使用CAS来进行并发检查
                    long oldValue, newValue;
                    do {
                        oldValue = largest.get();
                        newValue = Math.max(oldValue, observed);
                        // 如果执行CAS时largest的值变了导致结果会不准确，CAS会返回false并在下一轮循环中重试
                    } while (!largest.compareAndSet(oldValue, newValue));
                }
            });
        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.SECONDS);
        System.out.println(largest);
    }
}
