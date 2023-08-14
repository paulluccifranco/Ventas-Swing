/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas.Productos;

import Principales.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
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
public class TablasVtaProductos {

    public void visualizarVtaProductos(JTable tabla, String desde, String hasta, String name, String ven , JTextField total, JTextField cantidad, String turno, String esdecombo) {
        Conexion conn = new Conexion();
        Connection con = conn.conectar();
        ResultSet rs = null;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ventasproductos WHERE " + name + ven + turno + "fecha >= " + desde + " AND fecha <= " + hasta + esdecombo);
            rs = ps.executeQuery();
        } catch (Exception ex) {
            System.out.println("Error de consulta");
        }
        DefaultTableModel dt = new DefaultTableModel();
        dt.addColumn("Nombre");
        dt.addColumn("Cantidad");
        dt.addColumn("Total");
        dt.addColumn("Fecha");
        dt.addColumn("Tipo");
        dt.addColumn("Turno");
        Integer toti = 0;
        Integer cati = 0;
        try {
            while (rs.next()) {
                toti = toti + (Integer) rs.getObject(3);
                cati = cati + (Integer) rs.getObject(1);
                Object[] fila = new Object[6];
                fila[0] = rs.getObject(2);
                fila[1] = rs.getObject(1);
                fila[2] = rs.getObject(3);
                String formato = "dd-MM-yyyy";
                Date dsd = rs.getDate(4);
                SimpleDateFormat sdf = new SimpleDateFormat(formato);
                String ultima = String.valueOf(sdf.format(dsd));
                fila[3] = ultima;
                fila[4] = rs.getObject(5);
                fila[5] = rs.getString(6);
                dt.addRow(fila);
            }
            total.setText("$" + Integer.toString(toti));
            cantidad.setText(Integer.toString(cati));
            DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
            tcr.setHorizontalAlignment(SwingConstants.CENTER);
            tabla.setModel(dt);
            tabla.setRowHeight(60);
            TableColumnModel columnModel = tabla.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(150);
            columnModel.getColumn(0).setCellRenderer(tcr);
            columnModel.getColumn(1).setPreferredWidth(90);
            columnModel.getColumn(1).setCellRenderer(tcr);
            columnModel.getColumn(2).setPreferredWidth(74);
            columnModel.getColumn(2).setCellRenderer(tcr);
            columnModel.getColumn(3).setPreferredWidth(150);
            columnModel.getColumn(3).setCellRenderer(tcr);
            columnModel.getColumn(4).setPreferredWidth(150);
            columnModel.getColumn(4).setCellRenderer(tcr);
            columnModel.getColumn(5).setPreferredWidth(100);
            columnModel.getColumn(5).setCellRenderer(tcr);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
    public void visualizarVtaProductosDiarios(JTable tabla, String name, String ven , JTextField total, JTextField cantidad, String esdecombo) {
        Conexion conn = new Conexion();
        Connection con = conn.conectar();
        ResultSet rs = null;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM vtaproductos WHERE " + name + ven + esdecombo);
            rs = ps.executeQuery();
        } catch (Exception ex) {
            System.out.println("Error de consulta");
        }
        DefaultTableModel dt = new DefaultTableModel();
        dt.addColumn("Nombre");
        dt.addColumn("Cantidad");
        dt.addColumn("Total");
        dt.addColumn("Tipo");
        Integer toti = 0;
        Integer cati = 0;
        try {
            while (rs.next()) {
                toti = toti + (Integer) rs.getObject(3);
                cati = cati + (Integer) rs.getObject(1);
                Object[] fila = new Object[4];
                fila[0] = rs.getObject(2);
                fila[1] = rs.getObject(1);
                fila[2] = rs.getObject(3);
                fila[3] = rs.getObject(4);
                dt.addRow(fila);
            }
            total.setText("$" + Integer.toString(toti));
            cantidad.setText(Integer.toString(cati));
            DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
            tcr.setHorizontalAlignment(SwingConstants.CENTER);
            tabla.setModel(dt);
            tabla.setRowHeight(60);
            TableColumnModel columnModel = tabla.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(150);
            columnModel.getColumn(0).setCellRenderer(tcr);
            columnModel.getColumn(1).setPreferredWidth(90);
            columnModel.getColumn(1).setCellRenderer(tcr);
            columnModel.getColumn(2).setPreferredWidth(74);
            columnModel.getColumn(2).setCellRenderer(tcr);
            columnModel.getColumn(3).setPreferredWidth(150);
            columnModel.getColumn(3).setCellRenderer(tcr);
            columnModel.getColumn(4).setPreferredWidth(150);
            columnModel.getColumn(4).setCellRenderer(tcr);
            columnModel.getColumn(5).setPreferredWidth(100);
            columnModel.getColumn(5).setCellRenderer(tcr);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
