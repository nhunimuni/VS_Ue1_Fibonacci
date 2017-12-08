package pinnwand.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;

public class Server {

    //Namensdienst muss gleich sein auf Server und Client
    public final static String NAME_OF_SERVICE = "Pinnwand";

    public static void main(String[] args) {

        try {
            //erstellt für die Pinnwand Anwendung diesen Port (vom localhost)
            LocateRegistry.createRegistry(1099); //geht nur mit 1099
            Pinnwand pinnwand = new Pinnwand(); //neue Pinnwand

            Registry registry = LocateRegistry.getRegistry(); //speichert die Verbindung lokal ab
            registry.rebind(NAME_OF_SERVICE, pinnwand); //bindet die Pinnwand an diesen Namensdienst

            System.out.println("Pinnwand läuft jetzt auf dem Port '1099' unter dem Namensdienst '" + NAME_OF_SERVICE + "'");
        } catch (RemoteException e) {
            //Exception wird geworfen, wenn Registry nicht erstellt werden konnte
            //Port ist besetzt oder falsche Portnummer angegeben
            System.err.println("Registry konnte nicht erstellt werden.");
        }
    }

}
