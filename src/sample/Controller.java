package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.awt.event.ActionEvent;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.text.ParseException;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Button sendenB;

    @FXML
    private Label answerL;

    @FXML
    private TextField textF;

    private static Socket socket;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sendenB.setOnAction(event -> click());

    }

    private void click() {
        try {
            int zahl = Integer.parseInt(textF.getText());
        } catch (Exception e) {
            answerL.setText("Bitte eine Zahl eingeben");
            e.printStackTrace();
        }

        try
        {
            String host = "141.64.161.77"; //Nhus IP-Adresse (Server IP-Adresse)
            int port = 25000; //Beliebiger freier Port
            InetAddress address = InetAddress.getByName(host); //findet die IP-Adresse des Hosts
            //System.out.println(address.getHostAddress()+" ist die Adresse");
            socket = new Socket(address, port); //Verbindung zwischen 2 Maschinen (Client & Server)

            //Send the message to the server
            OutputStream os = socket.getOutputStream(); //verschickt Daten in form von Bytes
            OutputStreamWriter osw = new OutputStreamWriter(os); //wandelt Zeichen in Bytes um
            BufferedWriter bw = new BufferedWriter(osw); //schreibt Zeichen ins OutputStream und buffered die



            String sendMessage = textF.getText() + "\n"; //liest die Zahl aus dem entsprechenden Textfeld
            bw.write(sendMessage); //Nachricht (als String) wird in den BufferedWriter geschrieben
            bw.flush(); //Nachricht wird an den Server gesendet und geleert
            System.out.println("Message sent to the server : "+sendMessage);

            //Get the return message from the server
            InputStream is = socket.getInputStream(); //liefert einen InputStream der mit dem Anschlusstrom des Sockets verbunden ist den socket
            InputStreamReader isr = new InputStreamReader(is); //wird mit Anschlusstrom des Socketsv erbunden und wandelt byte in Zeichen
            BufferedReader br = new BufferedReader(isr); //BufferedReader liest die Zeichen vom input stream und puffert diese
            String message = br.readLine(); //Zeichen werden gelesen
            System.out.println("Message received from the server : " +message);
            answerL.setText(message); //Antwort vom Server wird in das Label geschrieben
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
        finally
        {
            //Closing the socket
            try
            {
                socket.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

}
