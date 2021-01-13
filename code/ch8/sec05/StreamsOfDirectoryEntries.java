import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;

public class StreamsOfDirectoryEntries {
    public static void main(String[] args) throws IOException {
        // Files.list(Path)
        System.out.println("non-hidden directories and files in current directory");
        try (Stream<Path> entries = Files.list(Paths.get("./"))) {
             entries
            .filter(p -> !p.getFileName().toString().startsWith("."))
            .limit(5)
            .forEach(System.out::println);
        }
        // non-hidden directories and files in current directory
        // ./note
        // ./out
        // ./code
        // ./README.md

        // Files.walk(Path)
        System.out.println("hidden directories and files including those in all sub-directories");
        try (Stream<Path> entries = Files.walk(Paths.get("./"))) {
             entries
            .filter(p -> p.getFileName().toString().startsWith("."))
            .limit(5)
            .forEach(System.out::println);
        }
        // hidden directories and files including those in all sub-directories
        // .
        // ./.DS_Store
        // ./code/ch7/sec01/.Rhistory
        // ./.gitignore
        // ./.git


        // Files.find(Path, maxDepth, FileVisitOption...)
        System.out.println("recent files in directories with directory-max-depth 5");
        int depth = 5;
        Instant oneMonthAgo = Instant.now().minus(30, ChronoUnit.DAYS);
        try (Stream<Path> entries = Files.find(Paths.get("./"), depth,
                (path, attrs) -> attrs.creationTime().toInstant().compareTo(oneMonthAgo) >= 0)) {
            entries.limit(5).forEach(System.out::println);
        }
        // recent files in directories with directory-max-depth 5
        // .
        // ./note
        // ./note/ch09_java7_features.md
        // ./note/ch02_stream.md
        // ./note/ch07_js_engine_noshorn.md
    }
}

