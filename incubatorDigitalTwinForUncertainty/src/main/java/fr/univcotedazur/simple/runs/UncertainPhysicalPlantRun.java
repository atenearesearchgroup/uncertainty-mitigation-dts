package fr.univcotedazur.simple.runs;

import fr.univcotedazur.models.ControllerModelUncertaintyAware;
import fr.univcotedazur.models.PlantModel;
import fr.univcotedazur.models.PlantSnapshot;
import fr.univcotedazur.models.UncertainPhysicalPlantMock;
import fr.univcotedazur.utils.ChartPlotterWithUncertainty;
import fr.univcotedazur.utils.PlotHelper;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RefineryUtilities;
import uDataTypes.UReal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UncertainPhysicalPlantRun {


    public static void main(String[] args) {
        double simulationTime= 1200;
        double timeStep = 0.1;
//        int epochs = (int) (simulationTime / timeStep);

        List<UReal> allBoxTemperaturesUncertainPhysicalPlant = new ArrayList<>();
        List<Boolean>allHeaterStateUncertainPhysicalPlant = new ArrayList<>();
        List<Double> allTimeStamps  = new ArrayList<Double>();

        // Inputs/Outputs
        UReal boxTemperatureUncertainPhysicalPlant = null;
        boolean heaterStateUncertainPhysicalPlant = false;

        double time = 0.0;

        ControllerModelUncertaintyAware controller = new ControllerModelUncertaintyAware();
        PlantModel uncertainPhysicalPlant= new UncertainPhysicalPlantMock();

        while (time <= simulationTime){
//            System.out.println(time);
            PlantSnapshot resUncertainPhysicalPlant   =  uncertainPhysicalPlant.doStep(timeStep,heaterStateUncertainPhysicalPlant, new UReal(21, 0.00));
            boxTemperatureUncertainPhysicalPlant = resUncertainPhysicalPlant.boxTemperature;
            heaterStateUncertainPhysicalPlant = controller.doStep(timeStep, boxTemperatureUncertainPhysicalPlant);

            allBoxTemperaturesUncertainPhysicalPlant.add(boxTemperatureUncertainPhysicalPlant);
            allTimeStamps.add(time);
            allHeaterStateUncertainPhysicalPlant.add(heaterStateUncertainPhysicalPlant);

            time+=timeStep;
        }

        XYDataset dataSet = PlotHelper.createJFreeChartDataset( allTimeStamps,
                Arrays.asList("T uncertain Physical Plant"),
                allBoxTemperaturesUncertainPhysicalPlant
        );
        ChartPlotterWithUncertainty plotter = new ChartPlotterWithUncertainty("Box Temperature", "time (s)", "T (°C)", dataSet);
        plotter.pack();
        RefineryUtilities.centerFrameOnScreen(plotter);
        plotter.setVisible(true);


    }



}
