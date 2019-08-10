package dad.recursos;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import dad.fam_com_cristo.gui.DataGui;
import mdlaf.utils.MaterialColors;

public class CellRenderer2 extends DefaultTableCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9064760396702211972L;
	private final ImageIcon right = new ImageIcon(getClass().getResource("/right.png"));
	private final ImageIcon wrong = new ImageIcon(getClass().getResource("/wrong.png"));

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
		g.fillRect(pmw + 22, 5, w - 1, getHeight() - 10);
		g.setColor(getForeground());
		Rectangle r = g.getFontMetrics().getStringBounds(match, g).getBounds();
		g.drawString(match, pmw + 21, -r.y + 6);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean hasFocus,
			int row, int column) {
		super.getTableCellRendererComponent(table, value, selected, hasFocus, row, column);
/*
		Emprestimo emp = TableModelEmprestimo.getInstance().getEmprestimo(table.convertRowIndexToModel(row));

		if (emp.getMulta() == 0.0 || emp.isPago())
			this.setIcon(right);
		else if(emp.getMulta()>0 || !emp.isPago())
			this.setIcon(wrong);
		*/
		return this;
	}
}