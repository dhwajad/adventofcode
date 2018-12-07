package twentyeighteen;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day6 {

    private static List<String> strings;

    static {
        try {
            strings = Files.readAllLines(Path.of(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("day6")).toURI()));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    final static Set<Coordinate> inputCoordinates = strings.stream()
            .map(Coordinate::new)
            .collect(Collectors.toSet());

    public static void main(String[] args) {

        IntSummaryStatistics ySummaryStatistics = inputCoordinates.stream()
                .mapToInt(Coordinate::getY)
                .summaryStatistics();

        IntSummaryStatistics xSummaryStatistics = inputCoordinates.stream()
                .mapToInt(Coordinate::getX)
                .summaryStatistics();


        Grid grid = new Grid(ySummaryStatistics.getMax(), ySummaryStatistics.getMin(), xSummaryStatistics.getMax(), xSummaryStatistics.getMin());

        //part 1
        long max = inputCoordinates.stream()
                .collect(Collectors.toMap(Function.identity(), coordinate -> grid.getAllCoordinates().stream()
                        .filter(gridCoordinate -> !gridCoordinate.isInputCoordinate())
                        .filter(gridCoordinate -> !gridCoordinate.isEquidistant())
                        .filter(gridCoordinate -> gridCoordinate.getNearest().equals(coordinate)).count() + 1)).entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .filter(entrySet -> !entrySet.getKey().isUnbounded())
                .map(Map.Entry::getValue)
                .mapToLong(Long::valueOf)
                .max()
                .orElse(0);

        System.out.println("part1 = " + max);
        
        //part 2
        long count = grid.getAllCoordinates().stream()
                        .map(coordinate -> inputCoordinates.stream()
                        .map(coordinate::calculateManhattanDistance)
                        .mapToInt(Integer::valueOf)
                        .sum())
                        .filter(total -> total < 10000)
                        .count();
        System.out.println("part2= " + count);

    }
}

class Grid {

    private Set<Coordinate> allCoordinates = new LinkedHashSet<>();

    Grid(int yBoundMax, int yBoundMin, int xBoundMax, int xBoundMin) {

        IntStream.range(xBoundMin -1, xBoundMax + 1)
            .forEach(x -> IntStream.range(yBoundMin - 1, yBoundMax + 2)
                    .forEach(y -> {
                        Coordinate coordinate = new Coordinate(y, x);
                        coordinate.setInputCoordinate(Day6.inputCoordinates.contains(coordinate));
                        if(!coordinate.isInputCoordinate()) {
                            Day6.inputCoordinates
                                    .forEach(coordinate::calculateNearest);
                        }
                        if(coordinate.getX() == xBoundMax || coordinate.getX() == xBoundMin || coordinate.getY() == yBoundMax || coordinate.getY() == yBoundMin) {
                            coordinate.setUnbounded();
                            if(coordinate.getNearest() != null) {
                                coordinate.getNearest().setUnbounded();
                            }
                        }
                        allCoordinates.add(coordinate);
                    }));

    }


    Set<Coordinate> getAllCoordinates() {
        return allCoordinates;
    }

}

class Coordinate {

    private int y;

    private int x;

    private boolean isInputCoordinate;

    private Coordinate nearest;

    private Integer distance;

    private boolean isEquidistant;

    private boolean isUnbounded;

    Coordinate(int y, int x) {
        this.x = x;
        this.y = y;
    }

    Coordinate(String line) {
        this.y = Integer.parseInt(line.substring(0, line.indexOf(",")).trim());
        this.x = Integer.parseInt(line.substring(line.indexOf(",") + 1).trim());
        this.isInputCoordinate = true;
    }

    int calculateManhattanDistance(Coordinate to) {
        return Math.abs(this.getY() - to.getY()) + Math.abs(this.getX() - to.getX());
    }

    void calculateNearest(Coordinate to) {
        int manhattanDistance = calculateManhattanDistance(to);
        if(distance == null || manhattanDistance < this.getDistance()) {
            this.setDistance(manhattanDistance);
            this.setNearest(to);
            this.setEquidistant(false);
        } else if (manhattanDistance == this.getDistance()) {
            this.setEquidistant(true);
            nearest = null;
        }
    }

    boolean isUnbounded() {
        return isUnbounded;
    }

    void setUnbounded() {
        isUnbounded = true;
    }

    boolean isEquidistant() {
        return isEquidistant;
    }

    private void setEquidistant(boolean equidistant) {
        isEquidistant = equidistant;
    }

    boolean isInputCoordinate() {
        return isInputCoordinate;
    }

    void setInputCoordinate(boolean inputCoordinate) {
        isInputCoordinate = inputCoordinate;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    Coordinate getNearest() {
        return nearest;
    }

    private void setNearest(Coordinate nearest) {
        this.nearest = nearest;
    }

    private int getDistance() {
        return distance;
    }

    private void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return getX() == that.getX() &&
                getY() == that.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "y=" + y +
                ", x=" + x +
                ", isInputCoordinate=" + isInputCoordinate +
                ", nearest=" + nearest +
                ", distance=" + distance +
                ", isEquidistant=" + isEquidistant +
                ", isUnbounded=" + isUnbounded +
                '}';
    }
}