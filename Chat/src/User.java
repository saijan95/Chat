import java.util.ArrayList;
import java.util.HashMap;

public class User {
	private String username;
	private String name;
	private String status;
	private String ip;
	private int port;
	private ArrayList<Contact> contactsList;
	private ArrayList<Contact> onlineContactsList;
	private ArrayList<Contact> friendRequestsList;
	private HashMap<String, ChatBox> chatting;
	
	public User(String username, String status, String ip, int port) {
		this.username = username;
		this.status = status;
		this.ip = ip;
		this.port = port;
		contactsList = new ArrayList<Contact>();
		onlineContactsList = new ArrayList<Contact>();
		friendRequestsList = new ArrayList<Contact>();
		chatting = new HashMap<String, ChatBox>();
	}
	
	protected void setStatus(String status) {
		this.status = status;
	}
	
	protected void addContactToList(Contact contact) {
		contactsList.add(contact);
		
		if(contact.getStatus().equals("ONLINE"))
			onlineContactsList.add(contact);
	}
	
	protected void addFriendRequest(Contact contact) {
		friendRequestsList.add(contact);
	}
	
	protected void addToChatting(String username, ChatBox chatBox) {
		chatting.put(username, chatBox);
	}
	
	protected void deleteContactsFromList(String username) {
		for(int i = 0; i < contactsList.size(); i++) {
			if(contactsList.get(i).getUsername().equals(username))
				contactsList.remove(i);
		}
	}
	
	protected void deleteOnlineContactsFromList(String username) {
		for(int i = 0; i < onlineContactsList.size(); i++) {
			if(onlineContactsList.get(i).getUsername().equals(username))
				onlineContactsList.remove(i);
		}
	}
	
	protected void removeFromChatting(String username) {
		chatting.remove(username);
	}
	
	protected String getUsername() { return username; }
	protected String getStatus() { return status; }
	protected String getIp() { return ip; }
	protected int getPort() { return port; }
	protected ArrayList<Contact> getContactsList() { return contactsList; }
	protected ArrayList<Contact> getOnlineContactsList() { return onlineContactsList; }
	protected ArrayList<Contact> getFriendRequestsList() { return friendRequestsList; }
	protected HashMap<String, ChatBox> getChatting() { return chatting; }
}
