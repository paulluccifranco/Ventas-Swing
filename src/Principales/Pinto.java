package Principales;

import static java.awt.Frame.MAXIMIZED_BOTH;

import java.sql.SQLException;

import javax.swing.UIManager;

import PantallaPrincipal.Principal;

public class Pinto {

	public static void main(String[] args) throws SQLException, ClassNotFoundException {

		try {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					System.out.println("CHOSEN THIS");
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("Error setting native LAF: " + e);
		}

		Principal miPrincipal = new Principal();
		miPrincipal.setExtendedState(MAXIMIZED_BOTH);
		miPrincipal.setVisible(true);
	}

}
