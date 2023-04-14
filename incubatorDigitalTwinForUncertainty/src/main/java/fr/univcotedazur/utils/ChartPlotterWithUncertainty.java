package fr.univcotedazur.utils;


import java.awt.*;
import java.util.Arrays;
import java.util.List;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DeviationRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleInsets;

    /**
     * A simple deviation renderer based on the {@link DeviationRenderer} class.
     */
    public class ChartPlotterWithUncertainty extends ApplicationFrame {

//        static List<Color> allColors = Arrays.asList(
//                new Color(255,0, 0),
//                new Color(0,   255, 0),
//                new Color(0,   0,   255),
//                new Color(255, 255, 0),
//                new Color(255, 0,   255),
//                new Color(0,   255, 255),
//                new Color(255, 255, 255),
//                new Color(0,   0,   0),
//                new Color(128, 128, 128),
//                new Color(192, 192, 192),
//                new Color(64,  64,  64),
//                new Color(255, 175, 175),
//                new Color(255, 200, 0)
//        );


        static List<Color> allColors = Arrays.asList(
                new Color(255,0, 0),
                new Color(0,   255, 0),
                new Color(0,   0,   255),
                new Color(229, 39, 140),
                new Color(88, 219, 22),
                new Color(33, 158, 248),
                new Color(128, 255, 255),
                new Color(0,   0,   0),
                new Color(128, 128, 128),
                new Color(192, 192, 192),
                new Color(64,  64,  64),
                new Color(255, 175, 175),
                new Color(255, 200, 0)
        );

        static List<Float> dasheStyles = Arrays.asList(
                0.0f, 5.0f, 10.0f
        );



        public JFreeChart chart = null;

        public ChartPlotterWithUncertainty(String title, String XLabel, String YLabel, XYDataset dataset) {
            super(title);
            chart = createChart(title, XLabel, YLabel, dataset);
            JPanel chartPanel = new ChartPanel(chart);
            //chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
            setContentPane(chartPanel);
        }


        /**
         * Creates a jfree chart.
         *
         * @param dataset  the data for the chart.
         * @return a chart.
         */
        public static JFreeChart createChart(String title, String XLabel, String YLabel, XYDataset dataset) {

            // create the chart...
            JFreeChart chart = ChartFactory.createXYLineChart(
                    title,XLabel,YLabel, dataset,
                    PlotOrientation.VERTICAL,
                    true,                     // include legend
                    true,                     // tooltips
                    false                     // urls
            );

            chart.setBackgroundPaint(Color.white);

            // get a reference to the plot for further customisation...
            XYPlot plot = (XYPlot) chart.getPlot();
            plot.setBackgroundPaint(Color.white);
            plot.setAxisOffset(new RectangleInsets(1.0, 1.0, 1.0, 1.0));
            plot.setDomainGridlinePaint(Color.lightGray);

            DeviationRenderer renderer = new DeviationRenderer(true, false);
            for(int sc = 0; sc < dataset.getSeriesCount(); sc++) {
                renderer.setSeriesStroke(sc, new BasicStroke(1.6f, BasicStroke.CAP_ROUND,
                        BasicStroke.JOIN_ROUND));//, 1.0f, new float[] {dasheStyles.get(sc%3), 4.0f}, 0.0f));
                renderer.setSeriesPaint(sc, allColors.get(sc));
                renderer.setSeriesFillPaint(sc, allColors.get(sc));
            }
            Font customFont = new Font("Helvetica", Font.BOLD, 25);
            plot.getDomainAxis().setLabelFont(customFont);
            plot.getRangeAxis().setLabelFont(customFont);
            plot.getDomainAxis().setTickLabelFont(customFont);
            plot.getRangeAxis().setTickLabelFont(customFont);
            renderer.setBaseLegendTextFont(customFont);
            plot.setRenderer(renderer);

            // change the auto tick unit selection to integer units only...
            NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
            yAxis.setAutoRangeIncludesZero(false);
            yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

            return chart;

        }


    }



