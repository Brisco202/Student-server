package src.main.java.server;

/**
 * Classe principale. elle lance le projet
 */
public class ServerLauncher {
	/**
	 * Constante permettant de stocker le numero de port du serveur
	 */
	public final static int PORT = 1337;

	/**
	 * Cette méthode permet de lancer le serveur
	 * 
	 * @param args sont des arguments
	 */
	public static void main(String[] args) {
		Server server;
		try {
			server = new Server(PORT);
			System.out.println("Server is running...");
			server.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}