import java.io.File;
import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HyperloopTrainNetwork implements Serializable {
    static final long serialVersionUID = 11L;
    public double averageTrainSpeed;
    public final double averageWalkingSpeed = 1000 / 6.0;
    public int numTrainLines;
    public Station startPoint;
    public Station destinationPoint;
    public List<TrainLine> lines;

    /**
     * Method with a Regular Expression to extract integer numbers from the fileContent
     * @return the result as int
     */
    public int getIntVar(String varName, String fileContent) {
        Pattern p = Pattern.compile("[\\t ]*" + varName + "[\\t ]*=[\\t ]*([0-9]+)");
        Matcher m = p.matcher(fileContent);

        m.find();

        return Integer.parseInt(m.group(1));
    }

    /**
     * Write the necessary Regular Expression to extract string constants from the fileContent
     * @return the result as String
     */
    public String getStringVar(String varName, String fileContent) {
        Pattern p = Pattern.compile("[\\t ]*" + varName + "[\\t ]*=[\\t ]*\"(.*?)\"");
        Matcher m = p.matcher(fileContent);

        m.find();

        return m.group(1);
    }

    /**
     * Write the necessary Regular Expression to extract floating point numbers from the fileContent
     * Your regular expression should support floating point numbers with an arbitrary number of
     * decimals or without any (e.g. 5, 5.2, 5.02, 5.0002, etc.).
     * @return the result as Double
     */
    public Double getDoubleVar(String varName, String fileContent) {
        Pattern p = Pattern.compile("[\\t ]*" + varName + "[\\t ]*=[\\t ]*([0-9]*\\.?[0-9]+)");
        Matcher m = p.matcher(fileContent);

        m.find();

        return Double.parseDouble(m.group(1));
    }

    /**
     * Write the necessary Regular Expression to extract a Point object from the fileContent
     * points are given as an x and y coordinate pair surrounded by parentheses and separated by a comma
     * @return the result as a Point object
     */
    public Point getPointVar(String varName, String fileContent) {
        Pattern p = Pattern.compile(
                "[\\t ]*" + varName + "[\\t ]*=[\\t ]*\\([\\t ]*([0-9]+)[\\t ]*,[\\t ]*([0-9]+)[\\t ]*\\)"
        );
        Matcher m = p.matcher(fileContent);

        m.find();

        return new Point(
                Integer.parseInt(m.group(1)),
                Integer.parseInt(m.group(2))
        );
    } 

    /**
     * Function to extract the train lines from the fileContent by reading train line names and their 
     * respective stations.
     * @return List of TrainLine instances
     */
    public List<TrainLine> getTrainLines(Scanner scanner) {
        List<TrainLine> trainLines = new ArrayList<>();

        for (int i = 0; i < numTrainLines; i++) {
            String name = getStringVar("train_line_name", scanner.nextLine());
            List<Station> stations = new ArrayList<>();
            Pattern p = Pattern.compile("\\([\\t ]*([0-9]+)[\\t ]*,[\\t ]*([0-9]+)[\\t ]*\\)");
            Matcher m = p.matcher(scanner.nextLine());

            for (int j = 1; m.find() ; j++) {

                stations.add(new Station(
                        new Point(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2))),
                        name + " Line Station " + j
                ));
            }

            trainLines.add(new TrainLine(
                    name,
                    stations
            ));
        }

        return trainLines;
    }

    /**
     * Function to populate the given instance variables of this class by calling the functions above.
     */
    public void readInput(String filename) {
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);

            numTrainLines = getIntVar("num_train_lines", scanner.nextLine());
            startPoint = new Station(
                    getPointVar("starting_point", scanner.nextLine()),
                    "Starting Point"
            );
            destinationPoint = new Station(
              getPointVar("destination_point", scanner.nextLine()),
              "Final Destination"
            );
            averageTrainSpeed = getDoubleVar("average_train_speed", scanner.nextLine()) / 0.06;
            lines = getTrainLines(scanner);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
