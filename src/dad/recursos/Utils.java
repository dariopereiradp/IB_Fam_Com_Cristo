package dad.recursos;

import java.awt.Desktop;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.NumberFormatter;

import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.DatePickerSettings.DateArea;

import dad.fam_com_cristo.Main;
import dad.fam_com_cristo.gui.DataGui;
import dad.fam_com_cristo.gui.Login;
import dad.fam_com_cristo.gui.themes.DarkTheme;
import dad.fam_com_cristo.gui.themes.LiteTheme;
import dad.fam_com_cristo.gui.themes.Theme;
import dad.fam_com_cristo.table.FinancasPanel;
import dad.fam_com_cristo.table.MembroPanel;
import dad.fam_com_cristo.table.models.TableModelFinancas;
import dad.fam_com_cristo.types.enumerados.EstatisticaPeriodos;
import dad.fam_com_cristo.types.enumerados.ImageFormats;
import mdlaf.MaterialLookAndFeel;
import mdlaf.animation.MaterialUIMovement;
import mdlaf.themes.MaterialTheme;
import mdlaf.utils.MaterialColors;

/**
 * Classe que agrupa diversas funções que são de utilidade em diferentes classes
 * do programa
 * 
 * @author dariopereiradp
 *
 */
public class Utils {

	private static Utils INSTANCE;
	private static final String DATE_FORMAT = "dd/MM/yyyy";
	private static final String DATETIME_FORMAT = "dd/MM/yyyy 'às' HH'h'mm'm'ss's'";
	public static final String APP_THEME = "APP_THEME";
	public static final String THEME_LITE = "Lite";
	public static final String THEME_DARK = "Dark";
	private Theme current_theme;

