package Week2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ChatServer {
	private ServerSocket serverSocket;
	HashMap<String, ClientConnection> connectionsMap = new HashMap<>();
	public ChatServer() {
		try {
			serverSocket = new ServerSocket(10000);
			serverSocket.setSoTimeout(30000); // 30 second timeout
			System.out.println("Server object successfully created");
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	public void acceptConnections() throws IOException {
		while(true) {
			System.out.println("Server waiting for connection from Client");
			Socket socket = serverSocket.accept();
			ClientConnection clientConnection = new ClientConnection(socket);
			this.connectionsMap.put(clientConnection.name, clientConnection);
			clientConnection.start();
		}
	}	
	class ClientConnection extends Thread {		
		@SuppressWarnings("unused")
		private Socket socket;
		PrintWriter out;
		BufferedReader in;
		private String name;		
		public ClientConnection(Socket socket) throws IOException {
			this.socket = socket;
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(
			socket.getInputStream()));
			
			System.out.println("ClientConnection is waiting for client to initialize");
			
			String initString = in.readLine();
			System.out.println("ClientConnection received:"+initString);
			this.name = initString.substring(initString.indexOf("Init:")+5);
			
			System.out.println("Created a new server-side connection for Client:" + this.name);
		}	
		public void sendInfoToClient(String information) {
			out.println(information);
		}		
		public void start() {			
		}	
		public void run() {
			try {
				while(true) {
					String inputFromClient = in.readLine();
					out.println("Server sending back:"+inputFromClient);
				}
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}		
	}
}