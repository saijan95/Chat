import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class DeleteContactDialog extends JDialog {
protected JTextField usernameText;
	
	public DeleteContactDialog(JFrame owner, String title, boolean modal) {
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
		
		JButton deleteButton = new JButton("Delete");
		deleteButton.setLocation(115, 80);
		deleteButton.setSize(80, 30);
		add(deleteButton);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setLocation(200, 80);
		cancelButton.setSize(80, 30);
		add(cancelButton);
		
		deleteButton.addActionListener(new ActionListener() {
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
