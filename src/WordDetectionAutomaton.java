import java.util.*;

/**
 * @author Gonzalo De Achaval
 * @author Marcos Khabie
 * @author Agustin Bettati
 * @version 1.0
 */
class WordDetectionAutomaton {
    private State initialState;
    private State currentState;
    private List<String> phrases;

    WordDetectionAutomaton(List<String> phrases){
        initialState = new State("_0");
        currentState = initialState;
        this.phrases = phrases;
        generateAutomaton(phrases);
    }

    private WordDetectionAutomaton(State initialState, List<String> phrases){
        this.initialState = initialState;
        currentState = initialState;
        this.phrases = phrases;
    }

    private void generateAutomaton(List<String> phrases) {
        int name = 1;
        List<State> newWordState = new ArrayList<>(); //Son estados que se llego solo con un esp.

        State htmlTag = new State("tag");
        htmlTag.addNewTransition('>', initialState);
        initialState.addNewTransition('<', htmlTag);

        for (String phrase : phrases) {
            //program is case insensitive
            String lowerCase = phrase.toLowerCase();

            char[] characters = lowerCase.toCharArray();

            State aux = initialState;
            for (int i = 0; i <= characters.length -2; i++) {
                State newState = new State("_"+name );
                name++;
                aux.addNewTransition(characters[i], newState);
                if(characters[i] == ' ')
                    newWordState.add(newState);
                aux = newState;
            }
            State finalState = new State("_"+name, phrase);
            name++;
            aux.addNewTransition(characters[characters.length - 1], finalState);
        }

        for (State stateThatGoesToNewWord : newWordState) {
            final Map<Character, List<State>> initTrans = initialState.getTransitions();
            for (Map.Entry<Character, List<State>> transition : initTrans.entrySet()){
                char character = transition.getKey();
                for (State state : transition.getValue()) {
                    stateThatGoesToNewWord.addNewTransition(character, state);
                }
            }
        }
    }

    WordDetectionAutomaton createDeterministic(){
        State determinState = new State("_0");

        State nonDeterminState = initialState;
        makeDeterministic(determinState, Collections.singletonList(nonDeterminState), determinState);

        return new WordDetectionAutomaton(determinState, phrases);
    }

    private void makeDeterministic(State determinState, List<State> nonDeterminStates, State determinInit) {
        Map<Character, List<State>> determinTransitions = new HashMap<>();
        Map<Character, String> nameOfNewStates = new HashMap<>();

        for (State nonDeterminState : nonDeterminStates) {
            //Si ya es el ultimo
            if(nonDeterminState.isEndingState()){
                List<String> endingWords = nonDeterminState.getEndingWords();
                determinState.addEndingWords(endingWords);
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
            List<State> states = transitions.getValue();

            State combinedState = findCombinedState(determinInit, states);
            if(combinedState != null){
                determinState.addNewTransition(transitions.getKey(), combinedState);
            }
            else {
                State newState = new State(nameOfNewStates.get(transitions.getKey()));
                determinState.addNewTransition(transitions.getKey(), newState);
                makeDeterministic(newState, transitions.getValue(), determinInit);
            }

        }

    }

    private State findCombinedState(State determinInit, List<State> states) {
        StringBuilder allNumbers = new StringBuilder();
        for (State state : states) {
            allNumbers.append(state.getName());
        }
        String[] nonDetStates = allNumbers.toString().split("_");

        final List<State> allDeterminStates = new ArrayList<>();
        allDeterminStates.add(determinInit);
        getAllStates(allDeterminStates);

        for (State determinState : allDeterminStates) {
            String nameOfPosibleState = determinState.getName();
            String[] determinCombinedState = nameOfPosibleState.split("_");

            HashSet<String> set1 = new HashSet<>(Arrays.asList(nonDetStates));
            HashSet<String> set2 = new HashSet<>(Arrays.asList(determinCombinedState));
            if(set1.equals(set2)){
                return determinState;
            }
        }
        return null;
    }

    private void getAllStates(List<State> allStates) {
        for(int i=0; i<allStates.size(); i++) {
            State from = allStates.get(i);
            Iterator<List<State>> states = from.getTransitions().values().iterator();
            states.forEachRemaining(statesList -> {
                for (State aState : statesList) {
                    if (!allStates.contains(aState)) {
                        allStates.add(aState);
                    }
                }
            });
        }
    }

    private void addToMap(char character, String name, Map<Character, String> map) {
        if(map.containsKey(character)){
            map.put(character,map.get(character) + name);
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

    Map<String, Integer> getFrequencies(String text){
        String lowerCase = text.toLowerCase();
        char[] array = lowerCase.toCharArray();
        Map<String, Integer> frequencies = new HashMap<>();
        for (String phrase : phrases) {
            frequencies.put(phrase, 0);
        }

        for (char character : array) {
            if(currentState.hasTransition(character)){
                List<State> listOfStates= currentState.getTransitionStates(character);
                //asumo que estoy llamando al metodo en un determinista
                currentState = listOfStates.get(0);
            }
            else {
                if(!currentState.getName().equals("tag")) {
                    currentState = initialState;
                }
            }

            if(currentState.isEndingState()){
                List<String> words = currentState.getEndingWords();
                for (String word : words) {
                    frequencies.put(word,frequencies.get(word) + 1);
                }
            }
        }

        return frequencies;
    }

    State getInitialState() {
        return initialState;
    }

}
