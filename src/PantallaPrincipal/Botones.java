/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PantallaPrincipal;

import Principales.Conexion;
import Principales.FiltrosTextField;
import Principales.RequestFocusListener;
import Principales.Ticket;
import Principales.VerTabla;
import Ordenes.ConexionOrdenes;
import Stock.TablaStock;
import Ventas.Productos.ConexionVtaProductos;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 *
 * @author Franco
 */
public class Botones {
    
    Conexion conn = new Conexion();
    ConexionVtaProductos convta = new ConexionVtaProductos();
    String ultimo = "";
    TablaStock vStock = new TablaStock();
    VerTabla v = new VerTabla();
    ConexionOrdenes ord = new ConexionOrdenes();
    FiltrosTextField filtros = new FiltrosTextField();
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    byte[] bb = new byte[]{0x1D, 0x21, 0x35};
    
    byte[] normalText = new byte[]{0x1D, 0x21, 0x00};
    byte[] normalSize = new byte[]{0x1D, 0x21, 0x00};
    byte[] largeSize = new byte[]{0x1D, 0x21, 0x11};
    byte[] boldOn = new byte[]{0x1b, 0x45, 0x01};
    byte[] boldOff = new byte[]{0x1b, 0x45, 0x00};
    byte[] doubleWidth = new byte[]{0x1b, 0x21, 0x20};
    byte[] doubleHeight = new byte[]{0x1b, 0x21, 0x10};
    byte[] left = new byte[]{0x1b, 0x61, 0x00};
    byte[] rigth = new byte[]{0x1b, 0x61, 0x02};
    byte[] center = new byte[]{0x1b, 0x61, 0x01};
    byte[] imagen = new byte[]{0x1d, 0x76, 0x30, 0x00};
    
    
    
    public void venta(Integer mediodepago, JTextField txtTotal, JTextField txtOrden, JTextField txtDelivery, JTextField txtVntasDia, JTextField txtVntasDelivery, JTextField txtVntasOtros, JTextField txtElturno, Date fecha22, Integer pedidosya, JButton pedidosyabtn, JTable tabla, JScrollPane jScrollPane1, JTable tStockeo) throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

