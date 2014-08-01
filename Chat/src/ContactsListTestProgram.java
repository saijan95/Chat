import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class ContactsListTestProgram extends JFrame {
	private User user;
	private ContactsListPanel contactsListPanel;
	public ContactsListTestProgram(User user) {
		super("Test Program");
		setSize(205, 550);
		
		this.user = user;
		
		contactsListPanel = new ContactsListPanel(user);
		contactsListPanel.setLocation(0, 0);
		getContentPane().add(contactsListPanel);
	}
	
	protected void updateList() {
		contactsListPanel.updateList();
	}
	
	public static void main(String[] args) {
		User user = new User("cool1", "ONLINE", "192.168.2.1", 123);
		for(int i = 0; i < 40; i++) {
			String username = "Cool" + i;
			user.addContactToList(new Contact(username, "Cool", "OFFLINE"));
		}
		
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

		ContactsListTestProgram app = new ContactsListTestProgram(user);
		app.updateList();
		app.setVisible(true);
	}
}
