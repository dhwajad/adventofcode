package twentyeighteen;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day1 {

    private static int beginFrequency = 0;
    private static boolean dupNotFound = true;

    public static void main(String[] args) throws URISyntaxException, IOException {

        List<String> strings = Files.readAllLines(Path.of(Thread.currentThread().getContextClassLoader().getResource("day1").toURI()));

        //Part 1
        int sum = strings.stream()
                .mapToInt(Integer::parseInt)
                .sum();
        System.out.println("sum = " + sum);


        //Part 2
        Set<Integer> noDups = new HashSet<>();
        int duplicateFrequency = 0;
        do {
            duplicateFrequency = strings.stream()
                    .mapToInt(Integer::parseInt)
                    .reduce(beginFrequency, (a, b) -> {
                        if (dupNotFound) {
                            beginFrequency = a + b;
                            dupNotFound = noDups.add(beginFrequency);
                        }
                        return beginFrequency;
                    });
        } while (dupNotFound);
        System.out.println("duplicateFrequency = " + duplicateFrequency);


    }
}
