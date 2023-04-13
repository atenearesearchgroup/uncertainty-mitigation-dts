package fr.univcotedazur.models;

import uDataTypes.UReal;


public class ClassicalPlantModel extends PerfectPlantModel{


    public UReal numericalError;

    public ClassicalPlantModel(UReal CAir, UReal GBox, UReal CHeater, UReal GHeater, UReal vHeater, UReal iHeater){
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
    public ClassicalPlantModel(){
        this(new UReal(20.01,0), new UReal(0.76,0), new UReal(243.52,0), new UReal(0.97,0), new UReal(12.01,0), new UReal(10.46, 0));
    }



}
