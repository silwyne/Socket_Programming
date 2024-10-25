package nilian.window;

import nilian.client.Client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClientWindow {
	public static JButton Username;
	public static String username = null;
	public static JButton Connect;
	public static JFrame frame;
	public static JTextArea textArea;
	public static Socket socket;
	public static Thread ConnectingThread;
	public static JButton sendButton ;
	public static boolean Connected_to_the_server = false ;


	public static void show()
	{
		frame = new JFrame();
		frame.setTitle("Client");
		frame.setSize(350, 450);
		frame.getContentPane().setBackground(Color.black);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		
		//text area
        textArea = new JTextArea(5, 20);
        textArea.setCaretPosition(0); // Set caret position to the beginning
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.green);
        Font font = textArea.getFont();
        Font biggerFont = font.deriveFont(font.getSize() + 10f); // Increase font size by 10
        textArea.setFont(biggerFont);

        // Add the text area to a scroll pane
        JScrollPane scrollPane = new JScrollPane(textArea);

        // Set the preferred size of the scroll pane
        scrollPane.setPreferredSize(frame.getSize());

		//buttons
		JButton CLOSE = new JButton("Close");
		CLOSE.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        System.exit(0);
		    }
		});
		Username = new JButton("username");
		Username.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	openUsernameInputWindow();
		    }
		});

		Connect = new JButton("Connect");
		Connect.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	if(username == null) {
		    		JOptionPane.showMessageDialog(null, "First set a username", "Warning", JOptionPane.WARNING_MESSAGE);
		    	} else {
					try {
						ConnectingThread = new Thread(ClientWindow::Connection);
						ConnectingThread.start();
					} 
					catch (Exception e1) 
					{
						JOptionPane.showMessageDialog(null, "Server is OFF", "Warning", JOptionPane.WARNING_MESSAGE);
					} 
		    	}
		    }
		});

		sendButton = new JButton("Send");
		sendButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	if(Connected_to_the_server) {
			    	SendMessageWindow.show();
		    	}
		    	else {
					JOptionPane.showMessageDialog(null, "First Connect to a server!", "Warning", JOptionPane.WARNING_MESSAGE);
		    	}
		    }
		});

		textArea.setEditable(false);
		Connect.setFocusable(false);
		textArea.setFocusable(true);
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(CLOSE);
		buttonPanel.add(Username);
		buttonPanel.add(Connect);
		buttonPanel.add(sendButton);
		frame.getContentPane().add(scrollPane);
        frame.pack();
		frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		frame.setVisible(true);
	}

	/**
	 * Handles all about getting username from user!
	 */
	private static void openUsernameInputWindow() 
	{
        // Create the JFrame for username input
        JFrame usernameFrame = new JFrame("Username Input");
        usernameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create a JPanel to hold components
        JPanel panel = new JPanel();

        // Create a JTextField for username input
        JTextField textField = new JTextField(20);
        if (username != null) {
        	textField.setText(username); // Set the last username as the initial text value
        }
        else {
        	textField.setText("at least 4 characters");
        }
        panel.add(textField);

        // Create a button to retrieve the username
		JButton button = getSaveUserNameButton(textField, usernameFrame);
		panel.add(button);
        // Disable the "Run" button in the initial window
        // Add the panel to the usernameFrame
        usernameFrame.setLocationRelativeTo(null);
        usernameFrame.getContentPane().add(panel);
        usernameFrame.pack();
        usernameFrame.setVisible(true);
    }

	/**
	 * Returns a button to set or save the username
	 * @param textField the field containing the username
	 * @param usernameFrame frame that takes the username
	 * @return a button to get the username!
	 */
	private static JButton getSaveUserNameButton(JTextField textField, JFrame usernameFrame) {
		JButton button = new JButton("Save");
		button.addActionListener(e -> {
			String username = textField.getText();
			if(username.length()>4)
			{
				JOptionPane.showMessageDialog(usernameFrame, "Username: " + username);
				ClientWindow.username = username ;
			}
			else
			{
				JOptionPane.showMessageDialog(null, "at least 4 characters", "Warning", JOptionPane.WARNING_MESSAGE);
			}
			usernameFrame.dispose();// Close the username input window
			// Enable the "Run" button in the initial window
		});
		return button;
	}


	/**
	 * Tries to connect to the server on port 1234
	 */
	public static void Connection() {
     	try 
     	{
			socket = new Socket("localHost" ,1234);
			Client c = new Client(socket , username);
			c.listenForMessage();
			SendMessageWindow.show();
			ClientWindow.textArea.setText("Connected to the Server !");
			Connected_to_the_server = true ;
		} 
     	catch (Exception e1) 
		{
			JOptionPane.showMessageDialog(null, "Server is OFF", "Warning", JOptionPane.WARNING_MESSAGE);
		} 
			
    }

}