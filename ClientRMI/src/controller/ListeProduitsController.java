package controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import dao.GenericDao;
import serviceRMI.Client;
import model.Employe;
import model.Produit;
import model.RequestContext;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ListeProduitsController implements Initializable {

	@FXML
	private AnchorPane anchorpane;
	@FXML
	private TableView<Produit> TableProduit;
	@FXML
	private TableColumn<Produit, Integer> idColumn;
	@FXML
	private TableColumn<Produit, String> nomColumn;
	@FXML
	private TableColumn<Produit, String> categorieColumn;
	@FXML
	private TableColumn<Produit, Integer> quantiteColumn;
	@FXML
	private TableColumn<Produit, Double> prixColumn;
	@FXML
	private TableColumn<Produit, String> editColumn;
	@FXML
	private ComboBox<Produit> coboSearchProduit;
	@FXML
	private Label username;
	@FXML
	private TextField textField;
	@FXML
	private Text textPopup;

	static String employeName;
	static int employeId;

	GenericDao<Produit, Integer> produitStub = Client.getProduitStub();
	ObservableList<Produit> listeProduits = FXCollections.observableArrayList();

	// initialisation ( set data )
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		loadData();
		username.setText(employeName);
		setValuesToComboCategorie();
	}

	// Lancer la pop up d'ajout produit
	@FXML
	private void getAddView(MouseEvent event) {
		try {
			Parent parent = FXMLLoader.load(getClass().getResource("/view/addProduitView.fxml"));
			Scene scene = new Scene(parent);
			Stage dialog = new Stage();
			dialog.setScene(scene);
			dialog.initModality(Modality.APPLICATION_MODAL);
			dialog.getIcons().add(new Image("/view/icon.png"));
			dialog.show();
		} catch (IOException e) {
			Logger.getLogger(ListeProduitsController.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	// Remplir le tableau
	@FXML
	public void refreshTable() {
		try {
			List<Produit> produits;
			listeProduits.clear();
			String nom = textField.getText();
			if (!nom.equals("")) {
				produits = produitStub.findByCritere("nom", nom);
			} else {
				produits = produitStub.findAll();
			}

			for (Produit p : produits) {
				listeProduits.add(new Produit(p.getId(), p.getNom(), p.getCategorie(), p.getQuantite(), p.getPrix()));
				TableProduit.setItems(listeProduits);
			}
		} catch (Exception e) {
			Logger.getLogger(ListeProduitsController.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	// Rechercher par Code client
	@FXML
	public void comboChanged(ActionEvent event) {
		if (coboSearchProduit.getValue() != null) {
			ObservableList<Produit> listeProduits;

			try {
				listeProduits = FXCollections.observableArrayList(
						produitStub.findByCritere("categorie", coboSearchProduit.getValue().getCategorie()));

				TableProduit.setItems(listeProduits);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	// Charger les donnees du tableau
	private void loadData() {
		refreshTable();

		idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
		categorieColumn.setCellValueFactory(new PropertyValueFactory<>("categorie"));
		quantiteColumn.setCellValueFactory(new PropertyValueFactory<>("quantite"));

		// Add color styling for the 'quantiteColumn'
		quantiteColumn.setCellFactory(column -> new TableCell<Produit, Integer>() {
			@Override
			protected void updateItem(Integer item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setText(null);
					setStyle("");
				} else {
					setText(String.valueOf(item));

					// Apply color based on stock level
					if (item <= 5) {
						setStyle("-fx-text-fill: red; -fx-font-weight: bold;"); // Critical level
					} else if (item <= 10) {
						setStyle("-fx-text-fill: #ff5c00;  -fx-font-weight: bold;"); // Low stock
					} else {
						setStyle("-fx-text-fill: green;"); // Sufficient stock
					}
					// Center-align the text
					setAlignment(Pos.CENTER);
				}
			}
		});

		// Update the prixColumn to display price followed by "MAD"
		prixColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));
		prixColumn.setCellFactory(param -> new TableCell<Produit, Double>() {
			@Override
			protected void updateItem(Double item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setText(null);
				} else {
					// Format the price to include "MAD"
					setText(String.format("%.2f MAD", item));
				}
			}
		});

		// add cell of button edit
		Callback<TableColumn<Produit, String>, TableCell<Produit, String>> cellFoctory1 = (
				TableColumn<Produit, String> param) -> {
			// make cell containing buttons
			final TableCell<Produit, String> cell1 = new TableCell<Produit, String>() {
				@Override
				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					// that cell created only on non-empty rows
					if (empty) {
						setGraphic(null);
						setText(null);
					} else {
						Button deleteIcon1 = new Button("Supprimer");
						Button editIcon1 = new Button("Modifier");

						editIcon1.setId("edittest");

						deleteIcon1.setOnMouseClicked((MouseEvent event) -> {

							try {
								Produit produit = (Produit) getTableRow().getItem();
								// allow user to decide between yes and no
								Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
								alert.setTitle("Confirmation suppression");
								alert.setHeaderText(null);
								alert.setContentText("Etes-vous sûr de vouloir supprimer ce produit ?");
								Optional<javafx.scene.control.ButtonType> action = alert.showAndWait();

								if (action.get() == javafx.scene.control.ButtonType.OK) {
									RequestContext context = new RequestContext();
									context.setEmployeId(employeId);
									produitStub.remove(produit.getId(), context);
									refreshTable();
								}

							} catch (Exception e) {
								Logger.getLogger(ListeProduitsController.class.getName()).log(Level.SEVERE, null, e);
							}
						});

						editIcon1.setOnMouseClicked((MouseEvent event) -> {

							Produit produit = (Produit) getTableRow().getItem();

							FXMLLoader loader1 = new FXMLLoader();
							loader1.setLocation(getClass().getResource("/view/addProduitView.fxml"));
							
							try {
								loader1.load();

								AddProduitController addClientController = loader1.getController();

								addClientController.setUpdate(true);
								addClientController.setTextField(produit.getId(), produit.getNom(),
										produit.getCategorie(), produit.getQuantite(), produit.getPrix());

								addClientController.setTextBtn("Modifier");
								addClientController.setTextLabel("Modifier produit");

								FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/addProduitView.fxml"));
								(new Stage()).setScene(new Scene(loader.load()));
								AddProduitController addProduitController = loader.getController();
								addProduitController.setEmploye(employeId);
								
								Parent parent1 = loader1.getRoot();
								final Stage dialog = new Stage();
								dialog.initModality(Modality.APPLICATION_MODAL);
								dialog.initOwner(new Stage());
								dialog.getIcons().add(new Image("/view/icon.png"));
								dialog.setScene(new Scene(parent1));
								dialog.show();
							} catch (IOException e) {
								Logger.getLogger(ListeProduitsController.class.getName()).log(Level.SEVERE, null, e);
							}
						});

						HBox managebtn1 = new HBox(editIcon1, deleteIcon1);
						managebtn1.setStyle("-fx-alignment:center");
						HBox.setMargin(deleteIcon1, new Insets(2, 2, 0, 3));
						HBox.setMargin(editIcon1, new Insets(2, 3, 0, 2));

						setGraphic(managebtn1);

						setText(null);
					}
				}
			};
			return cell1;
		};

		editColumn.setCellFactory(cellFoctory1);
		TableProduit.setItems(listeProduits);
	}

	public void setTableItems() {

//		ObservableList<Produit> listeProduits;
//
//		try {
//			listeProduits = FXCollections.observableArrayList(
//					produitStub.findAll());
//			Logger.getLogger(ListeProduitsController.class.getName()).info("setTableItems listproduit ldakhl");
//
//			TableProduit.setItems(listeProduits);
//		} catch (RemoteException e) {
//			e.printStackTrace();
//		}
		refreshTable();
//		try {
//			List<Produit> produits;
//			listeProduits.clear();
//			String nom = textField.getText();
//			if (!nom.equals("")) {
//				produits = produitStub.findByCritere("nom", nom);
//			} else {
//				produits = produitStub.findAll();
//			}
//
//			for (Produit p : produits) {
//				listeProduits.add(new Produit(p.getId(), p.getNom(), p.getCategorie(), p.getQuantite(), p.getPrix()));
//				TableProduit.setItems(listeProduits);
//			}
//		} catch (Exception e) {
//			Logger.getLogger(ListeProduitsController.class.getName()).log(Level.SEVERE, null, e);
//		}
	}

	public void setEmploye(Employe employe) {
		employeName = employe.getPrenom();
		employeId = employe.getId();
	}

	public void setValuesToComboCategorie() {
		// Create a static list of 'Produit' objects with categories
		ObservableList<Produit> produits = FXCollections.observableArrayList(new Produit("Accessoires"),
				new Produit("Informatique"), new Produit("Mobilier"), new Produit("Téléphonie"));

		// Set the items for the ComboBox
		coboSearchProduit.setItems(produits);

		// Set the cell factory to display the category name
		coboSearchProduit.setCellFactory(param -> new ListCell<Produit>() {
			@Override
			protected void updateItem(Produit item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getCategorie()); // Display the category name
			}
		});

		// Set the button cell to show the category name
		coboSearchProduit.setButtonCell(new ListCell<Produit>() {
			@Override
			protected void updateItem(Produit item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "Choisir une catégorie" : item.getCategorie()); // Display category when selected
			}
		});
	}

	@FXML
	private void close() throws IOException {
		Stage stage = (Stage) anchorpane.getScene().getWindow();
		stage.close();
		Parent root = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Gestion d'inventaire");
		stage.getIcons().add(new Image("/view/icon.png"));
		stage.show();
	}
}
