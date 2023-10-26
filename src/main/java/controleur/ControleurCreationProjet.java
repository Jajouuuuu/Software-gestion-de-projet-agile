package controleur;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Date;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;
import modele.Projet;

public class ControleurCreationProjet extends Controleur {
	
    @FXML private TextField inputNom;
    @FXML private TextArea inputDescription;
    @FXML private TextField inputPO;
    @FXML private DatePicker inputDate;
    
    @FXML
	protected void initialize() {
		actualiserAffichage();
	}

	@FXML
    void onClickRetour(ActionEvent event) {
		app.setFenetre("accueil.fxml");
    }
    
    @FXML
    void onClickSuivant(ActionEvent event) throws IOException {
    	try {
	    	Projet p = new Projet(inputNom.getText(), inputDescription.getText(), app.getUtilisateur());
	    	if (inputDate.getValue() == null)
	    		throw new IllegalArgumentException("Il faut spécifier la date de fin.");
	    	p.setDateFin(new Date(inputDate.getValue().toEpochSecond(LocalTime.MIN, ZoneOffset.UTC)*1000));
	    	LocalDate todaysDate = LocalDate.now();
	    	if (inputDate.getValue().isBefore(todaysDate)) {
	    		throw new IllegalArgumentException("La date spécifiée est avant celle d'aujourd'hui");
	    	}
			
			app.setProjet(p);
			app.setFenetre("listeAttente.fxml");
    	} catch (Exception e) {
    		Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText(e.getMessage());
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.showAndWait();
    	}
    }

    @Override
	public void actualiserAffichage() {
		inputPO.setText(app.getUtilisateur().toString());
		
	}

}
