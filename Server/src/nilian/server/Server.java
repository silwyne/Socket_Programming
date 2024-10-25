package nilian.server;

import nilian.window.ServerWindow;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Main server object.
 * Gets each nilian.client.client connection and let the ClientHandler.Class to handle the rest
 *
 * @author seyed mohamad hasan tabatabaei
 */
public class Server {

	private final ServerSocket ss;

	public Server(ServerSocket ss) {
		this.ss = ss;
	}

	/**
	 * This Object starts the server listening for new connections
	 * And handle each connection by passing it to ClientHandler.Class
	 */
	public void startServer() {
		while (!ss.isClosed() && !ServerWindow.StopServer) {
			Socket socket = null;
			try {
				// listens for new connections!
				socket = ss.accept();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			ServerWindow.textArea.setText(ServerWindow.textArea.getText() + "\na new nilian.client.client has been connected!");
			ClientHandler clientHandler = new ClientHandler(socket);
			Thread thread = new Thread(clientHandler);
			thread.start();
		}
	}

}