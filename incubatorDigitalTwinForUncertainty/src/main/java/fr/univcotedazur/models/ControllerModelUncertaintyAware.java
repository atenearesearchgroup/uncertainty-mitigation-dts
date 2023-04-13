package fr.univcotedazur.models;

import uDataTypes.UReal;

public class ControllerModelUncertaintyAware {

    public String name = "";
    private final double tolerance;
    public double desiredTemperature = 45.0;
    public double bound = 10.0;

    public ControllerModelUncertaintyAware(){
        this.tolerance = 0.95;
    }

    public ControllerModelUncertaintyAware(String n){
        this();
        this.name=n;
    }
    public ControllerModelUncertaintyAware(double tolerance){
        this.tolerance = tolerance;
    }

    public ControllerModelUncertaintyAware(double tolerance, String n){
        this(tolerance);
        this.name = n;
    }
    public enum HeaterState{
        CoolingDown,
        Heating;
    }

    public HeaterState current_state = HeaterState.CoolingDown;
    public double  time = 0.0;


    public boolean doStep(double currentTime, UReal in_temperature) {
        if (this.current_state == HeaterState.CoolingDown) {
            if ((in_temperature.le(new UReal(this.desiredTemperature - this.bound, 0)).getC() >= tolerance)) {
               // System.out.println("["+name+"] switch to heating @" + this.time+ "  T="+in_temperature);
                this.current_state = HeaterState.Heating;
                this.time = currentTime;
                return true;
            } else {
                this.time = currentTime;
                return false;
            }
        }
        if (this.current_state == HeaterState.Heating) {
            if ((in_temperature.ge(new UReal(this.desiredTemperature + this.bound, 0)).getC() >= tolerance)) {
               // System.out.println("["+name+"] switch to CoolingDown @" + this.time+ "  T="+in_temperature);
                this.current_state = HeaterState.CoolingDown;
                this.time = currentTime;
                return false;
            } else {
                this.time = currentTime;
                return true;
            }
        }
        System.out.println("NOT EXPECTED STATE IN CONTROLLER: T=" + in_temperature + " desired =" + this.desiredTemperature + " bound=" + this.bound);
        this.time = currentTime;
        return false;
    }

}
