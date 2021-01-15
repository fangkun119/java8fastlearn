import java.io.*;
import java.nio.file.*;
import java.util.concurrent.*;

public class ProcessDemo {
   private static Path path(String fileName) {
      return Paths.get("./code/ch9/sec05/", fileName);
   }

   public static void main(String[] args) throws IOException, InterruptedException {
      // 替代Java5的Runtime.exec执行外部命令
      // 可以使用builder模式，设置输入、输出、工作目录等

      // 例子1
      ProcessBuilder builder1 = new ProcessBuilder("pwd");
      builder1.redirectErrorStream(true);
      builder1.redirectOutput(ProcessBuilder.Redirect.INHERIT);
      Process process1 = builder1.start();
      process1.waitFor(10, TimeUnit.SECONDS);
      // 输出 : /Users/fangkun/Dev/git/java8note

      // 例子2
      ProcessBuilder builder2 = new ProcessBuilder("grep", "[A-Za-z_][A-Za-z_0-9]*", "-o");
      builder2.redirectInput(path("ProcessDemo.java").toFile());
      builder2.redirectOutput(path("identifiers.txt").toFile());
      Process process2 = builder2.start();
      process2.waitFor(1, TimeUnit.MINUTES);
   }
}
