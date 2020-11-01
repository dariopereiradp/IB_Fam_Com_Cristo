package dad.fam_com_cristo.table.conexao;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import dad.fam_com_cristo.Main;
import dad.recursos.Log;

/**
 * Classe para fazer a conexão com a tabela Financas e criar a tabela.
 * @author Dário Pereira
 *
 */
public class ConexaoFinancas implements Conexao {

	public static Connection con;

	public static final String csvFile = Main.BACKUP_DIR + "financas.csv";
	public static final String dbFile = Main.DATABASE_DIR + "financas.mdb";
	public static final String dbUrl = "jdbc:ucanaccess://" + dbFile + ";memory=true;immediatelyReleaseResources=true";

	public Connection getConnection() {
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
	
	public static void createTable() throws SQLException {
		File financas = new File(ConexaoFinancas.dbFile);
		if (!financas.exists()) {
			con = DriverManager.getConnection("jdbc:ucanaccess://" + ConexaoFinancas.dbFile
					+ ";newdatabaseversion=V2003;immediatelyReleaseResources=true");
			DatabaseMetaData dmd = con.getMetaData();
			try (ResultSet rs = dmd.getTables(null, null, "Financas", new String[] { "TABLE" })) {
				try (Statement s = con.createStatement()) {
					s.executeUpdate("CREATE TABLE Financas (ID int NOT NULL, Data date, Valor currency NOT NULL, Tipo varchar(255) NOT NULL,"
							+ "Descricao memo, Total currency,"
							+ "CONSTRAINT PK_Financas PRIMARY KEY (ID));");
					Log.getInstance().printLog("Base de dados financas.mbd criada com sucesso");
				}
			}
			con.close();
		}
	}

	@Override
	public File getCsvFile() {
		return new File(csvFile);
	}

}
