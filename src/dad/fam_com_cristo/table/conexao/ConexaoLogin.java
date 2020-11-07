package dad.fam_com_cristo.table.conexao;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import dad.fam_com_cristo.Main;
import dad.recursos.CriptografiaAES;
import dad.recursos.Log;

/**
 * Classe para fazer a conexão com a tabela Logins e criar a tabela.
 * 
 * @author Dário Pereira
 *
 */
public class ConexaoLogin {

	private static Connection con;

	private static final String ORIGINAL_FILE = "/databases/logins.accdb";
	private static String DB_FILE = Main.DATABASE_DIR + "logins.accdb";
	private static String DB_URL = "jdbc:ucanaccess://" + DB_FILE
			+ ";jackcessOpener=dad.fam_com_cristo.table.conexao.CryptCodecOpener;memory=true;immediatelyReleaseResources=true";

	public static Connection getConnection() {
		try {
			con = DriverManager.getConnection(DB_URL, "", Main.DEFAULT_PASS);
		} catch (Exception e) {
			Log.getInstance().printLog("Erro ao criar a base de dados - " + e.getMessage());
			e.printStackTrace();
		}

		return con;
	}

	/**
	 * Copia uma tabela de logins vazia dos recursos para a localização da base de dados
	 * @param reset - se true, vai substituir a tabela atual, eliminando todos os dados
	 * @throws IOException
	 */
	public static void createTable(boolean reset) throws Exception {
		File logins = new File(ConexaoLogin.DB_FILE);
		if (!logins.exists() || reset) {
			Files.copy(ConexaoLogin.class.getResourceAsStream(ORIGINAL_FILE), logins.toPath(),
					StandardCopyOption.REPLACE_EXISTING);

			// OLD CODE TO CREATE THE FILE
//			con = DriverManager.getConnection("jdbc:ucanaccess://" + ConexaoLogin.dbFile
//					+ ";newdatabaseversion=V2010;immediatelyReleaseResources=true;");
//			DatabaseMetaData dmd = con.getMetaData();
//			try (ResultSet rs = dmd.getTables(null, null, "Logins", new String[] { "TABLE" })) {
//				try (Statement s = con.createStatement()) {
//					s.executeUpdate("CREATE TABLE Logins (Nome varchar(255) NOT NULL,"
//							+ "Pass varchar(50) NOT NULL, Num_acessos int, Ultimo_Acesso date,Data_Criacao date, CONSTRAINT PK_Logins PRIMARY KEY (Nome));");
//					Log.getInstance().printLog("Base de dados logins.mbd criada com sucesso");
//				}

			con = getConnection();
			CriptografiaAES.setKey(Main.DEFAULT_PASS);
			CriptografiaAES.encrypt(Main.DEFAULT_PASS);
			PreparedStatement pst = con.prepareStatement(
					"insert into logins(Nome,Pass,Num_acessos,Ultimo_Acesso,Data_Criacao) values (?,?,?,?,?)");
			pst.setString(1, Main.DEFAULT_USER);
			pst.setString(2, CriptografiaAES.getEncryptedString());
			pst.setInt(3, 0);
			pst.setDate(4, new Date(System.currentTimeMillis()));
			pst.setDate(5, new Date(System.currentTimeMillis()));
			pst.execute();
			con.close();
			Log.getInstance().printLog("Utilizador admin criado com sucesso!");
		}

	}

}
