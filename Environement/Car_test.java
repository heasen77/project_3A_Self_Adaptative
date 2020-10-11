package Environement;



import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

public class Car_test {

    public IntVar position;
    public IntVar speed;
    public IntVar acceleration;

    public int speedMax;
    public int accelerationMax;
    public int accelerationMin;

    public Car_test(Model model, int pos, int vit, int acc, int vMax, int aMin, int aMax){

        this.position = model.intVar(pos);
        this.speed = model.intVar(vit);
        this.acceleration = model.intVar(acc);

        this.speedMax = vMax;
        this.accelerationMax = aMax;
        this.accelerationMin = aMin;
        
    }

    public void setPosition(IntVar newPosition) {
        this.position = newPosition;
    }

    public IntVar getPosition() {
        return position;
    }

    public void setSpeed(IntVar newSpeed) {
        this.speed = newSpeed;
    }

    public IntVar getSpeed() {
        return speed;
    }

    public void setAcceleration(IntVar newAcceleration){
        this.acceleration = newAcceleration;
    }

    public IntVar getAcceleration() {
        return acceleration;
    }

    public int getSpeedMax() {
        return speedMax;
    }

    public int getAccelerationMax(){
        return accelerationMax;
    }

    public int getAccelerationMin(){
        return accelerationMin;
    }

    public IntVar stopDistance(Model model, IntVar v, int tr){
        // Calculation of the stop distance
        int vMax = getSpeedMax();
        int aMin = getAccelerationMin();

        IntVar stopDist = model.intVar(0, vMax - (vMax*vMax)/(2*aMin));
        v.mul(tr).sub(v.mul(v).div(2*aMin)).eq(stopDist).post();

        return stopDist;
    }
    public IntVar SpeedCurve(Model model, IntVar x, int aMax){
		IntVar Curve = model.intVar(0, getSpeedMax());
		x.abs().mul(2*aMax).sqr().eq(Curve).post();
		return Curve;
	}
}