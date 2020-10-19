package dad.fam_com_cristo.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.RowSorter.SortKey;
import javax.swing.table.TableCellRenderer;

import mdlaf.utils.MaterialColors;

public class TableHeaderWithSortIcons implements TableCellRenderer {
	
	private TableCellRenderer tcr;
	private Icon ascendingIcon = UIManager.getIcon("Table.ascendingSortIcon");
	private Icon descendingIcon = UIManager.getIcon("Table.descendingSortIcon");
	
	public TableHeaderWithSortIcons (TableCellRenderer tcr) {
		this.tcr = tcr;
	}


	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
			boolean hasFocused, int row, int column) {

		Component comp = tcr.getTableCellRendererComponent(table, value, isSelected, hasFocused, row, column);
		if (comp instanceof JLabel) {
			JLabel label = (JLabel) comp;
			label.setPreferredSize(new Dimension(100, 30));
			label.setIcon(getSortIcon(table, column));
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setFont(new Font("Roboto", Font.BOLD, 15));
			label.setBorder(BorderFactory.createMatteBorder(0, 1, 3, 1, MaterialColors.GREEN_300));
			return label;
		}
		return comp;
	}

	private Icon getSortIcon(JTable table, int column) {
		SortOrder sortOrder = getColumnSortOrder(table, column);
		if (SortOrder.UNSORTED == sortOrder) {
			return new ImageIcon(getClass().getResource("/sort.png"));
		}
		return SortOrder.ASCENDING == sortOrder ? ascendingIcon : descendingIcon;
	}

	private SortOrder getColumnSortOrder(JTable table, int column) {
		if (table == null || table.getRowSorter() == null) {
			return SortOrder.UNSORTED;
		}
		List<? extends SortKey> keys = table.getRowSorter().getSortKeys();
		if (keys.size() > 0) {
			SortKey key = keys.get(0);
			if (key.getColumn() == table.convertColumnIndexToModel(column)) {
				return key.getSortOrder();
			}
		}
		return SortOrder.UNSORTED;
	}
}
