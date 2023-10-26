package controleur;

import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import modele.Attente;
import modele.EtatAttente;
import modele.Iteration;

public class ControleurIteration extends Controleur {
    @FXML
    private Text name;

    @FXML
    private TitledPane titledPane;

    @FXML
    private Pane pane;

    @FXML
    private ScrollPane scroll;
    
    @FXML
    private AnchorPane anchorAttente;
    
    private ArrayList<Accordion> colonnes;

    @FXML
    protected void initialize() {
    	scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setContent(pane);
        scroll.setPannable(true);
        
        creerIteration();
        actualiserAffichage();
    }

    public void versDashboard() {
        app.setFenetre("dashboard.fxml");
    }

    public void creerIteration() {
    	colonnes = new ArrayList<Accordion>();
    	Accordion acc0 = new Accordion();
    	anchorAttente.getChildren().add(acc0);
    	colonnes.add(acc0);
    	
        int nb = projet.getNbIterations();
        double nbTP = titledPane.getLayoutX();
        for (int i = 1; i <= nb; i++) { //Pour faire apparaitre les itérations
            nbTP += 125;
            TitledPane tP = new TitledPane();
            AnchorPane ap1 = new AnchorPane();
            ScrollPane sP = new ScrollPane();
            AnchorPane ap2 = new AnchorPane();
            Accordion acc = new Accordion();
            
            tP.setContent(ap1);
            tP.setStyle("-fx-box-border: transparent");
            sP.setStyle("-fx-box-border: lightgrey");
            ap1.getChildren().add(sP);
            
            
            sP.setHbarPolicy(ScrollBarPolicy.NEVER);
            sP.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
            sP.setPannable(false);
            
            tP.setPrefHeight(458);
            tP.setPrefWidth(216);
            tP.setLayoutX(nbTP += 105);
            tP.setLayoutY(113);
            pane.getChildren().add(tP);
            tP.setText("Itérations " + i);

            ap2.setPrefHeight(432);
            ap2.setPrefWidth(204);
           
            ap1.setMaxWidth(204);
            ap1.setMinWidth(204);
            ap1.setPrefWidth(204);

            tP.setCollapsible(false);
            pane.setPrefWidth(pane.getPrefWidth() + tP.getPrefWidth());
            
            colonnes.add(acc);
            sP.setContent(ap2);
            ap2.getChildren().add(acc);
            
            ap2.setOnDragOver(new EventHandler <DragEvent>() {
                public void handle(DragEvent event) {
                    if (event.getGestureSource() != ap2 && event.getDragboard().hasString()) {
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    }
                    event.consume();
                }
            });
            
            ap2.setOnDragDropped(new EventHandler <DragEvent>() {
                public void handle(DragEvent event) {
                    Dragboard db = event.getDragboard();
                    deplacerAttente(projet.getAttente(Integer.parseInt(db.getString())), colonnes.indexOf(ap2.getChildren().get(0)));
                    actualiserAffichage();
                    event.setDropCompleted(true);
                    event.consume();
                }
            });
        }
    }
    
    public void deplacerAttente(Attente att, int nouveauNum) {
    	if (app.getUtilisateur() != projet.getScrumMaster()) {
    		afficherErreur("Seul le Scrum Master peut changer le plan de livraison.");
    		return;
    	}
    	if (att.getEtat() == EtatAttente.VALIDE) {
    		afficherErreur("Cette attente ne peut être déplacée car elle a été validée.");
    		return;
    	}
    	if (projet.getIteration(nouveauNum).estTerminee()) {
    		afficherErreur("Impossible de déplacer une attente vers une itération terminée.");
    		return;
    	}
    	att.setNumeroIteration(nouveauNum);
    }

    public void afficherAttentes() {
    	Attente[] attentes;
        for (int i = 0; i <= projet.getNbIterations(); i++) {
        	if (i == 0)
        		attentes = projet.getAttentesDansAucuneIteration();
        	else
        		attentes = projet.getIteration(i).getAttentes();
        	
        	for (Attente attente: attentes) {
        		Text txt = new Text();
                TitledPane tache = new TitledPane();
                Accordion colonne = colonnes.get(i);
                tache.setPrefWidth(200);
                tache.setText(attente.getNom());
                txt.setText(attente.getDescription()+"\nvaleur : "+attente.getValeur());
                if (attente.getDifficulte() > -1) {
                	txt.setText(txt.getText()+"\ndifficulté : "+attente.getDifficulte());
                }
                txt.setWrappingWidth(tache.getPrefWidth());
                tache.setContent(txt);
                
                colonne.getPanes().add(tache);
                
                if (app.getUtilisateur() == projet.getScrumMaster()) {
                	tache.setOnDragDetected(new EventHandler <MouseEvent>() {
                        public void handle(MouseEvent event) {
                            Dragboard db = tache.startDragAndDrop(TransferMode.ANY);
                            ClipboardContent content = new ClipboardContent();
                            content.putString(String.valueOf(projet.getNumeroAttente(attente)));
                            db.setContent(content);
                            event.consume();
                        }
                    });
                }
        	}
        }
    }

    @Override
    public void actualiserAffichage() {
        for (Accordion acc: colonnes) {
        	acc.getPanes().clear();
        }
        afficherAttentes();
    }

    @FXML
    public void placementAutoAttentes() {
    	if (app.getUtilisateur() != projet.getScrumMaster()) {
    		afficherErreur("Seul le Scrum Master peut changer le plan de livraison.");
    		return;
    	}
        projet.repartirAttentesDansIterations();
        actualiserAffichage();
    }
    
    @FXML
    public void nouvelleIteration() {
    	if (app.getUtilisateur() != projet.getScrumMaster()) {
    		afficherErreur("Seul le Scrum Master peut ajouter une itération au projet.");
    		return;
    	}
        projet.nouvelleIteration();
        app.setFenetre("iteration.fxml");
    }
    
    @FXML
    public void retirerIteration() {
    	if (app.getUtilisateur() != projet.getScrumMaster()) {
    		afficherErreur("Seul le Scrum Master peut enlever une itération au projet.");
    		return;
    	}
    	try {
    		projet.retirerDerniereIteration();
    		app.setFenetre("iteration.fxml");
    	} catch (Exception e) {
    		afficherErreur(e.getMessage());
    		return;
    	}
    }
    
    private void afficherErreur(String message) {
    	Alert alert = new Alert(AlertType.ERROR);
		alert.setContentText(message);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		alert.showAndWait();
    }
    
    @FXML
    public void avancerPasValides() {
    	if (app.getUtilisateur() != projet.getScrumMaster()) {
    		afficherErreur("Seul le Scrum Master peut modifier le plan de livraison.");
    		return;
    	}
    	if (!projet.getIteration(1).estTerminee()) {
    		afficherErreur("Aucune itération n'est terminée.");
    		return;
    	}
    	if (projet.getIteration(projet.getNbIterations()).estTerminee()) {
    		afficherErreur("Toutes les itérations sont terminées.");
    		return;
    	}
        Iteration enCours = projet.getIterationEnCours();
        for (Attente a: projet.getAttentes()) {
        	if (a.getEtat() != EtatAttente.VALIDE && a.getIteration().estTerminee()) {
        		a.setNumeroIteration(enCours.getNumero());
        	}
        }
        actualiserAffichage();
        
    }
}
