package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.*;

import modele.Attente;
import modele.Base;
import modele.Iteration;
import modele.Personne;
import modele.Projet;

@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class TestModele {
	
	@Test
	public void test01Personne() {
		int l = Personne.getPersonnes().length;
		
		Personne p1 = new Personne("bouchard2", "Bouchard", "Valentin");
		assertEquals(Personne.getPersonnes().length, l+1);
		Personne p2 = new Personne("bouillet2", "Bouillet", "Rémi", "développeur Java");
		assertEquals(Personne.getPersonnes().length, l+2);
		
		assertThrows(IllegalArgumentException.class, ()->new Personne("bouchard2", "", ""));
		assertEquals(Personne.getPersonnes().length, l+2);
		
		assertEquals(p1.getLogin(), "bouchard2");
		assertEquals(p1.getNom(), "Bouchard");
		assertEquals(p1.getPrenom(), "Valentin");
		assertNull(p1.getPoste());
		
		assertEquals(p2.getPoste(), "développeur Java");
		
		assertEquals(Personne.get("bouchard2"), p1);
		assertEquals(Personne.get("bouillet2"), p2);
	}
	
	@Test
	public void test02ConstructeurProjet() {
		int l = Projet.getProjets().length;
		
		Personne admin = new Personne("admin", "Administrateur", "Super");
		Projet p1 = new Projet("p1", "desc1", admin);
		
		assertEquals(Projet.getProjets().length, l+1);
		
		new Projet("p2", "desc2", admin);
		
		assertEquals(Projet.getProjets().length, l+2);
		
		assertEquals(p1.getNom(), "p1");
		assertEquals(p1.getDescription(), "desc1");
		assertEquals(p1.getNbIterations(), 0);
		assertNull(p1.getDebut());
		assertEquals(p1.getDureeIteration(), 0);
		assertNull(p1.getScrumMaster());
		assertEquals(p1.getProductOwner(), admin);
		assertEquals(p1.getEquipiers().length, 0);
		
		p1.ajouterEquipier(new Personne("equip1", "Numero1", "Equipier"));
		assertEquals(p1.getEquipiers().length, 1);
		assertEquals(p1.getEquipiers()[0], Personne.get("equip1"));
		
	}
	
	@Test
	public void test03AjoutAttente() {
		Projet p = Base.getInstance().getProjet("p1");
		assertEquals(p.getAttentes().length, 0);
		p.ajouterAttente(new Attente("n1", "d1", 1000, p));
		assertEquals(p.getAttentes().length, 1);
		
		Attente a = p.getAttentes()[0];
		assertEquals(a.getNom(), "n1");
		assertEquals(a.getDescription(), "d1");
		assertEquals(a.getValeur(), 1000);
		assertEquals(a.getDifficulte(), -1);
		assertEquals(a.getNumeroIteration(), 0);	
	}
	
	@Test
	public void test04ConfigurationIterations() {
		Projet p = Projet.getProjets()[0];
		
		Calendar cal = Calendar.getInstance();
    	cal.setTime(new Date()); // maintenant
    	cal.add(Calendar.DAY_OF_MONTH, -16);
    	p.setDateDebut(cal.getTime()); // aujourd'hui - 16 jours
    	p.setIterations(4, 7);
    	
    	assertEquals(p.getNbIterations(), 4);
    	assertEquals(p.getDureeIteration(), 7);
    	cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
    	assertEquals(p.getIteration(1).getDebut(), cal.getTime());
    	assertEquals(p.getIteration(1).getDuree(), 7);
    	
    	Iteration iter;
    	for (int i = 1; i <= p.getNbIterations(); i++) {
    		iter = p.getIteration(i);
    		assertEquals(iter.getNumero(), i);
    		assertEquals(iter.getDuree(), 7);
    		assertEquals(iter.getDifficultePlanifiee(), 0);
    		assertEquals(iter.getDebut(), cal.getTime());
    		
    		assertEquals(Duration.between(iter.getDebut().toInstant(), iter.getFin().toInstant()).toDays(), 6);
    		
    		cal.add(Calendar.DAY_OF_MONTH, 7);
    	}
    	
    	
	}
}
