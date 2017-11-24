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

    private void test() {
        DatagramSocket multicastSocket = null;
        try {
            multicastSocket = new MulticastSocket(9876);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] buf = new byte[1000];
        DatagramPacket recv = new DatagramPacket(buf, buf.length);
        try {
            multicastSocket.receive(recv);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(new String(recv.getData()));
    }

    private void click() {
        zahl = textF.getText();

        DatagramSocket clientSocket = null;
        try {
            clientSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        InetAddress IPAddress = null;
        try {
            IPAddress = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        byte[] sendData;
        byte[] receiveData = new byte[1024];
        String sentence = zahl;

        sendData = sentence.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 2500);
        try {
            clientSocket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        try {
            clientSocket.receive(receivePacket);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String modifiedSentence = new String(receivePacket.getData());
        System.out.println("FROM SERVER : " + modifiedSentence);
        answerL.setText(modifiedSentence);
        clientSocket.close();

    }
}
