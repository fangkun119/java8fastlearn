import java.nio.file.Path;
import java.nio.file.Paths;

public class PathDemo {
    private static final String LOG_PREFIX = "\n\t// ";
    public static void main(String[] args) {
        // Path：(1)可能是含有多个目录名称的序列；(2)也可能带着一个文件名

        // [1] 绝对路径，Paths.get()
        // 对于无效路径会抛出InvalidPathException异常
        Path absolute = Paths.get("/", "home", "cay");
        System.out.println(LOG_PREFIX + absolute);
        // 输出：/home/cay

        Path relative = Paths.get("myprog", "conf", "user.properties");
        System.out.println(LOG_PREFIX + relative);
        // 输出：myprog/conf/user.properties

        Path homeDirectory = Paths.get("/home/cay");
        System.out.println(LOG_PREFIX + homeDirectory);
        // 输出：/home/cay

        // [2] 路径解析：
        // p.resolve(q)，规则如下：
        // * 如果q是绝对路径，返回${q}
        // * 如果q是相对路劲，返回${p}/${q}
        Path configPath = homeDirectory.resolve("myprog/conf/user.properties");
        System.out.println(LOG_PREFIX + configPath);
        // 输出：/home/cay/myprog/conf/user.properties

        // p.resolveSibling(q)，针对p的父目录进行解析
        Path workPath = Paths.get("/home/cay/myprog/work");
        Path tempPath = workPath.resolveSibling("temp");
        System.out.println(LOG_PREFIX + tempPath);
        // 输出：/home/cay/myprog/temp

        // p.relativize(q)：从路径p到路径q的相对路径
        Path relPath = Paths.get("/home/cay").relativize(Paths.get("/home/fred/myprog"));
        System.out.println(LOG_PREFIX + relPath);
        // 输出：../fred/myprog

        // p.normalize()：去掉冗余成分之后的路径
        Path normalizedPath = Paths.get("/home/cay/../fred/./myprog").normalize();
        System.out.println(normalizedPath);
        // 输出：/home/fred/myprog

        // [3] 路径拆分
        // p.getParent()：
        // 如果p是目录，返回的p的上一级目录
        // 如果p是文件，返回的p所在的目录
        Path parent1 = Paths.get("/home/cay/myfile.txt").getParent();
        System.out.println(LOG_PREFIX + parent1);
        // 输出：/home/cay
        Path parent2 = Paths.get("/home/cay/mydir/").getParent();
        System.out.println(LOG_PREFIX + parent2);
        // 输出：/home/cay

        // p.getFileName()：
        // 如果p是文件，返回文件名
        // 如果p是目录，返回最后一级目录的目录名
        Path file1 =  Paths.get("/home/cay/myfile.txt").getFileName(); // the path myprog.properties
        System.out.println(LOG_PREFIX + file1);
        // 输出：myfile.txt
        Path file2 =  Paths.get("/home/cay/mydir/").getFileName(); // the path myprog.properties
        System.out.println(LOG_PREFIX + file2);
        // 输出：myDir

        // p.getRoot()
        // 如果是绝对路径，返回该路径的根目录
        // 如果是相对路径，返回null
        Path root1 = Paths.get("/home/cay/myfile.txt").getRoot(); // the path /
        System.out.println(LOG_PREFIX + root1);
        // 输出：/
        Path root2 = Paths.get("/home/cay/myfile.txt").getFileName().getRoot();
        System.out.println(LOG_PREFIX + root2);
        // 输出：null （单独的文件名部分没有root）
        Path root3 = Paths.get("./code/next/").getRoot();
        System.out.println(LOG_PREFIX + root3);
        // 输出：null （相对路径没有root）
    }
}
