package controleur;

import java.text.DateFormat;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import modele.Attente;
import modele.Iteration;
import modele.Personne;
import vue.GenerateurCourbes;

public class ControleurDashboard extends Controleur {
	
	@FXML private Text nom;
	@FXML private Text description;
	@FXML private Text owner;
	@FXML private Text master;

	@FXML private TableView<Attente> tableauAttentes;
	@FXML private TableView<Personne> tableauEquipiers;
	@FXML private TableView<Iteration> tableauIterations;

	@FXML private BorderPane courbes;

	@FXML
	protected void initialize() {
		TableColumn<Iteration,String> etatCol = new TableColumn<Iteration,String>(); // la colonne prend une étoile pour l'itération en cours
		etatCol.setCellValueFactory(new Callback<CellDataFeatures<Iteration, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Iteration, String> row) {
				if (row.getValue().estCommencee() && !row.getValue().estTerminee()) {
					return new SimpleStringProperty("*");
				} else {
					return new SimpleStringProperty();
				}
			}
		});
		TableColumn<Iteration,String> nomCol = new TableColumn<Iteration,String>("N°");
		nomCol.setCellValueFactory(new PropertyValueFactory<Iteration, String>("numero"));
		TableColumn<Iteration,String> debutCol = new TableColumn<Iteration,String>("Début");
		debutCol.setCellValueFactory(data -> new SimpleStringProperty(DateFormat.getDateInstance().format(data.getValue().getDebut())));
		TableColumn<Iteration,String> finCol = new TableColumn<Iteration,String>("Fin");
		finCol.setCellValueFactory(data -> new SimpleStringProperty(DateFormat.getDateInstance().format(data.getValue().getFin())));
		tableauIterations.getColumns().addAll(etatCol, nomCol, debutCol, finCol);
		tableauIterations.autosize();
		
		tableauIterations.setRowFactory(tv -> {
		    TableRow<Iteration> row = new TableRow<>();
		    row.setOnMouseClicked(event -> {
		        if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
		        	Iteration clickedRow = row.getItem();
		        	((ControleurAvancementAttentes) app.setFenetre("avancement.fxml")).setIteration(clickedRow);
		        }
		    });
		    return row ;
		});
		
		TableColumn<Personne,String> nomCol2 = new TableColumn<Personne,String>("Nom");
		nomCol2.setCellValueFactory(new PropertyValueFactory<Personne, String>("nom"));
		TableColumn<Personne,String> prenomCol = new TableColumn<Personne,String>("Prénom");
		prenomCol.setCellValueFactory(new PropertyValueFactory<Personne, String>("prenom"));
		TableColumn<Personne,String> posteCol = new TableColumn<Personne,String>("Poste");
		posteCol.setCellValueFactory(new PropertyValueFactory<Personne, String>("poste"));
		tableauEquipiers.getColumns().addAll(nomCol2, prenomCol, posteCol);
		tableauEquipiers.autosize();
		
		TableColumn<Attente,String> nameCol = new TableColumn<Attente,String>("Nom");
		nameCol.setCellValueFactory(new PropertyValueFactory<Attente, String>("nom"));
		TableColumn<Attente,String> valCol = new TableColumn<Attente,String>("Valeur");
		valCol.setCellValueFactory(new PropertyValueFactory<Attente, String>("valeur"));
		TableColumn<Attente,String> diffCol = new TableColumn<Attente,String>("Difficulté");
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
		tableauAttentes.getColumns().addAll(nameCol, valCol, diffCol);
		tableauAttentes.autosize();
		
		actualiserAffichage();
	}
	
	@FXML
	public void retour() {
		app.setProjet(null);
		app.setFenetre("accueil.fxml");
	}

	@FXML
	public void versIteration(){
		app.setFenetre("iteration.fxml");
	}

	@FXML
	public void versAttente(){
		app.setFenetre("listeAttente.fxml");
	}
	
	@Override
	public void actualiserAffichage() {
		nom.setText("Nom : "+app.getProjet().getNom());
		description.setText("Description : "+projet.getDescription());
		owner.setText("Product Owner : "+projet.getProductOwner().toString());
		
		tableauAttentes.setItems(FXCollections.observableArrayList(projet.getAttentes()));
		tableauEquipiers.setItems(FXCollections.observableArrayList(projet.getEquipiers()));
		
		if (projet.getScrumMaster() == null) {
			master.setText("Scrum Master : AUCUN");
			courbes.getChildren().clear();
			courbes.setCenter(new Text("Veuillez attendre qu'un Scrum Master prenne en charge votre projet."));
		} else {
			master.setText("Scrum Master : "+projet.getScrumMaster().toString());
		}
		
		if (projet.iterationsConfigurees()) {
			tableauIterations.setItems(FXCollections.observableArrayList(projet.getIterations()));
			
			GenerateurCourbes gc = new GenerateurCourbes(projet);
			
			LineChart<Number, Number> courbe1 = gc.courbeTravail();
			courbe1.setMaxWidth(courbes.getPrefWidth()/2);
			courbes.setLeft(courbe1);
			
			LineChart<Number, Number> courbe2 = gc.courbeValeur();
			courbe2.setMaxWidth(courbes.getPrefWidth()/2);
			courbes.setRight(courbe2);
		}
		
		
	}

}
