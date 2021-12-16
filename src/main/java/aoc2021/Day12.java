package aoc2021;

import aoc2021.util.HttpUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Samson
 */
public class Day12 extends AoC2021 {
    private final static String INPUT_URL = "https://adventofcode.com/2021/day/12/input";

    public static void main(String[] args) {
        List<String> lines = HttpUtils.getLines(INPUT_URL, COOKIES);
//        lines = Arrays.asList("dc-end",
//                "HN-start",
//                "start-kj",
//                "dc-start",
//                "dc-HN",
//                "LN-dc",
//                "HN-end",
//                "kj-sa",
//                "kj-HN",
//                "kj-dc");

        Day12 day = new Day12();
        System.out.println(day.partOne(lines));
        System.out.println(day.partTwo(lines));
    }

    public int partOne(List<String> lines) {
        Map<String, List<String>> map = this.genMap(lines);

        Map<String, Boolean> visitedMap = new HashMap<>();
        Queue<String> workingQueue = new LinkedList<>();
        workingQueue.add("start");
        visitedMap.put("start", true);

        int count = this.countPath(map, visitedMap, "start");
        return count;
    }
    private int countPath(Map<String, List<String>> map, Map<String, Boolean> visitedMap, String start) {
        int count = 0;
        for (String s : map.get(start)) {
            if (visitedMap.getOrDefault(s, false)){
                continue;
            }
            if ("end".equals(s)) {
                count++;
                continue;
            }
            if (isSmall(s) && (!visitedMap.getOrDefault(s, false))) {
                visitedMap.put(s, true);
                count += this.countPath(map, visitedMap, s);
                visitedMap.put(s, false);
            } else if (isLarge(s)) {
                count += this.countPath(map, visitedMap, s);
            }
        }
        return count;
    }

    public int partTwo(List<String> lines) {
        Map<String, List<String>> map = this.genMap(lines);

        Map<String, Boolean> visitedMap = new HashMap<>();
        Queue<String> workingQueue = new LinkedList<>();
        workingQueue.add("start");
        visitedMap.put("start", true);

        Deque<String> path = new LinkedList<>();
        int count = this.countPath(map, visitedMap, "start", path);
        return count;
    }
    private int countPath(Map<String, List<String>> map, Map<String, Boolean> visitedMap, String start, Deque<String> path) {
        int count = 0;
        for (String s : map.get(start)) {
            if ("start".equals(s)) {
                continue;
            }
            if (visitedMap.getOrDefault(s, false)) {
                // if has visited
                if (isSmall(s) && !alreadyVisitedSingleSmallCaveTwice(path)) {
                    path.addLast(s);
                    count += this.countPath(map, visitedMap, s, path);
                    path.removeLast();
                }

            } else {
                // first visited
                if ("end".equals(s)) {
                    ++count;
                } else if (isSmall(s)) {
                    visitedMap.put(s, true);
                    path.addLast(s);
                    count += this.countPath(map, visitedMap, s, path);
                    path.removeLast();
                    visitedMap.put(s, false);
                } else if (isLarge(s)) {
                    path.addLast(s);
                    count += this.countPath(map, visitedMap, s, path);
                    path.removeLast();
                }
            }
        }
        return count;
    }

    private boolean alreadyVisitedSingleSmallCaveTwice(Deque<String> path) {
        List<String> collect = path.stream().filter(this::isSmall).collect(Collectors.toList());
        List<String> collect1 = path.stream().filter(this::isSmall).distinct().collect(Collectors.toList());

        return collect.size() != collect1.size();
    }

    private Map<String, List<String>> genMap(List<String> lines) {
        Map<String, List<String>> map = new HashMap<>();
        for (String line : lines) {
            String[] split = line.trim().split("-");
            String from = split[0].trim();
            String to = split[1].trim();

            map.putIfAbsent(from, new ArrayList<>());
            map.putIfAbsent(to, new ArrayList<>());

            map.get(from).add(to);
            map.get(to).add(from);

        }
        return map;
    }

    private boolean isSmall(String cave) {
        return cave.equals(cave.toLowerCase());
    }

    private boolean isLarge(String cave) {
        return cave.equals(cave.toUpperCase());
    }
}
