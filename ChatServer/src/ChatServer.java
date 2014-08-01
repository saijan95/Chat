import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

public class ChatServer {
	private final int SERVERPORT = 5000;
	private ServerSocket serverSocket;
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private ObjectInputStream objectIn;
	private ObjectOutputStream objectOut;
	private ChatServerProcesses processes;
	protected boolean on;
	
	public ChatServer() {
		on = true;
		socket = null;
		in = null;
		out = null;
		objectIn = null;
		objectOut = null;
		processes = new ChatServerProcesses();
	}
	
	private void goOnline() {
		serverSocket = null;
		try {
			serverSocket = new ServerSocket(SERVERPORT);
		} catch(IOException e) {
			System.out.println("SERVER ONLINE: FAIL");
			System.exit(-1);
		}
		
		System.out.println("SERVER ONLINE: PASS");
	}
	
	private void recieveMessages() {
		while(on == true) {
			try {
				socket = serverSocket.accept();
				objectOut = new ObjectOutputStream(socket.getOutputStream());
				objectIn = new ObjectInputStream(socket.getInputStream());
			} catch(IOException e) {
				System.out.println("SERVER CONNECTED TO CLIENT: FAIL");
				System.exit(-1);
			}
			
			System.out.println("SERVER CONNECTED TO CLIENT: PASS\n");
	
			String[] request = null;
			try {
					request = (String[])objectIn.readObject();
					
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(request[0].equals("LOGIN")) {
				try {
					objectOut.writeObject(processes.login(request[1], request[2], request[3], request[4]));
					objectOut.flush();
				} catch (IOException e) {
					System.out.println("SERVER: Cannot communicate with client.");
				}
				
				processes.updateStatus(request[1], "ONLINE");
				
			}
			else if(request[0].equals("CREATE ACCOUNT")) {
				try {
					objectOut.writeObject(processes.createAccount(request[1], request[2], request[3]));
					objectOut.flush();
				} catch (IOException e) {
					System.out.println("SERVER: Cannot communicate with client.");
				}
			}
			else if(request[0].equals("CHAT REQUEST")) {
				try {
					objectOut.writeObject(processes.connect(request[1]));
					objectOut.flush();
				} catch (IOException e) {
					System.out.println("SERVER: Cannot communicate with client.");
				}
			}
			else if(request[0].equals("SIGN OUT")) {
				processes.setOffline(request[1]);
				processes.updateStatus(request[1], "OFFLINE");
			}
			else if(request[0].equals("ADD CONTACT")) {
				processes.addContact(request[1], request[2]);
				
			}
			else if(request[0].equals("FRIEND REQUEST ANSWER")) {
				processes.answerFriendRequest(request[1], request[2], request[3]);
				processes.answerFriendRequest(request[2], request[1], request[3]);
				
				if(request[3].equals("FRIEND")) {
					try {
						objectOut.writeObject(processes.getContacts(request[1]));
						objectOut.flush();
					} catch (IOException e) {
						System.out.println("SERVER: Cannot communicate with client.");
					}
				}
			}
			
			try {
				socket.close();
			} catch (IOException e) {
				System.out.println("SERVER: Cannot disconnect from client");
			}
		}	
	}
	
	public String getIP() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			System.out.println("SERVER: Cannot retrive IP address");
		}
		return null;
	}
	
	public int getPort() { return serverSocket.getLocalPort(); }
	
	public static void main(String[] args) {
		ChatServer chatServer = new ChatServer();
		chatServer.goOnline();
		
		System.out.println(chatServer.getIP());
		System.out.println(chatServer.getPort());
		chatServer.recieveMessages();
		
		
	}
}
