package controleur;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import modele.Attente;

public class ControleurModificationAttente extends Controleur {

	private int numAttente = 0;

	@FXML
	private Button annulerAttente;

    @FXML
    private Button suppAttente;

	@FXML
	private Button validerAttente;

	@FXML
	private TextField nomAttente;

	@FXML
	private TextArea descAttente;

	@FXML
	private TextField valAttente;

	@FXML
	private TextField diffAttente;

	public void setNumAttente(int numAttente) {
		this.numAttente = numAttente;
		actualiserAffichage();
	}

	@FXML
	protected void initialize(){
		if(app.getUtilisateur() == projet.getScrumMaster()) {
			diffAttente.setEditable(true);
			descAttente.setEditable(true);
		}
		if(app.getUtilisateur() == projet.getProductOwner()) {
			nomAttente.setEditable(true);
			descAttente.setEditable(true);
			valAttente.setEditable(true);
			suppAttente.setVisible(true);
		}
	}

	@Override
	public void actualiserAffichage() {
		Attente[] tab = projet.getAttentes();
		nomAttente.setText(tab[numAttente].getNom());
		descAttente.setText(tab[numAttente].getDescription());
		valAttente.setText(String.valueOf(tab[numAttente].getValeur()));
		//Test si la difficulté a déjà été changée une fois
		if(tab[numAttente].getDifficulte()!=-1) {
			diffAttente.setText(String.valueOf(tab[numAttente].getDifficulte()));
		} else {
			diffAttente.setText("");
		}
	}

	@FXML
	void onClickAnnulerAttente(MouseEvent event) {
		app.setFenetre("listeAttente.fxml");
	}
	
	@FXML
	void onClickSupprimerAttente(MouseEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		Attente[] tab = projet.getAttentes();
		alert.setContentText(tab[numAttente].getNom() + "\n" + tab[numAttente].getDescription());
		alert.setHeaderText("Voulez-vous vraiment supprimer cette attente ?");
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		alert.showAndWait();
		if (alert.getResult() == ButtonType.OK) {
			projet.supprimerAttente(numAttente);
			alert.close();
			app.setFenetre("listeAttente.fxml");
		} else {
			alert.close();
			app.setFenetre("modificationAttente.fxml").actualiserAffichage();
		}
		
		
	}

	@FXML
	void onClickValiderAttente(MouseEvent event) {
		Attente ancienne = projet.getAttente(numAttente);
		
		if (!nomAttente.getText().equals(ancienne.getNom())) {
			ancienne.setNom(nomAttente.getText());
		}
		
		if (!descAttente.getText().equals(ancienne.getDescription())) {
			ancienne.setDescription(descAttente.getText());
		}
		
		if (Integer.parseInt(valAttente.getText()) != ancienne.getValeur()) {
			ancienne.setValeur(Integer.parseInt(valAttente.getText()));
		}
		
		// Si on change la difficulté, on change la valeur de l'attente
		if (diffAttente.getText() != "" && Float.parseFloat(diffAttente.getText()) != ancienne.getDifficulte()) {
			ancienne.setDifficulte(Float.parseFloat(diffAttente.getText()));
		}

		app.setFenetre("listeAttente.fxml");
	}

}
