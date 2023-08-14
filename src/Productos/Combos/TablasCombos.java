/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Productos.Combos;

import Principales.Conexion;
import Principales.VerTabla;
import java.sql.ResultSet;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Franco
 */
public class TablasCombos extends VerTabla{
    
    public void visualizar_comboproducto(JTable tabla, Integer id){
        ConexionCombos conn = new ConexionCombos();
        ResultSet rs = conn.selectcomboproducto(id);
        
        
        DefaultTableModel dt = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
		return false;
            }
        };
        dt.addColumn("Nombre");
        dt.addColumn("Cantidad");

        try{
            while(rs.next()){
                Object[] fila = new Object[2];
                
                fila[1] = rs.getInt(3);
                fila[0] = rs.getString(5);
                
                
                dt.addRow(fila);
            }
            tabla.setModel(dt);
            tabla.setRowHeight(20);
            TableColumnModel columnModel = tabla.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(100);
            columnModel.getColumn(1).setPreferredWidth(30);

            
        }catch(Exception ex){
            System.out.println("Aca1");
        }
    }
    
    public void visualizar_combos(JTable tabla){
        Conexion conn = new Conexion();
        ResultSet rs = conn.select("combos");
        
        
        DefaultTableModel dt = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
		return false;
            }
        };
        dt.addColumn("Codigo");
        dt.addColumn("Nombre");
        dt.addColumn("Precio");
        dt.addColumn("Precio2");
        
        try{
            while(rs.next()){
                Object[] fila = new Object[4];
                
                fila[0] = rs.getInt(1);
                fila[1] = rs.getString(2);
                fila[2] = rs.getInt(3);
                fila[3] = rs.getInt(4);

                
                dt.addRow(fila);
            }
            tabla.setModel(dt);
            tabla.setRowHeight(40);
            TableColumnModel columnModel = tabla.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(30);
            columnModel.getColumn(1).setPreferredWidth(200);
            columnModel.getColumn(2).setPreferredWidth(50);
            columnModel.getColumn(3).setPreferredWidth(50);
            columnModel.getColumn(4).setPreferredWidth(50);
            
        }catch(Exception ex){
            System.out.println(ex);
        }
    }
    
}
