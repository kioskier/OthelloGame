import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*It is used by MiniMax algorithm and represents a node in the tree the Minimax is searching.
It ultimately is the state of the playing board.*/

/*In order to handle the array values easier and to search more efficiently,
we convert each cell's coordinates to a number from 0 to 63, since the array is 8x8.
Basically, given i and j are the 2 coordinates of a cell, i for rows and j for columns,
we can get a cell's "place" in the array by doing some simple math.
i*8 + j-1 gives us the number representing the cell's "location" in the array.
This method is used throughout the code but mostly where we want to search for tiles.*/

public class State {

    //the dimension of the table. In our case the board is 8x8 so its dimension is 8.
    static public int dimension = 8;
    //A char table storing the tiles.
    public char[][] tiles;
    //Holds the score of the State
    public int score;
    //An ArrayList holding the valid moves of a player
    List<Integer> validMoves = new ArrayList<Integer>();
    //An ArrayList holding the tiles that are going to be flipped after a player made a move.
    static List<Integer> tilesToFlip = new ArrayList<Integer>();
    //Holds how many tiles are currently on the board/State.
    static int totalCounter;
    //Counter to indicate when the game is over. It increases by 1 when a player has no valid moves.
    //When both players have no moves left, the game is over.
    static int endCounter = 0;

    //Constructor with a char array as an input. It copies its data to the new State object created.

