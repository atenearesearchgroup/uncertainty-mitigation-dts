package fr.univcotedazur.comparison.to.eachothers;

import fr.univcotedazur.SimulationConstants;
import fr.univcotedazur.models.*;
import fr.univcotedazur.utils.ChartPlotterWithUncertainty;
import fr.univcotedazur.utils.PlotHelper;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import uDataTypes.UReal;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PhysicalPlantControlComparisonsForZoom extends SimulationConstants {


    public static void main(String[] args) {
        int epochs = (int) (simulationTime / timeStep);

        /**
         * Data lake !
         */

        List<Double> allTimeStamps  = new ArrayList<Double>();

        List<UReal> allBoxTemperaturesPhysicalPlantClassical = new ArrayList<>();
        List<UReal>allHeaterStatePhysicalPlantClassical = new ArrayList<>();
        List<Double> physicalPlantClassicalSwitchTime = new ArrayList<>();
        // Inputs/Outputs
        UReal boxTemperaturePhysicalPlantClassical = null;
        boolean heaterStatePhysicalPlantClassical = false;

        List<UReal> allBoxTemperaturesPhysicalPlantPerfect = new ArrayList<>();
        List<UReal>allHeaterStatePhysicalPlantPerfect = new ArrayList<>();
        List<Double> perfectPlantSwitchTime = new ArrayList<>();

        // Inputs/Outputs
        UReal boxTemperaturePhysicalPlantPerfect = null;
        boolean heaterStatePhysicalPlantPerfect = false;


        /**
         * instances of the various models
         */
        double time = 0.0;

        ControllerModel controllerClassical = new ControllerModel("controllerClassical");
        PlantModel physicalPlantClassical = new NoisyPhysicalPlantClassicalMock(0.5);

        ControllerModelUncertaintyAware controllerPerfect = new ControllerModelUncertaintyAware("controllerPerfect");
        PlantModel physicalPlantPerfect = new PerfectPlantModel();

        /**
         * simulation start
         */

        while (time <= simulationTime){

            /**
             *  Physical Plant Classical
             */
            PlantSnapshot resPhysicalPlantClassical   =  physicalPlantClassical.doStep(timeStep,heaterStatePhysicalPlantClassical, new UReal(21, 0.00));
            boxTemperaturePhysicalPlantClassical = resPhysicalPlantClassical.boxTemperature;
            if(time% ControlPERIOD < timeStep) {
                boolean previousState = heaterStatePhysicalPlantClassical;
                heaterStatePhysicalPlantClassical = controllerClassical.doStep(time, boxTemperaturePhysicalPlantClassical.getX());
                if (previousState != heaterStatePhysicalPlantClassical){
                    physicalPlantClassicalSwitchTime.add(time);
                }
                allBoxTemperaturesPhysicalPlantClassical.add(boxTemperaturePhysicalPlantClassical);
                allHeaterStatePhysicalPlantClassical.add(heaterStatePhysicalPlantClassical ? new UReal(20, 0) : new UReal(10, 0));
            }

            /**
             *  Perfect, unreachable Physical Plant (Measurand)
             */
            PlantSnapshot resPhysicalPlantPerfect   =  physicalPlantPerfect.doStep(timeStep,heaterStatePhysicalPlantPerfect, new UReal(21, 0.00));
            boxTemperaturePhysicalPlantPerfect = resPhysicalPlantPerfect.boxTemperature;
            if(time% ControlPERIOD < timeStep){
                boolean previousState = heaterStatePhysicalPlantPerfect;
                heaterStatePhysicalPlantPerfect = controllerPerfect.doStep(time, boxTemperaturePhysicalPlantPerfect);
                if (previousState != heaterStatePhysicalPlantPerfect){
                    perfectPlantSwitchTime.add(time);
                }
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
        ChartPlotterWithUncertainty plotter = PlotHelper.plotResults(Arrays.asList("t GT", "t PT", "ctrl GT", "ctrl PT"),
                allTimeStamps,
                allBoxTemperaturesPhysicalPlantPerfect,
                allBoxTemperaturesPhysicalPlantClassical,
                allHeaterStatePhysicalPlantPerfect,
                allHeaterStatePhysicalPlantClassical
                );
        /**
         * add markers at switch time
         */
        XYPlot plot = plotter.chart.getXYPlot();
        for (int i = 0; i < physicalPlantClassicalSwitchTime.size(); i++) {
            ValueMarker marker = new ValueMarker(physicalPlantClassicalSwitchTime.get(i));  // position is the value on the axis
            marker.setPaint(Color.green);
            marker.setLabel(""+Math.round(physicalPlantClassicalSwitchTime.get(i))); // see JavaDoc for labels, colors, strokes
            plot.addDomainMarker(marker);
        }
//        for (int i = 0; i < perfectPlantSwitchTime.size(); i++) {
//            ValueMarker marker = new ValueMarker(perfectPlantSwitchTime.get(i));  // position is the value on the axis
//            marker.setPaint(Color.red);
//            marker.setLabel(""+Math.round(perfectPlantSwitchTime.get(i))); // see JavaDoc for labels, colors, strokes
//            plot.addDomainMarker(marker);
//        }
        ValueMarker marker = new ValueMarker(55);  // position is the value on the axis
        marker.setPaint(Color.black);
        plot.addRangeMarker(marker);

    }



}
