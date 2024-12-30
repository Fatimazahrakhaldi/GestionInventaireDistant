package controller;

import model.Employe;
import serviceRMI.Client;

import helper.AlertHelper;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;

public class LoginController implements Initializable {

	@FXML
	private TextField username;

	@FXML
	private TextField password;

	@FXML
	private Button loginButton;

	Window window;

	@Override
	public void initialize(URL url, ResourceBundle rb) {

	}

	@FXML
	private void login() throws Exception {

		if (this.isValidated()) {

			try {
			    List<Employe> employes = Client.getEmployeStub().findByCritere("login", username.getText());

			    if (employes != null && !employes.isEmpty()) {
			    	// Assume the first result is the one we need
			        Employe employe = employes.get(0); 
			        
					boolean isAuthenticated = Employe.checkPassword(password.getText(), employe.getMotDePasse());
					if (isAuthenticated) {
						
						FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ListeProduitsView.fxml"));
						(new Stage()).setScene(new Scene(loader.load()));
						
						ListeProduitsController listeProduitsController = loader.getController();
						listeProduitsController.setEmploye(employe);
												
						Stage stage = (Stage) loginButton.getScene().getWindow();
						stage.close();

						Parent root = FXMLLoader.load(getClass().getResource("/view/ListeProduitsView.fxml"));
						Scene scene = new Scene(root);
						stage.setScene(scene);
						stage.setTitle("Gestion d'inventaire");
						stage.getIcons().add(new Image("/view/icon.png"));
						stage.show();
					}

				} else {
					AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error", "Invalid username and password.");
					username.requestFocus();
				}
			} catch (Exception ex) {
				System.out.println(ex);
			}
		}
	}

	private boolean isValidated() {

		window = loginButton.getScene().getWindow();
		if (username.getText().equals("")) {
			AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error", "Username text field cannot be blank.");
			username.requestFocus();
		} else if (username.getText().length() < 5 || username.getText().length() > 25) {
			AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
					"Username text field cannot be less than 5 and greator than 25 characters.");
			username.requestFocus();
		} else if (password.getText().equals("")) {
			AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error", "Password text field cannot be blank.");
			password.requestFocus();
		} else if (password.getText().length() < 5 || password.getText().length() > 25) {
			AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
					"Password text field cannot be less than 5 and greator than 25 characters.");
			password.requestFocus();
		} else {
			return true;
		}
		return false;
	}

	@FXML
	private void showRegisterStage() throws IOException {
		Stage stage = (Stage) loginButton.getScene().getWindow();
		stage.close();

		Parent root = FXMLLoader.load(getClass().getResource("/view/RegisterView.fxml"));

		Scene scene = new Scene(root);

		stage.setScene(scene);
		stage.setTitle("User Registration");
		stage.getIcons().add(new Image("icon.png"));
		stage.show();
	}
}
