/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpaaegeaetas.unidades.R1;

import java.io.BufferedWriter;
import rpaaegeaetas.unidades.R1.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import rpaaegeaetas.PlotarCsv;

/**
 *
 * @author vitor.heser
 */

public class R1 {
    private final ConnectionFactory conn = new ConnectionFactory();
    private PlotarCsv pcsv = null;
    public void R1(String data, PlotarCsv pcsv) throws ClassNotFoundException, SQLException, Exception{
        this.pcsv = pcsv;
        String url = 
                "SELECT "+ 
                    "b.name as unidade," +
                    "b.id as idwater "
            +"from pressure_entry_city a "
            +"JOIN water_quality_unit b ON  a.id = b.city_id "
            +"WHERE b.name like 'ETA %' "
            +"ORDER BY b.id asc";
        PreparedStatement ps = (PreparedStatement) conn.getConnectionPOSTGRES().prepareStatement(url);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            String unidade =  rs.getString("unidade");
            String idwater =  rs.getString("idwater");
            Indicadores(unidade, idwater,data);
        }
        
        
    }
    public String Indicadores(String unidade, String idwater, String data)throws Exception {
        String tb = null;
        String url = 
//                "SELECT TOP 1 "
            "SELECT * FROM water_quality_complexsample "
            + "WHERE date_entry BETWEEN  '"+ data +" 00:00:00' AND '"+ data +" 23:59:59' "
            + "AND unit_id = "+idwater+" "
            + "ORDER BY date_entry ASC" ;
        ResultSet rs;
        PreparedStatement ps = (PreparedStatement) conn.getConnectionPOSTGRES().prepareStatement(url);
        rs = ps.executeQuery();
        
        Double cloro =null;
        Double cor =null;
        Double turbidez =null;
        while(rs.next()){
            Double cloro2 =rs.getDouble("residual_chlorine");
            Double cor2 =rs.getDouble("treated_color");
            Double turbidez2 =rs.getDouble("filtered_turbidity");
            
            Integer quality = cloro2 == 0 ? 0 : 192;
            cloro = Calculo(quality,cloro2,cloro);
            
            quality = cor2 == 0 ? 0 : 192;
            cor = Calculo(quality,cor2,cor);
            
            quality = turbidez2 == 0 ? 0 : 192;
            turbidez = Calculo(quality,turbidez2,turbidez);
        }  
        if(cloro!=null || cor !=null || turbidez !=null){            
            if(cor!=null){
                pcsv.PlotarCsv("R1;"+unidade+";"+data+";Cor;"+cor.toString().replaceAll("\\.",","));
            }
            if(cloro!=null){
                pcsv.PlotarCsv("R1;"+unidade+";"+data+";Cloro;"+cloro.toString().replaceAll("\\.",","));
            }
            if(turbidez!=null){
                pcsv.PlotarCsv("R1;"+unidade+";"+data+";Turbidez;"+turbidez.toString().replaceAll("\\.",","));
            }
        }
        rs.close();
        return tb;
    }
    
    private Double Calculo(Integer quality, Double valor, Double Antigo){
        Double novo = null;
        if(quality==192){
            novo =  Antigo ==null ? valor : (Antigo + valor)/2;
        }else{
            novo =  Antigo;
        }
        return novo;
    }
}
