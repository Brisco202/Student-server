package src.main.java.client;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

import src.main.java.models.Course;
import src.main.java.models.RegistrationForm;

import com.google.gson.Gson;

public class ClientConnection {
	private Socket clientSocket;
	private ObjectInputStream objectInputStream;
	private ObjectOutputStream objectOutputStream;
	private Course coursChoisi = new Course();
	private ArrayList<Course> cours = new ArrayList<Course>();
	private Scanner sc = new Scanner(System.in);
	private String choice;

	public ClientConnection() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void connect() throws UnknownHostException, IOException {
		clientSocket = new Socket(InetAddress.getLocalHost().getHostAddress(), 1337);
		objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
		objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
//		closeAll();
	}

	public void getCourses() throws IOException, ClassNotFoundException, EOFException {
		connect();
		chooseCourse();
//		closeAll();

	}

	private void chooseCourse() throws IOException, ClassNotFoundException, EOFException {
		System.out.print("*** Bienvenue au portail d'inscription de cours de l'UDEM ***\n"
				+ "Veuillez choisir la session pour laquelle vous voulez consulter la liste des cours:\n"
				+ "1. Automne\n" + "2. Hiver\n" + "3. Ete\n" + "> Choix: ");
		choice = sc.nextLine();
		switch (choice) {
		case "1": {
			System.out.println("Les cours offerts pendant la session d'automne sont:");
			getSpecificCourse("Automne");
			break;
		}
		case "2": {
			System.out.println("Les cours offerts pendant la session d'hiver sont:");
			getSpecificCourse("Hiver");
			break;
		}
		case "3": {
			System.out.println("Les cours offerts pendant la session d'ete sont:");
			getSpecificCourse("Ete");
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: ");
		}

		System.out.println("> Choix:");
		registerUser();
	}

	private void registerUser() throws IOException, ClassNotFoundException, EOFException {
		connect();
		System.out.println("1. Consulter les cours offerts pour une autre session");
		System.out.println("2. Inscription à un cours");
		System.out.print("> Choix: ");
		choice = sc.nextLine();
		switch (choice) {
		case "1": {
			chooseCourse();
			break;
		}
		case "2": {
			RegistrationForm user = new RegistrationForm();
			System.out.print("Veuillez saisir votre prenom: ");
			user.setPrenom(sc.nextLine());
			System.out.print("Veuillez saisir votre nom: ");
			user.setNom(sc.nextLine());
			System.out.print("Veuillez saisir votre email: ");
			user.setEmail(sc.nextLine());
			System.out.print("Veuillez saisir votre matricule: ");
			user.setMatricule(sc.nextLine());
			System.out.print("Veuillez saisir le code du cours: ");
			String code = sc.nextLine();
			for (Course course : cours) {
				if (course.getCode().toUpperCase().equals(code.toUpperCase())) {
					coursChoisi = course;
				}
			}
			user.setCourse(coursChoisi);
			objectOutputStream.writeObject("INSCRIRE " + " " + " ");
			objectOutputStream.writeObject(new Gson().toJson(user));
			System.out.print(objectInputStream.readObject());
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value:");
		}

		closeAll();
	}

	private void getSpecificCourse(String arg) throws IOException, ClassNotFoundException {
		objectOutputStream.writeObject("CHARGER " + arg + " ");
		var a = new Gson().fromJson(objectInputStream.readObject().toString(), ArrayList.class);
		Course c;
		int index = 1;

		for (Object course : a) {
			Map<String, String> cour = new Gson().fromJson((String) course, Map.class);
			c = new Course(cour.get("name"), cour.get("code"), cour.get("session"));
			cours.add(c);
			System.out.println(index + ". " + c.getCode() + "\t" + c.getName());
			index++;
		}
		closeAll();
	}

	public void closeAll() throws IOException {
		objectInputStream.close();
		objectOutputStream.close();
		clientSocket.close();
	}
}