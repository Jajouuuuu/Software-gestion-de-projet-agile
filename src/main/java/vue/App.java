package vue;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import controleur.Controleur;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import modele.Attente;
import modele.Base;
import modele.EtatAttente;
import modele.Personne;
import modele.Projet;
import modele.Sauvegarde;

public class App extends Application {
	public final static String cheminBase = "base.json";
	private Stage primaryStage;
	private Personne utilisateur; // la personne connectée sur l'aplication
	private static App instance;
	private Controleur controleurCourant;
	private Projet projet;
	private boolean modeConfiguration;

    @Override
    public void start(Stage primaryStage) throws IOException {
    	instance = this;
    	modeConfiguration = false;
    	creerProjet();
    	this.primaryStage = primaryStage;
    	primaryStage.setTitle("Gestion de projet agile");
		primaryStage.setResizable(false);
		primaryStage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);
		setFenetre("login.fxml");
    }

    public static void main(String[] args) {
    	try {
			Base.getInstance().chargerDepuisFichier(cheminBase);
			System.out.println("Base chargée avec succès ("+Base.getInstance().getProjets().length+" projets chargés).");
		} catch (IOException e) {
			System.err.println("Aucune base n'a été trouvée, création d'une base vide.");
		    try {
		    	Base.getInstance().sauvegarder(cheminBase);
			} catch (IOException e2) {
				System.err.println("ERREUR FATALE : IMPOSSIBLE DE CRÉER UN FICHIER POUR SAUVEGARDER LA BASE !!!");
				return;
			}
		}
		
		Thread save = new Thread(new Sauvegarde());
		save.start();
        launch();
        save.interrupt();
    }
    
    public static App getInstance() {
    	return instance;
    }
    
    /** 
     * Prend un nom de fichier FXML (placé dans le package vue),
     * le charge dans la fenêtre et renvoie le contrôleur.
    */
    public Controleur setFenetre(String fxmlName) {
    	Controleur c = null;
    	try {
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlName));
			Parent root = loader.load();
			c = ((Controleur) loader.getController());
			primaryStage.setScene(new Scene(root));
			primaryStage.sizeToScene();
			primaryStage.show();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	controleurCourant = c;
    	return c;
    }
    
    public void setModeConfiguration(boolean mode) {
    	this.modeConfiguration = mode;
    }
    
    public boolean isModeConfiguration() {
    	return modeConfiguration;
    }

    private void closeWindowEvent(WindowEvent event) {
    	try {
			Base.getInstance().sauvegarder(cheminBase);
		} catch (IOException e) {
			System.err.println("La sauvegarde de la base a échoué !");
		}
    	if (isModeConfiguration()) {
    		Alert alert = new Alert(AlertType.CONFIRMATION);
    		alert.setTitle("Quitter l'application");
    		alert.setContentText("Vous avez une création de projet en cours, êtes vous sûr de vouloir quitter ?");
    		Optional<ButtonType> result = alert.showAndWait();
    		if (result.isPresent() && result.get() == ButtonType.CANCEL) {
    			event.consume();
    		}
    	}
    }
    
    /** Fonction provisoire qui crée un exemple de projet. */
    public void creerProjet() {
    	Personne brutus = Personne.get("brutus");
    	Personne delhoumi = Personne.get("delhoumi");
    	Personne bouchard = Personne.get("bouchard");
    	Personne bouillet = Personne.get("bouillet");
    	Personne hemery = Personne.get("hemery");
    	Personne lefevre = Personne.get("lefevre");
    	Personne rouvres = Personne.get("rouvres");

    	if (brutus == null) brutus = new Personne("brutus", "Brutus", "Philippe");
    	if (delhoumi == null) delhoumi = new Personne("delhoumi", "Delhoumi", "Sylvian");
    	if (bouchard == null) bouchard = new Personne("bouchard", "Bouchard", "Valentin", "développeur Android");
    	if (bouillet == null) bouillet = new Personne("bouillet", "Bouillet", "Rémi", "développeur Java");
    	if (hemery == null) hemery = new Personne("hemery", "Hemery", "Emilien", "développeur web");
    	if (lefevre == null) lefevre = new Personne("lefevre", "Lefevre", "Jeanne-Emma", "développeur web");
    	if (rouvres == null) rouvres = new Personne("rouvres", "Rouvres", "Mathis", "développeur Android");

    	Projet p = new Projet("Exemple", "Projet factice, application Android pour créer et rejoindre des covoiturages", brutus);
    	p.setScrumMaster(delhoumi);
    	p.ajouterEquipier(bouchard);
    	p.ajouterEquipier(bouillet);
    	p.ajouterEquipier(hemery);
    	p.ajouterEquipier(lefevre);
    	p.ajouterEquipier(rouvres);
    	
    	p.ajouterAttente(new Attente("chercher trajet", "chercher un trajet en spécifiant la date, le point de départ et le point d'arrivé", 2500, p));
    	p.ajouterAttente(new Attente("proposer covoit", "proposer un trajet (date, départ, arrivée, nombre de places)", 1700, p));
    	p.ajouterAttente(new Attente("déposer demande", "déposer une demande de trajet (date, départ, arrivée)", 1700, p));
    	p.ajouterAttente(new Attente("s'inscrire", "s'inscrire (mail, nom, prénom...)", 1700, p));
    	p.ajouterAttente(new Attente("renseigner infos trajets", "permettre de renseigner les infos d'un trajet (date, départ, arrivée)", 1300, p));
    	p.ajouterAttente(new Attente("être alerté du retard", "chaque passager peut avertir qu'il va arriver en retard", 1300, p));
    	p.ajouterAttente(new Attente("déclarer codes passagers", "générer un code sécurisé pour chaque passager", 900, p));
    	p.ajouterAttente(new Attente("régler et obtenir code passager", "vérifier que le conducteur rentre le bon code pour valider le paiment", 900, p));
    
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(new Date()); // maintenant
    	cal.add(Calendar.DAY_OF_MONTH, -16);
    	p.setDateDebut(cal.getTime()); // aujourd'hui - 16 jours
    	p.setIterations(4, 7);
    	
    	Attente[] attentes = p.getAttentes();
    	
    	attentes[0].setDifficulte(21);
    	attentes[1].setDifficulte(5);
    	attentes[2].setDifficulte(5);
    	attentes[3].setDifficulte(5);
    	attentes[4].setDifficulte(13);
    	attentes[5].setDifficulte(8);
    	attentes[6].setDifficulte(8);
    	attentes[7].setDifficulte(21);
    	
    	p.repartirAttentesDansIterations();
    	
    	attentes[0].setEtat(EtatAttente.VALIDE);
    	attentes[1].setEtat(EtatAttente.VALIDE);
    	attentes[2].setEtat(EtatAttente.VALIDE);
    	attentes[3].setEtat(EtatAttente.VALIDE);
    	attentes[4].setEtat(EtatAttente.A_VALIDER);
    	attentes[5].setEtat(EtatAttente.EN_COURS);
    	attentes[6].setEtat(EtatAttente.A_FAIRE);
    	attentes[7].setEtat(EtatAttente.A_FAIRE);
    	
    	attentes[0].setResponsable(Personne.get("bouillet"));
    	attentes[1].setResponsable(Personne.get("hemery"));
    	attentes[2].setResponsable(Personne.get("bouchard"));
    	attentes[3].setResponsable(Personne.get("rouvres"));
    	attentes[4].setResponsable(Personne.get("bouillet"));
    	attentes[5].setResponsable(Personne.get("hemery"));

//    	System.out.println(Base.getInstance().exporter());
     }

	public Personne getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Personne p) {
		this.utilisateur = p;
	}

	public void setProjet(Projet p){
		this.projet = p;
	}

	public void actualiserAffichage() {
		controleurCourant.actualiserAffichage();
	}

	public Projet getProjet(){
		return projet;
	}

}