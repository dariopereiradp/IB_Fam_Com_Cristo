package dad.fam_com_cristo.table.cells;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import dad.fam_com_cristo.gui.DataGui;
import dad.recursos.Utils;

/**
 * Classe para renderer das c�lulas de uma JTable que s�o edit�veis, ou seja,
 * vai aparecer o �cone de edi��o no lado esquerdo. Tamb�m est� adaptada para
 * pesquisa e filtragem, desenhando um ret�ngulo amarelo em volta do filtro.
 * 
 * @author D�rio Pereira
 *
 */
public class CellRenderer extends DefaultTableCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9064760396702211972L;
	private final ImageIcon editIcon = new ImageIcon(getClass().getResource("/edit.png"));

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
	public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean hasFocus,
			int row, int column) {
		super.getTableCellRendererComponent(table, value, selected, hasFocus, row, column);
		this.setIcon(table.isCellEditable(row, column) ? editIcon : null);
		setBorder(BorderFactory.createRaisedSoftBevelBorder());

		if (value instanceof LocalDate)
			this.setValue(((LocalDate) value).format(Utils.getInstance().getDateFormat()));
		return this;
	}
}