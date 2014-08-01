import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class ChatBox extends JFrame {
	private JTextArea chatDisplay;
	private JTextArea composeText;
	private JButton sendButton;
	private Server server;
	private Client client;
	
	private User user;
	private Contact contact;
	
	private String contactIP;
	private int contactPort;
	
	private int numberOfMessages;
	
	public ChatBox(User user, Server server, Contact contact, String contactIP, int contactPort) {
		super("Chat-" + contact.getName());
		setSize(500,500);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		setIconImage(Toolkit.getDefaultToolkit().getImage( System.getProperty("user.dir") + "\\Resources\\ChatIcon.png"));
		
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		setLayout(layout);
		
		Font defaultFont = new Font("Segoe WP", Font.PLAIN, 14);
		
		chatDisplay = new JTextArea();
		chatDisplay.setEditable(false);
		chatDisplay.setLineWrap(true);
		chatDisplay.setFont(defaultFont);
		JScrollPane scrollChatDisplay = new JScrollPane(chatDisplay);
		scrollChatDisplay.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.insets = new Insets(5,5,5,5);
		constraints.weightx = 1;
		constraints.weighty = 10; 
		layout.setConstraints(scrollChatDisplay, constraints);
		getContentPane().add(scrollChatDisplay);
		
		composeText = new JTextArea();
		composeText.setLineWrap(true);
		composeText.setFont(defaultFont);
		JScrollPane scrollComposeText = new JScrollPane(composeText);
		scrollComposeText.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.insets = new Insets(5,5,5,5);
		constraints.weightx = 12;
		constraints.weighty = 1; 
		layout.setConstraints(scrollComposeText, constraints);
		getContentPane().add(scrollComposeText);
		
		sendButton = new JButton("Send");
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(5,5,5,5);
		constraints.weightx = 1;
		constraints.weighty = 1; 
		layout.setConstraints(sendButton, constraints);
		getContentPane().add(sendButton);
		
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String outgoingMessage = composeText.getText().trim();
				
					if(!(outgoingMessage.equals(""))) {
						if(numberOfMessages == 0) {
							sendChatRequest(outgoingMessage);
							numberOfMessages++;
							return;
						}
						
						sendMessage(outgoingMessage);
						numberOfMessages++;
					}
			}		
		});
		
		InputMap inputMap = composeText.getInputMap(0);
		ActionMap actionMap = composeText.getActionMap();
		
		KeyStroke enterStroke = KeyStroke.getKeyStroke("ENTER");
		KeyStroke shiftEnter = KeyStroke.getKeyStroke("shift ENTER");
		
		inputMap.put(shiftEnter, "insert-break");
		inputMap.put(enterStroke, enterStroke.toString());
		actionMap.put(enterStroke.toString(), new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				String outgoingMessage = composeText.getText().trim();	
				
				if(!(outgoingMessage.equals(""))) {
					if(numberOfMessages == 0) {
						sendChatRequest(outgoingMessage);
						numberOfMessages++;
						return;
					}
					
					sendMessage(outgoingMessage);
					numberOfMessages++;
				}
			}
		});
		this.contactIP = contactIP;
		this.contactPort = contactPort;
		this.server = server;
		this.user = user;
		this.contact = contact;
		System.out.println(contactIP);
		client = new Client(contactIP, contactPort);
		numberOfMessages = 0;
	}
	
	public void sendChatRequest(String outgoingMessage) {
		if(client != null) {
			client.connectToServer();
			client.sendChatRequest(user.getUsername(), server.getIP(), server.getPort(), outgoingMessage);
			String updateMessage = "You say: \n" + outgoingMessage + "\n\n";
			this.updateChatDisplay(updateMessage);
			composeText.setText("");
		}
	}
	
	public void sendMessage(String outgoingMessage) {
		if (client != null) {
			client.connectToServer();
			client.sendMessage(user.getUsername(), outgoingMessage);
			String updateMessage = "You say: \n" + outgoingMessage + "\n\n";
			this.updateChatDisplay(updateMessage);
			composeText.setText("");
		} 
		else {
			JOptionPane.showMessageDialog(this, "CLIENT: Cannot find server", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void recieveMessage(String incomingMessage) {
		String updateMessage = contact.getName() + "\n" + incomingMessage + "\n\n";
		this.updateChatDisplay(updateMessage);
		numberOfMessages++;
	}
	
	public void updateChatDisplay(String updateMessage) {
		chatDisplay.append(updateMessage);
	}
}
