/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rms;

/**
 *
 * @author rajat
 */
public class Splash_Load {
    public static void main(String args[])
    {
        Splash obj = new Splash();
        obj.setVisible(true);
        try{
        for(int i=0;i<=100;i++)
        {
            Thread.sleep(55);
            obj.jLabel3.setText(Integer.toString(i)+" %");
            if(i==100)
            {
                
                obj.setVisible(false);
                Welcome.main(new String[]{});
            }
        }
        }
        catch(Exception e)
        {
        }
    }
    
}
