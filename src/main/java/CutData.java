import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

// Class which can cut the initial data.csv into small data_small.csv
public class CutData {

    public static void main(String[] args) throws IOException {
        String projectRootPath = System.getProperty("user.dir");
        String inputFilePath = projectRootPath + "/data_small.csv";
        String outputFilePath = projectRootPath + "/data_small_200_lines.csv";
        // Number of lines to take
        int chunkSize = 200;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {

            int lineCount = 0;
            String line;

            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();

                lineCount++;

                if (lineCount >= chunkSize) {
                    break;
                }
            }
        }

        System.out.println("Data chunk written to data_small_200_lines.csv");
    }
}