    public State(char[][] tiles) {
        this.tiles = new char[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                this.tiles[i][j] = tiles[i][j];
            }
        }
    }

    /*The default constructor of a State. It assigns 0 to the score, a new array of tiles is created
    and it's assigned the 4 initial values with which every game starts.*/

    public State() {
        this.score = 0;
        this.tiles = new char[dimension][dimension];
        this.tiles[3][3] = 'O';
        this.tiles[3][4] = 'X';
        this.tiles[4][3] = 'X';
        this.tiles[4][4] = 'O';
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getEndCounter() {
        return endCounter;
    }

    public void setEndCounter(int endCounter) {
        this.endCounter = endCounter;
    }

    //Generates the valid moves of the opponent. They are stored in the validMoves ArrayList.
    //It searches for the valid moves vertically, horizontally and diagonally.

    public void moveGenerator(State currentState, char tileColor) {

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (tileColor == 'X') {
                    if (currentState.tiles[i][j] == 'X') {
                        verticalSearch(i, j, tileColor, 'O', false);
                        horizontalSearch(i, j, tileColor, 'O', false);
                        diagonalSearch(i, j, tileColor, 'O', false);
                    }
                } else if (tileColor == 'O') {
                    if (currentState.tiles[i][j] == 'O') {
                        verticalSearch(i, j, tileColor, 'X', false);
                        horizontalSearch(i, j, tileColor, 'X', false);
                        diagonalSearch(i, j, tileColor, 'X', false);
                    }
                }
            }
        }
    }

    /*This method is responsible for finding the tiles needed to be flipped after a move was made.
    It searches vertically, horizontally and diagonally.
    i and j parameters are the coordinates of the tile played.*/

    public void flipTiles(int i, int j, char tileColor, char opponentTileColor) {
        verticalSearch(i, j, tileColor, opponentTileColor, true);
        horizontalSearch(i, j, tileColor, opponentTileColor, true);
        diagonalSearch(i, j, tileColor, opponentTileColor, true);
    }

    /*This method is used by moveGenerator and flipTiles methods above.
    It searches vertically in the array which holds the tiles for 2 different purposes depending on the boolean value.
    i and j parameters are the coordinates of the tile.
    The boolean determines if the method will search for valid moves or for tiles to flip.
    A false value means it searches for valid moves whereas the true value means it searches for tiles that need to be flipped.*/

    public void verticalSearch(int i, int j, char tileColor, char opponentTileColor, boolean toFlip) {

        //the math used here is explained at the start of this file

        //we search upwards
        //k is used to iterate through the rows
        int k = i - 1;
        //if i=0, so k=-1, then we don't have anything above to search
        if (k >= 0) {
            //while the above tiles are the opponent's tiles and we are not out of bounds
            while (tiles[k][j] == opponentTileColor && k > 0) {
                //check the boolean's value
                if (toFlip) {
                    tilesToFlip.add(k * 8 + j + 1);
                }
                k--;
                if (!toFlip) {
                    if (tiles[k][j] == 0) validMoves.add(k * 8 + j + 1);
                }
            }
            //flip the tiles found above, if any
            if (tiles[k][j] == tileColor && toFlip) {
                for (int tile : tilesToFlip) {
                    if (tile % 8 == 0) {
                        tiles[tile / 8 - 1][7] = tileColor;
                    } else {
                        tiles[tile / 8][tile % 8 - 1] = tileColor;
                    }
                }
            }
            tilesToFlip.clear();

        }

        //we search downwards now. Same logic as above
        k = i + 1;
        if (k <= 7) {
            while (tiles[k][j] == opponentTileColor && k < 7) {
                if (toFlip) {
                    tilesToFlip.add(k * 8 + j + 1);
                }
                k++;
                if (!toFlip)
                    if (tiles[k][j] == 0) validMoves.add(k * 8 + j + 1);
            }
            if (tiles[k][j] == tileColor && toFlip) {
                for (int tile : tilesToFlip) {
                    if (tile % 8 == 0) {
                        tiles[tile / 8 - 1][7] = tileColor;
                    } else {
                        tiles[tile / 8][tile % 8 - 1] = tileColor;
                    }
                }
            }
            tilesToFlip.clear();
        }

    }

    /*This method is used by moveGenerator and flipTiles methods above.
    It searches horizontally in the array which holds the tiles for 2 different purposes depending on the boolean value.
    i and j parameters are the coordinates of the tile.
    The boolean determines if the method will search for valid moves or for tiles to flip.
    A false value means it searches for valid moves whereas the true value means it searches for tiles that need to be flipped.*/

    public void horizontalSearch(int i, int j, char tileColor, char opponentTileColor, boolean toFlip) {

        //the math used here is explained at the start of this file

        //we search the column from right to left
        //k is used to iterate through the columns
        int k = j - 1;
        //if j=0, so k=-1, then we don't have anything left to search
        if (k >= 0) {
            //while the tiles on the left are the opponent's tiles and we are not out of bounds
            while (tiles[i][k] == opponentTileColor && k > 0) {
                if (toFlip) {
                    tilesToFlip.add(i * 8 + k + 1);
                }
                k--;
                if (!toFlip)
                    if (tiles[i][k] == 0) validMoves.add(i * 8 + k + 1);
            }
            //flip the tiles found above, if any.
            if (tiles[i][k] == tileColor && toFlip) {
                for (int tile : tilesToFlip) {
                    if (tile % 8 == 0) {
                        tiles[tile / 8 - 1][7] = tileColor;
                    } else {
                        tiles[tile / 8][tile % 8 - 1] = tileColor;
                    }
                }
            }
            tilesToFlip.clear();
        }


        //we search the column from right to left
        //same logic as above
        k = j + 1;
        if (k <= 7) {
            while (tiles[i][k] == opponentTileColor && k < 7) {
                if (toFlip) {
                    tilesToFlip.add(i * 8 + k + 1);
                }
                k++;
                if (!toFlip)
                    if (tiles[i][k] == 0) validMoves.add(i * 8 + k + 1);
            }
            if (tiles[i][k] == tileColor && toFlip) {
                for (int tile : tilesToFlip) {
                    if (tile % 8 == 0) {
                        tiles[tile / 8 - 1][7] = tileColor;
                    } else {
                        tiles[tile / 8][tile % 8 - 1] = tileColor;
                    }
                }
            }
            tilesToFlip.clear();
        }

    }

    /*This method is used by moveGenerator and flipTiles methods above.
    It searches diagonally in the array which holds the tiles for 2 different purposes depending on the boolean value.
    i and j parameters are the coordinates of the tile.
    The boolean determines if the method will search for valid moves or for tiles to flip.
    A false value means it searches for valid moves whereas the true value means it searches for tiles that need to be flipped.*/

    public void diagonalSearch(int i, int j, char tileColor, char opponentTileColor, boolean toFlip) {

        //the math used here is explained at the start of this file

        //we search the array's upper left diagonal
        //k is used to iterate through the rows
        //z is used to iterate through the columns
        int k = i - 1;
        int z = j - 1;
        if (k >= 0 && z >= 0) {
            while (tiles[k][z] == opponentTileColor && k > 0 && z > 0) {
                if (toFlip) {
                    tilesToFlip.add(k * 8 + z + 1);
                }
                k--;
                z--;
                if (!toFlip)
                    if (tiles[k][z] == 0) validMoves.add(k * 8 + z + 1);
            }
            if (tiles[k][z] == tileColor && toFlip) {
                for (int tile : tilesToFlip) {
                    if (tile % 8 == 0) {
                        tiles[tile / 8 - 1][7] = tileColor;
                    } else {
                        tiles[tile / 8][tile % 8 - 1] = tileColor;
                    }
                }
            }
            tilesToFlip.clear();
        }


        //we search the array's upper right diagonal
        //k is used to iterate through the rows
        //z is used to iterate through the columns

        k = i - 1;
        z = j + 1;
        if (k >= 0 && z <= 7) {
            while (tiles[k][z] == opponentTileColor && k > 0 && z < 7) {
                if (toFlip) {
                    tilesToFlip.add(k * 8 + z + 1);
                }
                k--;
                z++;
                if (!toFlip)
                    if (tiles[k][z] == 0) validMoves.add(k * 8 + z + 1);
            }
            if (tiles[k][z] == tileColor && toFlip) {
                for (int tile : tilesToFlip) {
                    if (tile % 8 == 0) {
                        tiles[tile / 8 - 1][7] = tileColor;
                    } else {
                        tiles[tile / 8][tile % 8 - 1] = tileColor;
                    }
                }
            }
            tilesToFlip.clear();
        }

        //we search the array's bottom left diagonal
        //k is used to iterate through the rows
        //z is used to iterate through the columns
        k = i + 1;
        z = j - 1;
        if (k <= 7 && z >= 0) {
            while (tiles[k][z] == opponentTileColor && k < 7 && z > 0) {
                if (toFlip) {
                    tilesToFlip.add(k * 8 + z + 1);
                }
                k++;
                z--;
                if (!toFlip)
                    if (tiles[k][z] == 0) validMoves.add(k * 8 + z + 1);
            }
            if (tiles[k][z] == tileColor && toFlip) {
                for (int tile : tilesToFlip) {
                    if (tile % 8 == 0) {
                        tiles[tile / 8 - 1][7] = tileColor;
                    } else {
                        tiles[tile / 8][tile % 8 - 1] = tileColor;
                    }
                }
            }
            tilesToFlip.clear();
        }


        //we search the array's bottom right diagonal
        //k is used to iterate through the rows
        //z is used to iterate through the columns
        k = i + 1;
        z = j + 1;
        if (k <= 7 && z <= 7) {
            while (tiles[k][z] == opponentTileColor && k < 7 && z < 7) {
                if (toFlip) {
                    tilesToFlip.add(k * 8 + z + 1);
                }
                k++;
                z++;
                if (!toFlip)
                    if (tiles[k][z] == 0) validMoves.add(k * 8 + z + 1);
            }
            if (tiles[k][z] == tileColor && toFlip) {
                for (int tile : tilesToFlip) {
                    if (tile % 8 == 0) {
                        tiles[tile / 8 - 1][7] = tileColor;
                    } else {
                        tiles[tile / 8][tile % 8 - 1] = tileColor;
                    }
                }
            }
            tilesToFlip.clear();
        }
    }


    /*Method that allows searching specific parts of an array.
    iStart is the row of the array where the search starts,
    and iEnd is the row of the array where the search stops.
    iStep is the step of the iteration.
    jStart, jEnd and jStep are correspondingly the same but for the columns of the array.*/

    public int comTotalTiles(State currentState, char tileColor, int iStart, int iEnd, int iStep, int jStart, int jEnd, int jStep) {
        int comTilesCounter = 0;
        for (int i = iStart; i < iEnd; i += iStep) {
            for (int j = jStart; j < jEnd; j += jStep) {
                if (currentState.tiles[i][j] == tileColor) comTilesCounter++;
            }
        }
        return comTilesCounter;
    }

    //This method is responsble for handling the player's input. It checks user input for possible mistakes
    //and when the input is in an acceptable form, it makes the player's move.


    public void scanning(State currentState, char tileColor, char opponentColor) {
        //if there are no valid moves, increment a counter and print something.
        if (validMoves.isEmpty()) {
            endCounter++;
            System.out.println("No valid moves exist for " + tileColor + "!");
        //else prompt the players to make his move
        } else {
            totalCounter++;
            endCounter = 0;
            //check the user input for errors until it is in a certain form
            System.out.print("Currently playing player " + tileColor + "\nEnter the position of your next move, using a letter and a number (eg. B7): ");
            Scanner scanner = new Scanner(System.in);
            String position = scanner.nextLine().trim().replace(" ", "").toUpperCase();
            while (position.length() != 2) {
                System.out.print("Please enter a valid position: ");
                position = scanner.nextLine().trim().replace(" ", "").toUpperCase();
            }
            //the player's move
            int move = ((int) position.charAt(1) - 49) * 8 + ((int) position.charAt(0) - 65) + 1;
            while (!(validMoves.contains(move) && (position.substring(0, 1).compareToIgnoreCase("A") >= 0 && position.substring(0, 1).compareToIgnoreCase("H") <= 0 &&
                    position.substring(1, 2).compareToIgnoreCase("1") >= 0 && position.substring(1, 2).compareToIgnoreCase("8") <= 0 &&
                    position.length() == 2))) {
                System.out.print("Please enter a valid position: ");
                position = scanner.nextLine().trim().replace(" ", "").toUpperCase();
                if (position.length() == 2) {
                    move = ((int) position.charAt(1) - 49) * 8 + ((int) position.charAt(0) - 65) + 1;
                }
            }
            System.out.println("User played on: " + position);
            //add the move to the array
            currentState.tiles[(int) position.charAt(1) - 49][(int) position.charAt(0) - 65] = tileColor;
            //call flipTiles to change the array
            flipTiles((int) position.charAt(1) - 49, (int) position.charAt(0) - 65, tileColor, opponentColor);
            validMoves.clear();
        }
    }

    /*Returns the children of a State in the MiniMax tree.
    Since we already have an ArrayList containing the valid moves a player can do,
    a child is a State with this valid move played.
    So if there are no valid moves, there are no children.*/

    public ArrayList<State> getChildren(char tileColor, char opponentTileColor) {
        ArrayList<State> children = new ArrayList<State>();
        for (int valid : validMoves) {
            State child = new State(this.tiles);
            if (valid % 8 == 0) {
                child.tiles[valid / 8 - 1][7] = tileColor;

            } else {
                child.tiles[valid / 8][valid % 8 - 1] = tileColor;
            }

            if (valid % 8 == 0) {
                child.flipTiles(valid / 8 - 1, 7, tileColor, opponentTileColor);
            } else {
                child.flipTiles(valid / 8, valid % 8 - 1, tileColor, opponentTileColor);
            }
            //call the moveGenerator so that the child has also an ArrayList
            //with its valid moves.
            child.moveGenerator(child, opponentTileColor);
            children.add(child);
        }
        return children;
    }

    //Prints the State in a certain way.
    //The player's valid moves are printed as ~,
    //the empty spaces are printed as a middle dot.

    public void printState(State currentState) {

        System.out.println("------------------------------------");
        System.out.println('\t' + "A" + '\t' + "B" + '\t' + "C" + '\t' + "D" + '\t' + "E" + '\t' + "F" + '\t' + "G" + '\t' + "H");


        for (int i = 0; i < this.dimension; i++) {
            System.out.print(i + 1 + "\t");
            for (int j = 0; j < this.dimension; j++) {
                if (validMoves.contains(i * 8 + j + 1)) {
                    System.out.print('~');
                } else if (currentState.tiles[i][j] == 0) {
                    System.out.print("\u00B7"); //the middle dot character
                } else {
                    System.out.print(currentState.tiles[i][j]);
                }
                if (j < this.dimension - 1) {
                    System.out.print("\t");
                }
            }
            System.out.println();
        }

        System.out.println("------------------------------------");
    }
}