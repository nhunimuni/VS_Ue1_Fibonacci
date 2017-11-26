package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.*;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Button sendenB;

    @FXML
    private Label answerL;

    @FXML
    private TextField textF;

    private String zahl;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sendenB.setOnAction(event -> click());
        test();
    }

    /*
    * Hier wird die Nachricht des BroadcastServers empfangen
    */
    private void test() {
        // Repräsentiert Socket zum versenden und erhalten von Datagram Paketen
        // verbindungslos, Datagramme, beliebige Reihenfolge (ausgehend und herauskommend)
        DatagramSocket multicastSocket = null;
        try {
            // Der MulticastSocket ist ein UDP DatagramSocket, mit dem Multicast-Pakete empfangen und verschickt werden können
            // Diese wird durch eine Port Nummer angebeben
            multicastSocket = new MulticastSocket(9876);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Wenn wir Daten empfangen, müssen wir nur ein DatagramPacket-Objekt anlegen und den Speicherplatz angeben, an dem die Daten abgelegt werden sollen.
        // Das Feld ist so etwas wie ein Container /Platzhalter.
        // Die folgenden Zeilen reichen für einen Server, der am Port 9876 horcht
        byte[] buf = new byte[1000];
        DatagramPacket recv = new DatagramPacket(buf, buf.length); //das DatagramPacket mit dem Array
        try {
            // Mit der Methodes des DatagramSocket "receive" erhalten wir das Paket.
            // Die Daten werden im DatagramPacket recv abgelegt
            multicastSocket.receive(recv);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Hier erhalten wir die Daten als Byte-Feld und wandeln diese in einen String um
        // Die erhaltene Nachricht wird auf der Konsole ausgegeben
        System.out.println(new String(recv.getData()));
    }

    private void click() {
        // Unsere Zahl, die wir berechnen möchten
        zahl = textF.getText();

        DatagramSocket clientSocket = null;
        try {
            // Erzeugung eines DatagramSockets
            clientSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        InetAddress IPAddress = null;
        try {
            // Empfänger auslesen
            IPAddress = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        // Platzahlter
        byte[] sendData;
        byte[] receiveData = new byte[1024];

        String sentence = zahl;
        //Konvertierung des Strings in Byte
        sendData = sentence.getBytes();

        // Erzeugung eines DatagramPacket-Objekts mit einem Byte-Feld für den Empfänger
        // Zusätzlich zum Byte-Feld "sendData" geben wir die Anzahl der Bytes an, die gesendet werden sollen
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 2500);
        try {
            // Mit der DatagramSocket-Methode "send" wird das Datagramm an die im DatargramPacket erhaltene Port-Nr. und -Adresse versendet
            clientSocket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Unsere Lösung, die wir empfangen
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        try {
            clientSocket.receive(receivePacket);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Die erhaltene Nachricht wird auf der Konsole ausgegeben
        String modifiedSentence = new String(receivePacket.getData());
        System.out.println("FROM SERVER : " + modifiedSentence);

        answerL.setText(modifiedSentence);
        clientSocket.close();

    }
}
