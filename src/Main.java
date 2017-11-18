import sample.TCPServer;

import java.net.*;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by nhunimuni on 10.11.17.
 */
public class Main {


    public static void main(String args[]) throws SocketException {

        UDPServer udpServer = new UDPServer();
        udpServer.start();

        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();

        for (NetworkInterface netint : Collections.list(nets)) {
            displayInterfaceInformation(netint);
        }
    }

    static void displayInterfaceInformation(NetworkInterface netint) throws SocketException {

        Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
        for (InetAddress inetAddress : Collections.list(inetAddresses)) {

            System.out.printf("\n" + "Display name: %s\n", netint.getDisplayName());
            System.out.printf("Name: %s\n", netint.getName());
            System.out.printf("InetAddress: %s\n", inetAddress);

            if (inetAddress instanceof Inet4Address) {

                List<InterfaceAddress> list = netint.getInterfaceAddresses();
                for (InterfaceAddress i : list) {
                    if (i.getBroadcast() != null)
                        System.out.println("Broadcast IP: " + i.getBroadcast());
                }

                String sub = inetAddress.toString().substring(1);
                String[] liste = sub.split("[.]");

                StringBuffer wort = new StringBuffer();

                for (String s : liste) {
                    wort.append(Integer.toBinaryString(Integer.parseInt(s)) + ".");
                }

                String w = wort.substring(0, wort.length() - 1);

                System.out.print("Binärformat: " + w);
                System.out.print("\n" + "IPv4 ");
            }
            if (inetAddress instanceof Inet6Address) System.out.print("IPv6 " + "\n");
        }
        System.out.printf("\n");
    }

}


