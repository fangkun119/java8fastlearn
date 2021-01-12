import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Util {
    public static String getPage(String urlString) {
        try {
            Scanner in = new Scanner(new URL(urlString).openStream());
            StringBuilder builder = new StringBuilder();
            while (in.hasNextLine()) {
                builder.append(in.nextLine());
                builder.append("\n");
            }
            return builder.toString();
        } catch (IOException ex) {
            RuntimeException rex = new RuntimeException();
            rex.initCause(ex);
            throw rex;
        }
    }

    public static List<String> matches(String input, String patternString) {
        Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);
        List<String> result = new ArrayList<>();
        while (matcher.find())
            result.add(matcher.group(1));
        return result;
    }

    public static Scanner in = new Scanner(System.in);

    public static String getInput(String prompt) {
        System.out.print(prompt + ": ");
        return in.nextLine();
    }

    // action: 执行的慢速阻塞操作
    // until：判断操作完成的条件，如果完成则返回，否则重试
    public static <T> CompletableFuture<T> repeat(Supplier<T> action,  Predicate<T> until) {
        return CompletableFuture.supplyAsync(action).thenComposeAsync((T t) -> {
            return until.test(t) ? CompletableFuture.completedFuture(t) : repeat(action, until);
        });
    }
}


public class FutureDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        String hrefPattern = "<a\\s+href\\s*=\\s*(\"[^\"]*\"|[^\\s>]*)\\s*>";
        // CompletableFuture<String> getURL = CompletableFuture.supplyAsync(() -> Util.getInput("URL"));

        // Make a function mapping URL to CompletableFuture
        CompletableFuture<String> f = Util.repeat(
                // action ：从标准输入获取一个URL
                () -> Util.getInput("URL"),
                // until  ：重复获取直到格式符合URL格式时返回一个封装着url的CompletableFuture
                s -> s.startsWith("http://")
        ).thenApplyAsync(
                // fn：根据url抓取网页页面，该操作封装在CompletableFuture中返回
                (String url) -> Util.getPage(url)
        );
        // 抓取完成时会调用thenApply提取页面中的链接，该操作封装在CompletableFuture中返回
        CompletableFuture<List<String>> links = f.thenApply(
                c -> Util.matches(c, hrefPattern)
        );
        // 提取链接操作完成时，会调用thenAccept设置的回调函数
        links.thenAccept(System.out::println);

        // 所有以Async结尾的方法都有两种形式：
        // * 一种会在普通的ForkJoinPool中运行所提供的操作
        // * 一种需要传入一个Executor参数，让操作在这个executor中运行
        // 下面对ForkJoinPool的属性进行了设置
        ForkJoinPool.commonPool().awaitQuiescence(10, TimeUnit.SECONDS);
        System.out.println("exiting");
    }
}
