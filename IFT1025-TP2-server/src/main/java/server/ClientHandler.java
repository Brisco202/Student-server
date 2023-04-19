package src.main.java.server;

import src.main.java.server.models.Course;
import src.main.java.server.models.RegistrationForm;
import javafx.util.Pair;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import com.google.gson.Gson;

public class ClientHandler implements Runnable {
	final private Socket client;
	private ObjectOutputStream objectOutputStream;
	private ObjectInputStream objectInputStream;
	/**
	 * Cette constante permet de stocker la commande d'enregistrement d'un étudiant
	 */
	public final static String REGISTER_COMMAND = "INSCRIRE";

	/**
	 * Cette constante permet de stocker la commande pour le chargement des cours
	 */
	public final static String LOAD_COMMAND = "CHARGER";
	final private ArrayList<EventHandler> handlers;

	public ClientHandler(Socket client, ArrayList<EventHandler> handlers) {
		this.client = client;
		this.handlers = handlers;
		this.addEventHandler(this::handleEvents);
	}

	@Override
	public void run() {
		try {
			objectOutputStream = new ObjectOutputStream(client.getOutputStream());
			;
			objectInputStream = new ObjectInputStream(client.getInputStream());
			listen();
//			disconnect();
//			System.out.println("Client déconnecté!");
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Lire un fichier texte contenant des informations sur les cours et les
	 * transofmer en liste d'objets 'Course'. La méthode filtre les cours par la
	 * session spécifiée en argument. Ensuite, elle renvoie la liste des cours pour
	 * une session au client en utilisant l'objet 'objectOutputStream'. La méthode
	 * gère les exceptions si une erreur se produit lors de la lecture du fichier ou
	 * de l'écriture de l'objet dans le flux.
	 * 
	 * @param arg la session pour laquelle on veut récupérer la liste des cours
	 */
	public void handleLoadCourses(String arg) {
		try {
			System.out.println("handleLoadCourses");
			Scanner scanner = new Scanner(new File(
					getClass().getResource("/IFT1025-TP2-server/src/main/java/server/data/cours.txt").getFile()));
			ArrayList<String> cours = new ArrayList<String>();
			String[] parts;
			String data;
			while (scanner.hasNextLine()) {
				data = scanner.nextLine();
				parts = data.split("\t");
				if (parts[2].trim().toLowerCase().equals(arg.trim().toLowerCase())) {
					System.out.println(data);
					cours.add(new Gson().toJson(new Course(parts[1], parts[0], parts[2])));
				}
			}
			System.out.println(cours.size());
			scanner.close();
			try {
				objectOutputStream.writeObject(new Gson().toJson(cours));
//				objectOutputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				try {
					disconnect();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			try {
				disconnect();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	/**
	 * Récupérer l'objet 'RegistrationForm' envoyé par le client en utilisant
	 * 'objectInputStream', l'enregistrer dans un fichier texte et renvoyer un
	 * message de confirmation au client. La méthode gére les exceptions si une
	 * erreur se produit lors de la lecture de l'objet, l'écriture dans un fichier
	 * ou dans le flux de sortie.
	 */
	public void handleRegistration() {
		try {
			System.out.println("register");
			FileWriter writer = new FileWriter(new File(
					getClass().getResource("/IFT1025-TP2-server/src/main/java/server/data/inscription.txt").getFile()),
					true);
			writer.write(System.getProperty("line.separator"));
			try {
				Map<String, Object> usr = new Gson().fromJson((String) objectInputStream.readObject(), Map.class);
				System.out.println("usr " + usr);
				RegistrationForm user = new RegistrationForm((String) usr.get("prenom"), (String) usr.get("nom"),
						(String) usr.get("email"), (String) usr.get("matricule"), null);

				Map<String, String> cours = (Map<String, String>) usr.get("course");
				Course course = new Course(cours.get("name"), cours.get("code"), cours.get("session"));
				user.setCourse(course);
				String vals = user.getCourse().getCode().substring(0, 3) + "-"
						+ user.getCourse().getCode().substring(3, user.getCourse().getCode().length());
				writer.write(
						user.getCourse().getSession() + "\t" + user.getCourse().getCode() + "\t" + user.getMatricule()
								+ "\t" + user.getPrenom() + "\t" + user.getNom() + "\t" + user.getEmail());
				writer.close();
				objectOutputStream.writeObject(
						"Felicitation! Inscription reussie de " + user.getPrenom() + " au cours " + vals + ".");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					objectOutputStream.writeObject("Erreur survenue");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			try {
				objectOutputStream.writeObject("Le fichier d'inscription n'existe pas");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	/**
	 * Cette méthode permet de fermer les differentes écoutes et communications
	 * client/serveur
	 * 
	 * @throws IOException est appelée en cas d'erreur lors de la fermeture des
	 *                     éléments d'écoute
	 */
	public void disconnect() throws IOException {
		objectOutputStream.close();
		objectInputStream.close();
		client.close();
	}

	/**
	 * Cette méthode permet de declencher l'ecoute d'un évènement
	 * 
	 * @throws IOException            est appelée en cas d'erreur lors de la
	 *                                communication client/serveur
	 * @throws ClassNotFoundException est appelée en si l'on ne retrouve pas la
	 *                                classe demandee
	 */
	public void listen() throws IOException, ClassNotFoundException {
		String line;
//		while ((line = this.objectInputStream.readObject().toString()) != null) {
//			Pair<String, String> parts = processCommandLine(line);
//			String cmd = parts.getKey();
//			String arg = parts.getValue();
//			this.alertHandlers(cmd, arg);
//		}
		if ((line = this.objectInputStream.readObject().toString()) != null) {
			Pair<String, String> parts = processCommandLine(line);
			String cmd = parts.getKey();
			String arg = parts.getValue();
			this.alertHandlers(cmd, arg);
		}
	}

	private void alertHandlers(String cmd, String arg) {
		for (EventHandler h : this.handlers) {
			h.handle(cmd, arg);
		}
	}

	/**
	 * Cette méthode retourne un object contenant une commande et un argument
	 * 
	 * @param line permet de recuperer le duo de valeur (commande, argument)
	 * @return
	 */
	public Pair<String, String> processCommandLine(String line) {
		String[] parts = line.split(" ");
		String cmd = parts[0];
		String args = String.join(" ", Arrays.asList(parts).subList(1, parts.length));
		return new Pair<>(cmd, args);
	}

	/**
	 * Cette méthode permet d'enregistrer un étudiant ou de charger les cours
	 * 
	 * @param cmd est la commande a traiter
	 * @param arg est l'argument a traiter
	 */
	public void handleEvents(String cmd, String arg) {
		if (cmd.equals(REGISTER_COMMAND)) {
			handleRegistration();
		} else if (cmd.equals(LOAD_COMMAND)) {
			handleLoadCourses(arg);
		}
	}

	/**
	 * Cette méthode permet d'ajouter un écouteur d'évènement à la liste handlers
	 * 
	 * @param h est un écouteur d'évènement
	 */
	public void addEventHandler(EventHandler h) {
		this.handlers.add(h);
	}
}
