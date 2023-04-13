package fr.univcotedazur.models;

import uDataTypes.UReal;


public class UncertainPlantModel extends PerfectPlantModel{


    public UReal numericalError;

    public UncertainPlantModel(UReal CAir, UReal GBox, UReal CHeater, UReal GHeater, UReal vHeater, UReal iHeater){
        this.C_air = CAir;
        this.G_box = GBox;
        this.C_heater = CHeater;
        this.G_heater = GHeater;
        this.setT(CAir);
        this.setTHeater(CAir);
        this.V_heater = vHeater;
        this.i_heater = iHeater;
    }

    /**
     * By default, create a plant with uncertainty
     */
    public UncertainPlantModel(){
        this(new UReal(20.01,0.006), new UReal(0.76,0.015), new UReal(243.40,0.035), new UReal(0.91,0.04), new UReal(12.01,0.005), new UReal(10.46, 0.005));
        //    public PerfectPlantModel(20.0, 0.74,243.46,0.87,12.0,10.45)
    }

    public PlantSnapshot doStep(double timeStep, boolean on_heater, UReal in_room_temperature) {//:Variable=ufloat(21.0,0.001)) -> tuple[Variable, Variable]:
        //update numericalError
        numericalError = new UReal(0,timeStep*timeStep);

        //update coefs
        UReal power_in = (on_heater) ? (this.V_heater.mult(this.i_heater)) : new UReal(0.0, 0);
        UReal power_transfer_heat = this.G_heater.mult((this.getTHeater().minus(this.getT())));
        UReal total_power_heater = power_in.minus(power_transfer_heat);
        UReal power_out_box = this.G_box.mult((this.getT().minus(in_room_temperature)));
        UReal total_power_box = power_transfer_heat.minus(power_out_box);


        UReal der_T = (new UReal(1.0, 0).divideBy(this.C_air)).mult(total_power_box).mult(new UReal(timeStep, 0));
        UReal tempT = this.getT().add(der_T);
        tempT.setU(tempT.getU()+(timeStep*timeStep)); //not sure how to add numerical error
        this.setT(tempT);
        UReal der_T_heater = (new UReal(1.0, 0).divideBy(this.C_heater)).mult(total_power_heater).mult(new UReal(timeStep, 0));
        UReal tempTH = this.getTHeater().add(der_T_heater);
        tempTH.setU(tempTH.getU()+(timeStep*timeStep)); //not sure how to add numerical error
        this.setTHeater(tempTH);;

        return constructResult(power_in, power_transfer_heat, total_power_heater, power_out_box, total_power_box, der_T, der_T_heater,on_heater);
    }


}
