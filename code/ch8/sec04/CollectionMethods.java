import java.time.*;
import java.util.*;

/**
* 除了stream、parellelStream、spliterator等方法之外，Java 8向集合类/接口中添加了以下方法
* Iterable    : forEach
* Collection  : removeIf
* List        : replaceAll, sort
* Map         : forEach, replace, replaceAll, remove(key,value) (仅在<key,value>存在时才删除), putIfAbsent, compute, compute If
* Iterator    : forEachRemaining (将迭代器剩余元素都传递给一个函数)
* BitSet      : stream (生成集合中的所有元素，返回一个由int组成的stream）
*/ 
public class CollectionMethods {
   public static void main(String[] args) {
      List<String> ids = new ArrayList<>(ZoneId.getAvailableZoneIds());

      // Collection.removeIf
      ids.removeIf(s -> !s.startsWith("America"));

      // Iterable.forEach
      ids.forEach(System.out::println);

      // List.replaceAll
      ids.replaceAll(s -> s.replace("America/", ""));
      System.out.println("---");
      
      // Iterable.forEach
      ids.forEach(System.out::println);
      BitSet bits = new BitSet();
      ids.forEach(s -> bits.set(s.length()));
      
      // BitSet.stream
      System.out.println(Arrays.toString(bits.stream().toArray()));
   }
}
