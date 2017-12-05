package pinnwand.client;

import pinnwand.interfaces.IPinnwand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
	
	private static boolean stopped = false;
	
	public static void main(String[] args) {
		
		Registry registry;
		try {
			registry = LocateRegistry.getRegistry();
			IPinnwand pinn = (IPinnwand) registry.lookup( "Pinnwand" );
			while(!stopped){	
				System.out.println("-------------------------------------------------------------------------");
				System.out.println("Bitte geben Sie Ihren Befehl ein: ");
				BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			    String input = in.readLine();

			    if (input.equals("quit")){
			    	stopped = true;
			    }else if (input.equals("putMsg")){
			    	System.out.println("Bitte geben Sie Ihre Nachricht ein: ");
			    	String msg = in.readLine();
			    	if(pinn.putMessage(msg)){
			    		System.out.println("Message erfolgreich hinzugefuegt.");
			    	}else{
			    		System.out.println("Entweder ist die Pinnwand voll oder Ihre Message zu lang oder Sie sind nicht eingeloggt.");
			    	};
			    }else if (input.equals("getMessages")){
			    	String [] msgs = pinn.getMessages();
			    	if (msgs != null){
			    		for(String msg : msgs){
			    			if(msg == null) System.out.println();
				    		System.out.println(msg);
				    	}	
			    	}else{
			    		System.out.println("Keine Messages vorhanden oder nicht eingeloggt.");
			    	}
			    	
			    }else if (input.equals("login")){
			    	System.out.println("Bitte geben Sie das Passwort f?r den Server ein: ");
			    	String password = in.readLine();
			    	if (pinn.login(password) == 1){
			    		System.out.println("Login erfolgreich.");;
			    	}else{
			    		System.out.println("Login fehlgeschlagen!");
			    	}
			    }else if (input.equals("getSingleMsg")){
			    	System.out.println("Bitte geben Sie den gsuchten Index der Nachricht ein: ");
			    	String indexString = in.readLine();
			    	int index = Integer.parseInt(indexString);
			    	try{
			    		if(pinn.getMessage(index) != null){
			    			System.out.println(pinn.getMessage(index));
			    		}else{
			    			System.out.println("Nicht eingeloggt.");
			    		}
			    		
			    	}catch (IndexOutOfBoundsException e){
			    		System.out.println("Keine Message an dieser Position vorhanden.");
			    	}
			    	
			    }else if (input.equals("getNumberMsg")){
			    	if(pinn.getMessageCount() == -1){
			    		System.out.println("Nicht eingeloggt.");
			    	}else{
			    		System.out.println(pinn.getMessageCount());
			    	}
			    	
			    }else if (input.equals("help")){
			    	System.out.println();
			    	System.out.println("Moegliche Befehle: login, putMsg, getMessages, getSingleMsg, getNumberMsg ");
			    }else{
			    	System.out.println();
			    	System.out.println("Befehl nicht gefunden.");
			    	System.out.println("Moegliche Befehle: login, putMsg, getMessages, getSingleMsg, getNumberMsg ");
			    	System.out.println();
			    }
			}
			System.out.println("Client wird beendet...");
			System.exit(0);
		} catch (RemoteException e) {
			System.err.println("irgendwas bei registry ist schief gelaufen");
			e.printStackTrace();
			System.exit(0);
		} catch (NotBoundException e) {
			System.err.println("Irgendwas beim Lookup schief gelaufen.");
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			System.err.println("Beim Einlesen ist ein Fehler aufgetreten.");
			e.printStackTrace();
		}
	    
	}

}
