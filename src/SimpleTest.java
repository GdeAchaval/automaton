import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Agustin Bettati
 * @version 1.0
 */
public class SimpleTest {

    public static void main(String[] args) {
        List<String> words = Arrays.asList("ganar", "tincho");
        WordDetectionAutomaton nonDeterministic = new WordDetectionAutomaton(words);

        WordDetectionAutomaton deterministic = nonDeterministic.createDeterministic();

        Map<String, Integer> frequencies = deterministic.getFrequencies("ganar 22 ganar tincho for life");

    }
}
