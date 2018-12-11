package twentyeighteen;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day10 {

    private static List<String> strings;

    static {
        try {
            strings = Files.readAllLines(Path.of(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("day10")).toURI()));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private final static Set<Star> inputCoordinates = strings.stream()
            .map(Star::new)
            .collect(Collectors.toSet());

    public static void main(String[] args) {

        int elapsedSeconds = 0;

        int previousXSize = inputCoordinates.size();
        int previousYSize = inputCoordinates.size();

        while (true) {

            int xSize = inputCoordinates.stream()
                    .mapToInt(Star::getX)
                    .boxed()
                    .collect(Collectors.toSet())
                    .size();

            int ySize = inputCoordinates.stream()
                    .mapToInt(Star::getY)
                    .boxed()
                    .collect(Collectors.toSet())
                    .size();


            if(xSize > previousXSize && ySize > previousYSize) {

                inputCoordinates.forEach(Star::tickTockReverse);

                System.out.println("elapsedSeconds = " + elapsedSeconds);
                IntSummaryStatistics xSummaryStatistics = inputCoordinates.stream()
                        .mapToInt(Star::getX)
                        .summaryStatistics();

                IntSummaryStatistics ySummaryStatistics = inputCoordinates.stream()
                        .mapToInt(Star::getY)
                        .summaryStatistics();

                IntStream.range(xSummaryStatistics.getMin() - 1, xSummaryStatistics.getMax() + 1)
                        .forEach(x -> {
                                    IntStream.range(ySummaryStatistics.getMin() - 1, ySummaryStatistics.getMax() + 2)
                                            .forEach(y -> {
                                                Star star = new Star(x, y);
                                                if(inputCoordinates.contains(star)) {
                                                    System.out.print("#");
                                                } else {
                                                    System.out.print(".");
                                                }
                                            });
                                    System.out.println();
                                }
                        );
                break;
            }

            previousXSize = xSize;
            previousYSize = ySize;
            elapsedSeconds++;
            inputCoordinates.forEach(Star::tickTock);

        }


    }
}

class Star {

    private int x;

    private int y;

    private int velocityX;

    private int velocityY;

    Star(String line) {
        String postition = line.substring(line.indexOf("position=<") + "position=<".length(), line.indexOf("> velocity=")).trim();
        this.x = Integer.parseInt(postition.split(", ")[0].trim());
        this.y = Integer.parseInt(postition.split(", ")[1].trim());

        String velocity = line.substring(line.indexOf("> velocity=") + "> velocity=".length() + 1, line.length() - 1).trim();
        this.velocityX = Integer.parseInt(velocity.split(", ")[0].trim());
        this.velocityY = Integer.parseInt(velocity.split(", ")[1].trim());
    }

    void tickTock() {
        x += velocityX;
        y+= velocityY;
    }

    void tickTockReverse() {
        x -= velocityX;
        y -= velocityY;
    }

    Star(int x, int y) {
        this.x = x;
        this.y = y;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Star star = (Star) o;
        return getX() == star.getX() &&
                getY() == star.getY();
    }

    @Override
    public int hashCode() {
        return 1;
    }
}