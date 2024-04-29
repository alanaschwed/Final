import java.io.*;
import java.util.*;

/**
 * The {@code ForestSimulation} class represents a simple simulation environment for managing forests and their trees.
 * It allows for various operations including adding, cutting down, and saving trees.
 */
public class ForestSimulation {
    private static Map<String, Forest> forests = new HashMap<>();
    private static Forest currentForest;
    private static Scanner keyboard = new Scanner(System.in);
    private static final int MIN_HEIGHT_TO_PLANT = 10;
    private static final int MIN_GROWTH_RATE = 10;


    /**
     * The main method that runs the forestry simulation, allowing user interaction through a command-line menu.
     * @param args The command line arguments, expected to include forest names for initialization.
     */

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("No forest names provided on the command line.");
            return;
        }

        System.out.println("Welcome to the Forestry Simulation");
        System.out.println("----------------------------------");
        loadForests(args);


        String option;
        do {
            System.out.print("(P)rint, (A)dd, (C)ut, (G)row, (R)eap, (S)ave, (L)oad, (N)ext, e(X)it : ");
            option = keyboard.nextLine().trim().toUpperCase();
            switch (option) {
                case "P":
                    System.out.println(currentForest);
                    break;
                case "A":
                    addTree();
                    break;
                case "C":
                    cutTree();
                    break;
                case "G":
                    currentForest.grow();
                    break;
                case "R":
                    reapTrees();
                    break;
                case "S":
                    saveForest();
                    break;
                case "L":
                    loadForest();
                    break;
                case "N":
                    nextForest(args);
                    break;
                case "X":
                    System.out.println("Exiting the Forestry Simulation");
                    break;
                default:
                    System.out.println("Invalid menu option, try again");
                    break;
            }
        } while (!"X".equals(option));
    } // End of main



    /**
     * Loads forests from specified CSV files.
     * @param forestNames An array of names of forests to be loaded, assuming files are named as {forestName}.csv
     */
    private static void loadForests(String[] forestNames) {
        boolean montaneInitialized = false;
        for (String name : forestNames) {
            try {
                Scanner fileScanner = new Scanner(new File(name + ".csv"));
                Forest forest = new Forest(name);
                while (fileScanner.hasNextLine()) {
                    String[] data = fileScanner.nextLine().split(",");
                    Tree tree = new Tree(data[0], Integer.parseInt(data[1].trim()),
                            Double.parseDouble(data[2].trim()), Double.parseDouble(data[3].trim()));
                    forest.addTree(tree);
                }
                forests.put(name, forest);
                if (name.equals("Montane") && !montaneInitialized) {
                    currentForest = forest; // Set Montane as the current forest only if it hasn't been initialized yet
                    System.out.println("Initializing from " + name);
                    montaneInitialized = true;
                }
            } catch (FileNotFoundException e) {
                System.out.println("Error opening/reading " + name + ".csv");
            }
        }
        if (!montaneInitialized) {
            System.out.println("Montane forest not found among provided names.");
        }
    }


    /**
     * Adds a random new tree to the current forest based on predefined species, height, and growth rate.
     */
    private static void addTree() {
        TreeSpecies species = TreeSpecies.values()[new Random().nextInt(TreeSpecies.values().length)];
        int year = Calendar.getInstance().get(Calendar.YEAR);
        double height = MIN_HEIGHT_TO_PLANT + new Random().nextDouble() * MIN_HEIGHT_TO_PLANT;
        double growthRate = MIN_GROWTH_RATE + new Random().nextDouble() * MIN_GROWTH_RATE;
        Tree tree = new Tree(species.toString(), year, height, growthRate);
        currentForest.addTree(tree);
    }

    /**
     * Prompts the user to specify a tree number to cut down from the current forest.
     */
    private static void cutTree() {
        while (true) {
            System.out.print("Tree number to cut down: ");
            try {
                int index = Integer.parseInt(keyboard.nextLine().trim());
                if (index < 0 || index >= currentForest.trees.size()) {
                    System.out.println("Tree number " + index + " does not exist");
                } else {
                    currentForest.cutDownTree(index);
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("That is not an integer");
            }
        }
    }

    /**
     * Prompts the user to specify a height from which trees should be reaped in the current forest.
     */
    private static void reapTrees() {
        while (true) {
            System.out.print("Height to reap from: ");
            try {
                double height = Double.parseDouble(keyboard.nextLine().trim());
                currentForest.reap(height);
                break;
            } catch (NumberFormatException e) {
                System.out.println("That is not a integer");
            }
        }
    }

    /**
     * Saves the current forest to a file.
     */
    private static void saveForest() {
        try {
            currentForest.saveToFile(currentForest.name + ".db");
            System.out.println("Forest saved as " + currentForest.name + ".db");
        } catch (IOException e) {
            System.out.println("Error saving forest to file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Loads a forest from a specified file, replacing the current forest.
     */
    private static void loadForest() {
        System.out.print("Enter forest name: ");
        String name = keyboard.nextLine().trim();
        try {
            currentForest = Forest.loadFromFile(name + ".db");
            System.out.println("Forest loaded: " + name);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading forest from file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Moves to the next forest in the array if available.
     * @param forestNames An array of forest names used to determine the sequence of forests.
     */
    private static void nextForest(String[] forestNames) {
        boolean found = false;
        for (int i = 0; i < forestNames.length; i++) {
            if (forestNames[i].equals(currentForest.name) && i + 1 < forestNames.length) {
                currentForest = forests.get(forestNames[i + 1]);
                System.out.println("Moving to the next forest");
                System.out.println("Initializing from " + currentForest.name);
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("No more forests to move to.");
        }
    }
} // End of ForestSimulation class
