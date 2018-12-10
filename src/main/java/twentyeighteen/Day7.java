package twentyeighteen;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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

        //step timings
        Map<String, Integer> stepTimings = Stream.of("abcdefghijklmnopqrstuvwxyz".split(""))
                .collect(Collectors.toMap(String::toUpperCase, (T) -> ((int) T.charAt(0)) - 36));

        //5 workers (for part 1 switch to 1 worker = 0, 1)
        Map<String, Integer> workerPool = IntStream.range(0, 5)
                .boxed()
                .collect(Collectors.toMap((T) -> T + ".", (T) -> 0));

        List<Integer> totalTime = new ArrayList<>();


        while (parents.size() > 0) {
            //find eligible
            List<Map.Entry<String, List<String>>> eligible = parents.entrySet().stream().
                    sorted(Map.Entry.comparingByKey())
                    .filter(es -> !workerPool.containsKey(es.getKey()))
                    .filter(es -> !children.containsKey(es.getKey()))
                    .collect(Collectors.toList());

            //assign work
            eligible.forEach( eligibleEntrySet -> {
                        Map.Entry<String, Integer> freeWorker = workerPool.entrySet().stream()
                                .filter(workerPoolEntrySet -> workerPoolEntrySet.getKey().contains("."))
                                .findFirst()
                                .orElse(null);
                        if(freeWorker != null) {
                            workerPool.remove(freeWorker.getKey());
                            workerPool.put(eligibleEntrySet.getKey(), stepTimings.get(eligibleEntrySet.getKey()));
                        }
                    }
            );

            //complete shortest Task
            Map.Entry<String, Integer> completeTaskEntrySet = workerPool.entrySet().stream()
                    .filter(workerPoolEntrySet -> !workerPoolEntrySet.getKey().contains("."))
                    .min(Map.Entry.comparingByValue())
                    .get();


            //remove parent dependency on child
            parents.get(completeTaskEntrySet.getKey()).forEach(child -> {
                children.get(child).remove(completeTaskEntrySet.getKey());
                if(children.get(child).size() == 0) {
                    children.remove(child);
                    if(!parents.containsKey(child)) {
                        parents.put(child, List.of());
                    }
                }
            });

            //parent complete
            parents.remove(completeTaskEntrySet.getKey());

            //register time
            final int taskTime = completeTaskEntrySet.getValue();
            System.out.println(completeTaskEntrySet.getKey() + " : " + taskTime);
            totalTime.add(taskTime);

            //reduce elapsed times
            workerPool.entrySet().stream()
                    .filter(workerPoolEntrySet -> !workerPoolEntrySet.getKey().contains("."))
                    .forEach(
                            workerPoolEntrySet -> workerPool.replace(workerPoolEntrySet.getKey(), workerPoolEntrySet.getValue() - taskTime)
                    );

            //free up worker
            workerPool.remove(completeTaskEntrySet.getKey());
            workerPool.put(completeTaskEntrySet.getKey() + ".", 0);

        }
        int sum = totalTime.stream()
                .mapToInt(Integer::intValue)
                .sum();

        System.out.println("sum = " + sum);

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
                            t.sort(Comparator.naturalOrder());
                            return t;
                        }
                ));
    }

}
