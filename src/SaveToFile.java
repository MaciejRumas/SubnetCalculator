import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class SaveToFile {


    public static void save(String fileName, String message) throws IOException {
        File file = new File(fileName);
        FileWriter fileWriter = new FileWriter(file);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(message);
        printWriter.close();
    }


}
