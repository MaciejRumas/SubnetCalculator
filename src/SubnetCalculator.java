import java.net.UnknownHostException;
import java.net.SocketException;
import java.util.Scanner;


public class SubnetCalculator {

    public static void main(String[] args) throws UnknownHostException, SocketException {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter ip address: (a.b.c.d/mask)");
        String address = scanner.nextLine();
        IpAddress ipAddress = new IpAddress(address);

        System.out.println("Ip address: " + ipAddress.getIpAddress());
        System.out.println("Binary Ip address: " + addressToBinary(ipAddress.getIpAddress()));

        System.out.println("Decimal mask: " + maskToDecimal(maskToBinary(ipAddress.getMask())));
        System.out.println("Binary mask: " + maskToBinary(ipAddress.getMask()));

        System.out.println("Network address: " + networkAddress(ipAddress.getIpAddress(), ipAddress.getMask()));
        System.out.println("Binary network address: " + addressToBinary(networkAddress(ipAddress.getIpAddress(), ipAddress.getMask())));

        System.out.println("Class: " + checkAddressClass(IpAddress.getIpAddress()));
        if(isPrivate(IpAddress.getIpAddress())){
            System.out.println("Address is private");
        }
        else{
            System.out.println("Address is public");
        }

        //System.out.println("Broadcast address: " + broadcastAddress(networkAddress(ipAddress.getIpAddress(), ipAddress.getMask()),IpAddress.getMask()));
        System.out.println(calculateBroadcastAddress(ipAddress.getIpAddress(),ipAddress.getMask()));

    }

    private static String maskToBinary(int number){
        String binaryNumber = "";
        for(int i=0;i<32;i++){
            if(i%8 == 0 && i != 0){
                binaryNumber += ".";
            }

            if(i < number) {
                binaryNumber += "1";
            }
            else{
                binaryNumber += "0";
            }
        }
        return binaryNumber;
    }

    private static String maskToDecimal(String number){
        String []parts = number.split("\\.");
        String decimalNumber = "";
        for(int i=0;i<=3;i++){
            String temp = parts[i];
            int maskPart = Integer.valueOf(temp);
            decimalNumber += binaryToDecimal(maskPart);
            if(i<3){
                decimalNumber += ".";
            }
        }
        return decimalNumber;
    }

    private static String addressToBinary(String number){
        String []parts = number.split("\\.");
        String binaryNumber = "";
        for(int i=0;i<=3;i++){
            String temp = parts[i];
            int tempInt = Integer.valueOf(temp);
            String toAdd = Integer.toBinaryString(tempInt);
            int length = toAdd.length();
            if(length<=8) {
                for (int j = 0; j < 8 - length; j++) {
                    binaryNumber += "0";
                }
                binaryNumber +=toAdd;
            }
            if(i<3){
                binaryNumber += ".";
            }
        }
        return binaryNumber;
    }

    private static int binaryToDecimal(int n) {
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

    private static String networkAddress(String ipAddress, int mask){
        String networkAddress = "";
        String[] ipParts = ipAddress.split("\\.");
        String[] maskParts = maskToDecimal(maskToBinary(mask)).split("\\.");
        for(int i=0;i<=3;i++){
            int intIp = Integer.valueOf(ipParts[i]);
            int intMask = Integer.valueOf(maskParts[i]);
            networkAddress += intIp & intMask;
            if(i<3){
                networkAddress += ".";
            }
        }
        return networkAddress;
    }

    private static String checkAddressClass(String address){
        String[] parts = address.split("\\.",2);
        int firstPart = Integer.valueOf(parts[0]);
        if(firstPart >= 1 && firstPart < 128){
            return "A";
        }
        else if(firstPart >= 128 && firstPart < 192){
            return "B";
        }
        else if(firstPart >= 192 && firstPart < 224){
            return "C";
        }
        else if(firstPart >= 224 && firstPart < 240){
            return "D";
        }
        else{
            return "E";
        }
    }

    private static boolean isPrivate(String address){
        String[] parts = address.split("\\.",3);
        int firstPart = Integer.valueOf(parts[0]);
        int secondPart = Integer.valueOf(parts[1]);
        if(firstPart == 10){
            return true;
        }
        else if(firstPart == 172 && (secondPart >= 16 && secondPart <=31)){
            return true;
        }
        else if(firstPart == 192 && secondPart == 168){
            return true;
        }
        else{
            return false;
        }
    }

    private static String broadcastAddress(String networkAddress, int mask){
        String broadcastAddress = "";
        String[] addressParts = addressToBinary(networkAddress).split("\\.");
        String[] maskParts = maskToBinary(mask).split("\\.");
        for(int i=0;i<=3;i++){
            int intIp = Integer.valueOf(addressParts[i]);
            int intMask = Integer.valueOf(maskParts[i]);
            System.out.println(intMask);
            System.out.println(~intMask);

            broadcastAddress += binaryToDecimal(intIp | (~intMask));
            if(i<3){
                broadcastAddress += ".";
            }
        }
        return broadcastAddress;
    }

    public static int addressToInt(String addressTo){
        int addressInt = 0;
        String[] addressParts = addressTo.split("\\.");
        for (int i = 3; i >= 0; i--) {
            long ip = Long.parseLong(addressParts[3 - i]);
            addressInt |= ip << (i * 8);
        }
        return addressInt;
    }

    public static String calculateBroadcastAddress(String addressTo, int mask){
       int broadcast = (((addressToInt(addressTo) & addressToInt(maskToDecimal(maskToBinary(mask)))) | ~(addressToInt(maskToDecimal(maskToBinary(mask))))));
        return intToAddress(broadcast);
    }

    public static String intToAddress(int addressTo){
        StringBuilder addressIP = new StringBuilder(15);
        for (int i = 0; i < 4; i++) {
            addressIP.insert(0,(addressTo & 0xff));
            if (i < 3) {
                addressIP.insert(0,'.');
            }
            addressTo = addressTo >> 8;
        }
        return addressIP.toString();
    }

}
