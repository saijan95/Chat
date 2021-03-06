import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;


public class ChatClient {
	private Socket socket;
	private ObjectInputStream objectIn;
	private ObjectOutputStream objectOut;
	private String contactIp;
	private int contactPort;
	
	public ChatClient(String ip, int port) {
		contactIp = ip;
		contactPort = port;
	}
	
	protected void connectToServer() {
		try {
			socket = new Socket(InetAddress.getByName(contactIp), contactPort);
			objectOut = new ObjectOutputStream(socket.getOutputStream());
			objectIn = new ObjectInputStream(socket.getInputStream());
			
		} catch(IOException e) {
			System.out.println("CLIENT CONNECTED TO SERVER: FAIL");
			System.exit(-1);
		}
		
		System.out.println("CLIENT CONNECTED TO SERVER: PASS");
	}
	
	protected void addNewContactToUser(String username, String name, String status) {
		try {
			objectOut.writeObject(new String[]{"ADD CONTACT TO USER", username, name, status});
			objectOut.flush();
		} catch (IOException e) {
			System.out.println("CLIENT: Cannot communicate with server");
		}
	}
	
	protected void updateContactStatus(String username, String name, String status) {
		try {
			objectOut.writeObject(new String[]{"STATUS UPDATE", username, name, status});
			objectOut.flush();
		} catch (IOException e) {
			System.out.println("CLIENT: Cannot communicate with server");
		}
		disconnectFromServer();
	}
	
	protected void sendFriendRequest(String username, String name) {
		try {
			objectOut.writeObject(new String[]{"FRIEND REQUEST", username, name});
			objectOut.flush();
		} catch (IOException e) {
			System.out.println("CLIENT: Cannot communicate with server");
		}
		disconnectFromServer();
	}
	
	private void disconnectFromServer() { 
		try {
			socket.close();
		} catch (IOException e) {
			System.out.println("CLIENT: Cannot disconnect from server.");
		}
	}

	protected int getContactPort() {return contactPort;}
	protected String getContactIp() {return contactIp;}
}
