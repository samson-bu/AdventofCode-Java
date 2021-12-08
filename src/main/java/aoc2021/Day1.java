package aoc2021;

import aoc2021.util.HttpUtils;

import java.util.List;

/**
 * @author Samson
 */
public class Day1 extends AoC2021{
    private final static String INPUT_URL = "https://adventofcode.com/2021/day/1/input";

    public static void main(String[] args) {
        List<String> lines = HttpUtils.getLines(INPUT_URL, COOKIES, " ");
        System.out.println(partOne(lines));
        System.out.println(partTwo(lines));
    }
    private static int partOne(List<String> inputLines) {
        int count = 0;
        for (int i = 1; i < inputLines.size(); ++i) {
            if (Integer.parseInt(inputLines.get(i)) > Integer.parseInt(inputLines.get(i - 1))) {
                count++;
            }
        }
        return count;
    }

    private static int partTwo(List<String> inputLines) {
        int count = 0;
        for (int i = inputLines.size() - 1; i >= 3; --i) {
            if (Integer.parseInt(inputLines.get(i)) > Integer.parseInt(inputLines.get(i - 3))) {
                count++;
            }
        }

        return count;
    }

}
