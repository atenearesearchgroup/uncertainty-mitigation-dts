package fr.univcotedazur.comparison.to.measurand;

import fr.univcotedazur.SimulationConstants;
import fr.univcotedazur.models.*;
import uDataTypes.UReal;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

public class MitigatedControlComparisonsToMeasurand extends SimulationConstants {

    static List<Double> allTimeStamps  = new ArrayList<Double>();

    static List<UReal> allBoxTemperaturesPhysicalPlantForMitigation = new ArrayList<>();
    static List<UReal> allBoxTemperaturesModelPlantForMitigation = new ArrayList<>();
    static List<UReal> allBoxTemperaturesMitigated = new ArrayList<>();


    public static void main(String[] args) {
        List<Double> allAverages = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_RUNS; i++) {
            allAverages.addAll(runOnce());
        }
        OptionalDouble diffAveraged = allAverages.stream().mapToDouble(d -> Math.abs(d)).average();
        System.out.println("#########################################");
        System.out.println("averaged deviation between computed and real temperature while switching:\n\t" + diffAveraged);
        System.out.println("\n" + allAverages);
        System.out.println("      -------uncertainties ---------     ");

        String str = "Hello";
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter("uncertaintiesComparison.csv"));
            writer.write("time,\t plant,\t model,\t mitigated\n");

            System.out.println();
            for (int i = 0; i < allBoxTemperaturesMitigated.size(); i++) {
                writer.write(allTimeStamps.get(i) + ",\t"
                        + allBoxTemperaturesPhysicalPlantForMitigation.get(i).getU() + ",\t"
                        + allBoxTemperaturesModelPlantForMitigation.get(i).getU() + ",\t"
                        + allBoxTemperaturesMitigated.get(i).getU()+"\n"
                );
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static List<Double> runOnce() {
        int epochs = (int) (simulationTime / timeStep);

        /**
         * Data lake !
         */
        List<Double> allDiffs = new ArrayList<>();

        List<UReal> allHeaterStatePhysicalPlantForMitigation = new ArrayList<>();
        // Inputs/Outputs
        UReal boxTemperaturePhysicalPlantForMitigation = null;
        List<UReal>allHeaterStateModelPlantForMitigation = new ArrayList<>();
        // Inputs/Outputs
        UReal boxTemperatureModelPlantForMitigation = null;

        boolean heaterStateMitigated = false;
        UReal boxTemperatureMitigated = new UReal(0,0);


        List<UReal> allBoxTemperaturesPhysicalPlantPerfect = new ArrayList<>();
        List<UReal>allHeaterStatePhysicalPlantPerfect = new ArrayList<>();
        // Inputs/Outputs
        UReal boxTemperaturePhysicalPlantPerfect = null;


        /**
         * instances of the various models
         */
        double time = 0.0;

        ControllerModelUncertaintyAware controllerForMitigation = new ControllerModelUncertaintyAware(0.95);
        PlantModel physicalPlantForMitigation= new NoisyPhysicalPlantMock(0.5);
        PlantModel modelPlantForMitigation= new UncertainPlantModel();


        PlantModel physicalPlantPerfect = new PerfectPlantModel();

        /**
         * simulation start
         */

        while (time <= simulationTime){
            /**
             *  Perfect, unreachable Physical Plant (Measurand)
             */
            PlantSnapshot resPhysicalPlantPerfect   =  physicalPlantPerfect.doStep(timeStep,heaterStateMitigated, new UReal(21, 0.00));
            boxTemperaturePhysicalPlantPerfect = resPhysicalPlantPerfect.boxTemperature;

            /**
             *  Physical Plant and Model For Mitigation
             */
            PlantSnapshot resPhysicalPlantForMitigation   =  physicalPlantForMitigation.doStep(timeStep,heaterStateMitigated, new UReal(21, 0.00));
            boxTemperaturePhysicalPlantForMitigation = resPhysicalPlantForMitigation.boxTemperature;
            PlantSnapshot resModelPlantForMitigation = modelPlantForMitigation.doStep(timeStep,heaterStateMitigated, new UReal(21, 0.00));
            boxTemperatureModelPlantForMitigation = resModelPlantForMitigation.boxTemperature;

            //mitigation
            boxTemperatureMitigated = new UReal(0,0);
            boxTemperatureMitigated.setX(
                    (boxTemperaturePhysicalPlantForMitigation.getX()*(Math.pow(boxTemperatureModelPlantForMitigation.getU(),2))
                            +
                            boxTemperatureModelPlantForMitigation.getX()*(Math.pow(boxTemperaturePhysicalPlantForMitigation.getU(),2)))
                            /
                            (Math.pow(boxTemperaturePhysicalPlantForMitigation.getU(),2)+Math.pow(boxTemperatureModelPlantForMitigation.getU(),2))
            );

            boxTemperatureMitigated.setU(
                    Math.sqrt((Math.pow(boxTemperaturePhysicalPlantForMitigation.getU(),2)*Math.pow(boxTemperatureModelPlantForMitigation.getU(),2))
                    / (Math.pow(boxTemperaturePhysicalPlantForMitigation.getU(),2)+Math.pow(boxTemperatureModelPlantForMitigation.getU(),2))
                    )
            );

            //boxTemperatureMitigated = boxTemperatureModelPlantForMitigation.add(boxTemperaturePhysicalPlantForMitigation).divideBy(new UReal(2.0,0.0));
            UReal heaterTemperatureMitigated = resModelPlantForMitigation.heaterTemperature.add(resPhysicalPlantForMitigation.heaterTemperature).divideBy(new UReal(2.0,0.0));

            if(time% ControlPERIOD < timeStep){
                boolean lastState = heaterStateMitigated;
                heaterStateMitigated = controllerForMitigation.doStep(timeStep, boxTemperatureMitigated);
                if(lastState != heaterStateMitigated){
                    Double diff = boxTemperatureMitigated.getX() - boxTemperaturePhysicalPlantPerfect.getX();
                    allDiffs.add(diff);
//                    System.out.println("switch comparison"+(heaterStateMitigated?" heating at ": " cooling at ")+ boxTemperatureMitigated + ", i.e. "+boxTemperaturePhysicalPlantPerfect+" in reality; diff="+diff);
                }

                //save to data lake
                allBoxTemperaturesPhysicalPlantForMitigation.add(boxTemperaturePhysicalPlantForMitigation);
                allBoxTemperaturesModelPlantForMitigation.add(boxTemperatureModelPlantForMitigation);
                allBoxTemperaturesMitigated.add(boxTemperatureMitigated);
                allHeaterStatePhysicalPlantForMitigation.add(heaterStateMitigated?new UReal(20,0):new UReal(10,0));

                allBoxTemperaturesPhysicalPlantPerfect.add(boxTemperaturePhysicalPlantPerfect);
            }

            //0.3 is very random
            if (boxTemperatureModelPlantForMitigation.getU() > 0.3){
               // System.out.println("resynch @"+time);
                UReal tempB = boxTemperatureMitigated;
                //tempB.setU(0);
                modelPlantForMitigation.setT(tempB);
                UReal tempH = heaterTemperatureMitigated;
                // tempH.setU(0);
                modelPlantForMitigation.setTHeater(tempH);
            }




            /**
             * advance time
             */
            if(time%3 < timeStep) {
                allTimeStamps.add(time);
            }
            time+=timeStep;
        }


        /**
         * manage results
         */

        OptionalDouble diffAveraged = allDiffs.stream().mapToDouble(d -> Math.abs(d)).average();
//        System.out.println(diffAveraged);

        return allDiffs;//diffAveraged.getAsDouble();

//        PlotHelper.plotResults(Arrays.asList("T Measurand Physical Plant", "T Physical Plant Mitigated", "Heater State Mitigated"),
//                allTimeStamps,
//                allBoxTemperaturesPhysicalPlantPerfect,
//                allBoxTemperaturesPhysicalPlantForMitigation,
//                allHeaterStatePhysicalPlantForMitigation
//        );
    }



}
