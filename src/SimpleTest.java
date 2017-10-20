import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Agustin Bettati
 * @version 1.0
 */
public class SimpleTest {

    public static void main(String[] args) throws IOException {

        List<String> sentences = Arrays.asList("hola", "hola mundo", "mundo");
        WordDetectionAutomaton nonDet = new WordDetectionAutomaton(sentences);
        WordDetectionAutomaton det = nonDet.createDeterministic();
        final Map<String, Integer> frequencies = det.getFrequencies("hola hola mundo mundo");

        Util.generateDot("src/files/testNonDet.dot", nonDet.getInitialState());
        Util.generateDot("src/files/testDet.dot",det.getInitialState());
        Util.createPNGfromDOT("src/files/testNonDet.dot");
        Util.createPNGfromDOT("src/files/testDet.dot");

    }
}
