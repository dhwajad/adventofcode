package twentyeighteen;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day4 {

    public static void main(String[] args) throws URISyntaxException, IOException {

        List<String> strings = Files.readAllLines(Path.of(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("day4")).toURI()));

        Map<Integer, List<Shift>> collect = strings.stream()
                .map(Shift::new)
                .sorted()
                .map(Shift::setGuardId)
                .filter(Shift::isSleepRelatedEvent)
                .collect(Collectors.groupingBy(Shift::getGuardId));

        //System.out.println("collect = " + collect);

        collect.keySet().stream()
                .map(guardId -> new Guard(guardId, collect.get(guardId)))
                .map(Guard::setMinMaxAsleep)
                .sorted(Comparator.comparing(Guard::getTotalMinAsleep).reversed())
                .forEach(System.out::println);

        Integer part1 = collect.keySet().stream()
                .map(guardId -> new Guard(guardId, collect.get(guardId)))
                .map(Guard::setMinMaxAsleep)
                .max(Comparator.comparing(Guard::getTotalMinAsleep))
                .map(guard -> guard.getGuardId() * guard.getMinMaxAsleep())
                .orElse(0);

        System.out.println("part1 = " + part1);

        Integer part2 = collect.keySet().stream()
                .map(guardId -> new Guard(guardId, collect.get(guardId)))
                .map(Guard::setMinMaxAsleep)
                .max(Comparator.comparing(Guard::getMinMaxAsleepCount))
                .map(guard -> guard.getGuardId() * guard.getMinMaxAsleep())
                .orElse(0);

        System.out.println("part2 = " + part2);
    }
}

class Guard {

    private int guardId;

    private volatile int sleepStartMin;

    private Map<Integer, Integer> sleepPattern = new HashMap<>();

    private int totalMinAsleep;

    private int minMaxAsleep;

    private int minMaxAsleepCount;

    int getTotalMinAsleep() {
        return totalMinAsleep;
    }

    int getGuardId() {
        return guardId;
    }

    int getMinMaxAsleep() {
        return minMaxAsleep;
    }

    int getMinMaxAsleepCount() {
        return minMaxAsleepCount;
    }

    public Guard(int guardId, List<Shift> shifts) {
        this.guardId = guardId;
        shifts.forEach(shift -> {
            if (shift.getEvent().equals("falls asleep")) {
                sleepStartMin = shift.getEventTime().getMinute();
            } else {
                int sleepEndMin = shift.getEventTime().getMinute();
                IntStream.range(sleepStartMin, sleepEndMin)
                        .forEach(min -> {
                            if (sleepPattern.containsKey(min)) {
                                int count = sleepPattern.get(min);
                                sleepPattern.put(min, count + 1);

                            } else {
                                sleepPattern.put(min, 1);
                            }
                            totalMinAsleep++;
                        });
            }
        });

    }

    Guard setMinMaxAsleep() {
        Optional<Map.Entry<Integer, Integer>> max = this.sleepPattern.entrySet().stream()
                .max(Comparator.comparing(Map.Entry::getValue));
        minMaxAsleep = max.get().getKey();
        minMaxAsleepCount = max.get().getValue();
        return this;
    }

    @Override
    public String toString() {
        return "Guard{" +
                "guardId=" + guardId +
                ", minMaxAsleep=" + minMaxAsleep +
                ", minMaxAsleepCount=" + minMaxAsleepCount +
                ", totalMinAsleep=" + totalMinAsleep +
                '}';
    }
}

class Shift implements Comparable {

    private LocalDateTime eventTime;

    private String event;

    private int guardId;

    private static int tempId;

    boolean isSleepRelatedEvent() {
        return !this.getEvent().contains("#");
    }

    Shift setGuardId() {
        if (this.getEvent().contains("#")) {
            tempId = Integer.parseInt(this.getEvent().substring(this.getEvent().indexOf("#") + 1, this.getEvent().indexOf("begins")).trim());
        }
        guardId = tempId;
        return this;
    }

    int getGuardId() {
        return guardId;
    }

    public void setGuardId(int guardId) {
        this.guardId = guardId;
    }

    Shift(String line) {
        this.eventTime = LocalDateTime.parse(line.substring(line.indexOf("[") + 1, line.indexOf("]")).trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.event = line.substring(line.indexOf("]") + 1).trim();
    }

    LocalDateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
    }

    String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shift shift = (Shift) o;
        return Objects.equals(getEventTime(), shift.getEventTime()) &&
                Objects.equals(getEvent(), shift.getEvent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEventTime(), getEvent());
    }

    @Override
    public String toString() {
        return "Shift{" +
                "eventTime=" + eventTime +
                ", event='" + event + '\'' +
                ", guardId=" + guardId +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        return eventTime.compareTo(((Shift) o).getEventTime());
    }
}
