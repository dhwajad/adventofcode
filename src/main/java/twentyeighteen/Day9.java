package twentyeighteen;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day9 {

    private static String string;

    static {
        try {
            string = Files.readString(Path.of(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("day9")).toURI()));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int numPlayers = Integer.parseInt(string.substring(0, string.indexOf("players")).trim());
        System.out.println("numPlayers = " + numPlayers);
        int totalMarbles = Integer.parseInt(string.substring(string.indexOf("worth") + 5, string.indexOf("points")).trim());
        System.out.println("totalMarbles = " + totalMarbles);

        Map<Integer, Integer> playerScore = new HashMap<>();
        List<Integer> circle = new ArrayList<>();
        int currentIndex = 0;
        for (int i = 0; i <= totalMarbles; i++) {
            int playerNum = i % numPlayers == 0 ? numPlayers : i % numPlayers;
            if(i != 0 && i % 23 == 0) {
                int score = i;
                currentIndex = currentIndex - 7;
                while (currentIndex < 0) {
                    currentIndex = currentIndex >= 0 ? currentIndex : currentIndex + circle.size();
                }
                score += circle.remove(currentIndex);
                if(playerScore.containsKey(playerNum)) {
                    playerScore.put(playerNum, playerScore.get(playerNum) + score);
                } else {
                    playerScore.put(playerNum, score);
                }
                currentIndex = currentIndex + 2;
                while (currentIndex > circle.size()) {
                    currentIndex = currentIndex <= circle.size() ? currentIndex : currentIndex - circle.size();
                }

            } else {
                circle.add(currentIndex, i);
                if((i + 1)  % 23 != 0) {
                    currentIndex = currentIndex + 2;
                    while (currentIndex > circle.size()) {
                        currentIndex = currentIndex <= circle.size() ? currentIndex : currentIndex - circle.size();
                    }
                }
            }
        }
        Map.Entry<Integer, Integer> integerIntegerEntry = playerScore.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .get();
        System.out.println("value = " + integerIntegerEntry);
    }
}
