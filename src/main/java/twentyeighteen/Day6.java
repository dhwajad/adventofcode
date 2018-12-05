package twentyeighteen;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class Day6 {

    public static void main(String[] args) throws URISyntaxException, IOException {
        List<String> strings = Files.readAllLines(Path.of(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("day6")).toURI()));


    }
}
