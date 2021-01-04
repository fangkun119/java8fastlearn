import java.util.Arrays;
import java.util.List;

/**
* 默认方法：
* 1. Java 8开始，可以给接口添加默认方法(default method)
*    是为了满足诸为Collection添加forEach方法、同时有不用修改各种实现Collection接口的容器类这样的需求
* 2. 默认方法的出现，使得Collection/AbstractCollection, WindowListener/WindowAdaptor这样的经典模式不再想之前那样那么需要
*    不再需要一个抽象类来实现大部分或全部代码，也可以实现在接口中
* 3. 命名冲突时应当如何处理？
*    父类、与接口出现同名函数：使用父类的函数
*    两个接口出现同名函数（不论是不是默认函数）：都会编译期报错，需要由实现类提供该函数的代码
*/

interface Person {
   long getId();
   // 接口默认函数getName
   default String getName() { return "Person " + getId(); }
}

interface Persistent {
   // 接口默认函数getName
   default String getName() { return getClass().getName() + "_" + hashCode(); }
}

class Student implements Person, Persistent {
   // 发生了函数命名冲突，冲突来自两个接口，由实现类提供该函数的代码
   // Person.super.getName()表示调用接口Person的默认函数getName()
   public String getName() {
      return Person.super.getName();
   }

   protected int id;

   public Student(int id) {
      this.id = id;
   }

   public long getId() {
      return this.id;
   }
}

public class DefaultMethods {
   public static void main(String[] args) {
      List<Student> students = Arrays.asList(
              new Student(1), new Student(2), new Student(3)
      );
      students.forEach(stu -> System.out.println(stu.getName()));
      // 输出
      // Person 1
      // Person 2
      // Person 3
   }
}
