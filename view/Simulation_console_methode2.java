package view;

import Environement.TrafficLight;
import Environement.Car;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;


public class Simulation_console_methode2 {

	private int timeStep;
	private int unitScale;
	private String unitstr;
	private Car car;
	private TrafficLight trafficLight;


	public Simulation_console_methode2(int h, int unit, int[] initTL1, boolean[] initTL2, int[] initCar){

		this.timeStep = h;
		this.unitScale = unit;
		this.car = new Car(initCar[0]*unit, initCar[1]*unit, initCar[2]*unit, initCar[3]*unit, initCar[4]*unit, initCar[5]*unit);
		this.trafficLight = new TrafficLight(initTL1[0]*unit, initTL1[1], initTL1[2], initTL1[3], initTL2[0], initTL2[1], initTL2[2], initTL2[3]);

		switch (unit){
			case 10:
				unitstr = " dm";
				break;
			case 100:
				unitstr = " cm";
				break;
			case 1000:
				unitstr = " mm";
				break;
		}
	}


	public int[] OneLap_IHM(int xMin, int vSL, boolean[] Lights){
		// xMin: Position from which the traffic light is considered
		// vSL: Speed Limit
		// tr: Reaction time

		xMin *= unitScale;
		vSL *= unitScale;

		// Creation of a new model
		Model model = new Model("Simulation");

		//trafficLight.updateLights();
		trafficLight.updateLightsIHM(Lights);
		// Get car parameters
		int xk = car.getPosition(); // Position of the car
		int xTL = trafficLight.getPosition(); // Position of the traffic light
		int xMax = (xTL*unitScale + car.stopDistance(1)); // Last position where the car can stop

		int vk = car.getSpeed(); // Speed of the car
		int vMax = car.getSpeedMax(); // Maximum speed of the car
		int aMin = car.getAccelerationMin(); // Maximum deceleration of the car
		int aMax = car.getAccelerationMax();
		IntVar vkParam = model.intVar("v_k", vk); // v_k: speed of the car
		IntVar vk1Var = model.intVar("v_k+1", 0, vMax); // v_k+1 to determine
		IntVar aVar = model.intVar("a", aMin, aMax); // acceleration in [aMin, aMax]

		// Update speed : v_k+1 = v_k + a * timeStep
		IntVar[] newSpeed = new IntVar[]{vk1Var, vkParam, aVar};
		int[] coefficient = new int[]{1, -1, -1*timeStep};
		
		
		// Booleans
		boolean xBet = (xMin < xk) && (xk <= xMax);
		boolean xInf = xk <= xMin;
		boolean xSup = xMax < xk && xk <= 0;
		boolean xOver = xk > 0;
		//boolean xNearTL = !(xk >= xTL - 2*unitScale && xk <= xTL);


		// CTLCi booleans
		BoolVar CTLC145Bool = model.boolVar("CTLC1, CTLC4 and CTLC5", xInf || xOver || trafficLight.isGreen());
        BoolVar CTLC2Bool = model.boolVar("CTLC2", xSup && trafficLight.isYellow());
        BoolVar CTLC3aBool = model.boolVar("CTLC3a", xBet && (trafficLight.isRed() || trafficLight.isYellow()));
        //BoolVar CTLC3bBool = model.boolVar("CTCL3b", xNearTL);
		

        // Speed limit constraint
		model.arithm(vk1Var, "<=", vSL).post();

		// Constraints CTLC1, CTLC4 and CTLC5
		model.ifThen(CTLC145Bool, model.arithm(vk1Var, ">=", vkParam));

		// Constraints CTLC2
		model.ifThen(CTLC2Bool, model.and(model.arithm(vk1Var, "=", vkParam)));

		// Constraints CTLC3
		BoolVar deceleration = model.boolVar("deceleration", car.getAcceleration() < 0);
		model.ifThen(model.and(deceleration, CTLC3aBool), model.arithm(aVar, ">=", car.getAcceleration()));
		model.ifThen(model.and(model.boolNotView(deceleration), CTLC3aBool),
				model.arithm(aVar, "=", (int) Math.floor((float)(-1*vk*vk)/(2*Math.abs(xk))))); // round down the acceleration to the lower integer

		model.scalar(newSpeed, coefficient, "=",0).post();

		// Resolution of the CSP
		Solver solver = model.getSolver();
		solver.setSearch(Search.inputOrderLBSearch(aVar)); // if the deceleration a_d = v�/(2*x) is impossible (v < a_d*timeStep),
														   // choose the lowest acceleration possible
		int[] liste = new int[5];
		if (solver.solve()){

			car.setPosition(xk + vk*timeStep); // update position
			System.out.println("Position : " + car.getPosition() + unitstr);

			car.setSpeed(vk1Var.getValue()); // update speed
			System.out.println("Speed : " + car.getSpeed() + unitstr + "/s");

			car.setAcceleration(aVar.getValue()); // update acceleration
			System.out.println("Acceleration : " + car.getAcceleration() + unitstr + "/s�");
			liste[0] = car.getPosition();
			liste[1] = car.getSpeed();
			if(trafficLight.isRed()) {liste[2] = 1;}else {liste[2] = 0;} 
			if(trafficLight.isYellow()) {liste[3] = 1;}else {liste[3] = 0;} 
			if(trafficLight.isGreen()) {liste[4] = 1;}else {liste[4] = 0;} 
			return liste;
		}

		else {
			System.out.println("Problem with the resolution");
			return liste;
		}
	}
	
