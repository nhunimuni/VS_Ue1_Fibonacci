package sample;

import java.io.IOException;
import java.net.*;

/**
 * Created by young on 22.11.2017.
 */
public class FibonacciServer extends Thread {

    public FibonacciServer() {
        System.out.println("Fibonacci-Server started...");
    }

    public void run() {

        //stellt eine verbindung auf port 2500 bereit
        DatagramSocket serverSocket = null;
        try {
            serverSocket = new DatagramSocket(2500);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        while (true) {
            byte[] receiveData = new byte[1024];
            byte[] sendData;

            //ankommendes paket
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            try {
                serverSocket.receive(receivePacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String sentence = new String(receivePacket.getData());
            System.out.println("FIBO RECEIVED: " + sentence);

            // schickt die auswertung zurück an den client (von dem er die anfrage bekommen hat)
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();
            String retMessage;
            try {
                //schaut hier nach, ob gültige eingabe
                //wenn ja, schicke auswertung
                //wenn nein, schicke fehlerbenachrichtigung
                int erg = fib(Integer.parseInt(sentence.trim()));
                retMessage = "" + erg;
            } catch (NumberFormatException e) {
                retMessage = "Das ist leider keine gültige Zahl!!!";
            }

            //zerlegt den string in ein byte array und schickt diesen ab
            sendData = retMessage.getBytes();
            DatagramPacket sendPacket =
                    new DatagramPacket(sendData, sendData.length, IPAddress, port);
            try {
                serverSocket.send(sendPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Fibonacci
    public static int fib(int n) {

        if (n <= 0) return 0;
        else if (n == 1) return 1;
        else return fib(n - 1) + fib(n - 2);
    }
}

