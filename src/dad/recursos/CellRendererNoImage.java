package dad.recursos;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import dad.fam_com_cristo.gui.DataGui;

/** Classe para renderer das células de uma JTable que não são editáveis.
 * Também está adaptada para pesquisa e filtragem, desenhando um retângulo amarelo em volta do filtro.
 * 
 * @author Dário Pereira
 *
 */
public class CellRendererNoImage extends DefaultTableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1672778921016249533L;

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		String filter = DataGui.getInstance().getPesquisa().getText().toLowerCase().trim();
		if (filter.length() == 0) {
			return;
		}
		String text = getText().toLowerCase();
		int index = text.indexOf(filter);
		if (index == -1) {
			return;
		}
		String preMatch = getText().substring(0, index);
		String match = getText().substring(preMatch.length(), preMatch.length() + filter.length());
		int pmw = g.getFontMetrics().stringWidth(preMatch);
		int w = g.getFontMetrics().stringWidth(match);
		g.setColor(Utils.getInstance().getCurrentTheme().getColorHighlight());
		g.fillRect(pmw + 23, 8, w, getHeight() - 10);
		g.setColor(getForeground());
		Rectangle r = g.getFontMetrics().getStringBounds(match, g).getBounds();
		g.drawString(match, pmw + 23, -r.y + 8);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		setBorder(BorderFactory.createRaisedSoftBevelBorder());
		return this;
	}
	
	
}
