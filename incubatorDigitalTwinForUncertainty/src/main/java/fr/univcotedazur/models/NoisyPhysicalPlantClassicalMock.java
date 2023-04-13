package fr.univcotedazur.models;

import uDataTypes.UReal;

import java.util.Random;


public class NoisyPhysicalPlantClassicalMock extends PerfectPlantModel {

    protected Random r = new Random();
    public double noise= 0.5;

    public NoisyPhysicalPlantClassicalMock(double noise){
        super();
        this.noise = noise;
    }


    @Override
    protected PlantSnapshot constructResult() {
        double doubleNoiseS1 = r.nextDouble() * (2*this.noise);
        double actualNoiseS1 = -noise+ doubleNoiseS1;
        double doubleNoiseS2 = r.nextDouble() * (2*this.noise);
        double actualNoiseS2 = -noise+ doubleNoiseS2;
        double actualNoise = (actualNoiseS1+actualNoiseS2)/2;
        UReal noisyBoxTemp = new UReal(this.getT().add(new UReal(actualNoise, 0)).getX(), 0);
        UReal noisyHeaterTemp = new UReal(this.getTHeater().add(new UReal(actualNoise, 0)).getX(), 0);

        return new PlantSnapshot(
                noisyBoxTemp,
                noisyHeaterTemp
        );
    }

    @Override
    protected PlantSnapshot constructResult(UReal power_in, UReal power_transfer_heat, UReal total_power_heater, UReal power_out_box, UReal total_power_box, UReal der_T, UReal der_T_heater, Boolean in_heater_on) {
        double doubleNoiseS1 = r.nextDouble() * (2*this.noise);
        double actualNoiseS1 = -noise+ doubleNoiseS1;
        double doubleNoiseS2 = r.nextDouble() * (2*this.noise);
        double actualNoiseS2 = -noise+ doubleNoiseS2;
        double actualNoise = (actualNoiseS1+actualNoiseS2)/2;
        UReal noisyBoxTemp = new UReal(this.getT().add(new UReal(actualNoise, 0)).getX(), 0);//noise/Math.sqrt(3));
        UReal noisyHeaterTemp = new UReal(this.getTHeater().add(new UReal(actualNoise, 0)).getX(), 0);//noise/Math.sqrt(3));

        noisyBoxTemp.setU(0);
        noisyHeaterTemp.setU(0);
        return new PlantSnapshot(
                noisyBoxTemp,
                noisyHeaterTemp,
                power_in, power_transfer_heat, total_power_heater, power_out_box, total_power_box, der_T, der_T_heater, in_heater_on
        );
    }
}
