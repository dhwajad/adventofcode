package twentyeighteen;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day5 {


    public static void main(String[] args) throws URISyntaxException, IOException {

        final String string = Files.readString(Path.of(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("day5")).toURI()));

        String collect = Stream.of("abcdefghijklmnopqrstuvwxyz".split(""))
                .map(c -> c + c.toUpperCase() + "|" + c.toUpperCase() + c)
                .collect(Collectors.joining("|"));
        final Pattern pattern = Pattern.compile(collect);

        System.out.println("collect = " + collect);
        System.out.println("time = " + LocalTime.now());
        System.out.println("string = " + string.length());


        Integer min = Stream.of("abcdefghijklmnopqrstuvwxyz".split("")).parallel()
                .map(c -> c + "|" + c.toUpperCase())
                .map(regex -> {
                    Matcher matcher = Pattern.compile(regex).matcher(string);
                    String newString = matcher.replaceAll("");
                    matcher = pattern.matcher(newString);
                    while (matcher.find()) {
                        newString = matcher.replaceAll("");
                        matcher = pattern.matcher(newString);
                    }
                    return newString.length();
                }).min(Comparator.naturalOrder()).orElse(0);

        System.out.println("min = " + min);

        System.out.println("time = " + LocalTime.now());

    }
}