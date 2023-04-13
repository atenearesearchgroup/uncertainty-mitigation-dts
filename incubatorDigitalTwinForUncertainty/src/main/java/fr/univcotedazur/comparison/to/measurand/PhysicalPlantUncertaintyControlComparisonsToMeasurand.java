package fr.univcotedazur.comparison.to.measurand;

import fr.univcotedazur.SimulationConstants;
import fr.univcotedazur.models.*;
import uDataTypes.UReal;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

public class PhysicalPlantUncertaintyControlComparisonsToMeasurand extends SimulationConstants {

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
         * Data lake !
         */

        List<Double> allDiffs = new ArrayList<>();


        List<Double> allTimeStamps  = new ArrayList<Double>();

        List<UReal> allBoxTemperaturesPhysicalPlantUncertainty = new ArrayList<>();
        List<UReal>allHeaterStatePhysicalPlantUncertainty = new ArrayList<>();
        // Inputs/Outputs
        UReal boxTemperaturePhysicalPlantUncertainty = null;
        boolean heaterStatePhysicalPlantUncertainty = false;

        List<UReal> allBoxTemperaturesPhysicalPlantPerfect = new ArrayList<>();
        List<UReal>allHeaterStatePhysicalPlantPerfect = new ArrayList<>();
        // Inputs/Outputs
        UReal boxTemperaturePhysicalPlantPerfect = null;

        /**
         * instances of the various models
         */
        double time = 0.0;

        ControllerModelUncertaintyAware controllerUncertainty = new ControllerModelUncertaintyAware("controllerUncertainty");
        PlantModel physicalPlantClassical = new NoisyPhysicalPlantMock(0.5);
        PlantModel physicalPlantPerfect = new PerfectPlantModel();

        /**
         * simulation start
         */

        while (time <= simulationTime){
            /**
             *  Physical Plant Classical
             */
            PlantSnapshot resPhysicalPlantClassical   =  physicalPlantClassical.doStep(timeStep,heaterStatePhysicalPlantUncertainty, new UReal(21, 0.00));
            boxTemperaturePhysicalPlantUncertainty = resPhysicalPlantClassical.boxTemperature;
            /**
             *  Perfect, unreachable Physical Plant (Measurand)
             */
            PlantSnapshot resPhysicalPlantPerfect   =  physicalPlantPerfect.doStep(timeStep,heaterStatePhysicalPlantUncertainty, new UReal(21, 0.00));
            boxTemperaturePhysicalPlantPerfect = resPhysicalPlantPerfect.boxTemperature;

            /**
             * Control and saving
             */
            if(time% ControlPERIOD < timeStep) {
                boolean lastState = heaterStatePhysicalPlantUncertainty;
                heaterStatePhysicalPlantUncertainty = controllerUncertainty.doStep(time, boxTemperaturePhysicalPlantUncertainty);
                if(lastState != heaterStatePhysicalPlantUncertainty){
                    Double diff = boxTemperaturePhysicalPlantUncertainty.getX() - boxTemperaturePhysicalPlantPerfect.getX();
                    allDiffs.add(diff);
//                    System.out.println("switch comparison"+(heaterStatePhysicalPlantUncertainty?" heating at ": " cooling at ")+ boxTemperaturePhysicalPlantUncertainty + ", i.e. "+boxTemperaturePhysicalPlantPerfect+" in reality; diff="+diff);
                }

                allBoxTemperaturesPhysicalPlantUncertainty.add(boxTemperaturePhysicalPlantUncertainty);
                allHeaterStatePhysicalPlantUncertainty.add(heaterStatePhysicalPlantUncertainty ? new UReal(20, 0) : new UReal(10, 0));
                allBoxTemperaturesPhysicalPlantPerfect.add(boxTemperaturePhysicalPlantPerfect);
                allHeaterStatePhysicalPlantPerfect.add(heaterStatePhysicalPlantUncertainty?new UReal(20,0):new UReal(10,0));
            }




            /**
             * advance time
             */
            if(time% ControlPERIOD < timeStep) {
                allTimeStamps.add(time);
            }
            time+=timeStep;
        }


        /**
         * manage results
         */

        OptionalDouble diffAveraged = allDiffs.stream().mapToDouble(d -> Math.abs(d)).average();
//        System.out.println(diffAveraged);

        return allDiffs;// diffAveraged.getAsDouble();

//        PlotHelper.plotResults(Arrays.asList("T Measurand Physical Plant", "T Uncertainty Physical Plant",  "Heater State Uncertainty"),
//                allTimeStamps,
//                allBoxTemperaturesPhysicalPlantPerfect,
//                allBoxTemperaturesPhysicalPlantUncertainty,
//                allHeaterStatePhysicalPlantUncertainty
//        );
    }



}
