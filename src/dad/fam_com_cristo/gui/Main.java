package dad.fam_com_cristo.gui;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.commons.io.FileUtils;

import dad.fam_com_cristo.Membro;
import dad.fam_com_cristo.table.TableModelFinancas;
import dad.fam_com_cristo.table.TableModelFuncionario;
import dad.fam_com_cristo.table.TableModelMembro;
import dad.recursos.ConexaoFinancas;
import dad.recursos.ConexaoLogin;
import dad.recursos.ConexaoMembro;
import dad.recursos.Log;
import dad.recursos.Utils;
import mdlaf.MaterialLookAndFeel;
import mdlaf.themes.MaterialTheme;

/**
 * Classe responsável por inicializar todo o programa e bases de dados.
 * 
 * @author Dário Pereira
 *
 */
public class Main {

	public static final String TITLE = "IGREJA BATISTA FAMÍLIAS COM CRISTO";
	public static final String TITLE_SMALL = "Igreja Batista Famílias com Cristo";
	public static final String PASTOR = "PASTOR";
	public static final String VERSION = "2.0";
	public static final String DATA_PUBLICACAO = "31 de Agosto de 2019";
	public static final String EMAIL_SUPORTE = "pereira13.dario@gmail.com";
	public static final String USER = "admin";
	public static final String PASS = "dad";
	public static final String DOCUMENTS_DIR = System.getProperty("user.home") + System.getProperty("file.separator")
			+ "Documents/IB_Fam_Com_Cristo/";
	public static final String LISTAS_DIR = DOCUMENTS_DIR + "Listas/";
	public static final String BACKUP_DIR = DOCUMENTS_DIR + "Backups/";
	public static final String BUG_REPORTS_DIR = DOCUMENTS_DIR + "BugReports/";
	public static final String DATA_DIR = System.getenv("APPDATA") + "/IB_Fam_Com_Cristo/";
	public static final String DATABASE_DIR = DATA_DIR + "Databases/";
	public static final String MEMBROS_PDF_PATH = DOCUMENTS_DIR + "Fichas de Membros/";
	public static final String[] OPTIONS = { "Sim", "Não" };
	public static final String AVISO_INI = "SE ALTERAR ESSE FICHEIRO O PROGRAMA PODE NÃO FUNCIONAR CORRETAMENTE";
	public static long inicialTime;

	/**
	 * Inicializa o LookAndFeel, as bases de dados e a splash screen.
	 */
	public Main() {
		try {
			createConfFile();
			MaterialLookAndFeel materialTheme = new MaterialLookAndFeel(
					(MaterialTheme) Utils.getInstance().getCurrentTheme());

			UIManager.setLookAndFeel(materialTheme);
			UIManager.getLookAndFeelDefaults().put("TabbedPane[tab].height", 5);
			Splash screen = new Splash();
			EventQueue.invokeLater(new Runnable() {

				@Override
				public void run() {
					screen.setVisible(true);
				}
			});

			Thread t1 = new Thread(new Runnable() {

				@Override
				public void run() {
					createTables();
					TableModelMembro.getInstance().uploadDataBase();
					TableModelFuncionario.getInstance().uploadDataBase();
					TableModelFinancas.getInstance().uploadDataBase();
				}
			});
			t1.start();

			try {
				Thread.sleep(500);
				for (int i = 0; i <= 100; i++) {
					Thread.sleep(30);
					EventQueue.invokeLater(new Incrementar(i, screen));
				}
			} catch (InterruptedException e) {
				String message = "Ocorreu um erro ao abrir o programa. Tenta novamente!\n" + e.getMessage();
				JOptionPane.showMessageDialog(null, message, "Erro", JOptionPane.ERROR_MESSAGE,
						new ImageIcon(getClass().getResource("/FC_SS.jpg")));
				Log.getInstance().printLog(message);
			}

			t1.join();

			EventQueue.invokeLater(new Runnable() {

				@Override
				public void run() {
					screen.setVisible(false);
					Login.getInstance().open();
					inicialTime = System.currentTimeMillis();
				}
			});

			Log.getInstance().printLog("O programa iniciou");

		} catch (Exception e1) {
			e1.printStackTrace();
			String message = "Ocorreu um erro ao abrir o programa. Tenta novamente!\n" + e1.getMessage();
			JOptionPane.showMessageDialog(null, message, "Erro", JOptionPane.ERROR_MESSAGE,
					new ImageIcon(getClass().getResource("/FC_SS.jpg")));
			Log.getInstance().printLog(message);
			System.exit(1);

		}
	}

