import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class AddContactDialog extends JDialog {
	protected JTextField usernameText;
	
	public AddContactDialog(JFrame owner, String title, boolean modal) {
		super(owner, title, modal);
		setSize(300, 160);
		setLayout(null);
		setLocationRelativeTo(owner);
		setResizable(false);
		
		JLabel label1 = new JLabel("Username: ");
		label1.setLocation(20, 20);
		label1.setSize(80,30);
		add(label1);
		
		usernameText = new JTextField();
		usernameText.setLocation(105, 20);
		usernameText.setSize(175, 30);
		add(usernameText);
		
		JButton addButton = new JButton("Add");
		addButton.setLocation(115, 80);
		addButton.setSize(80, 30);
		add(addButton);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setLocation(200, 80);
		cancelButton.setSize(80, 30);
		add(cancelButton);
		
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (getOwner() != null)
					((DialogClientInterface)getOwner()).dialogFinished(getClass().getName());
				dispose();
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});	
	}
	
	protected String getUsername() { return usernameText.getText().trim(); }
}
