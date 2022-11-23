package Week2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {
	private Socket socket = null;
	private String name = null;
	private PrintWriter out;
	private BufferedReader in;
	public ChatClient(String name) {
		try {
			socket = new Socket("localhost", 10000);
			this.name = name;
			System.out.println("Client object successfully created with name:" + name);
			this.out = new PrintWriter(socket.getOutputStream(), true);
			this.in = new BufferedReader(new InputStreamReader(
			socket.getInputStream())); 
			initialise();		
		} catch (IOException ex) {
			//System.out.println(ex);
			ex.printStackTrace();
		}
	}
	private void initialise() {
		System.out.println("Client is trying to initialise by sending its name to server");
		out.println("Init:"+name);
		System.out.println("Client sent its name to server");
	}	
	public void run() {
		try {
			while(true) {
				//await information from command line and then send it to server to be broadcast to other clients
				String userInput = System.console().readLine();
				out.println(userInput);
				
				//if new information is available from the server for this client, then display it on screen
				String serverInput = in.readLine();
				System.out.println("Received from server:" + serverInput);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
