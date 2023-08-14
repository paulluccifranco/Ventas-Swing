/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Productos.Variantes;

import java.sql.Connection;
import java.sql.PreparedStatement;

import Principales.Conexion;

public class ConexionVariantes extends Conexion {

	public void guardarvariante(String nombre, Integer tipo) {
		Connection con = conectar();
		String insert = "INSERT INTO variantes (nombre, tipo) VALUES (?,?)";
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(insert);
			ps.setString(1, nombre);
			ps.setInt(2, tipo);
			ps.executeUpdate();
		} catch (Exception ex) {
			System.out.println("Error al guardar");
		}
		cerrarConexion(null, con, ps);
	}

}
