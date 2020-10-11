package model;

import javafx.geometry.Pos;
import javafx.scene.control.Spinner;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;




public class Carpicker extends VBox {
	
	private ImageView CarImage;
	private Spinner<Integer> nombre;
	private Carurl car;
	
	
	public Carpicker(Carurl car) {
		CarImage = new ImageView(car.getUrl());
		this.car = car;
		this.setAlignment(Pos.CENTER);
		this.setSpacing(5);
		this.nombre = new Spinner<Integer>(0,10,0);
		this.nombre.setPrefWidth(70);
		this.getChildren().add(CarImage);
		this.getChildren().add(nombre);
	}
	
	public Carurl getCar() {
		return car;
		
	}
	
	public int getValue() {
		
		return nombre.getValue();
		
	}
	
	
	
}
