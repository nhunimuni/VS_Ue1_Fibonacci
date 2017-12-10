package pinnwand.server;

import pinnwand.interfaces.IPinnwand;

import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;


public class Pinnwand extends UnicastRemoteObject implements IPinnwand {
	
	private static final long serialVersionUID = 1L;
	private ArrayList<String>messages = new ArrayList<String>();

	protected Pinnwand() throws RemoteException {
		super();
	}

	//Rückgabe Anzahl der Nachrichten
	public int getMessageCount() throws RemoteException {
		return messages.size();
	}

	//Rückgabe aller Nachrichten
	public String[] getMessages() throws RemoteException {
		return messages.toArray(new String[messages.size()]);
	}

	//Rückgabe einer bestimmten Nachricht nach dem Index
	public String getMessage(int index) throws RemoteException {
		return messages.get(index) ;
	}
	
	//Nachricht hinzufügen
	public boolean putMessage(String msg) throws RemoteException {
		// Darf die Max. Anzahl "MAX_NUM_MESSAGES" und Länge "MAX_LENGTH_MESSAGE "nicht überschreiten
		if(messages.size() < MAX_NUM_MESSAGES && msg.length() <= MAX_LENGTH_MESSAGE){
			messages.add(msg);
			Timer timer = new Timer();
			final String destroyMsg = msg;
			timer.schedule(new DestroyTimer (destroyMsg){
				@Override
				public void run() {
					messages.remove(destroyMsg);
				}
			}, MESSAGE_LIFETIME*1000); //Max. Lebenszeit einer Nachricht
			return true;
		}else{
			return false;
		}
	}
}
