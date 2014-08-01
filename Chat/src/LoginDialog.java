import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class LoginDialog extends JDialog {
	private JTextField usernameText;
	private JTextField passwordText;
	
	public LoginDialog(JFrame owner, String title, boolean modal) {
		super(owner, title, modal);
		setLocationRelativeTo(owner);
		setSize(300, 180);
		setLayout(null);
		setResizable(false);
		
		JLabel label1 = new JLabel("Username: ");
		label1.setLocation(20, 20);
		label1.setSize(80, 25);
		add(label1);
		
		JLabel label2 = new JLabel("Password: ");
		label2.setLocation(20, 55);
		label2.setSize(80, 25);
		add(label2);
		
		usernameText = new JTextField();
		usernameText.setLocation(105, 20);
		usernameText.setSize(150, 25);
		add(usernameText);
		
		passwordText = new JTextField();
		passwordText.setLocation(105, 55);
		passwordText.setSize(150, 25);
		add(passwordText);
		
		JButton newAccountButton = new JButton("New Account");
		newAccountButton.setLocation(70, 100);
		newAccountButton.setSize(100, 30);
		add(newAccountButton);
		
		JButton loginButton = new JButton("Login");
		loginButton.setLocation(175, 100);
		loginButton.setSize(80, 30);
		add(loginButton);
		
		newAccountButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((DialogClientInterface)getOwner()).dialogCancelled(getClass().getName().toString());
			}
		});	
		
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ((usernameText.getText() != "") && (passwordText.getText() != "")) {
					((DialogClientInterface)getOwner()).dialogFinished(getClass().getName().toString());
				}
				else {
					showLoginError();
				}
			}
		});
	}
	
	private void showLoginError() {
		JOptionPane.showMessageDialog(this, "Invalid username or password", "Error",
				JOptionPane.ERROR_MESSAGE);
	}
	
	protected String getUsername() { return usernameText.getText(); }
	protected String getPassword() { return passwordText.getText(); }
	protected JTextField getUsernameTextField() { return usernameText; }
	protected JTextField getPasswordTextField() { return passwordText; }
}
