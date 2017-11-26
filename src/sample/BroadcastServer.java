package sample;

import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by young on 22.11.2017.
 */
public class BroadcastServer extends Thread {

    private List<InetAddress> broadcastList = new ArrayList<>();

    @Override
    public void run() {

        FibonacciServer fibonacciServer = new FibonacciServer(); //FiboServer wird in einem Thread getstartet
        fibonacciServer.start();

        try {
            Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces(); //Sammlung von NetworkInterfaces

            for (NetworkInterface netint : Collections.list(nets)) { //Durchläuft jedes Interface aus Sammlung
                displayInterfaceInformation(netint); //Methodenaufruf, um Netzwerkinformationen auszulesen
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (true) {
            for (InetAddress i : broadcastList) {
                try {
                    //InetAddress group = InetAddress.getByName("141.64.175.255"); //IP Adresse
                    System.out.println("sent message to: " + i.toString());
                    InetAddress group = InetAddress.getByName(i.toString().substring(1)); //IP Adresse
                    DatagramSocket multicastSocket = new MulticastSocket(9876); //Initialisierung eines DatagramSockets für Transport der Datenpakete

                    String sentence = "Dieser Server wurde von [Nhu Mong Tran, Thao Nguyen Thi, Ka Yan Lam]\n" +
                            "implementiert und stellt die Fibonacci-Funktion als Dienst bereit. Um den\n" +
                            "Dienst zu nutzen, senden Sie eine Nachricht an Port [2500] auf diesem Server. Das Format der\n" +
                            "Nachricht sollte folgendermaßen aussehen [...]";
                    DatagramPacket hi = new DatagramPacket(sentence.getBytes(), sentence.length() + 1, group, 9876); //Datenpaket mit benötigten Informationen für Transport
                    multicastSocket.send(hi); //Senden des Datenpakets


                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(5000); //Thread pausiert für 5000ms=5s nach jedem Nachrichtentransport
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void displayInterfaceInformation(NetworkInterface netint) throws SocketException {

        Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();                 //Sammlung von iNetAdressen
        for (InetAddress inetAddress : Collections.list(inetAddresses)) {                   //for-each Schleife geht alle Einträge durch

            System.out.printf("\n" + "Display name: %s\n", netint.getDisplayName());        //Konsolenausgabe für (Display) Namen und IPAdresse der Netzwerschnittstellen
            System.out.printf("Name: %s\n", netint.getName());
            System.out.printf("InetAddress: %s\n", inetAddress);

            if (inetAddress instanceof Inet4Address) {  //Findet IPAdressen im IPv4 Format
                int praefix = 0;

                List<InterfaceAddress> list = netint.getInterfaceAddresses();
                for (InterfaceAddress i : list) {
                    if (i.getBroadcast() != null) { //Ermittelt, ob Broadcastadresse vorhanden ist
                        System.out.println("Broadcast IP: " + i.getBroadcast()); //Konsolenausgabe für Broadcastadresse
                        praefix = i.getNetworkPrefixLength(); //Ermittelt Praefixlänge (Teil für Netzwerk)
                        broadcastList.add(i.getBroadcast());
                    }
                }

                if (!netint.isLoopback()) {  //Falls kein Loopback
                    String sub = inetAddress.toString().substring(1); //Entfernt "/" am Anfang der IPAdressen
                    String[] liste = sub.split("[.]"); //Teilt IPAdressen String an "." und speichert Zahlen in String-Array

                    StringBuffer wort = new StringBuffer(); //StringBuffer Instanz zum Zusammensetzen der IP Adressen im Binärformat
                    String binar;
                    for (String s : liste) { //Geht Einträge aus Array mit Teilzahlen der IPAdresse durch
                        binar = Integer.toBinaryString(Integer.parseInt(s)); //Berechnung der Binärzahl
                        if (binar.length() < 8) { //Prüft, ob berechnetes Binär 8stellig ist
                            int delta = 8 - binar.length();
                            binar = fillWithZeroes(delta, "0") + Integer.toBinaryString(Integer.parseInt(s)); //füllt fehlende Stellen mit 0en auf
                        }
                        wort.append(binar); //Hängt ermittelte (8stellige) Binärzahlen aneinander
                    }

                    //Fügt nach jeder 8. Stelle ein Punkt hinzu für eine bessere Leserlichkeit
                    StringBuilder str = new StringBuilder(wort);
                    int idx = str.length() - 8;

                    while (idx > 0)
                    {
                        str.insert(idx, ".");
                        idx = idx - 8;
                    }

                    String net = wort.substring(0, praefix); //Leerzeichen trennt für Netzwerk bzw. für Client stehenden Teil
                    String client = wort.substring(praefix, wort.length());

                    System.out.println("Praefix: " + praefix); //Konsolenausgabe
                    System.out.println("Binärformat: " + str + " (vollständig)"); //Konsolenausgabe
                    System.out.println("  (Network)  " + net + " " + client + "   (Host)"); //Konsolenausgabe
                }
                System.out.print("\n" + "IPv4 ");
            }
            if (inetAddress instanceof Inet6Address)
                System.out.print("IPv6 " + "\n"); //Findet IPAdressen im IPv6 Format
        }
        System.out.printf("\n");
    }

    //Rekursive Funktion, ermittelt fehlende Nullen
    private static String fillWithZeroes(int k, String zero) {
        if (k == 0) {
            return "";
        } else {
            return zero = zero + fillWithZeroes(--k, zero);
        }
    }


}
