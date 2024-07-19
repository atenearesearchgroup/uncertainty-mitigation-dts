package fr.univcotedazur.comparison.to.eachothers;

import fr.univcotedazur.SimulationConstants;
import fr.univcotedazur.models.*;
import fr.univcotedazur.utils.ChartPlotterWithUncertainty;
import fr.univcotedazur.utils.PlotHelper;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RefineryUtilities;
import uDataTypes.UReal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PhysicalPlantControlComparisonsRun extends SimulationConstants {


    public static void main(String[] args) {
        int epochs = (int) (simulationTime / timeStep);

        /**
         * Data lake !
         */

        List<Double> allTimeStamps  = new ArrayList<Double>();
        List<UReal> allBoxTemperaturesPhysicalPlantWithUncertainty = new ArrayList<>();
        List<UReal>allHeaterStatePhysicalPlantWithUncertainty = new ArrayList<>();
        // Inputs/Outputs
        UReal boxTemperaturePhysicalPlantWithUncertainty = null;
        boolean heaterStatePhysicalPlantWithUncertainty = false;

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

        ControllerModelUncertaintyAware controllerUncertaintyAware = new ControllerModelUncertaintyAware("controllerUncertaintyAware");
        PlantModel physicalPlantWithUncertainty= new NoisyPhysicalPlantMock(0.5);

        ControllerModel controllerClassical = new ControllerModel("controllerClassical");
        PlantModel physicalPlantClassical = new NoisyPhysicalPlantClassicalMock(0.5);

        ControllerModelUncertaintyAware controllerPerfect = new ControllerModelUncertaintyAware("controllerPerfect");
        PlantModel physicalPlantPerfect = new PerfectPlantModel();

        /**
         * simulation start
         */

        while (time <= simulationTime){
            /**
             *  Physical Plant With Uncertainty
             */
            PlantSnapshot resPhysicalPlantWithUncertainty   =  physicalPlantWithUncertainty.doStep(timeStep,heaterStatePhysicalPlantWithUncertainty, new UReal(21, 0.00));
            boxTemperaturePhysicalPlantWithUncertainty = resPhysicalPlantWithUncertainty.boxTemperature;
            if(time% ControlPERIOD < timeStep) {
                heaterStatePhysicalPlantWithUncertainty = controllerUncertaintyAware.doStep(time, boxTemperaturePhysicalPlantWithUncertainty);
                allBoxTemperaturesPhysicalPlantWithUncertainty.add(boxTemperaturePhysicalPlantWithUncertainty);
                allHeaterStatePhysicalPlantWithUncertainty.add(heaterStatePhysicalPlantWithUncertainty ? new UReal(20, 0) : new UReal(10, 0));
            }

            /**
             *  Physical Plant Classical
             */
            PlantSnapshot resPhysicalPlantClassical   =  physicalPlantClassical.doStep(timeStep,heaterStatePhysicalPlantClassical, new UReal(21, 0.00));
            boxTemperaturePhysicalPlantClassical = resPhysicalPlantClassical.boxTemperature;
            if(time% ControlPERIOD < timeStep) {
                heaterStatePhysicalPlantClassical = controllerClassical.doStep(time, boxTemperaturePhysicalPlantClassical.getX());
                allBoxTemperaturesPhysicalPlantClassical.add(boxTemperaturePhysicalPlantClassical);
                allHeaterStatePhysicalPlantClassical.add(heaterStatePhysicalPlantClassical ? new UReal(20, 0) : new UReal(10, 0));
            }

            /**
             *  Perfect, unreachable Physical Plant (Measurand)
             */
            PlantSnapshot resPhysicalPlantPerfect   =  physicalPlantPerfect.doStep(timeStep,heaterStatePhysicalPlantPerfect, new UReal(21, 0.00));
            boxTemperaturePhysicalPlantPerfect = resPhysicalPlantPerfect.boxTemperature;
            if(time% ControlPERIOD < timeStep){
                heaterStatePhysicalPlantPerfect = controllerPerfect.doStep(time, boxTemperaturePhysicalPlantPerfect);
                allBoxTemperaturesPhysicalPlantPerfect.add(boxTemperaturePhysicalPlantPerfect);
                allHeaterStatePhysicalPlantPerfect.add(heaterStatePhysicalPlantPerfect?new UReal(20,0):new UReal(10,0));
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
        PlotHelper.plotResults(Arrays.asList("T Measurand", "T Classical", "T Uncertainty", "Control Measurand", "Control Classical", "Control Uncertainty"),
                allTimeStamps,
                allBoxTemperaturesPhysicalPlantPerfect,
                allBoxTemperaturesPhysicalPlantClassical,
                allBoxTemperaturesPhysicalPlantWithUncertainty,
                allHeaterStatePhysicalPlantPerfect,
                allHeaterStatePhysicalPlantClassical,
                allHeaterStatePhysicalPlantWithUncertainty
        );
    }



}
