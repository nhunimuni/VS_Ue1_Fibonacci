package sample;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer extends Thread
{

    private static Socket socket; //Repräsentation einer Netzwerkverbindung zwischen zwei Maschinen

    public void run() {
        try {
            int port = 25000; //TCP-Portnummer
            ServerSocket serverSocket = new ServerSocket(port); //Erzeugung einer Instanz der Klasse ServerSocket auf dem Port, auf dem die Anwendung laufen soll
            System.out.println("Fibo-Server started and listening to the port 25000"); //für uns selber zur Überprüfung auf der Konsole

            //Server läuft weiter, solange die while-Schleife nicht unterbrochen wurde
            while(true) {
                //Nachricht vom Client lesen
                socket = serverSocket.accept(); // Verbindungsanfrage des Clients wird hier entgegen genommen
                InputStream is = socket.getInputStream(); //liefert einen InputStream der mit dem Anschlusstrom des Sockets verbunden ist den socket
                InputStreamReader isr = new InputStreamReader(is); //wird mit Anschlusstrom des Socketsv erbunden und wandelt byte in Zeichen
                BufferedReader br = new BufferedReader(isr); //BufferedReader liest die Zeichen vom input stream und puffert diese
                String number = br.readLine(); //Zeichen werden gelesen
                System.out.println("Message received from client is "+number); //für uns selber zur Überprüfung auf der Konsole

                String returnMessage; //lokale Variable

                try {
                    int numberInIntFormat = Integer.parseInt(number); //Konvertierung von String in int
                    int returnValue = fib(numberInIntFormat); //Fibonacci
                    returnMessage = String.valueOf(returnValue) + "\n"; //Zuweisung des Int-Wertes als String
                } catch(NumberFormatException e) {
                    //Input war kein int-Wert, weshalb diese Nachricht dem Clienten geschickt wird
                    returnMessage = "Please send a proper number\n";
                }

                //Eine Nachricht wird an den Client geschickt
                OutputStream os = socket.getOutputStream(); //verschickt Daten in form von Bytes
                OutputStreamWriter osw = new OutputStreamWriter(os); //wandelt Zeichen in Bytes um
                BufferedWriter bw = new BufferedWriter(osw); //schreibt Zeichen ins OutputStream und buffered die
                bw.write(returnMessage); //Nachricht (als String) wird in den BufferedWriter geschrieben
                bw.flush(); //Nachricht wird an den Server gesendet und geleert
                System.out.println("Message sent to the client is "+returnMessage);
            }
        } catch (Exception e) { e.printStackTrace(); }
        finally{
            try {
                socket.close();
            } catch(Exception e) { e.printStackTrace(); }
        }
    }

    //Fibonacci
    public static int fib(int n) {

        if(n<=0) return 0;
        else if(n==1) return 1;
        else return fib(n-1) + fib(n-2);
    }
}
