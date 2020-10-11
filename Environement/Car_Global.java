package Environement;


import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

public class Car_Global{

    private int position;
    private int speed;
    private int acceleration;

    private int speedMax;
    private int accelerationMax;
    private int accelerationMin;

    public Car_Global(int initialPosition, int initialSpeed, int initialAcceleration, int vMax, int aMin, int aMax){

        this.position = initialPosition;
        this.speed = initialSpeed;
        this.acceleration = initialAcceleration;

        this.speedMax = vMax;
        this.accelerationMax = aMax;
        this.accelerationMin = aMin;
        
    }

    public void setPosition(int newPosition) {
        this.position = newPosition;
    }

    public int getPosition() {
        return position;
    }

    public void setSpeed(int newSpeed) {
        this.speed = newSpeed;
    }

    public int getSpeed() {
        return speed;
    }

    public void setAcceleration(int newAcceleration){
        this.acceleration = newAcceleration;
    }

    public int getAcceleration() {
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
}