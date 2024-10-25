import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class RunServer {

	public static JButton Connect;
	public static JButton DisConnect;
	public static JFrame frame;
	public static JTextArea textArea;
	public static Socket socket;
	public static Thread RunningServer;
	public static boolean StopServer = false ;
	public static boolean Server_is_Running = false ;
	public static void main(String[] args){

		showWindow();

		
	}
	public static void showWindow()
	{
		frame = new JFrame();
		frame.setTitle("Server");
		frame.setSize(350, 450);
		frame.getContentPane().setBackground(Color.black);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);

		//text area
		textArea = new JTextArea(5, 20);
		textArea.setCaretPosition(0); // Set caret position to the beginning
		textArea.setBackground(Color.black);
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
		Connect = new JButton("Run");
		Connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(RunServer.Server_is_Running == false)
				{
					try 
					{
						RunningServer = new Thread(()->{
							serverON();
						});
						RunningServer.start();
						RunServer.Server_is_Running = true ;
						RunServer.textArea.setText(RunServer.textArea.getText()+"\nServer is Running!");
						JOptionPane.showMessageDialog(null, "Server is Running", "Warning", JOptionPane.INFORMATION_MESSAGE);
					} 
					catch (Exception e1) 
					{
						e1.printStackTrace();
					} 
				}
				else {
					JOptionPane.showMessageDialog(null, "Server is Running already!", "Warning", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		DisConnect = new JButton("Stop");
		DisConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(RunServer.Server_is_Running == true)
				{
					try 
					{
						RunServer.StopServer = true ;
						RunServer.Server_is_Running = false ;
						RunServer.textArea.setText(RunServer.textArea.getText()+"\nServer is Stoped!");
						JOptionPane.showMessageDialog(null, "Server is stoped !", "Warning", JOptionPane.INFORMATION_MESSAGE);
					} 
					catch (Exception e1) 
					{
						e1.printStackTrace();
					} 
				}
				else {
					JOptionPane.showMessageDialog(null, "Server is Off already!", "Warning", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		Connect.setFocusable(false);
		textArea.setFocusable(true);
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(CLOSE);
		buttonPanel.add(Connect);
		buttonPanel.add(DisConnect);
		frame.getContentPane().add(scrollPane);
		frame.pack();
		frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		frame.setVisible(true);
	}
	public static void serverON()
	{
		ServerSocket ss;
		try 
		{
			ss = new ServerSocket(1234);
			Server server = new Server(ss);
			server.startServer();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	}
}
