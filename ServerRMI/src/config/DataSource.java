package config;

import java.sql.Connection;
import java.sql.SQLException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DataSource {
	// Une seule instance de HikariDataSource
	private static HikariDataSource ds;

	// Initialisation du HikariCP via un bloc statique
	static {
		try {
			HikariConfig config = new HikariConfig("resources/hikari.properties");
			ds = new HikariDataSource(config);
		} catch (Exception e) {
			throw new RuntimeException("Erreur d'initialisation de la source de données", e);
		}
	}

	private DataSource() {
		// Constructeur privé pour empêcher l'instanciation
	}

	// Retourne une connexion depuis le pool
	public static Connection getConnection() throws SQLException {
		return ds.getConnection();
	}

	// Méthode pour fermer proprement la datasource (lors de l'arrêt de
	// l'application)
	public static void closeDataSource() {
		if (ds != null) {
			ds.close();
		}
	}
}