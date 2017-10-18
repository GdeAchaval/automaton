import java.util.Arrays;
import java.util.List;

/**
 * @author Agustin Bettati
 * @version 1.0
 */
public class SimpleTest {

    public static void main(String[] args) {
        List<String> words = Arrays.asList("Hi","Ho");
        WordDetectionAutomaton nonDeterministic = new WordDetectionAutomaton(words);

        WordDetectionAutomaton deterministic = nonDeterministic.createDeterministic();
    }
}
