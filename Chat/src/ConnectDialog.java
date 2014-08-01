import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class ConnectDialog extends JDialog {
	private JButton connectButton;
	private JButton cancelButton;
	private JTextField ipText;
	private JTextField portText;
	
	public ConnectDialog(JFrame owner, String title, boolean modal, String ip, int port) {
		super(owner, title, modal);
		setSize(555,285);
		setLayout(null);
		setLocationRelativeTo(owner);
		setResizable(false);
		
		JLabel label1 = new JLabel("<html>Enter your contact's network properties.<br> And share your network properties with your contact.</html>");
		label1.setLocation(10,10);
		label1.setSize(600,30);
		add(label1);
		
		JLabel label2 = new JLabel("Your Network Properties");
		label2.setLocation(120, 55);
		label2.setSize(150,15);
		add(label2);
		
		JLabel label3 = new JLabel("Contact's Network Properties");
		label3.setLocation(300, 55);
		label3.setSize(175,15);
		add(label3);
		
		JLabel label4 = new JLabel("IP Address: ");
		label4.setLocation(10, 105);
		label4.setSize(120,15);
		add(label4);
		
		JLabel label5 = new JLabel("Server Port: ");
		label5.setLocation(10, 145);
		label5.setSize(120,15);
		add(label5);
		
		JLabel label6 = new JLabel("" + ip);
		label6.setLocation(120, 105);
		label6.setSize(170, 15);
		add(label6);
		
		JLabel label7 = new JLabel("" + port);
		label7.setLocation(120, 145);
		label7.setSize(170, 15);
		add(label7);
		
		ipText = new JTextField();
		ipText.setLocation(300,105);
		ipText.setSize(170,30);
		add(ipText);
		
		portText = new JTextField();
		portText.setLocation(300,145);
		portText.setSize(170,30);
		add(portText);
		
		JPanel commandPanel = new JPanel();
		commandPanel.setLayout(null);
		commandPanel.setLocation(0,205);
		commandPanel.setSize(600,50);
		
		connectButton = new JButton("Connect");
		connectButton.setLocation(335,15);
		connectButton.setSize(100,30);
		commandPanel.add(connectButton);
		
		cancelButton = new JButton("Cancel");
		cancelButton.setLocation(440,15);
		cancelButton.setSize(100,30);
		commandPanel.add(cancelButton);
		
		add(commandPanel);
		
		connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (getOwner() != null)
					((DialogClientInterface)getOwner()).dialogFinished(getClass().getName());
				dispose();
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (getOwner() != null)
					((DialogClientInterface)getOwner()).dialogCancelled(getClass().getName());
				dispose();
			}
		});
	}
	
	public String getContactIp() {return ipText.getText();}
	public int getContactPort() {return Integer.parseInt(portText.getText());}
}
