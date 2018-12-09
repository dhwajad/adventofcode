package twentyeighteen;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Day7 {

    private static List<String> strings;

    static {
        try {
            strings = Files.readAllLines(Path.of(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("day7")).toURI()));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Map<String, List<String>> parents = mapInput(5, 6, 36, 37);


        Map<String, List<String>> children = mapInput(36, 37, 5, 6);


        List<String> order = new ArrayList<>();

        while (parents.size() > 0) {
            Map.Entry<String, List<String>> stringListEntry = parents.entrySet().stream().
                    sorted(Map.Entry.comparingByKey())
                    .filter(es -> !children.containsKey(es.getKey()))
                    .findFirst()
                    .orElse(null);

            if(stringListEntry != null) {
                order.add(stringListEntry.getKey());
                parents.remove(stringListEntry.getKey());

                stringListEntry.getValue().forEach( child -> {
                            children.get(child).remove(stringListEntry.getKey());
                            if (children.get(child).size() == 0) {
                                if (children.size() > 1) {
                                    children.remove(child);
                                } else {
                                    order.add(child);
                                }
                            }
                        });
            }
        }

        order.forEach(System.out::print);

    }

    private static Map<String, List<String>> mapInput(int i, int i2, int i3, int i4) {
        return strings.stream()
                .collect(Collectors.toMap(
                        string -> string.substring(i, i2),
                        string -> {
                            List<String> dependents = new ArrayList<>();
                            dependents.add(string.substring(i3, i4));
                            return dependents;
                        },
                        (t, u) -> {
                            t.addAll(u);
                            t.sort( Comparator.naturalOrder());
                            return t;
                        }
                ));
    }

}
