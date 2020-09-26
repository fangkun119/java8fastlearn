import java.util.*;

/**
* 1. Java 8提供Math.{add/substract/multiply/increment/decrement/negate}Exact方法
*    发生溢出时会抛异常，函数名中的“Exact”用来标识这个函数会检查溢出
* 2. Java 8提供的带“Exact”标识的类型转换函数，例如toIntExact, 同样会检查类型转换后是否发生异常
* 3. Java 8提供的floorMod能够解决取模时，当除数为负数时，得到的模为负数的问题（严格讲，floorMod仍然存在返回负数的可能，但是实际中很少发生）
*    * 模（即余数）应当永远大于0，
*    * 但是计算机诞生时，先驱们并不知道这条数学规则，导致了余数正负的不确定性
* 4. Java 8提供nextDown方法，返回比指定值小但是最接近的浮点数（与Java 6的Math.nextUp相对应）
*/

public class MathematicalFunctions {
   public static void main(String[] args) {
      // 直接相乘，整数溢出时，会得到一个错误的结果
      System.out.println(100000 * 100000);
      
      // Java 8提供Math.{add/substract/multiply/increment/decrement/negate}Exact方法
      // 发生溢出时会抛异常，函数名中的“Exact”用来标识这个函数会检查溢出
      try {
         System.out.println(Math.multiplyExact(100000, 100000));
      } catch (ArithmeticException ex) {
         ex.printStackTrace();
      }

      // Java 8提供的带“Exact”标识的类型转换函数，例如toIntExact, 同样会检查类型转换后是否发生异常
      long product = 100000L * 100000L;
      System.out.println(product);
      try {
         int n = Math.toIntExact(product);
      } catch (ArithmeticException ex) {
         ex.printStackTrace();
      }

      // Java 8提供的floorMod能够解决取模时，当除数为负数时，得到的模为负数的问题（严格讲，floorMod仍然存在返回负数的可能，但是实际中很少发生）
      // * 模（即余数）应当永远大于0，
      // * 但是计算机诞生时，先驱们并不知道这条数学规则，导致了余数正负的不确定性
      System.out.println(Math.floorMod(6 + 10, 12)); // Ten hours later
      System.out.println(Math.floorMod(6 - 10, 12)); // Ten hours earlier
      System.out.println(Math.floorMod(6 + 10, -12));
      System.out.println(Math.floorMod(6 + 10, -12));

      // Java 8提供nextDown方法，返回比指定值小但是最接近的浮点数（与Java 6的Math.nextUp相对应）
      Random generator = new Random(164311266871034L); 
         // Also try new Random(881498)
      for (int tries = 1; tries < 1000000000; tries++) {
         double r = 1.0 - generator.nextDouble();
         if (r == 1.0) {
            System.out.println("It happened at try " + tries);
            r = Math.nextDown(r);
            System.out.println(r);                       
         }
      }

      /*
        Isn't that awesome? After just two tries, we happened to get 
        a floating-point value of 0. 

        Of course, it didn't just happen. Here is how to get the seed.
        The random number generator computes 
           next(s) = s * m + a % N
        where m = 25214903917, a = 11, and N = 2^48. 
        The inverse of m mod N is v = 246154705703781, and therefore 
           prev(s) = (s - a) * v mod N
        To make a double, next is called twice, and the top 26 and 27 
        bit are taken each time. When s is 0, next(s) is 11, so that's 
        what we want to hit: two consecutive numbers whose top bits are
        zero. Now, working backwards, we start with
           s = prev(prev(prev(0))
        Almost there--the Random constructor sets 
           s = (initialSeed ^ m) % N,
        so we need to offer it
           s = prev(prev(prev(0)) ^ m = 164311266871034
      */
   }
}
