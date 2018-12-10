package twentyeighteen;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Day9 {

    private static String string;

    static {
        try {
            string = Files.readString(Path.of(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("day9")).toURI()));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private static int numPlayers = Integer.parseInt(string.substring(0, string.indexOf("players")).trim());
    private static int totalMarbles = Integer.parseInt(string.substring(string.indexOf("worth") + 5, string.indexOf("points")).trim());

    public static void main(String[] args) {

        ArrayDeque<Integer> circle = new ArrayDeque<>(totalMarbles);
        Map<Integer, Long> playerScore = new HashMap<>();
        for (int i = 0; i <= totalMarbles; i++) {
            int playerNum = i % numPlayers == 0 ? numPlayers : i % numPlayers;
            if(i != 0 && i % 23 == 0) {
                //remove on multiple of 23
                int score = i;
                rotate(circle, -7);
                score += circle.removeLast();
                if(playerScore.containsKey(playerNum)) {
                    playerScore.put(playerNum, playerScore.get(playerNum) + score);
                } else {
                    playerScore.put(playerNum, Integer.toUnsignedLong(score));
                }
            } else {
                //add if not multiple of 23
                circle.addLast(i);
            }
            if((i + 1)  % 23 != 0) {
                rotate(circle, 2);
            }
        }
        Map.Entry<Integer, Long> integerIntegerEntry = playerScore.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);
        System.out.println("value = " + integerIntegerEntry);
    }

    private static void rotate(ArrayDeque<Integer> circle, int byIndex) {
        if(byIndex > 0) {
            //clockwise
            for (int i = 0; i < byIndex; i++) {
                circle.addFirst(circle.removeLast());
            }
        } else if (byIndex < 0) {
            //anticlockwise
            for (int i = 0; i > byIndex; i--) {
                circle.addLast(circle.removeFirst());
            }
        }
    }
}
