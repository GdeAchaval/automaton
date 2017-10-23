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
        initialState = State.createInitState();
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

        State htmlTag = State.createHtmlTagState(initialState);
        initialState.addNewTransition('<', htmlTag);

        for (String phrase : phrases) {
            //program is case insensitive
            String lowerCase = phrase.toLowerCase();

            char[] characters = lowerCase.toCharArray();

            State aux = initialState;
            for (int i = 0; i <= characters.length -2; i++) {
                State newState = State.createNormalState("_"+name,initialState, htmlTag);
                name++;
                aux.addNewTransition(characters[i], newState);
                if(characters[i] == ' ')
                    newWordState.add(newState);
                aux = newState;
            }

            State finalState = State.createEndingState("_"+name, phrase,initialState,htmlTag);

            name++;
            aux.addNewTransition(characters[characters.length - 1], finalState);
        }

        for (State stateThatGoesToNewWord : newWordState) {
            final Map<Character, List<State>> initTrans = initialState.getTransitions();
            for (Map.Entry<Character, List<State>> transition : initTrans.entrySet()){
                char character = transition.getKey();
                if(character != '<') {
                    for (State state : transition.getValue()) {
                        stateThatGoesToNewWord.addNewTransition(character, state);
                    }
                }
            }
        }
    }

    WordDetectionAutomaton createDeterministic(){
        State determinState = State.createInitState();
        State htmlTag = State.createHtmlTagState(determinState);
        determinState.addNewTransition('<', htmlTag);

        State nonDeterminState = initialState;
        makeDeterministic(determinState, Collections.singletonList(nonDeterminState), determinState, htmlTag);

        return new WordDetectionAutomaton(determinState, phrases);
    }

    /*
    Este metodo recursivo convierte una lista de estados no deterministas en un solo
    estado determinista que contiene todas sus transiciones y posibles ending words.
     */
    private void makeDeterministic(State determinState, List<State> nonDeterminStates, State determinInit,State htmlState) {
        Map<Character, List<State>> determinTransitions = new HashMap<>();
        /*
            determinTransition es un mapa que va a contener para cada char todas sus transiciones
         */
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
                        addToMap(character,state, determinTransitions);
                        addToMap(character, state.getName(), nameOfNewStates);
                    }
                }
            }

        }

        for (Map.Entry<Character, List<State>> transitions : determinTransitions.entrySet()) {
            /* Cuando el char es '<', se sabe que  el determinState va al html tag por default,
               por lo cual no se realiza ningun cambio.
            */
            if(! (transitions.getKey() == '<')){

                List<State> states = transitions.getValue();

                State combinedState = findCombinedState(determinInit, states);
                if (combinedState != null) {
                    determinState.addNewTransition(transitions.getKey(), combinedState);
                } else {
                    String nameOfNewState = nameOfNewStates.get(transitions.getKey());
                    State newState = State.createNormalState(nameOfNewState, determinInit, htmlState);
                    determinState.addNewTransition(transitions.getKey(), newState);
                    makeDeterministic(newState, states, determinInit, htmlState);
                }
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

    /*
    Esto metodo se llama una vez que el automata es transformado a determinista.
     */
    Map<String, Integer> getFrequencies(String text){

        String lowerCase = text.toLowerCase();
        char[] array = lowerCase.toCharArray();
        Map<String, Integer> frequencies = new HashMap<>();
        for (String phrase : phrases) {
            frequencies.put(phrase, 0);
        }

        currentState = initialState;

        int i = 0;

        while(i < array.length){
            char character = array[i];
            List<State> listOfStates= currentState.getTransitionStates(character);
            //asumo que estoy llamando al metodo con un automata determinista
            currentState = listOfStates.get(0);

            List<String> endingWords = currentState.getEndingWords();

            //Si es un estado de aceptacion, y termina la palabra se agrega su frequency
            i++;
            if(i == array.length || array[i] == ' ' || (!Character.isDigit(array[i]) && !Character.isLetter(array[i])) ){
                for (String word : endingWords) {
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
