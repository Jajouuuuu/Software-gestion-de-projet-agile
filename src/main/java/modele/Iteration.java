package modele;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

public class Iteration {
	private Date debut;
	private Date fin;
	private int duree;
	private int numero;
	private transient Projet projet;
	
	/**
	 * Une itération liée à un Projet.
	 * L'itération commence au matin de la date getDebut() et se finit
	 * le soir de la date getFin().
	 * @param debut date de début (heure forcée à minuit)
	 * @param duree durée en jours
	 * @param numero numéro de l'itération (> 0)
	 * @param projet projet correspondant
	 */
	public Iteration(Date debut, int duree, int numero, Projet projet) {
		this.duree = duree;
		this.numero = numero;
		this.projet = projet;
		
		Calendar cal = Calendar.getInstance();
		
		cal.setTime(debut);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		this.debut = cal.getTime();		
		
		cal.add(Calendar.DAY_OF_MONTH, duree-1);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		this.fin = cal.getTime();
	}

	public Date getDebut() {
		return debut;
	}
	
	/** La date de fin est duree-1 jours après la date de début. */
	public Date getFin() {
		return fin;
	}

	public int getDuree() {
		return duree;
	}

	public int getNumero() {
		return numero;
	}
	
	void setprojet(Projet p) {
		this.projet = p;
	}

	public Projet getProjet() {
		return projet;
	}
	
	public Attente[] getAttentes() {
		if (!projet.iterationsConfigurees())
			throw new IllegalStateException("Les itérations de ce projet n'ont encore été configurées.");
		
		LinkedList<Attente> rep = new LinkedList<Attente>();
		for (Attente att: projet.getAttentes()) {
			if (att.getNumeroIteration() == numero) {
				rep.add(att);
			}
		}
		
		return Arrays.copyOf(rep.toArray(), rep.size(), Attente[].class);
	}
	
	/** Retourne la somme des difficultés des attentes planifiées dans l'itération (ignore les attentes sans difficultés). */
	public float getDifficultePlanifiee() {
		float s = 0;
		for (Attente a: getAttentes()) {
			if (a.getDifficulte() > 0)
				s += a.getDifficulte();
		}
		
		return s;
	}
	
	/** Retourne la somme des difficultés des attentes non validées (ignore les attentes sans difficultés). */
	public float getDifficulteRestante() {
		float s = 0;
		for (Attente a: getAttentes()) {
			if (a.getDifficulte() > 0 && a.getEtat() != EtatAttente.VALIDE)
				s += a.getDifficulte();
		}
		
		return s;
	}
	
	/** Retourne la somme des difficultés des attentes validées (ignore les attentes sans difficultés). */
	public float getDifficulteRealisee() {
		float s = 0;
		for (Attente a: getAttentes()) {
			if (a.getDifficulte() > 0 && a.getEtat() == EtatAttente.VALIDE)
				s += a.getDifficulte();
		}
		
		return s;
	}
	
	/** Renvoie la somme des valeurs des attentes planifiées dans l'itération. */
	public int getValeurPlanifiee() {
		int s = 0;
		for (Attente a: getAttentes()) {
			s += a.getValeur();
		}
		
		return s;
	}
	
	/** Renvoie la somme des valeurs des attentes validées. */
	public int getValeurValide() {
		int s = 0;
		for (Attente a: getAttentes()) {
			if (a.getEtat() == EtatAttente.VALIDE) {
				s += a.getValeur();
			}
		}
		
		return s;
	}
	
	/** Renvoie true si la date de début est aujourd'hui ou après aujourd'hui. */
	public boolean estCommencee() {
		Date auj = new Date();
		
		return !auj.before(debut);
	}
	
	/** Renvoie true si aujourd'hui est strictement après la date de fin. */
	public boolean estTerminee() {
		Date auj = new Date();
		
		return auj.after(fin);
	}
	
	@Override
	public String toString() {
		String rep = "itération n°"+numero;
		if (estTerminee())
			rep += " (terminée depuis le "+DateFormat.getDateInstance().format(fin)+")";
		else if (estCommencee())
			rep += " (en cours, se termine le "+DateFormat.getDateInstance().format(fin)+")";
		else
			rep += " (commencera le "+DateFormat.getDateInstance().format(debut)+")";
		return rep;
	}
}
