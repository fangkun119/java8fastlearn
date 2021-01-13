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
      // 1. Java 8开始，7种包装类（不包括Boolean）都提供了一个BYTES字段，以byte为单位表示该类型的长度
      System.out.printf("Integer   SIZE: %d BYTES: %d\n", Integer.SIZE  , Integer.BYTES  );
      System.out.printf("Long      SIZE: %d BYTES: %d\n", Long.SIZE     , Long.BYTES     );
      System.out.printf("Double    SIZE: %d BYTES: %d\n", Double.SIZE   , Double.BYTES   );
      System.out.printf("Float     SIZE: %d BYTES: %d\n", Float.SIZE    , Float.BYTES    );
      System.out.printf("Short     SIZE: %d BYTES: %d\n", Short.SIZE    , Short.BYTES    );
      System.out.printf("Byte      SIZE: %d BYTES: %d\n", Byte.SIZE     , Byte.BYTES     );
      System.out.printf("Character SIZE: %d BYTES: %d\n", Character.SIZE, Character.BYTES);
      // Integer   SIZE: 32 BYTES: 4
      // Long      SIZE: 64 BYTES: 8
      // Double    SIZE: 64 BYTES: 8
      // Float     SIZE: 32 BYTES: 4
      // Short     SIZE: 16 BYTES: 2
      // Byte      SIZE: 8 BYTES: 1
      // Character SIZE: 16 BYTES: 2

      // 2. Java 8开始，8种包装类，都提供了一个静态函数版本的hashCode方法，用于省去拆箱装箱的过程
      System.out.println(Integer.hashCode(1));     // 1
      System.out.println(Long.hashCode(1));        // 1
      System.out.println(Double.hashCode(0.25));   // 1070596096
      System.out.println(Float.hashCode(0.25f));   // 1048576000
      System.out.println(Short.hashCode((short)1));      // 1
      System.out.println(Byte.hashCode((byte)1));        // 1
      System.out.println(Character.hashCode('c')); // 99
      System.out.println(Boolean.hashCode(true));  // 1231
      
      // 3. 用于Stream聚合函数的静态方法
      // java8 为Short, Integer, Long, Float, Double这5种类型提供了静态方法sum、max、min
      Random generator = new Random();
      OptionalInt sum = generator.ints().limit(100).reduce(Integer::sum);
      OptionalInt min = generator.ints().limit(100).reduce(Integer::min);
      System.out.println(sum); // OptionalInt[-15423225]
      System.out.println(min); // OptionalInt[-2125555369]
      // java8 为Boolean提供了logicalAnd, logicalOr, logicalXor方法
      Optional<Boolean> xor = generator.ints().limit(100).mapToObj(i -> i % 2 == 0).reduce(Boolean::logicalXor);
      System.out.println(xor); // Optional[false]

      // 4. Java 8提供API，把有符号数当做无符号数来运算

      // 对于Long和Integer，compareUnsigned, divideUnsigned, remainderUnsigned都可以把他们当做无符号数来运算
      // 例如下面两个int
      int maxValue            = Integer.MAX_VALUE;  // 2147483647
      int nextValue           = maxValue + 1;       // -2147483648 (整形数溢出）
      System.out.printf("maxValue: %d, nextValue: %d\n", maxValue, nextValue);
      // 运算结果如下
      int maxCmpNext          = Integer.compareUnsigned(maxValue, nextValue);        // -1
      int nextCmpMax          = Integer.compareUnsigned(nextValue,maxValue );        // 1
      int nextDivide65536     = Integer.divideUnsigned(nextValue, 65536);     // 32768
      int nextRemainder65536  = Integer.remainderUnsigned(nextValue,  65536); // 0
      System.out.printf("maxCmpNext:%d; nextCmpNext:%d; nextDivide65536:%d; nextRemainder65536:%d\n"
              , maxCmpNext, nextCmpMax, nextDivide65536, nextRemainder65536);
      // maxCmpNext:-1; nextCmpNext:1; nextDivide65536:32768; nextRemainder65536:0

      // Byte，Short，Integer新增了toUnsignedInt方法；而Integer还拥有toUnsignedLong方法
      byte b = -127;
      int bi = Byte.toUnsignedInt(b); //  129
      System.out.println(bi);

      long l = Integer.toUnsignedLong(Integer.MAX_VALUE + 1);  //2147483648
      System.out.println(l);

      // 5. 浮点数finite检查
      // Float, Double新增了静态方法isFinite：表示不是无穷大、无穷小、也不是Nan
      System.out.println(Double.isFinite(1.0 / 0.0));    // false
      System.out.println(Double.isFinite(Math.sqrt(-1.0))); // false

      // 5. BigInteger增加了实例方法(long|int|short|byte)ValueExact四个方法，
      // 用来返回long、int、short、byte，并且当值不在目标范围内时会抛出一个ArithmeticException异常
      int intExact = new BigInteger("129").intValueExact();       // 129
      long longExact = new BigInteger("129").longValueExact();    // 129
      short shortExact = new BigInteger("129").shortValueExact(); // 129
      System.out.println(intExact);
      System.out.println(longExact);
      System.out.println(shortExact);
      byte byteExact = new BigInteger("129").byteValueExact();
      // Exception in thread "main" java.lang.ArithmeticException: BigInteger out of byte range
      //	at java.base/java.math.BigInteger.byteValueExact(BigInteger.java:4846)
      //	at Numbers.main(Numbers.java:70)
   }
}
