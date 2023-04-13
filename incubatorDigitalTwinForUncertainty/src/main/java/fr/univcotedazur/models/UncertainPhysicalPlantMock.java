package fr.univcotedazur.models;

import uDataTypes.UReal;


public class UncertainPhysicalPlantMock extends PerfectPlantModel{


    public UReal numericalError;

    public UncertainPhysicalPlantMock(){
        super();
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
        this.setT(this.getT().add(der_T).add(numericalError));
        UReal der_T_heater = (new UReal(1.0, 0).divideBy(this.C_heater)).mult(total_power_heater).mult(new UReal(timeStep, 0));
        this.setTHeater(this.getTHeater().add(der_T_heater).add(numericalError));;

        return constructResult(power_in, power_transfer_heat, total_power_heater, power_out_box, total_power_box, der_T, der_T_heater, on_heater);
    }

    @Override
    protected PlantSnapshot constructResult() {
        PlantSnapshot temp = super.constructResult();
        temp.boxTemperature.setU(0.204);
        temp.heaterTemperature.setU(0.204);
        return temp;
    }

    @Override
    protected PlantSnapshot constructResult(UReal power_in, UReal power_transfer_heat, UReal total_power_heater, UReal power_out_box, UReal total_power_box, UReal der_T, UReal der_T_heater, Boolean in_heater_on) {
        PlantSnapshot temp = super.constructResult(power_in, power_transfer_heat, total_power_heater, power_out_box, total_power_box, der_T, der_T_heater,in_heater_on);
        temp.boxTemperature.setU(0.204);
        temp.heaterTemperature.setU(0.204);
        return temp;
    }
}
