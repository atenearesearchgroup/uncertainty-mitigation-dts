package fr.univcotedazur.simple.runs;

import fr.univcotedazur.SimulationConstants;
import fr.univcotedazur.models.ControllerModelUncertaintyAware;
import fr.univcotedazur.models.PlantModel;
import fr.univcotedazur.models.PlantSnapshot;
import fr.univcotedazur.models.UncertainPlantModel;
import fr.univcotedazur.utils.ChartPlotterWithUncertainty;
import fr.univcotedazur.utils.PlotHelper;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RefineryUtilities;
import uDataTypes.UReal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UncertainModelRun extends SimulationConstants {


    public static void main(String[] args) {

        List<UReal> allBoxTemperaturesUncertainModel = new ArrayList<UReal>();
        List<Boolean>allHeaterStateUncertainModel = new ArrayList<Boolean>();
        List<Double> allTimeStamps  = new ArrayList<Double>();

        // Inputs/Outputs
        UReal boxTemperatureUncertainModel = null;
        boolean heaterStateUncertainModel = false;

        double time = 0.0;

        ControllerModelUncertaintyAware controller = new ControllerModelUncertaintyAware();
        PlantModel uncertainPlantModel= new UncertainPlantModel();

        while (time <= simulationTime){
//            System.out.println(time);
            PlantSnapshot resUncertainModel   =  uncertainPlantModel.doStep(timeStep,heaterStateUncertainModel, new UReal(21, 0.00));
            boxTemperatureUncertainModel = resUncertainModel.boxTemperature;
            if(time% ControlPERIOD < timeStep){
                heaterStateUncertainModel = controller.doStep(timeStep, boxTemperatureUncertainModel); // should it be the nominal or one amongst the possible values)
                allBoxTemperaturesUncertainModel.add(boxTemperatureUncertainModel);
                allTimeStamps.add(time);
                allHeaterStateUncertainModel.add(heaterStateUncertainModel);
            }


            System.out.println(boxTemperatureUncertainModel);
            time+=timeStep;
        }


        PlotHelper.plotResults(Arrays.asList("T uncertain plant Model"),
                allTimeStamps,
                allBoxTemperaturesUncertainModel
                );



    }



}
