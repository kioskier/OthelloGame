import java.util.Scanner;



public class Main {

    /*We made the assumption that the Black tiles, in our case the Xs, are always playing 1st.
    The white tiles are the Os
    We store both as char variables.
    Every game is started with both players having 2 tiles each, so 4 tiles in total.*/

    public static void main(String[] args) {

        //Holds the maximum depth the MiniMax algorithm will reach
        int depth;
        //Holds the user's color of choice and the cpu's color
        char userColor, cpuColor;

        //The current State of the Game
        State currentState = new State();

        //Prompts the user to select the difficulty of the game and the variable depth is
        //assigned a fixed value based on the user's selection
        System.out.println("Please select the difficulty, typing 1, 2 or 3.");
        System.out.print("1. Easy\n2. Normal\n3. Hard\n");
        Scanner scanner = new Scanner(System.in);
        String selection = scanner.nextLine().trim().replace(" ", "");
        while (!(selection.equals("1") || selection.equals("2") || selection.equals("3"))) {
            System.out.println("Please type 1, 2 or 3.");
            selection = scanner.nextLine().trim().replace(" ", "");
        }
        if (selection.equals("1")) {
            depth = 4;
        } else if (selection.equals("2")) {
            depth = 6;
        } else {
            depth = 8;
        }

        //Prompts the user to select to play 1st or 2nd

        System.out.println("Do you want to play first? If yes, type Y, else type N");
        selection = scanner.nextLine().trim().replace(" ", "").toUpperCase();
        while (!(selection.equals("Y") || selection.equals("N"))) {
            System.out.println("Please type Y or N.");
            selection = scanner.nextLine().trim().replace(" ", "").toUpperCase();
        }
        if (selection.equals("Y")) {
            userColor = 'X';
            cpuColor = 'O';
        } else {
            userColor = 'O';
            cpuColor = 'X';
        }

        //A Gaming object is created
        Gaming gaming = new Gaming(depth, cpuColor);
        //hold the number of X and O tiles in the current State.
        int oTiles, xTiles;
        //calculates the available moves of the one playing 1st
        currentState.moveGenerator(currentState, 'X');
        //prints the table
        currentState.printState(currentState);
        //Holds how many Xs are in the board
        xTiles = currentState.comTotalTiles(currentState, 'X', 0, 8, 1, 0, 8, 1);
        //Holds how many Os are in the board
        oTiles = currentState.comTotalTiles(currentState, 'O', 0, 8, 1, 0, 8, 1);
        State.totalCounter = xTiles + oTiles;
        System.out.println("X Tiles: " + xTiles + " || O Tiles: " + oTiles);

        /*Depending on the user's selection above, the main functionality needs to be adjusted somewhat differently.
        There are 2 while loops below with around the same lines of code,
        but in different order depending on who is playing first.
        After the while loop has finished, the game is over.*/

        if (userColor == 'X') {
            //Here the user plays 1st
            while (State.endCounter < 2) {
                //Scan the user's input/move
                currentState.scanning(currentState, userColor, cpuColor);
                //calculate the opponent's available moves
                currentState.moveGenerator(currentState, cpuColor);
                currentState.printState(currentState);
                xTiles = currentState.comTotalTiles(currentState, userColor, 0, 8, 1, 0, 8, 1);
                System.out.println("X Tiles: " + xTiles + " || O Tiles: " + (State.totalCounter - xTiles));
                //The pc plays here, so we initiate the Minimax algorithm
                currentState = new State(gaming.MiniMax(currentState).tiles);
                //clear the user's valid Moves from the previous round. They were previously stored but we don't need them anymore.
                currentState.validMoves.clear();
                //calculate the new user's available moves
                currentState.moveGenerator(currentState, userColor);
                currentState.printState(currentState);
                xTiles = currentState.comTotalTiles(currentState, userColor, 0, 8, 1, 0, 8, 1);
                System.out.println("X Tiles: " + xTiles + " || O Tiles: " + (State.totalCounter - xTiles));
            }
        } else {
            //Here the pc plays 1st, so we initiate the Minimax algorithm 1st before scanning for the user's input/move
            while (State.endCounter < 2) {
                currentState = new State(gaming.MiniMax(currentState).tiles);
                currentState.validMoves.clear();
                currentState.moveGenerator(currentState, userColor);
                currentState.printState(currentState);
                xTiles = currentState.comTotalTiles(currentState, 'X', 0, 8, 1, 0, 8, 1);
                System.out.println("X Tiles: " + xTiles + " || O Tiles: " + (State.totalCounter - xTiles));
                currentState.scanning(currentState, userColor, cpuColor);
                currentState.moveGenerator(currentState, cpuColor);
                currentState.printState(currentState);
                xTiles = currentState.comTotalTiles(currentState, 'X', 0, 8, 1, 0, 8, 1);
                System.out.println("X Tiles: " + xTiles + " || O Tiles: " + (State.totalCounter - xTiles));
            }
        }
        System.out.println("\nGame is over!");
        xTiles = currentState.comTotalTiles(currentState, 'X', 0, 8, 1, 0, 8, 1);
        System.out.print("Final Score: ");
        //If the game ended before the board is full,
        //the winning player gets the rest of the "unplayed" spots on his side,
        //so that the total score is always going to add up to 64.
        if (xTiles > (State.totalCounter - xTiles)) {
            System.out.println("X Tiles: " + (64 - State.totalCounter + xTiles) + " || O Tiles: " + (State.totalCounter - xTiles));
            System.out.println("X PLAYER WON");
        } else if (xTiles < (State.totalCounter - xTiles)) {
            System.out.println("X Tiles: " + xTiles + " || O Tiles: " + (64 - xTiles));
            System.out.println("O PLAYER WON");
        } else {
            System.out.println("X Tiles: 32 || O Tiles: 32");
            System.out.println("MATCH ENDED AS A DRAW");
        }
    }

}
