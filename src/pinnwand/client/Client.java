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
	private static final String COMMANDS = "List of possible commands: putMessage, getMessages, getSingleMessage, getMessageCount, help, quit ";

	/*Zeile wird eingelesen und mit aufgelisteten Befehlsmöglichkeiten verglichen
	- bei Übereinstimmung wird entsprechende Methode aufgerufen
	- bei keinem Treffen wird eine Konsolenausgabe getägigt, die User auffordert erneuten Befehl einzutippen
	*/
	public static void main(String[] args) {
		System.out.println("------------ Welcome to our bulletin board service ------------");
		System.out.println("Type in 'help' to get a list of all possible commands.");
		System.out.println("Please type in a command.");
		System.out.println();

		Registry registry;
		try {
			registry = LocateRegistry.getRegistry(); //holt alle Register
			IPinnwand pinn = (IPinnwand) registry.lookup( "Pinnwand" ); //sucht Register mit Namen "Pinnwand" aus

			while(!stopped){
				BufferedReader in = new BufferedReader(new InputStreamReader(System.in)); //BufferedReader Objekt zum Verarbeiten der Konsoleneingaben
				String input = in.readLine().trim(); //Einlesen der Usereingabe

				if (input.equals("quit")){
					stopped = true; //Befehl zum Verlassen des Programms
				}
				else if (input.equals("putMessage")){
					System.out.println("Please type in your message: ");
					String msg = in.readLine();
					if(pinn.putMessage(msg)){ //falls Hinzufügen erfolgreich
						System.out.println("Message was successfully added.");
					}else{ //Andernfalls
						System.out.println("The bulletin board is full.");
					}
				}else if (input.equals("getMessages")){
					String [] msgs = pinn.getMessages(); //String Array mit allen gespeicherten Nachrichten
					if (msgs != null){
						int i = 0;
						for(String msg : msgs){ //For-each zur Ausgabe der Einträge
							if(msg == null) System.out.println();
							System.out.println(i + ": " + msg);
							i++;
						}
					}else{
						System.out.println("No messages are currently stored."); //Wenn Array leer ist
					}
				}else if (input.equals("getSingleMessage")){
					System.out.println("Please type in the index of the message you are looking for: ");
					String indexString = in.readLine();
					int index = Integer.parseInt(indexString);
					try{
						if(pinn.getMessage(index) != null) { //Sucht aus StringArray Eintrag mit gesuchten Index aus
							System.out.println("Message under index number: " + index);
							System.out.println(pinn.getMessage(index));
						}
					}catch (IndexOutOfBoundsException e){ //Wenn unter Idex nichts gespeichert ist
						System.out.println("A message under the following index number does not exist: " + index);
					}
				}else if (input.equals("getMessageCount")){
					System.out.println("Number of currenntly stored messages: "+ pinn.getMessageCount());
				}else if (input.equals("help")){
					System.out.println(COMMANDS);
				}else{
					System.out.println("! Command does not exist. Please try again. Type in 'help' to get a list of possible commands.");
				}
			}
			System.out.println("Client has been stopped...");
			System.exit(0);

			//Behandlung möglicher Fehler
		} catch (RemoteException e) {
			System.err.println("Registry error");
			e.printStackTrace();
			System.exit(0);
		} catch (NotBoundException e) {
			System.err.println("Lookup error");
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			System.err.println("Error trying to read in line");
			e.printStackTrace();
		}
	}

}
