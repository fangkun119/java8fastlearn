import java.util.Objects;

/**
 * Java 7 提供API，用来简化编写equals, hashCode, compareTo的代码
 */

public class Person {
    private String first;
    private String last;

    public boolean equals(Object otherObject) {
        // 先对顶部的对象进行null值判断、类型判断
        if (this == otherObject) {
           return true;
        }
        if (otherObject == null || this.getClass() != otherObject.getClass()) {
           return false;
        }
        Person other = (Person) otherObject;

        // Java 6的老式写法
        /*
        boolean isFirstEqual = false;
        if (null == this.first) {
            isFirstEqual = (null == other.first);
        } else {
            isFirstEqual = this.first.equals(other.first);
        }
        boolean isSecondEqual =
        ...
        return isFirstEqual && isSecondEqual;
         */

        // Java 7提供Objects.equals方法，来简化上面代码的代码
        // 对于Object.equals(a, b)
        // * a,b都是null时返回true
        // * 只有a是null时返回false
        // * 其他情况返回a.equals(b)
        return Objects.equals(first, other.first) && Objects.equals(last, other.last);
    }

    public int hashCode() {
        // Java 7提供方法，
        // 替代诸如 31*Object.hashCode(first) + Object.hashCode(second)这样的代码
        return Objects.hash(first, last);
    }
}
