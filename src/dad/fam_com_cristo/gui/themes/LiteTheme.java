/**
 * 
 */
package dad.fam_com_cristo.gui.themes;

import javax.swing.plaf.ColorUIResource;

import dad.recursos.Utils;
import mdlaf.themes.MaterialLiteTheme;
import mdlaf.utils.MaterialColors;

/**
 * @author dariopereiradp
 *
 */
public class LiteTheme extends MaterialLiteTheme implements Theme {
	
	private static LiteTheme INSTANCE;

	@Override
	public ColorUIResource getColorLinhasPares() {
		return MaterialColors.WHITE;
	}

	@Override
	public ColorUIResource getColorLinhasImpares() {
		return MaterialColors.GRAY_100;
	}
	
	@Override
	public ColorUIResource getColorHighlight() {
		return MaterialColors.YELLOW_400;
	}

	public static LiteTheme getInstance() {
		if(INSTANCE == null)
			INSTANCE = new LiteTheme();
		return INSTANCE;
	}

	@Override
	public String getThemeName() {
		return Utils.THEME_LITE;
	}

	

}
