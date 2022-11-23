package Week2;

import java.io.IOException;

public class RunServer {
	public static void main(String[] args) {
		ChatServer server = new ChatServer();
		try {
			server.acceptConnections();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}