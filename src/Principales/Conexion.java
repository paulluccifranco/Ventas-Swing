/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Principales;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JTextField;

public class Conexion {

	public Connection conectar() {
		Connection con = null;
		String url = "jdbc:mysql://localhost:3306/ventas?serverTimezone=GMT-3";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url, "root", "495054");
			System.out.println("Cargado");
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return con;
	}

	public Boolean cerrarConexion(ResultSet rs, Connection conexion, PreparedStatement ps) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
			if (conexion != null) {
				conexion.close();
			}
		} catch (SQLException sqle) {
			System.out.println("Error al cerrar la conexion: " + sqle);
		}
		return true;
	}

	public ResultSet select(String tabla) {
		Connection con = conectar();
		ResultSet rs = null;
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM " + tabla);
			rs = ps.executeQuery();
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return rs;
	}

	public void guardarconsumoventa(String nombre, Integer precio, Integer cantidad, Integer mostrar) {
		Connection con = conectar();
		String update = "UPDATE venta SET cantidad = cantidad + ?, total = total + ? WHERE nombre = '" + nombre
				+ "' AND mostrar != 0";
		if (mostrar == 0) {
			update = "UPDATE venta SET cantidad = cantidad + ?, total = total + ? WHERE nombre = '" + nombre
					+ "' AND mostrar = 0";
		}

		PreparedStatement ps = null;
		try {

			ps = con.prepareStatement(update);
			ps.setInt(1, cantidad);
			ps.setInt(2, precio);

			int n = ps.executeUpdate();

			if (n == 0) {
				String insert = "INSERT INTO venta (cantidad, nombre, total, mostrar) VALUES (?,?,?,?)";
				try {
					ps = con.prepareStatement(insert);
					ps.setInt(1, cantidad);
					ps.setString(2, nombre);
					ps.setInt(3, precio);
					ps.setInt(4, mostrar);

					ps.executeUpdate();

				} catch (Exception ex) {
					System.out.println("Error al insertar");
				}
			}

		} catch (SQLException ex) {
			System.out.println("hola");
		}
		cerrarConexion(null, con, ps);
	}

	public void guardarVentavariante(Integer id, String nombre, Integer precio, Integer cantidad) {
		Connection con = conectar();

		ResultSet rs = select("venta WHERE nombre = 'Ensalada'");
		PreparedStatement ps = null;

		try {
			while (rs.next()) {
				if (rs.getInt(2) > 1) {
					String update = "UPDATE venta SET nombre = CONCAT(nombre, ? ), cantidad = 1 , total = ? WHERE nombre = 'Ensalada'";
					try {
						ps = con.prepareStatement(update);
						ps.setString(1, " (" + nombre + ")");
						ps.setInt(2, rs.getInt(4) / rs.getInt(2));

						ps.executeUpdate();

						guardarconsumoventa("Ensalada", rs.getInt(4) - (rs.getInt(4) / rs.getInt(2)), rs.getInt(2) - 1,
								0);

					} catch (SQLException ex) {
						System.out.println(ex);
					}

				} else {
					String update = "UPDATE venta SET nombre = CONCAT(nombre, ? ) WHERE nombre = 'Ensalada'";
					try {
						ps = con.prepareStatement(update);
						ps.setString(1, " (" + nombre + ")");

						ps.executeUpdate();

					} catch (SQLException ex) {
						System.out.println(ex);
					}
				}

			}
		} catch (Exception ex) {
			System.out.println("Error en cargar productos principal");
		}
		cerrarConexion(null, con, ps);

	}

	public void eliminar(Integer id, String tabla) {
		Connection con = conectar();

		String insert = "DELETE FROM " + tabla + " WHERE id = " + id;
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(insert);
			ps.execute();

		} catch (Exception ex) {
			System.out.println(ex);
		}
		cerrarConexion(null, con, ps);
	}

	public void guardarTicket(String ticket, String turno) {
		Connection con = conectar();
		Date fecha = new Date(System.currentTimeMillis());
		String insert = "INSERT INTO tickets (ticket, fecha, turno) VALUES (?,?,?)";
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(insert);
			ps.setString(1, ticket);
			ps.setDate(2, fecha);
			ps.setString(3, turno);

			ps.executeUpdate();

		} catch (Exception ex) {
			System.out.println("Error al guardar");
		}
		cerrarConexion(null, con, ps);

	}

	public void settotal(JTextField local, JTextField delivery, JTextField efectivo, JTextField encalle,
			JTextField otros) {
		Connection con = conectar();
		ResultSet rs = null;
		Integer numerolocal = 0;
		Integer numerodelivery = 0;
		Integer totalefectivo = 0;
		Integer totalencalle = 0;
		Integer totalotros = 0;
		PreparedStatement ps = null;

		local.setText("" + numerolocal);
		delivery.setText("" + numerodelivery);

		try {
			ps = con.prepareStatement("SELECT * FROM ordenes ORDER BY orden ASC");
			rs = ps.executeQuery();

			try {
				while (rs.next()) {
					if (rs.getString(1).equals("delivery")) {
						numerodelivery = rs.getInt(5);
						if (rs.getInt(4) == 1) {
							totalencalle = totalencalle + rs.getInt(2);
						} else {
							totalotros = totalotros + rs.getInt(2);
						}
					} else if (rs.getString(1).equals("deliveryl")) {
						numerodelivery = rs.getInt(5);
						if (rs.getInt(4) == 1) {
							totalefectivo = totalefectivo + rs.getInt(2);
						} else {
							totalotros = totalotros + rs.getInt(2);
						}
					} else {
						numerolocal = rs.getInt(5);
						if (rs.getInt(4) == 1) {
							totalefectivo = totalefectivo + rs.getInt(2);
						} else {
							totalotros = totalotros + rs.getInt(2);
						}
					}
				}
				local.setText("" + numerolocal);
				delivery.setText("" + numerodelivery);
				efectivo.setText("" + totalefectivo);
				encalle.setText("" + totalencalle);
				otros.setText("" + totalotros);

			} catch (Exception ex) {
				System.out.println(ex);
			}

		} catch (Exception ex) {
			System.out.println(ex);
		}
		cerrarConexion(null, con, ps);
	}

	public void guardarcliente(String telefono, String nombre, String direccion, String descripcion) {
		Connection con = conectar();

		String update = "UPDATE clientes SET nombre = ?, direccion = ?, descripcion = ? WHERE telefono = '" + telefono
				+ "'";
		PreparedStatement ps = null;
		try {

			ps = con.prepareStatement(update);
			ps.setString(1, nombre);
			ps.setString(2, direccion);
			ps.setString(3, descripcion);

			int n = ps.executeUpdate();

			if (n == 0) {
				String insert = "INSERT INTO clientes (telefono, nombre, direccion, descripcion) VALUES (?,?,?,?)";
				try {
					ps = con.prepareStatement(insert);
					ps.setString(1, telefono);
					ps.setString(2, nombre);
					ps.setString(3, direccion);
					ps.setString(4, descripcion);

					ps.executeUpdate();

				} catch (Exception ex) {
					System.out.println("Error al insertar");
				}
			}

		} catch (SQLException ex) {
			System.out.println("Error al updatear");
		}
		cerrarConexion(null, con, ps);

	}

	public ResultSet selectcliente(String telefono) {
		Connection con = conectar();
		ResultSet rs = null;

		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM clientes WHERE telefono = '" + telefono + "'");
			rs = ps.executeQuery();
		} catch (Exception ex) {
			System.out.println("Error de consulta");
		}
		return rs;
	}

	public void vaciar(String nombretabla) {
		Connection con = conectar();

		String insert = "TRUNCATE " + nombretabla;
		PreparedStatement ps = null;
		try {

			ps = con.prepareStatement(insert);

			ps.execute();

		} catch (Exception ex) {
			System.out.println(ex);
		}
		cerrarConexion(null, con, ps);
	}

	public void guardar_ingreso(String descripcion, Double gasto) {
		Connection con = conectar();

		String insert = "INSERT INTO ingresos (descripcion, gasto) VALUES (?,?)";
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(insert);
			ps.setString(1, descripcion);
			ps.setDouble(2, gasto);

			ps.executeUpdate();

		} catch (Exception ex) {
			System.out.println("Error al guardar");
		}
		cerrarConexion(null, con, ps);
	}

	public void guardar_salida(String descripcion, Double gasto) {
		Connection con = conectar();

		String insert = "INSERT INTO salidas (descripcion, gasto) VALUES (?,?)";
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(insert);
			ps.setString(1, descripcion);
			ps.setDouble(2, gasto);

			ps.executeUpdate();

		} catch (Exception ex) {
			System.out.println("Error al guardar");
		}
		cerrarConexion(null, con, ps);
	}

}
