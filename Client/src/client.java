import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class client {
	private static Socket soc;
	private static BufferedReader br ;
	private static BufferedWriter bw;
	private static String username ;
	public client(Socket soc , String Username)
	{
		try
		{
			client.soc = soc ;
			client.br = new BufferedReader(new InputStreamReader(soc.getInputStream()));
			client.bw = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));
			client.username = Username ;
			bw.write(username);
			bw.newLine();
			bw.flush();
		}catch(IOException e)
		{
			closeEverything(soc , br , bw);
		}
		
	}
	public static void sendMessage(String s)
	{
		try
		{
			RunClient.textArea.setText(RunClient.textArea.getText()+"\n"+username+": "+s);
			bw.write(username + ": " + s);
			bw.newLine();
			bw.flush();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void listenFormessage()
	{
		new Thread(new Runnable() {

			@Override
			public void run() {
				String messageFromGroupChat ;
				while(soc.isConnected())
				{
					try {
						messageFromGroupChat = br.readLine();
						RunClient.textArea.setText(RunClient.textArea.getText()+"\n"+messageFromGroupChat);
					}catch(IOException e)
					{
						closeEverything(soc , br , bw) ;
					}
				}
			}
			
		}).start();
	}
	public static void closeEverything(Socket soc , BufferedReader br, BufferedWriter bw)
	{
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
