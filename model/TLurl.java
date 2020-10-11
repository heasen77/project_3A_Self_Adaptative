package model;

public enum TLurl {
	YELLOW("view/ressources/feux/yellow.png"),
	RED("view/ressources/feux/red.png"),
	GREEN("view/ressources/feux/green.png"),
	FLASHINGYELLOW("view/ressources/feux/flashing_y.gif");
	private String urlGreen;
	
	private TLurl(String urlGreen) {
		this.urlGreen = urlGreen;
		
	}
	
	public String getUrl() {
		return this.urlGreen;
	}
}
