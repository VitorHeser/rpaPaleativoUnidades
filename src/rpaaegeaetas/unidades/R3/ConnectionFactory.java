/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpaaegeaetas.unidades.R3;

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

    public Connection getConnectionDestino() throws ClassNotFoundException, SQLException{
        Connection connection=null;
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://DSKPRL013862:3306/sispc","sispcAPI", "Prol@go55i5PC@pi");
        System.out.println(connection.getClass());
        return connection;
    }
    
    public Connection getConnectionPOSTGRES() throws SQLException, ClassNotFoundException {
         String url ="jdbc:postgresql://10.10.60.44:5432/intranet";
        String usuario="aegea.tableau";
        String senha = "cMJNoUL7u6zvI47";

        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(url,usuario,senha);
        
    }
}
