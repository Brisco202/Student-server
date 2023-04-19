package src.main.java.server.models;

import java.io.Serializable;

/**
 * Modele des enregistrements
 *
 */
public class RegistrationForm implements Serializable {
	private String prenom;
	private String nom;
	private String email;
	private String matricule;
	private Course course;

	/**
	 * Cette méthode est le constructeur de la classe et permet d'initialiser les
	 * differentes variables
	 * 
	 * @param prenom    est le prenom d'un étudiant
	 * @param nom       est le nom d'un étudiant
	 * @param email     est l'email d'un étudiant
	 * @param matricule est le matricule d'un étudiant
	 * @param course    est le cours d'un étudiant
	 */
	public RegistrationForm(String prenom, String nom, String email, String matricule, Course course) {
		this.prenom = prenom;
		this.nom = nom;
		this.email = email;
		this.matricule = matricule;
		this.course = course;
	}

	/**
	 * Cette méthode retourne le prenom d'un étudiant
	 * 
	 * @return prenom
	 */
	public String getPrenom() {
		return prenom;
	}

	/**
	 * Cette méthode initialise le prenom d'un étudiant
	 * 
	 * @param prenom est le prenom d'un étudiant
	 */
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	/**
	 * Cette méthode retourne le nom d'un étudiant
	 * 
	 * @return nom
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * Cette méthode initialise le nom d'un étudiant
	 * 
	 * @param nom est le nom d'un étudiant
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	 * Cette méthode retourne l'email d'un étudiant
	 * 
	 * @return email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Cette méthode initialise l'email d'un étudiant
	 * 
	 * @param email est l'email d'un étudiant
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Cette méthode retourne le matricule d'un étudiant
	 * 
	 * @return matricule
	 */
	public String getMatricule() {
		return matricule;
	}

	/**
	 * Cette méthode initialise le matricule d'un étudiant
	 * 
	 * @param matricule est le matricule d'un étudiant
	 */
	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}

	/**
	 * Cette méthode retourne le cours d'un étudiant
	 * 
	 * @return course
	 */
	public Course getCourse() {
		return course;
	}

	/**
	 * Cette méthode initialise le cours d'un étudiant
	 * 
	 * @param course est le cours d'un étudiant
	 */
	public void setCourse(Course course) {
		this.course = course;
	}

	/**
	 * Cette méthode retourne l'objet d'un enregistrement sous forme de chaine de
	 * caractères
	 */
	@Override
	public String toString() {
		return "InscriptionForm{" + "prenom='" + prenom + '\'' + ", nom='" + nom + '\'' + ", email='" + email + '\''
				+ ", matricule='" + matricule + '\'' + ", course='" + course + '\'' + '}';
	}
}
