package controleur;

import modele.Projet;
import vue.App;

public abstract class Controleur {
	protected App app;
	protected Projet projet;
	
	public Controleur() {
		app = App.getInstance();
		projet = app.getProjet();
	}
	
	public abstract void actualiserAffichage();

}
