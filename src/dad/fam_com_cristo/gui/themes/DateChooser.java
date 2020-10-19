package dad.fam_com_cristo.gui.themes;

import java.util.Date;
import java.util.Locale;

import com.toedter.calendar.JDateChooser;

import dad.recursos.Utils;
import mdlaf.utils.MaterialColors;

public class DateChooser extends JDateChooser {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 12051826987899268L;

	public DateChooser() {
		super();
		this.setLocale(new Locale("pt", "BR"));
		this.setDateFormatString("dd/MM/yyyy");
		this.setMaxSelectableDate(new Date());
		this.setDate(new Date());
		this.getJCalendar().setWeekOfYearVisible(false);
		
		if(Utils.getInstance().getCurrentTheme().equals(DarkTheme.getInstance())) {
			this.getJCalendar().setForeground(MaterialColors.WHITE);
			this.getJCalendar().setDecorationBackgroundColor(MaterialColors.AMBER_800);
			this.getJCalendar().setWeekdayForeground(MaterialColors.WHITE);			
			
		} else {
			this.getJCalendar().setForeground(MaterialColors.BLACK);
		}
			
	}

}
