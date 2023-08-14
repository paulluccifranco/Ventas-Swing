/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Principales;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Franco
 */
public class VerTabla {

    public void visualizar_tablaventa(JTable tabla, JTextField total){
        Conexion conn = new Conexion();
        ResultSet rs = conn.select("venta WHERE mostrar != 0");
        
        
        DefaultTableModel dt = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
		return false;
            }
        };
        dt.addColumn("Cantidad");
        dt.addColumn("Nombre");
        dt.addColumn("Total");
        
        Integer toti = 0;
        
        try{
            while(rs.next()){
                Object[] fila = new Object[3];
                
                fila[0] = rs.getInt(1);
                fila[1] = rs.getString(2);
                fila[2] = rs.getInt(3);
                
                toti = toti + rs.getInt(3);
                
                dt.addRow(fila);
            }
            tabla.setModel(dt);
            total.setText(""+toti);
            
        }catch(Exception ex){
            System.out.println(ex);
        }
    }
}
