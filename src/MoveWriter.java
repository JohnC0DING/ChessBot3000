import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MoveWriter {

    public static void writeMove(String fenLine){
        String fileName = "FEN.txt";

        try {
            File file = new File(fileName);
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(fenLine);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
