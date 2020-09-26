/**
* 接口中的静态方法：
* 1. 从Java 8开始，可以为接口添加静态函数
*    这样在编写新的类库时，诸如Collection/Collections, Path/Paths这样的成对的接口+静态函数封装类就不再需要了
*    这些静态函数可以直接编写在接口中
* 2. 在Java 7的时候，有一种办法可以walk around
*    例如：
*       Collections类提供shuffle(List<?> List)静态方法
*       List提供成员方法shuffle()，该方法调用Collection的静态方法
*    然而这个walk around并不能用在工厂方法上
* 3. Java8的Comparator接口提供了一个非常方便的静态方法Comparator.comparing(..)
*    例如：
*       Comparator.comparing(String::length) 
*       等价于使用lambda表达式
*       (first, second) -> Integer.compare(first.length(), second.length())
*       生成的Comparator对象
*/

public class StaticInterfaceMethods {
   public static void main(String[] args) {
      // 调用接口静态方法
      Greeter worldGreeter = Greeter.newInstance("World");
      System.out.println(worldGreeter.greet());
   }
}

interface Greeter {
   String greet();
   // 提供接口静态方法
   static Greeter newInstance(String greeted) {
      return new Greeter() {
         public String greet() { return "Hello, " + greeted; }
      };
   }
}
