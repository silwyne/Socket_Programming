import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	private ServerSocket ss;
	
	public Server(ServerSocket ss)
	{
		this.ss = ss;
	}
	public void startServer()
	{
		try
		{
			
			while(!ss.isClosed() && RunServer.StopServer == false)
			{
				Socket socket = ss.accept();
				RunServer.textArea.setText(RunServer.textArea.getText()+"\na new client has been connected!");
				ClientHandler clientHandler = new ClientHandler(socket);
				 Thread thread = new Thread(clientHandler) ;
				 thread.start() ;
			}
			
		}
		catch(IOException e)
		{
			
		}
	}
	public void closeServerSocket()
	{
		try {
			if(ss != null)
				ss.close();
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}

























