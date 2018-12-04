package twentyeighteen;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day4 {

    public static void main(String[] args) throws URISyntaxException, IOException {

        List<String> strings = Files.readAllLines(Path.of(Thread.currentThread().getContextClassLoader().getResource("day4").toURI()));
    }
}
