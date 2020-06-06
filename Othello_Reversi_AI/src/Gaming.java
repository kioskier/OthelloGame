import java.util.ArrayList;
import java.util.Random;

public class Gaming {

    Heuristic score = new Heuristic();

    //Variable that holds the maximum depth the MiniMax algorithm will reach for this player
    private int maxDepth;
    //Variable that holds which letter this player controls
    private char playerColor;

    //Default constructor. The depth is assigned to 2 randomly. Xs always play first, so the player's tile color is X.

    public Gaming() {
        maxDepth = 2;
        playerColor = 'X';
    }

    //This constructor is the one used in the main method to initialize the gaming object
    //with the user's choices as parameters.

    public Gaming(int maxDepth, char tileColor) {
        this.maxDepth = maxDepth;
        playerColor = tileColor;

    }

    //Initiates the MiniMax algorithm.
    //We made the assumption that the cpu is always the max.
    public State MiniMax(State curState) {

        if (playerColor == 'X') {
            if (curState.validMoves.isEmpty()) {
                State.endCounter++;
                return curState;
            }
            curState.totalCounter++;
            return max(curState, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 'X', 'O');
        }
        else {
            if (curState.validMoves.isEmpty()) {
                State.endCounter++;
                return curState;
            }
            curState.totalCounter++;
            return max(curState, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 'O', 'X');
        }
    }

    //The max and min functions are called interchangingly, one after another until a max depth is reached
    //a,b are the 2 variables used to implement the a-b prooning
    public State max(State state, int depth, int a, int b, char tileColor, char opponentTileColor) {
        Random r = new Random();

        /* If MAX is called on a state that is terminal or after a maximum depth is reached,
         * then a heuristic is calculated on the state and the state is returned.
         */
        if ((State.endCounter >= 2) || (depth == maxDepth)) {
            int stateScore = score.evaluate(state, tileColor, opponentTileColor);
            state.setScore(stateScore);
            return state;
        }
        //The children-moves of the state are calculated
        ArrayList<State> children = new ArrayList<State>(state.getChildren(tileColor, opponentTileColor));
        State maxState = new State(state.tiles);
        maxState.setScore(Integer.MIN_VALUE);
        for (State child : children) {
            //And for each child min is called, on a lower depth
            State minState = min(child, depth + 1, a, b, opponentTileColor, tileColor);
            //The child-move with the greatest value is selected and returned by max
            if (minState.getScore() >= maxState.getScore()) {
                if ((minState.getScore() == maxState.getScore())) {
                    //If the heuristic has the same value then we randomly choose one of the two moves
                    if (r.nextInt(2) == 0) {
                        maxState.setScore(minState.getScore());
                        maxState = new State(child.tiles);
                    }
                } else {
                    maxState.setScore(minState.getScore());
                    maxState = new State(child.tiles);
                }
            }
            if (maxState.getScore() >= b) return maxState;
            a = Math.max(a, maxState.getScore());
        }
        return maxState;
    }

    //Min works similarly to max
    public State min(State state, int depth, int a, int b, char tileColor, char opponentTileColor) {
        Random r = new Random();

        if ((State.endCounter >= 2) || (depth == maxDepth)) {
            int stateScore = score.evaluate(state, tileColor, opponentTileColor);
            state.setScore(stateScore);
            return state;
        }
        ArrayList<State> children = new ArrayList<State>(state.getChildren(tileColor, opponentTileColor));
        State minState = new State(state.tiles);
        minState.setScore(Integer.MAX_VALUE);
        for (State child : children) {
            State maxState = max(child, depth + 1, a, b, opponentTileColor, tileColor);
            if (maxState.getScore() <= minState.getScore()) {
                if ((maxState.getScore() == minState.getScore())) {
                    if (r.nextInt(2) == 0) {
                        minState.setScore(maxState.getScore());
                        minState = new State(child.tiles);
                    }
                } else {
                    minState.setScore(maxState.getScore());
                    minState = new State(child.tiles);
                }
            }
            if (minState.getScore() <= a) return minState;
            b = Math.min(b, minState.getScore());
        }
        return minState;
    }
}