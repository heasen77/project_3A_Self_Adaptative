package model;

import javafx.geometry.Pos;
import javafx.scene.control.Spinner;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;




public class TLpicker extends VBox {
	
	private ImageView TLImage;

	private TLurl TL;
	
	
	public TLpicker(TLurl TL) {
		TLImage = new ImageView(TL.getUrl());
		this.TL = TL;
		this.setAlignment(Pos.CENTER);
		this.setSpacing(5);

		this.getChildren().add(TLImage);

	}
	
	public TLurl getTL() {
		return TL;
		
	}

	
	
}