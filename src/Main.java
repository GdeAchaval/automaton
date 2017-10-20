import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Agustin Bettati
 * @version 1.0
 */
public class Main {
    public static void main(String[] args) throws IOException {
        List<String> phrases = Arrays.asList("hola mundo", "mundo", "hola");
        //TODO falta que busque las phrases en un search.txt
        WordDetectionAutomaton nonDet = new WordDetectionAutomaton(phrases);
        WordDetectionAutomaton deterministic = nonDet.createDeterministic();

        Util.generateDot("noDeterminante.dot", nonDet.getInitialState());
        Util.generateDot("determinante.dot",deterministic.getInitialState());
        Util.createPNGfromDOT("noDeterminante.dot");
        Util.createPNGfromDOT("determinante.dot");


        FileManager fm = new FileManager();
        File[] files = fm.getHtmlFilesInDirectory();

        String contentOfFile = "";
        for (String phrase : phrases) {
            contentOfFile += "Sentence: " + phrase + "\n\n";
            for (File file : files) {
                String content = fm.getContentOfFile(file);
                final Map<String, Integer> frequencies = deterministic.getFrequencies(content);

                if(frequencies.get(phrase) > 0){
                    contentOfFile += "File name: " + file.getName() + "\n";
                    contentOfFile += "Frequency: " + frequencies.get(phrase) + "\n";
                }
            }
            contentOfFile += "\n\n";
        }

        fm.writeToTextFile("index.txt", contentOfFile);
    }
}
