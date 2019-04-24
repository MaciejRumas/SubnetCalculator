import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class IpAddress {
    private String ipAddress;
    private int mask;
    private String broadcast;
    private String network;

    public IpAddress(String string) throws UnknownHostException, SocketException {
        if (string.matches("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])(\\/)([1-9]|1[0-9]|2[0-9]|3[0-2])$")) {
            String[] parts;
            parts = string.split("/");

            this.ipAddress = parts[0];
            this.mask = Integer.valueOf(parts[1]);
        } else {
            System.err.println("Invalid argument");
            InetAddress inetAddress = InetAddress.getLocalHost();
            this.ipAddress = inetAddress.getHostAddress();

            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(inetAddress);
            this.mask = networkInterface.getInterfaceAddresses().get(0).getNetworkPrefixLength();
        }
        networkAddress();
        broadcastAddress();
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getBinaryIpAddress() {
        return addressToBinary(ipAddress);
    }

    public int getMask() {
        return mask;
    }

    public String toString() {
        return "Ip address: " + ipAddress + " Mask: " + mask;
    }

    private String maskToBinary(int number) {
        String binaryNumber = "";
        for (int i = 0; i < 32; i++) {
            if (i % 8 == 0 && i != 0) {
                binaryNumber += ".";
            }

            if (i < number) {
                binaryNumber += "1";
            } else {
                binaryNumber += "0";
            }
        }
        return binaryNumber;
    }

    private String maskToDecimal(String number) {
        String[] parts = number.split("\\.");
        String decimalNumber = "";
        for (int i = 0; i <= 3; i++) {
            String temp = parts[i];
            int maskPart = Integer.valueOf(temp);
            decimalNumber += binaryToDecimal(maskPart);
            if (i < 3) {
                decimalNumber += ".";
            }
        }
        return decimalNumber;
    }


    private int binaryToDecimal(int n) {
        int num = n;
        int dec_value = 0;
        int base = 1;
        int temp = num;
        while (temp != 0) {
            int last_digit = temp % 10;
            temp = temp / 10;
            dec_value += last_digit * base;
            base = base * 2;
        }
        return dec_value;
    }

    private void networkAddress() {
        String networkAddress = "";
        String[] ipParts = this.ipAddress.split("\\.");
        String[] maskParts = maskToDecimal(maskToBinary(this.mask)).split("\\.");
        for (int i = 0; i <= 3; i++) {
            int intIp = Integer.valueOf(ipParts[i]);
            int intMask = Integer.valueOf(maskParts[i]);
            networkAddress += intIp & intMask;
            if (i < 3) {
                networkAddress += ".";
            }
        }
        network = networkAddress;
    }

    public String checkAddressClass() {
        String[] parts = this.ipAddress.split("\\.", 2);
        int firstPart = Integer.valueOf(parts[0]);
        if (firstPart >= 1 && firstPart < 128) {
            return "A";
        } else if (firstPart >= 128 && firstPart < 192) {
            return "B";
        } else if (firstPart >= 192 && firstPart < 224) {
            return "C";
        } else if (firstPart >= 224 && firstPart < 240) {
            return "D";
        } else {
            return "E";
        }
    }

    public boolean isPrivate() {
        String[] parts = this.ipAddress.split("\\.", 3);
        int firstPart = Integer.valueOf(parts[0]);
        int secondPart = Integer.valueOf(parts[1]);
        if (firstPart == 10) {
            return true;
        } else if (firstPart == 172 && (secondPart >= 16 && secondPart <= 31)) {
            return true;
        } else if (firstPart == 192 && secondPart == 168) {
            return true;
        } else {
            return false;
        }
    }

    public String checkPrivacy() {
        if (this.isPrivate()) {
            return "Address is private";
        } else {
            return "Address is public";
        }
    }

    private int addressToInt(String addressTo) {
        int addressInt = 0;
        String[] addressParts = addressTo.split("\\.");
        for (int i = 3; i >= 0; i--) {
            long ip = Long.parseLong(addressParts[3 - i]);
            addressInt |= ip << (i * 8);
        }
        return addressInt;
    }

    private void broadcastAddress() {
        broadcast = intToAddress(addressToInt(network) | ~(addressToInt(maskToDecimal(maskToBinary(this.mask)))));
    }

    private String intToAddress(int addressTo) {
        StringBuilder addressIP = new StringBuilder(15);
        for (int i = 0; i < 4; i++) {
            addressIP.insert(0, (addressTo & 0xff));
            if (i < 3) {
                addressIP.insert(0, '.');
            }
            addressTo = addressTo >> 8;
        }
        return addressIP.toString();
    }

    public String minHost() {
        String parts[] = network.split("\\.");
        int part3 = Integer.parseInt(parts[2]);
        int part4 = Integer.parseInt(parts[3]);
        if (part4 == 255) {
            part4 = 0;
            part3 += 1;
        } else {
            part4 += 1;
        }
        return parts[0] + "." + parts[1] + "." + part3 + "." + part4;
    }

    public String maxHost() {
        String parts[] = broadcast.split("\\.");
        int part3 = Integer.parseInt(parts[2]);
        int part4 = Integer.parseInt(parts[3]);

        if (part4 == 0) {
            part4 = 255;
            part3 -= 1;
        } else {
            part4 -= 1;
        }
        return parts[0] + "." + parts[1] + "." + part3 + "." + part4;
    }

    public int maxHostAmount() {
        int maxHostNumber;
        maxHostNumber = (addressToInt(broadcast) ^ 1) - addressToInt(network);
        return maxHostNumber;
    }

    private static String addressToBinary(String number) {
        String[] parts = number.split("\\.");
        String binaryNumber = "";
        for (int i = 0; i <= 3; i++) {
            String temp = parts[i];
            int tempInt = Integer.valueOf(temp);
            String toAdd = Integer.toBinaryString(tempInt);
            int length = toAdd.length();
            if (length <= 8) {
                for (int j = 0; j < 8 - length; j++) {
                    binaryNumber += "0";
                }
                binaryNumber += toAdd;
            }
            if (i < 3) {
                binaryNumber += ".";
            }
        }
        return binaryNumber;
    }

    public boolean isHostAddress() {
        String[] ipParts = ipAddress.split("\\.");
        String[] fiParts = minHost().split("\\.");
        String[] laParts = maxHost().split("\\.");
        if (addressToInt(ipAddress) >= addressToInt(minHost()) && addressToInt(ipAddress) <= addressToInt(maxHost())) {
            return true;
        }
        return false;
    }

    public String binaryMaxHost() {
        return addressToBinary(maxHost());
    }

    public String binaryMinHost() {
        return addressToBinary(minHost());
    }

    public String getMaskDecimal() {
        return maskToDecimal(maskToBinary(mask));
    }

    public String getMaskBinary() {
        return maskToBinary(mask);
    }

    public String getNetworkAddress() {
        return network;
    }

    public String getBinaryNetworkAddress() {
        return addressToBinary(network);
    }

    public String getBroadcastAddress() {
        return broadcast;
    }

    public String getBinaryBroadcastAddress() {
        return addressToBinary(broadcast);
    }

}