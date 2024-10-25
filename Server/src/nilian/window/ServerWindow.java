package nilian.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.net.ServerSocket;

import nilian.server.Server;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Main Graphic class for server
 * Makes them all visible!
 *
 * @author seyed mohamad hasan tabatabaei
 */
public class ServerWindow {

	public static JButton Connect;
	public static JButton DisConnect;
	public static JFrame frame;
	public static JTextArea textArea;
	public static Thread RunningServer;
	public static boolean StopServer = false ;
	public static boolean Server_is_Running = false ;

	public static Server server;

	public static void show() throws IOException {

		//server Object
		server = new Server(new ServerSocket(1234));


		frame = new JFrame();
		frame.setTitle("nilian.server.Server");
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
		CLOSE.addActionListener(e -> System.exit(0));

		// running server button
		Connect = new JButton("Run");
		Connect.addActionListener(e -> {
            if(!ServerWindow.Server_is_Running)
            {
                try
                {
                    RunningServer = new Thread(server::startServer);
                    RunningServer.start();
                    ServerWindow.Server_is_Running = true ;
                    ServerWindow.textArea.setText(ServerWindow.textArea.getText()+"\nnilian.server.Server is Running!");
                    JOptionPane.showMessageDialog(null, "Server is Running", "Warning", JOptionPane.INFORMATION_MESSAGE);
                }
                catch (Exception e1)
                {
                    e1.printStackTrace(System.out);
                }
            }
            else {
                JOptionPane.showMessageDialog(null, "Server is Running already!", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

		// stopping server button
		DisConnect = new JButton("Stop");
		DisConnect.addActionListener(e -> {
            if(ServerWindow.Server_is_Running)
            {
                try
                {
                    ServerWindow.StopServer = true ;
                    ServerWindow.Server_is_Running = false ;
                    ServerWindow.textArea.setText(ServerWindow.textArea.getText()+"\nServer is Stopped!");
                    JOptionPane.showMessageDialog(null, "Server is stopped !", "Warning", JOptionPane.INFORMATION_MESSAGE);
                }
                catch (Exception e1)
                {
                    e1.printStackTrace(System.out);
                }
            }
            else {
                JOptionPane.showMessageDialog(null, "Server is Off already!", "Warning", JOptionPane.INFORMATION_MESSAGE);
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
}
