package vue;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import modele.Projet;

public class GenerateurCourbes {
	private Projet projet;

	public GenerateurCourbes(Projet p) {
		this.projet = p;
	}
	
	public LineChart<Number,Number> courbeTravail() {
		final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("itérations");
		final LineChart<Number,Number> lineChart = new LineChart<Number,Number>(xAxis,yAxis);
        lineChart.setTitle("Travail restant");

        lineChart.getData().add(getSerieTravailPlanifie());
        lineChart.getData().add(getSerieTravailReel());
        lineChart.setLegendVisible(false);
        
        return lineChart;
	}
	
	private Series<Number, Number> getSerieTravailPlanifie() {
		Series<Number, Number> series = new Series<Number, Number>();
        float tafRestant = projet.getDifficulteTotale();
        series.getData().add(new Data<Number, Number>(0, tafRestant));
        for (int i = 1; i <= projet.getNbIterations(); i++) {
        	tafRestant -= projet.getIteration(i).getDifficultePlanifiee();
			series.getData().add(new Data<Number, Number>(i, tafRestant));
		}
        return series;
	}
	
	private Series<Number, Number> getSerieTravailReel() {
		Series<Number, Number> series = new Series<Number, Number>();
        float tafRestant = projet.getDifficulteTotale();
        series.getData().add(new Data<Number, Number>(0, tafRestant));
        
        for (int i = 1; i <= projet.getNbIterations(); i++) {
        	if (!projet.getIteration(i).estTerminee()) {
				break;
			}
        	tafRestant -= projet.getIteration(i).getDifficulteRealisee();
			series.getData().add(new Data<Number, Number>(i, tafRestant));
		}
        return series;
	}
	
	public LineChart<Number,Number> courbeValeur() {
		final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("itérations");
		final LineChart<Number,Number> lineChart = new LineChart<Number,Number>(xAxis,yAxis);
        lineChart.setTitle("Valeur livrée");
        
        lineChart.getData().add(getSerieValeurPlanifiee());
        lineChart.getData().add(getSerieValeurReelle());
        lineChart.setLegendVisible(false);
        
        return lineChart;
	}
	
	private Series<Number, Number> getSerieValeurPlanifiee() {
		Series<Number, Number> series = new Series<Number, Number>();
        int valeurLivree = 0;
        series.getData().add(new Data<Number, Number>(0, valeurLivree));
		for (int i = 1; i <= projet.getNbIterations(); i++) {
			valeurLivree += projet.getIteration(i).getValeurPlanifiee();
			series.getData().add(new Data<Number, Number>(i, valeurLivree));
		}
        return series;
	}
	
	private Series<Number, Number> getSerieValeurReelle() {
		Series<Number, Number> series = new Series<Number, Number>();
        int valeurLivree = 0;
        series.getData().add(new Data<Number, Number>(0, valeurLivree));
        
		for (int i = 1; i <= projet.getNbIterations(); i++) {
			if (!projet.getIteration(i).estTerminee()) {
				break;
			}
			valeurLivree += projet.getIteration(i).getValeurValide();
			series.getData().add(new Data<Number, Number>(i, valeurLivree));
		}
        return series;
	}

}
