package aoc2021;

import aoc2021.util.HttpUtils;

import java.util.List;

/**
 * @author Samson
 */
public class Day2 extends AoC2021{
    private final static String INPUT_URL = "https://adventofcode.com/2021/day/2/input";
    public static void main(String[] args){
        List<String> lines = HttpUtils.getLines(INPUT_URL, COOKIES, " ");
        System.out.println(partOne(lines));
        System.out.println(partTwo(lines));
    }

    private static int partOne(List<String> inputLines) {
        int[] sums = new int[] {0, 0, 0};
        int index = 0;
        for (int i = 0; i < inputLines.size(); ++i) {
            if ("forward".equalsIgnoreCase(inputLines.get(i))) {
                index = 0;
            } else if ("down".equalsIgnoreCase(inputLines.get(i))) {
                index = 1;
            } else if ("up".equalsIgnoreCase(inputLines.get(i))) {
                index = 2;
            } else {
                sums[index] += Integer.parseInt(inputLines.get(i));
            }
        }

        return sums[0] * Math.abs(sums[1] - sums[2]);
    }

    private static int partTwo(List<String> inputLines) {
        int aim = 0;
        int depth = 0, horizontal = 0;
        for (int i = 0; i < inputLines.size(); ++i) {
            if ("forward".equalsIgnoreCase(inputLines.get(i))) {
                horizontal += Integer.parseInt(inputLines.get(i+1));
                depth += (aim * Integer.parseInt(inputLines.get(i+1)));
            } else if ("down".equalsIgnoreCase(inputLines.get(i))) {
                aim += Integer.parseInt(inputLines.get(i+1));
            } else if ("up".equalsIgnoreCase(inputLines.get(i))) {
                aim -= Integer.parseInt(inputLines.get(i+1));
            }
        }
        return depth * horizontal;
    }


}
