package src.main.java.client;

import java.io.IOException;

public class Client {
	public static void main(String[] args) {
		ClientConnection clientConnection = new ClientConnection();
		try {
			clientConnection.getCourses();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}