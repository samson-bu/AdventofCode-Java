package aoc2021;

import aoc2021.util.HttpUtils;
import javafx.util.Pair;

import java.util.*;

/**
 * @author Samson
 */
public class Day11 extends AoC2021 {
    private final static String INPUT_URL = "https://adventofcode.com/2021/day/11/input";

    public static void main(String[] args) {
        List<String> lines = HttpUtils.getLines(INPUT_URL, COOKIES);
//        lines = Arrays.asList("5483143223",
//                "2745854711",
//                "5264556173",
//                "6141336146",
//                "6357385478",
//                "4167524645",
//                "2176841721",
//                "6882881134",
//                "4846848554",
//                "5283751526");

        Day11 day = new Day11();
        System.out.println(day.partOne(lines));
        System.out.println(day.partTwo(lines));
    }

    public int partOne(List<String> lines) {
        int[][] energyMap = this.genEnergyMap(lines);
        int count = 0;
        for (int i = 0; i < 100; ++i) {
            Queue<Pair<Integer, Integer>> workingQueue = this.step1(energyMap);
            List<Pair<Integer, Integer>> flashes = this.step2(energyMap, workingQueue);
            count += flashes.size();
            this.step3(energyMap, flashes);
        }

        return count;
    }

    public int partTwo(List<String> lines) {
        int[][] energyMap = this.genEnergyMap(lines);
        int count = 1;
        while (true) {
            Queue<Pair<Integer, Integer>> workingQueue = this.step1(energyMap);
            List<Pair<Integer, Integer>> flashes = this.step2(energyMap, workingQueue);
            if (flashes.size() == energyMap.length * energyMap[0].length) {
                break;
            }
            this.step3(energyMap, flashes);
            count++;
        }
        return count;
    }

    private int[][] genEnergyMap(List<String> lines) {
        int[][] energyMap = new int[lines.size()][lines.get(0).trim().length()];
        for (int i = 0; i < lines.size(); ++i) {
            for (int j = 0; j < lines.get(i).trim().length(); ++j) {
                energyMap[i][j] = lines.get(i).trim().charAt(j) - '0';
            }
        }
        return energyMap;
    }

    private Queue<Pair<Integer, Integer>> step1(int[][] energyMap) {
        Queue<Pair<Integer, Integer>> workingQueue = new LinkedList<>();

        for (int i = 0; i < energyMap.length; ++i) {
            for (int j = 0; j < energyMap[i].length; ++j) {
                energyMap[i][j] += 1;
                if (energyMap[i][j] > 9) {
                    workingQueue.offer(new Pair<>(i, j));
                }
            }
        }
        return workingQueue;
    }

    private List<Pair<Integer, Integer>> step2(int[][] energyMap, Queue<Pair<Integer, Integer>> workingQueue) {
        List<Pair<Integer, Integer>> flashes = new ArrayList<>();
        int[][] dir = new int[][] {
                // up
                {-1, 0},
                // down
                {1, 0},
                // left
                {0, -1},
                // right
                {0, 1},
                // up-left
                {-1, -1},
                // up-right
                {-1, 1},
                // down-left
                {1, -1},
                // down-right
                {1, 1}
        };

        boolean[][] visited = new boolean[energyMap.length][energyMap[0].length];
        for (boolean[] v : visited) {
            Arrays.fill(v, false);
        }

        workingQueue.forEach(e -> visited[e.getKey()][e.getValue()] = true);

        while (!workingQueue.isEmpty()) {
            Pair<Integer, Integer> poll = workingQueue.poll();
            flashes.add(poll);
            for (int x = 0; x < dir.length; ++x) {
                int nextI = poll.getKey() + dir[x][0];
                int nextJ = poll.getValue() + dir[x][1];

                if (!valid(energyMap, nextI, nextJ)) {
                    continue;
                }
                energyMap[nextI][nextJ]++;
                if (energyMap[nextI][nextJ] > 9 && !visited[nextI][nextJ]) {
                    workingQueue.offer(new Pair<>(nextI, nextJ));
                    visited[nextI][nextJ] = true;
                }

            }

        }

        return flashes;
    }

    private boolean valid(int[][] energyMap, int i, int j) {
        if (i < 0 || i >= energyMap.length) {
            return false;
        }

        if (j < 0 || j >= energyMap[0].length) {
            return false;
        }

        return true;
    }

    private void step3(int[][] energyMap, List<Pair<Integer, Integer>> flashes) {
        flashes.forEach(e -> energyMap[e.getKey()][e.getValue()] = 0);
    }
}
