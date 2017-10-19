import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Agustin Bettati
 * @version 1.0
 */
public class SimpleTest {

    public static void main(String[] args) {

        List<String> sentences = Arrays.asList("ho la", "ho");
        WordDetectionAutomaton nonDet = new WordDetectionAutomaton(sentences);
        WordDetectionAutomaton det = nonDet.createDeterministic();
        final Map<String, Integer> frequencies = det.getFrequencies("ho ho la la");

    }
}
