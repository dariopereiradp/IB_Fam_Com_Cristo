/**
 * 
 */
package dad.fam_com_cristo.table;

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
	private boolean[] cellEditable;

	public Table(AbstractTableModel model, String[] columnToolTips, boolean cellEditable) {
		super(model);
		boolean[] cellEditableValues = new boolean[columnToolTips.length];
		for (int i = 0; i < cellEditableValues.length; i++) {
			cellEditableValues[i] = cellEditable;
		}
		init(model, columnToolTips, cellEditableValues);
	}

	public Table(AbstractTableModel model, String[] columnToolTips, boolean[] cellEditable) {
		super(model);
		init(model, columnToolTips, cellEditable);
	}

	private void init(AbstractTableModel model, String[] columnToolTips, boolean[] cellEditable) {
		this.model = model;
		this.columnToolTips = columnToolTips;
		this.cellEditable = cellEditable;
		personalizarHeader();
		setFillsViewportHeight(true);
		setAutoCreateRowSorter(true);
		getTableHeader().setReorderingAllowed(false);
		setRowHeight(36);
	}

	@Override
	public boolean isCellEditable(int data, int columns) {
		return cellEditable[columns];
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
		setTableHeader(new JTableHeader(columnModel) {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1343809023580748958L;

			@Override
			public void setDefaultRenderer(TableCellRenderer defaultRenderer) {
				super.setDefaultRenderer(new TableHeaderWithSortIcons(tcr));
			}
			
		});
	}
}
