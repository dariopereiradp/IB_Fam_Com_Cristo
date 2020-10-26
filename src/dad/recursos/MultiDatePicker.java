package dad.recursos;

import java.time.LocalDate;

import javax.swing.JPanel;

import com.github.lgooddatepicker.optionalusertools.DateChangeListener;
import com.github.lgooddatepicker.zinternaltools.DateChangeEvent;

import dad.fam_com_cristo.gui.DataGui;
import dad.fam_com_cristo.gui.themes.DateChooser;

public class MultiDatePicker extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8633388726734290286L;
	private DateChooser initDateChooser;
	private DateChooser finalDateChooser;

	public MultiDatePicker() {
		initDateChooser = new DateChooser();

		finalDateChooser = new DateChooser();

		initDateChooser.addDateChangeListener(new Listener());
		finalDateChooser.addDateChangeListener(new Listener());

		initDateChooser.getSettings().setAllowEmptyDates(true);
		initDateChooser.getSettings().setDateRangeLimits(null, LocalDate.now());
		initDateChooser.setDate(null);

		finalDateChooser.getSettings().setDateRangeLimits(null, LocalDate.now());
		finalDateChooser.getSettings().setAllowEmptyDates(true);
		finalDateChooser.setDate(null);

		initDateChooser.getComponentDateTextField().setToolTipText("Data inicial");
		finalDateChooser.getComponentDateTextField().setToolTipText("Data final");

		add(initDateChooser);
		add(finalDateChooser);

	}

	private class Listener implements DateChangeListener {

		@Override
		public void dateChanged(DateChangeEvent event) {
			if (event.getSource().equals(initDateChooser)) {
				finalDateChooser.getSettings().setDateRangeLimits(event.getNewDate(), LocalDate.now());
				if (initDateChooser.getDate() != null && finalDateChooser.getDate() != null
						&& finalDateChooser.getDate().isBefore(initDateChooser.getDate()))
					finalDateChooser.setDate(initDateChooser.getDate());
			} else {
				if (event.getNewDate() != null)
					initDateChooser.getSettings().setDateRangeLimits(null, event.getNewDate());
				else
					initDateChooser.getSettings().setDateRangeLimits(null, LocalDate.now());
			}
			DataGui.getInstance().newFilter(DataGui.getInstance().getPesquisa().getText().toLowerCase());
		}
	}
	
	public DateChooser getInitDateChooser() {
		return initDateChooser;
	}
	
	public DateChooser getFinalDateChooser() {
		return finalDateChooser;
	}
	
	public LocalDate getInitDate() {
		return initDateChooser.getDate();
	}
	
	public LocalDate getFinalDate() {
		return finalDateChooser.getDate();
	}
}