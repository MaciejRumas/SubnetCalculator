import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class IpAddress {
    private String ipAddress;
    private int mask;


    public IpAddress(String string) throws UnknownHostException, SocketException {
        if(string.matches("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])(\\/)([1-9]|1[0-9]|2[0-9]|3[0-2])$")) {
            String[] parts;
            parts = string.split("/");

            this.ipAddress = parts[0];
            this.mask = Integer.valueOf(parts[1]);
        }
        else{
            System.err.println("Invalid argument");
            InetAddress inetAddress = InetAddress.getLocalHost();
            this.ipAddress = inetAddress.getHostAddress();

            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(inetAddress);
            this.mask = networkInterface.getInterfaceAddresses().get(0).getNetworkPrefixLength();
        }
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getMask() {
        return mask;
    }

    public String toString(){
        return "Ip address: " + ipAddress + " Mask: " + mask;
    }

    public boolean addressVerification(){
        if(!ipAddress.matches("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])(\\/)([1-9]|1[0-9]|2[0-9]|3[0-2])$")){
            return false;
        }
        return true;
    }
}
