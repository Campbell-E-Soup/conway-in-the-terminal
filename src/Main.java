import java.util.Dictionary;
import java.util.Scanner;
import java.util.Random;

public class Main {
    //finals
        public static final String ANSI_RESET = "\u001B[0m";  // Reset color
        public static final String ANSI_GREEN = "\033[38;5;10m"; // Green text
        public static final String ANSI_BLACK = "\u001B[30m"; // Black text
        public static final String ANSI_RED = "\u001B[31m";   // Red Text
        public static final String ANSI_BLUE = "\u001B[34m";  // Blue Text
        public static final String ANSI_YELLOW = "\033[93m";  // Blue Text
        public static final String ANSI_ORANGE = "\033[38;5;214m";  // Blue Text
        public static final String ANSI_COMMENT = "\u001B[38;5;75m";
    public static void main(String[] args) {
        //init vars
            clearScreen();
            Scanner in = new Scanner(System.in);
            Random rand = new Random();
            int[] surviveRules;
            int[] birthRules;
            int[][] gameSpace;
            int height = 0;
            int width = 0;
            String[] tileState = {ANSI_BLACK + "·" + ANSI_RESET,ANSI_GREEN+ "▀" + ANSI_RESET,ANSI_RED + "·" + ANSI_RESET};
        //ask for vars
            System.out.println(title);
            timeOut(200);
            System.out.print(ANSI_BLUE);
            for (int i = 0; i < 62; i++) {
                System.out.print("█");
                timeOut(25);
            }
            int repeat = 1;
            while (repeat != 0) {
                deaths = 0;
                births = 0;
                gens = 0;
                System.out.println(ANSI_COMMENT + "\n\nSELECT PATTERN:\n[0] Randomized Pattern (XxY)\n[1] Simple Glider (13x13)\n[2] 3 Phase Pulsar (15x15)\n[3] Glider Gun (15x38)\n[4] Humble Beginnings (35x35)\n[5] The Acorn (40x32)");

                System.out.println(ANSI_RESET);
                int pattern = getValidInt(in, "PATTERN: ");
                System.out.println(ANSI_COMMENT + "\nSELECT RULES:\n[0] Conway's Rules (B2 S23)\n[1] Replicator (B1357 S1357)\n[2] Mazectric with Mice (B37 S1234)\n[3] Diamoeba (B45678 S4678)\n");
                int rule = getValidInt(in, "RULE: ");
                birthRules = getBirthRules(rule);
                surviveRules = getSurviveRules(rule);
                boolean randomize = true;
                if (pattern <= 0 || pattern >= 6) {
                    width = getValidInt(in, "Enter A Width: ");
                    if (width > 80) {
                        System.out.println("Game space size is large, the terminal should be maximized.");
                    }
                    height = getValidInt(in, "Enter A Height: ");
                    if (height > 25) {
                        System.out.print(ANSI_COMMENT + "Game space size is large, the terminal should be maximized ");
                        timeOut(1000);
                        System.out.print(". ");
                        timeOut(1000);
                        System.out.print(". ");
                        timeOut(1000);
                        System.out.print(". ");
                        timeOut(1000);
                        if (width >= 100 || height >= 100) {
                            System.out.println(ANSI_RED + "\nWidth: " + width + "\nHeight: " + height + "\nGood Luck.");
                            timeOut(5000);
                        }
                        System.out.println();
                    }
                    gameSpace = new int[height][width];
                } else {
                    gameSpace = getPreset(pattern);
                    randomize = false;
                }
                width = gameSpace[0].length;
                height = gameSpace.length;
                int confirm = 0;
                while (confirm != 1) {
                    clearScreen();
                    //randomize game space
                    for (int y = 0; y < height; y++) {
                        for (int x = 0; x < width; x++) {
                            if (randomize) {
                                if (rule == 1 || rule == 2) {
                                    int dw = width/4;
                                    int dh = height/4;
                                    if (x > dw && x < dw*2 && y > dh && y < dh*2) {
                                        gameSpace[y][x] = rand.nextInt(2);
                                    }
                                }
                                else {
                                    gameSpace[y][x] = rand.nextInt(2);
                                }
                            }
                            ;
                            System.out.print(tileState[gameSpace[y][x]] + " ");
                        }
                        System.out.println();
                    }
                    if (randomize) {
                        confirm = getValidInt(in, "\nAre you happy with the starting conditions? [0] - NO, [1] - YES: ");
                    } else {
                        confirm = 1;
                    }
                }
                //ask for vars.
                int doAgain = 1;
                while (doAgain != 0) {
                    int generations = getValidInt(in, "How many generations do you want to play? ");
                    int gps = getValidInt(in, "How many generations to display per second (recommended 1 or 2): ");
                    if (gps > 6) {
                        System.out.println("Max is 6. Generations per second set to 6.");
                    } else if (gps < 1) {
                        System.out.println("Minimum is 1. Generations per second set to 1.");
                    }
                    long pause = (long) ((1.0 / gps) * 1000);
                    while (generations > 0) {
                        gens++;
                        gameSpace = updateGameSpace(gameSpace, surviveRules, birthRules);
                        drawGameSpace(gameSpace, tileState);
                        timeOut(pause);
                        generations--;
                    }
                    doAgain = getValidInt(in,"Continue game? [0] - NO, [1] - YES: ");
                }
                repeat = getValidInt(in,"Start a new game? [0] - NO, [1] - YES: ");
            }
        clearScreen();
        System.out.println(ANSI_ORANGE + "\n T  H  A  N  K    Y  O  U    F  O  R    P  L  A  Y  I  N  G ! " + ANSI_RESET);
        System.out.println(title);
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
                        deaths++;
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
        System.out.println(ANSI_ORANGE + "GENERATIONS: " + gens + " | DEATHS: " + deaths + " | BIRTHS: " + births );
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
                gameArray = new int[13][13];

                gameArray[0][0] = 1;

                gameArray[1][1] = 1;
                gameArray[1][2] = 1;

                gameArray[2][0] = 1;
                gameArray[2][1] = 1;
            }
            else if (id == 2) {
                gameArray = new int[15][15];

                gameArray[1][3] = 1;
                gameArray[1][4] = 1;
                gameArray[1][5] = 1;
                gameArray[1][9] = 1;
                gameArray[1][10] = 1;
                gameArray[1][11] = 1;

                gameArray[3][1] = 1;
                gameArray[3][6] = 1;
                gameArray[3][8] = 1;
                gameArray[3][13] = 1;

                gameArray[4][1] = 1;
                gameArray[4][6] = 1;
                gameArray[4][8] = 1;
                gameArray[4][13] = 1;

                gameArray[5][1] = 1;
                gameArray[5][6] = 1;
                gameArray[5][8] = 1;
                gameArray[5][13] = 1;

                gameArray[6][3] = 1;
                gameArray[6][4] = 1;
                gameArray[6][5] = 1;
                gameArray[6][9] = 1;
                gameArray[6][10] = 1;
                gameArray[6][11] = 1;

                gameArray[8][3] = 1;
                gameArray[8][4] = 1;
                gameArray[8][5] = 1;
                gameArray[8][9] = 1;
                gameArray[8][10] = 1;
                gameArray[8][11] = 1;

                gameArray[9][1] = 1;
                gameArray[9][6] = 1;
                gameArray[9][8] = 1;
                gameArray[9][13] = 1;

                gameArray[10][1] = 1;
                gameArray[10][6] = 1;
                gameArray[10][8] = 1;
                gameArray[10][13] = 1;

                gameArray[11][1] = 1;
                gameArray[11][6] = 1;
                gameArray[11][8] = 1;
                gameArray[11][13] = 1;

                gameArray[13][3] = 1;
                gameArray[13][4] = 1;
                gameArray[13][5] = 1;
                gameArray[13][9] = 1;
                gameArray[13][10] = 1;
                gameArray[13][11] = 1;
            }
            else if (id == 3) {
                gameArray = new int[15][38];
                gameArray[1][25] = 1;

                gameArray[2][23] = 1;
                gameArray[2][25] = 1;

                gameArray[3][13] = 1;
                gameArray[3][14] = 1;
                gameArray[3][21] = 1;
                gameArray[3][22] = 1;
                gameArray[3][35] = 1;
                gameArray[3][36] = 1;

                gameArray[4][12] = 1;
                gameArray[4][16] = 1;
                gameArray[4][21] = 1;
                gameArray[4][22] = 1;
                gameArray[4][35] = 1;
                gameArray[4][36] = 1;

                gameArray[5][1] = 1;
                gameArray[5][2] = 1;
                gameArray[5][11] = 1;
                gameArray[5][17] = 1;
                gameArray[5][21] = 1;
                gameArray[5][22] = 1;

                gameArray[6][1] = 1;
                gameArray[6][2] = 1;
                gameArray[6][11] = 1;
                gameArray[6][15] = 1;
                gameArray[6][17] = 1;
                gameArray[6][18] = 1;
                gameArray[6][23] = 1;
                gameArray[6][25] = 1;

                gameArray[7][11] = 1;
                gameArray[7][17] = 1;
                gameArray[7][25] = 1;

                gameArray[8][12] = 1;
                gameArray[8][16] = 1;

                gameArray[9][13] = 1;
                gameArray[9][14] = 1;
            }
            else if (id == 4) {
                gameArray = new int[35][35]; // Create a 35x35 array

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
            else if (id == 5) {
                gameArray = new int[40][32]; // Create a 40x32 array

                gameArray[16][16] = 1;

                gameArray[17][18] = 1;

                gameArray[18][19] = 1;
                gameArray[18][20] = 1;
                gameArray[18][21] = 1;
                gameArray[18][16] = 1;
                gameArray[18][15] = 1;
            }
        return gameArray;
    }

