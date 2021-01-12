import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class AtomicUpdates3 {
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
        for (int t = 0; t < ntasks; t++) {
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
                    // 更新原子变量
                    // 方法1： largest.updateAndGet(x -> Math.max(x, observed));
                    // 方法2： largest.accumulateAndGet(observed, Math::max);
                    largest.accumulateAndGet(observed, Math::max);
                }
            });
        }
        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.SECONDS);
        System.out.println(largest);
    }
}
