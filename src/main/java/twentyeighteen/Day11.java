package twentyeighteen;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day11 {

    static Map<String, Integer> cellMap = new HashMap<>();

    public static void main(String[] args) {

        List<Cell> cells = IntStream.range(1, 301)
                .mapToObj(y -> IntStream.range(1, 301)
                        .mapToObj(x -> new Cell(x, y))
                        .collect(Collectors.toList()))
                .flatMap(List::stream)
                .collect(Collectors.toList());

        cellMap = cells.stream().collect(Collectors.toMap( cell -> cell.getX() + "," + cell.getY(), Cell::getPower));


        cells.stream().parallel()
                .filter(Cell::calculateGridPower)
                .max(Comparator.comparingInt(Cell::getGridPower)).ifPresent(System.out::println);

    }
}


class Cell {

    private int x;

    private int y;

    private int power;

    private int gridPower;

    private int gridSize;

    Cell(int x, int y) {
        this.x = x;
        this.y = y;

        int rackId = x + 10;
        power = rackId * y;
        power += 6548;
        power *= rackId;
        String powerString = Integer.valueOf(power).toString();
        power = powerString.length() > 2 ? Integer.parseInt(powerString.substring(powerString.length() - 3, powerString.length() - 2)) : 0;
        power -= 5;
    }

    boolean calculateGridPower() {

        int gridSizeLimit = x < y ? 301 - x : 301 - y;

        Map<Integer, Integer> gridPowersMap = new HashMap<>();
        for (int i = 1; i < gridSizeLimit; i++) {
            final int j = i;
            int sum = IntStream.range(x, x + j)
                    .filter(x -> x < 301)
                    .mapToObj(x -> IntStream.range(y, y + j)
                            .filter(y -> y < 301)
                            .mapToObj(y -> x + "," + y)
                            .collect(Collectors.toList())
                    )
                    .flatMap(List::stream)
                    .map(Day11.cellMap::get)
                    .mapToInt(Integer::valueOf)
                    .sum();
            if (sum >= 40) {
                gridPowersMap.put(i, sum);
            }
        }
        if (gridPowersMap.isEmpty()) {
            return false;
        } else {
            Optional<Map.Entry<Integer, Integer>> max = gridPowersMap.entrySet().stream().max(Map.Entry.comparingByValue());
            if(max.isEmpty()) {
                return false;
            } else {
                this.gridSize = max.get().getKey();
                this.gridPower = max.get().getValue();
                System.out.println("this = " + this);
                return true;
            }
        }
    }

    int getGridPower() {
        return gridPower;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    int getPower() {
        return power;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return getX() == cell.getX() &&
                getY() == cell.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }

    @Override
    public String toString() {
        return "Cell{" +
                "x=" + x +
                ", y=" + y +
                ", power=" + power +
                ", gridPower=" + gridPower +
                ", gridSize=" + gridSize +
                '}';
    }
}
