import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.net.SocketException;
import java.util.Scanner;


public class SubnetCalculator {

    public static void main(String[] args) throws UnknownHostException, SocketException, IOException{


        Scanner scanner = new Scanner(System.in);

        //String info = "";
        System.out.println("Enter ip address: (a.b.c.d/mask)");
        String address = scanner.nextLine();
        IpAddress ipAddress = new IpAddress(address);

        StringBuilder info = new StringBuilder();

        info.append("Ip address: " + ipAddress.getIpAddress() + "\n"
        + "Binary Ip address: " + ipAddress.getBinaryIpAddress() + "\n"
        + "Decimal mask: " + ipAddress.getMaskDecimal() + "\n"
        + "Binary mask: " + ipAddress.getMaskBinary() + "\n"

        + "Network address: " + ipAddress.getNetworkAddress() + "\n"
        + "Binary network address: " + ipAddress.getBinaryNetworkAddress() + "\n"

        + "Class: " + ipAddress.checkAddressClass() + "\n"
        + ipAddress.checkPrivacy()  + "\n"

        + "Broadcast address: " + ipAddress.getBroadcastAddress() + "\n"
        + "Binary broadcast address: " + ipAddress.getBinaryBroadcastAddress() + "\n"

        + "First host address: " + ipAddress.minHost()  + "\n"
        + "Binary first host address: " + ipAddress.binaryMinHost() + "\n"

        + "Last host address: " + ipAddress.maxHost() + "\n"
        + "Binary last host address: " + ipAddress.binaryMaxHost() + "\n"

        + "Maximal host amount: " + ipAddress.maxHostAmount() + "\n");

        System.out.println(info.toString());

        if(ipAddress.isHostAddress()) {
            boolean loop = true;
            System.out.print("Do you want to send ping request? (Y/N): ");
            while (loop) {
                String answer = scanner.next().toUpperCase();
                switch (answer) {
                    case "Y": {
                        try {
                            String output = PingRequest.sendPingRequest(ipAddress.getIpAddress());
                            System.out.println(output);
                            info.append(output);
                            System.exit(0);
                        } catch (IOException e) {
                            System.err.println(e.getMessage());
                        }
                        break;
                    }
                    case "N": {
                        loop = false;
                        System.exit(0);
                        break;
                    }
                    default: {
                        System.out.println("Wrong character");
                    }
                }
            }
        }

        try {
            SaveToFile.save("Files/IpAddressInfo.txt", info.toString());
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }
}

