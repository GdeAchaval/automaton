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
    private String wordOfEndingState;

    
    State(String name) {
        this.name = name;
        this.transitions = new HashMap<>();
        wordOfEndingState = null;
    }

    State(String name, String wordOfEndingState) {
        this.name = name;
        this.wordOfEndingState = wordOfEndingState;
        this.transitions = new HashMap<>();
    }

    boolean isEndingState(){
        return wordOfEndingState != null;
    }

    void convertToEndingState(String endingWord){
        wordOfEndingState = endingWord;
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
    
    String getEndingWord(){
        return wordOfEndingState;
    }

    public String toString(){
        if(wordOfEndingState != null){
            return "-"+name+"- End state " + wordOfEndingState;
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
