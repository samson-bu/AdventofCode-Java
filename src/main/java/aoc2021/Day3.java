package aoc2021;

import aoc2021.util.HttpUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Samson
 */
public class Day3 extends AoC2021 {
    private final static String INPUT_URL = "https://adventofcode.com/2021/day/3/input";

    public static void main(String[] args) {
        List<String> lines = HttpUtils.getLines(INPUT_URL, COOKIES, " ");
        System.out.println(partOne(lines));
        System.out.println(partTwo(lines));
    }

    private static int partOne(List<String> inputLines) {
        Map<Integer, Integer> counts = new HashMap<>(16);
        for (String inputLine : inputLines) {
            for (int i = 0; i < inputLine.length(); ++i) {
                if (counts.containsKey(i)) {
                    counts.put(i, counts.get(i) + (inputLine.charAt(i) - '0'));
                } else {
                    counts.put(i, (inputLine.charAt(i) - '0'));
                }
            }
        }
        int gamma = 0, epsilon = 0;
        int factor = 1;
        for (int i = counts.keySet().size() - 1; i >= 0; --i) {
            if (counts.get(i) > (inputLines.size() / 2)) {
                gamma += factor;
            } else {
                epsilon += factor;
            }
            factor <<= 1;
        }
        return gamma * epsilon;
    }

    private static int partTwo(List<String> inputLines) {

        List<String> list = inputLines;
        int position = 0;
        while ((list = searchingOxygen(list, position)).size() > 1) {
            position++;
        }
        int oxygen = Integer.parseInt(list.get(0), 2);
        list = inputLines;
        position = 0;
        while ((list = searchingCarbon(list, position)).size() > 1) {
            position++;
        }
        int carbon = Integer.parseInt(list.get(0), 2);

        return oxygen * carbon;
    }

    private static List<String> searchingOxygen(List<String> inputLines, int position) {
        int[] counts = new int[] {0, 0};
        for (String inputLine : inputLines) {
            counts[inputLine.charAt(position) - '0']++;
        }
        char bit = counts[0] <= counts[1] ? '1' : '0';
        return inputLines.stream().filter(e -> (e.charAt(position) == bit)).collect(Collectors.toList());
    }
    private static List<String> searchingCarbon(List<String> inputLines, int position) {
        int[] counts = new int[] {0, 0};
        for (String inputLine : inputLines) {
            counts[inputLine.charAt(position) - '0']++;
        }
        char bit = counts[0] <= counts[1] ? '0' : '1';
        return inputLines.stream().filter(e -> (e.charAt(position) == bit)).collect(Collectors.toList());
    }
}
