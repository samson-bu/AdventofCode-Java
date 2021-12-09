package aoc2021;

import aoc2021.util.HttpUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author Samson
 */
public class Day6 extends AoC2021 {
    private final static String INPUT_URL = "https://adventofcode.com/2021/day/6/input";

    public static void main(String[] args) {
        Day6 day = new Day6();
        List<String> lines = HttpUtils.getLines(INPUT_URL, COOKIES);
        int days = 80;

        System.out.println(day.partOne(lines, days));
        System.out.println(day.partOne(lines, 256));
    }

    public long partOne(List<String> lines, int days) {
        long[] counter = new long[]{0, 0, 0, 0, 0, 0, 0, 0, 0};

        String[] split = lines.get(0).trim().split(",");
        for (String s : split) {
            counter[Integer.parseInt(s)]++;
        }

        int zero = 0;
        long zt = 0;
        int len = counter.length;
        for (int d = 1; d <= days; ++d) {
            zt = counter[zero];
            counter[zero] = 0;
            zero = (zero + 1) % len;

            counter[(zero + 6) % len] += zt;
            counter[(zero + 8) % len] += zt;

        }

        return Arrays.stream(counter).sum();
    }

    public int partTwo(List<String> lines, int days) {
        return 0;
    }
}
