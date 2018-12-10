package twentyeighteen;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day8 {

    private static List<Integer> numbers;

    private static List<Integer> metaData = new ArrayList<>();

    static {
        try {
            numbers = Stream.of(Files.readString(Path.of(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("day8")).toURI())).split(" "))
                    .mapToInt(Integer::valueOf)
                    .boxed()
                    .collect(Collectors.toList());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("numbers = " + numbers.size());
        findMetaData(numbers);
        int sum = metaData.stream()
                .mapToInt(Integer::intValue)
                .sum();
        System.out.println("sum = " + sum);
    }

    private static int findMetaData(List<Integer> node) {
        Integer quantityChildNodes = node.get(0);
        Integer quantityMetaData = node.get(1);

        int size = 2;

        for(int i = 0; i < quantityChildNodes; i++) {
            size += findMetaData(node.subList(size, node.size() - quantityMetaData));
        }

        for(int i = size; i < size + quantityMetaData; i++) {
            metaData.add(node.get(i));
            //System.out.println(node.get(i));
        }
        size += quantityMetaData;
        return size;
    }

}
