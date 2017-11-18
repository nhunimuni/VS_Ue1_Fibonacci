import sample.TCPServer;

import java.io.IOException;
import java.net.*;
import java.util.Timer;
import java.util.TimerTask;

class UDPServer extends Thread {

    public void run() {

        TCPServer tcpServer = new TCPServer();
        tcpServer.start();

        Timer timer = new Timer();

        DatagramSocket serverSocket = null;
        try {
            serverSocket = new DatagramSocket(9876);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        byte[] receiveData = new byte[1024];
        byte[] sendData;
        while (true) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            try {
                serverSocket.receive(receivePacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String sentence = "Dieser Server wurde von [Nhu Tran, Thao Nguyen Thi, Ka Yan Lam]\n" +
                    "implementiert und stellt die Fibonacci-Funktion als Dienst bereit. Um den\n" +
                    "Dienst zu nutzen, senden Sie eine Nachricht an Port [Portnummer Ihres\n" +
                    "Fibonacci-Servers aus der ersten Übung] auf diesem Server. Das Format der\n" +
                    "Nachricht sollte folgendermaßen aussehen [Beschreibung Ihres\n" +
                    "Nachrichtenformats aus der ersten Übung].";
            InetAddress IPAddress = null;
            try {
                IPAddress = InetAddress.getByName("192.168.178.255");
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            int port = receivePacket.getPort();
            String capitalizedSentence = sentence.toUpperCase();
            sendData = capitalizedSentence.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);

            DatagramSocket finalServerSocket = serverSocket;
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    try {
                        finalServerSocket.send(sendPacket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };

            timer.schedule(timerTask, 1000, 5000);
        }
    }
}
