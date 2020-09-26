import java.time.*;

/**
* Java 8为String增加了静态方法String.join(sep, ...)，即通过一个分隔符将一组字符串拼接起来
* 被拼接的字符串可以用三种方式传入：
* 1. 可变参数列表
* 2. 数组
* 3. Iterable<? extends CharSequences>对象，例如ZoneId.getAvailableZoneIds()返回的Set<String>
*/

public class Strings {
   public static void main(String[] args) {
      // 拼接可变参数列表中的字符串
      String joined = String.join("/", "usr", "local", "bin"); // "usr/local/bin"
      System.out.println(joined);

      // 拼接Set<String>中的字符串
      String ids = String.join(", ", ZoneId.getAvailableZoneIds());
      System.out.println(ids);
   }
}
