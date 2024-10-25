import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

	public static ArrayList<ClientHandler> clientHandlers = new ArrayList<ClientHandler>();
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
	
	public void removeClienthandler()
	{
		clientHandlers.remove(this);
		broadcastMessage("SERVER: "+clientUsername+" has left the chat");
	}
	
	public void closeEverything(Socket soc , BufferedReader br, BufferedWriter bw)
	{
		removeClienthandler();
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
			e.printStackTrace();
		}
	}
	
	
	
	
	
}
