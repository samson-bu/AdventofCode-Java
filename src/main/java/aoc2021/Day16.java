package aoc2021;

import aoc2021.util.HttpUtils;

import java.math.BigInteger;
import java.util.*;

/**
 * @author Samson
 */
public class Day16 extends AoC2021 {
    private static final String INPUT_URL = "https://adventofcode.com/2021/day/16/input";

    public static void main(String[] args) {
        List<String> lines = HttpUtils.getLines(INPUT_URL, COOKIES);
//        lines = Arrays.asList("9C0141080250320F1802104A08");

        Day16 day = new Day16();
        System.out.println(day.partOne(lines));
        System.out.println(day.partTwo(lines));
    }

    public int partOne(List<String> lines) {
        String str = this.hex2Bin(lines.get(0).trim());

        Pocket pocket = new Pocket();
        pocket.parse(str, 0);

        return traverse(pocket);
    }

    public long partTwo(List<String> lines) {
        String str = this.hex2Bin(lines.get(0).trim());

        Pocket pocket = new Pocket();
        pocket.parse(str, 0);

        return cal(pocket);
    }

    private static class Pocket {
        private String version;
        private String type;
        private String content;
        private Integer nextStart;
        private List<Pocket> subPockets;

        public Pocket parse(String str, int start) {
            this.version = str.substring(start, start + 3);
            this.type = str.substring(start + 3, start + 6);
            this.subPockets = new ArrayList<>();

            if ("100".equals(type)) {
                StringBuilder sb = new StringBuilder();
                int i = start + 6;
                while (str.charAt(i) == '1') {
                    sb.append(str, i + 1, i + 5);
                    i += 5;
                }
                sb.append(str, i + 1, i + 5);
                this.content = sb.toString();
                this.nextStart = i + 5;
            } else {
                int i =  start + 6;
                String strLen = "";
                if (str.charAt(i) == '1') {
                    strLen = str.substring(i + 1, i + 12);
                    i = i + 12;
                    int len = new BigInteger(strLen, 2).intValue();
                    for (int j = 0; j < len; ++j) {
                        Pocket pocket = new Pocket();
                        subPockets.add(pocket.parse(str, i));
                        i = pocket.nextStart;
                    }
                    this.nextStart = i;
                } else {
                    strLen = str.substring(i + 1, i + 16);
                    i = i + 16;
                    int len = new BigInteger(strLen, 2).intValue();
                    int t = 0;
                    while (t < len) {
                        Pocket pocket = new Pocket();
                        subPockets.add(pocket.parse(str, i));
                        t += (pocket.nextStart -  i);
                        i = pocket.nextStart;
                    }
                    this.nextStart = i;
                }
            }

            return this;
        }
    }

    private String hex2Bin(String hex) {
        Map<Character, String> map = new HashMap<Character, String>(){
            {
                put('0', "0000");
                put('1', "0001");
                put('2', "0010");
                put('3', "0011");
                put('4', "0100");
                put('5', "0101");
                put('6', "0110");
                put('7', "0111");
                put('8', "1000");
                put('9', "1001");
                put('A', "1010");
                put('B', "1011");
                put('C', "1100");
                put('D', "1101");
                put('E', "1110");
                put('F', "1111");
            }
        };

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hex.length(); ++i) {
            sb.append(map.get(hex.charAt(i)));
        }

        return sb.toString();
    }

    private int traverse(Pocket root) {
        int sum = new BigInteger(root.version, 2).intValue();

        for (Pocket subPocket : root.subPockets) {
            sum += traverse(subPocket);
        }
        return sum;
    }

    private long cal(Pocket root) {
        if ("100".equals(root.type)) {
            return new BigInteger(root.content, 2).longValue();
        } else if ("000".equals(root.type)) {
            long sum = 0L;
            for (Pocket subPocket : root.subPockets) {
                sum += cal(subPocket);
            }
            return sum;
        } else if ("001".equals(root.type)) {
            long product = 1L;
            for (Pocket subPocket : root.subPockets) {
                product *= cal(subPocket);
            }
            return product;
        } else if ("010".equals(root.type)) {
            long min = Long.MAX_VALUE;
            for (Pocket subPocket : root.subPockets) {
                long sub = cal(subPocket);
                if (min > sub) {
                    min  = sub;
                }
            }
            return min;
        } else if ("011".equals(root.type)) {
            long max = -1;
            for (Pocket subPocket : root.subPockets) {
                long sub = cal(subPocket);
                if (max < sub) {
                    max  = sub;
                }
            }
            return max;
        } else if ("101".equals(root.type)) {
            return cal(root.subPockets.get(0)) > cal(root.subPockets.get(1)) ? 1L : 0L;
        } else if ("110".equals(root.type)) {
            return cal(root.subPockets.get(0)) < cal(root.subPockets.get(1)) ? 1L : 0L;
        } else {
            return cal(root.subPockets.get(0)) == cal(root.subPockets.get(1)) ? 1L : 0L;
        }
    }
}
