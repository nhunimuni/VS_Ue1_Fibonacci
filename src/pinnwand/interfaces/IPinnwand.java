package pinnwand.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 * 
 * Interface, welches Funktionalität der Pinnwand beschreibt
 *
 */
public interface IPinnwand extends Remote {
	
	public final static int MAX_NUM_MESSAGES = 20;
	public final static int MESSAGE_LIFETIME = 600;  //Lifetime in Seconds
	public final static int MAX_LENGTH_MESSAGE = 160;

	public int getMessageCount()throws RemoteException;
	public String[]getMessages()throws RemoteException;
	public String getMessage(int index)throws RemoteException;
	public boolean putMessage(String msg)throws RemoteException;

}