	public Utils() {
		FileInputStream inputStream;
		try {
			if (getPropertiesFile().exists())
				inputStream = new FileInputStream(getPropertiesFile());
			else
				inputStream = new FileInputStream(createConfFile());
			Properties prop = new Properties();
			prop.load(inputStream);
			String theme = prop.getProperty(APP_THEME);
			inputStream.close();
			if (theme.equals(THEME_LITE))
				current_theme = LiteTheme.getInstance();
			else
				current_theme = DarkTheme.getInstance();
		} catch (IOException e) {
			current_theme = DarkTheme.getInstance();
			Log.getInstance().printLog(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Cria o ficheiro de configurações, se ainda não existir
	 * 
	 * @return
	 */
	public static File createConfFile() {
		File conf = null;
		try {
			conf = getPropertiesFile();
			if (conf.createNewFile()) {
				FileOutputStream output = new FileOutputStream(conf);
				Properties prop = new Properties();
				prop.setProperty(Utils.APP_THEME, Utils.THEME_DARK);
				prop.setProperty(Main.PASTOR, "");
				prop.store(output, Main.AVISO_INI);
				output.close();
			}
			return conf;

		} catch (IOException | InputMismatchException e1) {
			Log.getInstance().printLog("Erro ao carregar configurações! - " + e1.getMessage());
			e1.printStackTrace();
			return null;
		}

	}

	/**
	 * Altera o tema do programa, garantindo que todos os componentes são
	 * devidamente recriados (incluindo as cores dos ícones dos botões
	 * 
	 * @param theme
	 */
	public void changeTheme(Theme theme) {
		current_theme = theme;
		MaterialLookAndFeel.changeTheme((MaterialTheme) theme);
		UIManager.getLookAndFeelDefaults().put("TabbedPane[tab].height", 5);

		SwingUtilities.updateComponentTreeUI(DataGui.getInstance());
		SwingUtilities.updateComponentTreeUI(Login.getInstance().getFrame());

		DataGui.getInstance().getContentPane().removeAll();
		DataGui.getInstance().recreate();
		MembroPanel.getInstance().removeAll();
		MembroPanel.getInstance().recreate();
		FinancasPanel.getInstance().removeAll();
		FinancasPanel.getInstance().recreate();
		Login.getInstance().getFrame().getContentPane().removeAll();
		Login.getInstance().recreate();

		try {
			FileInputStream input = new FileInputStream(getPropertiesFile());
			Properties prop = new Properties();
			prop.load(input);
			input.close();
			FileOutputStream output = new FileOutputStream(getPropertiesFile());
			prop.setProperty(APP_THEME, theme.getThemeName());
			prop.store(output, Main.AVISO_INI);
			output.close();
		} catch (IOException e) {

		}
	}

	/**
	 * 
	 * @return o ficheiro de configurações
	 */
	public static File getPropertiesFile() {
		return new File(Main.DATABASE_DIR + "conf.dad");
	}

	/**
	 * 
	 * @return o tema atual em uso no programa (Light ou Dark)
	 */
	public Theme getCurrentTheme() {
		return current_theme;
	}

	/**
	 * Cria um backup das bases de dados e imagens.
	 */
	public void backup() {
		String message = "Deseja criar uma cópia de segurança de todas as bases de dados do programa?"
				+ "\nObs: A cópia irá incluir as configurações, membros, finanças e imagens, que serão salvos em um único ficheiro.\n"
				+ "Não modifique esse ficheiro!\n"
				+ "Você deve copiá-lo para um lugar seguro (por exemplo, uma pen-drive) para mais tarde ser possível restaurar,\n"
				+ "caso o computador seja formatado ou você pretenda usar o programa em outro computador.";
		int ok = JOptionPane.showOptionDialog(null, message, "Cópia de Segurança", JOptionPane.YES_NO_OPTION,
				JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/FC_SS.jpg")), Main.OPTIONS,
				Main.OPTIONS[0]);
		if (ok == JOptionPane.OK_OPTION) {
			backupDirect();
			JOptionPane.showMessageDialog(null, "Cópia de segurança salva com sucesso na pasta:\n" + Main.BACKUP_DIR,
					"Cópia de Segurança - Sucesso", JOptionPane.OK_OPTION,
					new ImageIcon(getClass().getResource("/FC_SS.jpg")));
			try {
				Desktop.getDesktop().open(new File(Main.BACKUP_DIR));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Cria um backup da base de dados diretamente.
	 * 
	 * @return o nome do ficheiro de backup criado.
	 */
	public String backupDirect() {
		String name = "IB_Fam_com_Cristo-Backup-" + DateTimeFormatter.ofPattern("ddMMMyyyy-HH'h'mm").format(LocalDateTime.now())
				+ ".fccb";
		ZipCompress.compress(Main.DATABASE_DIR, name, Main.BACKUP_DIR);
		return Main.BACKUP_DIR + name;
	}

	/**
	 * Personaliza o aspecto dos botões.
	 * 
	 * @param jb - botão a ser personalizado.
	 */
	public static void personalizarBotao(JButton jb) {
		jb.setFont(new Font("Roboto", Font.PLAIN, 15));
		jb.setBorder(new RoundedBorder(10));
		MaterialUIMovement.getMovement(jb, MaterialColors.GRAY_300, 5, 1000 / 30);
	}

	/**
	 * 
	 * @return o formato de moeda do Brasil
	 */
	public NumberFormat getNumberFormatCurrency() {
		NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "br"));
		format.setMinimumFractionDigits(2);
		format.setMaximumFractionDigits(4);
		format.setRoundingMode(RoundingMode.HALF_UP);
		return format;
	}

	/**
	 * 
	 * @return um novo JFormattedTextField com a máscara da moeda brasileira
	 */
	public JFormattedTextField getNewCurrencyTextField() {
		NumberFormatter formatter = new NumberFormatter(getNumberFormatCurrency());
		formatter.setAllowsInvalid(false);
		formatter.setMinimum(0.0);
		return new JFormattedTextField(formatter);
	}

	/**
	 * 
	 * @param valor - String com a máscara da moeda e o valor
	 * @return um valor BigDecimal correspondente à String passada como parametro
	 */
	public BigDecimal getNumberFromFormat(Object valor) {
		BigDecimal value = new BigDecimal("0.0");
		try {
			value = new BigDecimal(getNumberFormatCurrency().parse((String) valor).toString());
		} catch (ParseException e) {
			Log.getInstance().printLog(e.getMessage());
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * 
	 * @return o formatador de Data no formato dd/MM/yyyy
	 */
	public DateTimeFormatter getDateFormat() {
		return DateTimeFormatter.ofPattern(DATE_FORMAT);
	}

	/**
	 * 
	 * @return o formatador de Data e Hora no formato "dd/MM/yyyy 'às'
	 *         HH'h'mm'm'ss's'"
	 */
	public DateTimeFormatter getDateTimeFormat() {
		return DateTimeFormatter.ofPattern(DATETIME_FORMAT);
	}

	public String getMesAtualString() {
		LocalDate init = EstatisticaPeriodos.MES_ATUAL.getInit();
		LocalDate end = EstatisticaPeriodos.MES_ATUAL.getEnd();
		return getDateFormat().format(init) + " - " + getDateFormat().format(end);
	}

	public String getMesAnteriorString() {
		LocalDate init = EstatisticaPeriodos.MES_ANTERIOR.getInit();
		LocalDate fim = EstatisticaPeriodos.MES_ANTERIOR.getEnd();
		return getDateFormat().format(init) + " - " + getDateFormat().format(fim);
	}

	public String getAnoAtualString() {
		LocalDate init = EstatisticaPeriodos.ANO_ATUAL.getInit();
		LocalDate end = EstatisticaPeriodos.ANO_ATUAL.getEnd();
		return getDateFormat().format(init) + " - " + getDateFormat().format(end);
	}

	public String getAnoAnteriorString() {
		LocalDate init = EstatisticaPeriodos.ANO_ANTERIOR.getInit();
		LocalDate fim = EstatisticaPeriodos.ANO_ANTERIOR.getEnd();
		return getDateFormat().format(init) + " - " + getDateFormat().format(fim);
	}

	/**
	 * 
	 * @return uma String com a data da primeira transação e a data de hoje
	 */
	public String getDesdeSempreString() {
		LocalDate now = LocalDate.now();
		LocalDate init = TableModelFinancas.getInstance().getOldestDate();
		return getDateFormat().format(init) + " - " + getDateFormat().format(now);
	}

	/**
	 * Configura com o DatePicker padrão do programa (com cores para o tema, local,
	 * formato e outros)
	 * 
	 * @return
	 */
	public DatePickerSettings getDateSettings() {
		DatePickerSettings dateSettings = new DatePickerSettings(new Locale("pt", "br"));
		dateSettings.setFormatForDatesBeforeCommonEra(DATE_FORMAT);
		dateSettings.setFormatForDatesCommonEra(DATE_FORMAT);
		dateSettings.setAllowEmptyDates(false);

		dateSettings.setColor(DateArea.TextMonthAndYearMenuLabels, getCurrentTheme().getColorIcons());
		dateSettings.setColor(DateArea.TextMonthAndYearNavigationButtons, getCurrentTheme().getColorIcons());
		dateSettings.setColor(DateArea.TextTodayLabel, getCurrentTheme().getColorIcons());
		dateSettings.setColor(DateArea.TextClearLabel, getCurrentTheme().getColorIcons());
		dateSettings.setColor(DateArea.CalendarTextNormalDates, getCurrentTheme().getColorIcons());

		dateSettings.setColor(DateArea.BackgroundCalendarPanelLabelsOnHover, MaterialColors.LIGHT_BLUE_400);
		dateSettings.setColor(DateArea.TextCalendarPanelLabelsOnHover, MaterialColors.WHITE);

		dateSettings.setColor(DateArea.CalendarBackgroundNormalDates, getCurrentTheme().getColorLinhasImpares());
		dateSettings.setColor(DateArea.BackgroundOverallCalendarPanel, getCurrentTheme().getColorBackgroundCalendar());
		dateSettings.setColor(DateArea.BackgroundMonthAndYearMenuLabels, getCurrentTheme().getColorLinhasImpares());
		dateSettings.setColor(DateArea.BackgroundTodayLabel, getCurrentTheme().getColorLinhasImpares());
		dateSettings.setColor(DateArea.BackgroundClearLabel, getCurrentTheme().getColorLinhasImpares());
		dateSettings.setColor(DateArea.BackgroundMonthAndYearNavigationButtons,
				getCurrentTheme().getColorLinhasImpares());
		dateSettings.setColor(DateArea.CalendarBackgroundSelectedDate, MaterialColors.LIGHT_BLUE_400);

		dateSettings.setColor(DateArea.TextFieldBackgroundValidDate, getCurrentTheme().getColorFields());
		dateSettings.setColor(DateArea.DatePickerTextValidDate, getCurrentTheme().getColorIcons());

		return dateSettings;
	}

	/**
	 * 
	 * @return o nome do pastor, presente no ficheiro de configurações
	 */
	public String getPastorName() {
		FileInputStream input;
		String pastorName = "";
		try {
			input = new FileInputStream(getPropertiesFile());
			Properties prop = new Properties();
			prop.load(input);
			pastorName = prop.getProperty(Main.PASTOR, "");
			input.close();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		return pastorName;
	}

	/**
	 * Exporta o logotipo da igreja de acordo com o formato indicado
	 * 
	 * @param format
	 */
	public static void exportLogo(ImageFormats format) {
		try {
			InputStream stream = null;
			String name = Main.LOGO_DIR + Main.SIGLA;
			switch (format) {
			case JPG:
				name += ImageFormats.JPG.getFormat();
				stream = Utils.class.getResourceAsStream("/FC.jpg");
				break;
			case PNG:
				name += ImageFormats.PNG.getFormat();
				stream = Utils.class.getResourceAsStream("/FC-T-Big.png");
				break;
			case SVG:
				name += ImageFormats.SVG.getFormat();
				stream = Utils.class.getResourceAsStream("/FC.svg");
				break;
			case PDF:
				name += ImageFormats.PDF.getFormat();
				stream = Utils.class.getResourceAsStream("/FC.pdf");
				break;
			default:
				break;
			}
			File img = new File(name);

			if (stream != null) {
				Files.copy(stream, img.toPath(), StandardCopyOption.REPLACE_EXISTING);
				stream.close();
				String message = "O logotipo foi exportado com sucesso para a pasta:\n" + Main.LOGO_DIR
						+ "\nVocê quer abrir a imagem agora?";
				askMessage(message, Main.LOGO_DIR, name);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.getInstance().printLog(e.getMessage());
		}
	}

	/**
	 * Exporta uma imagem para a localização indicada
	 * 
	 * @param source
	 * @param target
	 * @throws IOException
	 */
	public static void exportImg(InputStream source, File target) throws IOException {
		if (source != null) {
			Files.copy(source, target.toPath(), StandardCopyOption.REPLACE_EXISTING);
			source.close();
			String message = "A imagem foi salva com sucesso na pasta:\n" + Main.SAVED_IMAGES
					+ "\nVocê quer abrir a imagem agora?";
			askMessage(message, Main.SAVED_IMAGES, target.getPath());

		}
	}

	/**
	 * Exporta um modelo de ofício (em .docx) para a localização indicada
	 */
	public static void exportModelo() {
		try {
			String filename = Main.MODELOS_DIR + "IBFC_Modelo_Oficio.docx";
			File modelo = new File(filename);
			InputStream stream = Utils.class.getResourceAsStream("/IBFC_Modelo.docx");
			Files.copy(stream, modelo.toPath(), StandardCopyOption.REPLACE_EXISTING);
			stream.close();
			String message = "O modelo de ofício foi exportado com sucesso!\n"
					+ "Foi salvo um documento do Word (que deve ser editado) na pasta:\n" + Main.MODELOS_DIR
					+ "\nVocê quer abrir o documento agora?";
			askMessage(message, Main.MODELOS_DIR, filename);
		} catch (Exception e1) {
			e1.printStackTrace();
			Log.getInstance().printLog(e1.getMessage());
		}
	}

	/**
	 * Mostra JOptionPane informando que foi salvo com sucesso e perguntando se quer
	 * abrir o ficheiro
	 * 
	 * @param message
	 * @throws IOException
	 */
	public static void askMessage(String message, String dir, String filepath) throws IOException {

		int ok = JOptionPane.showOptionDialog(DataGui.getInstance(), message, "Criado com sucesso",
				JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,
				new ImageIcon(DataGui.getInstance().getClass().getResource("/FC_SS.jpg")), Main.OPTIONS,
				Main.OPTIONS[1]);
		Log.getInstance().printLog(message);
		if (ok == JOptionPane.YES_OPTION) {
			Desktop.getDesktop().open(new File(dir));
			Desktop.getDesktop().open(new File(filepath));
		}
	}

	public static Utils getInstance() {
		if (INSTANCE == null)
			INSTANCE = new Utils();
		return INSTANCE;
	}

}