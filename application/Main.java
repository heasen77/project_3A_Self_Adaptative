package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import view.Simulation_console_methode3;
import view.Simulation_global_method1;
import view.Simulation_CSP_test;
import view.Simulation_Objective;
import view.Simulation_Opti_Test;
import view.Simulation_console_CSPG1;
import view.Simulation_console_CSPGlobal1;
import view.Simulation_console_methode1;
import view.Simulation_console_methode2;
import view.ViewManager;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import java.util.Scanner;

import org.chocosolver.solver.Model; 

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			ViewManager manager = new ViewManager();
			primaryStage = manager.getMainStage();
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		boolean test = false;
		while(test == false) {
			System.out.println("Simulation Console méthode 1 (1) ou la methode 2(2) ou la methode 3 (3) ou la méthode Global (4) ou l'IHM+Console?(5)");
			String str = sc.nextLine();
			System.out.println(str);
			if(Integer.parseInt(str) == 1) {
				System.out.println("Simulation Console méthode 1 lancée");
				test = true;
				int[] initTL1 = {0, 1, 5, 5};
		        boolean[] initTL2 = {false, false, true, false};
		        int[] initCar = {-30, 10, 0, 30, -4, 3};        // Car(int initialPosition, int initialSpeed, int initialAcceleration, int vMax, int aMin, int aMax)
		        Simulation_console_methode1 simulation = new Simulation_console_methode1(1, 10, initTL1, initTL2, initCar);

		        for (int i = 0; i <= 15; i++){
		            System.out.println("\n - Résultat du CSP :");
		            simulation.OneLap(-100, 14, 1);
		        }
				
			}
			if(Integer.parseInt(str) == 2) {
				System.out.println("Simulation Console méthode 2 lancée");
				int[] initTL1 = {0, 1, 10, 10};
		        boolean[] initTL2 = {false, false, true, false};
		        int[] initCar = {-110, 10, 0, 30, -4, 3};
		        Simulation_console_methode2 simulation = new Simulation_console_methode2(1, 10, initTL1, initTL2, initCar);

		        for (int i = 0; i <= 20; i++){
		            System.out.println("\n -CSP Results :");
		            simulation.OneLap(-100, 14);

		        }
				
			}
			
			
			
			if(Integer.parseInt(str) == 3) {
				System.out.println("Simulation Console méthode 3 lancée");
				int[] initTL1 = {0, 1, 10, 10};
		        boolean[] initTL2 = {false, false, false, true};
		        int[] initCar = {-110, 10, 0, 30, -4, 3};
		        Simulation_console_methode3 simulation = new Simulation_console_methode3(1, 10, initTL1, initTL2, initCar);

		        for (int i = 0; i <= 26; i++){
		            System.out.println("\n -CSP Results :");
		            simulation.OneLap(-100, 14);

		        }
				
			}
			
			if(Integer.parseInt(str) == 4) {
				System.out.println("Simulation Console méthode globale");
		        // Initialisation TL: position, Period green, yellow, red
		        int[] initTL1 = {0, 3, 10, 10};
		        // Initialisation TL: degraded, red, yellow, green
		        boolean[] initTL2 = {false, false, true, false};
		        int[] initCar = {-50, 10, 0, 30, -4, 3};
		        //Simulation_console_CSPGlobal1 simulation = new Simulation_console_CSPGlobal1(1, 10, initTL1, initTL2, initCar);
		        Simulation_console_CSPGlobal1 simulation = new Simulation_console_CSPGlobal1(1, 10, initTL1, initTL2, initCar);
		        simulation.Simulation(-100, 100, 14);
			}
			
			
			
			if(Integer.parseInt(str) == 5) {
				test = true;
				launch(args);
				System.out.println("Simulation IHM lancée");
			}
			
			if(Integer.parseInt(str) == 6) {
				System.out.println("Simulation Console méthode globale Opti");
		        // Initialisation TL: position, Period green, yellow, red
		        int[] initTL1 = {0, 1, 10, 10};
		        // Initialisation TL: degraded, red, yellow, green
		        boolean[] initTL2 = {false, false, false, true};
		        int[] initCar = {-40, 5, 0, 30, -4, 3};
		        //Simulation_console_CSPGlobal1 simulation = new Simulation_console_CSPGlobal1(1, 10, initTL1, initTL2, initCar);
		        Simulation_Objective simulation = new Simulation_Objective(1, 10, initTL1, initTL2, initCar);
		        simulation.Simulation(-100, 300, 14);
			}
			
			
			if(Integer.parseInt(str) == 7) {
				System.out.println("Simulation Console méthode globale Opti");
		        // Initialisation TL: position, Period green, yellow, red
		        int[] initTL1 = {0, 3, 10, 10};
		        // Initialisation TL: degraded, red, yellow, green
		        boolean[] initTL2 = {false, false, true, false};
		        int[] initCar = {-80, 5, 0, 30, -4, 3};		        //Simulation_console_CSPGlobal1 simulation = new Simulation_console_CSPGlobal1(1, 10, initTL1, initTL2, initCar);
		        Simulation_Opti_Test simulation = new Simulation_Opti_Test(1, 10, initTL1, initTL2, initCar);
		        simulation.Simulation(-100, 300, 14);
			}
			
			if(Integer.parseInt(str) == 8) {
				System.out.println("Simulation Console méthode globale Opti");
		        // Initialisation TL: position, Period green, yellow, red
		        int[] initTL1 = {0, 3, 10, 10};
		        // Initialisation TL: degraded, red, yellow, green
		        boolean[] initTL2 = {false, false, false, true};
		        int[] initCar = {-100, 13, 0, 30, -4, 3};		        //Simulation_console_CSPGlobal1 simulation = new Simulation_console_CSPGlobal1(1, 10, initTL1, initTL2, initCar);
		        Simulation_global_method1 simulation = new Simulation_global_method1(1, 10, initTL1, initTL2, initCar);
		        simulation.Simulation(-100, 100, 14);
			}
			if(Integer.parseInt(str) == 9) {
				System.out.println("Simulation Console méthode globale Opti");
				Model model = new Model("Simulation");
		        // Initialisation TL: position, Period green, yellow, red
		        int[] initTL1 = {0, 3, 10, 10};
		        // Initialisation TL: degraded, red, yellow, green
		        boolean[] initTL2 = {false, false, true, false};
		        int[] initCar = {-50, 13, 0, 30, -4, 3};		        //Simulation_console_CSPGlobal1 simulation = new Simulation_console_CSPGlobal1(1, 10, initTL1, initTL2, initCar);
		        Simulation_console_CSPG1 simulation = new Simulation_console_CSPG1(1, 10, initTL1, initTL2, initCar);
		        simulation.Simulation(model,-100, 300, 14);
			}
			
			
			
			
		}
		sc.close();
		
		
	}
}
