package fr.univcotedazur.models;

public class ControllerModel {

    public String name = "";
    public double desiredTemperature = 45.0;
    public double bound = 10.0;

    public ControllerModel(){

    }

    public ControllerModel(String n){
        this();
        this.name=n;
    }
    public enum HeaterState{
        CoolingDown,
        Heating;
    }

    public HeaterState current_state = HeaterState.CoolingDown;
    public double  time = 0.0;


    public boolean doStep(double currentTime, double in_temperature) {
        if (this.current_state == HeaterState.CoolingDown) {
            if (in_temperature <= (this.desiredTemperature - this.bound)) {
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
            if (in_temperature >= (this.desiredTemperature + this.bound)) {
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
