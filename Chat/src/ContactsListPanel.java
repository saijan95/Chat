import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.*;

public class ContactsListPanel extends JPanel {
	private JPanel contactsListPanel;
	private JScrollPane contactsListPane;
	private JButton connectButton;
	private ArrayList<ContactCard> contactCardsList;
	private ArrayList<ContactCard> selectedContactCardsList;
	private User user;
	private int listPosition;
	private int listHeight;
	private int selected;
	
	public ContactsListPanel(User user) {
		super();
		setSize(205,500);
		
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		contactsListPanel = new JPanel();
		contactsListPanel.setLocation(0, 0);
		contactsListPanel.setSize(200, 450);
		contactsListPanel.setLayout(null);
		
		contactsListPane = new JScrollPane(contactsListPanel);
		contactsListPane.setAutoscrolls(true);
		contactsListPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.insets = new Insets(5,5,5,5);
		constraints.weightx = 10;
		constraints.weighty = 10; 
		layout.setConstraints(contactsListPane, constraints);
		add(contactsListPane);
		
		connectButton = new JButton("Connect");
		connectButton.setEnabled(false);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.insets = new Insets(5,5,5,5);
		constraints.weightx = 1;
		constraints.weighty = 1; 
		layout.setConstraints(connectButton, constraints);
		add(connectButton);
		
		this.user = user;
		
	}
	
	protected void updateList() {
		contactsListPanel.removeAll();
		listPosition = 0;
		listHeight = 0;
		
		ArrayList<Contact> contactsList = user.getContactsList();
		contactCardsList = new ArrayList<ContactCard>();
		selectedContactCardsList = new ArrayList<ContactCard>();
		
		for(int i = 0; i < contactsList.size(); i++) {
			ContactCard contactCard = new ContactCard(contactsList.get(i));
			contactCard.setLocation(0, listPosition*24);
			
			if(listHeight >= 450)
				contactsListPanel.setPreferredSize(new Dimension(200, listHeight));
			
			class MouseHandler extends MouseAdapter {
				public void mouseClicked(MouseEvent e) {
					ContactCard cc = (ContactCard)e.getSource();
					select(cc);
				}
			}
			
			contactCard.addMouseListener(new MouseHandler());
			
			
			contactsListPanel.add(contactCard);
			listHeight += 20;
			listPosition += 1;
			
			contactCardsList.add(contactCard);
		}
		
		selected = 0;
		contactsListPane.updateUI();
		//contactsListPane.repaint();
		this.repaint();
	}
	
	protected void select(ContactCard contactCard) {
		if((contactCard.isSelected() == false) && (contactCard.getContact().getStatus().equals("ONLINE"))) {
			contactCard.setBackground(Color.GRAY);
			contactCard.setSelected(true);
			selectedContactCardsList.add(contactCard);
			selected += 1;
		}
		else if(contactCard.isSelected() == true) {
			contactCard.setBackground(null);
			contactCard.setSelected(false);
			selectedContactCardsList.remove(contactCard);
			selected -= 1;
		}
		
		if(selected > 0)
			connectButton.setEnabled(true);
		else if(selected == 0)
			connectButton.setEnabled(false);
	}
	
	protected ArrayList<ContactCard> getSelectedContactCardsList() { return selectedContactCardsList; }
	protected JButton getConnectButton() { return connectButton; }
}
