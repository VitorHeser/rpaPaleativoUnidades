/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpaaegeaetas.unidades.R3;

import java.io.BufferedWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import rpaaegeaetas.PlotarCsv;

/**
 *
 * @author vitor.heser
 */
public class R3_e_Sul {
    private final ConnectionFactory conn = new ConnectionFactory();
    private PlotarCsv pcsv = null;
    
    public void sul(String data, PlotarCsv pcsv) throws ClassNotFoundException, SQLException, Exception{
        this.pcsv = pcsv;
        String url = 
                "SELECT "
                + "* "
            + "FROM eta_eta ";
        PreparedStatement ps = (PreparedStatement) conn.getConnectionPOSTGRES().prepareStatement(url);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            String unidade =  rs.getString("name");
            Indicadores(unidade,data);
        }
        
        
    }
    public String Indicadores(String unidade, String data)throws Exception {
        String tb = null;
        String url = 
//                "SELECT TOP 1 "
                "SELECT "
                + "* "
            + "FROM eta_overallquality a "
            + "INNER JOIN eta_eta b ON a.eta_id = b.id "
                + "WHERE Sample_Time BETWEEN  '"+ data +" 00:00:00' AND '"+ data +" 23:59:59' "
                + "AND b.name like '"+unidade+"' "
                + "ORDER BY b.name, a.Sample_Time ASC" ;
        ResultSet rs;
        PreparedStatement ps = (PreparedStatement) conn.getConnectionPOSTGRES().prepareStatement(url);
        rs = ps.executeQuery();
        
        Double cloro =null;
        Double cor =null;
        Double turbidez =null;
        while(rs.next()){            
            cloro = Calculo(192,rs.getDouble("chlorine"),cloro);
            cor = Calculo(192,rs.getDouble("color_treated"),cor);
            turbidez = Calculo(192,rs.getDouble("Treated_Turbidity"),turbidez);
        }  
        if(cor!=null){
            pcsv.PlotarCsv("R3;"+unidade+";"+data+";Cor;"+cor.toString().replaceAll("\\.",","));
        }
        if(cloro!=null){
            pcsv.PlotarCsv("R3;"+unidade+";"+data+";Cloro;"+cloro.toString().replaceAll("\\.",","));
        }
        if(turbidez!=null){
            pcsv.PlotarCsv("R3;"+unidade+";"+data+";Turbidez;"+turbidez.toString().replaceAll("\\.",","));
        }
        
        rs.close();
        return tb;
    }
    
    private Double Calculo(Integer quality, Double valor, Double Antigo){
        Double novo = null;
        if(quality==192){
            novo =  Antigo ==null ? valor : (Antigo + valor)/2;
        }
        return novo;
    }
}
