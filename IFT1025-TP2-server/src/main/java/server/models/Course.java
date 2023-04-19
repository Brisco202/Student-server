package src.main.java.server.models;

import java.io.Serializable;

/**
 * 
 * Modele des cours
 */
public class Course implements Serializable {

	private String name;
	private String code;
	private String session;

	/**
	 * Cette méthode est le constructeur de la classe et permet d'initialiser les
	 * differentes variables
	 * 
	 * @param name    est le nom du cours
	 * @param code    est le code du cours
	 * @param session est la session du cours
	 */
	public Course(String name, String code, String session) {
		this.name = name;
		this.code = code;
		this.session = session;
	}

	/**
	 * Cette méthode retourne le nom d'un cours
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Cette méthode initialise le nom d'un cours
	 * 
	 * @param name est le nom du cours
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Cette méthode retourne le code d'un cours
	 * 
	 * @return code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Cette méthode initialise le code d'un cours
	 * 
	 * @param code est le code d'un cours
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Cette méthode retourne la session d'un cours
	 * 
	 * @return session
	 */
	public String getSession() {
		return session;
	}

	/**
	 * Cette méthode initialise la session d'un cours
	 * 
	 * @param session est la session d'un cours
	 */
	public void setSession(String session) {
		this.session = session;
	}

	/**
	 * Cette méthode retourne l'objet d'un cours sous forme de chaine de caractères
	 */
	@Override
	public String toString() {
		return "Course{" + "name=" + name + ", code=" + code + ", session=" + session + '}';
	}
}
