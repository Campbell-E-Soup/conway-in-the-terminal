import java.util.Scanner;
import java.util.Random;

public class Main {
    //macros
        public static final String ANSI_RESET = "\u001B[0m";  // Reset color
        public static final String ANSI_GREEN = "\u001B[32m"; // Green text
        public static final String ANSI_BLACK = "\u001B[30m"; // Black text
        public static final String ANSI_RED = "\u001B[31m";   // Red Text
        public static final String ANSI_BLUE = "\u001B[34m";  // Blue Text
        public static final  String ANSI_COMMENT = "\u001B[38;5;75m";
    public static void main(String[] args) {
        //init vars
            Scanner in = new Scanner(System.in);
            Random rand = new Random();
            int[] surviveRules = {2,3};
            int[] birthRules = {3};
            String[] tileState = {ANSI_BLACK + "·" + ANSI_RESET,ANSI_BLUE + "▀" + ANSI_RESET,ANSI_RED + "·" + ANSI_RESET};
        //ask for vars
            System.out.println(title);
            int width = getValidInt(in,"Enter A Width: ");
            if (width > 80) {
                System.out.println("Game space size is large, the terminal should be maximized.");
            }
            int height = getValidInt(in,"Enter A Height: ");
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
        int[][] gameSpace = new int[height][width];
        //gameSpace = getPreset(1);
        //width = gameSpace[0].length;
        //height = gameSpace.length;
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
            System.out.print( ANSI_COMMENT + message + ANSI_RESET);
            if (scan.hasNextInt()) {
                return scan.nextInt();
            }
            else {
                System.out.println( ANSI_RED + "Invalid Input, please enter a whole number." + ANSI_RESET);
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
            System.out.println(ANSI_RED + "Thread was interrupted: " + ex.getMessage() + ANSI_RESET);
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
                if (gameSpace[y][x] != 1) {
                    //check for birth
                    if (arrayContains(birth,neighbors)) {
                        newSpace[y][x] = 1;
                        births++;
                    }
                    else if (gameSpace[y][x] == 2) {
                        newSpace[y][x] = 2;
                    }
                }
                else {
                    if (!arrayContains(survive,neighbors)) {
                        newSpace[y][x] = 2;
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
    public static int[][] getPreset(int id) {
        int[][] gameArray = new int[1][1];
            if (id == 1) {
                gameArray = new int[][]{
                        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
                };
            }
            else if (id == 2) {
                gameArray = new int[40][40]; // Create a 40x42 array

                // Fill the array with the specified pattern
                    gameArray[12][8] = 0;
                    gameArray[12][9] = 0;
                    gameArray[12][10] = 1;
                    gameArray[12][11] = 1;

                    gameArray[13][8] = 0;
                    gameArray[13][9] = 0;
                    gameArray[13][10] = 1;
                    gameArray[13][11] = 1;

                    gameArray[14][8] = 1;
                    gameArray[14][9] = 1;
                    gameArray[14][10] = 1;
                    gameArray[14][11] = 0;

                    gameArray[15][8] = 0;
                    gameArray[15][9] = 1;
                    gameArray[15][10] = 0;
                    gameArray[15][11] = 0;
            }
        return gameArray;
    }

    public static String title = ANSI_BLUE +
            " ██████╗ ██████╗ ███╗   ██╗██╗    ██╗ █████╗ ██╗   ██╗███████╗\n" +
            "██╔════╝██╔═══██╗████╗  ██║██║    ██║██╔══██╗╚██╗ ██╔╝██╔════╝\n" +
            "██║     ██║   ██║██╔██╗ ██║██║ █╗ ██║███████║ ╚████╔╝ ███████╗\n" +
            "██║     ██║   ██║██║╚██╗██║██║███╗██║██╔══██║  ╚██╔╝  ╚════██║\n" +
            "╚██████╗╚██████╔╝██║ ╚████║╚███╔███╔╝██║  ██║   ██║   ███████║\n" +
            " ╚═════╝ ╚═════╝ ╚═╝  ╚═══╝ ╚══╝╚══╝ ╚═╝  ╚═╝   ╚═╝   ╚══════╝\n" + ANSI_RED +
            " G A M E    O F    L I F E    I N    T H E    T E R M I N A L\n" + ANSI_RESET;
    public static int gens = 0;
    public static int deaths = 0;
    public static int births = 0;
}