	public File createConfFile() {
		File conf = null;
		try {
			conf = Utils.getInstance().getPropertiesFile();
			if (conf.createNewFile()) {
				FileOutputStream output = new FileOutputStream(conf);
				Properties prop = new Properties();
				prop.setProperty(Utils.APP_THEME, Utils.THEME_DARK);
				prop.setProperty(PASTOR, "");
				prop.store(output, AVISO_INI);
			}
			return conf;

		} catch (IOException | InputMismatchException e1) {
			Log.getInstance().printLog("Erro ao carregar configurações! - " + e1.getMessage());
			e1.printStackTrace();
			return null;
		}

	}

	/**
	 * Classe que representa uma thread que incrementa o valor da porcentagem na
	 * splash screen.
	 * 
	 * @author Dário Pereira
	 *
	 */
	private class Incrementar implements Runnable {
		private int i;
		private Splash screen;

		private Incrementar(int i, Splash screen) {
			this.i = i;
			this.screen = screen;
		}

		public void run() {
			screen.incrementar(i);
		}
	}

	/**
	 * Cria as pastas e tabelas das bases de dados, caso não existam.
	 */
	private void createTables() {
		File dir = new File(DATABASE_DIR);
		if (!dir.exists())
			dir.mkdirs();

		File membrosPDF = new File(Main.MEMBROS_PDF_PATH);
		if (!membrosPDF.exists())
			membrosPDF.mkdirs();

		File Listdir = new File(Main.LISTAS_DIR);
		if (!Listdir.exists())
			Listdir.mkdirs();

		File backdir = new File(Main.BACKUP_DIR);
		if (!backdir.exists())
			backdir.mkdirs();

		File bugDir = new File(Main.BUG_REPORTS_DIR);
		if (!bugDir.exists())
			bugDir.mkdirs();

		restaurar();

		try {
			ConexaoLogin.createTable();

			ConexaoMembro.createTable();

			ConexaoFinancas.createTable();

			File imgs = new File(Membro.imgPath);
			if (!imgs.exists())
				imgs.mkdirs();

		} catch (

		SQLException e) {
			String message = "Ocorreu um erro ao criar a base de dados... Tenta novamente!\n" + e.getMessage() + "\n"
					+ this.getClass();
			JOptionPane.showMessageDialog(null, message, "Erro", JOptionPane.ERROR_MESSAGE,
					new ImageIcon(getClass().getResource("/FC_SS.jpg")));
			Log.getInstance().printLog(message);
			e.printStackTrace();
		}

	}

	/**
	 * Caso o programa esteja a iniciar após o restauro de uma cópia de segurança,
	 * esse método trata de substituir as bases de dados e apagar as temporárias.
	 */
	public void restaurar() {
		String path = DATA_DIR + "temp/";
		File tmp = new File(path);
		if (tmp.exists()) {
			try {
				File confFile = new File(path + "conf.dad");
				File confDest = new File(Main.DATABASE_DIR + "conf.dad");
				if (confFile.exists())
					Files.copy(confFile.toPath(), confDest.toPath(), StandardCopyOption.REPLACE_EXISTING);

				File funcFile = new File(path + "logins.mdb");
				File funcDest = new File(Main.DATABASE_DIR + "logins.mdb");
				if (funcFile.exists())
					Files.copy(funcFile.toPath(), funcDest.toPath(), StandardCopyOption.REPLACE_EXISTING);

				File images = new File(path + "Imagens/");
				File imagesDest = new File(Main.DATABASE_DIR + "Imagens/");
				if (images.exists())
					FileUtils.copyDirectory(images, imagesDest);

				File membrosFile = new File(path + "membros.mdb");
				File membrosDest = new File(Main.DATABASE_DIR + "membros.mdb");
				if (membrosFile.exists())
					Files.copy(membrosFile.toPath(), membrosDest.toPath(), StandardCopyOption.REPLACE_EXISTING);

				File financasFile = new File(path + "financas.mdb");
				File financasDest = new File(Main.DATABASE_DIR + "financas.mdb");
				if (financasFile.exists())
					Files.copy(financasFile.toPath(), financasDest.toPath(), StandardCopyOption.REPLACE_EXISTING);

				FileUtils.deleteDirectory(tmp);
			} catch (Exception e) {
				Log.getInstance().printLog("Erro ao restaurar bases de dados! - " + e.getMessage());
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args) {
		new Main();
	}

}
