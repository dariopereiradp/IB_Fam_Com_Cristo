package dad.recursos;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;

import dad.fam_com_cristo.Main;

/**
 * Classe para criar ficheiros de log do programa.
 * @author Dário Pereira
 *
 */
public class Log {

	private static Log INSTANCE;
	private Logger logger;
	private FileHandler fh;
	private String name;
	
	private Log() {
		INSTANCE = this;
		String logFormatName = "log_"
				+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMMyyyy-HH'h'mm'm'ss's'"));
		String month_year = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMMyyyy")).toUpperCase();
		logger = Logger.getLogger(logFormatName);
		try {
			File dir = new File(System.getenv("APPDATA") + "/IB_Fam_Com_Cristo/Logs/" + month_year + "/");
			if (!dir.exists())
				dir.mkdirs();
			name = dir.getAbsolutePath() + "/" + logFormatName + ".log";
			open();
			logger.setUseParentHandlers(true);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);

		} catch (SecurityException e) {
			String message = "Ocorreu um erro ao criar o log...\n" + e.getMessage() + "\n" + this.getClass();
			JOptionPane.showMessageDialog(null, message, "Erro", JOptionPane.ERROR_MESSAGE,
					new ImageIcon(getClass().getResource("FC_SS.jpg")));
			e.printStackTrace();
		}

	}

	/**
	 * Abre a conexão ao ficheiro de log
	 */
	public void open() {
		try {
			fh = new FileHandler(name);
			logger.addHandler(fh);
		} catch (Exception e) {
			String message = "Ocorreu um erro ao criar o log...\n" + e.getMessage() + "\n" + this.getClass();
			JOptionPane.showMessageDialog(null, message, "Erro", JOptionPane.ERROR_MESSAGE,
					new ImageIcon(getClass().getResource("FC_SS.jpg")));
			e.printStackTrace();
		}
	}
	
	/**
	 * Fecha a conexão ao ficheiro de log
	 */
	public void close(){
		fh.close();
	}

	/**
	 * Adiciona uma mensagem ao ficheiro de log
	 * @param message
	 */
	public void printLog(String message) {
		logger.info(message + "\n");
		System.out.println(message);
	}
	
	/**
	 * Apaga os logs dos meses anteriores, para limpar espaço no sistema.
	 */
	public static void limpar() {
		String logPath = Main.DATA_DIR + "Logs/";
		String month_year = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMMyyyy")).toUpperCase();
		File logs = new File(logPath);
		File logMonth = new File(logPath + month_year + "/");
		File logMonthTmp = new File(Main.DATA_DIR + month_year + "/");

		try {
			FileUtils.copyDirectory(logMonth, logMonthTmp);
			FileUtils.deleteDirectory(logs);
		} catch (IOException e) {
			try {
				Log.getInstance().close();
				INSTANCE = null;
				FileUtils.copyDirectory(logMonthTmp, logMonth);
				FileUtils.deleteDirectory(logMonthTmp);
				JOptionPane.showMessageDialog(null, "Limpeza feita!", "Limpar espaço - Sucesso", JOptionPane.OK_OPTION,
						new ImageIcon(Log.class.getResource("/FC_SS.jpg")));
			} catch (IOException e1) {
				Log.getInstance().printLog("Erro ao limpar o espaço! - " + e.getMessage());
				JOptionPane.showMessageDialog(null, "Erro ao fazer a limpeza! - " + e.getMessage(),
						"Limpar espaço - Erro", JOptionPane.OK_OPTION,
						new ImageIcon(Log.class.getResource("/FC_SS.jpg")));
				e1.printStackTrace();
			}
		}
	}
	
	public static Log getInstance() {
		if (INSTANCE == null)
			INSTANCE = new Log();
		return INSTANCE;
	}

}
