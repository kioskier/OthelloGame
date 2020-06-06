
public class Heuristic {

    /*Method which calls all the heuristics and assigns a score to a State. The score from each individual heuristic
    is weighted in order to play certain moves with a better chance.
    e.g. A State that has the player's tile in a corner ([1,1], [0,8] etc)
    is considered a better move and it usually should be played*/

    public int evaluate(State currentState, char tileColor, char opponentTileColor) {

        //holds the number of tiles with the player's tile color
        int comTilesCounter1 = currentState.comTotalTiles(currentState, tileColor, 0, State.dimension, 1, 0, State.dimension, 1);
        //holds the number of tiles with the opponent's tile color
        int comTilesCounter2 = currentState.comTotalTiles(currentState, opponentTileColor, 0, State.dimension, 1, 0, State.dimension, 1);
        //holds the number of all the tiles in a State
        int totalCounter = (comTilesCounter1 + comTilesCounter2);

        int score = 50 * cornerHeuristic(currentState, tileColor) + 3 * gamePhaseHeuristic(comTilesCounter1, totalCounter) +
                7 * centerSquareHeuristic(currentState, tileColor, totalCounter) + 25 * cornerNeighborsHeuristic(currentState, tileColor, opponentTileColor) +
                5 * dangerousSquareHeuristic(currentState, tileColor, totalCounter) + 5 * perimeterHeuristic(currentState, tileColor, totalCounter);
        return score;
    }


    //The heuristic to see if the State has the player's tile in a corner

    private int cornerHeuristic(State currentState, char tileColor) {
        int sum = 0;
        if (currentState.tiles[0][0] == tileColor) {
            sum += 4;
        }
        if (currentState.tiles[0][7] == tileColor) {
            sum += 4;
        }
        if (currentState.tiles[7][0] == tileColor) {
            sum += 4;
        }
        if (currentState.tiles[7][7] == tileColor) {
            sum += 4;
        }
        return sum;
    }


    /*Divided the game in 4 phases depending on how many tiles have been played,
    and the ratio NoOfUserTiles/AllTilesOnBoard.
    Variable totalcounter is the number of tiles on the board(State)
    Variable comTilesCounter1 is the number of the player's tiles.*/


    private int gamePhaseHeuristic(int comTilesCounter1, int totalCounter) {

        if (totalCounter < 25 && comTilesCounter1 / totalCounter < 0.35)
            return 1;
        else if (totalCounter > 24 && totalCounter < 45 && comTilesCounter1 / totalCounter < 0.4)
            return 2;
        else if (totalCounter > 44 && totalCounter < 55 && comTilesCounter1 / totalCounter < 0.5 && comTilesCounter1 / totalCounter > 0.35)
            return 2;
        else if (totalCounter > 54 && comTilesCounter1 / totalCounter > 0.5) return 3;
        else return 0;
    }


    /*The big center square are the 16 spots in the middle of the board.
    Staying in the middle area is considered a good strategy in early game,
    where early game is less than 35 tiles played.*/

    private int centerSquareHeuristic(State currentState, char tileColor, int totalCounter) {
        //holds the number of tiles in the center square
        int centerCounter = currentState.comTotalTiles(currentState, tileColor, 2, 6, 1, 2, 6, 1);
        if (totalCounter > 4 && totalCounter < 35) {
            if (centerCounter / totalCounter > 0.17 && centerCounter / totalCounter < 0.26) return 2;
        }
        return 0;
    }

    /*Playing in the perimeter of the board is considered a good move,
    so the score of the State is dependent on if the player's move is on the perimeter.*/

    private int perimeterHeuristic(State currentState, char tileColor, int totalCounter) {
        int comTilesCounterHorizontal = currentState.comTotalTiles(currentState, tileColor, 2, 6, 1, 0, 8, 7);
        int comTilesCounterVertical = currentState.comTotalTiles(currentState, tileColor, 0, 8, 7, 2, 6, 1);
        int totalPerimeter = comTilesCounterHorizontal + comTilesCounterVertical;

        if (totalCounter > 34 && totalPerimeter / totalCounter > 0.35) return 2;
        return 0;
    }


    /*The 2nd outer square is the 6x6 square in the board.
    Playing there is considered dangerous as it many times enables the opponent
    to play in the perimeter or in a corner.*/

    private int dangerousSquareHeuristic(State currentState, char tileColor, int totalCounter) {
        int comTilesCounterHorizontal = currentState.comTotalTiles(currentState, tileColor, 2, 6, 1, 1, 7, 5);
        int comTilesCounterVertical = currentState.comTotalTiles(currentState, tileColor, 1, 7, 5, 2, 6, 1);
        int totalPerimeter = comTilesCounterHorizontal + comTilesCounterVertical;

        if (totalCounter < 40 && totalPerimeter / totalCounter < 0.3) return 1;
        return 0;
    }

    /*The corner neighbors are the 3 tiles surrounding a corner.
    Playing there is considered a bad move as it often enables the opponent to take a corner.*/

    private int cornerNeighborsHeuristic(State currentState, char tileColor, char opponentTileColor) {
        int sum = 0;
        if (currentState.tiles[0][0] == opponentTileColor) {
            if (currentState.tiles[0][1] == tileColor || currentState.tiles[1][0] == tileColor || currentState.tiles[1][1] == tileColor) {
                sum++;
            }
        } else if (currentState.tiles[0][0] == 0) {
            if (currentState.tiles[0][1] == tileColor || currentState.tiles[1][0] == tileColor || currentState.tiles[1][1] == tileColor) {
                sum--;
            }
        }
        if (currentState.tiles[0][7] == opponentTileColor) {
            if (currentState.tiles[0][6] == tileColor || currentState.tiles[1][6] == tileColor || currentState.tiles[1][7] == tileColor) {
                sum++;
            }
        } else if (currentState.tiles[0][7] == 0) {
            if (currentState.tiles[0][6] == tileColor || currentState.tiles[1][6] == tileColor || currentState.tiles[1][7] == tileColor) {
                sum--;
            }
        }
        if (currentState.tiles[7][0] == opponentTileColor) {
            if (currentState.tiles[6][0] == tileColor || currentState.tiles[6][1] == tileColor || currentState.tiles[7][1] == tileColor) {
                sum++;
            }
        } else if (currentState.tiles[7][0] == 0) {
            if (currentState.tiles[6][0] == tileColor || currentState.tiles[6][1] == tileColor || currentState.tiles[7][1] == tileColor) {
                sum--;
            }
        }
        if (currentState.tiles[7][7] == opponentTileColor) {
            if (currentState.tiles[7][6] == tileColor || currentState.tiles[6][7] == tileColor || currentState.tiles[6][6] == tileColor) {
                sum++;
            }
        } else if (currentState.tiles[7][7] == 0) {
            if (currentState.tiles[7][6] == tileColor || currentState.tiles[6][7] == tileColor || currentState.tiles[6][6] == tileColor) {
                sum--;
            }
        }
        return sum;
    }

}