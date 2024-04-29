import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents a forest which can contain multiple trees.
 * Provides functionality to manage trees such as adding, removing, growing, and saving the forest.
 */

class Forest implements Serializable {
    private static final int MIN_HEIGHT_TO_PLANT = 10;
    private static final int MIN_GROWTH_RATE = 10;
    private static final long serialVersionUID = 1L;
    String name;
    List<Tree> trees;


    /**
     * Constructs a new Forest instance with a specified name.
     * @param name The name of the forest.
     */
    public Forest(String name) {
        this.name = name;
        this.trees = new ArrayList<>();
    }

    /**
     * Provides a string representation of the forest, including all trees.
     * @return A string detailing the forest name, each tree, and the average height of the trees.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Forest name: %s\n", name));
        for (int i = 0; i < trees.size(); i++) {
            Tree tree = trees.get(i);
            String[] parts = tree.toString().split(" ");
            sb.append(String.format("     %d %-6s %4d %6.2f' %5.1f%%\n",
                    i, parts[0], Integer.parseInt(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3])));
        }
        sb.append(String.format("There are %d trees, with an average height of %.2f\n", trees.size(), averageHeight()));

        return sb.toString();
    }

    /**
     * Adds a tree to the forest.
     * @param tree The tree to be added.
     */
    public void addTree(Tree tree) {
        trees.add(tree);
    }

    /**
     * Removes a tree at a specified index from the forest.
     * @param index The index of the tree to remove.
     */
    public void cutDownTree(int index) {
        if (index >= 0 && index < trees.size()) {
            trees.remove(index);
        } else {
            System.out.println("Tree number " + index + " does not exist.");
        }
    }

    /**
     * Prompts all trees in the forest to grow, simulating natural growth over time.
     */
    public void grow() {
        for (Tree tree : trees) {
            tree.grow();
        }
    }

    /**
     * Reaps trees that exceed a specified height, replacing them with new, smaller trees.
     * @param maxHeight The height threshold above which trees will be reaped.
     */
    public void reap(double maxHeight) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < trees.size(); i++) {
            Tree tree = trees.get(i);
            if (tree.height > maxHeight) {
                System.out.printf("Reaping the tall tree  %-6s %4d %6.2f' %5.1f%%\n",
                        tree.species, tree.plantingYear, tree.height, tree.growthRate);

                // Replacing old tree with new one
                TreeSpecies newSpecies = TreeSpecies.values()[random.nextInt(TreeSpecies.values().length)];
                int newYear = Calendar.getInstance().get(Calendar.YEAR);
                double newHeight = MIN_HEIGHT_TO_PLANT + random.nextDouble() * MIN_HEIGHT_TO_PLANT;
                double newGrowthRate = MIN_GROWTH_RATE + random.nextDouble() * MIN_GROWTH_RATE;

                // Replace the old tree with the new tree
                trees.set(i, new Tree(newSpecies.toString(), newYear, newHeight, newGrowthRate));
                System.out.printf("Replaced with new tree %-6s %4d %6.2f' %5.1f%%\n",
                        newSpecies, newYear, newHeight, newGrowthRate);
            }
        }
    }

     /**
     * Saves the forest to a file.
     * @param fileName The name of the file to save the forest to.
     * @throws IOException If there is a problem writing to the file.
     */
    public void saveToFile(String fileName) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(this);
        } catch (IOException e) {
            throw new IOException("Failed to save forest: " + e.getMessage(), e);
        }
    }

    /**
     * Loads a forest from a file.
     * @param fileName The name of the file to load the forest from.
     * @return The loaded forest.
     * @throws IOException If there is a problem reading the file.
     * @throws ClassNotFoundException If the class of a serialized object cannot be found.
     */
    public static Forest loadFromFile(String fileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            return (Forest) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new IOException("Failed to load forest: " + e.getMessage(), e);
        }
    }

    /**
     * Calculates the average height of the trees in the forest.
     * @return The average height of the trees.
     */
    public double averageHeight() {
        if (trees.isEmpty()) {
            return 0; // Return 0 if there are no trees
        }

        double totalHeight = 0;
        for (Tree tree : trees) {
            totalHeight += tree.height;
        }

        return totalHeight / trees.size();
    }

} // End of Forest class