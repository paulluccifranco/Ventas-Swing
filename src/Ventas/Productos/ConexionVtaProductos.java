/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas.Productos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import Principales.Conexion;

/**
 *
 * @author Franco
 */
public class ConexionVtaProductos extends Conexion {

	public void guardarventa(Integer cantidad, String nombre, Integer total, String tipo, Integer orden) {
		Connection con = conectar();
		String insert = "INSERT INTO vtaproductos (cantidad, nombre, total, tipo, orden) VALUES (?,?,?,?,?)";
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(insert);
			ps.setInt(1, cantidad);
			ps.setString(2, nombre);
			ps.setInt(3, total);
			ps.setString(4, tipo);
			ps.setInt(5, orden + 1);
			ps.executeUpdate();
		} catch (SQLException ex) {
			System.out.println(ex);
		}
		cerrarConexion(null, con, ps);
	}

}
