package aoc2021;

import aoc2021.util.HttpUtils;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Samson
 */
public class Day5 extends AoC2021 {
    private final static String INPUT_URL = "https://adventofcode.com/2021/day/5/input";

    public static void main(String[] args) {
        List<String> lines = HttpUtils.getLines(INPUT_URL, COOKIES);
//        lines = Arrays.asList("0,9 -> 5,9",
//                "8,0 -> 0,8",
//                "9,4 -> 3,4",
//                "2,2 -> 2,1",
//                "7,0 -> 7,4",
//                "6,4 -> 2,0",
//                "0,9 -> 2,9",
//                "3,4 -> 1,4",
//                "0,0 -> 8,8",
//                "5,5 -> 8,2");

        Day5 day = new Day5();

        System.out.println(day.partOne(lines));
        System.out.println(day.partTwo(lines));
    }

    public int partOne(List<String> lines) {

        Predicate<int[][]> horizontalOrVerticalPredicate = (coordinates) ->
                (coordinates[0][0] == coordinates[1][0]) || (coordinates[0][1] == coordinates[1][1]) ;

        List<int[][]> collect = lines.stream().map(this::line2Coordinates).filter(horizontalOrVerticalPredicate).collect(Collectors.toList());

        Map<Integer, Integer> count = new HashMap<>(4096);
        for (int[][] ints : collect) {
            this.mark(count, ints);
        }

        int number = 0;
        for (Map.Entry<Integer, Integer> entry : count.entrySet()) {
            if (entry.getValue() > 1) {
                number++;
            }
        }
        return number;
    }

    public int partTwo(List<String> lines) {
        Predicate<int[][]> horizontalOrVerticalPredicate = (coordinates) ->
                (coordinates[0][0] == coordinates[1][0])
                        || (coordinates[0][1] == coordinates[1][1])
                        || Math.abs(coordinates[0][0] - coordinates[1][0]) == Math.abs(coordinates[0][1] - coordinates[1][1]);
        List<int[][]> collect = lines.stream().map(this::line2Coordinates).filter(horizontalOrVerticalPredicate).collect(Collectors.toList());

        Map<Integer, Integer> count = new HashMap<>(4096);
        for (int[][] ints : collect) {
            this.mark(count, ints);
        }

        int number = 0;
        for (Map.Entry<Integer, Integer> entry : count.entrySet()) {
            if (entry.getValue() > 1) {
                number++;
            }
        }
        return number;
    }

    private void mark(Map<Integer, Integer> count, int[][] coordinates) {
        if (coordinates[0][0] == coordinates[1][0]) {
            // 若是横着的
            int base = coordinates[0][0] * 1000;
            for (int i = Math.min(coordinates[0][1], coordinates[1][1]); i <= Math.max(coordinates[0][1], coordinates[1][1]); ++i) {
                int key = base + i;
                count.computeIfPresent(key, (k, v) -> v + 1);
                count.putIfAbsent(key, 1);
            }

        } else if (coordinates[0][1] == coordinates[1][1]){
            // 若是竖着的
            int base = coordinates[0][1];

            for (int i = Math.min(coordinates[0][0], coordinates[1][0]); i <= Math.max(coordinates[0][0], coordinates[1][0]); ++i) {
                int key = i * 1000 + base;
                count.computeIfPresent(key, (k, v) -> v + 1);
                count.putIfAbsent(key, 1);
            }
        } else if (Math.abs(coordinates[0][0] - coordinates[1][0]) == Math.abs(coordinates[0][1] - coordinates[1][1])) {
            int x = (coordinates[0][0] - coordinates[1][0]) / Math.abs(coordinates[0][0] - coordinates[1][0]);
            x *= -1;
            int y = (coordinates[0][1] - coordinates[1][1]) / Math.abs(coordinates[0][1] - coordinates[1][1]);
            y *= -1;

            for (int i = 0; i <= Math.abs(coordinates[0][0] - coordinates[1][0]); ++i) {
                int key = (coordinates[0][0] + x * i) * 1000 + (coordinates[0][1] + y * i);
                count.computeIfPresent(key, (k, v) -> v + 1);
                count.putIfAbsent(key, 1);
            }
        }
    }

    private int[][] line2Coordinates(String line) {
        String[] coordinates = line.trim().split("->");
        int x1 = Integer.parseInt(coordinates[0].trim().split(",")[0]);
        int y1 = Integer.parseInt(coordinates[0].trim().split(",")[1]);

        int x2 = Integer.parseInt(coordinates[1].trim().split(",")[0]);
        int y2 = Integer.parseInt(coordinates[1].trim().split(",")[1]);

        return new int[][]{{x1, y1}, {x2, y2}};
    }
}
