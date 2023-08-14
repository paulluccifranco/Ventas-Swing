/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Productos.Combos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Principales.Conexion;

public class ConexionCombos extends Conexion {

	public ResultSet select(String tabla) {
		Connection con = conectar();
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement("SELECT * FROM " + tabla);
			rs = ps.executeQuery();
		} catch (Exception ex) {
			System.out.println("Error de consulta");
		}
		return rs;
	}

	public ResultSet selectcomboproducto(Integer id) {
		Connection con = conectar();
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(
					"SELECT * FROM comboproducto c RIGHT JOIN productos p ON c.id_producto = p.id WHERE c.id_combo = "
							+ id);
			rs = ps.executeQuery();
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return rs;
	}

	public void guardarcomboproducto(Integer idcombo, Integer idproducto) {
		Connection con = conectar();

		String update = "UPDATE comboproducto SET cantidad = cantidad + ? WHERE id_combo = " + idcombo
				+ " AND id_producto = " + idproducto;
		PreparedStatement ps = null;
		try {

			ps = con.prepareStatement(update);
			ps.setInt(1, 1);

			int n = ps.executeUpdate();

			if (n == 0) {
				String insert = "INSERT INTO comboproducto (id_combo, id_producto, cantidad) VALUES (?,?,?)";
				try {
					ps = con.prepareStatement(insert);
					ps.setInt(1, idcombo);
					ps.setInt(2, idproducto);
					ps.setInt(3, 1);

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

	public void guardarcombo(String nombre, Integer precio, Integer precio2) {
		Connection con = conectar();
		String insert = "INSERT INTO combos (nombre, precio, precio2) VALUES (?,?,?)";
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(insert);
			ps.setString(1, nombre);
			ps.setInt(2, precio);
			ps.setInt(3, precio2);

			ps.executeUpdate();

		} catch (Exception ex) {
			System.out.println("Error al guardar");
		}
		cerrarConexion(null, con, ps);
	}

	public void editarcombo(Integer id, Integer precio, Integer precio2) {
		Connection con = conectar();

		String insert = "UPDATE combos SET precio = ?, precio2 = ? WHERE id = " + id;
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(insert);
			ps.setInt(1, precio);
			ps.setInt(2, precio2);

			ps.executeUpdate();

		} catch (Exception ex) {
			System.out.println("Error al actualizar");
		}
		cerrarConexion(null, con, ps);
	}

}
