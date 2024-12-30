package dao;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import config.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import model.Produit;
import model.RequestContext;

import org.apache.logging.log4j.ThreadContext;

public class ProduitDaoImpl extends UnicastRemoteObject implements GenericDao<Produit, Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LogManager.getLogger(ProduitDaoImpl.class.getName());

	public ProduitDaoImpl() throws RemoteException {
		super();
	}

	@Override
	public List<Produit> findAll() throws RemoteException {
		List<Produit> produits = new ArrayList<>();
		try {
			Connection c = DataSource.getConnection();
			PreparedStatement ps = c.prepareStatement("SELECT * FROM produits");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Produit produit = new Produit();
				produit.setId(rs.getInt("id"));
				produit.setNom(rs.getString("nom"));
				produit.setCategorie(rs.getString("categorie"));
				produit.setQuantite(rs.getInt("quantite"));
				produit.setPrix(rs.getDouble("prix"));
				produits.add(produit);
			}
		} catch (SQLException e) {
			logger.error("LOGGER ERROR - Method findAll : {}", e);
		}

		return produits;
	}

	@Override
	public Produit findById(Integer id) throws RemoteException {
		try {
			Connection c = DataSource.getConnection();

			PreparedStatement ps = c.prepareStatement("SELECT * FROM produits WHERE id = ?");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				Produit produit = new Produit();
				produit.setId(rs.getInt("id"));
				produit.setNom(rs.getString("nom"));
				produit.setCategorie(rs.getString("categorie"));
				produit.setQuantite(rs.getInt("quantite"));
				produit.setPrix(rs.getDouble("prix"));
				return produit;
			}
		} catch (SQLException e) {
			logger.error("LOGGER ERROR - Method findById : {}", e);
		}
		return null;
	}

	@Override
	public Produit create(Produit produit, RequestContext context) throws RemoteException {
		try {
			Connection c = DataSource.getConnection();

			PreparedStatement ps = c.prepareStatement(
					"INSERT INTO produits (nom,categorie,quantite,prix) VALUES (?,?,?,?); ",
					PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setString(1, produit.getNom());
			ps.setString(2, produit.getCategorie());
			ps.setInt(3, produit.getQuantite());
			ps.setDouble(4, produit.getPrix());
			ps.executeUpdate();
			ResultSet resultat = ps.getGeneratedKeys();
			if (resultat.next()) {
				produit.setId(resultat.getInt(1));

				ThreadContext.put("action", "CREATE");
				ThreadContext.put("produit_id", String.valueOf(resultat.getInt(1)));
				System.out.println(context.getEmployeId());
				ThreadContext.put("employe_id", String.valueOf(context.getEmployeId()));

				logger.info("Le produit a été ajouté avec succès !");

				return produit;
			}
		} catch (SQLException e) {
			logger.error("LOGGER ERROR - Method save : {}", e);
		} finally {
			ThreadContext.clearAll();
		}
		return null;
	}

	@Override
	public Produit update(Produit produit, RequestContext context) throws RemoteException {
		try {
			Connection c = DataSource.getConnection();

			PreparedStatement ps = c.prepareStatement(
					"UPDATE produits SET nom = ?, categorie = ?, quantite = ?, prix = ? WHERE id = ?");
			ps.setString(1, produit.getNom());
			ps.setString(2, produit.getCategorie());
			ps.setInt(3, produit.getQuantite());
			ps.setDouble(4, produit.getPrix());
			ps.setInt(5, produit.getId());
			int rowsUpdated = ps.executeUpdate();
			if (rowsUpdated > 0) {
				ThreadContext.put("action", "UPDATE");
				ThreadContext.put("produit_id", String.valueOf(produit.getId()));
				ThreadContext.put("employe_id", String.valueOf(context.getEmployeId()));

				logger.info("Le produit a été modifié avec succès !");
				return produit;
			}
		} catch (SQLException e) {
			logger.error("LOGGER ERROR - Method update : {}", e);
		} finally {
			ThreadContext.clearAll();
		}
		return null;
	}

	@Override
	public boolean remove(Integer id, RequestContext context) throws RemoteException {
		try {
			Connection c = DataSource.getConnection();
			PreparedStatement ps = c.prepareStatement("DELETE FROM produits WHERE id = ?");
			ps.setInt(1, id);
			int rowsDeleted = ps.executeUpdate();

			ThreadContext.put("action", "DELETE");
			ThreadContext.put("produit_id", String.valueOf(id));
			ThreadContext.put("employe_id", String.valueOf(context.getEmployeId()));

			logger.info("Le produit a été supprimé avec succès !");
			return rowsDeleted > 0;
		} catch (SQLException e) {
			logger.error("LOGGER ERROR - Method remove : {}", e);
		} finally {
			ThreadContext.clearAll();
		}
		return false;
	}

	@Override
	public List<Produit> findByCritere(String critere, String valeur) throws RemoteException {
		List<Produit> produits = new ArrayList<>();

		try {
			Connection c = DataSource.getConnection();

			String query = "SELECT * FROM produits WHERE " + critere + " LIKE ?";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setString(1, "%" + valeur + "%");
			ResultSet rs = ps.executeQuery();

			// Iterate through the result set and add products to the list
			while (rs.next()) {
				Produit produit = new Produit();
				produit.setId(rs.getInt("id"));
				produit.setNom(rs.getString("nom"));
				produit.setCategorie(rs.getString("categorie"));
				produit.setQuantite(rs.getInt("quantite"));
				produit.setPrix(rs.getDouble("prix"));
				produits.add(produit);
			}
		} catch (SQLException e) {
			logger.error("LOGGER ERROR - Method findByCritere : {}", e);
			throw new RemoteException("Error while searching by criteria: " + e.getMessage());
		}

		return produits; // Return the list of matching products
	}

}
