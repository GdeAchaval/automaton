import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Agustin Bettati
 * @version 1.0
 */
public class Main {

    public static void main(String[] args) throws IOException {
        List<String> phrases = readSearch(args);
        WordDetectionAutomaton nonDet = new WordDetectionAutomaton(phrases);
        WordDetectionAutomaton deterministic = nonDet.createDeterministic();

        Util.generateDot("noDeterminante.dot", nonDet.getInitialState());
        Util.generateDot("determinante.dot",deterministic.getInitialState());
        Util.createPNGfromDOT("noDeterminante.dot");
        Util.createPNGfromDOT("determinante.dot");


        FileManager fm = new FileManager();
        File[] files = fm.getHtmlFilesInDirectory(args[0]);

        final Map<String, List<Couple>> frequencies = new HashMap<>();

        for (File file : files) {

            final String fileName = file.getName();
            String content = fm.getContentOfFile(file);
            final Map<String, Integer> freq = deterministic.getFrequencies(content);
            List<Couple> couples = new ArrayList<>();

            for(Map.Entry<String, Integer> element : freq.entrySet()){
                final Couple e = new Couple(element.getKey(), element.getValue());
                couples.add(e);
            }
            frequencies.put(fileName, couples);
        }

        FileWriter fw = new FileWriter("index.txt");
        for (String phrase : phrases) {

            fw.write("Phrase: "+phrase+ "\n");

            for(Map.Entry<String, List<Couple>> element : frequencies.entrySet()){
                String nameOfFile = element.getKey();
                final List<Couple> listOfCouples = element.getValue();
                for (Couple couple : listOfCouples) {
                    if(couple.getPhrase().equals(phrase)){
                        if(couple.getFrequency() != 0) {
                            fw.write("\t" + nameOfFile + " -> " + couple.getFrequency() + "\n");
                        }
                    }
                }
            }
            fw.write("\n");
        }
        fw.close();
    }

    private static List<String> readSearch(String[] args) throws IOException {
        List<String> phrasesToRead = new ArrayList<>();
        String directory;
        String name;
        String fullPath;
        try {
            directory = System.getProperty("user.dir");
            name = args[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException("You must run the program with two parameters: directory & filename");
        }
        fullPath = directory + "/" + name;
        File file = new File(fullPath);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String phrase;
        while((phrase = br.readLine()) != null) {
            phrasesToRead.add(phrase);
        }
        return phrasesToRead;
    }
}

class Couple{
    private String phrase;
    private int frequency;

    Couple(String phrase, int frequency) {
        this.phrase = phrase;
        this.frequency = frequency;
    }

    String getPhrase() {
        return phrase;
    }

    int getFrequency() {
        return frequency;
    }
}


