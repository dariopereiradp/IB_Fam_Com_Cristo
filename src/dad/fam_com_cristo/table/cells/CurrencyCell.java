package dad.fam_com_cristo.table.cells;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.util.EventObject;

import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.TableCellEditor;

import dad.recursos.Utils;

public class CurrencyCell extends AbstractCellEditor implements TableCellEditor {

	private static final long serialVersionUID = 1L;
	private static final int clickCountToStart = 2;
	private JFormattedTextField editor;

	public CurrencyCell() {
		editor = Utils.getInstance().getNewCurrencyTextField();

		InputMap iMapValor = editor.getInputMap(JComponent.WHEN_FOCUSED);
		iMapValor.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), KeyEvent.getKeyText(KeyEvent.VK_ENTER));
		ActionMap aMapValor = editor.getActionMap();
		aMapValor.put(KeyEvent.getKeyText(KeyEvent.VK_ENTER), new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				stopCellEditing();
			}
		});
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		editor.setValue(Utils.getInstance().getNumberFromFormat(value));
		return editor;
	}

	@Override
	public boolean stopCellEditing() {
		try {
			editor.commitEdit();
		} catch (ParseException e) {
			return false;
		}
		return super.stopCellEditing();
	}

	@Override
	public Object getCellEditorValue() {
		return Utils.getInstance().getNumberFromFormat(editor.getText());
	}
	
	@Override
	public boolean isCellEditable(EventObject anEvent) {
		 if (anEvent instanceof MouseEvent) {
             return ((MouseEvent)anEvent).getClickCount() >= clickCountToStart;
         }
         return true;
	}
}