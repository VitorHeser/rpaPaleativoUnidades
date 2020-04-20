/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpaaegeaetas;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import rpaaegeaetas.unidades.R1.R1;
import rpaaegeaetas.unidades.R2Prolagos.R2_Prolagos;
import rpaaegeaetas.unidades.R3.R3_e_Sul;

/**
 *
 * @author vitor.heser
 */
public class RpaAegeaETAs {

    /**
     * @param args the command line arguments
     */
    public static String TextoAntigo = null;
    public static SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd"); 
    public static void main(String[] args) throws SQLException, ClassNotFoundException, Exception {
        
        String primeiradata = lercsv("arquivo - Copia.csv");
        String stringdata = adicionaUmDia(primeiradata== null ? "2019-12-31" : primeiradata);
        
        Date data = new Date();
        String datahoje = formato.format(data);
        
        
        PlotarCsv pcsv = new PlotarCsv("arquivo - Copia.csv");
        if(TextoAntigo !=null){
            pcsv.PlotarCsv(TextoAntigo);
        }
        
        while(!datahoje.equals(stringdata)){

            System.out.println("\n========================================================\nDATA: "+stringdata);

            R1 r1 = new R1();
            r1.R1(stringdata,pcsv);
            
            R2_Prolagos prl = new R2_Prolagos();
            prl.prolagos(stringdata,pcsv);
            
            R3_e_Sul sul = new R3_e_Sul();
            sul.sul(stringdata,pcsv);
            
            stringdata = adicionaUmDia(stringdata);
        }
        
    }
    public static String lercsv(String csv) throws IOException{
        String data = null;
        String data2 = null;
        
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(csv)));
            String linha = null;
            while ((linha = reader.readLine()) != null) {
                String[] dadosUsuario = linha.split(";");
                data2 =dadosUsuario[2];
            }
            
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(csv)));
            linha = null;
            
            while ((linha = reader.readLine()) != null) {
                String[] dadosUsuario = linha.split(";");
                if(dadosUsuario[2].equals(data2)){
                    break;
                }
                TextoAntigo = TextoAntigo==null
                                ?                   dadosUsuario[0]+";"+dadosUsuario[1]+";"+dadosUsuario[2]+";"+dadosUsuario[3]+";"+dadosUsuario[4]
                                : TextoAntigo +"\n"+dadosUsuario[0]+";"+dadosUsuario[1]+";"+dadosUsuario[2]+";"+dadosUsuario[3]+";"+dadosUsuario[4];
                    data =dadosUsuario[2];
            }
            reader.close();

        }catch(Exception e){
            
        }
        return data;
    }
    
    public static String adicionaUmDia(String stringdata) throws ParseException {
        Date data = formato.parse(stringdata);
        Calendar c = Calendar.getInstance();//Instancia a classe Calendar.
        c.setTime(data);//Altera a data atual,pela sua data
        c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH)+1);//Altera a data para +1 dia.
        return formato.format(c.getTime());
    }
}
