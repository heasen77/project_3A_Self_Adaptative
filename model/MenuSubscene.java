package model;

import javafx.animation.TranslateTransition;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.util.Duration;

public class MenuSubscene extends SubScene{

	private final static String FONT_PATH = "src/model/ressources/kenney.ttf";
	private final static String BACKGROUND_IMAGE = "/model/ressources/test6.png";
	private boolean isHidden;
	
	public MenuSubscene() {
		super(new AnchorPane(), 525 , 312.5);
		// TODO Auto-generated constructor stub
		prefWidth(525);
		prefHeight(312.5);
		
		
		BackgroundImage image = new BackgroundImage(new Image(BACKGROUND_IMAGE, 555,262.5, false, true),
			BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);
		AnchorPane root2 = (AnchorPane) this.getRoot();
		root2.setBackground(new Background(image));
		isHidden = true;
		setLayoutX(976);
		setLayoutY(150);
		
		
	}

	public void moveSubScene() {
		TranslateTransition transition = new TranslateTransition();
		transition.setDuration(Duration.seconds(0.3));
		transition.setNode(this);
		if(isHidden) {
			transition.setToX(-676);
			isHidden = false;
		}
		else {
			transition.setToX(0);
			isHidden=true;
		}
		transition.play();
	}
	
	public AnchorPane getPane() {
		return (AnchorPane) this.getRoot();
	}
	
	
	
}
