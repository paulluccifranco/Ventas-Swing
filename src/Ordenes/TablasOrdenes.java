/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ordenes;

import Principales.Conexion;
import java.sql.ResultSet;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Franco
 */
public class TablasOrdenes {

    public void visualizar_ordenes(JTable tabla, String tipo, Integer orden) {
        Conexion conn = new Conexion();
        ResultSet rs = conn.select("ordenes");
        DefaultTableModel dt = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        dt.addColumn("Numero");
        dt.addColumn("Tipo");
        dt.addColumn("Medio de pago");
        dt.addColumn("Total");
        Integer mpago = 0;
        try {
            while (rs.next()) {
                Object[] fila = new Object[4];
                fila[0] = rs.getInt(5);
                fila[1] = rs.getString(1);
                mpago = rs.getInt(4);
                fila[3] = rs.getInt(2);
                if (mpago == 1) {
                    fila[2] = "Efectivo";
                }
                if (mpago == 2) {
                    fila[2] = "Tarjeta";
                }
                if (mpago == 3) {
                    fila[2] = "MPago";
                }
                dt.addRow(fila);
            }
            tabla.setModel(dt);
            tabla.setRowHeight(20);
            TableColumnModel columnModel = tabla.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(30);
            columnModel.getColumn(1).setPreferredWidth(50);
            columnModel.getColumn(2).setPreferredWidth(50);
            columnModel.getColumn(3).setPreferredWidth(50);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
    public void visualizar_ordenesFiltro(JTable tabla, Integer tipo, Integer orden) {
        ConexionOrdenes conn = new ConexionOrdenes();
        ResultSet rs = conn.selectFiltros(tipo, orden);
        DefaultTableModel dt = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        dt.addColumn("Numero");
        dt.addColumn("Tipo");
        dt.addColumn("Medio de pago");
        dt.addColumn("Total");
        Integer mpago = 0;
        try {
            while (rs.next()) {
                Object[] fila = new Object[4];
                fila[0] = rs.getInt(5);
                fila[1] = rs.getString(1);
                mpago = rs.getInt(4);
                fila[3] = rs.getInt(2);
                if (mpago == 1) {
                    fila[2] = "Efectivo";
                }
                if (mpago == 2) {
                    fila[2] = "Tarjeta";
                }
                if (mpago == 3) {
                    fila[2] = "MPago";
                }
                dt.addRow(fila);
            }
            tabla.setModel(dt);
            tabla.setRowHeight(20);
            TableColumnModel columnModel = tabla.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(30);
            columnModel.getColumn(1).setPreferredWidth(50);
            columnModel.getColumn(2).setPreferredWidth(50);
            columnModel.getColumn(3).setPreferredWidth(50);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
}
