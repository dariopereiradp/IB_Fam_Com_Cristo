package dad.fam_com_cristo.table.conexao;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;

import dad.fam_com_cristo.Main;
import dad.recursos.Log;

/**
 * Classe para fazer a conexão com a tabela Financas e criar a tabela.
 * 
 * @author Dário Pereira
 *
 */
public class ConexaoFinancas implements Conexao {

	private static Connection con;

	private static final String ORIGINAL_FILE = "/databases/financas.accdb";
	private static final String CSV_FILE = Main.BACKUP_DIR + "financas.csv";
	private static final String DB_FILE = Main.DATABASE_DIR + "financas.accdb";
	private static final String DB_URL = "jdbc:ucanaccess://" + DB_FILE
			+ ";jackcessOpener=dad.fam_com_cristo.table.conexao.CryptCodecOpener;memory=true;immediatelyReleaseResources=true";

	public Connection getConnection() {
		try {
			con = DriverManager.getConnection(DB_URL, "", Main.DEFAULT_PASS);
		} catch (Exception e) {
			Log.getInstance().printLog("Erro ao criar a base de dados - " + e.getMessage());
			e.printStackTrace();
		}

		return con;
	}

	/**
	 * Copia uma tabela de finanças vazia dos recursos para a localização da base de dados
	 * @param reset - se true, vai substituir a tabela atual, eliminando todos os dados
	 * @throws IOException
	 */
	public static void createTable(boolean reset) throws IOException {
		File financas = new File(ConexaoFinancas.DB_FILE);
		if (!financas.exists() || reset) {
			Files.copy(ConexaoLogin.class.getResourceAsStream(ORIGINAL_FILE), financas.toPath(),
					StandardCopyOption.REPLACE_EXISTING);
		}
		
			//OLD CODE TO CREATE DATABASES
//			con = DriverManager.getConnection("jdbc:ucanaccess://" + ConexaoFinancas.DB_FILE
//					+ ";newdatabaseversion=V2010;immediatelyReleaseResources=true");
//			DatabaseMetaData dmd = con.getMetaData();
//			try (ResultSet rs = dmd.getTables(null, null, "Financas", new String[] { "TABLE" })) {
//				try (Statement s = con.createStatement()) {
//					s.executeUpdate(
//							"CREATE TABLE Financas (ID int NOT NULL, Data date, Valor currency NOT NULL, Tipo varchar(255) NOT NULL,"
//									+ "Descricao memo, Total currency," + "CONSTRAINT PK_Financas PRIMARY KEY (ID));");
//					Log.getInstance().printLog("Base de dados financas.mbd criada com sucesso");
//				}
//			}
//			con.close();

	}

	@Override
	public File getCsvFile() {
		return new File(CSV_FILE);
	}

}
