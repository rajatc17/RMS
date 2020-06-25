/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rms;
import java.awt.BorderLayout;
import java.util.Locale;
import javax.swing.*;
import org.jfree.chart.*;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.chart.plot.*;
import org.jfree.util.Rotation;

/**
 *
 * @author rajat
 */
public class CreateChart {
    
    public CreateChart(String title,JPanel parent)
    {
        PieDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset,title);
        ChartPanel cp = new ChartPanel(chart);
        cp.setPreferredSize(new java.awt.Dimension(500,500));
        cp.setVisible(true);
        parent.add(cp,BorderLayout.CENTER);
        parent.repaint();
        parent.validate();
       
    }
    public  PieDataset createDataset()
    {
        DefaultPieDataset data = new DefaultPieDataset();
        data.setValue("EMINEM", 90);
        data.setValue("OTHER", 10);
        return data;
    }
    public JFreeChart createChart(PieDataset dataset,String title)
    {
        JFreeChart chart = ChartFactory.createPieChart3D(title, dataset, true, true, Locale.ITALY);
        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(0);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(1f);
        
        return chart;
    }
}
