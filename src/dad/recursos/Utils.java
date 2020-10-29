package dad.recursos;

import java.awt.Desktop;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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

import dad.fam_com_cristo.gui.DataGui;
import dad.fam_com_cristo.gui.Login;
import dad.fam_com_cristo.gui.Main;
import dad.fam_com_cristo.gui.themes.DarkTheme;
import dad.fam_com_cristo.gui.themes.LiteTheme;
import dad.fam_com_cristo.gui.themes.Theme;
import dad.fam_com_cristo.table.FinancasPanel;
import dad.fam_com_cristo.table.MembroPanel;
import dad.fam_com_cristo.table.TableModelFinancas;
import mdlaf.MaterialLookAndFeel;
import mdlaf.animation.MaterialUIMovement;
import mdlaf.themes.MaterialTheme;
import mdlaf.utils.MaterialColors;

/**
 * @author dariopereiradp
 *
 */
public class Utils {

	public static final String DATE_FORMAT = "dd/MM/yyyy";
	private Theme current_theme;
	private static Utils INSTANCE;
	public static final String APP_THEME = "APP_THEME";
	public static final String THEME_LITE = "Lite";
	public static final String THEME_DARK = "Dark";

	public Utils() {
		FileInputStream inputStream;
		try {
			inputStream = new FileInputStream(getPropertiesFile());
			Properties prop = new Properties();
			prop.load(inputStream);
			String theme = prop.getProperty(APP_THEME);
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

	public void changeTheme(Theme theme) {
		current_theme = theme;
		MaterialLookAndFeel.changeTheme((MaterialTheme) theme);
		UIManager.getLookAndFeelDefaults().put("TabbedPane[tab].height", 5);

		SwingUtilities.updateComponentTreeUI(DataGui.getInstance());
		SwingUtilities.updateComponentTreeUI(Login.getInstance().getFrame());
//		
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

	public File getPropertiesFile() {
		return new File(Main.DATABASE_DIR + "conf.dad");
	}

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
		String name = "IB_Fam_com_Cristo-Backup-" + new SimpleDateFormat("ddMMMyyyy-HH'h'mm").format(new Date())
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

	public NumberFormat getNumberFormatCurrency() {
		NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "br"));
		format.setMinimumFractionDigits(2);
		format.setMaximumFractionDigits(4);
		format.setRoundingMode(RoundingMode.HALF_UP);
		return format;
	}

	public JFormattedTextField getNewCurrencyTextField() {
		NumberFormatter formatter = new NumberFormatter(getNumberFormatCurrency());
		formatter.setAllowsInvalid(false);
		formatter.setMinimum(0.0);
		return new JFormattedTextField(formatter);
	}

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

	public DateTimeFormatter getDateFormat() {
		return DateTimeFormatter.ofPattern(DATE_FORMAT);
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

	public String getDesdeSempreString() {
		LocalDate now = LocalDate.now();
		LocalDate init = TableModelFinancas.getInstance().getOldestDate();
		return getDateFormat().format(init) + " - " + getDateFormat().format(now);
	}

	public DatePickerSettings getDateSettings() {
		DatePickerSettings dateSettings = new DatePickerSettings(new Locale("pt", "br"));
		dateSettings.setFormatForDatesBeforeCommonEra(DATE_FORMAT);
		dateSettings.setFormatForDatesCommonEra(DATE_FORMAT);
		dateSettings.setAllowKeyboardEditing(false);
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

	public static Utils getInstance() {
		if (INSTANCE == null)
			INSTANCE = new Utils();
		return INSTANCE;
	}

}