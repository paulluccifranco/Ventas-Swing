
package Principales;

import java.awt.Font;
import java.io.FileInputStream;
import javax.print.Doc;

import javax.print.DocFlavor;

import javax.print.DocPrintJob;

import javax.print.PrintService;

import javax.print.PrintServiceLookup;

import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;


public class Ticket{


    
    public PrintService impresora(String nombre){
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null); //Obtenemos los servicios de impresion del sistema 
        for (PrintService impresora : printServices){ //Recorremos el array de servicios de impresion
            if(impresora.getName().contentEquals(nombre)){ // Si el nombre del servicio es el mismo que el que buscamos
                return impresora; // Nos devuelve el servicio 
            }
        }
        return null;    // Si no lo encuentra nos devuelve un null
    }

    
    

    public static void ImprimirDocumento(String cadena){
        
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;

        Ticket ticket = new Ticket();
        PrintService service = ticket.impresora("tickets");
        
        DocPrintJob pj = service.createPrintJob();


        byte[]bytes =cadena.getBytes();

        Doc doc = new SimpleDoc(bytes, flavor,null);

        try{

        pj.print(doc,null);

        }catch(Exception e){ }

        }
    
    
    public static void tamañoletra(byte[]bytes){
        
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;

        Ticket ticket = new Ticket();
        PrintService service = ticket.impresora("tickets");
        
        DocPrintJob pj = service.createPrintJob();

        Doc doc = new SimpleDoc(bytes, flavor,null);

        try{

        pj.print(doc,null);

        }catch(Exception e){ }

        }

}