package view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.testng.internal.collections.Ints;

import Environement.Environement;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.BigInfoLabel;
import model.Carpicker;

import model.SmallInfoLabel;
import model.SmallInfoLabel2;
import model.TLpicker;

import model.Carurl;
import model.TLurl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.InputStream;
import java.lang.Integer.*;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

public class GameViewManager {
	
	
	//Hauteur et largeur de l'IHM
	private static final int GAME_WIDTH = 2400;
	private static final int GAME_HEIGHT = 550;
	
	private AnchorPane gamePane;
	private Scene gameScene;
	private Stage gameStage, menuStage;
	
	public ImageView car, car2, car3;
	private ImageView green, yellow, red, flashy;
	
	private boolean[] Lights_test;
	
	private boolean isEnterKeypressed;
	private boolean isExitKeypressed;
	private boolean isGreenKeypressed;
	private boolean isRedKeypressed;
	private boolean isYellowKeypressed;
	private boolean isDegratedKeypressed;
	private boolean graph_comparatif;
	private AnimationTimer gameTimer;
	
	private GridPane gridPane1;
	private final static String BACKGROUND_IMAGE = "view/ressources/backgroundroad4bis.png"; //background

	private int x0,v0, vlim, vmax, xmin,a0, amin, amax , unitScale, timeStep,a ;
	
	public static int ad;

	List<Carpicker> CarList;
	List<TLpicker> TLList;

	private SmallInfoLabel vitesse, vitesse1, vitesse2; 
	private SmallInfoLabel position, position1, position2;
	private SmallInfoLabel2 panneau_vit;

	
	private BigInfoLabel info_car1, info_car2, info_car3;
	
	private int vk, vk1, vk2;
	private int xk, xk1, xk2;
	private int compteur_N;
	private int N;
	
	private int[] X;
	private int[] V;
	private int[] X_CSP1;
	private int[] V_CSP1;
	private int[] green_CSP1, yellow_CSP1, red_CSP1;
	
	private int[] X_CSP2;
	private int[] V_CSP2;
	private int[] green_CSP2, yellow_CSP2, red_CSP2;
	
	
	private int[] X_CSP3;
	private int[] V_CSP3;
	private int[] green_CSP3, yellow_CSP3, red_CSP3;
	
	
	
	
	
	private int[] list_X1,list_X2, list_X3;
	private int[] list_V1,list_V2, list_V3;
	private String Actual_Light;
	private String Before_Light;
	private int compteur;
	
	private boolean green_bool;
	private boolean yellow_bool;
	private boolean red_bool;
	private boolean degrated_bool;
	private boolean[] initTL2;
	private boolean method1bool, method2bool, method3bool, cspglobal;
	
	private int greenint, yellowint, redint;
	
	private Simulation_console_methode1 Sim_Cons_1;
	private Simulation_console_methode2 Sim_Cons_2;
	private Simulation_console_methode3 Sim_Cons_3;
	private Simulation_CSP_test Sim_Cons_CSP1_test, Sim_Cons_CSP1_test1;
	private boolean doublemethods;
	private boolean triplemethods;
	
	private String initial_light;
	
	private ArrayList<ArrayList<Integer>> liste_CSPG, liste_CSPG1;
	private int[] acces_CSP1;
	private SmallInfoLabel vitesse3;
	private SmallInfoLabel position3;
	private int xk3;
	private int vk3;
	private int[][][] Liste_globale;
	private List<Integer> Solutions_generer;
	private int compteur_liste;
	private boolean cspopti;
	private ChoiceBox choice_opti;
	private boolean choix_charge;
	private int choix_solution;
	private Simulation_Objective Sim_Cons_Opti;
	private int i, compteur_opti;
	private ChoiceBox choiceBox;
	private boolean graph_comparatif_some;
	private Simulation_global_method2 Sim_Cons_Glob2, Sim_Cons_Glob2_bis;
	private Simulation_global_method3 Sim_Cons_Glob3, Sim_Cons_Glob3_bis;
	private boolean cspglobal2;
	private boolean cspglobal3;
	private int[] acces_CSP2;
	private int[] acces_CSP3;
	private ArrayList<ArrayList<Integer>> liste_CSPG2;
	private ArrayList<ArrayList<Integer>> liste_CSPG3; 
	
	
	

	
	public GameViewManager(int x0, int v0, int a0, int vmax, int amin, int amax, int vlim, int unit, int xmin, boolean method1, boolean method2, boolean method3,boolean cspglobal, boolean cspglobal2, boolean cspglobal3, boolean cspopti, String initial_light) {
		
		//pour tous les vegetaux avec une image qu'on a dans la class enum "vegetals", on récupère ses valeurs
		this.TLList =  new ArrayList<>();
		for (TLurl light : TLurl.values()) {
			TLpicker LightToPick = new TLpicker(light);
			TLList.add(LightToPick);
		}
		
		
		
	    //Initialisation des parametres algorithmiques 
	    this.unitScale = unit;
	    this.timeStep = 1;
	    this.compteur_opti = 1;
		this.compteur_liste = 1;
		this.compteur_N = 0;
		this.choix_solution = 1;
	    this.N = 30;
	    
	    //Initialisation des parametres initiaux de la voiture et de son environnement ( feux )
		this.x0 = x0*unit;
		this.v0 = v0*unit;
		this.xmin = xmin; 
		this.vlim = vlim; 
		this.vmax =vmax*unit;
		this.initial_light = initial_light;
		this.a0 = a0;
		this.amin = amin;
		this.amax = amax;
		int[] initCar = {x0, v0, a0, vmax, amin, amax}; 


		//Initialisation des listes permettant de stocker les valeurs des différentes positions de la voiture (et vitesse)
		this.X = new int[1000];
		this.V = new int[1000];
		this.list_X1 = new int[1000];
		this.list_V1 = new int[1000];
		this.list_X2 = new int[1000];
		this.list_V2 = new int[1000];
		this.list_X3 = new int[1000];
		this.list_V3 = new int[1000];
		this.X[0] = x0*unit;
		this.V[0] = v0*unit;
		this.list_X1[0] = x0;
		this.list_V1[0] = v0;
		this.list_X2[0] = x0;
		this.list_V2[0] = v0;
		this.list_X3[0] = x0;
		this.list_V3[0] = v0;
		this.vk = v0*unit;
		this.xk = x0*unit;
		this.vk1 = v0*unit;
		this.xk1 = x0*unit;
		this.vk2 = v0*unit;
		this.xk2 = x0*unit;
		this.xk3 = x0*unit;
		this.vk3 = v0*unit;
		this.compteur = 0;
		this.liste_CSPG =  new ArrayList<ArrayList<Integer>>();
		this.liste_CSPG2 =  new ArrayList<ArrayList<Integer>>();
		this.liste_CSPG3 =  new ArrayList<ArrayList<Integer>>();
	
		this.Solutions_generer = new ArrayList<Integer>();
		
	    
	    //Initialisation des feux de signalisations 
	    int[] initTL1 = {0, 5, 5, 5};
	    this.Actual_Light = initial_light;
	    this.Before_Light = initial_light;  
	    this.initTL2 = new boolean[4];
	    if(initial_light == "Green"){
	    	this.initTL2[0] = false;
	    	this.initTL2[1] = false;
	    	this.initTL2[2] = false;
	    	this.initTL2[3] = true;
	    	
		}
		else if(initial_light == "Red"){
			this.initTL2[0] = false;
	    	this.initTL2[1] = true;
	    	this.initTL2[2] = false;
	    	this.initTL2[3] = false;
		}
		else if(initial_light == "Yellow"){
			this.initTL2[0] = false;
	    	this.initTL2[1] = false;
	    	this.initTL2[2] = true;
	    	this.initTL2[3] = false;
		}
		else if(initial_light == "No working lights"){

			this.initTL2[0] = true;
	    	this.initTL2[1] = false;
	    	this.initTL2[2] = false;
	    	this.initTL2[3] = false;
		}
		
	    
	    
	    
	    this.method1bool = method1;
	    this.method2bool = method2;
	    this.method3bool = method3;
	    this.cspopti = cspopti;
	    this.cspglobal = cspglobal;
	    this.cspglobal2 = cspglobal2;
	    this.cspglobal3 = cspglobal3;

	    //Initialisation des différentes simulations selon ce que l'utilisateur a choisi
	    if(method1) {

	    	this.Sim_Cons_1 = new Simulation_console_methode1(timeStep, unit, initTL1, initTL2, initCar);
	    }
	    
	    if(method2) {
	    	this.Sim_Cons_2 = new Simulation_console_methode2(timeStep, unit, initTL1, initTL2, initCar);
	    }
	    
	    if(method3) {
	    	this.Sim_Cons_3 = new Simulation_console_methode3(timeStep, unit, initTL1, initTL2, initCar);
	    }

	    if(cspglobal){	
	    	//this.Sim_Cons_CSP1 = new Simulation_CSP(timeStep,unit, initTL1, initTL2, initCar); 
	    	this.Sim_Cons_CSP1_test = new Simulation_CSP_test(timeStep,unit, initTL1, initTL2, initCar, N); 

	    }
	    if(cspglobal2){	
	    	//this.Sim_Cons_CSP1 = new Simulation_CSP(timeStep,unit, initTL1, initTL2, initCar); 
	    	this.Sim_Cons_Glob2 = new Simulation_global_method2(timeStep,unit, initTL1, initTL2, initCar); 

	    }
	    if(cspglobal3){	
	    	//this.Sim_Cons_CSP1 = new Simulation_CSP(timeStep,unit, initTL1, initTL2, initCar); 
	    	this.Sim_Cons_Glob3  = new Simulation_global_method3(timeStep,unit, initTL1, initTL2, initCar); 

	    }
	    
		 //on initialise la simulation
	    if((method1 && method2 && !method3 && !cspglobal && !cspglobal2 && !cspglobal3) || (method2&&method3 && !method1 && !cspglobal && !cspglobal2 && !cspglobal3) || (method1&&method3 && !method2 && !cspglobal&& !cspglobal2 && !cspglobal3) || (method1&&cspglobal && !method2 && !method3&& !cspglobal2 && !cspglobal3) || (method2&&cspglobal && !method3 && !method1&& !cspglobal2 && !cspglobal3) || (method3&&cspglobal&& !method2 && !method1&& !cspglobal2 && !cspglobal3) || (!method3&&!cspglobal&& method2 && !method1&& cspglobal2 && !cspglobal3) || (method3&&!cspglobal&& !method2 && !method1&& !cspglobal2 && cspglobal3)  || (!method3&&!cspglobal&& !method2 && !method1&& cspglobal2 && cspglobal3)|| (!method3&&cspglobal&& !method2 && !method1&& cspglobal2 && !cspglobal3)|| (!method3&&cspglobal&& !method2 && !method1&& !cspglobal2 && cspglobal3) ) {
	    	this.doublemethods = true;
	    }
	    
	    if((method1 && method2 && method3 && !cspglobal&& !cspglobal2 && !cspglobal3 ) || (method2&&cspglobal && method3 && !method1&& !cspglobal2 && !cspglobal3) || (!method2&&cspglobal && method3 && method1&& !cspglobal2 && !cspglobal3) || (method2&&cspglobal && !method3 && method1&& !cspglobal2 && !cspglobal3) || (!method2&&cspglobal && !method3 && !method1&& cspglobal2 && cspglobal3) ){
	    	
	    	this.triplemethods = true;
	    }

	    if(cspopti){
	    	this.Sim_Cons_Opti = new Simulation_Objective(timeStep, unit, initTL1, initTL2, initCar);
	    }
	    
	    
	    //Initialisation des paramètres lié aux graphiques
	    this.graph_comparatif = false;
		this.graph_comparatif_some = false;

		
		initializeStage();
		
		createKeyListener();
	
	}
	
