package sample;

import java.net.*;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by young on 22.11.2017.
 */
public class BroadcastServer extends Thread {

    @Override
    public void run() {

        FibonacchiServer fibonacchiServer = new FibonacchiServer();
        fibonacchiServer.start();

        try {
            Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();

            for (NetworkInterface netint : Collections.list(nets)) {
                displayInterfaceInformation(netint);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                InetAddress group = InetAddress.getByName("141.64.175.255");
                DatagramSocket multicastSocket = new MulticastSocket(9876);

                String sentence = "Dieser Server wurde von [Nhu Tran, Thao Nguyen Thi, Ka Yan Lam]\n" +
                        "implementiert und stellt die Fibonacci-Funktion als Dienst bereit. Um den\n" +
                        "Dienst zu nutzen, senden Sie eine Nachricht an Port [2500] auf diesem Server. Das Format der\n" +
                        "Nachricht sollte folgendermaßen aussehen [...]";
                DatagramPacket hi = new DatagramPacket(sentence.getBytes(), sentence.length(),
                        group, 9876);
                multicastSocket.send(hi);


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
