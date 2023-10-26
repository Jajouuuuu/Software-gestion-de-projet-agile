package controleur;

import java.io.IOException;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Date;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;
import modele.Base;
import vue.App;

public class ControleurConfigIteration extends Controleur {
	
	@FXML private DatePicker inputDate;
    @FXML private TextField inputNb;
    @FXML private TextField inputDuree;
    
    @FXML
    void valider(ActionEvent event) throws IOException {
    	try {
	    	if (inputDate.getValue() == null)
	    		throw new IllegalArgumentException("Il faut spécifier la date de fin.");
	    	projet.setDateDebut(new Date(inputDate.getValue().toEpochSecond(LocalTime.MIN, ZoneOffset.UTC)*1000));
	    	projet.setIterations(Integer.valueOf(inputNb.getText()), Integer.valueOf(inputDuree.getText()));
	    	app.setModeConfiguration(false);
			app.setFenetre("dashboard.fxml");
			Base.getInstance().sauvegarder(App.cheminBase);
    	} catch (Exception e) {
    		Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText(e.getMessage());
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.showAndWait();
			e.printStackTrace();
    	}
    }

    @Override
	public void actualiserAffichage() {
		// do nothing
	}

}
