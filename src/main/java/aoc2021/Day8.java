package aoc2021;

import aoc2021.util.HttpUtils;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

/**
 * @author Samson
 */
public class Day8 extends  AoC2021 {
    private final static String INPUT_URL = "https://adventofcode.com/2021/day/8/input";

    public static void main(String[] args) {
        List<String> lines = HttpUtils.getLines(INPUT_URL, COOKIES);
        Day8 day = new Day8();

        System.out.println(day.partOne(lines));
        System.out.println(day.partTwo(lines));
    }

    public int partOne(List<String> lines) {
        int count = 0;
        for (String line : lines) {
            count += this.count(line.split("\\|")[1].trim().split(" "));
        }

        return count;
    }

    private int count(String[] strings) {
        int count = 0;
        for (String string : strings) {
            int len = string.trim().length();
            if (len == 2 || len == 3 || len == 4 || len == 7) {
                count++;
            }
        }
        return count;
    }

    public int partTwo(List<String> lines) {
        int count = 0;
        for (String line : lines) {
            String[] inputLine = line.trim().split("\\|");
            count += this.decode(inputLine[0].trim().split(" "), inputLine[1].trim().split(" "));
        }
        return count;
    }

    private int decode(String[] dict, String[] strings) {
        BinaryOperator<List<String>> mergeFunction = (a, b) -> {
            List<String> result = new ArrayList<>(a);
            result.addAll(b);
            return result;
        };

        Map<Integer, List<String>> collect = Arrays.stream(dict)
                .collect(Collectors.toMap(String::length, Arrays::asList, mergeFunction));

        char[] alphabet = new char[7];
        // 1. A: collect.get(3) collect.get(2) 差异的
        alphabet[0] = this.calA(collect);

        // 2. E: collect.get(5) collect.get(6) 中出现次数为3的
        // 3. G: collect.get(5) collect.get(6) 中出现次数为6的
        // 4. B: collect.get(2) / get(3) / get(4) / get(7) 中出现2次，且 collect.get(5) / get(6) 中出现4次
        // 5. C: collect.get(2) / get(3) / get(4) / get(7) 中出现4次，且 collect.get(5) / get(6) 中出现4次
        // 6. D: collect.get(2) / get(3) / get(4) / get(7) 中出现2次，且 collect.get(5) / get(6) 中出现5次
        // 7. F: collect.get(2) / get(3) / get(4) / get(7) 中出现4次，且 collect.get(5) / get(6) 中出现5次
        this.calOthers(collect, alphabet);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strings.length; ++i) {
            sb.append(parse(alphabet, strings[i]));
        }
        return Integer.parseInt(sb.toString());
    }

    private char calA(Map<Integer, List<String>> collect) {
        char c = 0;
        String len2 = collect.get(2).get(0);
        String len3 = collect.get(3).get(0);

        for (int i = 0; i < len2.length(); ++i) {
            c ^= len2.charAt(i);
        }
        for (int i = 0; i < len3.length(); ++i) {
            c ^= len3.charAt(i);
        }
        return c;
    }
    private void calOthers(Map<Integer, List<String>> collect, char[] alphabet) {

        Map<Character, Integer> counter56 = new HashMap<>(16);
        ArrayList<String> strings = new ArrayList<>(collect.get(5));
        strings.addAll(collect.get(6));
        for (String s : strings) {
            for (int i = 0; i < s.length(); ++i) {
                counter56.computeIfPresent(s.charAt(i), (k, v) -> v + 1);
                counter56.putIfAbsent(s.charAt(i), 1);
            }
        }

        Map<Character, Integer> counter2347 = new HashMap<>(16);
        strings = new ArrayList<>(collect.get(2));
        strings.addAll(collect.get(3));
        strings.addAll(collect.get(4));
        strings.addAll(collect.get(7));
        for (String s : strings) {
            for (int i = 0; i < s.length(); ++i) {
                counter2347.computeIfPresent(s.charAt(i), (k, v) -> v + 1);
                counter2347.putIfAbsent(s.charAt(i), 1);
            }
        }

        // 2. E: collect.get(5) collect.get(6) 中出现次数为3的
        // 3. G: collect.get(5) collect.get(6) 中出现次数为6的

        // 4. B: collect.get(2) / get(3) / get(4) / get(7) 中出现2次，且 collect.get(5) / get(6) 中出现4次
        // 5. C: collect.get(2) / get(3) / get(4) / get(7) 中出现4次，且 collect.get(5) / get(6) 中出现4次
        // 6. D: collect.get(2) / get(3) / get(4) / get(7) 中出现2次，且 collect.get(5) / get(6) 中出现5次
        // 7. F: collect.get(2) / get(3) / get(4) / get(7) 中出现4次，且 collect.get(5) / get(6) 中出现5次
        for (Map.Entry<Character, Integer> entry : counter56.entrySet()) {
            if (entry.getValue() == 3) {
                // E
                alphabet[4] = entry.getKey();
            } else if (entry.getValue() == 6 && entry.getKey() != alphabet[0]) {
                // G
                alphabet[6] = entry.getKey();
            } else if (entry.getValue() == 4) {
                // BC
                for (Map.Entry<Character, Integer> innerEntry : counter2347.entrySet()) {
                    if (innerEntry.getValue() == 2 && entry.getKey().equals(innerEntry.getKey())) {
                        // B
                        alphabet[1] = entry.getKey();
                    } else if (innerEntry.getValue() == 4 && entry.getKey().equals(innerEntry.getKey())) {
                        // C
                        alphabet[2] = entry.getKey();
                    }
                }
            } else if (entry.getValue() == 5) {
                // DF
                for (Map.Entry<Character, Integer> innerEntry : counter2347.entrySet()) {
                    if (innerEntry.getValue() == 2 && entry.getKey().equals(innerEntry.getKey())) {
                        // D
                        alphabet[3] = entry.getKey();
                    } else if (innerEntry.getValue() == 4 && entry.getKey().equals(innerEntry.getKey())) {
                        // F
                        alphabet[5] = entry.getKey();
                    }
                }
            }
        }


    }

    private int parse(char[] alphabet, String str) {
        if (str.length() == 2) {
            return 1;
        } else if (str.length() == 3) {
            return 7;
        } else if (str.length() == 4) {
            return 4;
        } else if (str.length() == 7) {
            return 8;
        } else if (str.length() == 5) {
            // 2 3 5
            if (str.indexOf(alphabet[1]) != -1) {
                return 5;
            } else if (str.indexOf(alphabet[4]) == -1) {
                return 3;
            } else if (str.indexOf(alphabet[5]) == -1) {
                return 2;
            }
        } else if (str.length() == 6) {
            // 0 6 9
            if (str.indexOf(alphabet[3]) == -1) {
                return 0;
            } else if (str.indexOf(alphabet[2]) == -1) {
                return 6;
            } else if (str.indexOf(alphabet[4]) == -1) {
                return 9;
            }
        }
        return 0;
    }
}

