import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class NewAccountDialog extends JDialog {
	private JTextField usernameText;
	private JTextField passwordText;
	private JTextField firstnameText;
	private JTextField lastnameText;
	
	public NewAccountDialog(JFrame owner, String title, boolean modal) {
		super(owner, title, modal);
		setLocationRelativeTo(owner);
		setSize(300, 230);
		setLayout(null);
		setResizable(false);
		
		JLabel label1 = new JLabel("Username: ");
		label1.setLocation(20, 20);
		label1.setSize(80, 20);
		add(label1);
		
		JLabel label2 = new JLabel("First Name: ");
		label2.setLocation(20, 50);
		label2.setSize(80, 20);
		add(label2);
		
		JLabel label3 = new JLabel("Last Name: ");
		label3.setLocation(20, 80);
		label3.setSize(80, 20);
		add(label3);
		
		JLabel label4 = new JLabel("Password: ");
		label4.setLocation(20, 110);
		label4.setSize(80, 20);
		add(label4);
		
		usernameText = new JTextField();
		usernameText.setLocation(105, 20);
		usernameText.setSize(150, 20);
		add(usernameText);
		
		firstnameText = new JTextField();
		firstnameText.setLocation(105, 50);
		firstnameText.setSize(150, 20);
		add(firstnameText);
		
		lastnameText = new JTextField();
		lastnameText.setLocation(105, 80);
		lastnameText.setSize(150, 20);
		add(lastnameText);
		
		passwordText = new JTextField();
		passwordText.setLocation(105, 110);
		passwordText.setSize(150, 20);
		add(passwordText);
		
		JButton createButton = new JButton("Create");
		createButton.setLocation(90, 150);
		createButton.setSize(80, 30);
		add(createButton);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setLocation(175, 150);
		cancelButton.setSize(80, 30);
		add(cancelButton);
		
		createButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((DialogClientInterface)getOwner()).dialogFinished(getClass().getName().toString());
				
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((DialogClientInterface)getOwner()).dialogCancelled(getClass().getName().toString());
				dispose();
			}
		});
		
	}
	
	protected String getUsername() { return usernameText.getText().trim(); }
	protected String getPassword() { return passwordText.getText().trim(); }
	protected String getNameOfUser() { return (firstnameText.getText().trim() + " " + lastnameText.getText().trim()); }
}
