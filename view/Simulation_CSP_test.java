package view;

import Environement.TrafficLight;
import Environement.TrafficLight_Global;
import Environement.Car_Global;

import java.util.ArrayList;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;



public class Simulation_CSP_test {

	private int timeStep, N;
	private int unitScale;
	private String unitstr;
	private Car_Global car;
	private TrafficLight_Global trafficLightGlobal;
	private boolean[] Lights;
	public Simulation_CSP_test(int h, int unit, int[] initTL1, boolean[] initTL2, int[] initCar, int N){

		this.timeStep = h;
		this.unitScale = unit;
		this.car = new Car_Global(initCar[0]*unit, initCar[1]*unit, initCar[2]*unit, initCar[3]*unit, initCar[4]*unit, initCar[5]*unit);
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
		this.N = N;
		this.Lights = new boolean[]{initTL2[1], initTL2[2], initTL2[3]};
	}

	public ArrayList<ArrayList<Integer>> Simulation(int xMin, int xStop, int vSL){

		xMin *= unitScale;
		xStop *= unitScale;
		vSL *= unitScale;

		//int N = abs((x0 - xStop)*unitScale);
		int N = 15;
		//int N = this.N;
		
		Model model = new Model("Simulation");

		int xTL = trafficLightGlobal.getPosition(); // Traffic Light position
		IntVar xTLParam = model.intVar("xTL", xTL);

		int aMax = car.getAccelerationMax();
 		int aMin = car.getAccelerationMin();

		IntVar[] xVar = model.intVarArray("position", N, -3000, xStop); // array for position values
		IntVar[] vVar = model.intVarArray("speed", N, 0, car.getSpeedMax()); // array for speed values
		IntVar[] aVar = model.intVarArray("acceleration", N, aMin, aMax); // array for acceleration values

		IntVar[] xMax = model.intVarArray("xMax", N, -3000, xTL); // array for xMax values

		// BoolVar array for the state of the car
		BoolVar[] xInf = model.boolVarArray("xInf", N); // xInf: xk < xMin
		BoolVar[] xOver = model.boolVarArray("xOver", N); // xOver: xk > xTL
		BoolVar[] xSup = model.boolVarArray("xSup", N); // xSup: xMax < xk <= xTL
		BoolVar[] xBet = model.boolVarArray("xBet", N); // xBet: xMin <= xk <= xMax

		// BoolVar array for the color of the TL
		BoolVar[] isGreen = model.boolVarArray(N); // BoolVar array if the TL is Green
		BoolVar[] isYellow = model.boolVarArray(N); // BoolVar array if the TL is Yellow
		BoolVar[] isRed = model.boolVarArray(N); // BoolVar array if the TL is Red

		BoolVar[] CTLC145 = model.boolVarArray(N); // BoolVar array for CTLC1, CTLC4 and CTLC5
		BoolVar[] CTLC2 = model.boolVarArray(N); // BoolVar array for CTLC2
		BoolVar[] CTLC3 = model.boolVarArray(N); // BoolVar array for CTLC3
		BoolVar[] CTLC6 = model.boolVarArray(N); // BoolVar array for CTLC6

		xVar[0].eq(car.getPosition()).post(); // Initialisation of the position
		vVar[0].eq(car.getSpeed()).post(); // Initialisation of the speed
		aVar[0].eq(car.getAcceleration()).post(); // Initialisation of the acceleration


		for (int i = 0; i <= N - 2; i++) {

			trafficLightGlobal.updateLightsIHM(Lights);

			// boolean for the traffic light
			isGreen[i] = model.boolVar("TL is Green", trafficLightGlobal.isGreen());
			isYellow[i] = model.boolVar("TL is Yellow", trafficLightGlobal.isYellow());
			isRed[i] = model.boolVar("TL is Red", trafficLightGlobal.isRed());

			// Computation of xMax
			IntVar stopDistance = car.stopDistance(model, vVar[i], 1);
			(xTLParam.sub(stopDistance)).eq(xMax[i]).post(); // xTL - stopDistance = xMax


			// Computation of x values
			xInf[i] = xVar[i].lt(xMin).boolVar(); // xInf: xk < xMin
			xOver[i] = xVar[i].gt(xTL).boolVar(); // xOver: xk > xTL
			xSup[i] = xVar[i].gt(xMax[i]).and(xVar[i].le(xTL)).boolVar(); // xSup: xMax < xk <= xTL
			xBet[i] = (xVar[i].le(xMax[i]).and(xVar[i].ge(xMin))).boolVar(); // xBet: xMin <= xk <= xMax

			// v_k+1 = vk + a*h
			(vVar[i].add(aVar[i].mul(timeStep))).eq(vVar[i+1]).post();

			// x_k+1 = x_k + v_k*h
			(xVar[i].add(vVar[i].mul(timeStep))).eq(xVar[i+1]).post();

			// Speed limit constraint
			vVar[i].le(vSL).post();

			// Constraints CTLC1, CTLC4, CTLC5 and CTLC6
			CTLC6[i] = model.and(xSup[i], isRed[i]).reify(); // CTLC6 <=> xSup and TL is red
			CTLC145[i] = model.or(xInf[i], xOver[i], isGreen[i]).reify();
			model.ifThen(model.or(CTLC145[i], CTLC6[i]), model.arithm(vVar[i+1], ">=", vVar[i]));

			// Constraint CTLC2
			CTLC2[i] = model.and(xSup[i], isYellow[i]).reify();
			model.ifThen(CTLC2[i], model.arithm(vVar[i+1], "=", vVar[i]));

			// Constraint CTLC3
			BoolVar isRedOrYellow = model.boolVar("TL is red or yellow", trafficLightGlobal.isRed() || trafficLightGlobal.isYellow());
			CTLC3[i] = model.and(xBet[i], isRedOrYellow).reify();

			model.ifThen(CTLC3[i],
					model.and(model.arithm(vVar[i+1], "<=", vVar[i]),
					model.arithm(xVar[i+1], "<=", xMax[i+1]))); // the bug was here [i] --> [i+1]
		}

		//xVar[maxTime].ge(500).post(); // The car must be at the TL before maxTime

		Solver solver = model.getSolver();
		solver.setSearch(Search.inputOrderUBSearch(aVar));

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
				if (CTLC145[i].getValue() == 1) {
					System.out.println("CTLC1, CTLC4 or CTLC5");
				}
				if (CTLC2[i].getValue() == 1) {
					System.out.println("CTLC2");
				}
				if (CTLC3[i].getValue() == 1) {
					System.out.println("CTLC3");
				}
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
