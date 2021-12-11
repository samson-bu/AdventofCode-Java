package aoc2021;

import aoc2021.util.HttpUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Samson
 */
public class Day7 extends AoC2021 {
    private final static String INPUT_URL = "https://adventofcode.com/2021/day/7/input";

    public static void main(String[] args) {
        List<String> lines = HttpUtils.getLines(INPUT_URL, COOKIES);
        Day7 day = new Day7();
//        lines.set(0, "16,1,2,0,4,2,7,1,2,14");

        System.out.println(day.partOne(lines));
        System.out.println(day.partTwo(lines));
    }

    public int partOne(List<String> lines) {
        String[] split = lines.get(0).trim().split(",");
        ArrayList<Integer> collect = Arrays.stream(split)
                .map(Integer::parseInt)
                .sorted()
                .collect(Collectors.toCollection(ArrayList::new));

        ArrayList<Integer> sum = new ArrayList<>(collect.size());
        sum.add(collect.get(0));
        for (int i = 1; i < collect.size(); ++i) {
            sum.add(sum.get(i - 1) + collect.get(i));
        }
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < collect.size(); ++i) {
            int right = 0, left = 0;
            if (i == 0) {
                right = sum.get(collect.size() - 1) - sum.get(0) - collect.get(0) * (collect.size() - 1);
            } else if (i == collect.size() - 1) {
                left = collect.get(collect.size() - 1) * (collect.size() - 1) - sum.get(collect.size() - 2);
            } else {
                int stripR = collect.size() - 1 - i;

                left = i * collect.get(i) - sum.get(i - 1);
                right = (sum.get(collect.size() - 1) - sum.get(i)) - stripR * collect.get(i);
            }

            if (min > left + right) {
                min = left + right;
            }
        }

        return min;
    }

    public long partTwo(List<String> lines) {
        String[] split = lines.get(0).trim().split(",");
        ArrayList<Long> collect = Arrays.stream(split)
                .map(Long::parseLong)
                .sorted()
                .collect(Collectors.toCollection(ArrayList::new));
        ArrayList<Long> sum = new ArrayList<>(collect.size());
        ArrayList<Long> powSum = new ArrayList<>(collect.size());
        sum.add(collect.get(0));
        powSum.add(collect.get(0) * collect.get(0));
        for (int i = 1; i < collect.size(); ++i) {
            sum.add(sum.get(i - 1) + collect.get(i));
            powSum.add(powSum.get(i - 1) + collect.get(i) * collect.get(i));
        }
        long min = Long.MAX_VALUE;
        long pS = powSum.get(collect.size() - 1);
        long sS = sum.get(collect.size() - 1);

        // 1. 到某个crab处，最小的燃料数
        List<Long> mins = new ArrayList<>();
        for (int i = 0; i < collect.size(); ++i) {
            long a = pS + (collect.size() - 2) * collect.get(i) * collect.get(i);
            long b = -2 * collect.get(i) * (sS - collect.get(i));
            long c = 0;
            if (i == 0) {
                c = sS - collect.get(i) - (collect.size() - 1) * collect.get(i);
            } else if (i == collect.size() - 1) {
                c = (collect.size() - 1) * collect.get(i) - sum.get(collect.size() - 2);
            } else {
                c = sS - sum.get(i) - (collect.size() - 1 - i) * collect.get(i) + i * collect.get(i) - sum.get(i - 1);
            }

            long s = a + b + c;
            if (min > s) {
                min = s;
            }
            mins.add(s);
        }

        min = min / 2;
        // 2. 任意两个crab之间，是否有更优的解
        for (int i = 1; i < collect.size(); ++i) {
            if (collect.get(i) - collect.get(i - 1) > 1) {
                int left = i - 1;
                for (long j = collect.get(left) + 1; j < collect.get(i); ++j ) {
                    long x = j * (left + 1) - sum.get(left);
                    long y = sS - sum.get(left) - (collect.size() - 1 - left) * collect.get(left);
                    long tmp = mins.get(left) / 2 + x - y;
                    if (tmp < min) {
                        min = tmp;
                    }
                }
            }
        }

        return min;
    }
}
