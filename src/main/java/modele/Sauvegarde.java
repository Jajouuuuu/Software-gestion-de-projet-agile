package modele;

import java.io.IOException;

import vue.App;

public class Sauvegarde implements Runnable {
	public static final int deltaTime = 10; // en minutes

	@Override
	public void run() {
		System.out.println("Thread de sauvegarde lancé, la base sera sauvegardée toutes les "+deltaTime+" minutes");
		
		while (true) {
			try {
				Thread.sleep(deltaTime * 60 * 1000);
			} catch (InterruptedException e1) {
				break;
			}
			
			try {
				Base.getInstance().sauvegarder(App.cheminBase);
			} catch (IOException e) {
				System.err.println("La sauvegarde de la base a échoué !");
			}
		}
	}

}
