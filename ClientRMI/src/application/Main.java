package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import serviceRMI.Client;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

/**
 * Classe principale de l'application JavaFX. Cette classe initialise le client
 * RMI et lance l'interface utilisateur (LoginView).
 */
public class Main extends Application {

	/**
	 * Méthode principale de l'application. Elle initialise le client RMI pour la
	 * communication distante et lance l'application JavaFX.
	 *
	 * @param args Les arguments passés à l'application.
	 */
	public static void main(String[] args) {
		// Initialisation du client RMI pour établir la connexion avec les services
		// distants
		Client.initialize();

		// Lancement de l'application JavaFX
		launch(args);
	}

	/**
	 * Méthode appelée au démarrage de l'application JavaFX. Elle charge la vue
	 * initiale définie dans le fichier FXML et configure la fenêtre principale.
	 *
	 * @param stage La fenêtre principale de l'application.
	 */
	@Override
	public void start(Stage stage) {
		try {
			// Chargement du fichier FXML pour la vue de connexion (LoginView)
			Parent root = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"));

			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle("Gestion d'inventaire");
			stage.getIcons().add(new Image("/view/icon.png"));
			stage.setResizable(true);
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
