package hr.fer.zemris.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class IzmijeniZapis {

	public static void main(String[] args) {
		
		try {
			Class.forName("org.apache.derby.jdbc.ClientDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		}

		String dbName="porukedb";
		String connectionURL = "jdbc:derby://localhost:1527/" + dbName;

		Connection con = null;
		try {
			con = DriverManager.getConnection(connectionURL);
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		PreparedStatement pst = null;
		
		try {
			pst = con.prepareStatement("UPDATE Poruke set title=?, userEMail=? WHERE id=?");
			pst.setLong(3, 2); // Napravi promjenu u retku s ID=2
			pst.setString(1, "Modificirana druga poruka");
			pst.setString(2, "Andela.Andelic@edu.edu");

			int numberOfAffectedRows = pst.executeUpdate(); // Ocekujemo da je numberOfAffectedRows=1
			System.out.println("Broj redaka koji su pogođeni ovom izmjenom: "+numberOfAffectedRows);
		} catch(SQLException ex) {
			ex.printStackTrace();
		} finally {
			try { pst.close(); } catch(SQLException ex) {
				ex.printStackTrace();
			}
		}
		try { con.close(); } catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
}
