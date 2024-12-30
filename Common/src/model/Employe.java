package model;

import java.io.Serializable;
import org.mindrot.jbcrypt.BCrypt;

public class Employe implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String nom, prenom, login, motDePasse;

	public Employe() {
		super();
	}

	public Employe(String login, String motDePasse) {
		super();
		this.login = login;
		this.motDePasse = motDePasse;
	}

	public Employe(String nom, String prenom, String login, String motDePasse) {
		super();
		this.nom = nom;
		this.prenom = prenom;
		this.login = login;
		this.motDePasse = motDePasse;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getMotDePasse() {
		return motDePasse;
	}

	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}
	
	public static String hashPassword(String plainPassword) {
	    return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
	}

	public static boolean checkPassword(String plainPassword, String hashedPassword) {
	    return BCrypt.checkpw(plainPassword, hashedPassword);
	}

	@Override
	public String toString() {
		return "Employe [id=" + id + ", nom=" + nom + ", prenom=" + prenom + ", login=" + login + ", motDePasse="
				+ motDePasse + "]";
	}

}
