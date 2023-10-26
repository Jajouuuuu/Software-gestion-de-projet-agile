package modele;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Projet {
	private String nom;
	private String description;
	private Personne productOwner;
	private Personne scrumMaster;
	private List<Personne> equipiers;
	private Date debut;
	private Date fin;
	private List<Iteration> iterations;
	private int dureeIteration;
	private List<Attente> attentes;

	public Projet(String nom, String description, Personne po) {
		if (nom.isEmpty())
			throw new IllegalArgumentException("Le nom du projet ne peut pas être vide.");
		if (description.isEmpty())
			throw new IllegalArgumentException("La description du projet ne peut pas être vide.");
		if (po == null)
			throw new IllegalArgumentException("Le Product Owner doit être spécifié.");
		
		this.nom = nom;
		this.description = description;
		this.productOwner = po;
		this.equipiers = new ArrayList<Personne>();
		this.attentes = new ArrayList<Attente>();
		this.iterations = new ArrayList<Iteration>();
		Base.getInstance().ajouterProjet(this);
	}

	/** alias pour Base.getInstance().getProjets() */
	public static Projet[] getProjets() {
		return Base.getInstance().getProjets();
	}

	public void setScrumMaster(Personne sm) {
		this.scrumMaster = sm;
	}

	public Personne getScrumMaster() {
		return scrumMaster;
	}

	public String getNom() {
		return nom;
	}

	public String getDescription() {
		return description;
	}

	public Personne getProductOwner() {
		return productOwner;
	}

	public Personne[] getEquipiers() {
		Object[] tmp = equipiers.toArray();
		return Arrays.copyOf(tmp, tmp.length, Personne[].class);
	}

	public void ajouterEquipier(Personne p) {
    	if (debut != null && debut.before(new Date(System.currentTimeMillis()))) {
    		throw new IllegalStateException("Impossible d'ajouter des équipiers car le projet a commencé.");
    	}
    	if (p == null) {
    		throw new IllegalArgumentException("Aucune personne n'est spécifiée.");
    	}
    	if (equipiers.contains(p)) {
    		throw new IllegalArgumentException("Cette personne fait déjà partie équipiers.");
    	}
		equipiers.add(p);
	}
	
	public void supprimerEquipier(Personne p) {
    	if (debut != null && debut.before(new Date(System.currentTimeMillis()))) {
    		throw new IllegalStateException("Impossible de supprimer un équipier car le projet a commencé.");
    	}
    	if (p == null) {
    		throw new IllegalArgumentException("Aucun équipier n'est spécifié.");
    	}
    	if (!equipiers.contains(p)) {
    		throw new IllegalArgumentException("Cette personne ne fait pas partie des équipiers.");
    	}
		equipiers.remove(p);
	}

	public void setDateDebut(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		this.debut = cal.getTime();		
	}
	
	/** Date du dernier jour (fin du projet à 23h59) */
	public void setDateFin(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		this.fin = cal.getTime();
	}
	
	public boolean iterationsConfigurees() {
		return !iterations.isEmpty();
	}

	/** Cette fonction va remplir la liste d'itérations.
	 * @param duree est la durée d'une itération, la 2e itération commencera duree+1 jours après le début.*/
	public void setIterations(int nb, int duree) {
		if (iterationsConfigurees()) {
			throw new IllegalStateException("Les itérations de ce projet ont déjà été configurées.");
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(getDebut());
		for (int i = 0; i < nb; i++) {
			iterations.add(new Iteration(cal.getTime(), duree, i+1, this));
			cal.add(Calendar.DATE, duree);
		}
		dureeIteration = duree;		
	}

	public void ajouterAttente(Attente attente) {
		this.attentes.add(attente);		
	}

	public Attente[] getAttentes() {
		Object[] tmp = attentes.toArray();
		return Arrays.copyOf(tmp, tmp.length, Attente[].class);
	}
	
	/** Renvoie le numéro de l'attente. Chaque attente a un numéro unique dans le projet.
	 * Renvoie -1 si l'attente n'est pas dans le projet.
	 *  */
	public int getNumeroAttente(Attente a) {
		return attentes.indexOf(a);
	}
	
	/** Renvoie une attente à partir de son numéro (récupérable avec getNumeroAttente).  */
	public Attente getAttente(int n) {
		return attentes.get(n);
	}
	
	public void modifierAttente(int num, Attente nvlAttente) {
		attentes.set(num, nvlAttente);
	}

	/**
	 * 
	 * @param num le numéro de l'attente à supprimer
	 * @return l'attente supprimée
	 */
	public Attente supprimerAttente(int num) {
		Attente copie = attentes.get(num);
		attentes.remove(num);
		return copie;
	}
	
	public Date getDebut() {
		return debut;
	}
	
	public Date getFin() {
		return fin;
	}

	/** Renvoie 0 si les itérations n'ont pas été configurées. */
	public int getNbIterations() {
		return iterations.size();
	}
	
	/** Retourne l'itération n°i (i commence à 1). */
	public Iteration getIteration(int i) {
		return iterations.get(i-1);
	}
	
	/** Retourne l'itération en cours actuellement (peut être null). */
	public Iteration getIterationEnCours() {
		Iteration rep = null;
		for (Iteration i: iterations) {
			if (i.estCommencee() && !i.estTerminee())
				return i;
		}
		return rep;
	}
	
	public Iteration[] getIterations() {
		return Arrays.copyOf(iterations.toArray(), iterations.size(), Iteration[].class);
	}
	
	public Attente[] getAttentesDansAucuneIteration() {
		LinkedList<Attente> rep = new LinkedList<Attente>();
		for (Attente att: getAttentes()) {
			if (att.getNumeroIteration() == 0) {
				rep.add(att);
			}
		}
		
		return Arrays.copyOf(rep.toArray(), rep.size(), Attente[].class);
	}

	public int getDureeIteration() {
		return dureeIteration;
	}
	
	/** Renvoie la somme de la difficulté de toutes les attentes. */
	public float getDifficulteTotale() {
		float rep = 0;
		for (Attente a: attentes) {
			if (a.getDifficulte() > 0)
				rep += a.getDifficulte();
		}
		return rep;
	}
	
	/** Renvoie la somme de la difficulté des attentes non validées. */
	public float getDifficulteRestante() {
		float rep = 0;
		for (Attente a: attentes) {
			if (a.getDifficulte() > 0 && a.getEtat() != EtatAttente.VALIDE)
				rep += a.getDifficulte();
		}
		return rep;
	}
	
	/** Renvoie la somme de la valeur de toutes les attentes. */
	public int getValeurTotale() {
		int rep = 0;
		for (Attente a: attentes) {
			rep += a.getValeur();
		}
		return rep;
	}
	
	/** Renvoie la somme de la valeur des attentes validées. */
	public int getValeurLivree() {
		int rep = 0;
		for (Attente a: attentes) {
			if (a.getEtat() == EtatAttente.VALIDE) {
				rep += a.getValeur();
			}
		}
		return rep;
	}
	
	/**
	 * Repartit intelligemment dans les itérations toutes les attentes qui ne sont dans aucune itération.
	 */
	public void repartirAttentesDansIterations() {
		if (!iterationsConfigurees()) {
			throw new IllegalStateException("Les itérations de ce projet n'ont pas encore été configurées.");
		}
		
		LinkedList<Attente> aPlacer = new LinkedList<Attente>();
		
		for (Attente a: attentes) {
			if (a.getNumeroIteration() == 0)
				aPlacer.add(a);
		}
		
		float moyDifficultes = getDifficulteTotale()/getNbIterations();
		Attente attenteValeurMax;
		
		int iteration = 1;
		while (!aPlacer.isEmpty()) {
			attenteValeurMax = aPlacer.getFirst();
			
			for (Attente a: aPlacer) {
				if (a.getValeur() > attenteValeurMax.getValeur()) {
					attenteValeurMax = a;
				}
			}
			
			if (iteration < getNbIterations()
					&& getIteration(iteration).getDifficultePlanifiee() + attenteValeurMax.getDifficulte()
							 > moyDifficultes
					&& getIteration(iteration).getDifficultePlanifiee() + attenteValeurMax.getDifficulte() - moyDifficultes
							 > moyDifficultes - getIteration(iteration).getDifficultePlanifiee()) {
				iteration++;
			}
			
			attenteValeurMax.setNumeroIteration(iteration);
			aPlacer.remove(attenteValeurMax);
		}
	}
	
	/**
	 * Renvoie true ssi la personne fait partie du projet (en tant qu'équipier, SM ou PO).
	 */
	public boolean aBesoinDe(Personne pers) {
		if (productOwner == pers)
			return true;
		if (scrumMaster == pers)
			return true;
		for (Personne p: equipiers) {
			if (p == pers)
				return true;
		}
		return false;
	}
	
	/**
	 * Renvoie une IllegalStateException si les itérations ne sont pas configurées.
	 * Sinon, rajoute une nouvelle itération après la dernière, de même durée que
	 * les autres.
	 */
	public void nouvelleIteration() {
		if (!iterationsConfigurees()) {
			throw new IllegalStateException("Les itérations de ce projet n'ont pas encore été configurées.");
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(iterations.get(iterations.size()-1).getFin());
		cal.add(Calendar.DATE, 1);
		iterations.add(new Iteration(cal.getTime(), dureeIteration, iterations.size()+1, this));	
	}
	
	/**
	 * Lève une IllegalStateException si la dernière itération n'est pas vide ou qu'elle est commencée. Sinon, supprime la dernière itération.
	 */
	public void retirerDerniereIteration() {
		Iteration derniere = iterations.get(iterations.size()-1);
		if (derniere.getAttentes().length > 0) {
			throw new IllegalStateException("Impossible de supprimer la dernière itération car elle contient des attentes.");
		}
		if (derniere.estCommencee()) {
			throw new IllegalStateException("Impossible de supprimer la dernière itération car elle est commencée.");
		}
		iterations.remove(derniere);	
	}

	@Override
	public String toString() {
		return "Projet "+nom;
	}

}
