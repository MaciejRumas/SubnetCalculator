import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class SaveToFile {


    public static void save(String fileName, String message) throws IOException {
        FileWriter fileWriter = new FileWriter(fileName);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.println(message);
        printWriter.close();
    }


}
