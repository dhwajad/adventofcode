package twentyeighteen;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day6 {

    public static List<String> strings;

    static {
        try {
            strings = Files.readAllLines(Path.of(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("day6")).toURI()));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    final public static Set<Coordinate> inputCoordinates = strings.stream()
            .map(Coordinate::new)
            .map(coordinate -> {
                coordinate.setInputCoordinate(true);
                return coordinate;
            })
            .collect(Collectors.toSet());

    public static void main(String[] args) throws URISyntaxException, IOException {

        int xBoundMax = inputCoordinates.stream()
                .max(Comparator.comparing(Coordinate::getX))
                .get()
                .getX();

        int xBoundMin = inputCoordinates.stream()
                .min(Comparator.comparing(Coordinate::getX))
                .get()
                .getX();

        int yBoundMax = inputCoordinates.stream()
                .max(Comparator.comparing(Coordinate::getY))
                .get()
                .getY();

        int yBoundMin = inputCoordinates.stream()
                .min(Comparator.comparing(Coordinate::getY))
                .get()
                .getY();

        Grid grid = new Grid(xBoundMax, xBoundMin, yBoundMax, yBoundMin);
        //System.out.println("grid = " + grid);

        Map<Coordinate, Long> collect = inputCoordinates.stream()
                .collect(Collectors.toMap(Function.identity(), coordinate -> {
                    long count = grid.getAllCoordinates().stream()
                            .filter(gridCoordinate -> {
                                return !gridCoordinate.isInputCoordinate();
                            })
                            .filter(gridCoordinate -> {
                                return !gridCoordinate.isEquidistant();
                            })
                            .filter(gridCoordinate -> {
                                return gridCoordinate.getNearest().equals(coordinate);
                            }).count();
                    return count + 1;
                }));

        //part 1
        Long max = collect.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .filter(entrySet -> {
                    return !entrySet.getKey().isUnbounded();
                })
                .map(entrySet -> {
                    return entrySet.getValue();
                })
                .mapToLong(Long::valueOf)
                .max()
                .orElseGet(() -> 0);

        System.out.println("part1 = " + max);
        
        //part 2
        long count = grid.getAllCoordinates().stream()
                .map(coordinate -> {
                    return inputCoordinates.stream()
                            .map(iC -> {
                                return coordinate.calculateManhattanDistance(iC);
                            })
                            .mapToInt(Integer::valueOf)
                            .sum();
                })
                .filter(total -> total < 10000)
                .count();
        System.out.println("part2= " + count);

        /*Coordinate coordinate = new Coordinate(9, 3);
        coordinate.calculateNearest(new Coordinate(9, 8));
        System.out.println(coordinate.getDistance());
        coordinate.calculateNearest(new Coordinate(6, 1));
        System.out.println(coordinate.getDistance());*/
    }
}

class Grid {

    private Coordinate maxMax;
    private Coordinate minMax;
    private Coordinate maxMin;
    private Coordinate minMin;

    private Set<Coordinate> allCoordinates = new LinkedHashSet<>();

    public Grid(int xBoundMax, int xBoundMin, int yBoundMax, int yBoundMin) {
        this.maxMax = new Coordinate(xBoundMax, yBoundMax);
        this.minMax = new Coordinate(xBoundMin, yBoundMax);
        this.maxMin = new Coordinate(xBoundMax, yBoundMin);
        this.minMin = new Coordinate(xBoundMin, yBoundMin);

        IntStream.range(xBoundMin -1, xBoundMax + 1)
            .forEach(x -> {
                IntStream.range(yBoundMin - 1, yBoundMax + 2)
                        .forEach(y -> {
                            Coordinate coordinate = new Coordinate(x, y);
                            coordinate.setInputCoordinate(Day6.inputCoordinates.contains(coordinate));
                            allCoordinates.add(coordinate);

                            if(!coordinate.isInputCoordinate()) {
                                Day6.inputCoordinates.stream()
                                        .forEach(inputCoordinate -> {
                                            coordinate.calculateNearest(inputCoordinate);
                                        });
                            }
                            if(coordinate.getX() == xBoundMax || coordinate.getX() == xBoundMin || coordinate.getY() == yBoundMax || coordinate.getY() == yBoundMin) {
                                coordinate.setUnbounded(true);
                                if(coordinate.getNearest() != null) {
                                    coordinate.getNearest().setUnbounded(true);
                                }
                            }
                            /*if (coordinate.getNearest() != null) {
                                System.out.print("[" + coordinate.getNearest().getY() + coordinate.getNearest().getX() + "]");
                            } else {
                                if(coordinate.isInputCoordinate()) {
                                    System.out.print("[oo]");
                                } else {
                                    System.out.print("[..]");
                                }

                            }*/


                        });
                //System.out.println();
            });

    }


    public Coordinate getMaxMax() {
        return maxMax;
    }

    public void setMaxMax(Coordinate maxMax) {
        this.maxMax = maxMax;
    }

    public Coordinate getMinMax() {
        return minMax;
    }

    public void setMinMax(Coordinate minMax) {
        this.minMax = minMax;
    }

    public Coordinate getMaxMin() {
        return maxMin;
    }

    public void setMaxMin(Coordinate maxMin) {
        this.maxMin = maxMin;
    }

    public Coordinate getMinMin() {
        return minMin;
    }

    public void setMinMin(Coordinate minMin) {
        this.minMin = minMin;
    }

    public Set<Coordinate> getAllCoordinates() {
        return allCoordinates;
    }

    public void setAllCoordinates(Set<Coordinate> allCoordinates) {
        this.allCoordinates = allCoordinates;
    }

    @Override
    public String toString() {
        return "Grid{" +
                "maxMax=" + maxMax +
                ", minMax=" + minMax +
                ", maxMin=" + maxMin +
                ", minMin=" + minMin +
                ", allCoordinates=" + allCoordinates +
                '}';
    }
}

class Coordinate {
    private int y;
    private int x;


    private boolean isInputCoordinate;

    Coordinate nearest;

    Integer distance;

    boolean isEquidistant;

    boolean isUnbounded;

    int distanceFromAll;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Coordinate(String line) {
        this.y = Integer.parseInt(line.substring(0, line.indexOf(",")).trim());
        this.x = Integer.parseInt(line.substring(line.indexOf(",") + 1, line.length()).trim());
    }

    public int calculateManhattanDistance(Coordinate to) {
        int xDistance = this.getX() - to.getX();
        if(xDistance < 0) {
            xDistance = -xDistance;
        }
        int yDistance = this.getY() - to.getY();
        if(yDistance < 0) {
            yDistance = -yDistance;
        }
        return xDistance + yDistance;
    }

    public void calculateNearest(Coordinate to) {
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

    public int getDistanceFromAll() {
        return distanceFromAll;
    }

    public int setDistanceFromAll(int distanceFromAll) {
        this.distanceFromAll += distanceFromAll;
        return distanceFromAll;
    }

    public boolean isUnbounded() {
        return isUnbounded;
    }

    public void setUnbounded(boolean unbounded) {
        isUnbounded = unbounded;
    }

    public boolean isEquidistant() {
        return isEquidistant;
    }

    public void setEquidistant(boolean equidistant) {
        isEquidistant = equidistant;
    }

    public boolean isInputCoordinate() {
        return isInputCoordinate;
    }

    public void setInputCoordinate(boolean inputCoordinate) {
        isInputCoordinate = inputCoordinate;
    }

    int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Coordinate getNearest() {
        return nearest;
    }

    public void setNearest(Coordinate nearest) {
        this.nearest = nearest;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
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