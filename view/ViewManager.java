/*Project Issou Ecosystem
 * 
 * ViewManager permet d'afficher le menu d'acceuil du jeu
 * 
 * 
 */



package view;

import java.util.ArrayList;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Carpicker;
import model.Infolabel;
import model.MenuBouton;
import model.MenuSubscene;
import model.Carurl;

public class ViewManager {
	private static final int HEIGHT = 425;      //hauteur du menu
	private static final int WIDTH = 850;      //largeur du menu
	private static final int MENU_BUTTON_START_X = 50; //position du bouton start (initial)
	private static final int MENU_BUTTON_START_Y = 100;

	private AnchorPane mainPane;
	private Scene mainScene;
	private Stage mainStage;
	
	private ChoiceBox choiceBox;
	private MenuSubscene StartSubScene;
	private MenuSubscene aproposSubScene, aproposSubScene2;
	private MenuSubscene ExitSubScene;

	private Spinner<Integer> pos_initial;
	private Spinner<Integer> vit_initial;
	private Spinner<Integer> acce_initiale;
	private Spinner<Integer> amin;
	private Spinner<Integer> amax;
	private Spinner<Integer> vit_max;
	private Spinner<Integer> unit;
	private Spinner<Integer> vmin;
	private Spinner<Integer> xmin;
	private CheckBox checkBox1,checkBox2, checkBox3, checkBox4, checkBox5;
	
	private MenuSubscene sceneToHide;
	
	List<MenuBouton> menuButtons;
	List<Carpicker> CarList;
	private CheckBox checkBox6;
	private CheckBox checkBox7;
	private CheckBox checkBox8;
	private CheckBox checkBox9;

	
	public ViewManager() {
		choiceBox = new ChoiceBox();
		
		menuButtons = new ArrayList<>();  //creation d'une liste de boutons
		mainPane = new AnchorPane(); //creation d'un anchorpane
		mainScene = new Scene(mainPane, WIDTH, HEIGHT); //creation d'une scène
		mainStage = new Stage(); //creation d'un stage

		mainStage.setScene(mainScene);
		mainStage.getIcons().add(new Image("view/ressources/voitures/car.png"));  //ajout d'un icone pour le jeu
		mainStage.setTitle("Car simulation"); //ajout d'un titre
		//appel aux fonctions permettant la creation du contenu du menu
		createSubScene(); 
		createButton();
		createBackground();
		createLogo();
		
	}
	
	
	//fonction permettant de montrer le contenu de "start", "paramètre"
	private void showSubScene(MenuSubscene subScene) {
		if(sceneToHide != null) {
			sceneToHide.moveSubScene();
			
		}
		subScene.moveSubScene();
		sceneToHide = subScene;
	}
	
	
	//fonction permettant de créer la subscene lié à chaque sous menu (exit, start, paramètre)
	public  void createSubScene() {
		createAniChooserSubScene();
		createaproposSubScene();
		createaproposSubScene2();
	}
	
