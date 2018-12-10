package twentyeighteen;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day8 {

    private static List<Integer> numbers;

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
        List<Integer> sizeValueList = findMetaData(numbers);
        System.out.println("sizeValueList = " + sizeValueList);
    }

    private static List<Integer> findMetaData(List<Integer> node) {
        Integer quantityChildNodes = node.get(0);
        Integer quantityMetaData = node.get(1);

        int size = 2;

        Map<Integer, Integer> childValueMap = new HashMap<>();
        for(int i = 0; i < quantityChildNodes; i++) {
            List<Integer> sizeValueList = findMetaData(node.subList(size, node.size() - quantityMetaData));
            size += sizeValueList.get(0);
            childValueMap.put(i + 1, sizeValueList.get(1));
        }

        int value = 0;
       
            for(int i = size; i < size + quantityMetaData; i++) {
                //System.out.println(node.get(i));
                if (quantityChildNodes == 0) {
                    value += node.get(i);
                } else {
                    if (childValueMap.containsKey(node.get(i))) {
                        value += childValueMap.get(node.get(i));
                    }
                }
            }

        size += quantityMetaData;
        return List.of(size, value);
    }

}
