package dad.fam_com_cristo.table.conexao;

import java.io.File;
import java.sql.Connection;

/**
 * Interface usada para facilitar a geração do ficheiro CSV
 * @author dariopereiradp
 *
 */
public interface Conexao {
	
	Connection getConnection();
	
	File getCsvFile();

}
