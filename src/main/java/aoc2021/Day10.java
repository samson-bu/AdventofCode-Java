package aoc2021;

import aoc2021.util.HttpUtils;

import java.util.*;

/**
 * @author Samson
 */
public class Day10 extends AoC2021 {
    private final static String INPUT_URL = "https://adventofcode.com/2021/day/10/input";
    private final static Map<Character, Character> PAIR_MAP = new HashMap<Character, Character>(8) {
        {
            put('(', ')');
            put('[', ']');
            put('{', '}');
            put('<', '>');
        }
    };
    private final static Map<Character, Long> CORRUPTED_SCORE_MAP = new HashMap<Character, Long> (8) {
        {
            put(')', 3L);
            put(']', 57L);
            put('}', 1197L);
            put('>', 25137L);
        }
    };
    private final static Map<Character, Long> CLOSING_SCORE_MAP = new HashMap<Character, Long>(8) {
        {
            put(')', 1L);
            put(']', 2L);
            put('}', 3L);
            put('>', 4L);
        }
    };

    public static void main(String[] args) {
        List<String> lines = HttpUtils.getLines(INPUT_URL, COOKIES);
//        lines = Arrays.asList("[({(<(())[]>[[{[]{<()<>>"
//                ,"[(()[<>])]({[<{<<[]>>("
//                ,"{([(<{}[<>[]}>{[]{[(<()>"
//                ,"(((({<>}<{<{<>}{[]{[]{}"
//                ,"[[<[([]))<([[{}[[()]]]"
//                ,"[{[{({}]{}}([{[{{{}}([]"
//                ,"{<[[]]>}<{[{[{[]{()[[[]"
//                ,"[<(<(<(<{}))><([]([]()"
//                ,"<{([([[(<>()){}]>(<<{{"
//                ,"<{([{{}}[<[[[<>{}]]]>[]]");

        Day10 day = new Day10();
        System.out.println(day.partOne(lines));
        System.out.println(day.partTwo(lines));
    }

    public long partOne(List<String> lines) {
        long score = 0L;
        for (String line : lines) {
            Character c;
            if ((c = findCorrupted(line)) == null) {
                continue;
            }
            score += CORRUPTED_SCORE_MAP.get(c);
        }
        return score;
    }

    public Long partTwo(List<String> lines) {
        List<Long> scores = new ArrayList<>();
        for (String line : lines) {
            Long score = calClosing(line);
            if (score != -1) {
                scores.add(score);
            }
        }
        Collections.sort(scores);
        return scores.get(scores.size() / 2);
    }

    private Character findCorrupted(String str) {
        Stack<Character> stack = new Stack<>();

        for (int i = 0; i < str.length(); ++i) {
            if (PAIR_MAP.containsKey(str.charAt(i))) {
                // left
                stack.push(str.charAt(i));
            } else if (PAIR_MAP.containsValue(str.charAt(i))) {
                // right
                if (stack.isEmpty() || !PAIR_MAP.get(stack.peek()).equals(str.charAt(i))) {
                    return str.charAt(i);
                }
                stack.pop();
            }
        }
        return null;
    }

    private Long calClosing(String str) {
        Stack<Character> stack = new Stack<>();

        for (int i = 0; i < str.length(); ++i) {
            if (PAIR_MAP.containsKey(str.charAt(i))) {
                // left
                stack.push(str.charAt(i));
            } else if (PAIR_MAP.containsValue(str.charAt(i))) {
                // right
                if (stack.isEmpty() || !PAIR_MAP.get(stack.peek()).equals(str.charAt(i))) {
                    return -1L;
                }
                stack.pop();
            }
        }
        if (stack.isEmpty()) {
            return 0L;
        } else {
            long score = 0L;
            while (!stack.isEmpty()) {
                score = score * 5 + CLOSING_SCORE_MAP.get(PAIR_MAP.get(stack.pop()));
            }
            return score;
        }
    }
}
