package dad.fam_com_cristo.table.cells;

import java.awt.Component;
import java.time.LocalDate;

import javax.swing.JTable;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.tableeditors.DateTableEditor;

import dad.recursos.Utils;
import mdlaf.utils.MaterialImageFactory;
import mdlaf.utils.icons.MaterialIconFont;

public class DataCellEditor extends DateTableEditor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3462212303587972563L;

	public DataCellEditor() {
		super(false, true, true);
		getDatePicker().setSettings(Utils.getInstance().getDateSettings());
		DatePickerSettings settings = getDatePickerSettings();
		settings.setGapBeforeButtonPixels(0);
		settings.setSizeTextFieldMinimumWidthDefaultOverride(false);
		settings.setSizeTextFieldMinimumWidth(20);

		getDatePickerSettings().setDateRangeLimits(null, LocalDate.now());
		getDatePicker().getComponentToggleCalendarButton().setText("");
		getDatePicker().getComponentToggleCalendarButton().setIcon(MaterialImageFactory.getInstance()
				.getImage(MaterialIconFont.DATE_RANGE, Utils.getInstance().getCurrentTheme().getColorIcons()));
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean hasFocus,
			int row, int column) {
		DatePicker c = (DatePicker) super.getTableCellRendererComponent(table, value, selected, hasFocus, row, column);

		return c;
	}

}