	private void createAniChooserSubScene() {
		StartSubScene = new MenuSubscene();
		mainPane.getChildren().add(StartSubScene);

		StartSubScene.getPane().getChildren().add(createAniToChoose());
		StartSubScene.getPane().getChildren().add(createAniToChoose2());
		StartSubScene.getPane().getChildren().add(createAniToChoose3());
		StartSubScene.getPane().getChildren().add(createButtonToSimuler());
	}

	
	private void createaproposSubScene2() {
		aproposSubScene2 = new MenuSubscene();
		mainPane.getChildren().add(aproposSubScene2);
		
		
        choiceBox.getItems().add("Red");
        choiceBox.getItems().add("Yellow");
        choiceBox.getItems().add("Green");
        choiceBox.getItems().add("No working lights");
        Infolabel  chooseLights = new Infolabel("Light Initial color");
        chooseLights.setLayoutX(100);
        chooseLights.setLayoutY(40);
        HBox hbox = new HBox(choiceBox);
        hbox.setLayoutX(120);
        hbox.setLayoutY(80);
        aproposSubScene2.getPane().getChildren().add(hbox);
        aproposSubScene2.getPane().getChildren().add(chooseLights);

	}
	
	
	//subscene permettant de paramètrer le nombre de tour que l'on souhaite
	private void createaproposSubScene() {
		aproposSubScene = new MenuSubscene();
		mainPane.getChildren().add(aproposSubScene);

		
		pos_initial = new Spinner<Integer>(-1000,0,-90);
		pos_initial.setLayoutX(80);
		pos_initial.setLayoutY(70);
		pos_initial.setPrefWidth(100);
		Infolabel  chooseParaLabel2 = new Infolabel("x0");
		chooseParaLabel2.setLayoutX(100);
		chooseParaLabel2.setLayoutY(40);
		aproposSubScene.getPane().getChildren().add(pos_initial);
		aproposSubScene.getPane().getChildren().add(chooseParaLabel2);
		
		vit_initial = new Spinner<Integer>(0,100,10);
		vit_initial.setLayoutX(200);
		vit_initial.setLayoutY(70);
		vit_initial.setPrefWidth(100);
		Infolabel  chooseParaLabel3 = new Infolabel("v0");
		chooseParaLabel3.setLayoutX(250);
		chooseParaLabel3.setLayoutY(40);
		aproposSubScene.getPane().getChildren().add(vit_initial);
		aproposSubScene.getPane().getChildren().add(chooseParaLabel3);
		
		acce_initiale = new Spinner<Integer>(0,100,0);
		acce_initiale.setLayoutX(320);
		acce_initiale.setLayoutY(70);
		acce_initiale.setPrefWidth(100);
		Infolabel  chooseParaLabel4 = new Infolabel("a0");
		chooseParaLabel4.setLayoutX(400);
		chooseParaLabel4.setLayoutY(40);
		aproposSubScene.getPane().getChildren().add(acce_initiale);
		aproposSubScene.getPane().getChildren().add(chooseParaLabel4);
		
		amin = new Spinner<Integer>(-1000,100,-4);
		amin.setLayoutX(80);
		amin.setLayoutY(120);
		amin.setPrefWidth(100);
		Infolabel  chooseParaLabel5 = new Infolabel("amin");
		chooseParaLabel5.setLayoutX(100);
		chooseParaLabel5.setLayoutY(85);
		aproposSubScene.getPane().getChildren().add(amin);
		aproposSubScene.getPane().getChildren().add(chooseParaLabel5);
		
		amax = new Spinner<Integer>(-1000,1000,3);
		amax.setLayoutX(200);
		amax.setLayoutY(120);
		amax.setPrefWidth(100);
		Infolabel  chooseParaLabel6 = new Infolabel("amax");
		chooseParaLabel6.setLayoutX(250);
		chooseParaLabel6.setLayoutY(85);
		aproposSubScene.getPane().getChildren().add(amax);
		aproposSubScene.getPane().getChildren().add(chooseParaLabel6);
		
		vit_max = new Spinner<Integer>(0,150,30);
		vit_max.setLayoutX(320);
		vit_max.setLayoutY(120);
		vit_max.setPrefWidth(100);
		Infolabel  chooseParaLabel7 = new Infolabel("vmax");
		chooseParaLabel7.setLayoutX(380);
		chooseParaLabel7.setLayoutY(85);
		aproposSubScene.getPane().getChildren().add(vit_max);
		aproposSubScene.getPane().getChildren().add(chooseParaLabel7);
		
		vmin = new Spinner<Integer>(-1000,100,14);
		vmin.setLayoutX(80);
		vmin.setLayoutY(170);
		vmin.setPrefWidth(100);
		Infolabel  chooseParaLabel8 = new Infolabel("vmin");
		chooseParaLabel8.setLayoutX(100);
		chooseParaLabel8.setLayoutY(130);
		aproposSubScene.getPane().getChildren().add(vmin);
		aproposSubScene.getPane().getChildren().add(chooseParaLabel8);
		
		unit = new Spinner<Integer>(0,1000,10);
		unit.setLayoutX(200);
		unit.setLayoutY(170);
		unit.setPrefWidth(100);
		Infolabel  chooseParaLabel9 = new Infolabel("unit");
		chooseParaLabel9.setLayoutX(250);
		chooseParaLabel9.setLayoutY(130);
		aproposSubScene.getPane().getChildren().add(unit);
		aproposSubScene.getPane().getChildren().add(chooseParaLabel9);
				
		xmin = new Spinner<Integer>(-1000,150,-100);
		xmin.setLayoutX(320);
		xmin.setLayoutY(170);
		xmin.setPrefWidth(100);
		Infolabel  chooseParaLabel10 = new Infolabel("xmin");
		chooseParaLabel10.setLayoutX(400);
		chooseParaLabel10.setLayoutY(130);
		aproposSubScene.getPane().getChildren().add(xmin);
		aproposSubScene.getPane().getChildren().add(chooseParaLabel10);

	}
	
	private HBox createAniToChoose() {
		HBox box = new HBox();
		box.setSpacing(10);
		CarList = new ArrayList<>();
		for (Carurl car1 : Carurl.values()) {
			
			Carpicker CarToPick = new Carpicker(car1);
			//box.getChildren().add(CarToPick);
			CarList.add(CarToPick);

		}
		
		this.checkBox1 = new CheckBox("Method 1");
		this.checkBox2 = new CheckBox("Method 2");
		this.checkBox3 = new CheckBox("Method 3");
		

		box.getChildren().add(checkBox1);
		box.getChildren().add(checkBox2);
		box.getChildren().add(checkBox3);
		
		//System.out.print(CarList);
		box.setLayoutX(105);
		box.setLayoutY(60);
		
		return box;
	}
	
