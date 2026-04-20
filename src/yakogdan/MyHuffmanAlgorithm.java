package yakogdan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

public class MyHuffmanAlgorithm {

    private static class CodeTreeNode implements Comparable<CodeTreeNode> {

        Character content;

        int weight;
        CodeTreeNode left;
        CodeTreeNode right;

        public CodeTreeNode(Character content, int weight) {
            this.content = content;
            this.weight = weight;
        }

        public CodeTreeNode(Character content, int weight, CodeTreeNode left, CodeTreeNode right) {
            this.content = content;
            this.weight = weight;
            this.left = left;
            this.right = right;
        }

        @Override
        public int compareTo(CodeTreeNode o) {
            return o.weight - weight;
        }

        public String getCodeForCharacter(Character ch, String parentPath) {
            if (content == ch) {
                return parentPath;
            } else {
                if (left != null) {
                    String path = left.getCodeForCharacter(ch, parentPath + 0);
                    if (path != null) {
                        return path;
                    }
                }

                if (right != null) {
                    String path = right.getCodeForCharacter(ch, parentPath + 1);
                    if (path != null) {
                        return path;
                    }
                }
            }

            return null;
        }

    }

    private static TreeMap<Character, Integer> countFrequency(String text) {
        TreeMap<Character, Integer> freqMap = new TreeMap<>();

        for (int i = 0; i < text.length(); i++) {
            Character c = text.charAt(i);
            Integer count = freqMap.get(c);
            freqMap.put(c, count != null ? count + 1 : 1);
        }

        return freqMap;
    }

    private static CodeTreeNode huffman(ArrayList<CodeTreeNode> codeTreeNodes) {
        while (codeTreeNodes.size() > 1) {
            Collections.sort(codeTreeNodes);
            CodeTreeNode left = codeTreeNodes.removeLast();
            CodeTreeNode right = codeTreeNodes.removeLast();

            CodeTreeNode parent = new CodeTreeNode(null, left.weight + right.weight, left, right);
            codeTreeNodes.add(parent);
        }

        return codeTreeNodes.getFirst();
    }

    private static String huffmanDecode(String encoded, CodeTreeNode tree) {
        StringBuilder decoded = new StringBuilder();

        CodeTreeNode node = tree;
        for (int i = 0; i < encoded.length(); i++) {
            node = encoded.charAt(i) == '0' ? node.left : node.right;

            if (node.content != null) {
                decoded.append(node.content);
                node = tree;
            }
        }

        return decoded.toString();
    }

    static void main() {
        String text = "where there's a will there's a way";

        TreeMap<Character, Integer> frequencies = countFrequency(text);

        System.out.println(frequencies);

        ArrayList<CodeTreeNode> codeTreeNodes = new ArrayList<>();
        for (Character c : frequencies.keySet()) {
            codeTreeNodes.add(new CodeTreeNode(c, frequencies.get(c)));
        }

        CodeTreeNode tree = huffman(codeTreeNodes);

        TreeMap<Character, String> codes = new TreeMap<>();
        for (Character c : frequencies.keySet()) {
            codes.put(c, tree.getCodeForCharacter(c, ""));
        }

        System.out.println("Таблица префиксных кодов: " + codes);

        StringBuilder encoded = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            encoded.append(codes.get(text.charAt(i)));
        }

        System.out.println("Размер исходной строки: " + text.getBytes().length * 8 + " бит");
        System.out.println("Размер сжатой строки: " + encoded.length() + " бит");
        System.out.println("Биты сжатой строки: " + encoded);

        String decoded = huffmanDecode(encoded.toString(), tree);
        System.out.println("Расшифрованно: " + decoded);
    }
}