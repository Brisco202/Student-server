package src.main.java.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Classe serveur
 *
 */
public class Server {
	private final ServerSocket server;
	private Socket client;
	private final ArrayList<EventHandler> handlers;

	/**
	 * Cette méthode a pour but d'initialiser les differentes sockets du projet et
	 * les ecouteurs
	 * 
	 * @param port est le port du serveur
	 * @throws IOException est appele en cas d'erreur lors de la connexion sur le
	 *                     socket serveur
	 */
	public Server(int port) throws IOException {
		this.server = new ServerSocket(port, 1);
		this.server.setReuseAddress(true);
		this.handlers = new ArrayList<EventHandler>();
	}

	/**
	 * Cette méthode permet de gérer la connexion client/serveur, d'initialiser les
	 * objets pour gérer la communication client/serveur, d'écouter un évènement,
	 * enfin de déconnecter les differents éléments qui gerent la communication
	 * client/serveur
	 */
	public void run() {
		while (true) {
			try {
				client = server.accept();
				System.out.println("Connecté au client: " + client);
				ClientHandler ch = new ClientHandler(client, handlers);
				;
				new Thread(ch).start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
