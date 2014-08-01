import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
	private Socket socket;
	private ObjectInputStream objectIn;
	private ObjectOutputStream objectOut;
	private String contactIp;
	private int contactPort;
	
	public Client(String ip, int port) {
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
	
	protected ArrayList requestLogin(String username, String password, String ip, int port) {
		ArrayList userInformation = null;
		
		try {
			objectOut.writeObject(new String[]{"LOGIN", username, password, ip, ""+port});
			objectOut.flush();
		} catch (IOException e) {
			System.out.println("CLIENT: Cannot communicate with server");
		}
		
		try {
			userInformation = (ArrayList)objectIn.readObject();
		} catch (ClassNotFoundException e1) {
			System.out.println("CLIENT: Cannot communicate with server1");
		} catch (IOException e1) {
			System.out.println("CLIENT: Cannot communicate with server2");
		}
		
		disconnectFromServer();
		
		return userInformation;
	}
	
	protected void requestSignOut(String username) {
		try {
			objectOut.writeObject(new String[]{"SIGN OUT", username});
			objectOut.flush();
		} catch (IOException e) {
			System.out.println("CLIENT: Cannot communicate with server");
		}
	}
	
	protected boolean createNewAccount(String username, String password, String name) {
		boolean successful = false;
		
		try {
			objectOut.writeObject(new String[]{"CREATE ACCOUNT", username, password, name});
			objectOut.flush();
		} catch (IOException e) {
			System.out.println("CLIENT: Cannot communicate with server");
		}
		
		try {
			successful = (boolean)objectIn.readObject();
		} catch (ClassNotFoundException e1) {
			System.out.println("CLIENT: Cannot communicate with server1");
		} catch (IOException e1) {
			System.out.println("CLIENT: Cannot communicate with server2");
		}
		
		disconnectFromServer();
		
		return successful;
	}
	
	protected String[] getContactInformation(String contactUsername) {
		String[] contactNetworkInformation = null;
		try {
			objectOut.writeObject(new String[]{"CHAT REQUEST", contactUsername});
			contactNetworkInformation = (String[])objectIn.readObject();
		} catch (ClassNotFoundException |IOException e) {
			System.out.println("CLIENT: Cannot communicate with server");
		}
		
		disconnectFromServer();
		
		return contactNetworkInformation;
	}
	
	protected void addContact(String username, String contactUsername) {
		try {
			objectOut.writeObject(new String[]{"ADD CONTACT", username, contactUsername});
			objectOut.flush();
		} catch (IOException e) {
			System.out.println("CLIENT: Cannot communicate with server");
		}
		
		disconnectFromServer();
		
	}
	
	protected void answerChatRequest(String username, String contactUsername, String answer) {
		try {
			objectOut.writeObject(new String[]{"ANSWER FR", username, contactUsername, answer});
			objectOut.flush();
		} catch (IOException e) {
			System.out.println("CLIENT: Cannot communicate with server");
		}
		
		disconnectFromServer();
	}
	
	protected void sendChatRequest(String username, String IP, int port, String outgoingMessage) {
		if (outgoingMessage != null) {
			try {
				objectOut.writeObject(new String[]{"CHAT REQUEST", username, IP, ("" + port), outgoingMessage});
				objectOut.flush();
			} catch (IOException e) {
				System.out.println("CLIENT: Cannot communicate with server");
			}
			
			disconnectFromServer();
		}
	}
	
	protected ArrayList<String[]> answerFriendRequest(String username, String contactUsername, String answer) {
		ArrayList<String[]> contactsInformation = null;
		try {
			objectOut.writeObject(new String[]{"FRIEND REQUEST ANSWER", username, contactUsername, answer});
			objectOut.flush();
		} catch (IOException e) {
			System.out.println("CLIENT: Cannot communicate with server");
		}
		
		try {
			contactsInformation = (ArrayList<String[]>)objectIn.readObject();
		} catch (ClassNotFoundException e1) {
			System.out.println("CLIENT: Cannot communicate with server1");
		} catch (IOException e1) {
			System.out.println("CLIENT: Cannot communicate with server2");
		}
		
		disconnectFromServer();
		
		return contactsInformation;
	}
		
	
	protected void sendMessage(String username, String outgoingMessage) {
		if (outgoingMessage != null) {
			try {
				objectOut.writeObject(new String[]{"CHAT", username, outgoingMessage});
				objectOut.flush();
			} catch (IOException e) {
				System.out.println("CLIENT: Cannot communicate with server");
			}
			
			disconnectFromServer();
		}
	}

	private void disconnectFromServer() {
		try {
			socket.close();
		} catch (IOException e) {
			System.out.println("CLIENT: Cannot disconnect from server.");
		}
	}
	
	protected int getContactPort() { return contactPort; }
	protected String getContactIp() { return contactIp; }
}