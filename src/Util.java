import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * @author Gonzalo de Achaval
 */
class Util {

    private enum Shapes {
        CIRCLE("circle"), DOUBLECIRCLE("doublecircle");
        private final String name;
        Shapes(String name){
            this.name = name;
        }
        public String getName() {
            return name;
        }
    }

    static void generateDot(String filename, State initialState) throws IOException {

        ArrayList<State> allStates = new ArrayList<>();
        allStates.add(initialState);
        getAllStates(allStates);
        FileWriter fw = new FileWriter(filename);
        fw.write("digraph {\nrankdir = \"LR\";\n");
        fw.write("// NODES\n");
        for (State state : allStates) {
            nodeWriter(fw, state);
        }
        fw.write("\n// TRANSITIONS\n");
        processTransitions(fw, allStates);
        fw.write("}");
        fw.close();
    }

    private static void nodeWriter(FileWriter fw, State state) throws IOException {
        String name = state.getName();
        fw.write("node [shape=");
        if(state.isEndingState()) {
            fw.write(Shapes.DOUBLECIRCLE.getName());
        } else {
            fw.write(Shapes.CIRCLE.getName());
        }
        String tidyName = name.replaceAll("_", " ");
        tidyName = tidyName.substring(1, tidyName.length());
        fw.write("] Node" + name + "[label=\"" + tidyName + "\"];\n");
    }

    private static void getAllStates(List<State> rslt) {
        for(int i=0; i<rslt.size(); i++) {
            State from = rslt.get(i);
            Iterator<List<State>> possibleStates = from.getTransitions().values().iterator();
            possibleStates.forEachRemaining(statesList -> {
                for (State to : statesList) {
                    if (!rslt.contains(to)) {
                        rslt.add(to);
                    }
                }
            });
        }
    }

    private static void processTransitions(FileWriter fw, List<State> allStates) {
        for(State state: allStates) {
            Map<Character, List<State>> states = state.getTransitions();
            states.forEach((character, stateList) -> {
                for(State toState: stateList){
                    try {
                        writeTransition(fw, state, toState, character);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private static void writeTransition(FileWriter fw, State state, State toState, Character character) throws IOException {
        fw.write("Node" + state.getName() + " -> " + "Node" + toState.getName());
        String escapeChar = "";
        if(character == '\'' || character == '\"'){
            escapeChar += "\\";
        }
        fw.write(" [label=\"" + escapeChar + character + "\"];\n");
    }

    static void createPNGfromDOT(String name) throws IOException {
        File f = new File(name);
        String arg1 = f.getAbsolutePath();
        String arg2 = arg1.substring(0, arg1.length()-4) + ".png"; //delete .dot, add.png
        String[] c = {"dot", "-Tpng", arg1, "-o", arg2};
        Runtime.getRuntime().exec(c);
    }
}
