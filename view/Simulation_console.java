package view;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import Environement.Environement;

public class Simulation_console {
	
	
	Environement environ;
	
	
	public Simulation_console(Environement environ){
		
		this.environ = environ;
		
	}
	
	public static int StopDistance(int v0, int aMin){
		//Calculation of the stopping distance in meters
		return (int)(-((v0*v0)/aMin));
	}
	
	public static int simu(int timeStep, int xk, int xmin, int v, int a, int vmax, String trafficLight, int vsl, int unitScale){
		double t_r = 1;
		double mu = 0.7;
		
		int xmax = -StopDistance(v, -1*a*unitScale); //xmax: stop distance
		
		System.out.println("Stop Distance = " + xmax + " dm");
		
		Model model = new Model("Traffic_Light");
		
		IntVar Vk = model.intVar("Vk", v);
		IntVar Vk1 = model.intVar("Vk+1", 0, vmax); //Max 50km/h => Environ 1389 cm/s
		IntVar A = model.intVar("A", -a, a); //Acceleration between amin and amax km/h
		
		IntVar[] newSpeed = new IntVar[]{Vk1, Vk, A};
		
		//Vk+1 = Vk + A * h 
		int[] coeffs1 = new int[]{1,-1, -1*timeStep*unitScale};
		
		
		//Boolean
		BoolVar xInfxMinBool = model.boolVar("xInfxMin", xk <= xmin); //boolean: if x <= xMin

		boolean xBetween = (xmin < xk) && (xk <= xmax);
	
		BoolVar xPastTL = model.boolVar("xPastTL", xk > 0); // boolean: if x > 0
		BoolVar xNearTL = model.boolVar("xNearTL",  -1*10 <= xk && xk <= 0);
		
		// Check v_k+1 is not too fast
        IntVar v1Square = model.intVar("v²", 0, vmax*vmax);
        IntVar dReactCoef = model.intVar("reaction distance * coef", 0, (int) (vmax*t_r*(mu*9.81*2)));
        IntVar coefParam = model.intVar("coefficient", (int) (mu*9.81*2));

        model.arithm(Vk1, "*", Vk1, "=", v1Square).post();
        model.arithm(Vk1, "*", coefParam, "=", dReactCoef).post();
        
		// CTCLi booleans
        BoolVar CTCL2Bool = model.boolVar("CTCL2", (trafficLight == "orange") && (xmax < xk) && (xk < 0));
        BoolVar CTLC3Bool = model.boolVar("CTCL3", ((trafficLight == "orange")||(trafficLight == "red"))&&xBetween);
        BoolVar CTCL4Bool = model.boolVar("CTCL4",  (trafficLight == "green") && xBetween);
		
        
        //Speed Limit constraint
        model.arithm(Vk1,  "<=", vsl).post();
        
        // Constraints appearing in several cases
        model.ifThen(model.or(CTCL4Bool, xInfxMinBool, xPastTL), model.arithm(Vk1, ">=", v));
      
        //CTLC1 if xk<xmin : vk+1 = vk + a*timeStep
        if (xk <= xmin){
        	System.out.println("CTLC1");
        }
    	model.ifThen(xInfxMinBool, model.scalar(newSpeed, coeffs1, "=",0));
    	
    	//CTLC2 if orange and xk>xmax: vk+1 = vk
    	if ((trafficLight == "orange") && (xmax < xk) && (xk < 0)){
        	System.out.println("CTLC2");
        }
    	model.ifThen(CTCL2Bool, model.and(model.arithm(Vk1, "=", v),model.scalar(newSpeed, coeffs1, "=",0)));
    	
    	//CTLC3 (orange or red) and (xmin < xk < xmax): vk+1 = vk + a(deceleration)*timeStep
    	if (((trafficLight == "orange")||(trafficLight == "red"))&&xBetween){
        	System.out.println("CTLC3");
        }
    	model.ifThen(CTLC3Bool,
                model.and(model.arithm(Vk1, "<=", v),
                		model.scalar(newSpeed, coeffs1, "=",0),
                		(model.arithm(v1Square, "/", A, ">=", xk))));
    	
    	model.ifThen(model.boolNotView(xNearTL), model.arithm(Vk1, ">", 0));
    	
    	//CTLC4 vk+1 = vk + a*timeStep if vk+1<vsl, else: vk+1 = vk => no change        	
    	if ((trafficLight == "green") && xBetween){
        	System.out.println("CTLC4");
        }
    	model.ifThen(CTCL4Bool, model.scalar(newSpeed, coeffs1, "=",0));
    	
    	//CTLC5 if x>0 vk+1 = vk + a*timeStep if vk+1<vsl, else: vk+1 = vk => no change
    	if (xk > 0){
        	System.out.println("CTLC5");
        }
    	model.ifThen(xPastTL, model.scalar(newSpeed, coeffs1, "=",0));
    	
        //Simulation
        Solver solver = model.getSolver();
        System.out.println(" *** The traffic light is : " + trafficLight + " ! ***");
        if(solver.solve()){
			v = Vk1.getValue();
			System.out.println("Speed = " + v + " dm/s");	
			
		}
		else {
			System.out.println("Problem with resolution");
		}
		return v;
	}
	
	public static void launch() {
		
		int timeStep = 1; //Time step
		
		int unitScale = 10;
		
		int x0 = -110 * unitScale; //Initial position in dm 
		int xmin = -80 * unitScale; 		
		int v0 = 10 * unitScale; //Initial speed 40 km/h
		int vmax = 35 * unitScale; //Speed max 130 km/h
		int vsl = 14 * unitScale; //Speed limit
		

		int a = 3; //Acceleration 3 km/h
		
		
		System.out.println("Original position : " + x0 + " dm");
		System.out.println("Original speed : " + v0 + " dm/s");
		
		v0 = simu(timeStep, x0,xmin, v0, a, vmax, "green", vsl, unitScale);
		x0 += v0*timeStep; //xk+1 = x0 + v*h
		
		System.out.println("Position :" + x0 + " dm");
		
		for(int i = 1; i <= 3; i++)
		{		
			v0 = simu(timeStep, x0, xmin, v0, a, vmax, "green", vsl, unitScale);
			x0 += v0*timeStep;
			
			System.out.println("Position :" + x0 + " dm");
		}
		
		for(int i = 1; i <= 3; i++)
		{
			v0 = simu(timeStep, x0, xmin,  v0, a, vmax, "orange", vsl, unitScale);
			x0 += v0*timeStep;
			
			System.out.println("Position :" + x0 + " dm");
		}
		for(int i = 1; i <= 5; i++)
		{

			v0 = simu(timeStep, x0, xmin, v0, a, vmax, "red", vsl, unitScale);
			x0 += v0*timeStep;
			System.out.println("Position :" + x0 + " dm");
		}
	}

}
