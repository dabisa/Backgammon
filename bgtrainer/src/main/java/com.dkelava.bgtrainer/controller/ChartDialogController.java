package com.dkelava.bgtrainer.controller;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;

import javax.swing.JDialog;

public class ChartDialogController {

    private JDialog dialog;
    private XYDataset dataSet;

    public ChartDialogController(XYDataset dataSet) {
        this.dataSet = dataSet;
    }

    public void show() {
        if(dialog == null) {
            constructDialog();
        }
        dialog.setVisible(true);
    }

    private void constructDialog() {
        final JFreeChart chart = ChartFactory.createXYLineChart(
                "Player statistics",
                "Examples",
                "Win Frequency",
                dataSet,
                PlotOrientation.VERTICAL,
                true, true, false);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setMouseZoomable(true, false);
        dialog = new JDialog();
        dialog.setContentPane(chartPanel);
        dialog.setModal(false);
        dialog.pack();
    }
}
