package nilian.client;

import nilian.window.ClientWindow;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client {


	private static Socket soc;
	private static BufferedReader br ;
	private static BufferedWriter bw;
	private static String username ;


	public Client(Socket soc , String Username)
	{
		try
		{
			Client.soc = soc ;
			Client.br = new BufferedReader(new InputStreamReader(soc.getInputStream()));
			Client.bw = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));
			Client.username = Username ;
			bw.write(username);
			bw.newLine();
			bw.flush();
		}catch(IOException e)
		{
			closeEverything(soc , br , bw);
		}
		
	}


	/**
	 * Simply sends message to server.
	 * @param s message string!
	 */
	public static void sendMessage(String s)
	{
		try
		{
			ClientWindow.textArea.setText(ClientWindow.textArea.getText()+"\n"+username+": "+s);
			bw.write(username + ": " + s);
			bw.newLine();
			bw.flush();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	/**
	 * Listens for other Clients messages!
	 * Actually from server which broadcasts other client messages  to me!
	 */
	public void listenForMessage()
	{
		new Thread(new Runnable() {

			@Override
			public void run() {
				String messageFromGroupChat ;
				while(soc.isConnected())
				{
					try {
						messageFromGroupChat = br.readLine();
						ClientWindow.textArea.setText(ClientWindow.textArea.getText()+"\n"+messageFromGroupChat);
					}catch(IOException e)
					{
						closeEverything(soc , br , bw) ;
					}
				}
			}
			
		}).start();
	}

	/**
	 * Closes all Connection and Objects!
	 * @param soc socket object
	 * @param br BufferedReader Object
	 * @param bw BufferedWriter Object
	 */
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
