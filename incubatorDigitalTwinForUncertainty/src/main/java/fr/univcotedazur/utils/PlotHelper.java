package fr.univcotedazur.utils;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.YIntervalSeries;
import org.jfree.data.xy.YIntervalSeriesCollection;
import org.jfree.ui.RefineryUtilities;
import uDataTypes.UReal;

import java.util.List;

public class PlotHelper {

    public static XYDataset createJFreeChartDataset(List<Double> timestamps,
                                                    List<String> titles,
                                                    List<UReal>... timeSeriesList
                                                   ) {

        return createJFreeChartDataset(timestamps,titles,ConfidenceLevel.NinetyFivePercent, timeSeriesList);

    }


    public static XYDataset createJFreeChartDataset(List<Double> timestamps,
                                                    List<String> titles,
                                                    ConfidenceLevel cl,
                                                    List<UReal>... timeSeriesList
    ) {

        YIntervalSeriesCollection dataset = new YIntervalSeriesCollection();
        int nb=0;
        for(List<UReal> points : timeSeriesList){
            YIntervalSeries series = new YIntervalSeries(titles.get(nb++));
            for (int i = 0; i < timestamps.size(); i++) {
                double ts = timestamps.get(i);
                if(points.get(i) != null) {
                    double val = points.get(i).getX();
                    double dev = points.get(i).getU();
                    series.add(ts, val, val - (cl.value)*dev, val + (cl.value)*dev); //show cl% confidence
                }
            }
            dataset.addSeries(series);
        }


        return dataset;

    }


    public static ChartPlotterWithUncertainty plotResults(List<String> titles, List<Double> allTimeStamps, ConfidenceLevel cl, List<UReal>... uRealLists) {
        XYDataset dataSet = PlotHelper.createJFreeChartDataset(allTimeStamps,
                titles, cl,
                uRealLists
        );
        ChartPlotterWithUncertainty plotter = new ChartPlotterWithUncertainty("Box Temperature", "time (s)", "T (°C)", dataSet);
        plotter.pack();
        plotter.setTitle(cl+" confidence");
        RefineryUtilities.centerFrameOnScreen(plotter);
        plotter.setVisible(true);
        return plotter;
    }

    public static ChartPlotterWithUncertainty plotResults(List<String> titles, List<Double> allTimeStamps, List<UReal>... uRealLists) {
       return plotResults(titles,allTimeStamps,ConfidenceLevel.NinetyFivePercent,uRealLists);
    }

}
