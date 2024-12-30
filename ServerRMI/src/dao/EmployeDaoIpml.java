package dao;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import config.DataSource;
import model.Employe;
import model.RequestContext;

public class EmployeDaoIpml extends UnicastRemoteObject implements GenericDao<Employe, Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LogManager.getLogger(EmployeDaoIpml.class.getName());

	public EmployeDaoIpml() throws RemoteException {
		super();
	}

	@Override
	public List<Employe> findAll() throws RemoteException {
		List<Employe> employes = new ArrayList<>();
		try {
			Connection c = DataSource.getConnection();
			PreparedStatement ps = c.prepareStatement("SELECT * FROM employes");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Employe employe = new Employe();
				employe.setId(rs.getInt("id"));
				employe.setNom(rs.getString("nom"));
				employe.setPrenom(rs.getString("prenom"));
				employe.setLogin(rs.getString("login"));
				employe.setMotDePasse(rs.getString("mot_de_passe"));
				employes.add(employe);
			}
		} catch (SQLException e) {
			logger.error("LOGGER ERROR - Method findAll : {}", e);
		}

		return employes;
	}

	@Override
	public Employe findById(Integer id) throws RemoteException {
		try {
			Connection c = DataSource.getConnection();

			PreparedStatement ps = c.prepareStatement("SELECT * FROM employes WHERE id = ?");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				Employe employe = new Employe();
				employe.setId(rs.getInt("id"));
				employe.setNom(rs.getString("nom"));
				employe.setPrenom(rs.getString("prenom"));
				employe.setLogin(rs.getString("login"));
				employe.setMotDePasse(rs.getString("mot_de_passe"));
				return employe;
			}
		} catch (SQLException e) {
			logger.error("LOGGER ERROR - Method findById : {}", e);
		}
		return null;
	}

	@Override
	public Employe create(Employe employe, RequestContext context) throws RemoteException {
		try {
			Connection c = DataSource.getConnection();

			PreparedStatement ps = c.prepareStatement(
					"INSERT INTO employes (nom,prenom,login,mot_de_passe) VALUES (?,?,?,?); ",
					PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setString(1, employe.getNom());
			ps.setString(2, employe.getPrenom());
			ps.setString(3, employe.getLogin());
			ps.setString(4, Employe.hashPassword(employe.getMotDePasse())); // Hash the password
			ps.executeUpdate();
			ResultSet resultat = ps.getGeneratedKeys();
			if (resultat.next()) {
				employe.setId(resultat.getInt(1));

				logger.info("L'employé a été ajouté avec succès !");

				return employe;
			}
		} catch (SQLException e) {
			logger.error("LOGGER ERROR - Method save : {}", e);
		}
		return null;
	}

	@Override
	public Employe update(Employe employe, RequestContext context) throws RemoteException {
		try {
			Connection c = DataSource.getConnection();

			PreparedStatement ps = c.prepareStatement(
					"UPDATE employes SET nom = ?, prenom = ?, login = ?, mot_de_passe = ? WHERE id = ?");
			ps.setString(1, employe.getNom());
			ps.setString(2, employe.getPrenom());
			ps.setString(3, employe.getLogin());
			ps.setString(4, Employe.hashPassword(employe.getMotDePasse())); // Hash the password
			ps.setInt(5, employe.getId());
			int rowsUpdated = ps.executeUpdate();
			if (rowsUpdated > 0) {
				logger.info("L'employé a été modifié avec succès !");
				return employe;
			}
		} catch (SQLException e) {
			logger.error("LOGGER ERROR - Method update : {}", e);
		}
		return null;
	}

	@Override
	public boolean remove(Integer id, RequestContext context) throws RemoteException {
		try {
			Connection c = DataSource.getConnection();
			PreparedStatement ps = c.prepareStatement("DELETE FROM employes WHERE id = ?");
			ps.setInt(1, id);
			int rowsDeleted = ps.executeUpdate();

			logger.info("L'employé a été supprimé avec succès !");
			return rowsDeleted > 0;
		} catch (SQLException e) {
			logger.error("LOGGER ERROR - Method remove : {}", e);
		}
		return false;
	}

	@Override
	public List<Employe> findByCritere(String critere, String valeur) throws RemoteException {
		List<Employe> employes = new ArrayList<>();

		try {
			Connection c = DataSource.getConnection();

			String query = "SELECT * FROM employes WHERE " + critere + " LIKE ?";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setString(1, "%" + valeur + "%"); // Use LIKE with wildcard search
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Employe employe = new Employe();
				employe.setId(rs.getInt("id"));
				employe.setNom(rs.getString("nom"));
				employe.setPrenom(rs.getString("prenom"));
				employe.setLogin(rs.getString("login"));
				employe.setMotDePasse(rs.getString("mot_de_passe"));
				employes.add(employe); // Add each found employee to the list
			}
		} catch (SQLException e) {
			logger.error("LOGGER ERROR - Method findByCritere : {}", e);
		}
	    return employes; // Return the list of employees
	}

}
