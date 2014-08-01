import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class ConnectionPropertiesDialog extends JDialog {
	private JButton okButton;
	
	public ConnectionPropertiesDialog(JFrame owner, String title, boolean modal, 
										String ip, int port, String contactIp, int contactPort) {
		super(owner, title, modal);
		setSize(570,290);
		setLayout(null);
		setLocationRelativeTo(owner);
		setResizable(false);
		
		JLabel label1 = new JLabel("Enter your contact's network properties. \n" +
									"And share your network properties with your contact.");
		label1.setLocation(10,10);
		label1.setSize(600,15);
		add(label1);
		
		JLabel label2 = new JLabel("Your Network Properties");
		label2.setLocation(120, 50);
		label2.setSize(150,15);
		add(label2);
		
		JLabel label3 = new JLabel("Contact's Network Properties");
		label3.setLocation(300, 50);
		label3.setSize(175,15);
		add(label3);
		
		JLabel label4 = new JLabel("IP Address: ");
		label4.setLocation(10, 100);
		label4.setSize(120,15);
		add(label4);
		
		JLabel label5 = new JLabel("Server Port: ");
		label5.setLocation(10, 140);
		label5.setSize(120,15);
		add(label5);
		
		JLabel label6 = new JLabel("" + ip);
		label6.setLocation(120, 100);
		label6.setSize(170, 15);
		add(label6);
		
		JLabel label7 = new JLabel("" + port);
		label7.setLocation(120, 140);
		label7.setSize(170, 15);
		add(label7);
		
		JLabel label8 = new JLabel("" + contactIp);
		label8.setLocation(300,100);
		label8.setSize(170,30);
		add(label8);
		
		JLabel label9 = new JLabel("" + contactPort);
		label9.setLocation(300,140);
		label9.setSize(170,30);
		add(label9);
		
		JPanel commandPanel = new JPanel();
		commandPanel.setLayout(null);
		commandPanel.setLocation(0,200);
		commandPanel.setSize(600,50);
		
		okButton = new JButton("Ok");
		okButton.setLocation(440,10);
		okButton.setSize(100,30);
		commandPanel.add(okButton);
		
		add(commandPanel);
		
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}
}
