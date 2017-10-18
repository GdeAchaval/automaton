import java.util.*;

/**
 * @author Agustin Bettati
 * @version 1.0
 */
public class WordDetectionAutomaton {
    private State initialState;
    private State currentState;
    private HashMap<String, Integer> frequency;

    public WordDetectionAutomaton(List<String> phrases){
        initialState = new State("0");
        currentState = initialState;
        frequency = new HashMap<>(); //TODO crear tabla con phrases
        generateAutomaton(phrases);
    }

    private WordDetectionAutomaton(State initialState){
        this.initialState = initialState;
        currentState = initialState;
        frequency = new HashMap<>();
    }

    private void generateAutomaton(List<String> phrases) {
        int name = 1;
        for (String phrase : phrases) {


            char[] characters = phrase.toCharArray();

            State aux = initialState;
            for (int i = 0; i <= characters.length -2; i++) {
                State newState = new State(name + "");
                name++;
                aux.addNewTransition(characters[i], newState);
                aux = newState;
            }
            State finalState = new State(name+"", phrase);
            name++;
            aux.addNewTransition(characters[characters.length - 1], finalState);

        }
    }

    public WordDetectionAutomaton createDeterministic(){
        State determinState = new State("0");

        State nonDeterminState = initialState;
        makeDeterministic(determinState, Arrays.asList(nonDeterminState));

        return new WordDetectionAutomaton(determinState);
    }

    private void makeDeterministic(State determinState, List<State> nonDeterminStates) {
        Map<Character, List<State>> determinTransitions = new HashMap<>();
        Map<Character, String> nameOfNewStates = new HashMap<>();

        for (State nonDeterminState : nonDeterminStates) {
            //Si ya es el ultimo
            if(nonDeterminState.isEndingState()){
                String endWord = nonDeterminState.getEndingWord();
                determinState.convertToEndingState(endWord);
            }
            else{
                Map<Character, List<State>> trans = nonDeterminState.getTransitions();
                for (Map.Entry<Character, List<State>> transition : trans.entrySet()){
                    char character = transition.getKey();
                    for (State state : transition.getValue()) {
                        //agregar a determin refs los states para el char
                        addToMap(character,state, determinTransitions);
                        addToMap(character, state.getName(), nameOfNewStates);
                    }
                }
                //para cada char creo nuevo estado, le pongo la ref y llamo al metodo recursivo
            }

        }

        for (Map.Entry<Character, List<State>> transitions : determinTransitions.entrySet()) {
            State newState = new State(nameOfNewStates.get(transitions.getKey()));
            determinState.addNewTransition(transitions.getKey(), newState);
            makeDeterministic(newState, transitions.getValue());

        }

    }

    private void addToMap(char character, String name, Map<Character, String> map) {
        if(map.containsKey(character)){
            map.put(character,map.get(character) +" " + name);
        }
        else{
            map.put(character,name);
        }
    }

    private void addToMap(char character, State state, Map<Character, List<State>> map) {
        if(map.containsKey(character)){
            map.get(character).add(state);
        }
        else{
            List<State> states = new ArrayList<>();
            states.add(state);
            map.put(character, states);
        }

    }


}
