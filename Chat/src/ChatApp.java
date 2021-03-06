import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.*;

public class ChatApp extends JFrame implements DialogClientInterface {
	private final String CHATSERVERIP = "192.168.56.1";
	private final int CHATSERVERPORT = 5000;
	private static LoginDialog loginDialog;
	private ContactsListPanel contactsListPanel;
	private AddContactDialog addContactDialog;
	private DeleteContactDialog deleteContactDialog;
	private NewAccountDialog newAccountDialog;
	private User user;
	
	private Server server;
	private Client client;
	private Thread serverThread;
	private JMenu contactsButton;
	private JMenu userButton;
	
	private static boolean login;
	private GridBagLayout layout;
	private GridBagConstraints constraints;
	
	public ChatApp(String title) {
		super(title);
		setSize(300,500);
		
		class MyWindowHandler extends WindowAdapter {
			public void windowClosing(WindowEvent e) {
				signOut();
			}
		}
		
		this.addWindowListener(new MyWindowHandler());
		
		layout = new GridBagLayout();
		setLayout(layout);
		
		constraints = new GridBagConstraints();

		setIconImage(Toolkit.getDefaultToolkit().getImage( System.getProperty("user.dir") + "\\Resources\\ChatIcon.png"));
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		userButton = new JMenu("User");
		userButton.setEnabled(false);
		menuBar.add(userButton);
		
		contactsButton = new JMenu("Contacts");
		contactsButton.setEnabled(false);
		menuBar.add(contactsButton);
		
		JMenuItem signOutButton = new JMenuItem("Sign Out");
		userButton.add(signOutButton);
		
		JMenuItem addContactButton = new JMenuItem("Add Contact");
		contactsButton.add(addContactButton);
		
		JMenuItem deleteContactButton = new JMenuItem("Delete Contact");
		contactsButton.add(deleteContactButton);
		
		signOutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				signOut();
			}
		});
		
		addContactButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showAddContactDialog();
			}
		});
		
		deleteContactButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showDeleteContactDialog();
			}
		});
		
		goOnline();
	}
	
	private void createContactList() {
		contactsListPanel = new ContactsListPanel(user);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.insets = new Insets(5,5,5,5);
		constraints.weightx = 1;
		constraints.weighty = 1; 
		layout.setConstraints(contactsListPanel, constraints);
		getContentPane().add(contactsListPanel);
		
		contactsListPanel.getConnectButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<ContactCard> selectedContactCards = contactsListPanel.getSelectedContactCardsList();
				
				for(int i = 0; i < selectedContactCards.size(); i++) {
					String contactUsername = selectedContactCards.get(i).getContact().getUsername();
					client.connectToServer();
					String[] contactDetails = client.getContactInformation(contactUsername);
					
					if(contactDetails != null) {
						ChatBox chatBox = new ChatBox(user, server, selectedContactCards.get(i).getContact(), contactDetails[0], Integer.parseInt(contactDetails[1]));
						user.addToChatting(contactUsername, chatBox);
						chatBox.setVisible(true);
					}
				}
			}
		});
	}
	
	private void goOnline() {
		server = new Server(this);
		server.goOnline();
		serverThread = new Thread(server);
		serverThread.start();
		client = new Client(CHATSERVERIP, CHATSERVERPORT);
	}
	
	protected void showFriendRequests() {
		ArrayList<Contact> friendRequestsList = user.getFriendRequestsList();
		for(int i = 0; i < friendRequestsList.size(); i++) {
			FriendRequestDialog friendRequestDialog = new FriendRequestDialog(this, "Friend Request", true, 
					friendRequestsList.get(i));
			Thread friendRequestThread = new Thread(friendRequestDialog);
			friendRequestThread.start();
		}
	}
	
	protected void incomingChatRequest(String contactUsername, String contactIP, int contactPort, String incomingMessage) {
		//TODO: Change contactsList to onlineContactsList.
		ArrayList<Contact> onlineContactsList = user.getOnlineContactsList();
		Contact contact = null;
		for(int i = 0; i < onlineContactsList.size(); i++) {
			if(onlineContactsList.get(i).getUsername().equals(contactUsername)) {
				contact = onlineContactsList.get(i);
				break;
			}
		}
		
		ChatBox chatBox = new ChatBox(user, server, contact, contactIP, contactPort);
		user.addToChatting(contactUsername, chatBox);
		chatBox.setVisible(true);
		chatBox.recieveMessage(incomingMessage);
	}
	
	protected void incominChatMessage(String username, String incomingMessage) {
		if(incomingMessage != null)
			user.getChatting().get(username).recieveMessage(incomingMessage);
	}
	
	protected void incomingFriendRequest(Contact contact) {
		user.getFriendRequestsList().add(contact);
		showFriendRequests();
	}

	private void showAddContactDialog() {
		addContactDialog = new AddContactDialog(this, "Add Contact", true);
		addContactDialog.setVisible(true);
	}
	
	private void showDeleteContactDialog() {
		deleteContactDialog = new DeleteContactDialog(this, "Delete Contact", true);
		deleteContactDialog.setVisible(true);
	}
	
	private void showNewAccountDialog() {
		newAccountDialog = new NewAccountDialog(this, "Create New Account", true);
		newAccountDialog.setVisible(true);
	}
	
	protected User getUser() { return user; }
	
	protected void addContactToUser(String username, String name, String status) {
		user.addContactToList(new Contact(username, name, status));
		contactsListPanel.updateList();
	}
	
	protected void updateContactStatus(String contactUsername, String name, String status) {
		ArrayList<Contact> contactsList = user.getContactsList();
		
		for(int i = 0; i < contactsList.size(); i++) {
			if(contactsList.get(i).getUsername().equals(contactUsername)) {
				contactsList.get(i).setName(name);
				
				if(status.equals("ONLINE"))
					contactsList.get(i).setOnline();
				else if(status.equals("OFFLINE"))
					contactsList.get(i).setOffline();
				break;
			}
		}
		
		contactsListPanel.updateList();
		
		
		ArrayList<Contact> onlineContacts = user.getOnlineContactsList();
		
		for(int i = 0; i < onlineContacts.size(); i++) {
			if(onlineContacts.get(i).getUsername().equals(contactUsername)) {
				onlineContacts.get(i).setOffline();
				break;
			}
		}
		
		contactsListPanel.updateList();
		
		this.repaint();
	}
	
	private void signOut() {
		if(login == true) {
			client.connectToServer();
			client.requestSignOut(user.getUsername());
		}
		
		System.exit(0);
	}
	
	protected void answerFriendRequest(FriendRequestDialog frd) {
		client.connectToServer();
		ArrayList<String[]>contactsInformation = client.answerFriendRequest(user.getUsername(), frd.getContact().getUsername(), "FRIEND");
		
		user.getContactsList().clear();
		user.getFriendRequestsList().clear();
		
		if(contactsInformation != null) {
			for(int i = 0; i < contactsInformation.size(); i++) {
				String[] contactInformation = contactsInformation.get(i);
				
				if(contactInformation[2].equals("PENDING"))
					user.getFriendRequestsList().add(new Contact(contactInformation[0], contactInformation[1], contactInformation[2]));
				else
					user.getContactsList().add(new Contact(contactInformation[0], contactInformation[1], contactInformation[2]));
			}
			contactsListPanel.updateList();
		}
	}
	
	@Override
	public void dialogFinished(String dialogType) {
		if(dialogType.startsWith("LoginDialog")) {
			client.connectToServer();
			
			ArrayList userInformation = null;
			userInformation = client.requestLogin(loginDialog.getUsername(), loginDialog.getPassword(), 
					server.getIP(), server.getPort());
			
			if(userInformation != null) {
				user = new User(loginDialog.getUsername(), "ONLINE", server.getIP(), server.getPort());
				ArrayList<String[]> contactList = (ArrayList<String[]>)userInformation.get(1);
				createContactList();
				
				for(int i = 0; i < contactList.size(); i++) {
					String[] contact = contactList.get(i);
					if(contact[2].equals("PENDING"))
						user.addFriendRequest(new Contact(contact[0], contact[1], contact[2]));
					else
						user.addContactToList(new Contact(contact[0], contact[1], contact[2]));
				}
					
				contactsListPanel.updateList();
				loginDialog.dispose();
				userButton.setEnabled(true);
				contactsButton.setEnabled(true);
				login = true;
				
			}
			else {
				JOptionPane.showMessageDialog(this, "Invalid username or password", "Error",
						JOptionPane.ERROR_MESSAGE);
				
				loginDialog.getUsernameTextField().setText(null);
				loginDialog.getPasswordTextField().setText(null);
			}
		}
		else if(dialogType.startsWith("NewAccountDialog")) {
			boolean successful;
			client.connectToServer();
			successful = client.createNewAccount(newAccountDialog.getUsername(), 
					newAccountDialog.getPassword(), newAccountDialog.getNameOfUser());
			
			if(successful == false) {
				JOptionPane.showMessageDialog(this, "The username you have enterd\nis already in use.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
			else {
				newAccountDialog.dispose();
			}
		}
		else if(dialogType.startsWith("AddContactDialog")) {
			client.connectToServer();
			client.addContact(user.getUsername(), addContactDialog.getUsername());
			addContactToUser(addContactDialog.getUsername(), addContactDialog.getUsername(), "OFFLINE");
		}
	}

	@Override
	public void dialogCancelled(String dialogType) {
		if(dialogType.startsWith("LoginDialog")) {
			showNewAccountDialog();
		}
		else if(dialogType.startsWith("NewAccountDialog")) {
		}
		else if(dialogType.startsWith("AddContactDialog")) {}
	}
	
	public static void main(String[] args) {
		try {
            
			UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName());
	    } 
	    catch (UnsupportedLookAndFeelException e) {
	    } catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}
		
		ChatApp app = new ChatApp("Chat");
		loginDialog = new LoginDialog(app, "Login", true);
		loginDialog.setVisible(true);
		app.setVisible(true);
		
		if(login == true)
			app.showFriendRequests();
	}
}
