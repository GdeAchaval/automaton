import dictionary.DictionaryUtil;

import java.io.*;
import java.util.*;

/**
 * @author Agustin Bettati
 * @version 1.0
 */
public class Main {

    public static void main(String[] args) throws IOException {
        List<String> phrases = readSearch(args);
        //List<String> phrases = Arrays.asList("hola", "hola mundo","mundo" );
        WordDetectionAutomaton nonDet = new WordDetectionAutomaton(phrases);
        WordDetectionAutomaton deterministic = nonDet.createDeterministic();

        Util.generateDot("noDeterminante.dot", nonDet.getInitialState());
        Util.generateDot("determinante.dot",deterministic.getInitialState());
        Util.createPNGfromDOT("noDeterminante.dot");
        Util.createPNGfromDOT("determinante.dot");


        FileManager fm = new FileManager();
        File[] files = fm.getHtmlFilesInDirectory();

        StringBuilder contentOfFile = new StringBuilder();
        for (String phrase : phrases) {
            contentOfFile.append("Sentence: ").append(phrase).append("\n\n");
            for (File file : files) {
                String content = fm.getContentOfFile(file);
                final Map<String, Integer> frequencies = deterministic.getFrequencies(content);

                if(frequencies.get(phrase) > 0){
                    contentOfFile.append("File name: ").append(file.getName()).append("\n");
                    contentOfFile.append("Frequency: ").append(frequencies.get(phrase)).append("\n");
                }
            }
            contentOfFile.append("\n\n");
        }

        fm.writeToTextFile("index.txt", contentOfFile.toString());
    }

    private static List<String> readSearch(String[] args) throws IOException {
        DictionaryUtil dictionaryUtil = new DictionaryUtil();
        List<String> wordsToRead = new ArrayList<>();
        String directory;
        String name;
        String fullPath;
        try {
            directory = args[0];
            name = args[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException("You must run the program with two parameters: directory & filename");
        }
        fullPath = directory + "/" + name;
        File file = new File(fullPath);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String phrase;
        String[] words;
        while((phrase = br.readLine()) != null) {
            phrase = phrase.toLowerCase();
            words = phrase.split("[^a-zA-Z]");
            for (String word : words) {
                if(!wordsToRead.contains(word) && !word.equals("")) {
                    if(dictionaryUtil.checkIfWordExists(word)) {
                        wordsToRead.add(word);
                    }
                }
            }
        }
        return wordsToRead;
    }
}
