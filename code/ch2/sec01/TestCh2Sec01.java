import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class TestCh2Sec01
{
    public static void main(String[] args) throws IOException
    {
        String contents = new String(Files.readAllBytes(
                Paths.get("../alice.txt")), StandardCharsets.UTF_8);
        List<String> words = Arrays.asList(contents.split("[\\P{L}]+"));

        // 使用迭代器
        long count = 0;
        for (String w : words) {
           if (w.length() > 12) {
               count++;
           }
        }
        System.out.println(count);

        // 使用stream
        count = words.stream().filter(w -> w.length() > 12).count();
        System.out.println(count);

        // 使用并行Stream
        count = words.parallelStream().filter(w -> w.length() > 12).count();
        System.out.println(count);
    }
}
