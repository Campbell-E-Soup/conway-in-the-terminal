import java.util.Arrays;
import java.util.Scanner;
import java.util.Random;

public class Main {
    //macros
        public static final String ANSI_RESET = "\u001B[0m";  // Reset color
        public static final String ANSI_GREEN = "\u001B[32m";  // Green text
        public static final String ANSI_BLACK = "\u001B[30m";
    public static void main(String[] args) {

        //init vars
            Scanner in = new Scanner(System.in);
            Random rand = new Random();
            int[] surviveRules = {2,3};
            int[] birthRules = {3};
        //ask for vars

            int width = getValidInt(in,"Enter a width: ");
            if (width > 80) {
                System.out.println("Game space size is large, the terminal should be maximized.");
            }
            int height = getValidInt(in,"Enter a height: ");
            if (height > 25) {
                System.out.println("Game space size is large, the terminal should be maximized");
                timeOut(1000);
                System.out.print(". ");
                timeOut(1000);
                System.out.print(". ");
                timeOut(1000);
                System.out.print(". ");
                timeOut(1000);
                if (width >= 100 || height >= 100) {
                    System.out.println("\nWidth: " + width + "\nHeight: " + height  + "\nGood Luck.");
                    timeOut(5000);
                }
            }

        String[] tileState = {ANSI_BLACK + "·" + ANSI_RESET,ANSI_GREEN + "▀" + ANSI_RESET};
        int[][] gameSpace = new int[height][width];

        width = gameSpace[0].length;
        height = gameSpace.length;
        int confirm = 0;
        while (confirm != 1) {
            clearScreen();
            //randomize game space
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    gameSpace[y][x] = rand.nextInt(2);
                    System.out.print(tileState[gameSpace[y][x]] + " ");
                }
                System.out.println();
            }
            confirm = getValidInt(in,"\nAre you happy with the starting conditions? 0 no, 1 yes: ");
        }
        //ask for vars.
        int generations = getValidInt(in,"How many generations do you want to play? ");
        int gps = getValidInt(in,"How many generations to display per second (recommended 1): ");
        if (gps > 10) {
            System.out.println("Max is 10. Generations per second set to 10.");
        }
        else if (gps < 1) {
            System.out.println("Minimum is 1. Generations per second set to 1.");
        }
        long pause = (long)((1.0/gps) * 1000);
        while (generations > 0) {
            gameSpace = updateGameSpace(gameSpace, surviveRules, birthRules);
            drawGameSpace(gameSpace, tileState);
            timeOut(pause);
            generations--;
        }

    }

    public static int getValidInt(Scanner scan, String message) {
        while (true) {
            System.out.print(message);
            if (scan.hasNextInt()) {
                return scan.nextInt();
            }
            else {
                System.out.println("Invalid Input, please enter a whole number.");
                scan.nextLine();
            }
        }
    }

    public static void clearScreen() {
        // ANSI escape code to clear the screen
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void timeOut(long time) {
        try {
            Thread.sleep(time);
        } catch (Exception ex) {
            System.out.println("Thread was interrupted: " + ex.getMessage());
        }
    }

    public static int[][] updateGameSpace(int[][] gameSpace, int[] survive, int[] birth) {
        int width = gameSpace[0].length;
        int height = gameSpace.length;
        int[][] newSpace = new int[height][width];
        //does the things
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int neighbors = 0;
                for (int xx = x-1; xx <= x+1; xx++) {
                    for (int yy = y-1; yy <= y+1; yy++) {
                        //check if coords are out of range
                        if (xx == x && yy == y) {continue;}
                        else if (xx < 0 || xx >= width) {continue;}
                        else if (yy < 0 || yy >= height ) {continue;}
                        if (gameSpace[yy][xx] == 1) {
                            neighbors++;
                        }
                    }
                }
                //now we have the number of neighbors
                if (gameSpace[y][x] == 0) {
                    //check for birth
                    if (arrayContains(birth,neighbors)) {
                        newSpace[y][x] = 1;
                    }
                }
                else {
                    if (!arrayContains(survive,neighbors)) {
                        newSpace[y][x] = 0;
                    }
                    else {
                        newSpace[y][x] = 1;
                    }
                }
            }
        }
        return newSpace;
    }
    public static void drawGameSpace(int[][] gameSpace, String[] symbols) {
        int width = gameSpace[0].length;
        int height = gameSpace.length;
        clearScreen();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.print(symbols[gameSpace[y][x]] + " ");
            }
            System.out.println();
        }
    }
    public static boolean arrayContains(int[] ary,int contains) {
        for (int j : ary) {
            if (j == contains) {
                return true;
            }
        }
        return false;
    }
}