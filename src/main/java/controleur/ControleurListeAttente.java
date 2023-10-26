package controleur;

import java.util.Arrays;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.util.Callback;
import modele.Attente;

public class ControleurListeAttente extends Controleur {
	
	@FXML
	private Label titre;

	@FXML
	private Button newAttente;

	@FXML
	private Button terminerAttente;

	@FXML
	private Button modifierAttente;

	@FXML
	private TableView<Attente> tableauAttente;

	@FXML
	protected void initialize(){
		if (app.isModeConfiguration()) {
			titre.setText("Étape 2/2 : "+titre.getText());
		}
			
		actualiserAffichage();
	}

	@Override
	public void actualiserAffichage() {
		TableColumn<Attente,String> nameCol = new TableColumn<Attente,String>("Nom");
		nameCol.setCellValueFactory(new PropertyValueFactory<Attente, String>("nom"));		
		TableColumn<Attente,String> valCol = new TableColumn<Attente,String>("Valeur");
		valCol.setCellValueFactory(new PropertyValueFactory<Attente, String>("valeur"));
		TableColumn<Attente,String> diffCol = new TableColumn<Attente,String>("Difficulte");
		diffCol.setCellValueFactory(new Callback<CellDataFeatures<Attente, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Attente, String> row) {
				if (row.getValue().getDifficulte() == -1) {
					return new SimpleStringProperty();
				} else {
					return new SimpleStringProperty(String.valueOf(row.getValue().getDifficulte()));
				}
			}
		});
		tableauAttente.setItems(FXCollections.observableArrayList(projet.getAttentes()));
		tableauAttente.getColumns().addAll(nameCol, valCol, diffCol);
		tableauAttente.autosize();
		//change le nom du bouton si l'utilisateur est un équipier
		if(Arrays.asList(projet.getEquipiers()).contains(app.getUtilisateur())) {
			modifierAttente.setText("Détails attente");
		}
	}
	
	@FXML
	void onClickModifierAttente(MouseEvent event) {
		Attente attenteSelec = tableauAttente.getSelectionModel().getSelectedItem();
		if(attenteSelec == null) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Vous devez d'abord sélectionner l'attente à modifier");
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.showAndWait();
			app.setFenetre("listeAttente.fxml");
			return;
		}
		ControleurModificationAttente cmodif = (ControleurModificationAttente) app.setFenetre("modificationAttente.fxml");
		cmodif.setNumAttente(projet.getNumeroAttente(attenteSelec));
	}

    @FXML
    void versNewAttente(MouseEvent event) {
    	app.setFenetre("creationAttente.fxml");
    }
	
	@FXML
	public void onClickTerminerAttente() {
		if (app.isModeConfiguration())
			app.setModeConfiguration(false);
		app.setFenetre("dashboard.fxml");	
	}
}
