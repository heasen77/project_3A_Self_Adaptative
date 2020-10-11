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

public class BigInfoLabel extends Label{
	private final static String FONT_PATH = "src/model/ressources/kenney1.ttf";
	
	public BigInfoLabel(String[] text) {
		setPrefWidth(190);
		setPrefHeight(140);
		BackgroundImage backgroundImage = new BackgroundImage(
				new Image("/view/ressources/green_button_long.png",190,140, false, true), BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);
				
		setBackground(new Background(backgroundImage));
		setAlignment(Pos.CENTER_LEFT);
		setPadding(new Insets(10,10,10,10));
		setLabelFont();
		setText(text[0]+"\n"+text[1]+"\n"+text[2]+"\n"+text[3]+"\n"+text[4]+"\n"+text[5]+"\n"+text[6]);  //7 elements possible

		
	}
	
	
	public void setTextList(String[] text){
		
		setText(text[0]+"\n"+text[1]+"\n"+text[2]+"\n"+text[3]+"\n"+text[4]+"\n"+text[5]+"\n"+text[6]);  //7 elements possible
		
	}
	
	
	
	
	
	private void setLabelFont() {
		try {
			
			setFont(Font.loadFont(new FileInputStream(new File(FONT_PATH)),13));
			
		} catch(FileNotFoundException e) {
			setFont(Font.font("Verdana",13));
			
			
			
		}
		
		
	}
	

}
