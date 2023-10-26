package controleur;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import modele.Attente;

public class ControleurCreationAttente extends Controleur {

	@FXML
	private Button AnnulerAttente;

	@FXML
	private Button ValiderAttente;

	@FXML
	private TextArea descAttente;

	@FXML
	private TextField nomAttente;

	@FXML
	private TextField valAttente;

	public ControleurCreationAttente() {
		super();
	}

	@Override
	public void actualiserAffichage() {
		// TODO Auto-generated method stub

	}
	@FXML
	public void onClickValiderAttente() {
		String nomAttente = this.getNomAttente();
		String descAttente = this.getDescAttente();
		boolean erreur = false;
		try {
			int valeurAttente = this.getValAttente();
			Attente attente = new Attente(nomAttente,descAttente,valeurAttente, projet);
			projet.ajouterAttente(attente);
		} catch (NumberFormatException e) {
			erreur = true;
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("ATTENTION LA VALEUR DOIT ETRE UN ENTIER");
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.showAndWait();
			if (alert.getResult() == ButtonType.OK) {
				alert.close();
			}
		}
		if (erreur == false) {
			app.setFenetre("listeAttente.fxml");
		}
	}
	@FXML
	public void onClickAnnulerAttente() {
		app.setFenetre("listeAttente.fxml");
	}
	public String getDescAttente() {
		return descAttente.getText();
	}

	public String getNomAttente() {
		return nomAttente.getText();
	}

	public int getValAttente() {
		return Integer.parseInt(valAttente.getText());
	}


}
