import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;

import javax.swing.*;

public class ContactCard extends JPanel {
	private JLabel statusLabel;
	private JLabel nameLabel;
	private Contact contact;
	private boolean selected;
	
	public ContactCard(Contact contact) {
		super();
		setSize(200, 25);
		setLayout(null);
		
		this.contact = contact;
		
		statusLabel = new JLabel();
		statusLabel.setLocation(2, 2);
		statusLabel.setSize(20, 20);
		add(statusLabel);
		
		nameLabel = new JLabel(this.contact.getName());
		nameLabel.setLocation(25, 2);
		nameLabel.setSize(123, 20);
		add(nameLabel);
		
		selected = false;
		updateStatus();
	}
	
	protected void updateStatus() {
		if (contact.getStatus().equals("ONLINE")) {
			statusLabel.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
					System.getProperty("user.dir") + "\\Resources\\Online.png")));
		}
		else if (contact.getStatus().equals("OFFLINE")) {
			statusLabel.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
					System.getProperty("user.dir") + "\\Resources\\Offline.png")));
		}
	}
	
	protected void setSelected(boolean selected) { 
		this.selected = selected;
	}
	
	protected Contact getContact() { return contact; }
	protected boolean isSelected() { return selected; }
}
