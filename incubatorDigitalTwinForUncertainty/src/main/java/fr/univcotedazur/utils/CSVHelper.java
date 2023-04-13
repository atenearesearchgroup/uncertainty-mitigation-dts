package fr.univcotedazur.utils;

import fr.univcotedazur.models.PlantSnapshot;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

public class CSVHelper {

    public static void printToCSV(String fileName, List<Double> allTimeStamps, List<PlantSnapshot> allPerfectSnapshots, List<PlantSnapshot> allPhysicalPlantSnapshots) {
        try {
            PrintStream csvFile = new PrintStream(new FileOutputStream(fileName));
            csvFile.println("time, in_heater_on, T, power_out_box, total_power_box, der_T");
            for(int i = 0; i < allTimeStamps.size(); i++){
                PlantSnapshot perfectSnap= allPerfectSnapshots.get(i);
                PlantSnapshot plantSnap= allPhysicalPlantSnapshots.get(i);
                csvFile.println(allTimeStamps.get(i).toString()+','+perfectSnap.in_heather+","+perfectSnap.boxTemperature.getX()+","+perfectSnap.power_out_box.getX()+","+perfectSnap.total_power_box.getX()+","+perfectSnap.der_T.getX());
                csvFile.println(allTimeStamps.get(i).toString()+','+plantSnap.in_heather+","+plantSnap.boxTemperature.getX()+","+plantSnap.power_out_box.getX()+","+plantSnap.total_power_box.getX()+","+plantSnap.der_T.getX());
            }
            csvFile.flush();
            csvFile.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
