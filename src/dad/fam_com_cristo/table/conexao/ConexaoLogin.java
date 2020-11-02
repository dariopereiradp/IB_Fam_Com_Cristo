package dad.fam_com_cristo.table.conexao;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import dad.fam_com_cristo.Main;
import dad.recursos.CriptografiaAES;
import dad.recursos.Log;

/**
 * Classe para fazer a conexão com a tabela Logins e criar a tabela.
 * @author Dário Pereira
 *
 */
public class ConexaoLogin {

	private static Connection con;

	private static String dbFile = Main.DATABASE_DIR + "logins.mdb";
	private static String dbUrl = "jdbc:ucanaccess://" + dbFile + ";memory=true;immediatelyReleaseResources=true";

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

	public static void createTable() throws SQLException {
		File logins = new File(ConexaoLogin.dbFile);
		if (!logins.exists()) {
			con = DriverManager.getConnection("jdbc:ucanaccess://" + ConexaoLogin.dbFile
					+ ";newdatabaseversion=V2003;immediatelyReleaseResources=true");
			DatabaseMetaData dmd = con.getMetaData();
			try (ResultSet rs = dmd.getTables(null, null, "Logins", new String[] { "TABLE" })) {
				try (Statement s = con.createStatement()) {
					s.executeUpdate("CREATE TABLE Logins (Nome varchar(255) NOT NULL,"
							+ "Pass varchar(50) NOT NULL, Num_acessos int, Ultimo_Acesso date,Data_Criacao date, CONSTRAINT PK_Logins PRIMARY KEY (Nome));");
					Log.getInstance().printLog("Base de dados logins.mbd criada com sucesso");
				}
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
				Log.getInstance().printLog("Utilizador admin criado com sucesso!");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			con.close();
		}
		
	}

}
