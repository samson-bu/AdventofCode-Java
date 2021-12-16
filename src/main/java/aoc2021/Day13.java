package aoc2021;

import aoc2021.util.HttpUtils;
import org.jsoup.internal.StringUtil;

import java.util.*;

/**
 * @author Samson
 */
public class Day13 extends AoC2021 {
    private final static String INPUT_URL = "https://adventofcode.com/2021/day/13/input";

    public static void main(String[] args) {
        List<String> lines = HttpUtils.getLines(INPUT_URL, COOKIES);

//        lines = Arrays.asList("6,10",
//                "0,14",
//                "9,10",
//                "0,3",
//                "10,4",
//                "4,11",
//                "6,0",
//                "6,12",
//                "4,1",
//                "0,13",
//                "10,12",
//                "3,4",
//                "3,0",
//                "8,4",
//                "1,10",
//                "2,14",
//                "8,10",
//                "9,0",
//                "",
//                "fold along y=7",
//                "fold along x=5");
        Day13 day = new Day13();
        System.out.println(day.partOne(lines));
        System.out.println(day.partTwo(lines));
    }

    public int partOne(List<String> lines) {
        List<String> positions = lines;
        List<String> operations = lines;
        for (int i = 0; i < lines.size(); ++i) {
            if (StringUtil.isBlank(lines.get(i))) {
                positions = lines.subList(0, i);
                operations = lines.subList(i + 1, lines.size());
                break;
            }
        }

        String[] s = operations.get(0).trim().split(" ")[2].trim().split("=");

        int size = this.ford(positions, s[0].trim(), s[1].trim()).size();

        return size;
    }

    private List<String> ford(List<String> positions, String dir, String value) {
        Integer v = Integer.parseInt(value);

        Set<String> result = new HashSet<>();
        if ("x".equals(dir)) {
            // 若是竖折
            for (String position : positions) {
                String x = position.trim().split(",")[0].trim();
                if (Integer.parseInt(x) > v) {
                    String s = new String((v * 2 - Integer.parseInt(x)) + position.substring(position.indexOf(',')));
                    result.add(s);
                } else if (Integer.parseInt(x) < v) {
                    result.add(position);
                    position.intern();
                }
            }
            return new ArrayList<>(result);
        } else if ("y".equals(dir)) {
            // 若是横折
            for (String position : positions) {
                String y = position.trim().split(",")[1].trim();
                if (Integer.parseInt(y) > v) {
                    String s = new String(position.substring(0, position.indexOf(',') + 1) + (v * 2 - Integer.parseInt(y)));
                    result.add(s);
                } else if (Integer.parseInt(y) < v) {
                    result.add(position);
                    position.intern();
                }
            }
            return new ArrayList<>(result);

        } else {
            return Collections.emptyList();
        }
    }

    public int partTwo(List<String> lines) {
        List<String> positions = lines;
        List<String> operations = lines;
        for (int i = 0; i < lines.size(); ++i) {
            if (StringUtil.isBlank(lines.get(i))) {
                positions = lines.subList(0, i);
                operations = lines.subList(i + 1, lines.size());
                break;
            }
        }
        int x = Integer.MAX_VALUE, y = Integer.MAX_VALUE;

        for (int i = 0; i < operations.size(); ++i) {
            String[] s = operations.get(i).trim().split(" ")[2].trim().split("=");
             positions = this.ford(positions, s[0].trim(), s[1].trim());
             if ("x".equals(s[0].trim())) {
                 if (x > Integer.parseInt(s[1].trim())) {
                     x = Integer.parseInt(s[1].trim());
                 }
             } else if ("y".equals(s[0].trim())) {
                 if (y > Integer.parseInt(s[1].trim())) {
                     y = Integer.parseInt(s[1].trim());
                 }
             }
        }
        this.printf(positions, x, y);
        return positions.size();
    }

    private void printf(List<String> positions, int x, int y) {
        int[][] view = new int[y][x];
        for (int[] ints : view) {
            Arrays.fill(ints, 0);
        }
        for (int i = 0; i < positions.size(); ++i) {
            String[] split = positions.get(i).trim().split(",");
            view[Integer.parseInt(split[1].trim())][Integer.parseInt(split[0].trim())] = 1;
        }

        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; ++j) {
                if (view[i][j] == 0) {
                    System.out.print(". ");
                } else {
                    System.out.print("# ");
                }
            }
            System.out.println();
        }
    }
}
