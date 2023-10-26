package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import modele.Attente;
import modele.Base;
import modele.EtatAttente;
import modele.Personne;
import modele.Projet;

public class TestGson {
	
	@Test
	public void unserializeProjet() {
		Base b = Base.getInstance();
		
		String t = "{\"projets\":[{\"nom\":\"Covoiturage\",\"description\":\"Application Android pour créer et rejoindre des covoiturages\",\"productOwner\":{\"login\":\"brutus\",\"nom\":\"Brutus\",\"prenom\":\"Philippe\"},\"scrumMaster\":{\"login\":\"brutus\",\"nom\":\"Brutus\",\"prenom\":\"Philippe\"},\"equipiers\":[{\"login\":\"bouchard\",\"nom\":\"Bouchard\",\"prenom\":\"Valentin\",\"poste\":\"développeur Android\"},{\"login\":\"bouillet\",\"nom\":\"Bouillet\",\"prenom\":\"Rémi\",\"poste\":\"développeur Java\"},{\"login\":\"hemery\",\"nom\":\"Hemery\",\"prenom\":\"Emilien\",\"poste\":\"développeur web\"},{\"login\":\"lefevre\",\"nom\":\"Lefevre\",\"prenom\":\"Jeanne-Emma\",\"poste\":\"développeur web\"},{\"login\":\"rouvres\",\"nom\":\"Rouvres\",\"prenom\":\"Mathis\",\"poste\":\"développeur Android\"}],\"debut\":\"Dec 13, 2021, 12:00:00 AM\",\"iterations\":[{\"debut\":\"Dec 13, 2021, 12:00:00 AM\",\"fin\":\"Dec 19, 2021, 11:59:59 PM\",\"duree\":7,\"numero\":1},{\"debut\":\"Dec 20, 2021, 12:00:00 AM\",\"fin\":\"Dec 26, 2021, 11:59:59 PM\",\"duree\":7,\"numero\":2},{\"debut\":\"Dec 27, 2021, 12:00:00 AM\",\"fin\":\"Jan 2, 2022, 11:59:59 PM\",\"duree\":7,\"numero\":3},{\"debut\":\"Jan 3, 2022, 12:00:00 AM\",\"fin\":\"Jan 9, 2022, 11:59:59 PM\",\"duree\":7,\"numero\":4}],\"dureeIteration\":7,\"attentes\":[{\"nom\":\"chercher trajet\",\"description\":\"chercher un trajet en spécifiant la date, le point de départ et le point d\\u0027arrivé\",\"valeur\":2500,\"iteration\":1,\"difficulte\":21.0,\"etat\":\"VALIDE\"},{\"nom\":\"proposer covoit\",\"description\":\"proposer un trajet (date, départ, arrivée, nombre de places)\",\"valeur\":1700,\"iteration\":2,\"difficulte\":5.0,\"etat\":\"VALIDE\"},{\"nom\":\"déposer demande\",\"description\":\"déposer une demande de trajet (date, départ, arrivée)\",\"valeur\":1700,\"iteration\":2,\"difficulte\":5.0,\"etat\":\"VALIDE\"},{\"nom\":\"s\\u0027inscrire\",\"description\":\"s\\u0027inscrire (mail, nom, prénom...)\",\"valeur\":1700,\"iteration\":2,\"difficulte\":5.0,\"etat\":\"VALIDE\"},{\"nom\":\"renseigner infos trajets\",\"description\":\"permettre de renseigner les infos d\\u0027un trajet (date, départ, arrivée)\",\"valeur\":1300,\"iteration\":2,\"difficulte\":13.0,\"etat\":\"A_VALIDER\"},{\"nom\":\"être alerté du retard\",\"description\":\"chaque passager peut avertir qu\\u0027il va arriver en retard\",\"valeur\":1300,\"iteration\":3,\"difficulte\":8.0,\"etat\":\"EN_COURS\"},{\"nom\":\"déclarer codes passagers\",\"description\":\"générer un code sécurisé pour chaque passager\",\"valeur\":900,\"iteration\":3,\"difficulte\":8.0,\"etat\":\"A_FAIRE\"},{\"nom\":\"régler et obtenir code passager\",\"description\":\"vérifier que le conducteur rentre le bon code pour valider le paiment\",\"valeur\":900,\"iteration\":4,\"difficulte\":21.0,\"etat\":\"A_FAIRE\"}]}]}";

		b.charger(t);
		assertThrows(IllegalStateException.class, ()->{b.charger(t);});
		
		assertEquals(b.getProjets().length, 1);
		assertEquals(b.getPersonnes().length, 6);
        
        Projet p = b.getProjets()[0];
        assertEquals(p.getNom(), "Covoiturage");
        assertEquals(p.getDescription(), "Application Android pour créer et rejoindre des covoiturages");
        
        Personne per = p.getEquipiers()[0];
        
        assertTrue(per == Personne.get("bouchard"));
        assertEquals(per.getNom(), "Bouchard");
        assertEquals(per.getPrenom(), "Valentin");
        assertEquals(per.getLogin(), "bouchard");
        assertEquals(per.getPoste(), "développeur Android");
        
        assertEquals(p.getProductOwner(), Personne.get("brutus"));
        assertEquals(p.getScrumMaster(), Personne.get("brutus"));
        assertTrue(p.getProductOwner() == p.getScrumMaster());
        
        assertEquals(p.getAttentes().length, 8);
        
        Attente a = p.getAttentes()[2];
        assertEquals(a.getNom(), "déposer demande");
        assertEquals(a.getDescription(), "déposer une demande de trajet (date, départ, arrivée)");
        assertEquals(a.getValeur(), 1700);
        assertEquals(a.getDifficulte(), 5);
        assertEquals(a.getEtat(), EtatAttente.VALIDE);
        
        // TODO : tester les itérations
        
	}

}
