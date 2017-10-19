import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Gonzalo De Achaval
 * @author Marcos Khabie
 * @author Agustin Bettati
 * @version 1.0
 */
public class State {

    private String name;
    private Map<Character, List<State>> transitions;
    private List<String> wordsOfEndingState;

    
    State(String name) {
        this.name = name;
        this.transitions = new HashMap<>();
        wordsOfEndingState = new ArrayList<>();
    }

    State(String name, String wordOfEndingState) {
        this.name = name;
        wordsOfEndingState = new ArrayList<>();
        wordsOfEndingState.add(wordOfEndingState);
        this.transitions = new HashMap<>();
    }

    boolean isEndingState(){
        return !wordsOfEndingState.isEmpty();
    }


    public void addEndingWords(List<String> endingWords) {
        for (String endingWord : endingWords) {
            wordsOfEndingState.add(endingWord);
        }
    }
    void addNewTransition(char character, State nextState){
        if(transitions.containsKey(character)){
            transitions.get(character).add(nextState);
        }
        else{
            List<State> states = new ArrayList<>();
            states.add(nextState);
            transitions.put(character, states);
        }
    }

    public boolean hasTransition(char character){
        return transitions.containsKey(character);
    }

    public List<State> getTransitionStates(char character){
        return transitions.get(character);
    }

    Map<Character,List<State>> getTransitions() {
        return transitions;
    }

    String getName() {
        return name;
    }
    
    List<String> getEndingWords(){
        return wordsOfEndingState;
    }

    public String toString(){
        String endingState = "";
        for (String word : wordsOfEndingState) {
            endingState += word + " ";
        }
        if(!wordsOfEndingState.isEmpty()){
            return "-"+name+"- End state " + endingState;
        }
        else{
            String result = "";
            for (Character character : transitions.keySet()) {
                result += character + " ";
            }
            return "-"+name+"- Moves with " + result;
        }
    }
}
