package twentyeighteen;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day5 {

    private static String string;

    static {
        try {
            string = Files.readString(Path.of(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("day5")).toURI()));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private static int currentLength = string.length();
    private static int previousLength = string.length();

    private static Map<String, Integer> map = new HashMap<>();

    private static Pattern pattern = Pattern.compile("aA|Aa|bB|Bb|cC|Cc|dD|Dd|eE|Ee|fF|Ff|gG|Gg|hH|Hh|iI|Ii|jJ|Jj|kK|Kk|lL|Ll|mM|Mm|nN|Nn|oO|Oo|pP|Pp|qQ|Qq|rR|Rr|sS|Ss|tT|Tt|uU|Uu|vV|Vv|wW|Ww|xX|Xx|yY|Yy|zZ|Zz");

    public static void main(String[] args) {

        System.out.println("time = " + LocalTime.now());
        System.out.println("string = " + string.length());

        Stream.of("a|A", "b|B", "c|C", "d|D", "e|E", "f|F", "g|G", "h|H", "i|I", "j|J", "k|K", "l|L", "m|M", "n|N", "o|O", "p|P", "q|Q", "r|R", "s|S", "t|T", "u|U", "v|V", "w|W", "x|X", "y|Y", "z|Z")
                .forEach(regex -> {
                    String newString = Pattern.compile(regex).matcher(string).replaceAll("");
                    do {
                        newString = pattern.matcher(newString).replaceAll("");
                        previousLength = currentLength;
                        currentLength = newString.length();
                    } while (previousLength != currentLength);
                    map.put(regex, newString.length());
                });

        System.out.println("time = " + LocalTime.now());

        map.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue))
                .forEach(System.out::println);
    }
}
