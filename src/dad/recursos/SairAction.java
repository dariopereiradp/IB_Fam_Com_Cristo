package dad.recursos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.apache.commons.lang3.time.DurationFormatUtils;

import dad.fam_com_cristo.gui.DataGui;
import dad.fam_com_cristo.gui.Login;

/**
 * Classe que representa um ActionListener para fazer Logout do funcion�rio.
 * @author D�rio Pereira
 *
 */
public class SairAction implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		DataGui.getInstance().dispose();
		long time = System.currentTimeMillis() - Login.inicialTime;
		Log.getInstance().printLog("Usu�rio " + Login.NOME + " saiu!\nTempo de Uso: "
				+ DurationFormatUtils.formatDuration(time, "HH'h'mm'm'ss's"));
		Login.getInstance().open();

	}

}
