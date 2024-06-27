import java.util.*;
import java.io.*;

class Statement implements Comparable<Statement>{

    public int keywordIndex;
    public String[] splitStatement;

    public Statement(int keywordIndex, String[] splitStatement) {
        this.keywordIndex = keywordIndex;
        this.splitStatement = splitStatement;
    }
    @Override
    public int compareTo(Statement s) {

        int keywordResult = splitStatement[keywordIndex].compareTo(s.splitStatement[s.keywordIndex]);

        if(keywordResult != 0) return keywordResult;
        if(Arrays.equals(splitStatement, s.splitStatement)) return keywordIndex - s.keywordIndex;
        return 0;
    }
}

public class Quiz1 {

    public static void main(String[] args) throws IOException {
        try {
            ArrayList<String> keywords = new ArrayList<>();

            File myObj = new File(args[0]);
            Scanner reader = new Scanner(myObj);
            String line;

            while (reader.hasNextLine()) {
                line = reader.nextLine().toLowerCase();

                if (line.equals("...")) break;
                if (line.isEmpty()) continue;

                keywords.add(line);
            }
            
            String[] splitLine;
            ArrayList<Statement> statements = new ArrayList<>();

            while (reader.hasNextLine()) {
                line = reader.nextLine().toLowerCase();
                if (line.isEmpty()) continue;

                splitLine = line.split("\\s+");

                for (int i = 0; i < splitLine.length; i++) {

                    if (!keywords.contains(splitLine[i])) {
                        statements.add(new Statement(i, splitLine));
                    }
                }
            }
            reader.close();

            Collections.sort(statements);

            for (Statement s : statements) {
                for (int i = 0; i < s.splitStatement.length; i++) {
                    System.out.print(
                            (i == s.keywordIndex ? s.splitStatement[i].toUpperCase() : s.splitStatement[i])
                                    +  (i == (s.splitStatement.length - 1) ? "\n" : " "));
                }
            }


        }
        catch (FileNotFoundException e) {
            System.out.println("Give me some real files my guy!!!\n");
        }




        // TODO: Use the first command line argument (args[0]) as the file to read the input from
        // TODO: Your code goes here
        // TODO: Print the solution to STDOUT

    }
}
