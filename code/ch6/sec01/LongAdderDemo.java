import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class LongAdderDemo {
   private static void await(CyclicBarrier barrier) {
      try {
         barrier.await();
      } catch (InterruptedException | BrokenBarrierException ex) {
         ex.printStackTrace();
         // Won't happen in this application
      }
   }

   public static double run(int nthreads, int iterations, Runnable action) {      
      Thread[] threads = new Thread[nthreads];      
      CyclicBarrier barrier = new CyclicBarrier(nthreads + 1);

      for (int t = 0; t < nthreads; t++) {
         threads[t] = new Thread(() -> {
               await(barrier);
               for (int i = 0;  i < iterations; i++) {
                  action.run();
               }
               await(barrier);
            });
         threads[t].start();
      }
      await(barrier);
      long start = System.nanoTime();
      await(barrier);
      long end = System.nanoTime();
      return (end - start) * 1E-9;
   }

   public static void main(String[] args) {
      // 100个线程，每个线程循环1000000次
      final int THREADS = 100;
      final int ITERATIONS = 1000000;
      final String LOG_SEP = "\n---------------------\n";

      // 1. 使用synchronized的版本
      System.out.println(LOG_SEP + "Synchronized");
      // (1) 用synchronized保护的计数器
      class Counter {
         private long count;
         synchronized void increment() { count++; }
         synchronized long get() { return count; }
      };
      Counter counter = new Counter();
      // (2) 启动100个线程，并返回计算耗时
      double elapsedTime = run(THREADS, ITERATIONS, () -> {
            counter.increment(); //用synchronized保护的1000000次累加操作
         });
      // (3) 输出
      System.out.println(counter.get());            // 计数器的值
      System.out.println(elapsedTime + " seconds"); // 计算耗时
      // ---------------------
      // Synchronized
      // 100000000
      // 5.170585531 seconds

      // 2. 使用AtomicLong的版本
      System.out.println(LOG_SEP + "AtomicLong");
      // (1) AtomicLong
      AtomicLong atomic = new AtomicLong();
      // (2) 启动100个线程，并返回计算耗时
      elapsedTime = run(THREADS, ITERATIONS, () -> {
            atomic.incrementAndGet(); // 1000000次incrementAndGet操作
         });
      // (3) 输出结果
      System.out.println(atomic.get());             // 原子变量的值
      System.out.println(elapsedTime + " seconds"); // 计算耗时
      // ---------------------
      // AtomicLong
      // 100000000
      // 1.694410524 seconds


      // 3. 使用LongAdder的版本
      System.out.println(LOG_SEP + "LongAdder");
      // (1) LongAdder
      LongAdder adder = new LongAdder();
      // (2) 启动100个线程，并返回计算耗时
      elapsedTime = run(THREADS, ITERATIONS, () -> {
            adder.increment(); // 让LongAdder执行1000000次increase加操作
         });
      // (3) 输出结果
      System.out.println(adder.sum());              // 累加和
      System.out.println(elapsedTime + " seconds"); // 计算耗时
      // ---------------------
      // LongAdder
      // 100000000
      // 0.45605228800000003 seconds


      // 4. 不做同步保护
      System.out.println(LOG_SEP + "Unsynchronized");
      long[] badCounter = new long[1];
      elapsedTime = run(THREADS, ITERATIONS, () -> {
            badCounter[0]++;
         });
      System.out.println(badCounter[0]);
      System.out.println(elapsedTime + " seconds");
      // ---------------------
      // Unsynchronized
      // 33282414 // 加出来的值时错的
      // 1.077450035 seconds


      // 5. 使用LongAccumulator的版本
      System.out.println(LOG_SEP + "LongAdder");
      // (1) LongAccumulator
      LongAccumulator accumulator = new LongAccumulator(Long::sum, 0);
      // (2) 启动100个线程，并返回计算耗时
      elapsedTime = run(THREADS, ITERATIONS, () -> {
         accumulator.accumulate(1); // 让LongAdder执行1000000次加1操作
      });
      // (3) 输出结果
      System.out.println(accumulator.get());        // 累加和
      System.out.println(elapsedTime + " seconds"); // 计算耗时
      // ---------------------
      // LongAdder
      // 100000000
      // 0.45605228800000003 seconds
   }
}
