package fr.univcotedazur.comparison.to.measurand;

import fr.univcotedazur.SimulationConstants;
import fr.univcotedazur.models.*;
import fr.univcotedazur.utils.PlotHelper;
import uDataTypes.UReal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;

public class ModelPlantUncertaintyControlComparisonsToMeasurand extends SimulationConstants {


    public static void main(String[] args) {
        List<Double> allAverages = new ArrayList<>();
        for(int i = 0; i < NUMBER_OF_RUNS; i++){
            allAverages.addAll(runOnce());
        }
        OptionalDouble diffAveraged = allAverages.stream().mapToDouble(d -> Math.abs(d)).average();
        System.out.println("averaged deviation between computed and real temperature while switching:\n\t"+diffAveraged);
        System.out.println("\n"+allAverages);
    }


    public static List<Double> runOnce() {
        int epochs = (int) (simulationTime / timeStep);

        /**
         * data lake
         */
        List<Double> allDiffs = new ArrayList<>();


        List<Double> allTimeStamps  = new ArrayList<Double>();

        List<UReal> allBoxTemperaturesModelPlantWithUncertainty = new ArrayList<>();
        List<UReal>allHeaterStateModelPlantWithUncertainty = new ArrayList<>();
        // Inputs/Outputs
        UReal boxTemperatureModelPlantWithUncertainty = null;
        boolean heaterStateModelPlantWithUncertainty = false;


        List<UReal> allBoxTemperaturesModelPlantPerfect = new ArrayList<>();
        List<UReal>allHeaterStateModelPlantPerfect = new ArrayList<>();
        // Inputs/Outputs
        UReal boxTemperatureModellPlantPerfect = null;



        double time = 0.0;


        /**
         * various instances
         */
        ControllerModelUncertaintyAware controllerUncertaintyAware = new ControllerModelUncertaintyAware(0.95);
        PlantModel ModelPlantWithUncertainty= new UncertainPlantModel();

        PlantModel physicalPlantPerfect = new PerfectPlantModel();

        /**
         * simulation starts
         */
        while (time <= (simulationTime)){
            PlantSnapshot resModelPlantPerfect   =  physicalPlantPerfect.doStep(timeStep,heaterStateModelPlantWithUncertainty, new UReal(21, 0.00));
            boxTemperatureModellPlantPerfect = resModelPlantPerfect.boxTemperature;

            PlantSnapshot resModelPlantWithUncertainty   =  ModelPlantWithUncertainty.doStep(timeStep,heaterStateModelPlantWithUncertainty, new UReal(21, 0.00));
            boxTemperatureModelPlantWithUncertainty = resModelPlantWithUncertainty.boxTemperature;
            if(time % ControlPERIOD < timeStep) {
                boolean lastState = heaterStateModelPlantWithUncertainty;
                heaterStateModelPlantWithUncertainty = controllerUncertaintyAware.doStep(timeStep, boxTemperatureModelPlantWithUncertainty);
                if(lastState != heaterStateModelPlantWithUncertainty){
                    Double diff = boxTemperatureModelPlantWithUncertainty.getX() - boxTemperatureModellPlantPerfect.getX();
                    allDiffs.add(diff);
//                    System.out.println("switch comparison"+(heaterStatePhysicalPlantClassical?" heating at ": " cooling at ")+ boxTemperaturePhysicalPlantClassical + ", i.e. "+boxTemperaturePhysicalPlantPerfect+" in reality. Diff = "+diff);
                }
                allBoxTemperaturesModelPlantWithUncertainty.add(boxTemperatureModelPlantWithUncertainty);
                allBoxTemperaturesModelPlantPerfect.add(boxTemperatureModellPlantPerfect);
                allHeaterStateModelPlantWithUncertainty.add(heaterStateModelPlantWithUncertainty ? new UReal(20, 0) : new UReal(10, 0));
            }




            if(time % ControlPERIOD < timeStep) {
                allTimeStamps.add(time);
            }
            time+=timeStep;
        }

        /**
         * manage results
         */

        OptionalDouble diffAveraged = allDiffs.stream().mapToDouble(d -> Math.abs(d)).average();
//        System.out.println(diffAveraged);



//        PlotHelper.plotResults(Arrays.asList("Measurand", "T Model Uncertainty", "Heater State Uncertainty"),
//                allTimeStamps,
//                allBoxTemperaturesModelPlantPerfect,
//                allBoxTemperaturesModelPlantWithUncertainty,
//                allHeaterStateModelPlantWithUncertainty
//        );
        return allDiffs;//diffAveraged.getAsDouble();


    }



}
