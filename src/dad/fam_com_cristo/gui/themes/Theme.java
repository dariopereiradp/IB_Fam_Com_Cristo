package dad.fam_com_cristo.gui.themes;

import javax.swing.plaf.ColorUIResource;

/**
 * Proporciona alguns m�todos para configurar facilmente as cores ao longo do programa
 * @author dariopereiradp
 *
 */
public interface Theme {
	
	String getThemeName();
	
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

	/**
	 * Cor de fundo do calend�rio
	 * @return
	 */
	ColorUIResource getColorBackgroundCalendar();

	/**
	 * Cor dos campos
	 * @return
	 */
	ColorUIResource getColorFields();
}
