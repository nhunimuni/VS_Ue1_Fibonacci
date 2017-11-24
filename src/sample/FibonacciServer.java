package sample;

import java.io.IOException;
import java.net.*;

/**
 * Created by young on 22.11.2017.
 */
public class FibonacciServer extends Thread {

    public FibonacciServer() {
        System.out.println("started...");
    }

    public void run() {
        DatagramSocket serverSocket = null;
        try {
            serverSocket = new DatagramSocket(2500);
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
            String sentence = new String( receivePacket.getData());
            System.out.println("FIBO RECEIVED: " + sentence);
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();
            String capitalizedSentence;
            try {
                int erg = fib(Integer.parseInt(sentence.trim()));
                capitalizedSentence = "" + erg;
            } catch (NumberFormatException e) {
                capitalizedSentence = "Das ist leider keine gültige Zahl!!!";
            }
            sendData = capitalizedSentence.getBytes();
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

        if(n<=0) return 0;
        else if(n==1) return 1;
        else return fib(n-1) + fib(n-2);
    }
}