            if (txtTotal.getText().equals("0")) {
                JOptionPane.showMessageDialog(null, "Debe cargar un articulo primero", "Error al aceptar", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Integer totaltext = Integer.parseInt(txtTotal.getText());
            Integer orden = Integer.parseInt(txtOrden.getText());
            orden++;

        out.write(boldOn);
        out.write(center);
        out.write(largeSize);
        out.write("Pinto el pollo\n".getBytes());
        out.write(bb);
        out.write(("Numero:\n" + orden + "\n========\n").getBytes());
        out.write(normalText);
        out.write(doubleHeight);
        out.write(left);

            String cadena = "";
            String detalle = "";
            ResultSet rs = conn.select("venta");

            try {
                while (rs.next()) {
                    Integer cantidad = rs.getInt(1);
                    String articulo = rs.getString(2);
                    Integer total = rs.getInt(3);
                    Integer mostrar = rs.getInt(4);

                    String tipi;

                    tipi = "" + articulo;

                    if (articulo.length() > 29) {
                        articulo = articulo.substring(0, 25);
                    }
                    convta.guardarventa(cantidad, articulo, total, "local", Integer.parseInt(txtOrden.getText()));

                    if(mostrar == 1){
                        cadena = cadena + cantidad + ":" + tipi + ".....$" + total + "\n";
                    }else if(mostrar == 2){
                        detalle = detalle + cantidad + ":" + tipi + "\n";
                    }
                    
                }
                
            } catch (Exception ex) {
                System.out.println(ex);
            }
        out.write((cadena+"\nEspecificaciones: \n"+detalle).getBytes());
        out.write(("\n================================\n").getBytes());
        out.write(boldOn);
        out.write(largeSize);
        out.write(center);
        out.write(("Total: $" + totaltext).getBytes());
        out.write(normalText);
        out.write(doubleHeight);
        out.write(("\n\n\n Fecha: "+dateFormat.format(fecha22)+"\n\n\n\n\n\n\n").getBytes());
        Ticket.ImprimirDocumento(out.toByteArray());


            ultimo = "Numero:\n" + "  " + orden + "\n========\n" + cadena +"\nEspecificaciones: \n"+detalle+ "\n=======\nTotal: $" + totaltext + "\n\n\n Fecha: "+dateFormat.format(fecha22)+"\n\n\n\n\n";

            String pedido = "local";
            if (pedidosya == 5) {
                pedido = "pedidosya";
                pedidosyabtn.doClick();
            }
            ord.guardarOrden(pedido, totaltext, mediodepago, orden, ultimo);

            tabla.setEnabled(false);
            jScrollPane1.setViewportView(tabla);

            conn.settotal( txtOrden,  txtDelivery,  txtVntasDia,  txtVntasDelivery,  txtVntasOtros);
            conn.vaciar("venta");
            v.visualizar_tablaventa(tabla, txtTotal);
            vStock.visualizar_stock(tStockeo);
    }
    
    
    
    
    
    public void delivery(Integer mediodepago, JTextField txtTotal, JTextField txtOrden, JTextField txtDelivery, JTextField txtVntasDia, JTextField txtCosto,JTextField txtVntasDelivery, JTextField txtVntasOtros, JTextField txtElturno, Date fecha22, Integer pedidosya, JButton pedidosyabtn, JTable tabla, JScrollPane jScrollPane1, JTable tStockeo, String TelNumero) throws IOException {
            if (txtTotal.getText().equals("0")) {
                JOptionPane.showMessageDialog(null, "Debe cargar un articulo primero", "Error al aceptar", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Integer totaltext = Integer.parseInt(txtTotal.getText());
            Integer orden = Integer.parseInt(txtDelivery.getText());
            Integer preciodelivery = Integer.parseInt(txtCosto.getText());
            totaltext = totaltext + preciodelivery;
            orden++;

            JTextField txTelefono = new JTextField(20);
            txTelefono.addAncestorListener(new RequestFocusListener());
            JTextField txNombre = new JTextField(20);
            txNombre.addAncestorListener(new RequestFocusListener());
            JTextField txDireccion = new JTextField(20);
            JTextField txDescripcion = new JTextField(20);
            JTextField cField = new JTextField(20);
            txTelefono.setText(TelNumero);
            JTextField txHora = new JTextField(20);

            JPanel myTelefono = new JPanel();
            myTelefono.add(new JLabel("Telefono:"));
            myTelefono.add(txTelefono);
            filtros.filtroeneteros(txTelefono);
            filtros.filtrotamaño(txTelefono, 14);
            filtros.filtrotamaño(txHora, 5);
            filtros.filtrotamaño(txNombre, 25);
            filtros.filtrotamaño(txDireccion, 50);
            filtros.filtrotamaño(txDescripcion, 50);
            filtros.filtroeneteros(cField);

            int confirm = JOptionPane.showConfirmDialog(null, myTelefono, "Telefono", JOptionPane.OK_CANCEL_OPTION);

            if (confirm != 0) {
                conn.vaciar("venta");
                v.visualizar_tablaventa(tabla, txtTotal);

                tabla.setEnabled(false);
                jScrollPane1.setViewportView(tabla);
                return;
            }

            String telefono = "0";

            if (txTelefono.getText().equals("")) {
                telefono = "0";
            } else {
                telefono = txTelefono.getText();
            }

            ResultSet cliente = conn.selectcliente(telefono);
            try {
                while (cliente.next()) {
                    txNombre.setText(cliente.getString(2));
                    txDireccion.setText(cliente.getString(3));
                    txDescripcion.setText(cliente.getString(4));
                }
            } catch (SQLException ex) {
                System.out.println("Fallo");
            }

            JPanel myPanelPrincipal = new JPanel();
            myPanelPrincipal.setLayout(new BoxLayout(myPanelPrincipal, BoxLayout.Y_AXIS));
            myPanelPrincipal.add(new JLabel("Nombre:"));
            myPanelPrincipal.add(txNombre);
            txNombre.requestFocusInWindow();
            myPanelPrincipal.add(new JLabel("Direccion:"));
            myPanelPrincipal.add(txDireccion);
            myPanelPrincipal.add(new JLabel("Descripcion:"));
            myPanelPrincipal.add(txDescripcion);
            myPanelPrincipal.add(new JLabel("Hora de entrega:"));
            myPanelPrincipal.add(txHora);
            myPanelPrincipal.add(new JLabel("Con cuanto abona:"));
            myPanelPrincipal.add(cField);
            JCheckBox estapago = new JCheckBox("Esta Pago");
            myPanelPrincipal.add(estapago);

            int conf = JOptionPane.showConfirmDialog(null, myPanelPrincipal, "Informacion de delivery", JOptionPane.OK_CANCEL_OPTION);

            if (conf != 0) {
                conn.vaciar("venta");
                v.visualizar_tablaventa(tabla, txtTotal);
                tabla.setEnabled(false);
                jScrollPane1.setViewportView(tabla);
                return;
            }

            conn.guardarcliente(telefono, txNombre.getText(), txDireccion.getText(), txDescripcion.getText());


            String cadena = "";
            String detalle = "";
            ResultSet rs = conn.select("venta");


            try {
                while (rs.next()) {
                    Integer cantidad = rs.getInt(1);
                    String articulo = rs.getString(2);
                    Integer total = rs.getInt(3);
                    Integer mostrar = rs.getInt(4);

                    String tipi;

                    tipi = "" + articulo;

                    if (articulo.length() > 29) {
                        articulo = articulo.substring(0, 25);
                    }
                    convta.guardarventa(cantidad, articulo, total, "delivery", Integer.parseInt(txtDelivery.getText()));

                    if(mostrar == 1){
                        cadena = cadena + cantidad + ":" + tipi + ".....$" + total + "\n";
                    }else if(mostrar == 2){
                        detalle = detalle + cantidad + ":" + tipi + "\n";
                    }

                }

            } catch (Exception ex) {
                System.out.println(ex);
            }

            Integer abona;
            if (cField.getText().equals("")) {
                abona = 0;
            } else {
                abona = Integer.parseInt(cField.getText());
                abona = abona - totaltext;
            }
            
            String strestapago = "";
            if (estapago.isSelected()) {
                strestapago = "   PEDIDO PAGO";
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(left);
        out.write(normalText);
        out.write(doubleHeight);
        out.write(("Delivery: " + orden).getBytes());
        out.write(boldOn);
        out.write(largeSize);
        out.write(("    "+txHora.getText()).getBytes());
        out.write(left);
        out.write(normalText);
        out.write(doubleHeight);
        out.write(("\n================================\n" + cadena + "\nEspecificaciones: \n"+detalle + "\n================================\n" + "Telefono: " + txTelefono.getText() + "\n\n" + "Nombre: " + txNombre.getText() + "\n\n" + "Direccion: " + txDireccion.getText() + "\n\n" + "Descripcion: " + txDescripcion.getText() + "\n================================\n" + "Total: $" + totaltext + strestapago +"\n" + "Vuelto: $" + abona + "\n\n\n Fecha: "+dateFormat.format(fecha22)+"\n\n\n\n\n").getBytes());

            int rta = JOptionPane.showConfirmDialog(null, "Desea imprimir un segundo ticket?");
            if (rta != 0) {
            } else {
                out.write(left);
                out.write(normalText);
                out.write(doubleHeight);
                out.write(("Delivery: " + orden).getBytes());
                out.write(rigth);
                out.write(boldOn);
                out.write(largeSize);
                out.write(("    "+txHora.getText()).getBytes());
                out.write(left);
                out.write(normalText);
                out.write(doubleHeight);
                out.write(("\n================================\n" + cadena + "\nEspecificacion: \n" + detalle + "\n================================\n").getBytes());
                out.write(("Telefono: " + txTelefono.getText() + "\n\n" + "Nombre: " + txNombre.getText() + "\n\n" + "Direccion: " + txDireccion.getText() + "\n\n" + "Descripcion: " + txDescripcion.getText() + "\n================================\n" + "Total: $" + totaltext + strestapago + "\n" + "Vuelto: $" + abona + "\n\n\n Fecha: " + dateFormat.format(fecha22) + "\n\n\n\n\n").getBytes());
                Ticket.ImprimirDocumento(out.toByteArray());
            }

            ultimo = "Delivery: " + orden + "\n=======\n" + cadena + "\n\n Especificacion: \n" + detalle + "\n=======\n" + "Telefono: " + txTelefono.getText() + "\n\n" + "Nombre: " + txNombre.getText() + "\n\n" + "Direccion: " + txDireccion.getText() + "\n\n" + "Descripcion: " + txDescripcion.getText() + "\n=======\n" + "Total: $" + totaltext + "\n" + "Vuelto: $" + abona + "\n\n\n Fecha: "+dateFormat.format(fecha22)+"\n\n\n\n\n";

            
            if (estapago.isSelected()) {
                ord.guardarOrden("deliveryl", totaltext, mediodepago, orden, ultimo);
            }else{
                ord.guardarOrden("delivery", totaltext, mediodepago, orden, ultimo);
            }
            
            

            conn.settotal( txtOrden,  txtDelivery,  txtVntasDia,  txtVntasDelivery,  txtVntasOtros);
            conn.vaciar("venta");
            v.visualizar_tablaventa(tabla, txtTotal);
            vStock.visualizar_stock(tStockeo);
            tabla.setEnabled(false);
            jScrollPane1.setViewportView(tabla);
            if (pedidosya == 6) {
                pedidosyabtn.doClick();
            }
    }
}
