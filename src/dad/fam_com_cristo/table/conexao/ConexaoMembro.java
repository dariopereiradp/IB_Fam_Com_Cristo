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
 * Classe para fazer a conexão com a tabela Membros e criar a tabela.
 * 
 * @author Dário Pereira
 *
 */
public class ConexaoMembro implements Conexao {

	public static Connection con;

	private static final String ORIGINAL_FILE = "/databases/membros.accdb";
	private static final String CSV_FILE = Main.BACKUP_DIR + "membros.csv";
	private static final String DB_FILE = Main.DATABASE_DIR + "membros.accdb";
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
	 * Copia uma tabela de membros vazia dos recursos para a localização da base de dados
	 * @param reset - se true, vai substituir a tabela atual, eliminando todos os dados
	 * @throws IOException
	 */
	public static void createTable(boolean reset) throws IOException {
		File membros = new File(ConexaoMembro.DB_FILE);
		if (!membros.exists() || reset) {
			Files.copy(ConexaoLogin.class.getResourceAsStream(ORIGINAL_FILE), membros.toPath(),
					StandardCopyOption.REPLACE_EXISTING);
		}

		// OLD CODE TO CREATE THE FILE
//			con = DriverManager.getConnection("jdbc:ucanaccess://" + ConexaoMembro.dbFile
//					+ ";newdatabaseversion=V2010;immediatelyReleaseResources=true");
//			DatabaseMetaData dmd = con.getMetaData();
//			try (ResultSet rs = dmd.getTables(null, null, "Membros", new String[] { "TABLE" })) {
//				try (Statement s = con.createStatement()) {
//					s.executeUpdate("CREATE TABLE Membros (ID int NOT NULL, Nome varchar(255) NOT NULL,"
//							+ "Data_Nascimento date, Sexo varchar(10), Estado_Civil varchar(25), Profissao varchar(50),"
//							+ "Endereco memo, Telefone varchar(15), Email varchar(255), Igreja_Origem varchar(255),"
//							+ "Tipo_Membro varchar(127), Batizado varchar(5), Membro_Desde date, Data_Batismo date, Observacoes memo,"
//							+ "CONSTRAINT PK_Membros PRIMARY KEY (ID));");
//					Log.getInstance().printLog("Base de dados membros.mbd criada com sucesso");
//				}
//			}
//			con.close();

	}

	@Override
	public File getCsvFile() {
		return new File(CSV_FILE);
	}

}
