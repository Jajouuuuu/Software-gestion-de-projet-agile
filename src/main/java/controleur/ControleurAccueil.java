package controleur;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.util.Callback;
import modele.Personne;
import modele.Projet;

public class ControleurAccueil extends Controleur {
	@FXML
	private Text message;

	@FXML
	private TableView<Projet> tableau;

	@FXML
	protected void initialize() {
		actualiserAffichage();
	}

	
	@FXML
	public void onClickNewProject() {
		app.setModeConfiguration(true);
		app.setFenetre("creationprojet.fxml");
	}
	
	@FXML
	public void onClickLogout() {
		app.setUtilisateur(null);
		app.setFenetre("login.fxml");
	}
	
	public void onClick(Projet p) {
		if (p.getScrumMaster() == null) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setContentText("Ce projet n'a pas de Scrum Master. Souhaitez-vous devenir le Scrum master du projet "+p.getNom()+" ?");
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.showAndWait();
			if (alert.getResult() != ButtonType.OK) {
    			return;
    		}
			p.setScrumMaster(app.getUtilisateur());
			app.setProjet(p);
			app.setFenetre("choixEquipiers.fxml");
			return;
		}
		if (!p.aBesoinDe(app.getUtilisateur())) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Vous ne faites pas partie de ce projet.");
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.showAndWait();
			return;
		}
		if (p.getEquipiers().length == 0 && app.getUtilisateur() == p.getScrumMaster()) {
			app.setProjet(p);
			app.setModeConfiguration(true);
			app.setFenetre("choixEquipiers.fxml");
			return;
		}
		if (!p.iterationsConfigurees() && app.getUtilisateur() == p.getScrumMaster()) {
			app.setProjet(p);
			app.setModeConfiguration(true);
			app.setFenetre("configIterations.fxml");
			return;
		}
		app.setProjet(p);
		app.setFenetre("dashboard.fxml");
	}

	@Override
	public void actualiserAffichage() {
		Personne p = app.getUtilisateur();
		message.setText("Bonjour "+p.toString());
		
        
		TableColumn<Projet,String> nameCol = new TableColumn<Projet,String>("Nom");
		nameCol.setCellValueFactory(new PropertyValueFactory<Projet, String>("nom"));
		
		TableColumn<Projet,String> poCol = new TableColumn<Projet,String>("Product Owner");
		poCol.setCellValueFactory(new PropertyValueFactory<Projet, String>("productOwner"));
		
		TableColumn<Projet,String> smCol = new TableColumn<Projet,String>("Scrum Master");
		smCol.setCellValueFactory(new PropertyValueFactory<Projet, String>("productOwner"));
		smCol.setCellValueFactory(new Callback<CellDataFeatures<Projet, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Projet, String> row) {
				if (row.getValue().getScrumMaster() == null) {
					return new SimpleStringProperty("AUCUN");
				} else {
					return new SimpleStringProperty(row.getValue().getScrumMaster().toString());
				}
			}
		});

		tableau.setItems(FXCollections.observableArrayList(Projet.getProjets()));
		
		tableau.getColumns().addAll(nameCol, poCol, smCol);
		
		tableau.setRowFactory(tv -> {
		    TableRow<Projet> row = new TableRow<Projet>();
		    row.setOnMouseClicked(event -> {
		        if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
		        	Projet clickedRow = row.getItem();
		            onClick(clickedRow);
		        }
		    });
		    return row ;
		});
	}
}
