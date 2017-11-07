import java.util.*;

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
    private State defaultTransition;

    
    protected State(String name) {
        this.name = name;
        this.transitions = new HashMap<>();
        wordsOfEndingState = new ArrayList<>();
    }

    private State(String name, String wordOfEndingState) {
        this.name = name;
        wordsOfEndingState = new ArrayList<>();
        wordsOfEndingState.add(wordOfEndingState);
        this.transitions = new HashMap<>();
    }

    static State createInitState(){
        State result = new State("_0");
        result.defaultTransition = result;
        return result;
    }

    static State createHtmlTagState(State initState){
        State result = new TagState(initState);
        result.defaultTransition = result;
        result.addNewTransition('>', initState);
        return result;
    }


    static State createNormalState(String name, State initState, State tagState){
        State result = new State(name);
        result.defaultTransition = initState;
        result.addNewTransition('<', tagState);
        return result;
    }

    static State createEndingState(String name, String endingWord, State initState, State tagState){
        State result = new State(name, endingWord);
        result.defaultTransition = initState;
        result.addNewTransition('<', tagState);
        return result;
    }

    boolean isEndingState(){
        return !wordsOfEndingState.isEmpty();
    }


    void addEndingWords(List<String> endingWords) {
        wordsOfEndingState.addAll(endingWords);
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


    List<State> getTransitionStates(char character){
        // Si no tiene una transicion, devuelve el default
        if(!transitions.containsKey(character)){
            return Collections.singletonList(defaultTransition);
        }

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
        StringBuilder endingState = new StringBuilder();
        for (String word : wordsOfEndingState) {
            endingState.append(word).append(" ");
        }
        if(!wordsOfEndingState.isEmpty()){
            return "-"+name+"- End state " + endingState;
        }
        else{
            StringBuilder result = new StringBuilder();
            for (Character character : transitions.keySet()) {
                result.append(character).append(" ");
            }
            return "-"+name+"- Moves with " + result;
        }
    }
}
