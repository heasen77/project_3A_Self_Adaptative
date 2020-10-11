package view;

import Environement.TrafficLight;
import Environement.TrafficLight_Global;
import Environement.Car;
import Environement.Car_test;

import java.util.ArrayList;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;



public class Simulation_global_method2 {

	private int timeStep;
	private int unitScale;
	private String unitstr;
	private Car_test car;
	private TrafficLight trafficLight;
	private TrafficLight_Global trafficLightGlobal;
	private boolean[] Lights;


	public Simulation_global_method2(int h, int unit, int[] initTL1, boolean[] initTL2, int[] initCar){

		this.timeStep = h;
		this.unitScale = unit;
		Model model = new Model("Test");
		this.car = new Car_test(model,initCar[0]*unit, initCar[1]*unit, initCar[2]*unit, initCar[3]*unit, initCar[4]*unit, initCar[5]*unit);
		this.trafficLightGlobal = new TrafficLight_Global(initTL1[0]*unit, initTL2[0], initTL2[1], initTL2[2], initTL2[3]);

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
		
		this.Lights = new boolean[]{initTL2[1], initTL2[2], initTL2[3]};
		
	}

	public ArrayList<ArrayList<Integer>> Simulation(int xMin, int xStop, int vSL){

		xMin *= unitScale;
		xStop *= unitScale;
		vSL *= unitScale;

		//int N = abs((x0 - xStop)*unitScale);
		int N = 15;

		Model model = new Model("Simulation");
		IntVar Nspeed = model.intVar("N speed", 0, N);

		int xTL = trafficLightGlobal.getPosition(); // Traffic Light position
		IntVar xTLParam = model.intVar("xTL", xTL);
		
		
		int aMax = car.getAccelerationMax();
 		int aMin = car.getAccelerationMin();

		IntVar[] xVar = model.intVarArray("position", N, -3000, xStop); // array for position values
		IntVar[] vVar = model.intVarArray("speed", N, 0, car.getSpeedMax()); // array for speed values
		IntVar[] aVar = model.intVarArray("acceleration", N, aMin, aMax); // array for acceleration values

		IntVar[] xMax = model.intVarArray("xMax", N, -3000, xTL); // array for xMax values

		// BoolVar array for the state of the car
		BoolVar[] xInf = model.boolVarArray(N); // xInf: xk < xMin
		BoolVar[] xOver = model.boolVarArray(N); // xOver: xk > xTL
		BoolVar[] xSup = model.boolVarArray(N); // xSup: xMax < xk <= xTL
		BoolVar[] xBet = model.boolVarArray(N); // xBet: xMin <= xk <= xMax
		BoolVar[] xNearTL = model.boolVarArray(N); // xNearTL: (xk >= (xTL - 2*unitScale) && xk <= xTL) or xk >=0 && xk<10*unitScale
		BoolVar[] xStopNow = model.boolVarArray(N); //xStopNow: xk< 10*unitScale & TL Red or Yellow

		// BoolVar array for the color of the TL
		BoolVar[] isGreen = model.boolVarArray(N); // BoolVar array if the TL is Green
		BoolVar[] isYellow = model.boolVarArray(N); // BoolVar array if the TL is Yellow
		BoolVar[] isRed = model.boolVarArray(N); // BoolVar array if the TL is Red
		BoolVar[] isDegraded = model.boolVarArray(N); // BoolVar array if the TL is Red

		BoolVar[] CTLC1456 = model.boolVarArray(N); // BoolVar array for CTLC1, CTLC4, CTLC5 and CTLC6
		BoolVar[] CTLC2 = model.boolVarArray(N); // BoolVar array for CTLC2
		BoolVar[] CTLC3 = model.boolVarArray(N); // BoolVar array for CTLC3
		BoolVar[] CTLDC2 = model.boolVarArray(N); // BoolVar array for CTLC2
		BoolVar[] CTLDC3 = model.boolVarArray(N); // BoolVar array for CTLC3
		
		BoolVar[] Deceleration = model.boolVarArray(N); //BoolVar array for deceleration
		BoolVar[] AccPos = model.boolVarArray(N); // BoolVar array if the acceleration is positive
		
		BoolVar[] BrakeMore =  model.boolVarArray(N);

		xVar[0].eq(car.getPosition()).post(); // Initialisation of the position
		vVar[0].eq(car.getSpeed()).post(); // Initialisation of the speed
		aVar[0].eq(car.getAcceleration()).post(); // Initialisation of the acceleration

		for (int i = 0; i <= N - 2; i++) {

			//trafficLight.updateLights();
			trafficLightGlobal.updateLightsIHM(Lights);

			// boolean for the traffic light
			isGreen[i] = model.boolVar("TL is Green", trafficLightGlobal.isGreen());
			isYellow[i] = model.boolVar("TL is Yellow", trafficLightGlobal.isYellow());
			isRed[i] = model.boolVar("TL is Red", trafficLightGlobal.isRed());
			isDegraded[i] = model.boolVar("TL is Degraded", trafficLightGlobal.isDegraded());

			// Computation of xMax
			IntVar stopDistance = car.stopDistance(model, vVar[i], 1);
			(xTLParam.sub(stopDistance)).eq(xMax[i]).post(); // xTL - stopDistance = xMax

			// v_k+1 = vk + a*h
			(vVar[i].add(aVar[i].mul(timeStep))).eq(vVar[i+1]).post();

			// x_k+1 = x_k + v_k*h
			(xVar[i].add(vVar[i].mul(timeStep))).eq(xVar[i+1]).post();

			// Speed limit constraint: v <= vSL
			vVar[i].le(vSL).post();

			// Computation of x values
			xInf[i] = xVar[i].lt(xMin).boolVar(); // xInf: xk < xMin
			xOver[i] = xVar[i].gt(xTL).boolVar(); // xOver: xk > xTL
			xSup[i] = xVar[i].gt(xMax[i]).and(xVar[i].le(xTL)).boolVar(); // xSup: xMax < xk <= xTL
			xBet[i] = (xVar[i].le(xMax[i]).and(xVar[i].ge(xMin))).boolVar(); // xBet: xMin <= xk <= xMax
			xNearTL[i]= (xVar[i].ge(xTL - 2*unitScale).and(xVar[i].le(xTL))).or(xVar[i].ge(0).and(xVar[i].le(10*unitScale))).boolVar();
			xStopNow[i] = xVar[i].lt(-10*unitScale).and(isRed[i].or(isYellow[i])).boolVar(); //xk< 10*unitScale & TL Red or Yellow
			
			// Constraints CTLC1, CTLC4, CTLC5 and CTLC6
			CTLC1456[i] = xInf[i].or(xOver[i], isGreen[i], xSup[i].and(isRed[i])).boolVar();
			//if aVar[i-1] >= 0: vk+1>=vk & 0<= ak <= ak-1 +4
			if (i!=0){
				AccPos[i] = aVar[i-1].ge(0).boolVar();
				CTLC1456[i].and(AccPos[i]).imp(vVar[i+1].ge(vVar[i]).and(aVar[i].le(aVar[i-1].add(4)), aVar[i].ge(0))).post();
			}
			else{
				AccPos[i] = car.getAcceleration().ge(0).boolVar();
				CTLC1456[i].and(AccPos[i]).imp(vVar[i+1].ge(vVar[i]).and(aVar[i].le(car.getAcceleration().add(4)), aVar[i].ge(0))).post();
			}
			CTLC1456[i].and(AccPos[i].not()).imp(vVar[i+1].ge(vVar[i]).and(aVar[i].eq(1))).post();
			//CTLC1456[i].imp(vVar[i+1].ge(vVar[i])).post(); // CTLC1456 => v_k+1 >= v_k
			
			
			CTLDC2[i] = model.and(xBet[i], model.boolNotView(xNearTL[i]), isDegraded[i]).reify();
			CTLDC3[i] = model.and(isDegraded[i], xNearTL[i]).reify();
			
			// Constraint CTLC2
			CTLC2[i] = model.and(xSup[i], isYellow[i]).reify();
			model.ifThen(CTLC2[i], model.arithm(vVar[i+1], "=", vVar[i]));

			// Constraint CTLC3
			BoolVar isRedOrYellow = model.boolVar("TL is red or yellow", trafficLightGlobal.isRed() || trafficLightGlobal.isYellow());
			CTLC3[i] = model.and(xBet[i], isRedOrYellow).reify();
			
			
			if (i!=0){
				Deceleration[i] = aVar[i-1].lt(0).boolVar(); // a<0
				
				//a <= ak-1
				CTLC3[i].and(Deceleration[i]).imp(aVar[i].le(aVar[i-1])).post();
				//(CTLC3[i].or(xStopNow[i])).and(Deceleration[i]).imp(aVar[i].le(aVar[i-1].add(-5))).post();
				
				CTLDC2[i].and(Deceleration[i]).imp(aVar[i].le(aVar[i-1])).post();
				
				CTLDC3[i].and(aVar[i-1].lt(0)).imp(aVar[i].eq(0)).post();
				CTLDC3[i].and(aVar[i-1].ge(0)).imp(aVar[i].eq(aVar[i-1].add(1))).post();
			}
			else {
				Deceleration[i] = car.getAcceleration().lt(0).boolVar();
				
				//a <= ak-1
				CTLC3[i].and(Deceleration[i]).imp(aVar[i].le(car.getAcceleration())).post();
				
				CTLDC2[i].and(Deceleration[i]).imp(aVar[i].le(car.getAcceleration())).post();
				
				CTLDC3[i].and(car.getAcceleration().lt(0)).imp(aVar[i].eq(0)).post();
				CTLDC3[i].and(car.getAcceleration().ge(0)).imp(aVar[i].eq(car.getAcceleration().add(1))).post();
			}
			
			// a = -(v²/ (2* |x|))
			CTLC3[i].and(Deceleration[i].not()).imp(aVar[i].eq(vVar[i].pow(2).div(xVar[i].abs().mul(2)).mul(-1).sub(1))).post();
			//Degraded conditions 
			CTLDC2[i].and(Deceleration[i].not()).imp(aVar[i].eq(vVar[i].pow(2).div(xVar[i].abs().mul(2)).mul(-1).sub(1))).post();
			

			//if xStopNow => vk=0 at least one time
			model.count(0, vVar, Nspeed).post();
			xStopNow[i].imp(Nspeed.ge(1)).post();
				
		}
		

		Solver solver = model.getSolver();
		
		// Product all the solutions
		
		// Product all the solutions
		ArrayList<ArrayList<Integer>> liste_all = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> poslist = new ArrayList<Integer>();
		ArrayList<Integer> vitlist = new ArrayList<Integer>();
		ArrayList<Integer> greenlist = new ArrayList<Integer>();
		ArrayList<Integer> redlist = new ArrayList<Integer>();
		ArrayList<Integer> yellowlist = new ArrayList<Integer>();
		ArrayList<Integer> Acces = new ArrayList<Integer>();
		
		
		if (solver.solve()){

			System.out.println("CSP solved\n");
			System.out.println(Nspeed);
			for (int i = 0; i <= N - 2 ; i++) {
				if (isGreen[i].getValue() == 1) {
					System.out.println("The traffic light is green !");
				}
				if (isYellow[i].getValue() == 1) {
					System.out.println("The traffic light is yellow !");
				}
				if (isRed[i].getValue() == 1) {
					System.out.println("The traffic light is red !");
				}
				if (isDegraded[i].getValue() == 1){
					System.out.println("The traffic light is degraded !");
				}
				if (CTLC1456[i].getValue() == 1) {
					System.out.println("CTLC1, CTLC4, CTLC5 or CTLC6");
				}
				if (CTLC2[i].getValue() == 1) {
					System.out.println("CTLC2");
				}
				if (CTLC3[i].getValue() == 1) {
					System.out.println("CTLC3");
				}
				if (CTLDC2[i].getValue() == 1) {
					System.out.println("CTLDC2");
				}
				if (CTLDC3[i].getValue() == 1) {
					System.out.println("CTLDC3");
				}
				if (BrakeMore[i].getValue() == 1){
					System.out.println("Brake More");
				}
				if (xStopNow[i].getValue() == 1){
					System.out.println("Stop Now");
				}
				
				System.out.println("aMin = " + car.getAccelerationMin());
				System.out.println("xMin = " + xMin + unitstr);
				System.out.println(xMax[i] + unitstr);
				System.out.println(xVar[i] + unitstr);
				System.out.println(vVar[i] + unitstr + "/s");
				System.out.println(aVar[i] + unitstr + "/s²\n");
				
				poslist.add(xVar[i].getValue());
				vitlist.add(vVar[i].getValue());
				greenlist.add(isGreen[i].getValue());
				yellowlist.add(isYellow[i].getValue());
				redlist.add(isRed[i].getValue());
				Acces.add(aVar[i].getValue());
				

			}
			System.out.println(" ----- Final Position & Speed ----- \n");
			System.out.println(xVar[N-1] + unitstr);
			System.out.println(vVar[N-1] + unitstr + "/s");
			
			liste_all.add(poslist);
			liste_all.add(vitlist);	
			liste_all.add(greenlist);
			liste_all.add(yellowlist);
			liste_all.add(redlist);
			liste_all.add(Acces);
			
		}

		else {
			System.out.println("Problem with the resolution");
		}
		return liste_all;
	}
}