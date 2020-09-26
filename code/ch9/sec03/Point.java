/**
* Java 7 提供API，用来简化编写equals, hashCode, compareTo的代码
*/

public class Point {
   private int x, y;
   public int compareTo(Point other) {
      // 使用Java 7提供的方法，可以避免直接相减带来的溢出问题
      // 除了Integer
      // Long、Short、Byte、Boolean都提供了各自的静态方法compare
      int diff = Integer.compare(x, other.x); // No risk of overflow
      if (diff != 0) return diff;
      return Integer.compare(y, other.y);
   }
}
