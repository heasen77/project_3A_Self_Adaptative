package Environement;

import java.util.ArrayList;


public class Environement {
	
	private int speedLimit;
	private ArrayList<Car> carList;
	private ArrayList<TrafficLight> trafficLightList;
	
	
	public Environement(int nbCar, int nbTrafficLight) {
		this.carList = new ArrayList<Car>(nbCar);
		this.trafficLightList = new ArrayList<TrafficLight>(nbTrafficLight);
	}

	public int getSpeedLimit(){return speedLimit;}

	public void setSpeedLimit(int newSpeedLimit){this.speedLimit = newSpeedLimit;}

	public ArrayList<Car> getCarList(){return carList;}

	public ArrayList<TrafficLight> getTrafficLightList(){return trafficLightList;}

	public void addCar(int i, Car car){
		carList.add(i, car);
	}

	public void addTrafficLight(int i, TrafficLight trafficLight){
		trafficLightList.add(i, trafficLight);
	}

}
