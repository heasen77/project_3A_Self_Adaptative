package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.text.Font;

public class Infolabel extends Label {
	public final static String FONT_PATH = "src/model.ressources/kenney.ttf";
			
	private final static String BACKGROUND_IMAGE = "/model/ressources/alez.png";
	
	public Infolabel(String text) {
		setPrefWidth(200);
		setPrefHeight(40);
		setText(text);
		setWrapText(true);
		setLabelFont();
		setAlignment(Pos.BASELINE_LEFT);
		
		BackgroundImage backgroundImage = new BackgroundImage(new Image(BACKGROUND_IMAGE, 325,212.5, false, true),
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);
		setBackground(new Background(backgroundImage));
	
	
	}
			
	private void setLabelFont() {
		try{
			setFont(Font.loadFont(new FileInputStream(new File(FONT_PATH)),18));
		} catch(FileNotFoundException e) {
			setFont(Font.font("Verdana",18));
			
		}
		
	}
	
	
}
