package nilian.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * This class handles connected clients!
 * Makes it possible for clients to see each others messages.
 *
 * @author seyed mohamad hasan tabatabaei
 */
public class ClientHandler implements Runnable {

	//List of Shared Connections between all Clients!
	public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
	private Socket soc;
	private BufferedReader br ;
	private BufferedWriter bw ;
	private String clientUsername ;


	public ClientHandler(Socket socket)
	{
		try
		{
			this.soc = socket ;
			this.bw = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));
			this.br = new BufferedReader(new InputStreamReader(soc.getInputStream()));
			this.clientUsername = br.readLine();
			clientHandlers.add(this) ;
			broadcastMessage("SERVER: "+ clientUsername+" has entered the chat!") ;
		}catch(IOException e)
		{
			closeEverything(soc, br , bw);
		}
	}


	/**
	 * This runnable listens for clients messages and broadcasts them to other clients!
	 */
	@Override
	public void run() {
		String messageFromClient;
		while(soc.isConnected())
		{
			try {
				messageFromClient = br.readLine();
				broadcastMessage(messageFromClient);
			}catch(IOException e)
			{
				closeEverything(soc , br , bw);
				break ;
			}
		}
	}

	/**
	 * This broadCasts a message to all connected Clients!
	 * @param message some client message to other Clients
	 */
	public void broadcastMessage(String message)
	{
		for(ClientHandler clientHandler : clientHandlers)
		{
			try
			{
				if(!clientHandler.clientUsername.equals(clientUsername))
				{
					clientHandler.bw.write(message);
					clientHandler.bw.newLine();
					clientHandler.bw.flush();
				}
			}catch(IOException e)
			{
				closeEverything(soc , br , bw) ;
			}
		}
	}
	

	public void removeClientHandler()
	{
		clientHandlers.remove(this);
		broadcastMessage("SERVER: "+clientUsername+" has left the chat");
	}


	public void closeEverything(Socket soc , BufferedReader br, BufferedWriter bw)
	{
		removeClientHandler();
		try
		{
			if(br != null)
			{
				br.close();
			}
			if(bw != null)
			{
				bw.close();
			}
			if(soc != null)
			{
				soc.close();
			}
		}catch(IOException e)
		{
			e.printStackTrace(System.out);
		}
	}

}