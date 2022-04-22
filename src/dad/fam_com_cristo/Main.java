package dad.fam_com_cristo;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileSystemView;

import org.apache.commons.io.FileUtils;

import dad.fam_com_cristo.gui.Login;
import dad.fam_com_cristo.gui.Splash;
import dad.fam_com_cristo.table.conexao.ConexaoFinancas;
import dad.fam_com_cristo.table.conexao.ConexaoLogin;
import dad.fam_com_cristo.table.conexao.ConexaoMembro;
import dad.fam_com_cristo.table.models.TableModelFinancas;
import dad.fam_com_cristo.table.models.TableModelFuncionario;
import dad.fam_com_cristo.table.models.TableModelMembro;
import dad.fam_com_cristo.types.Membro;
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
	public static final String SIGLA = "IBFC";
	public static final String PASTOR = "PASTOR";
	public static final String VERSION = "2.1.0 - Reforma";
	public static final String DATA_PUBLICACAO = "21 de Abril de 2022";
	public static final String EMAIL_SUPORTE = "pereira13.dario@gmail.com";
	public static final String DEFAULT_USER = "admin";
	public static final String DEFAULT_PASS = "dad";
//	private static final String DOCUMENTS_DIR = System.getProperty("user.home") + System.getProperty("file.separator")
//			+ "Documents/IB_Fam_Com_Cristo/";
	private static final String DOCUMENTS_DIR = FileSystemView.getFileSystemView().getDefaultDirectory().getPath()
			+ System.getProperty("file.separator") + "IB_Fam_Com_Cristo" + System.getProperty("file.separator");
	public static final String LISTAS_DIR = DOCUMENTS_DIR + "Listas de Membros" + System.getProperty("file.separator");
	public static final String RELATORIOS_DIR = DOCUMENTS_DIR + "Relatórios Financeiros"
			+ System.getProperty("file.separator");
	public static final String BACKUP_DIR = DOCUMENTS_DIR + "Backups" + System.getProperty("file.separator");
	public static final String LOGO_DIR = DOCUMENTS_DIR + "Imagem de Logotipo" + System.getProperty("file.separator");
	public static final String SAVED_IMAGES = DOCUMENTS_DIR + "Imagem Salvas" + System.getProperty("file.separator");
	public static final String MODELOS_DIR = DOCUMENTS_DIR + "Modelos" + System.getProperty("file.separator");
	public static final String BUG_REPORTS_DIR = DOCUMENTS_DIR + "BugReports" + System.getProperty("file.separator");
	public static final String DATA_DIR = System.getenv("APPDATA") + System.getProperty("file.separator")
			+ "IB_Fam_Com_Cristo" + System.getProperty("file.separator");
	public static final String DATABASE_DIR = DATA_DIR + "Databases" + System.getProperty("file.separator");
	public static final String MEMBROS_PDF_PATH = DOCUMENTS_DIR + "Fichas de Membros"
			+ System.getProperty("file.separator");
	public static final String[] OPTIONS = { "Sim", "Não" };
	public static final String[] OPTIONS_CANCEL = { "Sim", "Não", "Cancelar" };
	public static final String AVISO_INI = "SE ALTERAR ESSE FICHEIRO O PROGRAMA PODE NÃO FUNCIONAR CORRETAMENTE";
	public static long inicialTime;

	/**
	 * Inicializa o LookAndFeel, as bases de dados e a splash screen.
	 */
	public Main() {
		try {
			createFolders();
			restaurar();
			MaterialLookAndFeel materialTheme = new MaterialLookAndFeel(
					(MaterialTheme) Utils.getInstance().getCurrentTheme());

			UIManager.setLookAndFeel(materialTheme);
			UIManager.getLookAndFeelDefaults().put("TabbedPane[tab].height", 5);
			UIManager.getLookAndFeelDefaults().put("OptionPane.minimumSize",new Dimension(800,100));
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
					Thread.sleep(35);
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

		try {
			File dir = new File(DATABASE_DIR);
			if (!dir.exists())
				dir.mkdirs();

			ConexaoLogin.createTable(false);

			ConexaoMembro.createTable(false);

			ConexaoFinancas.createTable(false);

			File imgs = new File(Membro.IMG_PATH);
			if (!imgs.exists())
				imgs.mkdirs();

		} catch (Exception e) {
			String message = "Ocorreu um erro ao criar a base de dados... Tenta novamente!\n" + e.getMessage() + "\n"
					+ this.getClass();
			JOptionPane.showMessageDialog(null, message, "Erro", JOptionPane.ERROR_MESSAGE,
					new ImageIcon(getClass().getResource("/FC_SS.jpg")));
			Log.getInstance().printLog(message);
			e.printStackTrace();
		}

	}

	/**
	 * 
	 */
	private void createFolders() {
		File dir = new File(DATABASE_DIR);
		if (!dir.exists())
			dir.mkdirs();

		File membrosPDF = new File(Main.MEMBROS_PDF_PATH);
		if (!membrosPDF.exists())
			membrosPDF.mkdirs();

		File listDir = new File(Main.LISTAS_DIR);
		if (!listDir.exists())
			listDir.mkdirs();

		File relatoriosDir = new File(Main.RELATORIOS_DIR);
		if (!relatoriosDir.exists())
			relatoriosDir.mkdirs();

		File backdir = new File(Main.BACKUP_DIR);
		if (!backdir.exists())
			backdir.mkdirs();

		File logoImg = new File(Main.LOGO_DIR);
		if (!logoImg.exists())
			logoImg.mkdirs();

		File savedImg = new File(Main.SAVED_IMAGES);
		if (!savedImg.exists())
			savedImg.mkdirs();

		File modelos = new File(Main.MODELOS_DIR);
		if (!modelos.exists())
			modelos.mkdirs();

		File bugDir = new File(Main.BUG_REPORTS_DIR);
		if (!bugDir.exists())
			bugDir.mkdirs();
	}

	/**
	 * Caso o programa esteja a iniciar após o restauro de uma cópia de segurança,
	 * esse método trata de substituir as bases de dados e apagar as temporárias.
	 */
	private void restaurar() {
		String path = DATA_DIR + "temp/";
		File tmp = new File(path);
		if (tmp.exists()) {
			try {
				File confFile = new File(path + "conf.dad");
				File confDest = new File(Main.DATABASE_DIR + "conf.dad");
				if (confFile.exists())
					Files.copy(confFile.toPath(), confDest.toPath(), StandardCopyOption.REPLACE_EXISTING);

				File funcFile = new File(path + "logins.accdb");
				File funcDest = new File(Main.DATABASE_DIR + "logins.accdb");
				if (funcFile.exists())
					Files.copy(funcFile.toPath(), funcDest.toPath(), StandardCopyOption.REPLACE_EXISTING);

				File images = new File(path + "Imagens/");
				File imagesDest = new File(Main.DATABASE_DIR + "Imagens/");
				if (images.exists())
					FileUtils.copyDirectory(images, imagesDest);

				File membrosFile = new File(path + "membros.accdb");
				File membrosDest = new File(Main.DATABASE_DIR + "membros.accdb");
				if (membrosFile.exists())
					Files.copy(membrosFile.toPath(), membrosDest.toPath(), StandardCopyOption.REPLACE_EXISTING);

				File financasFile = new File(path + "financas.accdb");
				File financasDest = new File(Main.DATABASE_DIR + "financas.accdb");
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