    public static String title = ANSI_COMMENT +
            " ██████╗ ██████╗ ███╗   ██╗██╗    ██╗ █████╗ ██╗   ██╗███████╗\n" +
            "██╔════╝██╔═══██╗████╗  ██║██║    ██║██╔══██╗╚██╗ ██╔╝██╔════╝\n" +
            "██║     ██║   ██║██╔██╗ ██║██║ █╗ ██║███████║ ╚████╔╝ ███████╗\n" +
            "██║     ██║   ██║██║╚██╗██║██║███╗██║██╔══██║  ╚██╔╝  ╚════██║\n" +
            "╚██████╗╚██████╔╝██║ ╚████║╚███╔███╔╝██║  ██║   ██║   ███████║\n" +
            " ╚═════╝ ╚═════╝ ╚═╝  ╚═══╝ ╚══╝╚══╝ ╚═╝  ╚═╝   ╚═╝   ╚══════╝\n" + ANSI_RED +
            " G A M E    O F    L I F E    I N    T H E    T E R M I N A L " + ANSI_RESET;
    public static int gens = 0;
    public static int deaths = 0;
    public static int births = 0;

    public static int[] getBirthRules(int index) {
        int[] birthRules = {3};
        if (index == 1) {
            birthRules = new int[] {1,3,5,7};
        }
        else if (index == 2) {
            birthRules = new int[] {3,7};
        }
        else if (index == 3) {
            birthRules = new int[] {4,5,6,7,6};
        }

        return birthRules;
    }
    public static int[] getSurviveRules(int index) {
        int[] surviveRules = {3};
        if (index == 1) {
            surviveRules = new int[] {1,3,5,7};
        }
        else if (index == 2) {
            surviveRules = new int[] {1,2,3,4};
        }
        else if (index == 3) {
            surviveRules = new int[] {4,6,7,8};
        }

        return surviveRules;
    }
}