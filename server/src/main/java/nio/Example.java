package nio;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class Example {
    public static void main(String[] args) throws IOException {

        Path path = Paths.get("server", "serverFiles", "test.txt");
        Files.write(path,
                Arrays.asList("Hello", "world"),
                StandardOpenOption.APPEND
        );
        Path root = Paths.get("./");
//        Files.walk(root)
//                .filter(p->{
//            try{
//                return !Files.isHidden(p);
//            }catch (IOException e){
//                e.printStackTrace();
//            }
//            return false;
//        })
//                .filter(p->!Files.isDirectory(p))
//                .forEach(System.out::println);
        List<Path> paths = Files.walk(root)
                .filter(p -> !Files.isDirectory(p))
                .collect(Collectors.toList());
        System.out.println(paths);

        Files.walkFileTree(root, new HashSet<>(), 3, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.out.println(file);
                return super.visitFile(file, attrs);
            }
        });
    }
}
