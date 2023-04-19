package src.main.java.server;

/**
 * Interface qui permet l'ecoute d'un evenement
 */
@FunctionalInterface
public interface EventHandler {
	void handle(String cmd, String arg);
}
