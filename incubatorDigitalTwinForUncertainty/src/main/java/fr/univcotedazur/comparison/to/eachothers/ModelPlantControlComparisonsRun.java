package fr.univcotedazur.comparison.to.eachothers;

import fr.univcotedazur.SimulationConstants;
import fr.univcotedazur.models.*;
import fr.univcotedazur.utils.PlotHelper;
import uDataTypes.UReal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModelPlantControlComparisonsRun extends SimulationConstants {


    public static void main(String[] args) {
        int epochs = (int) (simulationTime / timeStep);

        List<Double> allTimeStamps  = new ArrayList<Double>();

        List<UReal> allBoxTemperaturesModelPlantWithUncertainty = new ArrayList<>();
        List<UReal>allHeaterStateModelPlantWithUncertainty = new ArrayList<>();
        // Inputs/Outputs
        UReal boxTemperatureModelPlantWithUncertainty = null;
        boolean heaterStateModelPlantWithUncertainty = false;


        List<UReal> allBoxTemperaturesModelPlantClassical = new ArrayList<>();
        List<UReal>allHeaterStateModelPlantClassical = new ArrayList<>();
        // Inputs/Outputs
        UReal boxTemperatureModelPlantClassical = null;
        boolean heaterStateModelPlantClassical = false;


        List<UReal> allBoxTemperaturesModelPlantPerfect = new ArrayList<>();
        List<UReal>allHeaterStateModelPlantPerfect = new ArrayList<>();
        // Inputs/Outputs
        UReal boxTemperatureModellPlantPerfect = null;
        boolean heaterStateModelPlantPerfect = false;



        double time = 0.0;

        ControllerModelUncertaintyAware controllerUncertaintyAware = new ControllerModelUncertaintyAware(0.8);
        PlantModel ModelPlantWithUncertainty= new UncertainPlantModel();

        ControllerModel controllerClassical = new ControllerModel();
        // the classical plant model differs from the perfect but does not characterize the uncertainty
        PlantModel modelPlantClassical = new ClassicalPlantModel();

        ControllerModelUncertaintyAware controllerPerfect = new ControllerModelUncertaintyAware();
        PlantModel physicalPlantPerfect = new PerfectPlantModel();

        while (time <= simulationTime){
//            System.out.println(time);
            PlantSnapshot resModelPlantWithUncertainty   =  ModelPlantWithUncertainty.doStep(timeStep,heaterStateModelPlantWithUncertainty, new UReal(21, 0.00));
            boxTemperatureModelPlantWithUncertainty = resModelPlantWithUncertainty.boxTemperature;
            if(time % ControlPERIOD < timeStep) {
                heaterStateModelPlantWithUncertainty = controllerUncertaintyAware.doStep(timeStep, boxTemperatureModelPlantWithUncertainty);
                allBoxTemperaturesModelPlantWithUncertainty.add(boxTemperatureModelPlantWithUncertainty);
                allHeaterStateModelPlantWithUncertainty.add(heaterStateModelPlantWithUncertainty ? new UReal(20, 0) : new UReal(10, 0));
            }

            PlantSnapshot resModelPlantClassical   =  modelPlantClassical.doStep(timeStep,heaterStateModelPlantClassical, new UReal(21, 0.00));
            boxTemperatureModelPlantClassical = resModelPlantClassical.boxTemperature;
            if(time % ControlPERIOD < timeStep) {
                heaterStateModelPlantClassical = controllerClassical.doStep(timeStep, boxTemperatureModelPlantClassical.getX());
                allBoxTemperaturesModelPlantClassical.add(boxTemperatureModelPlantClassical);
                allHeaterStateModelPlantClassical.add(heaterStateModelPlantClassical ? new UReal(20, 0) : new UReal(10, 0));
            }

            PlantSnapshot resModelPlantPerfect   =  physicalPlantPerfect.doStep(timeStep,heaterStateModelPlantPerfect, new UReal(21, 0.00));
            boxTemperatureModellPlantPerfect = resModelPlantPerfect.boxTemperature;
            if(time % ControlPERIOD < timeStep) {
                heaterStateModelPlantPerfect = controllerPerfect.doStep(timeStep, boxTemperatureModellPlantPerfect);
                allBoxTemperaturesModelPlantPerfect.add(boxTemperatureModellPlantPerfect);
                allHeaterStateModelPlantPerfect.add(heaterStateModelPlantPerfect ? new UReal(20, 0) : new UReal(10, 0));
            }

            if(time % ControlPERIOD < timeStep) {
                allTimeStamps.add(time);
            }
            time+=timeStep;
        }



        PlotHelper.plotResults(Arrays.asList("T Measurand", "T Classical", "T Uncertainty", "control Measurand", "control Classical", "control Uncertainty"),
                allTimeStamps,
                allBoxTemperaturesModelPlantPerfect,
                allBoxTemperaturesModelPlantClassical,
                allBoxTemperaturesModelPlantWithUncertainty,
                allHeaterStateModelPlantPerfect,
                allHeaterStateModelPlantClassical,
                allHeaterStateModelPlantWithUncertainty
        );



    }



}
