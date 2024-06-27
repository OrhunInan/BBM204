import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileReader {

    public static ArrayList<Integer> getElements(String fileName) {
        ArrayList<Integer> data = new ArrayList<>();

        try {
            File file = new File(fileName);
            Scanner reader = new Scanner(file);
            String[] line;

            if(reader.hasNextLine()) reader.nextLine();


            while (reader.hasNextLine()) {
                line = reader.nextLine().split(",");
                data.add( Integer.parseInt(line[6]) );
            }
            reader.close();


        } catch (FileNotFoundException e) {
            System.out.println("Give me some real files my guy!!!\n");
            return null;
        }

        return data;
    }
}
