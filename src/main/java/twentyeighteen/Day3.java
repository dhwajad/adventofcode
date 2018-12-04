package twentyeighteen;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day3 {

    public static void main(String[] args) throws URISyntaxException, IOException {

        List<String> strings = Files.readAllLines(Path.of(Thread.currentThread().getContextClassLoader().getResource("day3").toURI()));

        //part 1
        final long count = strings.stream()
                .map(Claim::new)
                .flatMap(claim -> {
                    return claim.getCoordinates().stream();
                })
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .filter(stringLongEntry -> stringLongEntry.getValue() > 1)
                .count();

        System.out.println("count = " + count);

        //part 2

        final Set<String> collect = strings.stream()
                .map(Claim::new)
                .flatMap(claim -> {
                    return claim.getCoordinates().stream();
                })
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .filter(stringLongEntry -> stringLongEntry.getValue() == 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        final Integer id = strings.stream()
                .map(Claim::new)
                .filter(claim ->
                        collect.containsAll(claim.getCoordinates())
                ).findFirst()
                .map(Claim::getId)
                .orElseGet(() -> -1);
        System.out.println("id = " + id);
    }

}

class Claim {
    int id;

    Set<String> coordinates = new HashSet<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<String> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Set<String> coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * #123 @ 3,2: 5x4
     * {id=123, coordinates=[2,3, 2,4, 2,5, 2,6, 2,7, 3,3, 3,4, 3,5, 3,6, 3,7, 4,3, 4,4, 4,5, 4,6, 4,7, 5,3, 5,4, 5,5, 5,6, 5,7]}
     */
    public Claim(String line) {
        id = Integer.parseInt(line.substring(line.indexOf("#") + 1, line.indexOf("@")).trim());
        int startTop = Integer.parseInt(line.substring(line.indexOf(",") + 1, line.indexOf(":")).trim());
        int startLeft = Integer.parseInt(line.substring(line.indexOf("@") + 1, line.indexOf(",")).trim());
        int wide = Integer.parseInt(line.substring(line.indexOf(":") + 1, line.indexOf("x")).trim());
        int tall = Integer.parseInt(line.substring(line.indexOf("x") + 1, line.length()).trim());

        IntStream.range(startTop, startTop + tall)
                .mapToObj(Integer::toString)
                .forEach(x -> {
                    IntStream.range(startLeft, startLeft + wide)
                            .mapToObj(Integer::toString)
                            .forEach(y -> {
                                coordinates.add(x + "," + y);
                            });
                });
    }

    @Override
    public String toString() {
        return String.format(
                "{id=%s, coordinates=%s}", id, coordinates);
    }

}