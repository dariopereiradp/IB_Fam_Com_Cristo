package dad.recursos;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import dad.fam_com_cristo.Main;
import dad.fam_com_cristo.gui.DataGui;
import dad.fam_com_cristo.table.conexao.Conexao;
import dad.fam_com_cristo.table.conexao.ConexaoFinancas;

/**
 * Classe que exporta a lista de Membros ou Financas para CSV,
 * diretamente da base de dados (Access para CSV)
 * @author dariopereiradp
 *
 */
public class CSVExport {

	public static void exportToCsv(Conexao connection) {
		try {
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(connection.getCsvFile())));

			PreparedStatement pst = null;
			ResultSet resultSet = null;

			if (connection != null) {
				String statement = "select * from membros order by ID";
				if (connection instanceof ConexaoFinancas)
					statement = "select * from financas order by ID";

				pst = connection.getConnection().prepareStatement(statement);
				resultSet = pst.executeQuery();
				CSVPrinter csvPrinter = new CSVPrinter(writer,
						CSVFormat.DEFAULT.withHeader(resultSet).withDelimiter(';'));
				csvPrinter.printRecords(resultSet);
				csvPrinter.close();

				String message = "O ficheiro CSV" + " foi criado com sucesso e foi salvo na pasta:\n" + Main.BACKUP_DIR
						+ "\nVocê quer abrir o documento agora?";
				int ok = JOptionPane.showOptionDialog(DataGui.getInstance(), message, "Criado com sucesso",
						JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,
						new ImageIcon(DataGui.getInstance().getClass().getResource("/FC_SS.jpg")), Main.OPTIONS,
						Main.OPTIONS[1]);
				Log.getInstance().printLog(message);
				if (ok == JOptionPane.YES_OPTION) {
					Desktop.getDesktop().open(new File(Main.BACKUP_DIR));
					Desktop.getDesktop().open(connection.getCsvFile());
				}
			}
		} catch (Exception throwables) {
			throwables.printStackTrace();
		}
	}
}
