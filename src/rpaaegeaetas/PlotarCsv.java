/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpaaegeaetas;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author vitor.heser
 */
public class PlotarCsv {
    BufferedWriter writer;
           
    public PlotarCsv(String Caminho) throws IOException{
        FileWriter relatorio = new FileWriter(new File(Caminho));
        writer = new BufferedWriter(relatorio);
    }
    public void PlotarCsv(String Linha) throws IOException{
        writer.write(Linha);
        writer.newLine();
        writer.flush();
        
    }
    
}
