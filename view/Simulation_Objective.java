package view;

import Environement.TrafficLight;
import Environement.Car;
import Environement.Car_Global;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.objective.ParetoOptimizer;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.max;
import static java.lang.Math.abs;


public class Simulation_Objective{

	private int timeStep;
	private int unitScale;
	private String unitstr;
	private Car_Global car;
	private TrafficLight trafficLight;


	public Simulation_Objective(int h, int unit, int[] initTL1, boolean[] initTL2, int[] initCar){

		this.timeStep = h;
		this.unitScale = unit;
		this.car = new Car_Global(initCar[0]*unit, initCar[1]*unit, initCar[2]*unit, initCar[3]*unit, initCar[4]*unit, initCar[5]*unit);
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


	/* Return the sum of the squared values of the speed array */
	public IntVar arraySquareSum(IntVar[] array, int arrayLength, IntVar sum, Model model){

		IntVar[] arraySquare = model.intVarArray("arraySquare", arrayLength, 0, car.getSpeedMax()*car.getSpeedMax()); // Array to store the squared values

		for (int i = 0; i <= arrayLength - 1; i++){
			array[i].sqr().eq(arraySquare[i]).post(); // Each value of the array is squared and stored in arraySquare
		}

		model.sum(arraySquare, "=", sum).post(); // All the values are summed

		return sum;
	}

	/* Return the opposite of the sum of the speed array */
	public IntVar negativeArraySum(IntVar[] array, int arrayLength, IntVar sum, Model model){

		IntVar[] negativeArray = model.intVarArray("negativeArray", arrayLength, - car.getSpeedMax(), 0);

		for (int i = 0; i <= arrayLength - 1; i++){
			array[i].neg().eq(negativeArray[i]).post();
		}

		model.sum(negativeArray, "=", sum).post();

		return sum;
	}

	/* Return the sum of the delta values between each couple of consecutive values of the acceleration array */
	public IntVar absArraySum(IntVar[] array, int arrayLength, IntVar sum, Model model){

		IntVar[] absArray = model.intVarArray("negativeArray", arrayLength, car.getAccelerationMin(), car.getAccelerationMax());

		for (int i = 0; i <= arrayLength - 1; i++){
			array[i].abs().eq(absArray[i]).post();
		}

		model.sum(absArray, "=", sum).post();

		return sum;
	}

	/* Return the sum of the delta values between each couple of consecutive values of the acceleration array */
	public IntVar deltaArrayMax(IntVar[] array, int arrayLength, IntVar max, Model model){

		IntVar[] deltaArray = model.intVarArray("deltaArray", arrayLength - 1, 0, car.getAccelerationMax() - car.getAccelerationMin());

		for (int i = 0; i <= arrayLength - 2; i++){
			deltaArray[i].eq(array[i+1].sub(array[i]).abs()).post();
		}

		model.max(max, deltaArray).post();

		return max;
	}


	/* Simulate the behaviour of the car according to the initial conditions */
	public int[][][] Simulation(int xMin, int xStop, int vSL) {

		xMin *= unitScale;
		xStop *= unitScale;
		vSL *= unitScale;

		int N = 15;

		Model model = new Model("Simulation");

		int xTL = trafficLight.getPosition(); // Traffic Light position
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

			trafficLight.updateLights();

			// boolean for the traffic light
			isGreen[i] = model.boolVar("TL is Green", trafficLight.isGreen());
			isYellow[i] = model.boolVar("TL is Yellow", trafficLight.isYellow());
			isRed[i] = model.boolVar("TL is Red", trafficLight.isRed());

			// Computation of xMax
			IntVar stopDistance = car.stopDistance(model, vVar[i], 1);
			(xTLParam.sub(stopDistance)).eq(xMax[i]).post(); // xTL - stopDistance = xMax


			// Computation of x values
			xInf[i] = xVar[i].lt(xMin).boolVar(); // xInf: xk < xMin
			xOver[i] = xVar[i].gt(xTL).boolVar(); // xOver: xk > xTL
			xSup[i] = xVar[i].gt(xMax[i]).and(xVar[i].le(xTL)).boolVar(); // xSup: xMax < xk <= xTL
			xBet[i] = (xVar[i].le(xMax[i]).and(xVar[i].ge(xMin))).boolVar(); // xBet: xMin <= xk <= xMax

			// v_k+1 = vk + a*h
			(vVar[i].add(aVar[i].mul(timeStep))).eq(vVar[i + 1]).post();

			// x_k+1 = x_k + v_k*h
			(xVar[i].add(vVar[i].mul(timeStep))).eq(xVar[i + 1]).post();

			// Speed limit constraint
			vVar[i].le(vSL).post();

			// Constraints CTLC1, CTLC4, CTLC5 and CTLC6
			CTLC6[i] = model.and(xSup[i], isRed[i]).reify(); // CTLC6 <=> xSup and TL is red
			CTLC145[i] = model.or(xInf[i], xOver[i], isGreen[i]).reify();
			model.ifThen(model.or(CTLC145[i], CTLC6[i]), model.arithm(vVar[i + 1], ">=", vVar[i]));

			// Constraint CTLC2
			CTLC2[i] = model.and(xSup[i], isYellow[i]).reify();
			model.ifThen(CTLC2[i], model.arithm(vVar[i + 1], "=", vVar[i]));

			// Constraint CTLC3
			BoolVar isRedOrYellow = model.boolVar("TL is red or yellow", trafficLight.isRed() || trafficLight.isYellow());
			CTLC3[i] = model.and(xBet[i], isRedOrYellow).reify();

			model.ifThen(CTLC3[i],
					model.and(model.arithm(vVar[i + 1], "<=", vVar[i]),
							model.arithm(xVar[i + 1], "<=", xMax[i + 1]))); // the bug was here [i] --> [i+1]
		}

		IntVar vObjective = model.intVar("vObjective", -N * car.getSpeedMax(), 0);
		vObjective = negativeArraySum(vVar, N, vObjective, model);

		IntVar aDelta = model.intVar("aDelta", 0, aMax - aMin);
		aDelta = deltaArrayMax(aVar, N, aDelta, model);

		IntVar aMinimize = model.intVar("aMininimize", 0, max(abs(aMin), aMax));
		model.min(aMinimize, aVar).post();

		IntVar aObjective = model.intVar("aObjective", 0, max(abs(aMin), aMax) + aMax - aMin);
		aMinimize.add(aDelta).eq(aObjective).post();

		ParetoOptimizer po = new ParetoOptimizer(Model.MINIMIZE, new IntVar[]{vObjective, aObjective});
		Solver solver = model.getSolver();
		solver.plugMonitor(po);

		
		while (solver.solve());

		List<Solution> paretoFront = po.getParetoFront();
		System.out.println("The pareto front has " + paretoFront.size() + " solutions : ");
		int[][][] liste_all = new int[paretoFront.size()][6][1000];
		int compteur = 0;
		
		for (Solution s : paretoFront) {
			System.out.println("-------------------------\nObjectives value:\nvObjective = " + s.getIntVal(vObjective) +" and aObjective = " + s.getIntVal(aObjective) +"\n");
			
			for (int i = 0; i <= N - 2; i++) {

				if (isGreen[i].getValue() == 1) {
					System.out.println("The traffic light is green !");
				}
				if (isYellow[i].getValue() == 1) {
					System.out.println("The traffic light is yellow !");
				}
				if (isRed[i].getValue() == 1) {
					System.out.println("The traffic light is red !");
				}
				if (CTLC6[i].getValue() == 1) {
					System.out.println("CTLC6");
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
				System.out.println(liste_all[compteur][2][i]);
				liste_all[compteur][2][i] = isGreen[i].getValue();
				liste_all[compteur][4][i] = isRed[i].getValue();
				liste_all[compteur][3][i] = isYellow[i].getValue();
				
				liste_all[compteur][0][i] = s.getIntVal(xVar[i]);
				liste_all[compteur][1][i] = s.getIntVal(vVar[i]);
				liste_all[compteur][5][i] = s.getIntVal(aVar[i]);

				System.out.println("xMin = " + xMin + unitstr);
				System.out.println("xMax = " + s.getIntVal(xMax[i]) + unitstr);
				System.out.println("x[" + i + "] = " + s.getIntVal(xVar[i]) + unitstr);
				System.out.println("v[" + i + "] = " + s.getIntVal(vVar[i]) + unitstr + "/s");
				System.out.println("a[" + i + "] = " + s.getIntVal(aVar[i]) + unitstr + "/s²\n");
				System.out.println("-------------------------\n");
			}
			compteur++;
		}
		
		return liste_all;
	}
}
