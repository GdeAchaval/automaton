import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Agustin Bettati
 * @version 1.0
 */
public class State {

    private Map<Character, List<State>> transitions;
    private final String wordOfEndingState;


    public State() {
        this.transitions = new HashMap<>();
        wordOfEndingState = null;
    }

    public State(String wordOfEndingState) {
        this.wordOfEndingState = wordOfEndingState;
    }

    public boolean isEndingState(){
        return wordOfEndingState != null;
    }

    public void addNewTransition(char character, State nextState){
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
        if(transitions.containsKey(character)){
            return true;
        }
        else{
            return false;
        }
    }

    public List<State> getTransitionStates(char character){
        return transitions.get(character);
    }

    public String toString(){
        if(wordOfEndingState != null){
            return "End state " + wordOfEndingState;
        }
        else{
            String result = "";
            for (Character character : transitions.keySet()) {
                result += character + " ";
            }
            return "Goes to " + result;
        }
    }
}
