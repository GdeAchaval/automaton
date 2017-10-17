import java.util.HashMap;
import java.util.List;

/**
 * @author Agustin Bettati
 * @version 1.0
 */
public class WordDetectionAutomaton {
    private State initialState;
    private State currentState;
    private HashMap<String, Integer> frequency;

    public WordDetectionAutomaton(List<String> phrases){
        initialState = new State();
        currentState = initialState;
        frequency = new HashMap<>();
        generateAutomaton(phrases);
    }

    private void generateAutomaton(List<String> phrases) {
        phrases.forEach( phrase -> {
            char[] characters = phrase.toCharArray();

            State aux = initialState;
            for (int i = 0; i <= characters.length -2; i++) {
                State newState = new State();
                aux.addNewTransition(characters[i], newState);
                aux = newState;
            }
            State finalState = new State(phrase);
            aux.addNewTransition(characters[characters.length - 1], finalState);
        });
    }


}
