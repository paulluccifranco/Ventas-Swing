/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Stock;

import Principales.Conexion;
import Principales.VerTabla;
import java.sql.ResultSet;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Franco
 */
public class TablaStock extends VerTabla{

    public void visualizarStockNormal(JTable tabla) {
        Conexion conn = new Conexion();
        ResultSet rs = conn.select("stock");
        DefaultTableModel dt = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        dt.addColumn("Nombre");
        dt.addColumn("Cantidad");
        try {
            while (rs.next()) {
                Object[] fila = new Object[2];
                fila[1] = rs.getInt(2);
                fila[0] = rs.getString(1);
                dt.addRow(fila);
            }
            tabla.setModel(dt);
            tabla.setRowHeight(20);
            DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
            tcr.setHorizontalAlignment(SwingConstants.CENTER);
            TableColumnModel columnModel = tabla.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(90);
            columnModel.getColumn(1).setPreferredWidth(40);
            columnModel.getColumn(1).setCellRenderer(tcr);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void visualizar_stock(JTable tabla) {
        Conexion conn = new Conexion();
        ConexionStock convta = new ConexionStock();
        ResultSet rs = conn.select("stock");
        DefaultTableModel dt = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        dt.addColumn("Nombre");
        dt.addColumn("Cantidad");
        try {
            String nombre = "";
            Integer descontar = 0;
            while (rs.next()) {
                descontar = 0;
                nombre = rs.getString(1);
                ResultSet rs2 = convta.contarVentas(nombre);
                while (rs2.next()) {
                    descontar = descontar + rs2.getInt(1);
                }
                Object[] fila = new Object[2];
                fila[1] = rs.getInt(2) - descontar;
                fila[0] = nombre;
                dt.addRow(fila);
            }
            tabla.setModel(dt);
            tabla.setRowHeight(20);
            DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
            tcr.setHorizontalAlignment(SwingConstants.CENTER);
            TableColumnModel columnModel = tabla.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(90);
            columnModel.getColumn(1).setPreferredWidth(40);
            columnModel.getColumn(1).setCellRenderer(tcr);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
}
