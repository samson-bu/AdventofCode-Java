package aoc2021;

import aoc2021.util.HttpUtils;
import javafx.util.Pair;
import org.jsoup.internal.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Samson
 */
public class Day4 extends AoC2021 {
    private final static String INPUT_URL = "https://adventofcode.com/2021/day/4/input";
    public static void main(String[] args) {
        List<String> lines = HttpUtils.getLines(INPUT_URL, COOKIES);

        System.out.println(partOne(lines));
        System.out.println(partTwo(lines));

    }

    private static int partOne(List<String> lines) {
        List<String> inputLines = new ArrayList(lines);
        if (!"".equalsIgnoreCase(inputLines.get(inputLines.size() - 1).trim())) {
            inputLines.add("");
        }

        String[] numbers = inputLines.get(0).split(",");

        int index = Integer.MAX_VALUE;
        int score = 0;
        List<String[]> board = new ArrayList();
        for (int i = 2; i < inputLines.size(); ++i) {
            if ("".equalsIgnoreCase(inputLines.get(i).trim())) {
                String[][] gameBoard = new String[board.size()][board.size()];
                board.toArray(gameBoard);
                Pair<Integer, Integer> game = game(gameBoard, numbers);
                if (index > game.getKey()) {
                    index = game.getKey();
                    score = game.getValue();
                }
                board = new ArrayList<>();
            } else {
                board.add(filterEmptyElement(inputLines.get(i).split("( )+")));
            }
        }
        return score * Integer.parseInt(numbers[index]);
    }

    private static int partTwo(List<String> lines) {
        List<String> inputLines = new ArrayList<>(lines);

        if (!"".equalsIgnoreCase(inputLines.get(inputLines.size() - 1).trim())) {
            inputLines.add("");
        }

        String[] numbers = inputLines.get(0).split(",");

        int index = -1;
        int score = 0;
        List<String[]> board = new ArrayList();
        for (int i = 2; i < inputLines.size(); ++i) {
            if ("".equalsIgnoreCase(inputLines.get(i).trim())) {
                String[][] gameBoard = new String[board.size()][board.size()];
                board.toArray(gameBoard);
                Pair<Integer, Integer> game = game(gameBoard, numbers);
                if (Integer.MAX_VALUE != game.getKey() && index < game.getKey()) {
                    index = game.getKey();
                    score = game.getValue();
                }
                board = new ArrayList<>();
            } else {
                board.add(filterEmptyElement(inputLines.get(i).split("( )+")));
            }
        }
        return score * Integer.parseInt(numbers[index]);
    }

    /**
     * one game
     * @param board board
     * @param numbers input
     * @return
     */
    private static Pair<Integer, Integer> game(String[][] board, String[] numbers) {
        boolean[][] mark = new boolean[board.length][board.length];
        for (int i = 0; i < numbers.length; ++i) {
            for (int j = 0; j < mark.length; j++) {
                for (int k = 0; k < mark.length; k++) {
                    if (!mark[j][k] && board[j][k].equalsIgnoreCase(numbers[i])) {
                        mark[j][k] = true;
                    }
                }
            }
            if (bingo(mark)) {
                return new Pair<>(i, score(board, mark));
            }
        }
        return new Pair<>(Integer.MAX_VALUE, 0);
    }

    /**
     * whether win
     * @param mark
     * @return
     */
    private static boolean bingo(boolean[][] mark) {
        // 任意行
        for (int i = 0; i < mark.length; ++i) {
            boolean row = true, column = true;
            for (int j = 0; j < mark.length; ++j) {
                row = row && mark[i][j];
                column = column && mark[j][i];
            }
            if (row || column) {
                return true;
            }
        }
        return false;
    }

    private static int score(String[][] board, boolean[][] mark) {
        int score = 0;
        for (int i = 0; i < board.length; ++i) {
            for (int j = 0; j < board.length; ++j) {
                if (!mark[i][j]) {
                    score += Integer.parseInt(board[i][j]);
                }
            }
        }
        return score;
    }

    private static String[] filterEmptyElement(String[] line) {

        return Arrays.stream(line).filter(e -> !StringUtil.isBlank(e)).toArray(String[]::new);
    }
}
