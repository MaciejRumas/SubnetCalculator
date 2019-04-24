import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PingRequest{
    public static String sendPingRequest(String address)throws IOException {
        String pingRequest = "Sending Ping Request to " + address + "\n";
        String command = "ping " + address;
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec(command);

        InputStream stdout = process.getInputStream ();
        BufferedReader reader = new BufferedReader (new InputStreamReader(stdout));
        String line;
        while ((line = reader.readLine ()) != null) {
            pingRequest += line + "\n";
        }

        return pingRequest + "\n";
    }
}
