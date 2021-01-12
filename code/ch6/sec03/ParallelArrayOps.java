import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class ParallelArrayOps {
    public static void main(String[] args) throws IOException {
        // 测试数据，一篇文章中所有的词
        // 读入文件内容
        String contents = new String(Files.readAllBytes(
                Paths.get("./code/ch2/alice.txt")),
                StandardCharsets.UTF_8); // Read file into string
        // 根据非字母字符对文件内容进行分隔
        String[] strArray = contents.split("[\\P{L}]"); // Split along nonletters

        // Arrays.parallelSort(strArray)
        // 以多线程的方式对数组进行排序
        Arrays.parallelSort(strArray);
        // System.out.println(Arrays.toString(strArray));
        // [, , , , ...... , youth, youth, youth, zigzag, zip]

        // Arrays.parallelSetAll
        // 用传入的lambda表达式，根据数组下标计算出一个值填充到数组中
        int[] values = new int[20];
        Arrays.parallelSetAll(values, i -> i % 10);
        System.out.println(Arrays.toString(values));
        // [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9]

        // Arrays.parallelPrefix
        // 用数组从下标0到当前下标i的累积运算（由传入的lambda表达式指定）计算出来的值填充数组
        Arrays.parallelSetAll(values, i -> i + 1);
        System.out.println(Arrays.toString(values));
        // [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]
        Arrays.parallelPrefix(values, (x, y) -> x * y);
        System.out.println(Arrays.toString(values));
        // [1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800, 39916800, 479001600, 1932053504, 1278945280, 2004310016, 2004189184, -288522240, -898433024, 109641728, -2102132736]
    }
}
