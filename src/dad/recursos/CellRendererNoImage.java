package dad.recursos;

import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.table.DefaultTableCellRenderer;

import dad.fam_com_cristo.gui.DataGui;
import mdlaf.utils.MaterialColors;

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
		g.setColor(MaterialColors.YELLOW_A200);
		g.fillRect(pmw + 1, 5, w - 1, getHeight() - 10);
		g.setColor(getForeground());
		Rectangle r = g.getFontMetrics().getStringBounds(match, g).getBounds();
		g.drawString(match, pmw + 1, -r.y + 6);
	}
}