	//This function allows the user to interact with the program by pressing some keys 
	//If you want the light to be green/yellow/red/degraded next step, press G/Y/R/D
	//If you want to create a graph, press Q

	private void createKeyListener() {
		
		gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(event.getCode()==KeyCode.ENTER) {
					isEnterKeypressed = true;
					
				}else if (event.getCode()== KeyCode.ESCAPE) {
					isExitKeypressed = true;

					
				}
				else if (event.getCode()==KeyCode.G) {
					isGreenKeypressed = true;
					isRedKeypressed = false;
					isYellowKeypressed = false;
					isDegratedKeypressed = false;
					
				}
				else if (event.getCode()==KeyCode.R) {
					isGreenKeypressed = false;
					isRedKeypressed = true;
					isYellowKeypressed = false;
					isDegratedKeypressed = false;
					
				}
				
				
				else if (event.getCode()==KeyCode.Y) {
					isGreenKeypressed = false;
					isRedKeypressed = false;
					isYellowKeypressed = true;
					isDegratedKeypressed = false;
					
				}
				
				else if (event.getCode()==KeyCode.D) {
					isGreenKeypressed = false;
					isRedKeypressed = false;
					isYellowKeypressed = false;
					isDegratedKeypressed = true;
					
				}
				
				
				else if (event.getCode()==KeyCode.Q) {
					graph_comparatif = true;
					
				}
				
				
			}

			
		});
		
		gameScene.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(event.getCode()==KeyCode.ENTER) {
					isEnterKeypressed = false;
				}else if (event.getCode()== KeyCode.ESCAPE) {
					isExitKeypressed = false;

					
				}
				
			}

			
		});
		
		
	}
	
	//Initialize the stage, with the anchorpane, the scene, the icon..
	private void initializeStage() {
		
		gamePane = new AnchorPane();
		gameScene = new Scene(gamePane, GAME_WIDTH, GAME_HEIGHT);
		gameStage = new Stage();
		gameStage.setScene(gameScene);
		gameStage.getIcons().add(new Image("view/ressources/voitures/car2.png"));
		gameStage.setTitle("Car Simulation");
		
	}
	
	//Create lights, cars etc.
	//Number of cars are created according to the number of methods selected (if 3 methods are select, three cars will be created) 3 cars max
	
	public void createNewGame(Stage menuStage, List<Carpicker> CarList) {
		this.CarList = CarList;
		this.menuStage = menuStage;
		this.menuStage.hide();
		
		createBackground();

	
		
		
		//Here we create the image button that will allow us to call the function that will show us the graphic of the car(s) (speed/position)
	   Image image = new Image("view/ressources/icone_bouton.png");
	   ImageView imageView = new ImageView(image);
	   Button button2 = new Button("");
	   button2.setGraphic(imageView);
	   button2.setLayoutX(880);
	   button2.setLayoutY(80);
	   button2.setPrefHeight(50);
	   button2.setOnAction(new EventHandler<ActionEvent>() {
		   
		    @Override
		    public void handle(ActionEvent event) {
				graph_comparatif = true;
		    }
		});
	   
	    gamePane.getChildren().add(button2);
		int nb = CarList.size();
		int nb2 = TLList.size();
		
		for(int i = 0; i< nb2; i++) {
			
			if(TLList.get(i).getTL().getUrl().contains("green")) {
		
				createGreenlight(TLList.get(i).getTL(), 1);
			}
			
			if(TLList.get(i).getTL().getUrl().contains("red")) {
				createRedlight(TLList.get(i).getTL(), 1);

			}
			
			if(TLList.get(i).getTL().getUrl().contains("yellow")) {
				createYellowlight(TLList.get(i).getTL(), 1);
			}
			
			if(TLList.get(i).getTL().getUrl().contains("flashing_y")) {
				createFlashYellowlight(TLList.get(i).getTL(), 1);
			}
			
			
		}
		
		
		for(int i = 0; i< nb; i++) {
			
			if(CarList.get(i).getCar().getUrl().contains("car")) {
				createCar(CarList.get(i).getCar(), CarList.get(i).getValue());
			}
			
			
		}

		
		//Create the box where we will get the speed limit of the road (initialized in the menu)
		panneau_vit = new SmallInfoLabel2((int)(vlim*3.6) + " km/h");
		panneau_vit.setLayoutX(630);
		panneau_vit.setLayoutY(190);
		gamePane.getChildren().add(panneau_vit);

		
		
		//If only one method is selected, we only need to create two panels for the car's info (speed/position)
		if(!doublemethods && !triplemethods) {
			vitesse = new SmallInfoLabel("Vitesse :" + v0*0.36 + "km/h");
			vitesse.setLayoutX(1300);
			vitesse.setLayoutY(0);
			
			gamePane.getChildren().add(vitesse);
			
			position = new SmallInfoLabel("Position : " + x0/10 + "m");
			position.setLayoutX(1100);
			position.setLayoutY(0);

			gamePane.getChildren().add(position);
			
			
			
			
			
			String[] Text_car1 = new String[7];
			if(method1bool){
				Text_car1[0] = "Method : 1";
			}
			if(method2bool){
				Text_car1[0] = "Method : 2";
			}
			if(method3bool){
				Text_car1[0] = "Method : 3";
			}
			
			//If we are working with the CSP Global method, the initialization is different since we need to load every positions/speed at once, it's not step by step.
			if(cspglobal){
				Text_car1[0] = "Method : CSPG1";
				
				this.liste_CSPG = this.Sim_Cons_CSP1_test.Simulation(xmin, 150, vlim);
				System.out.println(liste_CSPG);
				this.X_CSP1 = liste_CSPG.get(0).stream().mapToInt(i->i).toArray();
				this.V_CSP1 = liste_CSPG.get(1).stream().mapToInt(i->i).toArray();
				this.green_CSP1 = liste_CSPG.get(2).stream().mapToInt(i->i).toArray();
				this.yellow_CSP1 = liste_CSPG.get(3).stream().mapToInt(i->i).toArray();
				this.red_CSP1 = liste_CSPG.get(4).stream().mapToInt(i->i).toArray();
				this.acces_CSP1 = liste_CSPG.get(5).stream().mapToInt(i->i).toArray();
				
			}
			
			if(cspglobal2){
				Text_car1[0] = "Method : CSPG2";
				
				this.liste_CSPG2 = this.Sim_Cons_Glob2.Simulation(xmin, 150, vlim);
				System.out.println(liste_CSPG);
				this.X_CSP2 = liste_CSPG2.get(0).stream().mapToInt(i->i).toArray();
				this.V_CSP2 = liste_CSPG2.get(1).stream().mapToInt(i->i).toArray();
				this.green_CSP2 = liste_CSPG2.get(2).stream().mapToInt(i->i).toArray();
				this.yellow_CSP2 = liste_CSPG2.get(3).stream().mapToInt(i->i).toArray();
				this.red_CSP2 = liste_CSPG2.get(4).stream().mapToInt(i->i).toArray();
				this.acces_CSP2 = liste_CSPG2.get(5).stream().mapToInt(i->i).toArray();
				
			}
			
			if(cspglobal3){
				Text_car1[0] = "Method : CSPG3";
				
				this.liste_CSPG3 = this.Sim_Cons_Glob3.Simulation(xmin, 150, vlim);
				System.out.println(liste_CSPG);
				this.X_CSP3 = liste_CSPG3.get(0).stream().mapToInt(i->i).toArray();
				this.V_CSP3 = liste_CSPG3.get(1).stream().mapToInt(i->i).toArray();
				this.green_CSP3 = liste_CSPG3.get(2).stream().mapToInt(i->i).toArray();
				this.yellow_CSP3 = liste_CSPG3.get(3).stream().mapToInt(i->i).toArray();
				this.red_CSP3 = liste_CSPG3.get(4).stream().mapToInt(i->i).toArray();
				this.acces_CSP3 = liste_CSPG3.get(5).stream().mapToInt(i->i).toArray();
				
			}
			
			
			
			
			
			if(cspopti){
				
				Text_car1[0] = "Method : CSPG_Opti";
				this.Liste_globale = this.Sim_Cons_Opti.Simulation(xmin, 150, vlim);
				
				
				this.choiceBox = new ChoiceBox();
				this.i = 1;
				
				for(i=1; i<Liste_globale.length+1;i++){
					choiceBox.getItems().add(i);
				}
				//With the CSP Global optimization method, a lot of solutions are possible, so we need to show them all in order to choose which one is interesting to us
		        HBox hbox = new HBox(choiceBox);
		        hbox.setLayoutX(0);
		        hbox.setLayoutY(0);
				gamePane.getChildren().add(hbox);
				Button button = new Button("Charge");
				HBox hbox2 = new HBox(button);
				hbox2.setLayoutX(0);
			    hbox2.setLayoutY(25);
			    gamePane.getChildren().add(hbox2);
			    
			    button.setOnAction(new EventHandler<ActionEvent>() {
					   
				    @Override
				    public void handle(ActionEvent event) {
				    	
				    	choix_charge = true;
				    	choix_solution = i;
				    	
				    }
				});
			    
			}
			//These panels allow the user to get the info of the car by right clicking on me. Releasing the click will hide the panel
			Text_car1[1] = "Position: " + this.x0/this.unitScale + "m";
			Text_car1[2] = "Vitesse: " + this.v0/this.unitScale + "m/s";
			Text_car1[3] = "Acceleration: " + this.a0/this.unitScale + "m/s^2";
			
			info_car1 = new BigInfoLabel(Text_car1);

			info_car1.setLayoutX(1500+x0);
			info_car1.setLayoutY(26*13);

			gamePane.getChildren().add(info_car1);
			
			supprimer_label(info_car1);
			
		}
		//For doublem ethods and triple methods, it's the same as for the solo method, but you need to load 2 or 3 times more the number of panels,etc.
		else if(doublemethods) {
			
			vitesse1 = new SmallInfoLabel("Vitesse :" + v0*0.36 + "km/h");
			vitesse1.setLayoutX(1300);
			vitesse1.setLayoutY(0);
			
			gamePane.getChildren().add(vitesse1);
			
			position1 = new SmallInfoLabel("Position : " + x0/10 + "m");
			position1.setLayoutX(1100);
			position1.setLayoutY(0);

			gamePane.getChildren().add(position1);
			
			vitesse2 = new SmallInfoLabel("Vitesse 2 :" + v0*0.36 + "km/h");
			vitesse2.setLayoutX(1300);
			vitesse2.setLayoutY(70);
			
			gamePane.getChildren().add(vitesse2);
			
			position2 = new SmallInfoLabel("Position 2 : " + x0/10 + "m");
			position2.setLayoutX(1100);
			position2.setLayoutY(70);

			gamePane.getChildren().add(position2);
			
			
			String[] Text_car1 = new String[7];
			String[] Text_car2 = new String[7];

			
			if(method1bool && method2bool && !method3bool && !cspglobal) {
				Text_car1[0] = "Method : 1";
				Text_car1[1] = "Position: " + this.x0/this.unitScale + "m";
				Text_car1[2] = "Vitesse: " + this.v0/this.unitScale + "m/s";
				Text_car1[3] = "Acceleration: " + this.a0/this.unitScale + "m/s^2";
				
				info_car1 = new BigInfoLabel(Text_car1);
				info_car1.setLayoutX(1500+x0);
				info_car1.setLayoutY(26*13);

				gamePane.getChildren().add(info_car1);
				
				Text_car2[0] = "Method : 2";
				Text_car2[1] = "Position: " + this.x0/this.unitScale + "m";
				Text_car2[2] = "Vitesse: " + this.v0/this.unitScale + "m/s";
				Text_car2[3] = "Acceleration: " + this.a0/this.unitScale + "m/s^2";
				
				info_car2 = new BigInfoLabel(Text_car2);
				info_car2.setLayoutX(1500+x0);
				info_car2.setLayoutY(32*13);
				gamePane.getChildren().add(info_car2);

				
			}
			else if(method2bool && !method1bool && method3bool && !cspglobal) {
				Text_car1[0] = "Method : 2";
				Text_car1[1] = "Position: " + this.x0/this.unitScale + "m";
				Text_car1[2] = "Vitesse: " + this.v0/this.unitScale + "m/s";
				Text_car1[3] = "Acceleration: " + this.a0/this.unitScale + "m/s^2";
				
				info_car1 = new BigInfoLabel(Text_car1);
				info_car1.setLayoutX(1500+x0);
				info_car1.setLayoutY(26*13);

				gamePane.getChildren().add(info_car1);
				
				Text_car2[0] = "Method : 3";
				Text_car2[1] = "Position: " + this.x0/this.unitScale + "m";
				Text_car2[2] = "Vitesse: " + this.v0/this.unitScale + "m/s";
				Text_car2[3] = "Acceleration: " + this.a0/this.unitScale + "m/s^2";
				
				info_car2 = new BigInfoLabel(Text_car2);
				info_car2.setLayoutX(1500+x0);
				info_car2.setLayoutY(32*13);
				gamePane.getChildren().add(info_car2);
				
				
		    }
			else if(method3bool && method1bool && !method2bool && !cspglobal) {
				
				Text_car1[0] = "Method : 1";
				Text_car1[1] = "Position: " + this.x0/this.unitScale + "m";
				Text_car1[2] = "Vitesse: " + this.v0/this.unitScale + "m/s";
				Text_car1[3] = "Acceleration: " + this.a0/this.unitScale + "m/s^2";
				
				info_car1 = new BigInfoLabel(Text_car1);
				info_car1.setLayoutX(1500+x0);
				info_car1.setLayoutY(26*13);

				gamePane.getChildren().add(info_car1);
				
				Text_car2[0] = "Method : 3";
				Text_car2[1] = "Position: " + this.x0/this.unitScale + "m";
				Text_car2[2] = "Vitesse: " + this.v0/this.unitScale + "m/s";
				Text_car2[3] = "Acceleration: " + this.a0/this.unitScale + "m/s^2";
				
				info_car2 = new BigInfoLabel(Text_car2);
				info_car2.setLayoutX(1500+x0);
				info_car2.setLayoutY(32*13);

				gamePane.getChildren().add(info_car2);
				
		    }
			
			
			else if(!method3bool && method1bool && !method2bool && cspglobal) {
				
				Text_car1[0] = "Method : 1";
				Text_car1[1] = "Position: " + this.x0/this.unitScale + "m";
				Text_car1[2] = "Vitesse: " + this.v0/this.unitScale + "m/s";
				Text_car1[3] = "Acceleration: " + this.a0/this.unitScale + "m/s^2";
				
				info_car1 = new BigInfoLabel(Text_car1);
				info_car1.setLayoutX(1500+x0);
				info_car1.setLayoutY(26*13);

				gamePane.getChildren().add(info_car1);
				
				Text_car2[0] = "Method : CSPG1";
				Text_car2[1] = "Position: " + this.x0/this.unitScale + "m";
				Text_car2[2] = "Vitesse: " + this.v0/this.unitScale + "m/s";
				Text_car2[3] = "Acceleration: " + this.a0/this.unitScale + "m/s^2";
				
				info_car2 = new BigInfoLabel(Text_car2);
				info_car2.setLayoutX(1500+x0);
				info_car2.setLayoutY(32*13);

				gamePane.getChildren().add(info_car2);
				this.liste_CSPG = this.Sim_Cons_CSP1_test.Simulation(xmin, 150, vlim);
				System.out.println(liste_CSPG);
				this.X_CSP1 = liste_CSPG.get(0).stream().mapToInt(i->i).toArray();
				this.V_CSP1 = liste_CSPG.get(1).stream().mapToInt(i->i).toArray();
				this.green_CSP1 = liste_CSPG.get(2).stream().mapToInt(i->i).toArray();
				this.yellow_CSP1 = liste_CSPG.get(3).stream().mapToInt(i->i).toArray();
				this.red_CSP1 = liste_CSPG.get(4).stream().mapToInt(i->i).toArray();
				this.acces_CSP1 = liste_CSPG.get(5).stream().mapToInt(i->i).toArray();
				
		    }
			
			else if(!method3bool && !method1bool && method2bool && cspglobal) {
				
				Text_car1[0] = "Method : 2";
				Text_car1[1] = "Position: " + this.x0/this.unitScale + "m";
				Text_car1[2] = "Vitesse: " + this.v0/this.unitScale + "m/s";
				Text_car1[3] = "Acceleration: " + this.a0/this.unitScale + "m/s^2";
				
				info_car1 = new BigInfoLabel(Text_car1);
				info_car1.setLayoutX(1500+x0);
				info_car1.setLayoutY(26*13);

				gamePane.getChildren().add(info_car1);
				
				Text_car2[0] = "Method : CSPG1";
				Text_car2[1] = "Position: " + this.x0/this.unitScale + "m";
				Text_car2[2] = "Vitesse: " + this.v0/this.unitScale + "m/s";
				Text_car2[3] = "Acceleration: " + this.a0/this.unitScale + "m/s^2";
				
				info_car2 = new BigInfoLabel(Text_car2);
				info_car2.setLayoutX(1500+x0);
				info_car2.setLayoutY(32*13);

				gamePane.getChildren().add(info_car2);
				this.liste_CSPG = this.Sim_Cons_CSP1_test.Simulation(xmin, 150, vlim);
				System.out.println(liste_CSPG);
				this.X_CSP1 = liste_CSPG.get(0).stream().mapToInt(i->i).toArray();
				this.V_CSP1 = liste_CSPG.get(1).stream().mapToInt(i->i).toArray();
				this.green_CSP1 = liste_CSPG.get(2).stream().mapToInt(i->i).toArray();
				this.yellow_CSP1 = liste_CSPG.get(3).stream().mapToInt(i->i).toArray();
				this.red_CSP1 = liste_CSPG.get(4).stream().mapToInt(i->i).toArray();
				this.acces_CSP1 = liste_CSPG.get(5).stream().mapToInt(i->i).toArray();
				
		    }
			
			else if(method3bool && cspglobal3) {
				
				Text_car1[0] = "Method : 3";
				Text_car1[1] = "Position: " + this.x0/this.unitScale + "m";
				Text_car1[2] = "Vitesse: " + this.v0/this.unitScale + "m/s";
				Text_car1[3] = "Acceleration: " + this.a0/this.unitScale + "m/s^2";
				
				info_car1 = new BigInfoLabel(Text_car1);
				info_car1.setLayoutX(1500+x0);
				info_car1.setLayoutY(26*13);

				gamePane.getChildren().add(info_car1);
				
				Text_car2[0] = "Method : CSPG3";
				Text_car2[1] = "Position: " + this.x0/this.unitScale + "m";
				Text_car2[2] = "Vitesse: " + this.v0/this.unitScale + "m/s";
				Text_car2[3] = "Acceleration: " + this.a0/this.unitScale + "m/s^2";
				
				info_car2 = new BigInfoLabel(Text_car2);
				info_car2.setLayoutX(1500+x0);
				info_car2.setLayoutY(32*13);

				gamePane.getChildren().add(info_car2);
				this.liste_CSPG3 = this.Sim_Cons_Glob3.Simulation(xmin, 150, vlim);
				System.out.println(liste_CSPG);
				this.X_CSP3 = liste_CSPG3.get(0).stream().mapToInt(i->i).toArray();
				this.V_CSP3 = liste_CSPG3.get(1).stream().mapToInt(i->i).toArray();
				this.green_CSP3 = liste_CSPG3.get(2).stream().mapToInt(i->i).toArray();
				this.yellow_CSP3 = liste_CSPG3.get(3).stream().mapToInt(i->i).toArray();
				this.red_CSP3 = liste_CSPG3.get(4).stream().mapToInt(i->i).toArray();
				this.acces_CSP3 = liste_CSPG3.get(5).stream().mapToInt(i->i).toArray();
				
			
				
				
		    }
			
			else if(method2bool && cspglobal2) {
				
				Text_car1[0] = "Method : 2";
				Text_car1[1] = "Position: " + this.x0/this.unitScale + "m";
				Text_car1[2] = "Vitesse: " + this.v0/this.unitScale + "m/s";
				Text_car1[3] = "Acceleration: " + this.a0/this.unitScale + "m/s^2";
				
				info_car1 = new BigInfoLabel(Text_car1);
				info_car1.setLayoutX(1500+x0);
				info_car1.setLayoutY(26*13);

				gamePane.getChildren().add(info_car1);
				
				Text_car2[0] = "Method : CSPG2";
				Text_car2[1] = "Position: " + this.x0/this.unitScale + "m";
				Text_car2[2] = "Vitesse: " + this.v0/this.unitScale + "m/s";
				Text_car2[3] = "Acceleration: " + this.a0/this.unitScale + "m/s^2";
				
				info_car2 = new BigInfoLabel(Text_car2);
				info_car2.setLayoutX(1500+x0);
				info_car2.setLayoutY(32*13);

				gamePane.getChildren().add(info_car2);
				this.liste_CSPG2 = this.Sim_Cons_Glob2.Simulation(xmin, 150, vlim);
				System.out.println(liste_CSPG);
				this.X_CSP2 = liste_CSPG2.get(0).stream().mapToInt(i->i).toArray();
				this.V_CSP2 = liste_CSPG2.get(1).stream().mapToInt(i->i).toArray();
				this.green_CSP2 = liste_CSPG2.get(2).stream().mapToInt(i->i).toArray();
				this.yellow_CSP2 = liste_CSPG2.get(3).stream().mapToInt(i->i).toArray();
				this.red_CSP2 = liste_CSPG2.get(4).stream().mapToInt(i->i).toArray();
				this.acces_CSP2 = liste_CSPG2.get(5).stream().mapToInt(i->i).toArray();
						
				
		    }
			
			else if(method3bool && !method1bool && !method2bool && cspglobal) {
				
				Text_car1[0] = "Method : 3";
				Text_car1[1] = "Position: " + this.x0/this.unitScale + "m";
				Text_car1[2] = "Vitesse: " + this.v0/this.unitScale + "m/s";
				Text_car1[3] = "Acceleration: " + this.a0/this.unitScale + "m/s^2";
				
				info_car1 = new BigInfoLabel(Text_car1);
				info_car1.setLayoutX(1500+x0);
				info_car1.setLayoutY(26*13);

				gamePane.getChildren().add(info_car1);
				
				Text_car2[0] = "Method : CSPG1";
				Text_car2[1] = "Position: " + this.x0/this.unitScale + "m";
				Text_car2[2] = "Vitesse: " + this.v0/this.unitScale + "m/s";
				Text_car2[3] = "Acceleration: " + this.a0/this.unitScale + "m/s^2";
				
				info_car2 = new BigInfoLabel(Text_car2);
				info_car2.setLayoutX(1500+x0);
				info_car2.setLayoutY(32*13);

				gamePane.getChildren().add(info_car2);
				this.liste_CSPG = this.Sim_Cons_CSP1_test.Simulation(xmin, 150, vlim);
				System.out.println(liste_CSPG);
				this.X_CSP1 = liste_CSPG.get(0).stream().mapToInt(i->i).toArray();
				this.V_CSP1 = liste_CSPG.get(1).stream().mapToInt(i->i).toArray();
				this.green_CSP1 = liste_CSPG.get(2).stream().mapToInt(i->i).toArray();
				this.yellow_CSP1 = liste_CSPG.get(3).stream().mapToInt(i->i).toArray();
				this.red_CSP1 = liste_CSPG.get(4).stream().mapToInt(i->i).toArray();
				this.acces_CSP1 = liste_CSPG.get(5).stream().mapToInt(i->i).toArray();
				
			
				
				
		    }
			
			
			supprimer_label(info_car2);
			supprimer_label(info_car1);
			
			
		}
		else if(triplemethods){

			vitesse1 = new SmallInfoLabel("Vitesse :" + v0*0.36 + "km/h");
			vitesse1.setLayoutX(1300);
			vitesse1.setLayoutY(0);
			
			gamePane.getChildren().add(vitesse1);
			
			position1 = new SmallInfoLabel("Position : " + x0/10 + "m");
			position1.setLayoutX(1100);
			position1.setLayoutY(0);

			gamePane.getChildren().add(position1);
			
			vitesse2 = new SmallInfoLabel("Vitesse 2 :" + v0*0.36 + "km/h");
			vitesse2.setLayoutX(1300);
			vitesse2.setLayoutY(70);
			
			gamePane.getChildren().add(vitesse2);
			
			position2 = new SmallInfoLabel("Position 2 : " + x0/10 + "m");
			position2.setLayoutX(1100);
			position2.setLayoutY(70);

			gamePane.getChildren().add(position2);
			
			
			vitesse3 = new SmallInfoLabel("Vitesse 3 :" + v0*0.36 + "km/h");
			vitesse3.setLayoutX(1300);
			vitesse3.setLayoutY(140);
			
			gamePane.getChildren().add(vitesse3);
			
			position3 = new SmallInfoLabel("Position 3 : " + x0/10 + "m");
			position3.setLayoutX(1100);
			position3.setLayoutY(140);

			gamePane.getChildren().add(position3);
			
			
			
			
			String[] Text_car1 = new String[7];
			String[] Text_car2 = new String[7];
			String[] Text_car3 = new String[7];
			
			if(method1bool && method2bool && method3bool && !cspglobal) {
				Text_car1[0] = "Method : 1";
				Text_car1[1] = "Position: " + this.x0/this.unitScale + "m";
				Text_car1[2] = "Vitesse: " + this.v0/this.unitScale + "m/s";
				Text_car1[3] = "Acceleration: " + this.a0/this.unitScale + "m/s^2";
				
				info_car1 = new BigInfoLabel(Text_car1);
				info_car1.setLayoutX(1500+x0);
				info_car1.setLayoutY(26*13);

				gamePane.getChildren().add(info_car1);
				
				Text_car2[0] = "Method : 2";
				Text_car2[1] = "Position: " + this.x0/this.unitScale + "m";
				Text_car2[2] = "Vitesse: " + this.v0/this.unitScale + "m/s";
				Text_car2[3] = "Acceleration: " + this.a0/this.unitScale + "m/s^2";
				
				info_car2 = new BigInfoLabel(Text_car2);
				info_car2.setLayoutX(1500+x0);
				info_car2.setLayoutY(32*13);
				gamePane.getChildren().add(info_car2);
				
				
				Text_car3[0] = "Method : 3";
				Text_car3[1] = "Position: " + this.x0/this.unitScale + "m";
				Text_car3[2] = "Vitesse: " + this.v0/this.unitScale + "m/s";
				Text_car3[3] = "Acceleration: " + this.a0/this.unitScale + "m/s^2";
				
				info_car3 = new BigInfoLabel(Text_car3);
				info_car3.setLayoutX(1500+x0);
				info_car3.setLayoutY(32*13);

				gamePane.getChildren().add(info_car3);

			}
			else if(!method1bool && method2bool && method3bool && cspglobal) {
				Text_car1[0] = "Method : 2";
				Text_car1[1] = "Position: " + this.x0/this.unitScale + "m";
				Text_car1[2] = "Vitesse: " + this.v0/this.unitScale + "m/s";
				Text_car1[3] = "Acceleration: " + this.a0/this.unitScale + "m/s^2";
				
				info_car1 = new BigInfoLabel(Text_car1);
				info_car1.setLayoutX(1500+x0);
				info_car1.setLayoutY(26*13);

				gamePane.getChildren().add(info_car1);
				
				Text_car2[0] = "Method : 3";
				Text_car2[1] = "Position: " + this.x0/this.unitScale + "m";
				Text_car2[2] = "Vitesse: " + this.v0/this.unitScale + "m/s";
				Text_car2[3] = "Acceleration: " + this.a0/this.unitScale + "m/s^2";
				
				info_car2 = new BigInfoLabel(Text_car2);
				info_car2.setLayoutX(1500+x0);
				info_car2.setLayoutY(32*13);
				gamePane.getChildren().add(info_car2);
				
				
				Text_car3[0] = "Method : CSPG1";
				Text_car3[1] = "Position: " + this.x0/this.unitScale + "m";
				Text_car3[2] = "Vitesse: " + this.v0/this.unitScale + "m/s";
				Text_car3[3] = "Acceleration: " + this.a0/this.unitScale + "m/s^2";
				
				info_car3 = new BigInfoLabel(Text_car3);
				info_car3.setLayoutX(1500+x0);
				info_car3.setLayoutY(32*13);

				gamePane.getChildren().add(info_car3);
				this.liste_CSPG = this.Sim_Cons_CSP1_test.Simulation(xmin, 150, vlim);
				System.out.println(liste_CSPG);
				this.X_CSP1 = liste_CSPG.get(0).stream().mapToInt(i->i).toArray();
				this.V_CSP1 = liste_CSPG.get(1).stream().mapToInt(i->i).toArray();
				this.green_CSP1 = liste_CSPG.get(2).stream().mapToInt(i->i).toArray();
				this.yellow_CSP1 = liste_CSPG.get(3).stream().mapToInt(i->i).toArray();
				this.red_CSP1 = liste_CSPG.get(4).stream().mapToInt(i->i).toArray();
				this.acces_CSP1 = liste_CSPG.get(5).stream().mapToInt(i->i).toArray();
			}
			
			
			else if(method1bool && !method2bool && method3bool && cspglobal) {
				Text_car1[0] = "Method : 1";
				Text_car1[1] = "Position: " + this.x0/this.unitScale + "m";
				Text_car1[2] = "Vitesse: " + this.v0/this.unitScale + "m/s";
				Text_car1[3] = "Acceleration: " + this.a0/this.unitScale + "m/s^2";
				
				info_car1 = new BigInfoLabel(Text_car1);
				info_car1.setLayoutX(1500+x0);
				info_car1.setLayoutY(26*13);

				gamePane.getChildren().add(info_car1);
				
				Text_car2[0] = "Method : 3";
				Text_car2[1] = "Position: " + this.x0/this.unitScale + "m";
				Text_car2[2] = "Vitesse: " + this.v0/this.unitScale + "m/s";
				Text_car2[3] = "Acceleration: " + this.a0/this.unitScale + "m/s^2";
				
				info_car2 = new BigInfoLabel(Text_car2);
				info_car2.setLayoutX(1500+x0);
				info_car2.setLayoutY(32*13);
				gamePane.getChildren().add(info_car2);
				
				
				Text_car3[0] = "Method : CSPG1";
				Text_car3[1] = "Position: " + this.x0/this.unitScale + "m";
				Text_car3[2] = "Vitesse: " + this.v0/this.unitScale + "m/s";
				Text_car3[3] = "Acceleration: " + this.a0/this.unitScale + "m/s^2";
				
				info_car3 = new BigInfoLabel(Text_car3);
				info_car3.setLayoutX(1500+x0);
				info_car3.setLayoutY(32*13);

				gamePane.getChildren().add(info_car3);
				this.liste_CSPG = this.Sim_Cons_CSP1_test.Simulation(xmin, 150, vlim);
				System.out.println(liste_CSPG);
				this.X_CSP1 = liste_CSPG.get(0).stream().mapToInt(i->i).toArray();
				this.V_CSP1 = liste_CSPG.get(1).stream().mapToInt(i->i).toArray();
				this.green_CSP1 = liste_CSPG.get(2).stream().mapToInt(i->i).toArray();
				this.yellow_CSP1 = liste_CSPG.get(3).stream().mapToInt(i->i).toArray();
				this.red_CSP1 = liste_CSPG.get(4).stream().mapToInt(i->i).toArray();
				this.acces_CSP1 = liste_CSPG.get(5).stream().mapToInt(i->i).toArray();

			}
			
			else if(method1bool && method2bool && !method3bool && cspglobal) {
				Text_car1[0] = "Method : 1";
				Text_car1[1] = "Position: " + this.x0/this.unitScale + "m";
				Text_car1[2] = "Vitesse: " + this.v0/this.unitScale + "m/s";
				Text_car1[3] = "Acceleration: " + this.a0/this.unitScale + "m/s^2";
				
				info_car1 = new BigInfoLabel(Text_car1);
				info_car1.setLayoutX(1500+x0);
				info_car1.setLayoutY(26*13);

				gamePane.getChildren().add(info_car1);
				
				Text_car2[0] = "Method : 2";
				Text_car2[1] = "Position: " + this.x0/this.unitScale + "m";
				Text_car2[2] = "Vitesse: " + this.v0/this.unitScale + "m/s";
				Text_car2[3] = "Acceleration: " + this.a0/this.unitScale + "m/s^2";
				
				info_car2 = new BigInfoLabel(Text_car2);
				info_car2.setLayoutX(1500+x0);
				info_car2.setLayoutY(32*13);
				gamePane.getChildren().add(info_car2);
				
				
				Text_car3[0] = "Method : CSPG1";
				Text_car3[1] = "Position: " + this.x0/this.unitScale + "m";
				Text_car3[2] = "Vitesse: " + this.v0/this.unitScale + "m/s";
				Text_car3[3] = "Acceleration: " + this.a0/this.unitScale + "m/s^2";
				
				info_car3 = new BigInfoLabel(Text_car3);
				info_car3.setLayoutX(1500+x0);
				info_car3.setLayoutY(32*13);

				gamePane.getChildren().add(info_car3);
				this.liste_CSPG = this.Sim_Cons_CSP1_test.Simulation(xmin, 150, vlim);
				System.out.println(liste_CSPG);
				this.X_CSP1 = liste_CSPG.get(0).stream().mapToInt(i->i).toArray();
				this.V_CSP1 = liste_CSPG.get(1).stream().mapToInt(i->i).toArray();
				this.green_CSP1 = liste_CSPG.get(2).stream().mapToInt(i->i).toArray();
				this.yellow_CSP1 = liste_CSPG.get(3).stream().mapToInt(i->i).toArray();
				this.red_CSP1 = liste_CSPG.get(4).stream().mapToInt(i->i).toArray();
				this.acces_CSP1 = liste_CSPG.get(5).stream().mapToInt(i->i).toArray();

			}
			
			else if(cspglobal&&cspglobal2&&cspglobal3) {
				Text_car1[0] = "Method : CSPG1";
				Text_car1[1] = "Position: " + this.x0/this.unitScale + "m";
				Text_car1[2] = "Vitesse: " + this.v0/this.unitScale + "m/s";
				Text_car1[3] = "Acceleration: " + this.a0/this.unitScale + "m/s^2";
				
				info_car1 = new BigInfoLabel(Text_car1);
				info_car1.setLayoutX(1500+x0);
				info_car1.setLayoutY(26*13);

				gamePane.getChildren().add(info_car1);
				
				this.liste_CSPG = this.Sim_Cons_CSP1_test.Simulation(xmin, 150, vlim);
				System.out.println(liste_CSPG);
				
				this.X_CSP1 = liste_CSPG.get(0).stream().mapToInt(i->i).toArray();
				this.V_CSP1 = liste_CSPG.get(1).stream().mapToInt(i->i).toArray();
				this.green_CSP1 = liste_CSPG.get(2).stream().mapToInt(i->i).toArray();
				this.yellow_CSP1 = liste_CSPG.get(3).stream().mapToInt(i->i).toArray();
				this.red_CSP1 = liste_CSPG.get(4).stream().mapToInt(i->i).toArray();
				this.acces_CSP1 = liste_CSPG.get(5).stream().mapToInt(i->i).toArray();

				
				
				
				
				
				
				Text_car2[0] = "Method : CSPG2";
				Text_car2[1] = "Position: " + this.x0/this.unitScale + "m";
				Text_car2[2] = "Vitesse: " + this.v0/this.unitScale + "m/s";
				Text_car2[3] = "Acceleration: " + this.a0/this.unitScale + "m/s^2";
				
				info_car2 = new BigInfoLabel(Text_car2);
				info_car2.setLayoutX(1500+x0);
				info_car2.setLayoutY(32*13);
				gamePane.getChildren().add(info_car2);
				
				
				this.liste_CSPG2 = this.Sim_Cons_Glob2.Simulation(xmin, 150, vlim);
				System.out.println(liste_CSPG);
				System.out.println(liste_CSPG);

				this.X_CSP2 = liste_CSPG2.get(0).stream().mapToInt(i->i).toArray();
				this.V_CSP2 = liste_CSPG2.get(1).stream().mapToInt(i->i).toArray();
				this.green_CSP2 = liste_CSPG2.get(2).stream().mapToInt(i->i).toArray();
				this.yellow_CSP2 = liste_CSPG2.get(3).stream().mapToInt(i->i).toArray();
				this.red_CSP2 = liste_CSPG2.get(4).stream().mapToInt(i->i).toArray();
				this.acces_CSP2 = liste_CSPG2.get(5).stream().mapToInt(i->i).toArray();

				
				
				
				
				Text_car3[0] = "Method : CSPG3";
				Text_car3[1] = "Position: " + this.x0/this.unitScale + "m";
				Text_car3[2] = "Vitesse: " + this.v0/this.unitScale + "m/s";
				Text_car3[3] = "Acceleration: " + this.a0/this.unitScale + "m/s^2";
				
				info_car3 = new BigInfoLabel(Text_car3);
				info_car3.setLayoutX(1500+x0);
				info_car3.setLayoutY(32*13);

				gamePane.getChildren().add(info_car3);
				this.liste_CSPG3 = this.Sim_Cons_Glob3.Simulation(xmin, 150, vlim);
				System.out.println(liste_CSPG3);
				this.X_CSP3 = liste_CSPG3.get(0).stream().mapToInt(i->i).toArray();
				this.V_CSP3 = liste_CSPG3.get(1).stream().mapToInt(i->i).toArray();
				this.green_CSP3 = liste_CSPG3.get(2).stream().mapToInt(i->i).toArray();
				this.yellow_CSP3 = liste_CSPG3.get(3).stream().mapToInt(i->i).toArray();
				this.red_CSP3 = liste_CSPG3.get(4).stream().mapToInt(i->i).toArray();
				this.acces_CSP3 = liste_CSPG3.get(5).stream().mapToInt(i->i).toArray();

			}
			
			
			
			
			
			supprimer_label(info_car3);
			supprimer_label(info_car2);
			supprimer_label(info_car1);
			
		}
		
		//This call the game loop, and this is what allow us to do the step by step prorgram.
		createGameLoop(); 

		gameStage.show();
		
	}	
	
	
	//Create a degraded lights with imageview
	private void createFlashYellowlight(TLurl trafficlight, int taille) {
		flashy  = new ImageView();
		

			
			flashy = new ImageView(trafficlight.getUrl());
			flashy.setLayoutX(1755);  //le bord gauche se trouve a -175.5(*10)
			flashy.setLayoutY(2*30);
	
			gamePane.getChildren().add(flashy);
		
		
	}	
	
	//Create a green light with imageview
	private void createGreenlight(TLurl trafficlight, int taille) {
		green  = new ImageView();
					
			
			green = new ImageView(trafficlight.getUrl());
			green.setLayoutX(1755); 
			green.setLayoutY(2*30);
		
			gamePane.getChildren().add(green);
			
			
	}	
		
	//Create a red light with imageview

	private void createRedlight(TLurl trafficlight, int taille) {
		red  = new ImageView();

				
			red = new ImageView(trafficlight.getUrl());
			red.setLayoutX(1755);
			red.setLayoutY(2*30);
		
			gamePane.getChildren().add(red);

	}
	//Create a yellow light with imageview

	private void createYellowlight(TLurl trafficlight, int taille) {
		yellow  = new ImageView();
		
		yellow = new ImageView(trafficlight.getUrl());
		yellow.setLayoutX(1755);
		yellow.setLayoutY(2*30);
		
		gamePane.getChildren().add(yellow);
	
	}
	

	
	
	//Create the car(s) with imageview
	private void createCar(Carurl carurl, int taille) {
		
		
		car = new ImageView();
		

			int x = x0;
			System.out.print("position initiale" + (1700+x0));
			int y = 22;
		
			car = new ImageView("view/ressources/voitures/car_60.png");
			if(x0<-2000) {
				System.out.println("La position initiale n'est pas correcte, veuillez choisir une distance initiale supérieure ou égale à -175");
				gameStage.close();  //On quitte le jeu
				this.menuStage.show();
			}
			car.setLayoutX(1700+x);
			car.setLayoutY(y*16);
			car.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
			     apparaitre_label(info_car1);
			     event.consume();
			 });
			car.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
				supprimer_label(info_car1);
			     event.consume();
			 });
			gamePane.getChildren().add(car);

		if(doublemethods || triplemethods) {
			car2 = new ImageView();

			int x2 = x0;
			System.out.print("position initiale" + (1700+x0));
			int y2 = 26;
			
			car2 = new ImageView("view/ressources/voitures/car_60.png");
			if(x0<-2000) {
				System.out.println("La position initiale n'est pas correcte, veuillez choisir une distance initiale supérieure ou égale à -175");
				gameStage.close();  //On quitte le jeu
				this.menuStage.show();
			}
			car2.setLayoutX(1700+x2);
			car2.setLayoutY(y2*16);
			car2.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
			     apparaitre_label(info_car2);
			     event.consume();
			 });
			car2.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
				supprimer_label(info_car2);
			     event.consume();
			 });	
			gamePane.getChildren().add(car2);
		}
		
		if(triplemethods){
			car3 = new ImageView();

			int x3 = x0;
			System.out.print("position initiale" + (1700+x0));
			int y3 = 30;
			
			car3 = new ImageView("view/ressources/voitures/car_60.png");
			if(x0<-2000) {
				System.out.println("La position initiale n'est pas correcte, veuillez choisir une distance initiale supérieure ou égale à -175");
				gameStage.close();  //On quitte le jeu
				this.menuStage.show();
			}
			car3.setLayoutX(1700+x3);
			car3.setLayoutY(y3*16);
			car3.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
			     apparaitre_label(info_car3);
			     event.consume();
			 });
			car3.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
				supprimer_label(info_car3);
			     event.consume();
			 });	
			gamePane.getChildren().add(car3);

			
		}
			
	}
	
	
	//Create the Game Loop
	private void createGameLoop() {
		gameTimer = new AnimationTimer() {

			@Override
			public void handle(long now) {
				if(!cspopti){
					moveTour();
					Quitter();
					graph();
				}
				if(cspopti){
					
					moveTour_opti();
					Quitter();
					charge_solution();
					graph_opti();
				}
				
			}

		};
		gameTimer.start();

	}
	
	//moveTour calls the simulation (according to the method chosen) and move the car(s) with the new values of pos and speed.
	private void moveTour_opti() {
		String text_pos = "X = ";
		String text_vit = "V = ";
		if(initial_light == "Green"){
			isGreenKeypressed = true;
			initial_light = "Done";
		}
		if(initial_light == "Red"){
			isRedKeypressed = true;
			initial_light = "Done";
		}
		if(initial_light == "Yellow"){
			isYellowKeypressed = true;
			initial_light = "Done";
		}
		if(initial_light == "No working lights"){
			
			isDegratedKeypressed = true;
			initial_light = "Done";
		}
		
		 if(isEnterKeypressed) {
			 
			greenint = this.Liste_globale[choix_solution-1][2][compteur_opti];
			redint = this.Liste_globale[choix_solution-1][4][compteur_opti];
			yellowint = this.Liste_globale[choix_solution-1][3][compteur_opti];
			
			update_TL(); 

			int[] New = new int[4];

			String[] Text_car1 = new String[7];

	
			if(!doublemethods && !triplemethods) {
				New[0] = this.Liste_globale[choix_solution-1][0][compteur_opti];
				New[1] = this.Liste_globale[choix_solution-1][1][compteur_opti];
				
					
				int xdiff = (int)((New[0]-xk));
				    
				deplacement(car, xdiff);
				deplacement_label(info_car1,xdiff);
				if(car.getLayoutX()>1700) {
					green.setLayoutX(135);
					yellow.setLayoutX(135);
					red.setLayoutX(135);
					flashy.setLayoutX(135);
					car.setLayoutX(car.getLayoutX()-1700+135);
					info_car1.setLayoutX(info_car1.getLayoutX()-1500+135);
				}
				this.xk = New[0];
				this.vk = New[1];
				
				position.setText(text_pos+(this.xk)/10+" m");
				vitesse.setText(text_vit+(this.vk)*0.36+ " km/h");
				System.out.println("xk+1 = " + xk + " dm");		
				System.out.println("vk+1 = " + vk + "dm/s");
					
				if(cspopti){
					Text_car1[0] = "Method : CSPG Opti Solution " + this.choix_solution;
				}
		
				Text_car1[1] = "Position: " + this.xk/this.unitScale + "m";
				Text_car1[2] = "Vitesse: " + this.vk/this.unitScale + "m/s";
				Text_car1[3] = "Acceleration initiale: " + this.a0/this.unitScale + "m/s^2";
					
				info_car1.setTextList(Text_car1);

			}
	
			this.compteur_liste++;
			this.compteur_opti++;
			try {
			    Thread.sleep(300);
			} catch(InterruptedException e) {
			    System.out.println("Petite pause!");
			}
			}
		 
	}
	
	
	
	//moveTour calls the simulation (according to the method chosen) and move the car(s) with the new values of pos and speed.

	private void moveTour() {
		String text_pos = "X = ";
		String text_vit = "V = ";
		
		//Initialize the traffic light according to what the user has chose in the menu.
		if(initial_light == "Green"){
			isGreenKeypressed = true;
			initial_light = "Done";
		}
		if(initial_light == "Red"){
			isRedKeypressed = true;
			initial_light = "Done";
		}
		if(initial_light == "Yellow"){
			isYellowKeypressed = true;
			initial_light = "Done";
		}
		if(initial_light == "No working lights"){
			
			isDegratedKeypressed = true;
			initial_light = "Done";
		}
		
		//A key listen change the boolean isEnterKeypressed so that we can enter this loop if we press the enter key, it allows us to make the user choose the speed of the simulation.
		 if(isEnterKeypressed) {
			 
			 
			boolean[] Lights = new boolean[3];
			Before_Light = Actual_Light;
			
			Lights = update_TL_IHM(); 

			if((Before_Light != Actual_Light) && cspglobal){
				
				if(Actual_Light == "Green"){
			    	this.initTL2[0] = false;
			    	this.initTL2[1] = false;
			    	this.initTL2[2] = false;
			    	this.initTL2[3] = true;
			    	
				}
				else if(Actual_Light == "Red"){
					this.initTL2[0] = false;
			    	this.initTL2[1] = true;
			    	this.initTL2[2] = false;
			    	this.initTL2[3] = false;
				}
				else if(Actual_Light == "Yellow"){
					this.initTL2[0] = false;
			    	this.initTL2[1] = false;
			    	this.initTL2[2] = true;
			    	this.initTL2[3] = false;
				}
				else if(Actual_Light == "No working lights"){

					this.initTL2[0] = true;
			    	this.initTL2[1] = false;
			    	this.initTL2[2] = false;
			    	this.initTL2[3] = false;
				}
				System.out.println(" NOUVELLE METHODE LA ");
				System.out.println("x csp1" + X_CSP1[compteur+1]);
				System.out.println("x csp1" + V_CSP1[compteur+1]);
				System.out.println("x csp1" + acces_CSP1[compteur+1]);
			    int[] initCar = {X_CSP1[compteur+1]/this.unitScale, V_CSP1[compteur+1]/this.unitScale, acces_CSP1[compteur+1]/this.unitScale, vmax, amin, amax}; 
			    int[] initCar2 = {X_CSP2[compteur+1]/this.unitScale, V_CSP2[compteur+1]/this.unitScale, acces_CSP2[compteur+1]/this.unitScale, vmax, amin, amax};  
			    int[] initCar3 = {X_CSP3[compteur+1]/this.unitScale, V_CSP3[compteur+1]/this.unitScale, acces_CSP3[compteur+1]/this.unitScale, vmax, amin, amax};  
			    int[] initTL1 = {0, 1, 10, 10};
			    
				this.Sim_Cons_CSP1_test1 = new Simulation_CSP_test(timeStep,this.unitScale, initTL1, initTL2, initCar, N); 
				this.liste_CSPG1 = this.Sim_Cons_CSP1_test1.Simulation(this.xmin, 500, vlim);
				System.out.println("LA EST LE PB : "+(liste_CSPG1.size()));
				System.out.println(liste_CSPG1);
				this.X_CSP1 = liste_CSPG1.get(0).stream().mapToInt(i->i).toArray();
				this.V_CSP1 = liste_CSPG1.get(1).stream().mapToInt(i->i).toArray();
				this.green_CSP1 = liste_CSPG1.get(2).stream().mapToInt(i->i).toArray();
				this.yellow_CSP1 = liste_CSPG1.get(3).stream().mapToInt(i->i).toArray();
				this.red_CSP1 = liste_CSPG1.get(4).stream().mapToInt(i->i).toArray();
				this.acces_CSP1 = liste_CSPG1.get(5).stream().mapToInt(i->i).toArray();	
				
				
				this.Sim_Cons_Glob2_bis = new Simulation_global_method2(timeStep,this.unitScale, initTL1, initTL2, initCar); 
				this.liste_CSPG2 = this.Sim_Cons_Glob2_bis.Simulation(this.xmin, 500, vlim);

				this.X_CSP2 = liste_CSPG2.get(0).stream().mapToInt(i->i).toArray();
				this.V_CSP2 = liste_CSPG2.get(1).stream().mapToInt(i->i).toArray();
				this.green_CSP2 = liste_CSPG2.get(2).stream().mapToInt(i->i).toArray();
				this.yellow_CSP2 = liste_CSPG2.get(3).stream().mapToInt(i->i).toArray();
				this.red_CSP2 = liste_CSPG2.get(4).stream().mapToInt(i->i).toArray();
				this.acces_CSP2 = liste_CSPG2.get(5).stream().mapToInt(i->i).toArray();	
				
				
				
				
				
				this.Sim_Cons_Glob3_bis = new Simulation_global_method3(timeStep,this.unitScale, initTL1, initTL2, initCar); 
				this.liste_CSPG3 = this.Sim_Cons_Glob3_bis.Simulation(this.xmin, 500, vlim);

				this.X_CSP3 = liste_CSPG3.get(0).stream().mapToInt(i->i).toArray();
				this.V_CSP3 = liste_CSPG3.get(1).stream().mapToInt(i->i).toArray();
				this.green_CSP3 = liste_CSPG3.get(2).stream().mapToInt(i->i).toArray();
				this.yellow_CSP3 = liste_CSPG3.get(3).stream().mapToInt(i->i).toArray();
				this.red_CSP3 = liste_CSPG3.get(4).stream().mapToInt(i->i).toArray();
				this.acces_CSP3 = liste_CSPG3.get(5).stream().mapToInt(i->i).toArray();	

				
				this.compteur = 0;
				
				
				
				
				
				
				
				
				
			}

			 
			int[] New = new int[4];
			int[] New1 = new int[4];
			int[] New2 = new int[4];
			int[] New3 = new int[4];
			String[] Text_car1 = new String[7];
			String[] Text_car2 = new String[7];
			String[] Text_car3 = new String[7];
			
			
			if(!doublemethods && !triplemethods) {
				
					//New is a list of 2 elements, one for the new position and one for the new speed.
					if(method1bool && !method2bool && !method3bool) {
						System.out.println("Methode1");
						New = this.Sim_Cons_1.OneLap_IHM(this.xmin, this.vlim, 1,Lights);
					}
					else if(method2bool && !method1bool && !method3bool ) {
						System.out.println("Methode2");
				    	New = this.Sim_Cons_2.OneLap_IHM(this.xmin, this.vlim,Lights);
				    }
					else if(method3bool && !method1bool && !method2bool ) {
						System.out.println("Methode3");
				    	New = this.Sim_Cons_3.OneLap_IHM(this.xmin, this.vlim,Lights);
				    }
					
					else if(!method3bool && !method1bool && !method2bool  && cspglobal) {
						System.out.println("CSP Global");
						  New[0] = this.X_CSP1[this.compteur+1];
						  New[1] = this.V_CSP1[this.compteur+1];
				    }
					
					else if(!method3bool && !method1bool && !method2bool  && cspglobal2) {
						System.out.println("CSP Global");
						  New[0] = this.X_CSP2[this.compteur+1];
						  New[1] = this.V_CSP2[this.compteur+1];
				    }
					
					else if(!method3bool && !method1bool && !method2bool  && cspglobal3) {
						System.out.println("CSP Global");
						  New[0] = this.X_CSP3[this.compteur+1];
						  New[1] = this.V_CSP3[this.compteur+1];
				    }
					//xdiff allows us to know how many "units" the car moved. 
					
				    int xdiff = (int)((New[0]-xk));
				    
				    deplacement(car, xdiff);
				    deplacement_label(info_car1,xdiff);
				    
				    //If the car ever go over the traffic light, we change the background of the interface. 
				    if(car.getLayoutX()>1700) {
				    	green.setLayoutX(135);
				    	yellow.setLayoutX(135);
				    	red.setLayoutX(135);
				    	flashy.setLayoutX(135);
				    	car.setLayoutX(car.getLayoutX()-1700+135);
				    	info_car1.setLayoutX(info_car1.getLayoutX()-1500+135);
				    }
				    
				    //Update the current position and the current speed
					this.xk = New[0];
					this.vk = New[1];
					//These lists are updated too, they are used for the graphics after.
					this.list_X1[compteur_liste] = New[0]/this.unitScale;
					this.list_V1[compteur_liste] = New[1]/this.unitScale;
					position.setText(text_pos+(this.xk)/10+" m");
					vitesse.setText(text_vit+(this.vk)*0.36+ " km/h");
					System.out.println("xk+1 = " + xk + " dm");		
					System.out.println("vk+1 = " + vk + "dm/s");
					
					if(method1bool){
						Text_car1[0] = "Method : 1";
					}
					if(method2bool){
						Text_car1[0] = "Method : 2";
					}
					if(method3bool){
						Text_car1[0] = "Method : 3";
					}
					if(cspglobal){
						Text_car1[0] = "Method : CSPG1";
					}
					if(cspglobal2){
						Text_car1[0] = "Method : CSPG2";
					}
					if(cspglobal3){
						Text_car1[0] = "Method : CSPG3";
					}
					
					Text_car1[1] = "Position: " + this.xk/this.unitScale + "m";
					Text_car1[2] = "Vitesse: " + this.vk/this.unitScale + "m/s";
					Text_car1[3] = "Acceleration initiale: " + this.a0/this.unitScale + "m/s^2";
					
					info_car1.setTextList(Text_car1);
					
					

				
			}
			//Same thing for double methods and triple methods.
			else if(doublemethods) {
				
				System.out.println("Double method");
				if(method1bool && method2bool) {
					New1 = this.Sim_Cons_1.OneLap_IHM(this.xmin, this.vlim, 1,Lights);
					New2 = this.Sim_Cons_2.OneLap_IHM(this.xmin, this.vlim,Lights);
					Text_car1[0] = "Method : 1";
					Text_car2[0] = "Method : 2";
					
				}
				else if(method2bool && method3bool) {
					New1 = this.Sim_Cons_2.OneLap_IHM(this.xmin, this.vlim,Lights);
					New2 = this.Sim_Cons_3.OneLap_IHM(this.xmin, this.vlim,Lights);
					Text_car1[0] = "Method : 2";
					Text_car2[0] = "Method : 3";
			    }
				else if(method3bool && method1bool) {
					New1 = this.Sim_Cons_1.OneLap_IHM(this.xmin, this.vlim, 1,Lights);
					New2 = this.Sim_Cons_3.OneLap_IHM(this.xmin, this.vlim,Lights);
					Text_car1[0] = "Method : 1";
					Text_car2[0] = "Method : 3";
			    }
				
				else if(cspglobal && method1bool) {
					New1 = this.Sim_Cons_1.OneLap_IHM(this.xmin, this.vlim, 1,Lights);
					New2[0] = this.X_CSP1[this.compteur+1];
					New2[1] = this.V_CSP1[this.compteur+1];
					
					Text_car1[0] = "Method : 1";
					Text_car2[0] = "Method : CSPG1";
			    }
				else if(method2bool && cspglobal) {
					New1 = this.Sim_Cons_2.OneLap_IHM(this.xmin, this.vlim,Lights);
					New2[0] = this.X_CSP1[this.compteur+1];
					New2[1] = this.V_CSP1[this.compteur+1];
					Text_car1[0] = "Method : 2";
					Text_car2[0] = "Method : CSPG1";
			    }
				
				else if(method3bool && cspglobal) {
					New1 = this.Sim_Cons_3.OneLap_IHM(this.xmin, this.vlim,Lights);
					New2[0] = this.X_CSP1[this.compteur+1];
					New2[1] = this.V_CSP1[this.compteur+1];
					Text_car1[0] = "Method : 3";
					Text_car2[0] = "Method : CSPG1";
			    }
				
				else if(cspglobal2 && cspglobal) {
				
					New[0] = this.X_CSP1[this.compteur+1];
					New[1] = this.V_CSP1[this.compteur+1];
					
					New2[0] = this.X_CSP2[this.compteur+1];
					New2[1] = this.V_CSP2[this.compteur+1];
					Text_car1[0] = "Method : CSPG1";
					Text_car2[0] = "Method : CSPG2";
			    }
				else if(cspglobal && cspglobal3) {
					
					New[0] = this.X_CSP1[this.compteur+1];
					New[1] = this.V_CSP1[this.compteur+1];
					
					New2[0] = this.X_CSP3[this.compteur+1];
					New2[1] = this.V_CSP3[this.compteur+1];
					Text_car1[0] = "Method : CSPG1";
					Text_car2[0] = "Method : CSPG3";
			    }
				
				else if(cspglobal2 && cspglobal3) {
					
					New[0] = this.X_CSP2[this.compteur+1];
					New[1] = this.V_CSP2[this.compteur+1];
					
					New2[0] = this.X_CSP3[this.compteur+1];
					New2[1] = this.V_CSP3[this.compteur+1];
					Text_car1[0] = "Method : CSPG2";
					Text_car2[0] = "Method : CSPG3";
					
			    }
				
				else if(method2bool && cspglobal2) {
					New1 = this.Sim_Cons_2.OneLap_IHM(this.xmin, this.vlim,Lights);
					New2[0] = this.X_CSP2[this.compteur+1];
					New2[1] = this.V_CSP2[this.compteur+1];
					Text_car1[0] = "Method : 2";
					Text_car2[0] = "Method : CSPG2";
			    }
				
				else if(method3bool && cspglobal3) {
					New1 = this.Sim_Cons_3.OneLap_IHM(this.xmin, this.vlim,Lights);
					

					New2[0] = this.X_CSP3[this.compteur+1];
					New2[1] = this.V_CSP3[this.compteur+1];
					Text_car1[0] = "Method : 3";
					Text_car2[0] = "Method : CSPG3";
			    }

			    
			    int xdiff1 = (int)((New1[0]-xk1));
			    int xdiff2 = (int)((New2[0]-xk2));
			    deplacement(car, xdiff1);
			    deplacement(car2, xdiff2);
			    
			    deplacement_label(info_car1,xdiff1);
			    deplacement_label(info_car2,xdiff2);
			    
			    this.xk1 = New1[0];
				this.vk1 = New1[1];
			    
				this.xk2 = New2[0];
				this.vk2 = New2[1];
				
				
				this.list_X1[compteur_liste] = New1[0]/this.unitScale;
				this.list_V1[compteur_liste] = New1[1]/this.unitScale;
				
				this.list_X2[compteur_liste] = New2[0]/this.unitScale;
				this.list_V2[compteur_liste] = New2[1]/this.unitScale;
				
				position1.setText(text_pos+(this.xk1)/10+" m");
				vitesse1.setText(text_vit+(this.vk1)*0.36+ " km/h");
				
				position2.setText(text_pos+(this.xk2)/10+" m");
				vitesse2.setText(text_vit+(this.vk2)*0.36+ " km/h");
				
				Text_car1[1] = "Position: " + this.xk1/this.unitScale + "m";
				Text_car1[2] = "Vitesse: " + this.vk1/this.unitScale + "m/s";
				Text_car1[3] = "Acceleration initiale: " + this.a0/this.unitScale + "m/s^2";
				
				info_car1.setTextList(Text_car1);
				
				Text_car2[1] = "Position: " + this.xk2/this.unitScale + "m";
				Text_car2[2] = "Vitesse: " + this.vk2/this.unitScale + "m/s";
				Text_car2[3] = "Acceleration initiale: " + this.a0/this.unitScale + "m/s^2";
				
				info_car2.setTextList(Text_car2);
				
			    
			}
			
			else if(triplemethods){
				System.out.println("Triple method");
				
				if(method1bool && method2bool && method3bool) {
					New1 = this.Sim_Cons_1.OneLap_IHM(this.xmin, this.vlim, 1,Lights);
					New2 = this.Sim_Cons_2.OneLap_IHM(this.xmin, this.vlim,Lights);
					New3 = this.Sim_Cons_3.OneLap_IHM(this.xmin, this.vlim,Lights);
					Text_car1[0] = "Method : 1";
					Text_car2[0] = "Method : 2";
					Text_car3[0] = "Method : 3";
					
				}
				else if(method2bool && method3bool && cspglobal) {
					New1 = this.Sim_Cons_2.OneLap_IHM(this.xmin, this.vlim,Lights);
					New2 = this.Sim_Cons_3.OneLap_IHM(this.xmin, this.vlim,Lights);
					New3[0] = this.X_CSP1[this.compteur+1];
					New3[1] = this.V_CSP1[this.compteur+1];
					
					Text_car1[0] = "Method : 2";
					Text_car2[0] = "Method : 3";
					Text_car3[0] = "Method : CSPG1";
			    }
				else if(method3bool && method1bool && cspglobal) {
					New1 = this.Sim_Cons_1.OneLap_IHM(this.xmin, this.vlim, 1,Lights);
					New2 = this.Sim_Cons_3.OneLap_IHM(this.xmin, this.vlim,Lights);
					New3[0] = this.X_CSP1[this.compteur+1];
					New3[1] = this.V_CSP1[this.compteur+1];
					Text_car1[0] = "Method : 1";
					Text_car2[0] = "Method : 3";
					Text_car3[0] = "Method : CSPG1";
			    }
				
				else if(cspglobal && method1bool && method2bool) {
					New1 = this.Sim_Cons_1.OneLap_IHM(this.xmin, this.vlim, 1,Lights);
					New2 = this.Sim_Cons_2.OneLap_IHM(this.xmin, this.vlim,Lights);
					New3[0] = this.X_CSP1[this.compteur+1];
					New3[1] = this.V_CSP1[this.compteur+1];
					
					Text_car1[0] = "Method : 1";
					Text_car2[0] = "Method : 2";
					Text_car3[0] = "Method : CSPG1";
			    }

				else if(cspglobal && cspglobal2 && cspglobal3) {

					New1[0] = this.X_CSP1[this.compteur+1];
					New1[1] = this.V_CSP1[this.compteur+1];
					
					New2[0] = this.X_CSP2[this.compteur+1];
					New2[1] = this.V_CSP2[this.compteur+1];
					
					New3[0] = this.X_CSP3[this.compteur+1];
					New3[1] = this.V_CSP3[this.compteur+1];
					
					
					
					Text_car1[0] = "Method : CSPG1";
					Text_car2[0] = "Method : CSPG2";
					Text_car3[0] = "Method : CSPG3";
			    }
				
				
				
			    
			    int xdiff1 = (int)((New1[0]-xk1));
			    int xdiff2 = (int)((New2[0]-xk2));
			    int xdiff3 = (int)((New3[0]-xk3));
			    deplacement(car, xdiff1);
			    deplacement(car2, xdiff2);
			    deplacement(car3, xdiff3);
			    
			    deplacement_label(info_car1,xdiff1);
			    deplacement_label(info_car2,xdiff2);
			    deplacement_label(info_car3,xdiff3);
			    
			    this.xk1 = New1[0];
				this.vk1 = New1[1];
			    
				this.xk2 = New2[0];
				this.vk2 = New2[1];
				
				this.xk3 = New3[0];
				this.vk3 = New3[1];
				
				this.list_X1[compteur_liste] = New1[0]/this.unitScale;
				this.list_V1[compteur_liste] = New1[1]/this.unitScale;
				
				this.list_X2[compteur_liste] = New2[0]/this.unitScale;
				this.list_V2[compteur_liste] = New2[1]/this.unitScale;
				
				this.list_X3[compteur_liste] = New3[0]/this.unitScale;
				this.list_V3[compteur_liste] = New3[1]/this.unitScale;
				position1.setText(text_pos+(this.xk1)/10+" m");
				vitesse1.setText(text_vit+(this.vk1)*0.36+ " km/h");
				
				position2.setText(text_pos+(this.xk2)/10+" m");
				vitesse2.setText(text_vit+(this.vk2)*0.36+ " km/h");
				
				position3.setText(text_pos+(this.xk3)/10+" m");
				vitesse3.setText(text_vit+(this.vk3)*0.36+ " km/h");
				
				
				Text_car1[1] = "Position: " + this.xk1/this.unitScale + "m";
				Text_car1[2] = "Vitesse: " + this.vk1/this.unitScale + "m/s";
				Text_car1[3] = "Acceleration initiale: " + this.a0/this.unitScale + "m/s^2";
				
				info_car1.setTextList(Text_car1);
				
				Text_car2[1] = "Position: " + this.xk2/this.unitScale + "m";
				Text_car2[2] = "Vitesse: " + this.vk2/this.unitScale + "m/s";
				Text_car2[3] = "Acceleration initiale: " + this.a0/this.unitScale + "m/s^2";
				
				info_car2.setTextList(Text_car2);
				
				Text_car3[1] = "Position: " + this.xk3/this.unitScale + "m";
				Text_car3[2] = "Vitesse: " + this.vk3/this.unitScale + "m/s";
				Text_car3[3] = "Acceleration initiale: " + this.a0/this.unitScale + "m/s^2";
				
				info_car3.setTextList(Text_car3);
				
			}
	
			this.compteur_liste++;
			this.compteur++;
			try {
			    Thread.sleep(300);
			} catch(InterruptedException e) {
			    System.out.println("Petite pause!");
			}
			}
		 
	}
	
	//This function allows the update of the traffic light. (with the user pressing G,Y,R or D.
	private boolean[] update_TL_IHM() {
		
			
			
			
		if(isDegratedKeypressed ) {
			//System.out.println("Feu VERT");
			green_bool = false;
			red_bool = false;
			yellow_bool = false;
			degrated_bool = true;
			this.Actual_Light = "No working lights";
			
					
		}
		
		
		if(isGreenKeypressed ) {
			//System.out.println("Feu VERT");
			green_bool = true;
			red_bool = false;
			yellow_bool = false;
			degrated_bool = false;
			this.Actual_Light = "Green";
		}
		
		
		if(isYellowKeypressed ) {
			//System.out.println("Feu Jaune");
			green_bool = false;
			red_bool = false;
			yellow_bool = true;
			degrated_bool = false;
			this.Actual_Light = "Yellow";
			
			
		}
		
		if(isRedKeypressed ) {
			
			//System.out.println("Feu Rouge");
			green_bool = false;
			red_bool = true;
			yellow_bool = false;
			degrated_bool = false;
			this.Actual_Light = "Red";
			
		}
		
		if(Actual_Light != Before_Light){
			
		}
		
		
	     if(red_bool){

	    	 supprimer(green);
	    	 supprimer(yellow);
	    	 apparaitre(red);
	    	 supprimer(flashy);
	     }
				

	     if(yellow_bool){

	    	 supprimer(red);
	    	 supprimer(green);
	    	 apparaitre(yellow);
	    	 supprimer(flashy);
			}
			

	     if(green_bool){
	    	 supprimer(red);
	    	 supprimer(yellow);
	    	 apparaitre(green);
	    	 supprimer(flashy);
			}
		
		if(degrated_bool){
			 supprimer(red);
	    	 supprimer(yellow);
	    	 supprimer(green);
	    	 apparaitre(flashy);
			
		}
		boolean[] Lights = new boolean[3];
		
		Lights[0] = red_bool;
		Lights[1] = yellow_bool;
		Lights[2] = green_bool;
		return Lights;
	}
	
	
	private void update_TL() {
		
		if(isGreenKeypressed || (greenint == 1)) {
			//System.out.println("Feu VERT");
			green_bool = true;
			red_bool = false;
			yellow_bool = false;
					
		}
		
		
		if(isYellowKeypressed || (yellowint == 1)) {
			//System.out.println("Feu Jaune");
			green_bool = false;
			red_bool = false;
			yellow_bool = true;
			
			
		}
		
		if(isRedKeypressed || (redint == 1)) {
			
			//System.out.println("Feu Rouge");
			green_bool = false;
			red_bool = true;
			yellow_bool = false;
			
		}
		
		
	     if(red_bool){

	    	 supprimer(green);
	    	 supprimer(yellow);
	    	 apparaitre(red);
	    	 supprimer(flashy);

	     }
				

	     if(yellow_bool){

	    	 supprimer(red);
	    	 supprimer(green);
	    	 apparaitre(yellow);
	    	 supprimer(flashy);
			}
			

	     if(green_bool){
	    	 supprimer(red);
	    	 supprimer(yellow);
	    	 apparaitre(green);
	    	 supprimer(flashy);
			}
		
		
	}
	
	
	//Allow the user to quit the interface by pressing ESC.
	private void Quitter() {
		
		if(isExitKeypressed) {
				
			gameStage.close();
			}
	}

	
	
	//This function allows to hide an imageview.
	private void supprimer(ImageView Image) {
		if(gamePane.getChildren().contains(Image)) {
			
			gamePane.getChildren().remove(Image) ;
			Image = null;
	
			
		}


	}
	//This function allows to hide a panel
	private void supprimer_label(BigInfoLabel label) {
		if(gamePane.getChildren().contains(label)) {
			
			gamePane.getChildren().remove(label) ;
			label = null;
			
		}


	}
	
	
	//This function allows to show an imageview
	private void apparaitre(ImageView Image) {
		if(!gamePane.getChildren().contains(Image)) {
			
			gamePane.getChildren().add(Image) ;
			
		}
		
	}
	
	//This function allows to show a panel
	private void apparaitre_label(BigInfoLabel label) {
		if(!gamePane.getChildren().contains(label)) {
			
			gamePane.getChildren().add(label) ;
			
		}
		
	}
	
	
	//allow to move an imageview(car or trafficlight) (with xdiff)
	private void deplacement(ImageView Image, int xdiff) {

		Image.setLayoutX(Image.getLayoutX()+xdiff);
		//System.out.println("xlayout" + Image.getLayoutX());
		//Image.setLayoutX(Image.getLayoutX()+1900);
	}
	
	//allow to move a panel(car or trafficlight)
	private void deplacement_label(BigInfoLabel label, int xdiff) {

		label.setLayoutX(label.getLayoutX()+xdiff);
		//System.out.println("xlayout" + Image.getLayoutX());
		//Image.setLayoutX(Image.getLayoutX()+1900);
	}

	

	
	//Create the background with the URL BACKGROUND_IMAGE
	private void createBackground() {
		gridPane1 = new GridPane();
	
		ImageView backgroundImage1 = new ImageView(BACKGROUND_IMAGE);
		gridPane1.getChildren().add(backgroundImage1);

		
		
		gamePane.getChildren().add(gridPane1);
	}
	
	private void sleep(int time) {
		
		try {
		    Thread.sleep(time);
		} catch(InterruptedException e) {
		    System.out.print("Petite pause!");
		}
		
		
	}
	
	//This function allows the user to charge a new solution and to update the interface (car, trafic light)
	private void charge_solution(){
		
		if(choix_charge){
						
			choix_charge = false;
			this.compteur_opti = 0;
			this.compteur_liste = 0;
			this.choix_solution = (int) this.choiceBox.getValue();
			
			
			this.Solutions_generer.add(this.choix_solution);
			
			int x = x0;
			int y = 22;
			car.setLayoutX(1700+x);
			car.setLayoutY(y*16);
			
			green.setLayoutX(1755);  //le bord gauche se trouve a -175.5(*10)
			green.setLayoutY(2*30);
			red.setLayoutX(1755);  //le bord gauche se trouve a -175.5(*10)
			red.setLayoutY(2*30);
			yellow.setLayoutX(1755);  //le bord gauche se trouve a -175.5(*10)
			yellow.setLayoutY(2*30);
			flashy.setLayoutX(1755);  //le bord gauche se trouve a -175.5(*10)
			flashy.setLayoutY(2*30);
			
			this.xk = x0;
			this.vk = v0;
			

		}
		
	}
	
	

	// 
	private void graph(){
		
		if(graph_comparatif){
			System.out.println(list_X3);
			System.out.println(list_V3);
			System.out.println(list_X1);
			System.out.println(list_V1);
			if(triplemethods){
				
				Graph graph = new Graph();
				
				graph.demarrer3(this.gameStage, list_X1, list_V1, list_X2, list_V2, list_X3, list_V3, method1bool, method2bool, method3bool, cspglobal, cspglobal2, cspglobal3, this.compteur_liste);
				graph_comparatif = false;

			}
			
			if(doublemethods){
				
				Graph graph = new Graph();
				
				graph.demarrer2(this.gameStage, list_X1, list_V1, list_X2, list_V2, method1bool, method2bool, method3bool, cspglobal, cspglobal2, cspglobal3, this.compteur_liste);
				graph_comparatif = false;
				
				
			}
			else if(!doublemethods && !triplemethods){
				
				Graph graph = new Graph();
				
				graph.demarrer1(this.gameStage, list_X1, list_V1, method1bool, method2bool, method3bool, cspglobal, cspglobal2, cspglobal3, this.compteur_liste);
				graph_comparatif = false;

			}

		}

	}
	
	private void graph_opti(){
		
		if(graph_comparatif){

			if(!doublemethods && !triplemethods){
				
				Graph_Opti graph = new Graph_Opti();
				graph.demarrer1(this.gameStage, Liste_globale, Liste_globale.length,13);
				graph.demarrer2(this.gameStage, Liste_globale, Liste_globale.length,13,this.Solutions_generer);
				graph_comparatif = false;

			}

		}

	}

}


















