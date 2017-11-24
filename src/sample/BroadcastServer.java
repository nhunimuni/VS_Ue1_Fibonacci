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

        FibonacciServer fibonacciServer = new FibonacciServer();
        fibonacciServer.start();

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
                DatagramSocket multicastSocket = new MulticastSocket(8765);

                String sentence = "Dieser Server wurde von [Nhu Tran, Thao Nguyen Thi, Ka Yan Lam]\n" +
                        "implementiert und stellt die Fibonacci-Funktion als Dienst bereit. Um den\n" +
                        "Dienst zu nutzen, senden Sie eine Nachricht an Port [2500] auf diesem Server. Das Format der\n" +
                        "Nachricht sollte folgendermaßen aussehen [...]";
                DatagramPacket hi = new DatagramPacket(sentence.getBytes(), sentence.length(),
                        group, 8765);
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
                short praefix = 0;
                InetAddress bc = null;
                for (InterfaceAddress i : list) {
                    if (i.getBroadcast() != null) {


                        praefix = i.getNetworkPrefixLength();
                        bc = i.getBroadcast();

                        System.out.println("Broadcast IP: " + i.getBroadcast() + "/" + praefix);
                    }
                }

                if (!netint.isLoopback()) {
                String sub = bc.toString().substring(1); //entfernt den / aus der BroadcastIP im Index 0
                String[] liste = sub.split("[.]");

                StringBuffer wort = new StringBuffer(); //unsere Zahlenfolge ohne Punkt

                for (String s : liste) {
                    wort.append(Integer.toBinaryString(Integer.parseInt(s)));
                }
                    int trenner = 32 - praefix;
                    String erg = wort.substring(0, wort.length() - trenner) + " " + wort.substring(wort.length() - trenner, wort.length());
                    System.out.print("Binärformat: " + erg);
                }
                System.out.print("\n" + "IPv4 ");
            }

            if (inetAddress instanceof Inet6Address) System.out.print("IPv6 " + "\n");
        }
        System.out.printf("\n");
    }

}
