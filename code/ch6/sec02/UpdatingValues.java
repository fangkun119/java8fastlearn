import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class UpdatingValues {
   public static void main(String[] args) {

      String word = "";
      ConcurrentHashMap<String, Long> concurrentHashMap = new ConcurrentHashMap<>();

      // (1) 线程不安全的代码（get，put）
      // 虽然ConcurrentHashMap保证了get和put不会因为并发操作而破坏HashMap的数据结构
      // 但是get和put并没有放到一个原子操作中
      Long oldValue = concurrentHashMap.get(word);
      Long newValue = oldValue == null ? 1 : oldValue + 1;    
      concurrentHashMap.put(word, newValue); // Error—might not replace oldValue

      // (2) 使用基于乐观锁的方法replace来更新
      do {
         oldValue = concurrentHashMap.get(word);
         newValue = oldValue == null ? 1 : oldValue + 1;
      } while (!concurrentHashMap.replace(word, oldValue, newValue));

      // (3) 用LongAdder的原子属性来确保线程安全
      ConcurrentHashMap<String, LongAdder> map2 = new ConcurrentHashMap<>();
      map2.putIfAbsent(word, new LongAdder()); // 保证已经存在了一个LongAdder
      map2.get(word).increment();              // 调用LongAdder的原子自增
      // 上面两行代码可以合并成一个
      map2.putIfAbsent(word, new LongAdder()).increment();

      // (4) 传入lambda表达式并原子执行：compute
      // lambda表达式封装操作的内容、及value初始值设置以及更新
      // compute、computeIfAbsent, computeIfPresent方法保证该操作被原子执行
      concurrentHashMap.compute(word, (k, v) -> v == null ? 1 : v + 1);
      map2.computeIfAbsent(word, k -> new LongAdder()).increment();
      map2.computeIfPresent(word, (k,v) -> v).increment();

      // (5) 传入lambda表达式并原子执行：merge
      // 与compute类似，但是lambda表达式只需要提供更新操作，而value初始值可以通过另一个参数传入
      concurrentHashMap.merge(
              word,        // key
              1L,    // value缺失时的hash初始值
              (existingValue, newValue2) -> existingValue + newValue2 // value更新操作
      );
      concurrentHashMap.merge(word, 1L, Long::sum);
   }
}
