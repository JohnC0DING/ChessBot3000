package fenfilemanagement;

import movement.Move;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MoveReader {

    public static String getOpponentMove(boolean isWhite) throws InterruptedException {

        String fileName = "FEN.txt"; // Change this to your file path
        String blackOrWhite = isWhite ? "w" : "b";

        while(true){
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                String fenLine;
                while ((fenLine = br.readLine()) != null) {
                    System.out.println(fenLine); // Print each fenLine
                    if(fenLine.contains(blackOrWhite)){
                        return fenLine;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            Thread.sleep(2000);
        }
    }
}
