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


        Cell cell = cells.stream()
                .max(Comparator.comparingInt(Cell::gridPower))
                .orElse(null);

        if(cell != null) {
            System.out.println(cell);
            System.out.println(cell.gridPower());
        }

    }
}


class Cell {

    private int x;

    private int y;

    private int power;

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

    int gridPower() {
        return IntStream.range(x, x + 3)
                .filter(x -> x < 301)
                .mapToObj(x -> IntStream.range(y, y + 3)
                        .filter(y -> y < 301)
                        .mapToObj( y -> x + "," + y)
                        .collect(Collectors.toList()))
                .flatMap(List::stream)
                .map(Day11.cellMap::get)
                .mapToInt(Integer::valueOf)
                .sum();
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
                '}';
    }
}
