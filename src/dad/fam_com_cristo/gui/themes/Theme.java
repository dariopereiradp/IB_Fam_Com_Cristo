package dad.fam_com_cristo.gui.themes;

import javax.swing.plaf.ColorUIResource;

public interface Theme {
	
	/**
	 * Cor das linhas pares da tabela
	 * @return
	 */
	ColorUIResource getColorLinhasPares ();
	
	/**
	 * Cor das linhas ímpares da tabela
	 * @return
	 */
	ColorUIResource getColorLinhasImpares();

	String getThemeName();

}
