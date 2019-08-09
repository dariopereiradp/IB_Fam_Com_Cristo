package dad.recursos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.apache.commons.lang.time.DurationFormatUtils;

import dad.fam_com_cristo.gui.DataGui;
import dad.fam_com_cristo.gui.Login;

public class SairAction implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		DataGui.getInstance().dispose();
		long time = System.currentTimeMillis() - Login.inicialTime;
		Log.getInstance().printLog("Usuário " + Login.NOME + " saiu!\nTempo de Uso: "
				+ DurationFormatUtils.formatDuration(time, "HH'h'mm'm'ss's"));
		Login.getInstance().open();

	}

}
