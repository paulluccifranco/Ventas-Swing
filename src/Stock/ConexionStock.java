/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Stock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Principales.Conexion;

/**
 *
 * @author Franco
 */
public class ConexionStock extends Conexion {

	public void guardarstock(String nombre, Integer cantidad) {
		Connection con = conectar();
		String update = "UPDATE stock SET cantidad = ? WHERE nombre = '" + nombre + "'";
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(update);
			ps.setInt(1, cantidad);
			int n = ps.executeUpdate();
			if (n == 0) {
				String insert = "INSERT INTO stock (nombre, cantidad) VALUES (?,?)";
				try {
					ps = con.prepareStatement(insert);
					ps.setString(1, nombre);
					ps.setInt(2, cantidad);
					ps.executeUpdate();
				} catch (Exception ex) {
					System.out.println("Error al insertar");
				}
			}
		} catch (SQLException ex) {
			System.out.println(ex);
		}
		cerrarConexion(null, con, ps);
	}

	public ResultSet contarVentas(String nombre) {
		Connection con = conectar();
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement("SELECT * FROM vtaproductos WHERE nombre = '" + nombre + "'");
			rs = ps.executeQuery();
		} catch (Exception ex) {
			System.out.println("Error de consulta");
		}
		return rs;
	}

}
