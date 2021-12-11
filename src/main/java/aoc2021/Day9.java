package aoc2021;

import aoc2021.util.HttpUtils;
import javafx.util.Pair;

import java.util.*;

/**
 * @author Samson
 */
public class Day9 extends AoC2021 {
    private final static String INPUT_URL = "https://adventofcode.com/2021/day/9/input";

    public static void main(String[] args) {
        List<String> lines = HttpUtils.getLines(INPUT_URL, COOKIES);
//        lines = Arrays.asList("2199943210",
//                "3987894921",
//                "9856789892",
//                "8767896789",
//                "9899965678");
        Day9 day = new Day9();
        System.out.println(day.partOne(lines));
        System.out.println(day.partTwo(lines));
    }

    public int partOne(List<String> lines) {
        int[][] map = genMap(lines);

        int count = 0;
        for (int i = 0; i < map.length; ++i) {
            for (int j = 0; j < map[i].length; ++j) {
                if (isLowPoint(map, i, j)) {
                    count += (map[i][j] + 1);
                }
            }
        }

        return count;
    }

    private int[][] genMap(List<String> lines) {
        int[][] map = new int[lines.size()][lines.get(0).trim().length()];
        for (int j = 0; j < lines.size(); ++j) {
            String line = lines.get(j).trim();
            for(int i = 0; i < line.length(); ++i) {
                map[j][i] = line.charAt(i) - '0';
            }
        }
        return map;
    }
    private boolean isLowPoint(int[][] map, int i, int j) {
        // i == 0, no up
        // i == length - 1, no down
        // j == 0, no left
        // j == length - 1, no right
        int[][] dir = new int[][] {
                // up
                {-1, 0},
                // down
                {1, 0},
                // left
                {0, -1},
                // right
                {0, 1}
        };

        for (int x = 0; x < dir.length; ++x) {
            if (i == 0) {
                if (x == 0) {
                    continue;
                }
            } else if (i == (map.length - 1)) {
                if (x == 1) {
                    continue;
                }
            }

            if (j == 0) {
                if (x == 2) {
                    continue;
                }
            } else if (j == (map[i].length - 1)) {
                if (x == 3) {
                    continue;
                }
            }
            if (map[i][j] >= map[i + dir[x][0]][j + dir[x][1]]) {
                return false;
            }
        }

        return true;
    }

    public int partTwo(List<String> lines) {
        List<Integer> sizeOfBasin = new ArrayList<>();

        int[][] map = genMap(lines);
        for (int i = 0; i < map.length; ++i) {
            for (int j = 0; j < map[i].length; ++j) {
                if (isLowPoint(map, i, j)) {
                    sizeOfBasin.add(this.calSizeOfBasin(map, i, j));
                }
            }
        }

        Optional<Integer> reduce = sizeOfBasin.stream().sorted(Comparator.reverseOrder()).limit(3).reduce((a, b) -> a * b);

        return reduce.get();
    }

    private int calSizeOfBasin(int[][] map, int i, int j) {

        int[][] dir = new int[][] {
                // up
                {-1, 0},
                // down
                {1, 0},
                // left
                {0, -1},
                // right
                {0, 1}
        };

        int row = map.length, column = map[0].length;

        boolean[][] visited = new boolean[row][column];
        for (boolean[] booleans : visited) {
            Arrays.fill(booleans, false);
        }

        int size = 1;
        visited[i][j] = true;

        Queue<Pair<Integer, Integer>> workingQueue = new LinkedList<>();
        workingQueue.add(new Pair<>(i, j));

        while (!workingQueue.isEmpty()) {
            Pair<Integer, Integer> poll = workingQueue.poll();
            for (int k = 0; k < dir.length; ++k) {
                int nextI = poll.getKey() + dir[k][0];
                int nextJ = poll.getValue() + dir[k][1];

                if (scope(nextI, nextJ, row, column) && !visited[nextI][nextJ] && map[nextI][nextJ] < 9) {
                    workingQueue.add(new Pair<>(nextI, nextJ));
                    visited[nextI][nextJ] = true;
                    size++;
                }
            }
        }

        return size;
    }

    private boolean scope(int i, int j, int row, int column) {
        if (i < 0 || i > row - 1) {
            return false;
        }
        if (j < 0 || j > column - 1) {
            return false;
        }

        return true;
    }
}
