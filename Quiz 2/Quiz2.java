import java.util.*;
import java.io.*;

class MassOptimizer{
    private int[][] dynamicMap;
    private boolean[][] outputMap;
    private final int[] possibleItems;
    private int bestPossibleCase;

    public MassOptimizer(int maxWeight, int[] possibleItems) {
        this.dynamicMap = new int[maxWeight + 1][possibleItems.length];
        this.possibleItems = possibleItems;
        this.bestPossibleCase = 0;
        this.outputMap = new boolean[maxWeight + 1][possibleItems.length + 1];
    }

    //dynamically calculates biggest weight possible at (i, j)
    public int opt(int i, int j) {
        if (j == 0) return 0;
        else if (possibleItems[j] > i) return dynamicMap[i][j-1];
        else return Math.max(dynamicMap[i][j-1], possibleItems[j] + dynamicMap[i - possibleItems[j]][j - 1]);
    }

    //L(m, i) function at pdf
    public boolean l(int i, int j) {
        if (i == 0 && j == 0) return true;
        else if (i > 0 && j == 0) return false;
        else if (j > 0 && possibleItems[j-1] > i) return outputMap[i][j-1];
        else return outputMap[i][j - 1] || outputMap[i - possibleItems[j-1]][j-1];
    }

    //calls opt(i, j) for every element in dynamic map and l(i, j) for every element in output map
    public void calculate() {
        for (int i = 1; i < dynamicMap.length; i++) {
            for (int j = 0; j < dynamicMap[0].length; j++) {
                dynamicMap[i][j] = opt(i, j);
            }
        }
        bestPossibleCase = dynamicMap[dynamicMap.length-1][dynamicMap[0].length-1];

        for (int i = 0; i < outputMap.length; i++) {
            for (int j = 0; j < outputMap[0].length; j++) {
                outputMap[i][j] = l(i, j);
            }
        }
    }

    //for printing to STDOUT
    @Override
    public String toString(){

        String output = bestPossibleCase + "\n" ;

        for (boolean[] row : outputMap) {
            for (boolean element : row) {
                output += element ? "1" : "0";
            }
            output += "\n";
        }

        return output;
    }
}

public class Quiz2 {


    public static void main(String[] args) throws IOException {
        try {
            //variable declarations for input file
            int max_weight;
            int[] possibleItems;
            File file = new File(args[0]);
            Scanner reader = new Scanner(file);
            String[] split_line;

            //getting first line of the input
            split_line = reader.nextLine().split("\\s+");
            max_weight = Integer.parseInt(split_line[0]);

            //getting second line of the input
            possibleItems = Arrays.stream(reader.nextLine().split("\\s+")).mapToInt(Integer::parseInt).toArray();

            reader.close();

            //calculating and printing output
            MassOptimizer massOptimizer = new MassOptimizer(max_weight, possibleItems);
            massOptimizer.calculate();
            System.out.println(massOptimizer);
        }
        catch (FileNotFoundException e) {
            System.out.println("Exception occurred!\n");
        }
    }
}
