/**
* 方法引用：直接向调用方的函数传递方法名称，来作为lambda表达式的简写，有如下几种简写语法：
* 
* 对象::实例方法，例如
*   button.setOnAction(System.out::println) 
*   等价于 button.setOnAction(x -> System.out.println(x))
*
* 类::静态方法，例如
*   Math::pow 等价于 (x,y) -> Math::pow(x,y)
*
* 类::实例方法，例如
*   String::compareToIgnoreCase 等价于 (x,y) -> x.compareToIgnoreCase(y)
*
* 还可以使用this和super对象，例如
*   this::equals 等同于 x -> this.equals(x)
*   super::great 等同于 x -> super.great(x)
*/

public class SuperTest {
   public static void main(String[] args) {
      class Greeter {
         public void greet() { 
            System.out.println("Hello, world!"); 
         }
      }

      class ConcurrentGreeter extends Greeter {
         public void greet() { 
            // super::great 等同于 x -> super.great(x)
            Thread t = new Thread(super::greet);
            t.start();
         }
      }

      new ConcurrentGreeter().greet();
   }   
}
