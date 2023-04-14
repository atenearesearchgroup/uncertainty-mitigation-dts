package fr.univcotedazur.comparison.to.eachothers;

import fr.univcotedazur.SimulationConstants;
import fr.univcotedazur.models.*;
import fr.univcotedazur.utils.ConfidenceLevel;
import fr.univcotedazur.utils.ConsistencyChecker;
import fr.univcotedazur.utils.PlotHelper;
import uDataTypes.UBoolean;
import uDataTypes.UReal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MitigatedControlFailureComparisonRun extends SimulationConstants {



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
        UReal boxTemperatureMitigated = null;


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
        double time = 0.0;
        int nbDivergence = 0;
        while (time <= simulationTime){
            if(time > 600){
                //add a (artificial) failure to add significant divergence (in each but perfect)
                ((NoisyPhysicalPlantMock)(physicalPlantForMitigation)).G_box= new UReal(6, 0.02);
                ((NoisyPhysicalPlantMock)(physicalPlantForMitigation)).C_heater= new UReal(153.46, 0.02);

                ((NoisyPhysicalPlantClassicalMock)(physicalPlantClassical)).G_box= new UReal(6, 0);
                ((NoisyPhysicalPlantClassicalMock)(physicalPlantClassical)).C_heater= new UReal(153.46, 0);


            }



            /**
             *  Physical Plant and Model For Mitigation
             */
            PlantSnapshot resPhysicalPlantForMitigation   =  physicalPlantForMitigation.doStep(timeStep,heaterStateMitigated, new UReal(21, 0.00));
            boxTemperaturePhysicalPlantForMitigation = resPhysicalPlantForMitigation.boxTemperature;
            PlantSnapshot resModelPlantForMitigation = modelPlantForMitigation.doStep(timeStep,heaterStateMitigated, new UReal(21, 0.00));
            boxTemperatureModelPlantForMitigation = resModelPlantForMitigation.boxTemperature;

            //mitigation
            boxTemperatureMitigated = boxTemperatureModelPlantForMitigation.add(boxTemperaturePhysicalPlantForMitigation).divideBy(new UReal(2.0,0.0));
            UReal heaterTemperatureMitigated = resModelPlantForMitigation.heaterTemperature.add(resPhysicalPlantForMitigation.heaterTemperature).divideBy(new UReal(2.0,0.0));


            /**
             *  checking consistency and safe stop if divergence for more then 5*timestep
             */
            if(time% ControlPERIOD < timeStep){

                UBoolean twinsAreConsistent = ConsistencyChecker.areConsistent(boxTemperatureModelPlantForMitigation, boxTemperaturePhysicalPlantForMitigation, ConfidenceLevel.NinetyNineDotSevenPercent);
                if (twinsAreConsistent.getC() < 0.1 || nbDivergence > 5){
                    if (nbDivergence++ == 5) {
                        System.out.println("warning something bad happens --> diverge @"+time+ "("+boxTemperatureModelPlantForMitigation+" versus "+boxTemperaturePhysicalPlantForMitigation+") => "+twinsAreConsistent.getC());
                    }
                    if (nbDivergence >= 5) {
                        //inconsistent => safe stop !
                        heaterStateMitigated = false;
                    }else{
                        System.out.println("diverge@"+time+"   "+twinsAreConsistent.getC());
                    }
                }else{ // consistent => normal control
                    nbDivergence = 0;
                    heaterStateMitigated = controllerForMitigation.doStep(timeStep, boxTemperatureMitigated);
                }
            }

            //0.3 is very random
            if (boxTemperatureModelPlantForMitigation.getU() > 0.5){
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
            if(time%3 < timeStep){
                heaterStatePhysicalPlantClassical = controllerClassical.doStep(timeStep, boxTemperaturePhysicalPlantClassical.getX());

            }


            /**
             *  Perfect, unreachable Physical Plant (Measurand)
             */
            PlantSnapshot resPhysicalPlantPerfect   =  physicalPlantPerfect.doStep(timeStep,heaterStatePhysicalPlantPerfect, new UReal(21, 0.00));
            boxTemperaturePhysicalPlantPerfect = resPhysicalPlantPerfect.boxTemperature;
            if(time%3 < timeStep) {
                heaterStatePhysicalPlantPerfect = controllerPerfect.doStep(timeStep, boxTemperaturePhysicalPlantPerfect);

            }


            /**
             * advance time
             */
            if(time%3 < timeStep) {
                allTimeStamps.add(time);


                //save to data lake
                allBoxTemperaturesPhysicalPlantForMitigation.add(boxTemperaturePhysicalPlantForMitigation);
                allBoxTemperaturesModelPlantForMitigation.add(boxTemperatureModelPlantForMitigation);
                allBoxTemperaturesMitigated.add(boxTemperatureMitigated);
                allHeaterStatePhysicalPlantForMitigation.add(heaterStateMitigated?new UReal(20,0):new UReal(10,0));

                boxTemperaturePhysicalPlantClassical.setU(0);
                allBoxTemperaturesPhysicalPlantClassical.add(boxTemperaturePhysicalPlantClassical);
                allHeaterStatePhysicalPlantClassical.add(heaterStatePhysicalPlantClassical?new UReal(20,0):new UReal(10,0));

                allBoxTemperaturesPhysicalPlantPerfect.add(boxTemperaturePhysicalPlantPerfect);
                allHeaterStatePhysicalPlantPerfect.add(heaterStatePhysicalPlantPerfect ? new UReal(20, 0) : new UReal(10, 0));
            }
            time+=timeStep;
        }


        /**
         * manage results
         */
        PlotHelper.plotResults(Arrays.asList("t GT", "t PT", "t MDTS", "ctrl GT", "ctrl PT", "ctrl MDTS"),
                allTimeStamps,
                allBoxTemperaturesPhysicalPlantPerfect,
                allBoxTemperaturesPhysicalPlantClassical,
                allBoxTemperaturesPhysicalPlantForMitigation,
                allHeaterStatePhysicalPlantPerfect,
                allHeaterStatePhysicalPlantClassical,
                allHeaterStatePhysicalPlantForMitigation
        );
    }



}
