import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class FriendRequestDialog extends JDialog implements Runnable {
	private JButton acceptButton;
	private JButton cancelButton;
	private String contactName;
	private Contact contact;
	private ChatApp chatApp;
	
	public FriendRequestDialog(ChatApp owner, String title, boolean modal, Contact contact) {
		super((JFrame)owner, title, modal);
		setSize(350,200);
		setLocationRelativeTo((JFrame)owner);
		setResizable(false);
		
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		JLabel label1 = new JLabel("<html>This person would like to be your friend:</html>");
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.fill = GridBagConstraints.EAST;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.insets = new Insets(5,5,5,5);
		constraints.weightx = 1;
		constraints.weighty = 1; 
		layout.setConstraints(label1, constraints);
		add(label1);
		
		this.contactName = contact.getName();
		this.contact = contact;
		chatApp = owner;
		
		JLabel nameLabel = new JLabel(this.contactName);
		Font nameFont = new Font("Segoe WP", Font.BOLD, 14);
		nameLabel.setFont(nameFont);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.fill = GridBagConstraints.EAST;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.insets = new Insets(5,5,5,5);
		constraints.weightx = 1;
		constraints.weighty = 5; 
		layout.setConstraints(nameLabel, constraints);
		add(nameLabel);
		
		acceptButton = new JButton("Accept");
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.insets = new Insets(5,5,5,5);
		constraints.weightx = 1;
		constraints.weighty = 1; 
		layout.setConstraints(acceptButton, constraints);
		add(acceptButton);
		
		cancelButton = new JButton("Cancel");
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.insets = new Insets(5,5,5,5);
		constraints.weightx = 1;
		constraints.weighty = 1; 
		layout.setConstraints(cancelButton, constraints);
		add(cancelButton);
		
		acceptButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				accept();
				dispose();
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((DialogClientInterface)getOwner()).dialogCancelled(getClass().getName());
				dispose();
			}
		});	
	}
	
	private void accept() {
		chatApp.answerFriendRequest(this);
	}
	protected Contact getContact() { return contact; }

	@Override
	public void run() {
		this.setVisible(true);
	}
}
