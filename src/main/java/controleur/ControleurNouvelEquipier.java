package controleur;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import modele.Personne;


public class ControleurNouvelEquipier extends Controleur {

	@FXML
	private TextField loginEquipier;

	@FXML
	private TextField nomEquipier;

	@FXML
	private TextField prenomEquipier;
	
	@FXML
	private TextField posteEquipier;

	@FXML
	void enregistrerCreation(MouseEvent event) {
		try {
			if (posteEquipier.getText().isBlank())
				new Personne(loginEquipier.getText(), nomEquipier.getText(), prenomEquipier.getText());
			else
				new Personne(loginEquipier.getText(), nomEquipier.getText(), prenomEquipier.getText(), posteEquipier.getText());
			((Stage) loginEquipier.getScene().getWindow()).close();
		} catch (IllegalArgumentException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText(e.getMessage());
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.showAndWait();
		}
	}

	public void retour(){
		((Stage) loginEquipier.getScene().getWindow()).close();;
	}

	@Override
	public void actualiserAffichage() {
		// TODO Auto-generated method stub

	}

}
