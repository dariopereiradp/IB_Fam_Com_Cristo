/**
 * 
 */
package dad.fam_com_cristo.gui.themes;

import javax.swing.plaf.ColorUIResource;

import dad.recursos.Utils;
import mdlaf.themes.JMarsDarkTheme;
import mdlaf.utils.MaterialColors;

/**
 * @author dariopereiradp
 *
 */
public class DarkTheme extends JMarsDarkTheme implements Theme {

	private static DarkTheme INSTANCE;

	public static DarkTheme getInstance() {
		if (INSTANCE == null)
			INSTANCE = new DarkTheme();
		return INSTANCE;
	}

	@Override
	protected void installColor() {
		super.installColor();
		this.backgroundPrimary = MaterialColors.GRAY_900;
		
		this.buttonDefaultTextColor = MaterialColors.WHITE;
		this.buttonTextColor = MaterialColors.WHITE;

		this.selectedForegroundComboBox = MaterialColors.WHITE;

		this.backgroundTextField = MaterialColors.GRAY_800;
		this.inactiveBackgroundTextField = new ColorUIResource(41, 41, 41);
		super.disabledBackgroudnTextField = new ColorUIResource(41, 41, 41);
		super.disabledForegroundTextField = MaterialColors.WHITE;

		this.backgroundTableHeader = MaterialColors.AMBER_900;
		super.backgroundTable = getColorLinhasPares();
		super.alternateRowBackgroundTable = getColorLinhasImpares();
		super.selectionBackgroundTable = MaterialColors.bleach(this.highlightBackgroundPrimary, 0.2f);
		
		 this.trackColorScrollBar = MaterialColors.GRAY_900;

	}

	@Override
	public ColorUIResource getColorLinhasPares() {
		return MaterialColors.GRAY_800;
	}

	@Override
	public ColorUIResource getColorLinhasImpares() {
		return MaterialColors.GRAY_900;
	}

	@Override
	public String getThemeName() {
		return Utils.THEME_DARK;
	}

}
