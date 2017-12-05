package pinnwand.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;

public class Server {
	
	public final static String NAME_OF_SERVICE = "Pinnwand";
	
	public static void main(String[] args) {
		
		try
		{
		  LocateRegistry.createRegistry( Registry.REGISTRY_PORT );
		  Pinnwand pinnwand = new Pinnwand();
		  
		  Registry registry = LocateRegistry.getRegistry();
		  registry.rebind( NAME_OF_SERVICE, pinnwand );
		 
		}
		catch ( RemoteException e )  {
			System.err.println("Wups, was schief gelaufen...");
		} 
		System.out.println("Pinnwand-Service is running.");
		
	}

}
