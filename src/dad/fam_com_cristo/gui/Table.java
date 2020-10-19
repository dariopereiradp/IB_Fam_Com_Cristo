/**
 * 
 */
package dad.fam_com_cristo.gui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

/**
 * @author dariopereiradp
 *
 */
public class Table extends JTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4473733357164145475L;
	private AbstractTableModel model;
	private String[] columnToolTips;

	public Table(AbstractTableModel model, String[] columnToolTips) {
		super(model);
		this.model = model;
		this.columnToolTips = columnToolTips;
		personalizarHeader();
		setPreferredScrollableViewportSize(new Dimension(800, 600));
		setFillsViewportHeight(true);
		setAutoCreateRowSorter(true);
		getTableHeader().setReorderingAllowed(false);
		setRowHeight(30);
	}

	@Override
	public boolean isCellEditable(int data, int columns) {
		return true;
	}

	// Implement table cell tool tips.
	@Override
	public String getToolTipText(MouseEvent e) {
		String tip = null;
		Point p = e.getPoint();
		int rowIndex = rowAtPoint(p);
		int colIndex = columnAtPoint(p);
		int realColumnIndex = convertColumnIndexToModel(colIndex);
		if (rowIndex != -1) {
			int realRowIndex = convertRowIndexToModel(rowIndex);
			tip = String.valueOf(model.getValueAt(realRowIndex, realColumnIndex));
		} else
			tip = null;
		return tip;
	}

	// Implement table header tool tips.
	@Override
	protected JTableHeader createDefaultTableHeader() {
		return new JTableHeader(columnModel) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -6962458419476848334L;

			public String getToolTipText(MouseEvent e) {
				@SuppressWarnings("unused")
				String tip = null;
				Point p = e.getPoint();
				int index = columnModel.getColumnIndexAtX(p.x);
				int realIndex = columnModel.getColumn(index).getModelIndex();
				return columnToolTips[realIndex];
			}
		};
	}

	public void personalizarHeader() {
		TableCellRenderer tcr = getTableHeader().getDefaultRenderer();
		getTableHeader().setDefaultRenderer(new TableHeaderWithSortIcons(tcr));
	}
}
