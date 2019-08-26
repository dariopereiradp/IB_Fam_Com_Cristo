package dad.recursos;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

import dad.fam_com_cristo.gui.Main;

/**
 * Classe para fazer a conex�o com a tabela Financas.
 * @author D�rio Pereira
 *
 */
public class ConexaoFinancas {

	public static Connection con;

	public static String dbFile = Main.DATABASE_DIR + "financas.mdb";
	public static String dbUrl = "jdbc:ucanaccess://" + dbFile + ";memory=true;immediatelyReleaseResources=true";

	public static Connection getConnection() {
		try {
			File dir = new File(Main.DATABASE_DIR);
			if (!dir.exists())
				dir.mkdirs();
			con = DriverManager.getConnection(dbUrl);
		} catch (Exception e) {
			Log.getInstance().printLog("Erro ao criar a base de dados - " + e.getMessage());
			e.printStackTrace();
		}

		return con;
	}

}
