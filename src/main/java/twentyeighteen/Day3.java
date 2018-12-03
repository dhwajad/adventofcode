package twentyeighteen;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

        strings.stream()
                .map(Claim::new)
                .filter(claim -> {
                    long countUnique = claim.getCoordinates().stream()
                            .filter(coordinate -> {
                                return collect.contains(coordinate);
                            }).count();
                    if(countUnique == claim.getCoordinates().size()) {
                        System.out.println("claim.getId() = " + claim.getId());
                        return true;
                    }
                    return false;
                }).findFirst();
    }

}

class Claim {
    int id;

    List<String> coordinates = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<String> coordinates) {
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