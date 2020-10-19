package dad.recursos;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.SwingUtilities;
import dad.fam_com_cristo.gui.DataGui;
import dad.fam_com_cristo.gui.Main;
import dad.fam_com_cristo.gui.themes.DarkTheme;
import dad.fam_com_cristo.gui.themes.LiteTheme;
import dad.fam_com_cristo.gui.themes.Theme;
import dad.fam_com_cristo.table.MembroPanel;
import mdlaf.MaterialLookAndFeel;
import mdlaf.animation.MaterialUIMovement;
import mdlaf.themes.MaterialTheme;
import mdlaf.utils.MaterialColors;

/**
 * @author dariopereiradp
 *
 */
public class Utils {
	
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
		MaterialLookAndFeel.changeTheme((MaterialTheme) theme);
		SwingUtilities.updateComponentTreeUI(DataGui.getInstance());
		SwingUtilities.updateComponentTreeUI(MembroPanel.getInstance());
		current_theme = theme;
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
	 * Personaliza o aspecto dos botões.
	 * 
	 * @param jb - botão a ser personalizado.
	 */
	public static void personalizarBotao(JButton jb) {
		jb.setFont(new Font("Roboto", Font.PLAIN, 15));
		MaterialUIMovement.getMovement(jb, MaterialColors.GRAY_300, 5, 1000 / 30);
	}
	
	public static Utils getInstance() {
		if(INSTANCE == null)
			INSTANCE = new Utils();
		return INSTANCE;
	}

}