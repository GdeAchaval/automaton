package dictionary;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Gonzalo de Achaval
 * @author Agustin Bettati
 * @author Marcos Khabie
 * Used for searching in dictionary
 */
class RWayTrieMap {

    private static final int A_ASCII = 97;
    private ArrayList<String> keys = new ArrayList<>();

    private Node head;

    private class Node {
        List<Node> next;
        Node() {
            next = generateList();
        }
    }

    boolean containsKey(@NotNull String key) {
        return find(head, key, 0) != null;
    }

    void put(@NotNull String key) {
        head = put(head, key, 0);
        addKey(key);
    }

    private void addKey(String key) {
        if (!keys.contains(key)) {
            keys.add(key);
        }
    }

    private Node put(Node node, String key, int level) {
        if (node == null) {
            Node result = new Node();
            if (level < key.length()) {
                int next = (int) key.charAt(level) - A_ASCII;
                result.next.set(next, put(result.next.get(next), key, level + 1));
            }
            return result;
        } else if (level == key.length()) {
            return node;
        } else {
            int next = (int) key.charAt(level) - A_ASCII;
            node.next.set(next, put(node.next.get(next), key, level + 1));
            return node;
        }
    }

    private Node find(Node node, String key, int level) {
        if (node == null) return null;
        if (level == key.length()) return node;
        int next = (int) key.charAt(level) - A_ASCII;
        return find(node.next.get(next), key, level + 1);
    }

    private ArrayList<Node> generateList() {
        return new ArrayList<>(Collections.nCopies(26, null));
    }
}

