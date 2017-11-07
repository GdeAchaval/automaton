import java.util.Collections;
import java.util.List;

/**
 * @author Agustin Bettati
 * @version 1.0
 */
public class TagState extends State {

    private boolean inComment;
    private State initState;

    public TagState(State initState) {
        super("_tag");
        this.initState = initState;

    }

    @Override
    List<State> getTransitionStates(char character) {
        if(character == '>' && !inComment){
            return Collections.singletonList(initState);
        }
        if(character == '"' || character == '\''){
            inComment = !inComment;
        }

        return Collections.singletonList(this);

    }
}
