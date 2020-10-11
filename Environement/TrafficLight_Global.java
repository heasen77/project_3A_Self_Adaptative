package Environement;

public class TrafficLight_Global {
	
	private int position;
	private boolean degraded;

	private boolean green;
	private boolean red;
	private boolean yellow;



	public TrafficLight_Global(int position, boolean degraded, boolean red, boolean yellow , boolean green){
		//int[] initTL1 = {0, 5, 2, 5};
	        this.position = position;
	        this.degraded  = degraded;
	        this.red = red;
	        this.yellow = yellow;
	        this.green = green;

	}

	public int getPosition(){
		return position;
	}
	
	public void setDegraded(boolean degraded){
		this.degraded = degraded;
	}

	public boolean isDegraded(){
		return degraded;
	}
	
	public void setGreen(boolean green){
		this.green = green;
	}

	public boolean isGreen(){
		return green;
	}

	public void setYellow(boolean yellow){
	  	this.yellow = yellow;
	}

	public boolean isYellow(){
	  	return yellow;
	}

	public void setRed(boolean red){
		this.red = red;
	}

	public boolean isRed(){
	  	return red;
	}

	public void updateLightsIHM(boolean[] Lights){
		
		setGreen(Lights[2]);
		setYellow(Lights[1]);
		setRed(Lights[0]);
		if(!Lights[0] && !Lights[1] && !Lights[2]){
			setDegraded(true);

		}
		
	}

	@Override
	public String toString(){
		if (this.isGreen()){
			return "The traffic light is green !";
		}
		else if (this.isYellow()){
			return "The traffic light is yellow !";
		}
		else if (this.isRed()){
			return "The traffic light is red !";
		}
		return "The traffic is in degraded state !";
	}


}
