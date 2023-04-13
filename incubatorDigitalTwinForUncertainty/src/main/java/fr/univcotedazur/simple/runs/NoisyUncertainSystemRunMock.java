package fr.univcotedazur.simple.runs;

import fr.univcotedazur.models.*;
import fr.univcotedazur.utils.ChartPlotterWithUncertainty;
import fr.univcotedazur.utils.PlotHelper;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RefineryUtilities;
import uDataTypes.UReal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NoisyUncertainSystemRunMock {


    public static void main(String[] args) {
        double simulationTime= 1200;
        double timeStep = 0.1;
        int epochs = (int) (simulationTime / timeStep);

        List<UReal> allBoxTemperaturesUncertainPhysicalPlant = new ArrayList<UReal>();
        List<Boolean>allHeaterStateUncertainPhysicalPlant = new ArrayList<Boolean>();
        List<UReal> allBoxTemperaturesPhysicalPlant = new ArrayList<UReal>();
        List<Boolean>allHeaterStatePhysicalPlant = new ArrayList<Boolean>();
        List<Double> allTimeStamps  = new ArrayList<Double>();

        // Inputs/Outputs
        UReal boxTemperatureUncertainPhysicalPlant = null;
        boolean heaterStateUncertainPhysicalPlant = false;
        UReal boxTemperaturePhysicalPlant = null;
        boolean heaterStatePhysicalPlant = false;

        double time = 0.0;

        ControllerModelUncertaintyAware controller = new ControllerModelUncertaintyAware();
        PlantModel uncertainPhysicalPlant= new UncertainPlantModel();

        ControllerModelUncertaintyAware controller2 = new ControllerModelUncertaintyAware();
        PlantModel physicalPlant= new NoisyPhysicalPlantMock(0.5);


        while (time <= simulationTime){
//            System.out.println(time);
            PlantSnapshot resUncertainPhysicalPlant   =  uncertainPhysicalPlant.doStep(timeStep,heaterStateUncertainPhysicalPlant, new UReal(21, 0.00));
            boxTemperatureUncertainPhysicalPlant = resUncertainPhysicalPlant.boxTemperature;
            heaterStateUncertainPhysicalPlant = controller.doStep(timeStep, boxTemperatureUncertainPhysicalPlant);

            allBoxTemperaturesUncertainPhysicalPlant.add(boxTemperatureUncertainPhysicalPlant);
            allHeaterStateUncertainPhysicalPlant.add(heaterStateUncertainPhysicalPlant);




            PlantSnapshot resPhysicalPlant   =  physicalPlant.doStep(timeStep,heaterStatePhysicalPlant, new UReal(21, 0.00));
            boxTemperaturePhysicalPlant = resPhysicalPlant.boxTemperature;
            heaterStatePhysicalPlant = controller2.doStep(timeStep, boxTemperaturePhysicalPlant);

            allBoxTemperaturesPhysicalPlant.add(boxTemperaturePhysicalPlant);
            allHeaterStatePhysicalPlant.add(heaterStatePhysicalPlant);

            allTimeStamps.add(time);
            time+=timeStep;
        }

        PlotHelper.plotResults(Arrays.asList("T uncertain physical plant", "T Physical Plant"),
                allTimeStamps,
                allBoxTemperaturesUncertainPhysicalPlant,
                allBoxTemperaturesPhysicalPlant);


    }




}
