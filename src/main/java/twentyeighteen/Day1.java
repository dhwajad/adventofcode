package twentyeighteen;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Day1 {

    public static void main(String[] args) throws URISyntaxException, IOException {

        List<String> strings = Files.readAllLines(Path.of(Thread.currentThread().getContextClassLoader().getResource("day1").toURI()));

        //Part 1
        int sum = strings.stream()
                .mapToInt(Integer::parseInt)
                .sum();
        System.out.println("sum = " + sum);


        //Part 2
        List<Integer> ints = new ArrayList<>();
        ints.add(0);
        Set<Integer> unique = new LinkedHashSet<>();
        boolean duplicateFound = false;
        while(true) {
            for(String string : strings) {
                int previousIndex = ints.size() - 1;
                Integer candidate = Integer.parseInt(string) + ints.get(previousIndex);
                ints.add(candidate);
                boolean duplicate = unique.add(candidate);
                if(!duplicate) {
                    System.out.println("duplicate = " + candidate);
                    duplicateFound = true;
                    break;
                }
            }
            if(duplicateFound) {
                break;
            }
        }

    }
}
