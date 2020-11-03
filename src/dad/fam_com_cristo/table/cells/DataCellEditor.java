package dad.fam_com_cristo.table.cells;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.time.LocalDate;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.KeyStroke;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.tableeditors.DateTableEditor;

import dad.recursos.Utils;
import mdlaf.utils.MaterialImageFactory;
import mdlaf.utils.icons.MaterialIconFont;

/**
 * Classe para representar uma célula com input de data. É uma extensão do
 * DataTableEditor, com algumas configurações predefinidas, para poupar código.
 * 
 * @author dariopereiradp
 *
 */
public class DataCellEditor extends DateTableEditor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3462212303587972563L;

	public DataCellEditor() {
		super(false, true, true);
		getDatePicker().setSettings(Utils.getInstance().getDateSettings());
		this.clickCountToEdit = 2;
		DatePickerSettings settings = getDatePickerSettings();
		settings.setGapBeforeButtonPixels(0);
		settings.setSizeTextFieldMinimumWidthDefaultOverride(false);
		settings.setSizeTextFieldMinimumWidth(20);

		getDatePickerSettings().setDateRangeLimits(null, LocalDate.now());
		getDatePicker().getComponentToggleCalendarButton().setText("");
		getDatePicker().getComponentToggleCalendarButton().setIcon(MaterialImageFactory.getInstance()
				.getImage(MaterialIconFont.DATE_RANGE, Utils.getInstance().getCurrentTheme().getColorIcons()));

		InputMap iMap = getDatePicker().getComponentDateTextField().getInputMap(JComponent.WHEN_FOCUSED);
		iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), KeyEvent.getKeyText(KeyEvent.VK_ENTER));
		ActionMap aMap = getDatePicker().getComponentDateTextField().getActionMap();
		aMap.put(KeyEvent.getKeyText(KeyEvent.VK_ENTER), new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				stopCellEditing();
			}
		});

	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean hasFocus,
			int row, int column) {
		DatePicker c = (DatePicker) super.getTableCellRendererComponent(table, value, selected, hasFocus, row, column);
		return c;
	}
}
