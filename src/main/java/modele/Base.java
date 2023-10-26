package modele;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Base {
	private static Base instance = null;
	private HashSet<Projet> projets;
	private transient HashMap<String, Personne> personnes;
	
	private Base() {
		projets = new HashSet<Projet>();
		personnes = new HashMap<String, Personne>();
	}
	
	public static Base getInstance() {
		if (instance == null) {
			instance = new Base();
		}
		
		return instance;
	}
	
	public Projet getProjet(String nom) {
		for (Projet p: projets) {
			if (p.getNom().equals(nom)) {
				return p;
			}
		}
		
		return null;
	}
	
	public Projet[] getProjets() {
		return Arrays.copyOf(projets.toArray(), projets.size(), Projet[].class);
	}
	
	public Personne[] getPersonnes() {
		return Arrays.copyOf(personnes.values().toArray(), personnes.size(), Personne[].class);
	}
	
	public Personne getPersonne(String login) {
		return personnes.get(login);
	}
	
	public void ajouterProjet(Projet projet) {
		for (Projet p: projets) {
			if (p.getNom().equals(projet.getNom())) {
				throw new IllegalArgumentException("Ce nom de projet est déjà utilisé");
			}
		}

		projets.add(projet);
	}
	
	/** SI le projet est dans la base, le supprimer et renvoyer true. Sinon renvoyer false; */
	public boolean supprimerProjet(Projet p) {
		return projets.remove(p);
	}
	
	public void ajouterPersonne(Personne p) {
		if (personnes.containsKey(p.getLogin())) {
			throw new IllegalArgumentException("Cet identifiant est déjà utilisé");
		}
		personnes.put(p.getLogin(), p);
	}
	
	/** Méthode permettant de charger la base depuis des données en JSON. */
	public void charger(String json) {
		if (projets.size() > 0) {
			throw new IllegalStateException("La méthode chargée ne peut être appelée qu'avec une base vide.");
		}
		
		final GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Personne.class, new DeserialisationPersonne());
        final Gson gson = builder.create();
        
        // La désérialisation de Personne remplie automatiquement la vraie base (instance)
        // mais pas Projet car il serait trop galère de redéfinir sa désérialisation
        Base b2 = gson.fromJson(json, Base.class);
        
        if (b2 == null) // le fichier chargé est vide 
        	return;
        
        for (Projet p: b2.getProjets()) {
        	if (p.getNom().equals("Exemple")) // le Projet "Exemple" n'est pas chargé
        		continue;
        	for (Iteration i: p.getIterations())
        		i.setprojet(p);
        	for (Attente a: p.getAttentes())
        		a.setprojet(p);
        	this.ajouterProjet(p);
        }
	}
	
	public String exporter() {
		final GsonBuilder builder = new GsonBuilder();
        final Gson gson = builder.create();
        return gson.toJson(this);
	}
	
	public void sauvegarder(String chemin) throws IOException {
		Projet fake = getProjet("Exemple");
		if (fake != null)
			supprimerProjet(fake);
		FileWriter myWriter = new FileWriter(chemin);
		myWriter.write(exporter());
	    myWriter.close();
	    if (fake != null)
	    	ajouterProjet(fake);
	}
	
	public void chargerDepuisFichier(String chemin) throws IOException {
		charger(new String(Files.readAllBytes(Paths.get(chemin))));
	}

}
