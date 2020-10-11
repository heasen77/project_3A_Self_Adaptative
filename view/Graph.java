package view;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.Axis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
 
 
public class Graph extends Application {
 
    public void start3(Stage stage,  String name1, String name2 , String name3 , int[] X1, int[] V1, int[] X2, int[] V2, int[] X3, int[] V3, int n) {
        stage.setTitle("Comparaison");
        final Axis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
         xAxis.setLabel("Position (m)");
        final LineChart<Number,Number> lineChart = 
                new LineChart<Number,Number>(xAxis,yAxis);
       
        lineChart.setTitle("Car Methods Comparaison");
                          
        XYChart.Series series1 = new XYChart.Series();
        series1.setName(name1);
        
        XYChart.Series series2 = new XYChart.Series();
        series2.setName(name2);
        
        XYChart.Series series3 = new XYChart.Series();
        series3.setName(name3);
        
        
       // int n = X1.length;
        for(int i=0;i<n;i++){
        	
        	series1.getData().add(new XYChart.Data(X1[i], V1[i]));
        	series2.getData().add(new XYChart.Data(X2[i], V2[i]));
        	series3.getData().add(new XYChart.Data(X3[i], V3[i]));
        }

        
        Scene scene  = new Scene(lineChart,800,600);       
        lineChart.getData().addAll(series1, series2, series3);
       
        stage.setScene(scene);
        stage.show();
    }
    
    
    public void start2(Stage stage, String name1, String name2 , int[] X1, int[] V1, int[] X2, int[] V2, int n) {
        stage.setTitle("Comparaison");
        final Axis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
         xAxis.setLabel("Position (m)");
        final LineChart<Number,Number> lineChart = 
                new LineChart<Number,Number>(xAxis,yAxis);
       
        lineChart.setTitle("Car Methods Comparaison");
                          
        XYChart.Series series1 = new XYChart.Series();
        series1.setName(name1);
        
        XYChart.Series series2 = new XYChart.Series();
        series2.setName(name2);
        
        
      //  int n = X1.length;
        for(int i=0;i<n;i++){
        	
        	series1.getData().add(new XYChart.Data(X1[i], V1[i]));
        	series2.getData().add(new XYChart.Data(X2[i], V2[i]));
        }

        

        Scene scene  = new Scene(lineChart,800,600);       
        lineChart.getData().addAll(series1, series2);
       
        stage.setScene(scene);
        stage.show();
    }
    
    
    public void start1(Stage stage, String name, int[] X1, int[] V1, int n) {
        stage.setTitle("Comparaison");
        final Axis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
         xAxis.setLabel("Position (m)");
        final LineChart<Number,Number> lineChart = 
                new LineChart<Number,Number>(xAxis,yAxis);
       
        lineChart.setTitle("Car Methods Comparaison");
                          
        XYChart.Series series1 = new XYChart.Series();
        series1.setName(name);
        
        //int n = X1.length;
        for(int i=0;i<n;i++){
        	
        	series1.getData().add(new XYChart.Data(X1[i], V1[i]));

        }

        

        
        Scene scene  = new Scene(lineChart,800,600);       
        lineChart.getData().addAll(series1);
       
        stage.setScene(scene);
        stage.show();
    }
    
  
    public void demarrer3(Stage gameStage, int[] X1, int[] V1, int[] X2, int[] V2, int[] X3, int[] V3, boolean method1, boolean method2, boolean method3, boolean cspglobal, boolean cspglobal2, boolean cspglobal3, int compteur){
    	gameStage.hide();
    	Stage GraphStage = new Stage();
    	String name1 = new String();
    	String name2 = new String();
    	String name3 = new String();
    	if(method1&&method2&&method3){
    		name1 = "Method 1";
    		name2 = "Method 2";
    		name3 = "Method 3";
    		
    	}
    	if(method1&&method2&&cspglobal){
    		name1 = "Method 1";
    		name2 = "Method 2";
    		name3 = "Method CSP Global 1";
    		
    	}
    	
    	if(method2&&method3&&cspglobal){
    		name1 = "Method 2";
    		name2 = "Method 3";
    		name3 = "Method CSP Global 1";
    		
    	}
    	
    	
    	if(method1&&method3&&cspglobal){
    		name1 = "Method 1";
    		name2 = "Method 3";
    		name3 = "Method CSP Global 1";
    		
    	}
    	if(cspglobal3&&cspglobal2&&cspglobal){
    		name1 = "Method CSP Global 1";
    		name2 = "Method CSP Global 2";
    		name3 = "Method CSP Global 3";
    		
    	}
    	start3(GraphStage, name1, name2,name3, X1, V1, X2, V2, X3, V3, compteur);
    	
    	
    }
    
    public void demarrer2(Stage gameStage, int[] X1, int[] V1, int[] X2, int[] V2, boolean method1, boolean method2, boolean method3, boolean cspglobal, boolean cspglobal2, boolean cspglobal3, int compteur){
    	gameStage.hide();
    	Stage GraphStage = new Stage();
    	String name1 = new String();
    	String name2 = new String();
    	if(method1&&method2){
    		name1 = "Method 1";
    		name2 = "Method 2";
    		
    	}
    	if(method1&&method3){
    		name1 = "Method 1";
    		name2 = "Method 3";
    		
    	}
    	if(method2&&method3){
    		name1 = "Method 2";
    		name2 = "Method 3";
    		
    	}
    	if(method3&&cspglobal){
    		name1 = "Method 3";
    		name2 = "Method CSP Global 1";
    		
    	}
    	if(method2&&cspglobal){
    		name1 = "Method 2";
    		name2 = "Method CSP Global 1";
    		
    	}
    	
    	if(method1&&cspglobal){
    		name1 = "Method 1";
    		name2 = "Method CSP Global 1";
    		
    	}
    	if(method2&&cspglobal2){
    		name1 = "Method 2";
    		name2 = "Method CSP Global 2";

    	}
    	if(method3&&cspglobal3){
    		name1 = "Method 3";
    		name2 = "Method CSP Global 3";

    	}
    	
    	
    	start2(GraphStage, name1, name2, X1, V1, X2, V2, compteur);
    	
    	
    }
    
    
    public void demarrer1(Stage gameStage, int[] X1, int[] V1, boolean method1, boolean method2, boolean method3, boolean cspglobal, boolean cspglobal2, boolean cspglobal3, int compteur){
    	gameStage.hide();
    	Stage GraphStage = new Stage();
    	String name = new String();
    	if(method1){
    		name = "Method 1";
    	}
    	if(method2){
    		name = "Method 2";
    	}
    	if(method3){
    		name = "Method 3";
    	}
    	if(cspglobal){
    		name = "Method CSP Global 1";
    	}
    	if(cspglobal2){
    		name = "Method CSP Global 2";
    	}
    	if(cspglobal3){
    		name = "Method CSP Global 3";
    	}
    	start1(GraphStage, name, X1, V1, compteur);
    	
    	
    }


	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
	}
    
    
    
    
    
    
    
}