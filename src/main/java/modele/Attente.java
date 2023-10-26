package modele;

import java.util.ArrayList;
import java.util.Arrays;

public class Attente {
	private String nom;
	private String description;
	private int valeur;
	private transient Projet projet;
	private int iteration;
	private float difficulte;
	private EtatAttente etat;
	private Personne responsable;
	private ArrayList<String> messagesPO;
	
	public Attente(String nom, String description, int valeur, Projet projet) {
		this.nom = nom;
		this.description = description;
		this.valeur = valeur;
		this.projet = projet;
		this.iteration = 0;
		this.difficulte = -1;
		this.etat = EtatAttente.A_FAIRE;
		this.messagesPO = new ArrayList<String>();
	}
	
	public void setNom(String nom) {
		this.nom = nom;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setValeur(int valeur) {
		this.valeur = valeur;
	}

	public String getNom() {
		return nom;
	}

	public String getDescription() {
		return description;
	}

	public int getValeur() {
		return valeur;
	}
	
	/** Renvoie le coéquipier chargé de la réalisation de cette attente. */
	public Personne getResponsable() {
		return responsable;
	}

	/** Définie le coéquipier chargé de la réalisation de cette attente. Peut être null. */
	public void setResponsable(Personne responsable) {
		this.responsable = responsable;
	}

	/** Renvoie la liste (du plus vieux au plus récent) des messages laissés par le Product Owner lorsqu'il n'a pas validé cette tâche. Peut être vide. */
	public String[] getMessagesPO() {
		return Arrays.copyOf(messagesPO.toArray(), messagesPO.size(), String[].class);
	}

	/** Ajoute un message laissé par le Product Owner lorsqu'il n'a pas validé cette tâche. */
	public void addMessagePO(String message) {
		this.messagesPO.add(message);
	}

	public void setNumeroIteration(int n) {
		if (n <= 0)
			throw new IllegalArgumentException("Le numéro d'itération doit être strictement positif.");
		if (n > projet.getNbIterations())
			throw new IllegalArgumentException("Le projet contient seulement "+projet.getNbIterations()+" itérations.");
		
		this.iteration = n;
	}
	
	/** Retourne 0 si l'itération n'a pas été définie sinon 1 pour l'itération n°1...*/
	public int getNumeroIteration() {
		return iteration;
	}
	
	/** Retourne l'itération contenant l'attente (ou null).*/
	public Iteration getIteration() {
		if (iteration == 0) {
			return null;
		}
		return projet.getIteration(iteration);
	}
	
	public void setDifficulte(float d) {
		if (d < 0)
			throw new IllegalArgumentException("La difficulté doit être positive ou nulle.");
		this.difficulte = d;
	}
	
	/** Retourne -1 si la difficulté n'a pas encore été entrée. */
	public float getDifficulte() {
		return difficulte;
	}
	
	public Projet getProjet() {
		return projet;
	}
	
	void setprojet(Projet p) {
		this.projet = p;
	}
	
	public void setEtat(EtatAttente etat) {
		this.etat = etat;
	}
	
	public EtatAttente getEtat() {
		return this.etat;
	}
}
