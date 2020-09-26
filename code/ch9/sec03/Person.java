import java.util.*;

/**
* Java 7 提供API，用来简化编写equals, hashCode, compareTo的代码
*/

public class Person {
   private String first;
   private String last;
   
   public boolean equals(Object otherObject) {
      // Java 6的老式写法
      if (this == otherObject) return true;
      if (otherObject == null) return false;
      if (getClass() != otherObject.getClass()) return false;
      Person other = (Person) otherObject;
      
      // Java 7提供Objects.equals方法，来简化上面代码的代码
      // 对于Object.equals(a, b)
      // * a,b都是null时返回true
      // * 只有a是null时返回false
      // * 其他情况返回a.equals(b)
      return Objects.equals(first, other.first) && Objects.equals(last, other.last);
   }

   public int hashCode() {
      // Java 7提供方法，替代诸如 31*Object.hashCode(first) + Object.hashCode(second)这样的代码
      return Objects.hash(first, last);
   }
}
