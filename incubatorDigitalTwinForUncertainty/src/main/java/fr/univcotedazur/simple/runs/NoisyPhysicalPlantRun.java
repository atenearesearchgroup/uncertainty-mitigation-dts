package fr.univcotedazur.simple.runs;

import fr.univcotedazur.SimulationConstants;
import fr.univcotedazur.models.ControllerModelUncertaintyAware;
import fr.univcotedazur.models.NoisyPhysicalPlantMock;
import fr.univcotedazur.models.PlantModel;
import fr.univcotedazur.models.PlantSnapshot;
import fr.univcotedazur.utils.PlotHelper;
import uDataTypes.UReal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NoisyPhysicalPlantRun extends SimulationConstants {


    public static void main(String[] args) {
        List<UReal> allBoxTemperaturesPhysicalPlant = new ArrayList<UReal>();
        List<UReal> allHeaterStatePhysicalPlant = new ArrayList<>();
        List<Double> allTimeStamps  = new ArrayList<Double>();

        // Inputs/Outputs
        UReal boxTemperaturePhysicalPlant = null;
        boolean heaterStatePhysicalPlant = false;

        double time = 0.0;

        ControllerModelUncertaintyAware controller = new ControllerModelUncertaintyAware();
        PlantModel physicalPlant= new NoisyPhysicalPlantMock(0.5);

        while (time <= simulationTime){
//            System.out.println(time);
            PlantSnapshot resPhysicalPlant   =  physicalPlant.doStep(timeStep,heaterStatePhysicalPlant, new UReal(21, 0.00));
            boxTemperaturePhysicalPlant = resPhysicalPlant.boxTemperature;
            if(time% ControlPERIOD < timeStep) {
                heaterStatePhysicalPlant = controller.doStep(timeStep, boxTemperaturePhysicalPlant);

                boxTemperaturePhysicalPlant.setU(0);
                allBoxTemperaturesPhysicalPlant.add(boxTemperaturePhysicalPlant);
                allTimeStamps.add(time);
                allHeaterStatePhysicalPlant.add(heaterStatePhysicalPlant ? new UReal(20, 0) : new UReal(10, 0));
            }
            time+=timeStep;
        }

        PlotHelper.plotResults(Arrays.asList("t PT", "ctrl PT"),
                allTimeStamps,
                allBoxTemperaturesPhysicalPlant,
                allHeaterStatePhysicalPlant
                );



    }



}
