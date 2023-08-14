/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PantallaPrincipal;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JTextField;

import Principales.Conexion;

/**
 *
 * @author Franco
 */
public class ConexionPrincipal extends Conexion {

	Conexion conn = new Conexion();

	public void cargarNombre(String telefono, JTextField txNombre, JTextField txDireccion) {
		ResultSet cliente = conn.selectcliente(telefono);
		try {
			while (cliente.next()) {
				txNombre.setText(cliente.getString(2));
				txDireccion.setText(cliente.getString(3));
			}
		} catch (SQLException ex) {
			System.out.println("Fallo");
		}
	}

	public ResultSet buscarticket(String fecha, String turno) {
		Connection con = conectar();
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(
					"SELECT * FROM tickets WHERE fecha = '" + fecha + "' AND turno = '" + turno + "'");
			rs = ps.executeQuery();
		} catch (Exception ex) {
			System.out.println("Error de consulta");
		}
		return rs;
	}

	public void guardarVentas(Integer cantidad, String nombre, Integer total, String tipo, String turno) {
		Connection con = conectar();
		Date fecha = new Date(System.currentTimeMillis());
		String insert = "INSERT INTO ventasproductos (cantidad, nombre, total, fecha, tipo, turno) VALUES (?,?,?,?,?,?)";
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(insert);
			ps.setInt(1, cantidad);
			ps.setString(2, nombre);
			ps.setInt(3, total);
			ps.setDate(4, fecha);
			ps.setString(5, tipo);
			ps.setString(6, turno);

			ps.executeUpdate();

		} catch (Exception ex) {
			System.out.println(ex);
		}
		cerrarConexion(null, con, ps);

	}
}
