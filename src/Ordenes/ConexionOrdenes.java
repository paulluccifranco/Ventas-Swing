/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ordenes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import Principales.Conexion;

/**
 *
 * @author Franco
 */
public class ConexionOrdenes extends Conexion {

	public void guardarOrden(String tipo, Integer total, Integer pago, Integer orden, String recibo) {
		Connection con = conectar();
		String insert = "INSERT INTO ordenes (tipo, total, pago, orden, recibo) VALUES (?,?,?,?,?)";
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(insert);
			ps.setString(1, tipo);
			ps.setInt(2, total);
			ps.setInt(3, pago);
			ps.setInt(4, orden);
			ps.setString(5, recibo);

			ps.executeUpdate();
			cerrarConexion(null, con, ps);

		} catch (Exception ex) {
			System.out.println("Error al guardar orden");
		}

	}

	public void eliminarOrden(Integer id, String tipo, String turno) {
		Connection con = conectar();

		String delete = "DELETE FROM ordenes WHERE tipo = '" + tipo + "' AND orden = " + id;
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(delete);
			ps.execute();

		} catch (Exception ex) {
			System.out.println("Error al borrar");
		}

		if (tipo.equals("deliveryl")) {
			tipo = "delivery";
		}

		delete = "DELETE FROM vtaproductos WHERE tipo = '" + tipo + "' AND orden = " + id;
		ps = null;
		try {
			ps = con.prepareStatement(delete);
			ps.execute();

		} catch (Exception ex) {
			System.out.println("Error al borrar");
		}
		cerrarConexion(null, con, ps);
	}

	public ResultSet selectFiltros(Integer tipo, Integer orden) {
		Connection con = conectar();
		String tipo2 = "local";
		if (tipo == 1) {
			tipo2 = "delivery";
		}
		ResultSet rs = null;
		try {
			PreparedStatement ps = con
					.prepareStatement("SELECT * FROM ordenes WHERE tipo = '" + tipo2 + "' AND orden = " + orden);
			if (orden == 0) {
				ps = con.prepareStatement("SELECT * FROM ordenes WHERE tipo = '" + tipo2 + "'");
			}
			rs = ps.executeQuery();
		} catch (Exception ex) {
			System.out.println("Error de consulta");
		}
		return rs;
	}
}
