/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Productos.Unidades;

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
public class TablaUnidades extends VerTabla{

    public void visualizar_unidades(JTable tabla) {
        Conexion conn = new Conexion();
        ResultSet rs = conn.select("productos");
        DefaultTableModel dt = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        dt.addColumn("Codigo");
        dt.addColumn("Nombre");
        dt.addColumn("Tipo");
        dt.addColumn("Precio");
        dt.addColumn("Precio2");
        try {
            while (rs.next()) {
                Object[] fila = new Object[5];
                fila[0] = rs.getInt(1);
                fila[1] = rs.getString(2);
                Integer tipo = rs.getInt(4);
                fila[3] = rs.getInt(3);
                fila[4] = rs.getInt(5);
                if (tipo == 0) {
                    fila[2] = "Unidades";
                } else if (tipo == 1) {
                    fila[2] = "Bebidas";
                }
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
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
}
