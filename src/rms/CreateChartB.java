/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rms;
import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Locale;
import javax.swing.*;
import org.jfree.chart.*;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.chart.plot.*;
import org.jfree.util.Rotation;
import java.util.*;


/**
 *
 * @author rajat
 */
public class CreateChartB implements Connectivity{
    
    ArrayList<String> icodes = new ArrayList<>();
    Map<String,Integer> code_count = new HashMap<String,Integer>();
    Map<String,String> iname = new HashMap<String,String>();
    
    public CreateChartB(String title,JPanel parent)
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
    public void init_icodes()
    {
        try
        { 
            String str=null;
            String name=null;
            int qty=0;
            Connection cn=Connectivity.getConnection();
            PreparedStatement ps=cn.prepareStatement("select ICode,QTY,Description from orderdetails where Type='Beverage'");
            ResultSet rs=ps.executeQuery();
            while(rs.next())
            {
                str=rs.getString("ICode");
                qty=rs.getInt("QTY");
                name=rs.getString("Description");
                
                if(code_count.containsKey(str))
                    code_count.put(str,code_count.get(str)+qty);
                else
                {
                    code_count.put(str,qty);
                    iname.put(str,name);
                }
            }
            this.code_count = code_count;
            
        }
        
        catch(Exception e)
        {   
            JOptionPane.showMessageDialog(null,e);
        }
    }
    public DefaultPieDataset set_data(DefaultPieDataset data)
    {
        for (Map.Entry<String, Integer> entry : code_count.entrySet()) 
        {
                data.setValue(iname.get(entry.getKey()), entry.getValue());
        }
        
        return data;
    }
    public  PieDataset createDataset()
    {
        DefaultPieDataset data = new DefaultPieDataset();
        init_icodes();
        data = set_data(data);
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
