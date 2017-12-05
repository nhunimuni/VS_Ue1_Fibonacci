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


public class Pinnwand extends  UnicastRemoteObject implements IPinnwand {
	

	private static final long serialVersionUID = 1L;
	private ArrayList<String>messages = new ArrayList<String>();
	private ArrayList<String>loggedIps = new ArrayList<String>();
	private Map<String,Timer> loggedIpsTimed = new HashMap<String,Timer>();
	
	protected Pinnwand() throws RemoteException {
		super();
	}

	public int login(String password) throws RemoteException {
		 String clientIp;
		try {
			clientIp = RemoteServer.getClientHost();
			if(password.equals(PASSWORD)){
				final String ip2Destroy = clientIp;
				Timer newTimer = new Timer(ip2Destroy);
				newTimer.schedule(new DestroyTimer (ip2Destroy){
					  @Override
					  public void run() {
						
					    loggedIpsTimed.remove(ip2Destroy);
					  }
					}, 1*60*1000);
				loggedIpsTimed.put(clientIp	, newTimer);
				System.out.println(clientIp + "added to the LoggedIps Pool...");
				return 1; //if loggedIn, value = 1, -1 else
			}else{
				return -1;
			}
		} catch (ServerNotActiveException e) {
			System.err.println("Hier lief was schief mit der Client-IP. Stacktrace: ");
			e.printStackTrace();
		}
		 
		return 0;
	}

	public int getMessageCount() throws RemoteException {
		try {
			String clientIp = RemoteServer.getClientHost();
			if(!this.isLoggedIn(clientIp))return -1;
			
		} catch (ServerNotActiveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return messages.size();
	}

	public String[] getMessages() throws RemoteException {
		try {
			String clientIp = RemoteServer.getClientHost();
			if(!this.isLoggedIn(clientIp))return null;
			
		} catch (ServerNotActiveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return messages.toArray(new String[messages.size()]);
	}

	public String getMessage(int index) throws RemoteException {
		try {
			String clientIp = RemoteServer.getClientHost();
			if(!this.isLoggedIn(clientIp))return null;
			
		} catch (ServerNotActiveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return messages.get(index) ;
	}

	public boolean putMessage(String msg) throws RemoteException {
		try {
			String clientIp = RemoteServer.getClientHost();
			if(!this.isLoggedIn(clientIp))return false;
			
		} catch (ServerNotActiveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		if(messages.size() < 20 && msg.length() < 161){
			messages.add(msg);
			Timer timer = new Timer();
			final String destroyMsg = msg;
			timer.schedule(new DestroyTimer (destroyMsg){
				  @Override
				  public void run() {
					
				    messages.remove(destroyMsg);
				  }
				}, 10*60*1000);
			return true;
		}else{
			return false;
		}
		
	}
	
	private boolean isLoggedIn(String clientIp){
		boolean status = false;
			if(loggedIpsTimed.containsKey(clientIp)){
				System.out.println("�berpr�fe Login von "+ clientIp);
				System.out.println("IP in Hashmap: "+loggedIpsTimed.containsKey(clientIp) );
				System.out.println();
				final String ip2Destroy = clientIp;
				Timer newTimer = new Timer(ip2Destroy);
				newTimer.schedule(new DestroyTimer (ip2Destroy){
					  @Override
					  public void run() {
						
					    loggedIpsTimed.remove(ip2Destroy);
					  }
					}, 10*60*1000);
				loggedIpsTimed.get(ip2Destroy).cancel();
				loggedIpsTimed.remove(ip2Destroy);  //Altes entfernen
				loggedIpsTimed.put(ip2Destroy, newTimer);  //Neuen Mechanismus setzen
				status = true;
				
			}
		
		return status;
	}
	
	


}
