package aoc2021;

import aoc2021.util.HttpUtils;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author Samson
 */
public class Day15 extends AoC2021 {
    private final static String INPUT_URL = "https://adventofcode.com/2021/day/15/input";


    public static void main(String[] args) {
        List<String> lines = HttpUtils.getLines(INPUT_URL, COOKIES);
//        lines = Arrays.asList(
//                "1163751742",
//                "1381373672",
//                "2136511328",
//                "3694931569",
//                "7463417111",
//                "1319128137",
//                "1359912421",
//                "3125421639",
//                "1293138521",
//                "2311944581");

        Day15 day = new Day15();
        System.out.println(day.partOne(lines));
        System.out.println(day.partTwo(lines));
    }
    public int partOne(List<String> lines) {
        int[][] riskMap = this.genMap(lines);

        return findMinimalRiskPath(riskMap);
    }

    private int findMinimalRiskPath(int[][] map) {
        int[][] visited = new int[map.length][map[0].length];
        for (int[] ints : visited) {
            Arrays.fill(ints, -1);
        }
        visited[0][0] = 0;

        Queue<Pair<Integer, Integer>> workingQueue = new LinkedList<>();
        workingQueue.add(new Pair<>(0, 0));

        // directions: up down left right
        int[][] dir = new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        while (!workingQueue.isEmpty()) {
            Pair<Integer, Integer> poll = workingQueue.poll();
            int i = poll.getKey();
            int j = poll.getValue();
            for (int x = 0; x < dir.length; ++x) {
                int nextI = i + dir[x][0];
                int nextJ = j + dir[x][1];

                if (!valid(map, nextI, nextJ)) {
                    continue;
                }

                int risk =  visited[i][j] + map[nextI][nextJ];
                if (visited[nextI][nextJ] == -1 || visited[nextI][nextJ] > risk) {
                    // 尚未被赋值，即第一次 || 曾赋值，但却较大
                    visited[nextI][nextJ] = risk;
                    workingQueue.add(new Pair<>(nextI, nextJ));
                }
            }
        }

        return visited[visited.length - 1][visited[0].length - 1];
    }

    private boolean valid(int[][] map, int i, int j) {
        if (i < 0 || i >= map.length) {
            return false;
        }

        if (j < 0 || j >= map[0].length) {
            return false;
        }

        return true;
    }


    public int partTwo(List<String> lines) {
        int[][] tile = this.genMap(lines);
        int[][] map = this.genEntireMap(tile);

        return findMinimalRiskPath(map);
    }

    private int[][] genMap(List<String> lines) {
        int x = lines.size();
        int y = lines.get(0).trim().length();
        int[][] map = new int[x][y];

        for (int i = 0; i < x; ++i) {
            for (int j = 0; j < y; ++j) {
                map[i][j] = lines.get(i).charAt(j) - '0';
            }
        }

        return map;
    }

    private int[][] genEntireMap(int[][] tile) {
        int[][] map = new int[tile.length * 5][tile[0].length * 5];

        for (int x = 0; x < 5; ++x) {
            for (int y = 0; y < 5; ++y) {
                int i = tile.length * x;
                int j = tile[0].length * y;
                int s = x + y;

                fill(map, i, j, tile, s);
            }
        }

        return map;
    }

    private void fill(int[][] map, int i, int j, int[][] tile, int s) {
        for (int ti = 0; ti < tile.length; ++ti) {
            for (int tj = 0; tj < tile[ti].length; ++tj) {
                int x = tile[ti][tj] + s;
                map[i + ti][j + tj] = x > 9 ? x - 9 : x;
            }
        }
    }
}