	private HBox createAniToChoose2() {
		HBox box = new HBox();
		box.setSpacing(10);
		
		this.checkBox4 = new CheckBox("CSPG1");
		this.checkBox6 = new CheckBox("CSPG2");
		this.checkBox7 = new CheckBox("CSPG3");

		
		
		box.getChildren().add(checkBox4);
		box.getChildren().add(checkBox6);
		box.getChildren().add(checkBox7);
		//System.out.print(CarList);
		box.setLayoutX(105);
		box.setLayoutY(100);

		return box;
	}
	
	private HBox createAniToChoose3() {
		HBox box = new HBox();
		box.setSpacing(10);
		

		this.checkBox5 = new CheckBox("CSPG1 Opti");
		this.checkBox8 = new CheckBox("CSPG2 Opti");
		this.checkBox9 = new CheckBox("CSPG3 Opti");
		
		box.getChildren().add(checkBox5);
		box.getChildren().add(checkBox8);
		box.getChildren().add(checkBox9);
		//System.out.print(CarList);
		box.setLayoutX(105);
		box.setLayoutY(140);

		return box;
	}
	
	
	
	//Creation du bouton simuler et permet de lancer la nouvelle fenetre de jeu (dans la classe GameViewManager)
	
	private MenuBouton createButtonToSimuler() {
		
		
		MenuBouton simulerButton = new MenuBouton("Simuler");
		simulerButton.setLayoutX(150);
		simulerButton.setLayoutY(170);
			
		simulerButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				GameViewManager gameManager = new GameViewManager(pos_initial.getValue(), vit_initial.getValue(), acce_initiale.getValue(), vit_max.getValue(), amin.getValue(), amax.getValue(), vmin.getValue(), unit.getValue(), xmin.getValue(),checkBox1.isSelected(),checkBox2.isSelected(),checkBox3.isSelected(),checkBox4.isSelected(),checkBox6.isSelected() ,checkBox7.isSelected() , checkBox5.isSelected(), (String)choiceBox.getValue() );
				gameManager.createNewGame(mainStage, CarList);
				
			}
			
			
			
			
		});
		
		return simulerButton;
	}
	
	
	
	
	public Stage getMainStage() {
		return mainStage;
	}
	
	private void addMenuButton(MenuBouton button) {
		button.setLayoutX(MENU_BUTTON_START_X);
		button.setLayoutY(MENU_BUTTON_START_Y + menuButtons.size()*100);
		menuButtons.add(button);
		mainPane.getChildren().add(button);
	}
	
	private void createButton() {
		createStartButton();
		createAproposButton();
		createApropos2Button();
		//createExitButton();
	}
	
	private void createStartButton() {
		MenuBouton startbutton = new MenuBouton("START");
		addMenuButton(startbutton);
		
		startbutton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				showSubScene(StartSubScene);
			}
			

		});

	}
	
	private void createAproposButton() {
		MenuBouton aproposbutton = new MenuBouton("PARAMETERS");
		addMenuButton(aproposbutton);
		
		aproposbutton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				showSubScene(aproposSubScene);
			}
			
			
			
			
		});

	}
	
	private void createApropos2Button() {
		MenuBouton aproposbutton2 = new MenuBouton("PARAMETERS 2");
		addMenuButton(aproposbutton2);
		
		aproposbutton2.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				showSubScene(aproposSubScene2);
			}
			
			
			
			
		});

	}
	
	private void createExitButton() {
		MenuBouton exitbutton = new MenuBouton("EXIT");
		addMenuButton(exitbutton);
		exitbutton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				mainStage.close();
			}
			
			
			
			
		});

	}
	
	private void createBackground() {
		Image backgroundImage = new Image("view/ressources/bg_car.png", 850 ,425, false,true);
		BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
		mainPane.setBackground(new Background(background));
	}
	
	
	//permet la création du logo sur le menu
	
	private void createLogo() {
		
		ImageView logo = new ImageView("view/ressources/logo3.png");
		logo.setLayoutX(350);
		logo.setLayoutY(0);
		logo.setOnMouseEntered(new EventHandler<MouseEvent>() {
			
			@Override
			public void handle(MouseEvent event) {
				logo.setEffect(new DropShadow());
				
			}
		});
		
		logo.setOnMouseExited(new EventHandler<MouseEvent>() {
			
			@Override
			public void handle(MouseEvent event) {
				logo.setEffect(null);
				
			}
		});
		
		mainPane.getChildren().add(logo);
	}
	
}
