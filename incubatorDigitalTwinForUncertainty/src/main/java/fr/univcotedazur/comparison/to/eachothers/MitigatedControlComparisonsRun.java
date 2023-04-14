package fr.univcotedazur.comparison.to.eachothers;

import fr.univcotedazur.SimulationConstants;
import fr.univcotedazur.models.*;
import fr.univcotedazur.utils.PlotHelper;
import uDataTypes.UReal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MitigatedControlComparisonsRun extends SimulationConstants {



    public static void main(String[] args) {
        int epochs = (int) (simulationTime / timeStep);

        /**
         * Data lake !
         */

        List<Double> allTimeStamps  = new ArrayList<Double>();
        List<UReal> allBoxTemperaturesPhysicalPlantForMitigation = new ArrayList<>();
        List<UReal>allHeaterStatePhysicalPlantForMitigation = new ArrayList<>();
        // Inputs/Outputs
        UReal boxTemperaturePhysicalPlantForMitigation = null;
        List<UReal> allBoxTemperaturesModelPlantForMitigation = new ArrayList<>();
        List<UReal>allHeaterStateModelPlantForMitigation = new ArrayList<>();
        // Inputs/Outputs
        UReal boxTemperatureModelPlantForMitigation = null;

        List<UReal> allBoxTemperaturesMitigated = new ArrayList<>();
        boolean heaterStateMitigated = false;
        UReal boxTemperatureMitigated = new UReal(0,0);


        List<UReal> allBoxTemperaturesPhysicalPlantClassical = new ArrayList<>();
        List<UReal>allHeaterStatePhysicalPlantClassical = new ArrayList<>();
        // Inputs/Outputs
        UReal boxTemperaturePhysicalPlantClassical = null;
        boolean heaterStatePhysicalPlantClassical = false;

        List<UReal> allBoxTemperaturesPhysicalPlantPerfect = new ArrayList<>();
        List<UReal>allHeaterStatePhysicalPlantPerfect = new ArrayList<>();
        // Inputs/Outputs
        UReal boxTemperaturePhysicalPlantPerfect = null;
        boolean heaterStatePhysicalPlantPerfect = false;


        /**
         * instances of the various models
         */
        double time = 0.0;

        ControllerModelUncertaintyAware controllerForMitigation = new ControllerModelUncertaintyAware(0.95);
        PlantModel physicalPlantForMitigation= new NoisyPhysicalPlantMock(0.5);
        PlantModel modelPlantForMitigation= new UncertainPlantModel();

        ControllerModel controllerClassical = new ControllerModel();
        PlantModel physicalPlantClassical = new NoisyPhysicalPlantClassicalMock(0.5);

        ControllerModelUncertaintyAware controllerPerfect = new ControllerModelUncertaintyAware(1);
        PlantModel physicalPlantPerfect = new PerfectPlantModel();

        /**
         * simulation start
         */

        while (time <= simulationTime){
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

          //  boxTemperatureMitigated = boxTemperatureModelPlantForMitigation.add(boxTemperaturePhysicalPlantForMitigation).divideBy(new UReal(2.0,0.0));

            UReal heaterTemperatureMitigated = resModelPlantForMitigation.heaterTemperature.add(resPhysicalPlantForMitigation.heaterTemperature).divideBy(new UReal(2.0,0.0));

            if(time% ControlPERIOD < timeStep){
//                System.out.println("doStep@"+time);
                heaterStateMitigated = controllerForMitigation.doStep(timeStep, boxTemperatureMitigated);
                //save to data lake
                allBoxTemperaturesPhysicalPlantForMitigation.add(boxTemperaturePhysicalPlantForMitigation);
                allBoxTemperaturesModelPlantForMitigation.add(boxTemperatureModelPlantForMitigation);
                allBoxTemperaturesMitigated.add(boxTemperatureMitigated);
                allHeaterStatePhysicalPlantForMitigation.add(heaterStateMitigated?new UReal(20,0):new UReal(10,0));
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
             *  Physical Plant Classical
             */
            PlantSnapshot resPhysicalPlantClassical   =  physicalPlantClassical.doStep(timeStep,heaterStatePhysicalPlantClassical, new UReal(21, 0.00));
            boxTemperaturePhysicalPlantClassical = resPhysicalPlantClassical.boxTemperature;
            if(time%3 < 0.001){
                heaterStatePhysicalPlantClassical = controllerClassical.doStep(timeStep, boxTemperaturePhysicalPlantClassical.getX());
                allBoxTemperaturesPhysicalPlantClassical.add(boxTemperaturePhysicalPlantClassical);
                allHeaterStatePhysicalPlantClassical.add(heaterStatePhysicalPlantClassical?new UReal(20,0):new UReal(10,0));
            }


            /**
             *  Perfect, unreachable Physical Plant (Measurand)
             */
            PlantSnapshot resPhysicalPlantPerfect   =  physicalPlantPerfect.doStep(timeStep,heaterStatePhysicalPlantPerfect, new UReal(21, 0.00));
            boxTemperaturePhysicalPlantPerfect = resPhysicalPlantPerfect.boxTemperature;
            if(time%3 < timeStep) {
                heaterStatePhysicalPlantPerfect = controllerPerfect.doStep(timeStep, boxTemperaturePhysicalPlantPerfect);
                allBoxTemperaturesPhysicalPlantPerfect.add(boxTemperaturePhysicalPlantPerfect);
                allHeaterStatePhysicalPlantPerfect.add(heaterStatePhysicalPlantPerfect ? new UReal(20, 0) : new UReal(10, 0));
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
        PlotHelper.plotResults(Arrays.asList("t GT ", "t PT", "t MDTS", "ctrl GT", "ctrl PT", "ctrl MDTS"),
                allTimeStamps,
                allBoxTemperaturesPhysicalPlantPerfect,
                allBoxTemperaturesPhysicalPlantClassical,
                allBoxTemperaturesMitigated,
                allHeaterStatePhysicalPlantPerfect,
                allHeaterStatePhysicalPlantClassical,
                allHeaterStatePhysicalPlantForMitigation
        );
    }



}
