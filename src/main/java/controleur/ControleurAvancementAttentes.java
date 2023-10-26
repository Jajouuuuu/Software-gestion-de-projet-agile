package controleur;

import java.util.ArrayList;
import java.util.Optional;

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

public class ControleurAvancementAttentes extends Controleur {
    @FXML
    private Pane pane;
    
    @FXML
    private Text titre;

    @FXML
    private ScrollPane scroll;
    
    private ArrayList<Accordion> colonnes;
    private Iteration iteration;

    @FXML
    protected void initialize() {
    	scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setContent(pane);
        scroll.setPannable(true);
        
        creerColonnes();
    }
    
    public void setIteration(Iteration i) {
    	iteration = i;
    	titre.setText(iteration.toString());
    	actualiserAffichage();
    }

    public void versDashboard() {
        app.setFenetre("dashboard.fxml");
    }

    public void creerColonnes() {
    	colonnes = new ArrayList<Accordion>();
    	
        double xPos = 20;
        for (EtatAttente etat: EtatAttente.values()) {
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
            tP.setLayoutX(xPos);
            tP.setLayoutY(113);
            pane.getChildren().add(tP);
            tP.setText(etat.nom);

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
                    // projet.getAttente(Integer.parseInt(db.getString())).setEtat(EtatAttente.values()[colonnes.indexOf(ap2)]);
                    deplacerAttente(Integer.parseInt(db.getString()), colonnes.indexOf(ap2.getChildren().get(0)));
                    actualiserAffichage();
                    event.setDropCompleted(true);
                    event.consume();
                }
            });
            
            xPos += 230;
        }
    }

    public void afficherAttentes() {
    	for (Attente attente: iteration.getAttentes()) {
    		Text txt = new Text();
            TitledPane tache = new TitledPane();
            Accordion colonne = colonnes.get(attente.getEtat().ordinal());
            
            tache.setPrefWidth(200);
            tache.setText(attente.getNom());
            
            String contenu = "";
            
            contenu += attente.getDescription();
            contenu += "\nvaleur : "+attente.getValeur();
            if (attente.getDifficulte() > -1) {
            	contenu += "\ndifficulté : "+attente.getDifficulte();
            }
            contenu += "\n";
            
            if (attente.getResponsable() != null) {
            	contenu += "Fait par "+attente.getResponsable().toString()+"\n";
            }
            String[] messages = attente.getMessagesPO();
            if (messages.length > 0) {
            	contenu += "Messages du PO :\n";
            	for (String m: messages) {
                	contenu += "- "+m+"\n";
                }
            }            
            txt.setText(contenu);
            
            txt.setWrappingWidth(tache.getPrefWidth());
            tache.setContent(txt);
            
            colonne.getPanes().add(tache);
            
            if (!iteration.estCommencee() || iteration.estTerminee())
            	continue;
            if (attente.getEtat() == EtatAttente.VALIDE)
            	continue;
            if (app.getUtilisateur() == projet.getProductOwner() && attente.getEtat() != EtatAttente.A_VALIDER)
            	continue;

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


    @Override
    public void actualiserAffichage() {
        for (Accordion ap: colonnes) {
        	ap.getPanes().clear();
        }
        afficherAttentes();
    }
    
    private void deplacerAttente(int numAttente, int numEtat) {
    	Attente att = projet.getAttente(numAttente);
    	EtatAttente ancienEtat = att.getEtat();
    	EtatAttente nouvelEtat = EtatAttente.values()[numEtat];
    	
    	if (nouvelEtat == EtatAttente.VALIDE && app.getUtilisateur() != projet.getProductOwner()) {
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setContentText("Seul le Product Owner peut valider une tâche.");
    		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
    		alert.showAndWait();
    		return;
    	}
    	
    	if (ancienEtat == EtatAttente.A_FAIRE && nouvelEtat == EtatAttente.A_VALIDER) {
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setContentText("Vous devez mettre la tâche dans la colonne \"En cours\" avant de pouvoir la mettre dans \"À valider \".");
    		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
    		alert.showAndWait();
    		return;
    	}
    	
    	if (app.getUtilisateur() == projet.getProductOwner() && ancienEtat == EtatAttente.A_VALIDER && nouvelEtat == EtatAttente.EN_COURS) {
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setContentText("Si vous ne validez pas la tâche, vous devez la mettre dans la colonne \"À faire\".");
    		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
    		alert.showAndWait();
    		return;
    	}
    	
    	if (att.getResponsable() != null && att.getResponsable() != app.getUtilisateur() && app.getUtilisateur() != projet.getProductOwner()) {
    		Alert alert = new Alert(AlertType.CONFIRMATION);
    		// TODO : mettre le symbole /!\ plutôt que (?)
    		alert.setContentText("Cette tâche est attribuée à un autre équipier. Êtes-vous sûr(e) de vouloir la déplacer ?");
    		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
    		alert.showAndWait();
    		if (alert.getResult() != ButtonType.OK) {
    			return;
    		}
    	}
    	
    	if (app.getUtilisateur() == projet.getProductOwner()
    			&& ancienEtat == EtatAttente.A_VALIDER && nouvelEtat != EtatAttente.VALIDE) {
    		TextInputDialog dialog = new TextInputDialog("");

    		dialog.setTitle("Message");
    		dialog.setHeaderText("Veuillez laisser un message expliquant au coéquipier pourquoi vous ne validez pas cette attente.");
    		dialog.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
    		dialog.setContentText("Message :");

    		Optional<String> result = dialog.showAndWait();
    		
    		if (result.isPresent()) {
    			att.addMessagePO(result.get());
    		} else {
    			return;
    		}
    	}
    	
    	if (ancienEtat == EtatAttente.A_FAIRE) {
    		att.setResponsable(app.getUtilisateur());
    	}
    	
    	if (nouvelEtat == EtatAttente.A_FAIRE) {
    		att.setResponsable(null);
    	}

    	att.setEtat(nouvelEtat);
    }

}
