/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rms;

import java.sql.*;

/**
 *
 * @author RAJAT
 */
public interface Connectivity 
{
    default Statement connectdata() throws Exception
    {
        try{  
                Class.forName("com.mysql.cj.jdbc.Driver");  
                Connection con=DriverManager.getConnection(  
                "jdbc:mysql://root@localhost/dominos","root","root");  //here master is database name, root is username and password  
                Statement stmt=con.createStatement();  
                return stmt;
            }
        catch(Exception e)
        {
            throw e;
        }  
    }
    static Connection getConnection()
    {
		Connection con=null;
		try{
			Class.forName("com.mysql.cj.jdbc.Driver");
			con=DriverManager.getConnection("jdbc:mysql://root@localhost/dominos","root","root");
		}catch(Exception e){System.out.println(e);}
		return con;
    }
}
