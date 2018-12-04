package twentyeighteen;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day2 {

    private static Long twiceTotal = 0L;
    private static Long thriceTotal = 0L;

    public static void main(String[] args) throws URISyntaxException, IOException {

        List<String> strings = Files.readAllLines(Path.of(Thread.currentThread().getContextClassLoader().getResource("day2").toURI()));

        // part 1
        strings.forEach(string -> {
            Map<Character, Long> collect = IntStream.range(0, string.length())
                    .mapToObj(string::charAt)
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
            Long twice = collect.values().stream()
                    .filter(number -> number == 2)
                    .findFirst()
                    .orElseGet(() -> 0L);
            if(twice != 0) twiceTotal++;

            Long thrice = collect.values().stream()
                    .filter(number -> number == 3)
                    .findFirst()
                    .orElseGet(() -> 0L);
            if(thrice != 0) thriceTotal++;
        });

        System.out.println("twiceTotal = " + twiceTotal);
        System.out.println("thriceTotal = " + thriceTotal);
        System.out.println("checksum = " + (twiceTotal * thriceTotal));

        // part 2
        strings.sort(Comparator.naturalOrder());
        strings.stream()
                .map(Box::new)
                .sorted()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

}

class Box implements Comparable {

    private String id;

    Box(String id) {
        this.id = id;
    }

    private String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Box &&
                Objects.equals(this.getId(), ((Box) obj).getId());
        //return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

    @Override
    public String toString() {
        return String.format(
                "{id=%s}", getId());
    }

    @Override
    public int compareTo(Object obj) {
        if(obj instanceof Box) {
            Box box = (Box)obj;
            List<Character> collect1 = IntStream.range(0, this.id.length())
                    .mapToObj(index -> this.id.charAt(index))
                    .collect(Collectors.toList());
            List<Character> collect2 = IntStream.range(0, box.id.length())
                    .mapToObj(index -> box.id.charAt(index))
                    .collect(Collectors.toList());
            long count = IntStream.range(
                    0, Math.min(collect1.size(), collect2.size()))
                    .mapToObj(i -> collect1.get(i) == collect2.get(i))
                    .filter(uncommon -> !uncommon)
                    .count();
            if(count == 1) {
                System.out.println("id1 = " + this.getId());
                System.out.println("id2 = " + box.getId());
                IntStream.range(
                        0, Math.min(collect1.size(), collect2.size()))
                        .mapToObj(i -> {
                            if(collect1.get(i) == collect2.get(i)) {
                                return collect1.get(i);
                            } else {
                                return "";
                            }
                        })
                        .forEachOrdered(System.out::print);
                System.out.println();
            }
        }
        return 0;
    }
}
