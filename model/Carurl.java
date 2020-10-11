package model;

public enum Carurl {

	CAR("view/ressources/voitures/car.png");
	
	private String urlAni;
	private Carurl(String urlAni) {
		this.urlAni = urlAni;
		
	}
	
	public String getUrl() {
		return this.urlAni;
	}
}
