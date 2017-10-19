package dictionary;

import java.io.*;

/**
 * @author Gonzalo de Achaval
 */
public class DictionaryUtil {

    private RWayTrieMap trieMap;

    private DictionaryUtil() throws IOException {
        this.trieMap = buildTrie();
    }

    private RWayTrieMap buildTrie() throws IOException {
        RWayTrieMap trieMap = new RWayTrieMap();
        File file = new File("src/dictionary/dictionary.txt"); //10000 most common English words
        FileReader fr = new FileReader(file);
        BufferedReader bf = new BufferedReader(fr);
        String word;

        while((word = bf.readLine()) != null){
            trieMap.put(word);
        }
        return trieMap;
    }

    private boolean checkIfWordExists(String word) {
        return trieMap.containsKey(word);
    }
}
