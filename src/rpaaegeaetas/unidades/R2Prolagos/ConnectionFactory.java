/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpaaegeaetas.unidades.R2Prolagos;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author vitor.heser
 */



public class ConnectionFactory {
    
public Statement stm;
public ResultSet rs;

    public Connection getConnectionMySql() throws ClassNotFoundException, SQLException{
        Connection connection=null;
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://SISPCPRL01:3306/sispc?autoReconnect=true&useSSL=false","sispcRPA", "Prol@go55i5PCRp@s");
        return connection;
    }
    
    public Connection getConnectionSQL() throws SQLException, ClassNotFoundException {
        Connection conn = null;
 
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        return DriverManager.getConnection("jdbc:sqlserver://172.20.64.4;user=aegea.prolagos;password=aegea.prolagos"); 
    }
}