	public int[] OneLap(int xMin, int vSL){
		// xMin: Position from which the traffic light is considered
		// vSL: Speed Limit
		// tr: Reaction time

		xMin *= unitScale;
		vSL *= unitScale;

		// Creation of a new model
		Model model = new Model("Simulation");

		trafficLight.updateLights();
		//trafficLight.updateLightsIHM(Lights);
		// Get car parameters
		int xk = car.getPosition(); // Position of the car
		int xTL = trafficLight.getPosition(); // Position of the traffic light
		int xMax = (xTL*unitScale + car.stopDistance(1)); // Last position where the car can stop

		int vk = car.getSpeed(); // Speed of the car
		int vMax = car.getSpeedMax(); // Maximum speed of the car
		int aMin = car.getAccelerationMin(); // Maximum deceleration of the car
		int aMax = car.getAccelerationMax();
		IntVar vkParam = model.intVar("v_k", vk); // v_k: speed of the car
		IntVar vk1Var = model.intVar("v_k+1", 0, vMax); // v_k+1 to determine
		IntVar aVar = model.intVar("a", aMin, aMax); // acceleration in [aMin, aMax]

		// Update speed : v_k+1 = v_k + a * timeStep
		IntVar[] newSpeed = new IntVar[]{vk1Var, vkParam, aVar};
		int[] coefficient = new int[]{1, -1, -1*timeStep};
		
		
		// Booleans
		boolean xBet = (xMin < xk) && (xk <= xMax);
		boolean xInf = xk <= xMin;
		boolean xSup = xMax < xk && xk <= 0;
		boolean xOver = xk > 0;
		//boolean xNearTL = !(xk >= xTL - 2*unitScale && xk <= xTL);


		// CTLCi booleans
		BoolVar CTLC145Bool = model.boolVar("CTLC1, CTLC4 and CTLC5", xInf || xOver || trafficLight.isGreen());
        BoolVar CTLC2Bool = model.boolVar("CTLC2", xSup && trafficLight.isYellow());
        BoolVar CTLC3aBool = model.boolVar("CTLC3a", xBet && (trafficLight.isRed() || trafficLight.isYellow()));
        //BoolVar CTLC3bBool = model.boolVar("CTCL3b", xNearTL);
		

        // Speed limit constraint
		model.arithm(vk1Var, "<=", vSL).post();

		// Constraints CTLC1, CTLC4 and CTLC5
		model.ifThen(CTLC145Bool, model.arithm(vk1Var, ">=", vkParam));

		// Constraints CTLC2
		model.ifThen(CTLC2Bool, model.and(model.arithm(vk1Var, "=", vkParam)));

		// Constraints CTLC3
		BoolVar deceleration = model.boolVar("deceleration", car.getAcceleration() < 0);
		model.ifThen(model.and(deceleration, CTLC3aBool), model.arithm(aVar, ">=", car.getAcceleration()));
		model.ifThen(model.and(model.boolNotView(deceleration), CTLC3aBool),
				model.arithm(aVar, "=", (int) Math.floor((float)(-1*vk*vk)/(2*Math.abs(xk))))); // round down the acceleration to the lower integer

		model.scalar(newSpeed, coefficient, "=",0).post();

		// Resolution of the CSP
		Solver solver = model.getSolver();
		solver.setSearch(Search.inputOrderLBSearch(aVar)); // if the deceleration a_d = v�/(2*x) is impossible (v < a_d*timeStep),
														   // choose the lowest acceleration possible
		int[] liste = new int[5];
		if (solver.solve()){

			car.setPosition(xk + vk*timeStep); // update position
			System.out.println("Position : " + car.getPosition() + unitstr);

			car.setSpeed(vk1Var.getValue()); // update speed
			System.out.println("Speed : " + car.getSpeed() + unitstr + "/s");

			car.setAcceleration(aVar.getValue()); // update acceleration
			System.out.println("Acceleration : " + car.getAcceleration() + unitstr + "/s�");
			liste[0] = car.getPosition();
			liste[1] = car.getSpeed();
			if(trafficLight.isRed()) {liste[2] = 1;}else {liste[2] = 0;} 
			if(trafficLight.isYellow()) {liste[3] = 1;}else {liste[3] = 0;} 
			if(trafficLight.isGreen()) {liste[4] = 1;}else {liste[4] = 0;} 
			return liste;
		}

		else {
			System.out.println("Problem with the resolution");
			return liste;
		}
	}

}
