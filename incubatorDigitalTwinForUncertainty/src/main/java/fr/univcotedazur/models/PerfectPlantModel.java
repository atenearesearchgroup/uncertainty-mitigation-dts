package fr.univcotedazur.models;

import uDataTypes.UReal;


public class PerfectPlantModel implements PlantModel {
    public UReal C_air;
    public UReal G_box;
    public UReal C_heater;
    public UReal G_heater;

    //Constants
    public UReal V_heater;
    public UReal i_heater;

    //States
    protected UReal T;
    protected UReal T_heater;


    public PerfectPlantModel(double CAir, double GBox, double CHeater, double GHeater, double vHeater, double iHeater){
        this.C_air = new UReal(CAir, 0);
        this.G_box = new UReal(GBox, 0);
        this.C_heater = new UReal(CHeater, 0);
        this.G_heater = new UReal(GHeater, 0);
        this.setT(new UReal(CAir, 0));
        this.setTHeater(new UReal(CAir, 0));
        this.V_heater = new UReal(vHeater, 0);
        this.i_heater = new UReal(iHeater, 0);
    }

    /**
     * By default, create a plant with uncertainty
     */
    public PerfectPlantModel(){
        this(20.0, 0.74,243.46,0.87,12.0,10.45);
    }


    public PlantSnapshot doStep(double timeStep, boolean on_heater, UReal in_room_temperature) {//:Variable=ufloat(21.0,0.001)) -> tuple[Variable, Variable]:
        //update coefs
        UReal power_in = (on_heater) ? (this.V_heater.mult(this.i_heater)) : new UReal(0.0, 0);
        UReal power_transfer_heat = this.G_heater.mult((this.getTHeater().minus(this.getT())));
        UReal total_power_heater = power_in.minus(power_transfer_heat);
        UReal power_out_box = this.G_box.mult((this.getT().minus(in_room_temperature)));
        UReal total_power_box = power_transfer_heat.minus(power_out_box);


        UReal der_T = (new UReal(1.0, 0).divideBy(this.C_air)).mult(total_power_box).mult(new UReal(timeStep, 0));
        this.setT(this.getT().add(der_T));
        UReal der_T_heater = (new UReal(1.0, 0).divideBy(this.C_heater)).mult(total_power_heater).mult(new UReal(timeStep, 0));
        this.setTHeater(this.getTHeater().add(der_T_heater));

        return this.constructResult(power_in, power_transfer_heat, total_power_heater, power_out_box, total_power_box, der_T, der_T_heater, on_heater);
    }

    protected PlantSnapshot constructResult(UReal power_in, UReal power_transfer_heat, UReal total_power_heater, UReal power_out_box, UReal total_power_box, UReal der_T, UReal der_T_heater, Boolean in_heater_on) {
        return new PlantSnapshot(this.getT(), this.getTHeater(), power_in, power_transfer_heat, total_power_heater, power_out_box, total_power_box, der_T, der_T_heater, in_heater_on);
    }

    protected PlantSnapshot constructResult() {
        return new PlantSnapshot(this.getT(), this.getTHeater());
    }


    @Override
    public UReal getT() {
        return T;
    }

    public void setT(UReal t) {
        T = t;
    }



    @Override
    public UReal getTHeater() {
        return T_heater;
    }

    @Override
    public void setTHeater(UReal t_heater) {
        T_heater = t_heater;
    }
}
