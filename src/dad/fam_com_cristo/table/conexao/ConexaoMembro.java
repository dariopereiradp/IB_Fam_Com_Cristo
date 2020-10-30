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
 * Classe para fazer a conexão com a tabela Membros.
 * @author Dário Pereira
 *
 */
public class ConexaoMembro implements Conexao{

	public static Connection con;

	public static final String csvFile = Main.BACKUP_DIR + "membros.csv";
	public static final String dbFile = Main.DATABASE_DIR + "membros.mdb";
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
		File membros = new File(ConexaoMembro.dbFile);
		if (!membros.exists()) {
			con = DriverManager.getConnection("jdbc:ucanaccess://" + ConexaoMembro.dbFile
					+ ";newdatabaseversion=V2003;immediatelyReleaseResources=true");
			DatabaseMetaData dmd = con.getMetaData();
			try (ResultSet rs = dmd.getTables(null, null, "Membros", new String[] { "TABLE" })) {
				try (Statement s = con.createStatement()) {
					s.executeUpdate("CREATE TABLE Membros (ID int NOT NULL, Nome varchar(255) NOT NULL,"
							+ "Data_Nascimento date, Sexo varchar(10), Estado_Civil varchar(25), Profissao varchar(50),"
							+ "Endereco memo, Telefone varchar(15), Email varchar(255), Igreja_Origem varchar(255),"
							+ "Tipo_Membro varchar(127), Batizado varchar(5), Membro_Desde date, Data_Batismo date, Observacoes memo,"
							+ "CONSTRAINT PK_Membros PRIMARY KEY (ID));");
					Log.getInstance().printLog("Base de dados membros.mbd criada com sucesso");
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
