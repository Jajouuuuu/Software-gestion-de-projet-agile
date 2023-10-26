package controleur;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import modele.Personne;
import vue.App;

public class ControleurChoixEquipiers extends Controleur {

	@FXML
	private TableView<Personne> tableau1;

	@FXML
	private TableView<Personne> tableau2;

	@FXML
	protected void initialize() {
		TableColumn<Personne, String> nomCol = new TableColumn<Personne, String>("Nom");
		nomCol.setCellValueFactory(new PropertyValueFactory<Personne, String>("nom"));
		TableColumn<Personne, String> prenomCol = new TableColumn<Personne, String>("Prénom");
		prenomCol.setCellValueFactory(new PropertyValueFactory<Personne, String>("prenom"));
		TableColumn<Personne, String> posteCol = new TableColumn<Personne, String>("Poste");
		posteCol.setCellValueFactory(new PropertyValueFactory<Personne, String>("poste"));
		tableau1.getColumns().addAll(nomCol, prenomCol, posteCol);
		
		nomCol = new TableColumn<Personne, String>("Nom");
		nomCol.setCellValueFactory(new PropertyValueFactory<Personne, String>("nom"));
		prenomCol = new TableColumn<Personne, String>("Prénom");
		prenomCol.setCellValueFactory(new PropertyValueFactory<Personne, String>("prenom"));
		posteCol = new TableColumn<Personne, String>("Poste");
		posteCol.setCellValueFactory(new PropertyValueFactory<Personne, String>("poste"));
		tableau2.getColumns().addAll(nomCol, prenomCol, posteCol);

		actualiserAffichage();
	}
	
	@FXML
	void ajouter(ActionEvent event) {
		Personne p = tableau1.getSelectionModel().getSelectedItem();
		if (p == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Il faut sélectionner une personne dans le tableau de gauche.");
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.showAndWait();
		} else {
			try {
				projet.ajouterEquipier(p);
				actualiserAffichage();
			} catch (IllegalArgumentException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText(e.getMessage());
				alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
				alert.showAndWait();
			}
			
		}
	}

	@FXML
	void retirer(ActionEvent event) {
		Personne p = tableau2.getSelectionModel().getSelectedItem();
		if (p == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Il faut sélectionner une personne dans le tableau de droite.");
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.showAndWait();
		} else {
			try {
				projet.supprimerEquipier(p);
				actualiserAffichage();
			} catch (IllegalArgumentException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText(e.getMessage());
				alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
				alert.showAndWait();
			}
		}
	}

	@FXML
    void nouvelEquipier(ActionEvent event) {
		Stage dialog = new Stage();
		try {
			FXMLLoader loader = new FXMLLoader(App.getInstance().getClass().getResource("creationEquipier.fxml"));
			Parent root = loader.load();
			dialog.setScene(new Scene(root));
			dialog.sizeToScene();
			dialog.showAndWait();
			actualiserAffichage();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
    }

	@Override
	public void actualiserAffichage() {
		tableau1.setItems(FXCollections.observableArrayList(Personne.getPersonnes()));
		tableau2.setItems(FXCollections.observableArrayList(projet.getEquipiers()));
		tableau2.autosize();
	}
	
	@FXML
	void suivant(ActionEvent event) {
		app.setFenetre("configIterations.fxml");
	}

}
