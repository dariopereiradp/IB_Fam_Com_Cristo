package dad.fam_com_cristo.gui.themes;

import com.github.lgooddatepicker.components.DatePicker;

import dad.recursos.Utils;
import mdlaf.utils.MaterialColors;
import mdlaf.utils.MaterialImageFactory;
import mdlaf.utils.icons.MaterialIconFont;

/**
 * DatePicker com algumas configura��es, para evitar repeti��o de c�digo
 * @author dariopereiradp
 *
 */
public class DateChooser extends DatePicker {

	/**
	 * 
	 */
	private static final long serialVersionUID = 12051826987899268L;

	public DateChooser() {
		super();

		setSettings(Utils.getInstance().getDateSettings());
		
		getComponentDateTextField().setDisabledTextColor(MaterialColors.GRAY_600);
		getComponentDateTextField().setForeground(Utils.getInstance().getCurrentTheme().getColorIcons());
		getComponentToggleCalendarButton().setText("");
		getComponentToggleCalendarButton().setIcon(MaterialImageFactory.getInstance().getImage(
                MaterialIconFont.DATE_RANGE,
                Utils.getInstance().getCurrentTheme().getColorIcons()));
		this.setDateToToday();

	}
}
