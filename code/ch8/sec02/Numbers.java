import java.math.*;
import java.util.*;

/**
* 1. Java 5开始，7种原始类型的包装类（不包含Boolean）都提供静态字段SIZE表示以bit为单位的长度；
*    Java 8开始，他们都提供了一个BYTES字段，以byte为单位表示该类型的长度
* 2. Java 8开始，8种包装类，都提供了一个静态函数版本的hashCode方法，用于省去拆箱装箱的过程
* 3. Java 8为Short, Integer, Long, Float, Double这5种类型提供了静态方法sum、max、min用于在stream操作中作为聚合函数使用
*    也为Boolean提供了logicalAnd, logicalOr, logicalXor方法
* 4. 以往的java中，Integer，Byte，…… 都表示有符号值的数据范围，只能当做有符号值来计算
*    Java 8为这些包装类提供了方法以支持无符号计算，即把他们的值当成无符号数（丢失符号并获得原来两倍范围的整数）进行计算
*    * Integer, Long新增了compareUnsigned, divideUnsigned, remainderUnsigned方法Integer.compareUnsigned
*    * Byte，Short，Integer新增了toUnsignedInt方法；而Integer还拥有toUnsignedLong方法
* 5. Float, Double新增了静态方法isFinite：表示不是无穷大、无穷小、也不是Nan
* 6. BigInteger增加了实例方法(long|int|short|byte)ValueExtract四个方法，
*    用来返回long、int、short、byte，并且当值不在目标范围内时会抛出一个ArithmeticException异常
*/

public class Numbers {
   public static void main(String[] args) {
      // Java 8开始，7种包装类（不包括Boolean）都提供了一个BYTES字段，以byte为单位表示该类型的长度
      System.out.printf("Integer   SIZE: %d BYTES: %d\n", Integer.SIZE  , Integer.BYTES  );
      System.out.printf("Long      SIZE: %d BYTES: %d\n", Long.SIZE     , Long.BYTES     );
      System.out.printf("Double    SIZE: %d BYTES: %d\n", Double.SIZE   , Double.BYTES   );
      System.out.printf("Float     SIZE: %d BYTES: %d\n", Float.SIZE    , Float.BYTES    );
      System.out.printf("Short     SIZE: %d BYTES: %d\n", Short.SIZE    , Short.BYTES    );
      System.out.printf("Byte      SIZE: %d BYTES: %d\n", Byte.SIZE     , Byte.BYTES     );
      System.out.printf("Character SIZE: %d BYTES: %d\n", Character.SIZE, Character.BYTES);

      // Java 8开始，8种包装类，都提供了一个静态函数版本的hashCode方法，用于省去拆箱装箱的过程
      System.out.println(Integer.hashCode(1));
      System.out.println(Long.hashCode(1));
      System.out.println(Double.hashCode(0.25));      
      System.out.println(Float.hashCode(0.25f));
      System.out.println(Short.hashCode((short)1));
      System.out.println(Byte.hashCode((byte)1));
      System.out.println(Character.hashCode('c'));
      System.out.println(Boolean.hashCode(true));
      
      // Java 8为Short, Integer, Long, Float, Double这5种类型提供了静态方法sum、max、min用于在stream操作中作为聚合函数使用
      // 也为Boolean提供了logicalAnd, logicalOr, logicalXor方法
      Random generator = new Random();
      System.out.println(generator.ints().limit(100).reduce(Integer::sum));
      System.out.println(generator.ints().limit(100).reduce(Integer::min));
      System.out.println(generator.ints().limit(100).mapToObj(i -> i % 2 == 0).reduce(Boolean::logicalXor));   

      // 以往的java中，Integer，Byte，…… 都表示有符号值的数据范围，只能当做有符号值来计算
      // Java 8为这些包装类提供了方法以支持无符号计算，即把他们的值当成无符号数（丢失符号并获得原来两倍范围的整数）进行计算
      int maxValue  = Integer.MAX_VALUE;
      int nextValue = maxValue + 1;

      // * Integer, Long新增了compareUnsigned, divideUnsigned, remainderUnsigned方法Integer.compareUnsigned
      System.out.printf("maxValue: %d, nextValue: %d, compared: %d\n",
         maxValue, nextValue, Integer.compareUnsigned(maxValue, nextValue));
      //    maxValue: 2147483647, nextValue: -2147483648, compared: -1
      System.out.printf("nextValue / 65536 as unsigned: %d\n", Integer.divideUnsigned(nextValue, 65536));
      //    nextValue / 65536 as unsigned: 32768

      // * Byte，Short，Integer新增了toUnsignedInt方法；而Integer还拥有toUnsignedLong方法
      byte b = -127; 
      System.out.println(Byte.toUnsignedInt(b));
      //    129

      // Float, Double新增了静态方法isFinite：表示不是无穷大、无穷小、也不是Nan
      System.out.println(Double.isFinite(1.0 / 0.0));
      System.out.println(Double.isFinite(Math.sqrt(-1.0)));

      // BigInteger增加了实例方法(long|int|short|byte)ValueExtract四个方法，
      // 用来返回long、int、short、byte，并且当值不在目标范围内时会抛出一个ArithmeticException异常
      b = new BigInteger("129").byteValueExact();
   }
}
