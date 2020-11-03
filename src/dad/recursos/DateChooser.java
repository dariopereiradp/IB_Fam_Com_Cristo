package dad.recursos;

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import com.github.lgooddatepicker.components.DatePicker;

import mdlaf.utils.MaterialColors;
import mdlaf.utils.MaterialImageFactory;
import mdlaf.utils.icons.MaterialIconFont;

/**
 * DatePicker com algumas configurações, para evitar repetição de código
 * 
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
		getComponentToggleCalendarButton().setIcon(MaterialImageFactory.getInstance()
				.getImage(MaterialIconFont.DATE_RANGE, Utils.getInstance().getCurrentTheme().getColorIcons()));
		
		InputMap iMap = getComponentDateTextField().getInputMap(JComponent.WHEN_FOCUSED);
		iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), KeyEvent.getKeyText(KeyEvent.VK_ENTER));
		ActionMap aMap = getComponentDateTextField().getActionMap();
		aMap.put(KeyEvent.getKeyText(KeyEvent.VK_ENTER), new AbstractAction() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1798319452088234825L;

			@Override
			public void actionPerformed(ActionEvent e) {
				getComponentDateTextField().dispatchEvent(new FocusEvent(DateChooser.this, Event.LOST_FOCUS));				
			}
		});
		
		
		this.setDateToToday();

	}
}
