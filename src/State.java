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
    private Set<String> wordsOfEndingState;
    private State defaultTransition;

    
    protected State(String name) {
        this.name = name;
        this.transitions = new HashMap<>();
        wordsOfEndingState = new HashSet<>();
    }

    private State(String name, String wordOfEndingState) {
        this.name = name;
        wordsOfEndingState = new HashSet<>();
        wordsOfEndingState.add(wordOfEndingState);
        this.transitions = new HashMap<>();
    }

    static State createInitState(){
        State result = new State("_0");
        result.defaultTransition = result;
        return result;
    }

    static State createHtmlTagState(State initState){
//        State result = new TagState(initState);
        State result = new State("_tag");
        result.defaultTransition = result;
        result.addNewTransition('>', initState);
        createsTransitionsHtmlTag(result, initState);
        return result;
    }

    private static void createsTransitionsHtmlTag(State tagState, State initState) {
        State field = new State("q0");
        field.defaultTransition = field;
        tagState.addNewTransition('"', field);
        tagState.addNewTransition('\'', field);
        field.addNewTransition('"', tagState);
        field.addNewTransition('\'', tagState);



        State comment1 = new State("q1");
        tagState.addNewTransition('!', comment1);
        comment1.defaultTransition = tagState;

        State innerComment = new State("aux");
        tagState.addNewTransition('<', innerComment);
        innerComment.defaultTransition = tagState;
        innerComment.addNewTransition('!', comment1);

        State comment2 = new State("q2");
        comment1.addNewTransition('-',comment2);
        comment2.defaultTransition = tagState;

        State commentState = new State("q3");
        comment2.addNewTransition('-',commentState);
        commentState.defaultTransition = commentState;

        State endComment1 = new State("q4");
        commentState.addNewTransition('-',endComment1);
        endComment1.defaultTransition = commentState;

        State endComment2 = new State("q5");
        endComment1.addNewTransition('-',endComment2);
        endComment2.defaultTransition = commentState;

        endComment2.addNewTransition('>', initState);

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
        List<String> list = new ArrayList<>();
        list.addAll(wordsOfEndingState);
        return list;
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
