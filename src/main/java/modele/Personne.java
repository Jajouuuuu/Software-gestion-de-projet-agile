package modele;

public class Personne {
	private String login;
	private String nom;
	private String prenom;
	private String poste;
	
	public Personne() throws NoSuchMethodException {
		throw new NoSuchMethodException("Le constructeur par défaut de Personne ne doit pas être appelé.");
	}

	public Personne(String login, String nom, String prenom) {
		if (prenom.isEmpty()) {
			throw new IllegalArgumentException("Un prénom ne peut pas être vide");
		}
		if (nom.isEmpty()) {
			throw new IllegalArgumentException("Un nom ne peut pas être vide");
		}
		if (login.isEmpty()) {
			throw new IllegalArgumentException("Un login ne peut pas être vide");
		}
		this.login = login;
		this.nom = nom;
		this.prenom = prenom;
		
		Base.getInstance().ajouterPersonne(this);
	}

	public Personne(String login, String nom, String prenom, String poste) {
		this(login, nom, prenom);
		this.poste = poste;
	}

	/** alias pour Base.getInstance().getPersonne(login) */
	public static Personne get(String login) {
		return Base.getInstance().getPersonne(login);
	}

	/** alias pour Base.getInstance().getPersonnes() */
	public static Personne[] getPersonnes() {
		return Base.getInstance().getPersonnes();
	}

	public String getLogin() {
		return login;
	}

	public String getNom() {
		return nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public String getPoste() {
		return poste;
	}

	@Override
	public String toString() {
		return prenom+" "+nom;
	}

}
