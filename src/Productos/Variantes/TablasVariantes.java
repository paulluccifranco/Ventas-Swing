/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Productos.Variantes;

import Principales.Conexion;
import java.sql.ResultSet;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Franco
 */
public class TablasVariantes {

    public void visualizar_variantes(JTable tabla, String nombre, Integer categoria) {
        Conexion conn = new Conexion();
        ResultSet rs = conn.select("variantes");
        DefaultTableModel dt = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        dt.addColumn("Codigo");
        dt.addColumn("Nombre");
        dt.addColumn("Tipo");
        try {
            while (rs.next()) {
                Object[] fila = new Object[3];
                fila[0] = rs.getInt(1);
                fila[1] = rs.getString(2);
                Integer tipo = rs.getInt(3);
                if (tipo == 0) {
                    fila[2] = "Ensalada";
                } else if (tipo == 1) {
                    fila[2] = "Salsa";
                }
                dt.addRow(fila);
            }
            tabla.setModel(dt);
            tabla.setRowHeight(40);
            TableColumnModel columnModel = tabla.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(30);
            columnModel.getColumn(1).setPreferredWidth(200);
            columnModel.getColumn(2).setPreferredWidth(50);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
}
