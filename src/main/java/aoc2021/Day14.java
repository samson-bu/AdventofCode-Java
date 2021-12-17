package aoc2021;

import aoc2021.util.HttpUtils;

import java.util.*;

/**
 * @author Samson
 */
public class Day14 extends AoC2021 {
    private final static String INPUT_URL = "https://adventofcode.com/2021/day/14/input";

    public static void main(String[] args) {
        List<String> lines = HttpUtils.getLines(INPUT_URL, COOKIES);
//        List<String> lines = Arrays.asList("NNCB",
//                "",
//                "CH -> B",
//                "HH -> N",
//                "CB -> H",
//                "NH -> C",
//                "HB -> C",
//                "HC -> B",
//                "HN -> C",
//                "NN -> C",
//                "BH -> H",
//                "NC -> B",
//                "NB -> B",
//                "BN -> B",
//                "BB -> N",
//                "BC -> B",
//                "CC -> N",
//                "CN -> C");

        Day14 day = new Day14();
        System.out.println(day.partOne(lines));
        System.out.println(day.partTwo(lines));
    }

    public long partOne(List<String> lines) {
        String str = lines.get(0).trim();
        Map<String, String> rules = this.genRules(lines.subList(2, lines.size()));

        for (int i = 0; i < 10; ++i) {
            str = this.step(str, rules);
        }
        long[] count = new long[26];
        Arrays.fill(count, 0L);

        this.count(str, count);
        long max = Arrays.stream(count).max().getAsLong();
        long min = Arrays.stream(count).filter(e -> e != 0).min().getAsLong();

        return max - min;
    }

    public long partTwo(List<String> lines) {
        String str = lines.get(0).trim();
        Map<String, Long> countMap = this.genCountMap(str);
        Map<String, List<String>> transferRules = this.genTransferRules(lines.subList(2, lines.size()));

        for (int i = 0; i < 40; ++i) {
            countMap = this.step(countMap, transferRules);
        }
        long[] count = this.count(countMap);
        count[str.charAt(0) - 'A']++;
        count[str.charAt(str.length() - 1) - 'A']++;

        long max = Arrays.stream(count).max().getAsLong();
        long min = Arrays.stream(count).filter(e -> e != 0L).min().getAsLong();

        return (max - min) / 2;
    }

    private Map<String, String> genRules(List<String> lines) {
        Map<String, String> rules = new HashMap<>(1024);

        for (String line : lines) {
            String[] s = line.split(" ");
            String key = s[0].trim();
            String value = String.format("%s%c", s[2].trim(), key.charAt(1));
            rules.put(key, value);
        }
        return rules;
    }

    private String step(String str, Map<String, String> rules) {
        StringBuilder sb = new StringBuilder();
        sb.append(str.charAt(0));

        for (int i = 1; i < str.length(); ++i) {
            sb.append(rules.get(str.substring(i - 1, i + 1)));
        }

        return sb.toString();
    }

    private void count(String str, long[] count) {
        for (int i = 0; i < str.length(); ++i) {
            count[str.charAt(i) - 'A']++;
        }
    }

    private Map<String, Long> step(Map<String, Long> count, Map<String, List<String>> rules) {
        Map<String, Long> stepForward = new HashMap<>();
        for (Map.Entry<String, Long> entry : count.entrySet()) {
            for (String s : rules.get(entry.getKey())) {
                stepForward.putIfAbsent(s, 0L);
                stepForward.put(s, stepForward.get(s) + entry.getValue());
            }
        }
        return stepForward;
    }

    private Map<String, List<String>> genTransferRules(List<String> lines) {
        Map<String, List<String>> rules = new HashMap<>(1024);

        for (String line : lines) {
            String[] s = line.split(" ");
            String key = s[0].trim();
            List<String> value = Arrays.asList(String.format("%s%c", s[2].trim(), key.charAt(1)),
                    String.format("%c%s", key.charAt(0), s[2].trim()));
            rules.put(key, new ArrayList<>(value));
        }
        return rules;
    }
    private Map<String, Long> genCountMap(String str) {
        Map<String, Long> countMap = new HashMap<>(1024);

        for (int i = 1; i < str.length(); ++i) {
            String key = str.substring(i - 1, i + 1);
            countMap.putIfAbsent(key, 0L);
            countMap.computeIfPresent(key, (k, v) -> v + 1L);
        }

        return countMap;
    }
    private long[] count(Map<String, Long> countMap) {
        long[] count = new long[26];
        Arrays.fill(count, 0L);

        for (Map.Entry<String, Long> entry : countMap.entrySet()) {
            for (int i = 0; i < entry.getKey().length(); ++i) {
                count[entry.getKey().charAt(i) - 'A'] += entry.getValue();
            }
        }

        return count;
    }

}
