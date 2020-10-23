package dad.fam_com_cristo.gui.themes;

import javax.swing.plaf.ColorUIResource;

public interface Theme {
	
	/**
	 * Cor das linhas pares da tabela
	 * @return
	 */
	ColorUIResource getColorLinhasPares ();
	
	/**
	 * Cor das linhas �mpares da tabela
	 * @return
	 */
	ColorUIResource getColorLinhasImpares();
	
	/**
	 * Cor do highlight do filtro
	 * @return
	 */
	ColorUIResource getColorHighlight();
	
	/**
	 * Cor dos �cones
	 * @return
	 */
	ColorUIResource getColorIcons();
	
	/**
	 * Cor da fonte dos hints (dicas)
	 * @return
	 */
	ColorUIResource getColorHint();

	String getThemeName();
}
