/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.Produit;
import model.RequestContext;
import serviceRMI.Client;
import dao.GenericDao;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 */
public class AddProduitController implements Initializable {

	@FXML
	private TextField nomField;
	@FXML
	private ComboBox<String> categorieCombo;
	@FXML
	private ToggleGroup genre;
	@FXML
	private TextField quantiteField;
	@FXML
	private TextField prixField;
	@FXML
	private Text textPopup;
	@FXML
	private Button textBtn;

	private boolean modifier;
	@FXML
	private VBox popup;

	int idProduit;
	
	static int employeId;
	
	GenericDao<Produit, Integer> produitStub = Client.getProduitStub();

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeComboBox();
	}

	@FXML
	private void annuler() {
		Stage stage = (Stage) popup.getScene().getWindow();
		stage.close();
	}

	// Static categories initialization
	private void initializeComboBox() {
		ObservableList<String> categories = FXCollections.observableArrayList("Informatique", "Accessoires", "Mobilier", "Téléphonie");
		categorieCombo.setItems(categories); // Set the list of categories
	}

	@FXML
	private void ajouter(MouseEvent event) {
		try {
			// Validate fields
			if (!validateFields()) {
				return; // Stop execution if validation fails
			}

			// Retrieve values
			String nom = nomField.getText();
			String categorie = (String) categorieCombo.getValue(); // Get the selected value from the ComboBox
			if (categorie == null || categorie.isEmpty()) {
				// Handle the case where no category is selected
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setHeaderText(null);
				alert.setContentText("Veuillez sélectionner une catégorie !");
				alert.showAndWait();
				return; // Stop execution if category is not selected
			}

			int quantite = Integer.parseInt(quantiteField.getText());
			double prix = Double.parseDouble(prixField.getText());

			// Process the product
			editer(new Produit(idProduit, nom, categorie, quantite, prix));

			annuler();

		} catch (NumberFormatException e) {
			// Handle invalid number input
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText("Veuillez entrer des valeurs numériques valides pour la quantité et le prix !");
			alert.showAndWait();

			Logger.getLogger(AddProduitController.class.getName()).log(Level.SEVERE, null, e);
		} catch (Exception e) {
			Logger.getLogger(AddProduitController.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	// Separate method to validate fields
	private boolean validateFields() {
		if (nomField.getText().isEmpty()) {
			showErrorAlert("Le champ 'Nom' est obligatoire !");
			nomField.requestFocus();
			return false;
		}
	    if (categorieCombo.getValue() == null || categorieCombo.getValue().isEmpty()) {
	        showErrorAlert("Le champ 'Catégorie' est obligatoire !");
	        categorieCombo.requestFocus();
	        return false;
	    }
		if (quantiteField.getText().isEmpty()) {
			showErrorAlert("Le champ 'Quantité' est obligatoire !");
			quantiteField.requestFocus();
			return false;
		}
		if (prixField.getText().isEmpty()) {
			showErrorAlert("Le champ 'Prix' est obligatoire !");
			prixField.requestFocus();
			return false;
		}
		return true;
	}

	// Helper method to show an error alert
	private void showErrorAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	public void setEmploye(int id) {
		employeId = id;
	}

	private void editer(Produit produit) {
		try {
			RequestContext context = new RequestContext();
			context.setEmployeId(employeId);

			if (modifier == false) {
				produitStub.create(produit, context);
			} else {
				produitStub.update(produit, context);
			}
		} catch (Exception e) {
			Logger.getLogger(AddProduitController.class.getName()).log(Level.SEVERE, null, e);
		}

		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setHeaderText(null);
		alert.setContentText(
				"Les modifications sont bien enregistré !");
		alert.showAndWait();

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ListeProduitsView.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			ListeProduitsController listeProduitsController = loader.getController();
			listeProduitsController.setTableItems();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void setTextField(int id, String nom, String categorie, int quantite, Double prix) {
		idProduit = id;
		nomField.setText(nom);
	    categorieCombo.setValue(categorie);
		quantiteField.setText(String.valueOf(quantite));
		prixField.setText(String.valueOf(prix));
	}

	void setTextBtn(String text) {
		textBtn.setText(text);
	}

	void setTextLabel(String text) {
		textPopup.setText(text);
	}

	void setUpdate(boolean b) {
		this.modifier = b;
	}
}
