/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpaaegeaetas.unidades.R2Prolagos;

import java.io.BufferedWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import rpaaegeaetas.PlotarCsv;

/**
 *
 * @author vitor.heser
 */
public class R2_Prolagos {
    private final ConnectionFactory conn = new ConnectionFactory();
    private PlotarCsv pcsv = null;
    
    public void prolagos(String data, PlotarCsv pcsv) throws ClassNotFoundException, SQLException, Exception{
        this.pcsv = pcsv;
        String unidade = "ETA JUTURNAÍBA";
//        Indicadores(unidade, data);
        
        IndicadoresDiarios(unidade,43, "Cor" ,data);
        IndicadoresDiarios(unidade,30, "Cloro" ,data);
        IndicadoresDiarios(unidade,34, "Turbidez" ,data);
        
    }
    public String Indicadores(String unidade,String data)throws Exception {
        String tb = null;
        String url = 
//                "SELECT TOP 1 "
                "SELECT "
                + "E3TimeStamp, "
                + "ETA_Tratada_Cloro, "
                + "ETA_Tratada_Cor, "
                + "ETA_Tratada_Turbidez, "
                + "ETA_Tratada_Cloro_Quality, "
                + "ETA_Tratada_Cor_Quality, "
                + "ETA_Tratada_Turbidez_Quality "
            + "FROM Qualidade_ETA WHERE E3TimeStamp BETWEEN  '"+ data +" 00:00:00' AND '"+ data +" 23:59:59' ORDER BY E3TimeStamp ASC" ;
        ResultSet rs;
        PreparedStatement ps = (PreparedStatement) conn.getConnectionSQL().prepareStatement(url);
        rs = ps.executeQuery();
        
        Double cloro =null;
        Double cor =null;
        Double turbidez =null;
        
        while(rs.next()){
            cloro = Calculo(rs.getInt("ETA_Tratada_Cloro_Quality"),rs.getDouble("ETA_Tratada_Cloro"),cloro);
            cor = Calculo(rs.getInt("ETA_Tratada_Cor_Quality"),rs.getDouble("ETA_Tratada_Cor"),cor);
            turbidez = Calculo(rs.getInt("ETA_Tratada_Turbidez_Quality"),rs.getDouble("ETA_Tratada_Turbidez"),turbidez);

        }
        if(cor!=null){
            pcsv.PlotarCsv("R2;"+unidade+";"+data+";Cor;"+cor.toString().replaceAll("\\.",","));
        }
        if(cloro!=null){
            pcsv.PlotarCsv("R2;"+unidade+";"+data+";Cloro;"+cloro.toString().replaceAll("\\.",","));
        }
        if(turbidez!=null){
            pcsv.PlotarCsv("R2;"+unidade+";"+data+";Turbidez;"+turbidez.toString().replaceAll("\\.",","));
        }
        
        rs.close();
        return tb;
    }
    
    public String IndicadoresDiarios(String unidade, Integer indicador, String indicador2, String data)throws Exception {
        String tb = null;
        String url = 
//                "SELECT TOP 1 "
                "SELECT "
                + "realizado "
            + "FROM sispc.appweb_ind_exeindicadores "
                + "WHERE dataindicador BETWEEN  '"+ data +" 00:00:00' AND '"+ data +" 23:59:59' "
                + "AND fk_indicador_id = "+indicador+" "
                + "ORDER BY dataindicador ASC" ;
        ResultSet rs;
        PreparedStatement ps = (PreparedStatement) conn.getConnectionMySql().prepareStatement(url);
        rs = ps.executeQuery();
        
        Double realizado =null;
        
        while(rs.next()){
            realizado = Calculo(192,rs.getDouble("realizado"),realizado);
        }
        if(realizado!=null){
            pcsv.PlotarCsv("R2;"+unidade+";"+data+";"+indicador2+";"+realizado.toString().replaceAll("\\.",","));
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
