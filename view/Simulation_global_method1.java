package view;

import Environement.TrafficLight;
import Environement.Car;
import Environement.Car_Global;
import Environement.Car_test;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.expression.discrete.relational.ReExpression;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;



public class Simulation_global_method1 {

	private int timeStep;
	private int unitScale;
	private String unitstr;
	public Car_test car;
	private TrafficLight trafficLight;


	public Simulation_global_method1(int h, int unit, int[] initTL1, boolean[] initTL2, int[] initCar){

		this.timeStep = h;
		this.unitScale = unit;
		Model model = new Model("Test");
										
		this.car = new Car_test(model,initCar[0]*unit, initCar[1]*unit, initCar[2]*unit, initCar[3]*unit, initCar[4]*unit, initCar[5]*unit);
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

	public void Simulation(int xMin, int xStop, int vSL){

		xMin *= unitScale;
		xStop *= unitScale;
		vSL *= unitScale;

		//int N = abs((x0 - xStop)*unitScale);
		int N = 14;

		Model model = new Model("Simulation");
		IntVar Nspeed = model.intVar("N speed", 0, N);
		
		int xTL = trafficLight.getPosition(); // Traffic Light position
		IntVar xTLParam = model.intVar("xTL", xTL);

		int aMax = car.getAccelerationMax();
 		int aMin = car.getAccelerationMin();

		IntVar[] xVar = model.intVarArray("position", N, -3000, xStop); // array for position values
		IntVar[] vVar = model.intVarArray("speed", N, 0, car.getSpeedMax()); // array for speed values
		IntVar[] aVar = model.intVarArray("acceleration", N, aMin, aMax); // array for acceleration values

		IntVar[] xMax = model.intVarArray("xMax", N, -3000, xTL); // array for xMax values

		IntVar[] squareDiv = model.intVarArray("v²/(2amin)", N, car.getSpeedMax()*car.getSpeedMax()/(2*aMin), 0);
		
		// BoolVar array for the state of the car
		BoolVar[] xInf = model.boolVarArray(N); // xInf: xk < xMin
		BoolVar[] xOver = model.boolVarArray(N); // xOver: xk > xTL
		BoolVar[] xSup = model.boolVarArray(N); // xSup: xMax < xk <= xTL
		BoolVar[] xBet = model.boolVarArray(N); // xBet: xMin <= xk <= xMax
		BoolVar[] xNearTL = model.boolVarArray(N); // xNearTL: (xk >= (xTL - 2*unitScale) && xk <= xTL) or xk >=0 && xk<10*unitScale
		BoolVar[] xStopNow = model.boolVarArray(N); 
		
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

		BoolVar[] AccPos = model.boolVarArray(N);
		
		xVar[0].eq(car.getPosition()).post(); // Initialisation of the position
		vVar[0].eq(car.getSpeed()).post(); // Initialisation of the speed
		aVar[0].eq(car.getAcceleration()).post(); // Initialisation of the acceleration

		for (int i = 0; i <= N - 2; i++) {

			trafficLight.updateLights();

			// boolean for the traffic light
			isGreen[i] = model.boolVar("TL is Green", trafficLight.isGreen());
			isYellow[i] = model.boolVar("TL is Yellow", trafficLight.isYellow());
			isRed[i] = model.boolVar("TL is Red", trafficLight.isRed());
			isDegraded[i] = model.boolVar("TL is Degraded", trafficLight.isDegraded());

			// Computation of xMax
			IntVar stopDistance = car.stopDistance(model, vVar[i], 1);
			(xTLParam.sub(stopDistance)).eq(xMax[i]).post(); // xTL - stopDistance = xMax

			// v_k+1 = vk + a*h
			(vVar[i].add(aVar[i].mul(timeStep))).eq(vVar[i+1]).post();

			// x_k+1 = x_k + v_k*h
			(xVar[i].add(vVar[i].mul(timeStep))).eq(xVar[i+1]).post();

			// Speed limit constraint
			vVar[i].le(vSL).post();

			// Computation of x values
			xInf[i] = xVar[i].lt(xMin).boolVar(); // xInf: xk < xMin
			xOver[i] = xVar[i].gt(xTL).boolVar(); // xOver: xk > xTL
			xSup[i] = xVar[i].gt(xMax[i]).and(xVar[i].le(xTL)).boolVar(); // xSup: xMax < xk <= xTL
			xBet[i] = (xVar[i].le(xMax[i]).and(xVar[i].ge(xMin))).boolVar(); // xBet: xMin <= xk <= xMax
			xNearTL[i]= (xVar[i].ge(xTL - 2*unitScale).and(xVar[i].le(xTL))).or(xVar[i].ge(0).and(xVar[i].le(10*unitScale))).boolVar();

			// Constraints CTLC1, CTLC4, CTLC5 and CTLC6
			CTLC1456[i] = xInf[i].or(xOver[i], isGreen[i], xSup[i].and(isRed[i])).boolVar();
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
			CTLC2[i].imp(vVar[i+1].eq(vVar[i])).post(); // CTLC2 => v_k+1 = v_k

			// Constraint CTLC3
			xStopNow[i] = xVar[i].abs().lt(10*unitScale).and(isRed[i].or(isYellow[i])).boolVar();
			BoolVar isRedOrYellow = model.boolVar("TL is red or yellow", trafficLight.isRed() || trafficLight.isYellow());
			CTLC3[i] = model.and(xBet[i], isRedOrYellow).reify();

			squareDiv[i].eq(vVar[i].sqr().div(2*aMin));
			
			CTLC3[i].imp(vVar[i+1].lt(vVar[i]).and(xVar[i+1].add(vVar[i]).le(squareDiv[i]), aVar[i].gt(aMin+20))).post();
			//CTLC3[i].imp(vVar[i+1].lt(vVar[i]).and(xVar[i+1].lt(xMax[i+1]))).post();
			
			
			//Degraded conditions 
			model.ifThen(CTLDC2[i],
					model.and(model.arithm(vVar[i+1], "<=", vVar[i]),
					model.arithm(xVar[i+1], "<=", xMax[i+1])));

			
			if (i!=0){
				CTLDC3[i].and(aVar[i-1].lt(0)).imp(aVar[i].eq(0)).post();
				CTLDC3[i].and(aVar[i-1].ge(0)).imp(aVar[i].eq(aVar[i-1].add(1))).post();
			}
			else {
				CTLDC3[i].and(car.getAcceleration().lt(0)).imp(aVar[i].eq(0)).post();
				CTLDC3[i].and(car.getAcceleration().ge(0)).imp(aVar[i].eq(car.getAcceleration().add(1))).post();
			}
			//model.ifThen(CTLDC3[i], model.arithm(aVar[i], "=", Math.max(0,car.getAcceleration().add(1))));
			
			model.count(0, vVar, Nspeed).post();
			xStopNow[i].imp(Nspeed.ge(1)).post();
			//xStopNow[i].not().imp(Nspeed.eq(0).and(vVar[i].gt(5*unitScale))).post();
		}

		Solver solver = model.getSolver();
        solver.setSearch(Search.inputOrderUBSearch(aVar));

		// Product all the solutions

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
				System.out.println("xMin = " + xMin + unitstr);
				System.out.println(xMax[i] + unitstr);
				System.out.println(squareDiv[i] + unitstr);
				System.out.println(xVar[i] + unitstr);
				System.out.println(vVar[i] + unitstr + "/s");
				System.out.println(aVar[i] + unitstr + "/s²\n");

				
			}
			
			
			System.out.println(" ----- Final Position & Speed ----- \n");
			System.out.println(xVar[N-1] + unitstr);
			System.out.println(vVar[N-1] + unitstr + "/s");
		}

		else {
			System.out.println("Problem with the resolution");
		}

	}
}

