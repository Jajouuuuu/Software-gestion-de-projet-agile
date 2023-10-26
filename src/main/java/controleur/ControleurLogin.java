package controleur;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import modele.Personne;

public class ControleurLogin extends Controleur {
	@FXML
	private TextField login;
	
	@FXML
	public void clicBoutonOK(MouseEvent e) {
		Personne p = Personne.get(login.getText());
		if (p == null) {
			Alert a = new Alert(AlertType.ERROR);
			a.setContentText("Cet identifiant n'appartient à aucune personne.");
			a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			a.showAndWait();
		} else {
			app.setUtilisateur(p);
			app.setFenetre("accueil.fxml");
		}
	}
	
	@FXML
	public void touchePressee(KeyEvent e) {
		if (e.getCode() == KeyCode.ENTER) {
			clicBoutonOK(null);
		}
	}

	@Override
	public void actualiserAffichage() {
		// do nothing
	}

}
