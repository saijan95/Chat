import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server implements Runnable {
	protected boolean on;
	private Socket socket;
	private ServerSocket serverSocket;
	private ObjectInputStream objectIn;
	private ObjectOutputStream objectOut;
	private ChatApp chatApp;
	
	public Server(ChatApp chatApp) {
		on = true;
		socket = null;
		serverSocket = null;
		objectIn = null;
		objectOut = null;
		this.chatApp = chatApp;
	}
	
	protected void goOnline() {
		try {
			serverSocket = new ServerSocket(0);
		} catch(IOException e) {
			System.out.println("SERVER ONLINE: FAIL");
			System.exit(-1);
		}
		
		System.out.println("SERVER ONLINE: PASS");
	}
	
	@Override
	public void run() {
		recieveRequests();
	}
		
	private void recieveRequests() {
		while(on == true) {
			try {
				socket = serverSocket.accept();
				objectIn = new ObjectInputStream(socket.getInputStream());
				objectOut = new ObjectOutputStream(socket.getOutputStream());
			} catch(IOException e) {
				System.out.println("SERVER CONNECTED TO CLIENT: FAIL");
				System.exit(-1);
			}
			
			System.out.println("SERVER CONNECTED TO CLIENT: PASS\n");
			
			Object incomingRequest = null;
			
			try {
				incomingRequest = objectIn.readObject();
			} catch(IOException | ClassNotFoundException e) {
				System.out.println("SERVER: Cannot communicate with client.");
				disconnectFromClient();
			}
			
			disconnectFromClient();
			
			String[] request = (String[])incomingRequest;
			
			if(request[0].equals("CHAT"))
				chatApp.incominChatMessage(request[1], request[2]);
			else if(request[0].equals("CHAT REQUEST"))
				chatApp.incomingChatRequest(request[1], request[2], Integer.parseInt(request[3]), request[4]);
			else if(request[0].equals("STATUS UPDATE"))
				chatApp.updateContactStatus(request[1], request[2], request[3]);
			else if(request[0].equals("FRIEND REQUEST"))
				chatApp.incomingFriendRequest(new Contact(request[1], request[2], "OFFLINE"));
		}	
	}
	
	private void disconnectFromClient() {
		try {
			socket.close();
		} catch (IOException e) {
			System.out.println("SERVER: Cannot disconnected with client.");
		}
	}
	
	protected String getIP() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			System.out.println("Cannot retrive ip address.");
		}
		
		return null;
	}
	
	protected int getPort() {return serverSocket.getLocalPort();}
}