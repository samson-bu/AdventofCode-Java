package aoc2021;

import aoc2021.util.HttpUtils;
import javafx.util.Pair;

import java.util.*;

/**
 * @author Samson
 */
public class Day18 extends AoC2021 {
    private static final String INPUT_URL = "https://adventofcode.com/2021/day/18/input";

    public static void main(String[] args) {
        List<String> lines = HttpUtils.getLines(INPUT_URL, COOKIES);
//        lines = Arrays.asList("[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]",
//                "[[[5,[2,8]],4],[5,[[9,9],0]]]",
//                "[6,[[[6,2],[5,6]],[[7,6],[4,7]]]]",
//                "[[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]",
//                "[[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]",
//                "[[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]",
//                "[[[[5,4],[7,7]],8],[[8,3],8]]",
//                "[[9,3],[[9,9],[6,[4,9]]]]",
//                "[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]",
//                "[[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]");

        Day18 day = new Day18();
        System.out.println(day.partOne(lines));
        System.out.println(day.partTwo(lines));
    }

    public int partOne(List<String> lines) {

        Node sum = this.treefy(lines.get(0).trim());
        for (int i = 1; i < lines.size(); ++i) {
            sum = sum.add(this.treefy(lines.get(i).trim()));
            sum.reduce();
        }
        return sum.magnitude();
    }

    public int partTwo(List<String> lines) {
        int max = 0;
        for (int i = 1; i < lines.size(); ++i) {
            for (int j = 0; j < i; ++j) {

                Node sum1 = this.treefy((lines.get(i).trim())).add(this.treefy(lines.get(j).trim()));
                Node sum2 = this.treefy((lines.get(j).trim())).add(this.treefy(lines.get(i).trim()));

                sum1.reduce();
                sum2.reduce();

                max = Math.max(max, Math.max(sum1.magnitude(), sum2.magnitude()));
            }
        }

        return max;
    }


    private static class Node {
        Integer value;
        Node left, right;
        Node brotherLeft, brotherRight;
        Integer depth;

        public Node(Integer value, Node left, Node right) {
            this.value = value;
            this.left = left;
            this.right = right;

            if (left != null && right != null) {
                Node lr = left.rightMost();
                Node rl = right.leftMost();

                lr.brotherRight = rl;
                rl.brotherLeft = lr;
            }
        }

        public Node add(Node n) {
            return new Node(-1, this, n);
        }

        public Node leftMost() {
            if (left == null || right == null) {
                return this;
            }

            return left.leftMost();
        }

        public Node rightMost() {
            if (left == null || right == null) {
                return this;
            }
            return right.rightMost();
        }

        public void reduce() {
            Deque<Node> explodeQueue = new LinkedList<>();

            Queue<Pair<Node, Integer>> workingQueue = new LinkedList<>();
            workingQueue.add(new Pair<>(this, 1));
            while (!workingQueue.isEmpty()) {
                Pair<Node, Integer> poll = workingQueue.poll();
                poll.getKey().depth = poll.getValue();

                if (null != poll.getKey().left) {
                    workingQueue.add(new Pair<>(poll.getKey().left, poll.getValue() + 1));
                }
                if (null != poll.getKey().right) {
                    workingQueue.add(new Pair<>(poll.getKey().right, poll.getValue() + 1));
                }
                if (poll.getValue() > 4 && poll.getKey().value == -1) {
                    explodeQueue.addLast(poll.getKey());
                }
            }

            while (!explodeQueue.isEmpty()) {
                this.explode(explodeQueue.removeFirst());

                while (explodeQueue.isEmpty()) {
                    Node leftMost = this.leftMost();
                    while (leftMost.value <= 9 && leftMost.brotherRight != null) {
                        leftMost = leftMost.brotherRight;
                    }
                    if (leftMost.value > 9) {
                        this.split(leftMost, explodeQueue);
                    } else {
                        break;
                    }
                }
            }
        }

        private void explode(Node node) {
            node.value = 0;

            Node br = node.right.brotherRight;
            node.brotherRight = br;
            if (null != br) {
                br.value += node.right.value;
                br.brotherLeft = node;
            }

            Node bl = node.left.brotherLeft;
            node.brotherLeft = bl;
            if (null != bl) {
                bl.value += node.left.value;
                bl.brotherRight = node;
            }

            node.left = null;
            node.right = null;
        }

        private void split(Node node, Deque<Node> queue) {
            if (node.value <= 9) {
                return ;
            }
            int lv = node.value / 2, rv = node.value - lv;
            Node left = new Node(lv, null, null);
            left.depth = node.depth + 1;
            Node right = new Node(rv, null, null);
            right.depth = node.depth + 1;

            left.brotherLeft = node.brotherLeft;
            if (null != left.brotherLeft) {
                left.brotherLeft.brotherRight = left;
            }
            left.brotherRight = right;

            right.brotherLeft = left;
            right.brotherRight = node.brotherRight;
            if (null != right.brotherRight) {
                right.brotherRight.brotherLeft = right;
            }

            node.value = -1;
            node.left = left;
            node.right = right;
            node.brotherLeft = null;
            node.brotherRight = null;

            if (node.depth > 4) {
                queue.addFirst(node);
            }
        }

        @Override
        public String toString() {
            if (this.left == null || this.right == null) {
                return Integer.toString(this.value);
            }

            return String.format("[%s,%s]", this.left, this.right);
        }

        public Integer magnitude() {
            if (this.value != -1) {
                return this.value;
            }
            return 3 * this.left.magnitude() + 2 * this.right.magnitude();
        }
    }
    private Node treefy(String str) {
        Stack<Integer> lsp = new Stack<>();
        Stack<Integer> comma = new Stack<>();
        Stack<Node> pair = new Stack<>();
        for (int i = 0; i < str.length(); ++i) {
            // [ 要创建 left
            // , left创建完，要创建right
            // ] right创建完

            if ('[' == str.charAt(i)) {
                lsp.push(i);
            } else if (',' == str.charAt(i)) {
                comma.push(i);
                if (']' ==  str.charAt(i - 1)) {
                    // 前一个字符是 ]
                } else {
                    // 前一个字符是 regular number
                    int v = Integer.parseInt(str.substring(lsp.peek() + 1, comma.peek()));
                    Node n = new Node(v, null, null);
                    pair.push(n);
                }
            } else if (']' == str.charAt(i)) {
                if (']' == str.charAt(i - 1)) {
                    // 前一个字符是]

                    Node r = pair.pop();
                    Node l = pair.pop();
                    Node p = new Node(-1, l, r);
                    pair.push(p);

                    comma.pop();
                    lsp.pop();

                } else {
                    // 前一个字符是 regular number
                    int v = Integer.parseInt(str.substring(comma.peek() + 1, i));
                    Node r = new Node(v, null, null);

                    Node p = new Node(-1, pair.pop(), r);
                    pair.push(p);

                    comma.pop();
                    lsp.pop();
                }
            }
        }
        return pair.pop();
    }
}
