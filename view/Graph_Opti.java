package view;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.Axis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
 
 
public class Graph_Opti extends Application {


    public void start1(Stage stage, int[][][] Liste, int size, int n) {
        stage.setTitle("Comparaison");
        final Axis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
         xAxis.setLabel("Position (dm)");
         yAxis.setLabel("Vitesse (dm/s)");
         
         
        final LineChart<Number,Number> lineChart = 
                new LineChart<Number,Number>(xAxis,yAxis);
       
        lineChart.setTitle("Car Methods Comparaison");
        
        List<XYChart.Series> list=new ArrayList<XYChart.Series>();
        for(int i=1; i<size+1; i++){
        	XYChart.Series series = new XYChart.Series();
            series.setName("Sol. " +i);
        	list.add(series);      	
        }
        int compteur = 0;
        for(XYChart.Series s : list){
        	
        	for(int j=0; j<n;j++){
            	s.getData().add(new XYChart.Data(Liste[compteur][0][j],Liste[compteur][1][j]));

        	}
        	lineChart.getData().addAll(s);
        	compteur++;
        }


        
        Scene scene  = new Scene(lineChart,800,600);       
        
       
        stage.setScene(scene);
        stage.show();
    }
    
    
    public void start2(Stage stage, int[][][] Liste, int size, int n, List<Integer> Solutions) {

    	
        stage.setTitle("Comparaison");
        final Axis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
         xAxis.setLabel("Position (dm)");
         yAxis.setLabel("Vitesse (dm/s)");
         
        final LineChart<Number,Number> lineChart = 
                new LineChart<Number,Number>(xAxis,yAxis);
       
        lineChart.setTitle("Car Methods Comparaison");
        
        List<XYChart.Series> list=new ArrayList<XYChart.Series>();
        for(int i=0; i<Solutions.size(); i++){
        	XYChart.Series series = new XYChart.Series();
            series.setName("Sol. " + (int)(Solutions.get(i)));
        	list.add(series);      	
        }
        
        int compteur = 0;
        
        for(XYChart.Series s : list){
        	
        	for(int j=0; j<n;j++){
            	s.getData().add(new XYChart.Data(Liste[Solutions.get(compteur)-1][0][j],Liste[Solutions.get(compteur)-1][1][j]));
        	}
        	lineChart.getData().addAll(s);
        	compteur++;
        }
        
        Scene scene  = new Scene(lineChart,800,600);       
        
        stage.setScene(scene);
        stage.show();
    }
    
    

    public void demarrer2(Stage gameStage, int[][][] Liste, int size, int n, List<Integer> Solutions){
    	gameStage.hide();
    	Stage GraphStage = new Stage();


    	
    	start2(GraphStage, Liste, size, n, Solutions);
    	
    	
    }
    
    public void demarrer1(Stage gameStage, int[][][] Liste, int size, int n){
    	gameStage.hide();
    	Stage GraphStage = new Stage();

    	start1(GraphStage, Liste, size, n);
    	
    	
    }


	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
	}
    
    
    
    
    
    
    
}