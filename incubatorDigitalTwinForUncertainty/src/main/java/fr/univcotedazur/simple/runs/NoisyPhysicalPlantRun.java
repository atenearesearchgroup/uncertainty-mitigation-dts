package fr.univcotedazur.simple.runs;

import fr.univcotedazur.SimulationConstants;
import fr.univcotedazur.models.ControllerModelUncertaintyAware;
import fr.univcotedazur.models.NoisyPhysicalPlantMock;
import fr.univcotedazur.models.PlantModel;
import fr.univcotedazur.models.PlantSnapshot;
import fr.univcotedazur.utils.ChartPlotterWithUncertainty;
import fr.univcotedazur.utils.PlotHelper;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RefineryUtilities;
import uDataTypes.UReal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NoisyPhysicalPlantRun extends SimulationConstants {


    public static void main(String[] args) {
        List<UReal> allBoxTemperaturesPhysicalPlant = new ArrayList<UReal>();
        List<Boolean>allHeaterStatePhysicalPlant = new ArrayList<Boolean>();
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

                allBoxTemperaturesPhysicalPlant.add(boxTemperaturePhysicalPlant);
                allTimeStamps.add(time);
                allHeaterStatePhysicalPlant.add(heaterStatePhysicalPlant);
            }
            time+=timeStep;
        }

        PlotHelper.plotResults(Arrays.asList("T Physical Plant"),
                allTimeStamps,
                allBoxTemperaturesPhysicalPlant
                );



    }



}
