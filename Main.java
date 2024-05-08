import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    static class Trie {
        Trie[] children = new Trie[26];
        boolean isEnd = false;
    }

    private static Trie root = new Trie();

    private static void insert(String word) {
        Trie curr = root;
        int index = 0;
        for (char ch : word.toCharArray()) {
            index = ch - 'a';
            if (curr.children[index] == null) {
                curr.children[index] = new Trie();
            }
            curr = curr.children[index];
        }
        curr.isEnd = true;
    }

    private static boolean search(String word) {
        Trie curr = root;
        int index = 0;
        for (char ch : word.toCharArray()) {
            index = ch - 'a';
            if (curr.children[index] == null) {
                return false;
            }
            curr = curr.children[index];
        }
        return curr.isEnd;
    }

    private static ArrayList<String> getALlPefixes(String word) {
        Trie curr = root;
        ArrayList<String> prefixes = new ArrayList<>();
        StringBuilder prefix = new StringBuilder();
        int index = 0;

        for (char ch : word.toCharArray()) {
            index = ch - 'a';
            if (curr.children[index] == null) {
                return prefixes;
            }
            curr = curr.children[index];
            prefix.append(ch);
            if (curr.isEnd) {
                prefixes.add(prefix.toString());
            }
        }
        return prefixes;
    }

    public static ArrayList<String> readFile(String filePath) {
        ArrayList<String> list = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String string;
            while ((string = reader.readLine()) != null) {
                list.add(string);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

// Main Logic:
    public static void longestCompoundWord(String filepath) {
        long startTime = System.currentTimeMillis();
        ArrayList<String> list = readFile(filepath);
        Trie trie = new Trie();
        Deque<Pair<String, String>> queue = new ArrayDeque<>();

        for (String words : list) {
            ArrayList<String> prefixes = getALlPefixes(words);
            for (String prefix : prefixes) {
                queue.offer(new Pair<>(words, words.substring(prefix.length())));
            }
            insert(words);
        }

        String longest = new String();
        String second = new String();
        int maxlen = 0;

        while (!queue.isEmpty()) {
            String word = queue.peek().getKey();
            String suffix = queue.poll().getValue();

            if (search(suffix) && word.length() > maxlen) {
                second = longest;
                longest = word;
                maxlen = word.length();
            } else {
                ArrayList<String> prefixes = getALlPefixes(suffix);
                for (String prefix : prefixes) {
                    queue.offer(new Pair<>(word, suffix.substring(prefix.length())));
                }
            }
        }
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime);
        System.out.println("Longest Compound Word" + longest);
        System.out.println("Second Longest Compound Word" + second);
        System.out.println("Time taken to process file" + filepath + duration + " milli seconds");
    }
}
class Pair<K, V> {
    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}