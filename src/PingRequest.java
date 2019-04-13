import java.io.IOException;
import java.net.InetAddress;

public class PingRequest{
    public static String sendPingRequest(String address)throws IOException {
        InetAddress inetAddress = InetAddress.getByName(address);

        String pingRequest = "Sending Ping Request to " + address + "\n";
        pingRequest += inetAddress.isReachable(5000) ? "Host is reachable" : "Host is NOT reachable";
        return pingRequest + "\n";
    }
}
