import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class ChatServerProcesses {
	private HashMap<String, String[]> nodesInformation;
	
	public ChatServerProcesses() {
		nodesInformation = new HashMap<String, String[]>();
	}
	
	protected ArrayList login(String username, String password, String ip, String port) {
		ArrayList userInformation = null;
		
		if(isValidAccount(username, password)) {
			userInformation = new ArrayList();
			userInformation.add(getName(username));
			userInformation.add(getContacts(username));
			setOnline(username, new String[]{ip,port});
		}
		
		return userInformation;
	}
	
	private boolean isValidAccount(String username, String password) {
		/** Checks if an account with the given username and password exists in the database. **/
		try {
			BufferedReader in = new BufferedReader(new FileReader(System.getProperty("user.dir") + 
					"\\Account Details\\Logins\\logins.txt"));
			String line;
			while((line = in.readLine()) != null) {
				String[] lineContent = line.trim().split(",");
				if((lineContent[0].equals(username)) && (lineContent[1].equals(password))) {
					in.close();
					return true;
				}
			}
			
			in.close();
			return false;
			
		} catch (FileNotFoundException e) {
			System.out.println("Cannot find login file");
			return false;
		} catch (IOException e) {
			System.out.println("Cannot read from login file");
			return false;
		}
	}
	
	private String getName(String username) {
		String name = null;
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(System.getProperty("user.dir") +
					"\\Account Details\\Users\\names.txt"));
			String line;
			while((line = in.readLine()) != null) {
				String[] lineContent = line.trim().split(",");
				
				if(lineContent[0].equals(username))
					name = lineContent[1];
			}
			
			in.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("Cannot find names file.");
		} catch (IOException e) {
			System.out.println("Cannot read from names file.");
		}
		
		return name;
	}
	
	protected ArrayList<String[]> getContacts(String username) {
		ArrayList<String[]> contacts = null;
		try {
			BufferedReader in = new BufferedReader(new FileReader(System.getProperty("user.dir") + 
					"\\Account Details\\Contacts\\" + username + ".txt"));
			
			contacts = new ArrayList<String[]>();
			String line;
			while((line = in.readLine()) != null) {
				String[] contactInformation = line.trim().split(",");
				String relationship = contactInformation[1];
				
				if (!(relationship.equals("BLOCKED"))) {
					String contactUsername = contactInformation[0];
					String[] contact = new String[3];
					
					contact[0] = contactUsername;
					
					
					if (relationship.equals("PENDING1")) {
						contact[1] = contactUsername;
						contact[2] = "OFFLINE";
					}
					else if (relationship.equals("PENDING2")) {
						contact[1] = getName(contactUsername);
						contact[2] = "PENDING";
					}
					else {
						if(isOnline(contactUsername))
							contact[2] = "ONLINE";
						else
							contact[2] = "OFFLINE";
						
						contact[1] = getName(contactUsername);
					}
					
					contacts.add(contact);
				}
			}
			
			in.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("Cannot find contacts file.");
		} catch (IOException e) {
			System.out.println("Cannot read from contacts file.");
		}
		
		return contacts;         // return format: <{contact's username, contact's name, contact's status}>
	}
	
	protected boolean createAccount(String newUsername, String newPassword, String name) {
		if (getName(newUsername) == null) {
			addLoginCredentials(newUsername, newPassword);
			addToUsersList(newUsername, name);
			createContactsList(newUsername);
			return true;                             // if true then an account has been created successfully.
		}
		
		return false;								 // if false then an account with the given username already exists.
	}
	
	
	private void addLoginCredentials(String newUsername, String newPassword) {
		try {
			PrintWriter out = new PrintWriter(new FileWriter((System.getProperty("user.dir") + 
					"\\Account Details\\Logins\\logins.txt"), true));
			
			out.println(newUsername + "," + newPassword);
			out.close();
			
		} catch (IOException e) {
			System.out.println("Cannot wirte to login file");
			return;
		}
	}
	
	private void addToUsersList(String username, String name) {
		try {
			PrintWriter out = new PrintWriter(new FileWriter((System.getProperty("user.dir") + 
					"\\Account Details\\Users\\names.txt"), true));
			out.println(username + "," + name);
			out.close();
		} catch (IOException e) {
			System.out.println("Cannot wirte to login file");
		}
	}
	
	private void createContactsList(String username) {
		try {
			PrintWriter out = new PrintWriter(new FileWriter((System.getProperty("user.dir") + 
					"\\Account Details\\Contacts\\" + username + ".txt"), true));
			out.close();
			
		} catch (IOException e) {
			System.out.println("Cannot create contacts file.");
		}
	}
	
	protected void addContact(String username, String contactUsername) {
		if ((!isAlreadyFriend(username, contactUsername) || isBlocked(username, contactUsername))
					&& (getName(contactUsername) != null)) {
			try {
				PrintWriter out = new PrintWriter(new FileWriter((System.getProperty("user.dir") + 
						"\\Account Details\\Contacts\\" + username + ".txt"), true));
				out.println(contactUsername + "," + "PENDING1");
				out.close();
				
				out = new PrintWriter(new FileWriter((System.getProperty("user.dir") + 
						"\\Account Details\\Contacts\\" + contactUsername + ".txt"), true));
				out.println(username + "," + "PENDING2");
				out.close();
				
			} catch (IOException e) {
				System.out.println("Cannot wirte to contacts file");
			}
		}
		
		if(isOnline(contactUsername)) {
			String[] contactNodeInformation = nodesInformation.get(contactUsername);
			ChatClient chatClient = new ChatClient(contactNodeInformation[0], Integer.parseInt(contactNodeInformation[1]));
			chatClient.connectToServer();
			chatClient.sendFriendRequest(username, getName(username));
		}
	}
	
	private boolean isAlreadyFriend(String username, String contactUsername) {
		try {
			BufferedReader in = new BufferedReader(new FileReader(System.getProperty("user.dir") +
					"\\Account Details\\Contacts\\" + username + ".txt"));
			String line;
			while ((line = in.readLine()) != null) {
				String contact = line.trim().split(",")[0];
				
				if (contact.equals(contactUsername)) {
					String relationship = line.trim().split(",")[1];
					
					if(relationship.equals("FRIEND")) {
						in.close();
						return true;
					}
				}
			}
			
			in.close();
		} catch (FileNotFoundException e) {
			System.out.println("Cannot find contacts file.");
		} catch (IOException e) {
			System.out.println("Cannot read from contacts file.");
		}
		
		return false;
	}
	
	private boolean isBlocked(String username, String contactUsername) {
		try {
			BufferedReader in = new BufferedReader(new FileReader(System.getProperty("user.dir") +
					"\\Account Details\\Contacts\\" + username + ".txt"));
			
			while (in.readLine() != null) {
				String line = in.readLine();
				String contact = line.trim().split(",")[0];
				
				if (contact.equals(contactUsername)) {
					String relationship = line.trim().split(",")[1];
					
					if(relationship.equals("BLOCKED")) {
						in.close();
						return true;
					}
				}
			}
			
			in.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("Cannot find contacts file.");
		} catch (IOException e) {
			System.out.println("Cannot read from contacts file.");
		}
		
		return false;
	}
	
	protected void answerFriendRequest(String username, String contactUsername, String answer) {
		ArrayList<String> contactsCopy = new ArrayList<String>();
		
		if((username.equals(contactUsername))&&(answer.equals("BLOCKED")))
			return;
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(System.getProperty("user.dir") +
				"\\Account Details\\Contacts\\" + username + ".txt"));
			
			String line;
			while((line = in.readLine()) != null)
				contactsCopy.add(line);
			
			in.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("Cannot find contacts file.");
		} catch (IOException e) {
			System.out.println("Cannot read contacts file.");
		}
		
		for(int i = 0; i < contactsCopy.size(); i++) {
			String[] lineCopy = contactsCopy.get(i).split(",");
			if (lineCopy[0].equals(contactUsername)) {
				lineCopy[1] = answer;
				contactsCopy.set(i, (lineCopy[0] + "," + lineCopy[1]));
				break;
			}
		}
		
		try {
			PrintWriter out = new PrintWriter(new FileWriter(System.getProperty("user.dir") +
					"\\Account Details\\Contacts\\" + username + ".txt"));
			
			for(int i = 0; i < contactsCopy.size(); i++)
				out.println(contactsCopy.get(i));
			
			out.close();
			
		} catch (IOException e) {
			System.out.println("Cannot write to contacts file");
		}
		
		updateStatus(contactUsername, "ONLINE");
	}
	
	protected String[] connect(String username) {
		if(isOnline(username))
			return nodesInformation.get(username);
		
		return null;
	}
	
	private void setOnline(String username, String[] nodeInformation) { 
		nodesInformation.put(username, nodeInformation); 
	}
	
	protected void updateStatus(String username, String status) {
		try {
			BufferedReader in = new BufferedReader(new FileReader(System.getProperty("user.dir") + 
					"\\Account Details\\Contacts\\" + username + ".txt"));
			
			
			String line;
			while((line = in.readLine()) != null) {
				String contactUsername = line.split(",")[0];
				
				if(isOnline(contactUsername) && line.split(",")[1].equals("FRIEND")) {
					String[] nodeInformation = nodesInformation.get(contactUsername);
					ChatClient chatClient = new ChatClient(nodeInformation[0], 
							Integer.parseInt(nodeInformation[1]));
					chatClient.connectToServer();
					chatClient.updateContactStatus(username, getName(username), status);
				}
			}
			
			in.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("Cannot find contacts file.");
		} catch (IOException e) {
			System.out.println("Cannot read from contacts file.");
		}
	}
	
	protected void setOffline(String username) { nodesInformation.remove(username); }
	protected boolean isOnline(String username) { return nodesInformation.containsKey(username); }
	
}