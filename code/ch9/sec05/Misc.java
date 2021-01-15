import java.math.BigInteger;
import java.util.BitSet;
import java.util.Objects;
import java.util.logging.Logger;

public class Misc {
    public static void main(String[] args) {
        // 9.5.1 将字符串转换为数字
        double x1 = Double.parseDouble("+1.0");
        // 下面的操作在Java6中有问题，在Java7中修复了
        int      n1 = Integer.parseInt("+1");
        short    s1 = Short.parseShort("+1");
        byte     b1 = Byte.parseByte("+1");
        BigInteger bi1 = new BigInteger("+1");
        int      n2 = Integer.decode("0x10");
        int      n3 = Integer.decode("010");
        Integer nw1 = Integer.valueOf("+1");
        Long    lw1 = Long.valueOf("+1");
        System.out.printf("%f %d %s %d %s %d %d %d %d\n", x1, n1, s1, b1, bi1, n2, n3, nw1, lw1);
        // 输出： 1.000000 1 1 1 1 16 8 1 1


        // 9.5.2 全局Logger
        // Java 7提供了一个简洁又安全的形式来输出全局Logger，它用来解决老版本Java的如下问题：
        // * `Logger.global.finest("x=" + x)` （已经deprecated了）需要初始化、并且容易造成静态初始化代码段死锁
        // * `Logger.getLogger(Logger.GLOBAL_LOGGER_NAME)`（来自Java6）使用起来比较繁琐
        Logger.getGlobal().info("x1=" + 1.0);
        // 输出： 信息: x1=1.0


        // 9.5.3 Null检查
        try {
            new Misc().process(null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // java.lang.NullPointerException: directions must not be null
        // at java.base/java.util.Objects.requireNonNull(Objects.java:233)
        // at Misc.process(Misc.java:45)
        // at Misc.main(Misc.java:25)


        // 9.5.6 BitSet
        // 一个表示bit序列的集合，计算非常高效

        // (1) 构造BitSet
        // 用byte[]构造
        byte[] bytes = {(byte) 0b10101100, (byte) 0b00101000};
        BitSet primes = BitSet.valueOf(bytes);
        System.out.println(primes);
        // {2, 3, 5, 7, 11, 13}

        // 用long[]构造
        long[] longs = {0x100010116L, 0x1L, 0x1L, 0L, 0x1L};
        BitSet powersOfTwo = BitSet.valueOf(longs);
        System.out.println(powersOfTwo);
        // {1, 2, 4, 8, 16, 32, 64, 128, 256}

        // (2) 转换成byte[], long[], IntStream
        // toByteArray()
        for (byte bt : powersOfTwo.toByteArray()) {
            System.out.print(
                    Integer.toBinaryString(
                            Byte.toUnsignedInt(bt)));
        }
        System.out.println();
        // 1011011010001000000010000000000000001

        // toLongArray()
        for (long l : powersOfTwo.toLongArray()) {
            System.out.print(l + ",");
        }
        System.out.println();
        // 4295033110,1,1,0,1,

        // stream()：返回IntStream
        powersOfTwo.stream().forEach(System.out::println);
        // 1
        // 2
        // 4
        // 8
        // 16
        // 32
        // 64
        // 128
        // 256
    }

    private String directions;

    public void process(String directions) {
        // directions为null时，会抛出NullPointerException并且可以设置错误提示
        // 相比普通的null值检查，代码简洁并且更容易定位错误
        this.directions = Objects.requireNonNull(directions, "directions must not be null");
    }
}
