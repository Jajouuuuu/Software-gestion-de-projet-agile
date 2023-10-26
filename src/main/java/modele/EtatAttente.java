package modele;

public enum EtatAttente {
	A_FAIRE("À faire"), EN_COURS("En cours"), A_VALIDER("À valider"), VALIDE("Validé");
	
	public final String nom;

	private EtatAttente(String nom) {
		this.nom = nom;
	}
}
