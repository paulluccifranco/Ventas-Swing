/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Productos.Unidades;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import Principales.Conexion;

public class ConexionUnidades extends Conexion {

	public void guardar(String nombre, Integer precio, Integer tipo, Integer precio2) {
		Connection con = conectar();
		String insert = "INSERT INTO productos (nombre, precio, tipo, precio2) VALUES (?,?,?,?)";
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(insert);
			ps.setString(1, nombre);
			ps.setInt(2, precio);
			ps.setInt(3, tipo);
			ps.setInt(4, precio2);
			ps.executeUpdate();
		} catch (Exception ex) {
			System.out.println("Error al guardar");
		}
		cerrarConexion(null, con, ps);
	}

	public ResultSet SeleccionarUnidades(String nombre, Integer categoria) {
		Connection con = conectar();
		ResultSet rs = null;
		PreparedStatement ps = null;
		String filtro = "";
		Integer cat;
		if (nombre.equals("")) {
			if (categoria == 0) {
				filtro = "";
			} else {
				cat = categoria - 1;
				filtro = " WHERE tipo = " + cat;
			}
		} else {
			if (categoria == 0) {
				filtro = " WHERE nombre = '" + nombre + "'";
			} else {
				cat = categoria - 1;
				filtro = " WHERE nombre = '" + nombre + "' AND tipo = " + cat;
			}
		}
		try {
			ps = con.prepareStatement("SELECT * FROM productos" + filtro);
			rs = ps.executeQuery();
		} catch (Exception ex) {
			System.out.println("Error de consulta");
		}
		return rs;
	}

	public void editarUnidad(Integer id, Integer precio, Integer precio2) {
		Connection con = conectar();
		String insert = "UPDATE productos SET precio = ?, precio2 = ? WHERE id = " + id;
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
