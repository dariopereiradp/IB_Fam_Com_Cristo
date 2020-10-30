package dad.fam_com_cristo.table.conexao;

import java.io.File;
import java.sql.Connection;

public interface Conexao {
	
	Connection getConnection();
	
	File getCsvFile();

}
