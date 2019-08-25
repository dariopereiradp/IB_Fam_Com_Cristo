package dad.fam_com_cristo.table;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.RowFilter;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import javax.swing.text.MaskFormatter;

import dad.fam_com_cristo.Membro;
import dad.fam_com_cristo.Tipo_Membro;
import dad.fam_com_cristo.gui.MembroDetail;
import dad.recursos.CellRenderer;
import dad.recursos.SairAction;
import mdlaf.animation.MaterialUIMovement;
import mdlaf.utils.MaterialColors;

public class FinancasPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5202224261982802705L;
	private static FinancasPanel INSTANCE;
	private JTable entradas, saidas;
	private TableModelMembro modelMembro;
	private JPanel pInferior, panel2;
	private JTextField jtfTotal;
	private JButton bAdd;
	private String[] columnToolTips = { "Nome do membro", "Data de nascimento do membro",
			"Telefone de contato do membro",
			"Tipo de membro (Congregado, Membro Ativo, Membro Nominal, Liderança ou Ex-membro)" };
	private JPanel panel_total;
	private JPanel panel;
	private JLabel lTotalEnt;
	private JTextField jft_totalsaidas;
	private JPanel panel_1;
	private JLabel lTotalsaidas;
	private JTextField jft_totalSaidas;
	private JPanel panel_2;
	private JLabel lTotalDizimos;
	private JTextField jft_totalDizimos;
	private JPanel panel_3;
	private JLabel lTotalOfertas;
	private JTextField jft_TotalOfertas;
	private JPanel panel_11;
	private JScrollPane jsSaidas;

	public FinancasPanel() {
		super();
		INSTANCE = this;
		setLayout(new BorderLayout());
		modelMembro = TableModelMembro.getInstance();

		MaskFormatter mascaraData;
		JFormattedTextField data;

		try {
			mascaraData = new MaskFormatter("##/##/####");
			mascaraData.setCommitsOnValidEdit(true);
			data = new JFormattedTextField(mascaraData);
		} catch (ParseException e1) {
			data = new JFormattedTextField();
			e1.printStackTrace();
		}
		data.setFont(new Font("Arial", Font.PLAIN, 15));

		final TableCellEditor dataEditor = new DefaultCellEditor(data);

		InputMap iMap = data.getInputMap(JComponent.WHEN_FOCUSED);
		iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), KeyEvent.getKeyText(KeyEvent.VK_ENTER));
		ActionMap aMap = data.getActionMap();
		aMap.put(KeyEvent.getKeyText(KeyEvent.VK_ENTER), new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				dataEditor.stopCellEditing();
			}
		});

		MaskFormatter maskPhone;
		JFormattedTextField phone;

		try {
			maskPhone = new MaskFormatter("(##) # ####-####");
			maskPhone.setCommitsOnValidEdit(true);
			phone = new JFormattedTextField(maskPhone);
		} catch (ParseException e1) {
			phone = new JFormattedTextField();
			e1.printStackTrace();
		}
		phone.setFont(new Font("Arial", Font.PLAIN, 15));

		final TableCellEditor phoneEditor = new DefaultCellEditor(phone);

		InputMap iMap1 = phone.getInputMap(JComponent.WHEN_FOCUSED);
		iMap1.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), KeyEvent.getKeyText(KeyEvent.VK_ENTER));
		ActionMap aMap1 = phone.getActionMap();
		aMap1.put(KeyEvent.getKeyText(KeyEvent.VK_ENTER), new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				phoneEditor.stopCellEditing();
			}
		});

		JComboBox<Tipo_Membro> tipo_membro = new JComboBox<Tipo_Membro>();
		tipo_membro.setBounds(370, 255, 191, 25);
		tipo_membro.setModel(new DefaultComboBoxModel<Tipo_Membro>(Tipo_Membro.values()));

		panel_11 = new JPanel();
		add(panel_11, BorderLayout.CENTER);
		panel_11.setLayout(new GridLayout(0, 2, 0, 0));

		saidas = new JTable();

		entradas = new JTable(modelMembro) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 776311897765510270L;

			@Override
			public boolean isCellEditable(int data, int columns) {
				return true;
			}

			@Override
			public Component prepareRenderer(TableCellRenderer r, int data, int columns) {
				// int row = convertRowIndexToModel(data);
				Component c = super.prepareRenderer(r, data, columns);
				if (data % 2 == 0)
					c.setBackground(Color.WHITE);
				else
					c.setBackground(MaterialColors.GRAY_100);
				if (isCellSelected(data, columns)) {
					c.setBackground(MaterialColors.GREEN_A100);
				}
				return c;
			}

			// Implement table cell tool tips.
			public String getToolTipText(MouseEvent e) {
				String tip = null;
				Point p = e.getPoint();
				int rowIndex = rowAtPoint(p);
				int colIndex = columnAtPoint(p);
				int realColumnIndex = convertColumnIndexToModel(colIndex);
				if (rowIndex != -1) {
					int realRowIndex = convertRowIndexToModel(rowIndex);
					tip = String.valueOf(modelMembro.getValueAt(realRowIndex, realColumnIndex));
				} else
					tip = null;
				return tip;
			}

			// Implement table header tool tips.
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
		};
		TableCellRenderer tcr = entradas.getTableHeader().getDefaultRenderer();
		entradas.getTableHeader().setDefaultRenderer(new TableCellRenderer() {

			private Icon ascendingIcon = UIManager.getIcon("Table.ascendingSortIcon");
			private Icon descendingIcon = UIManager.getIcon("Table.descendingSortIcon");

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocused, int row, int column) {

				Component comp = tcr.getTableCellRendererComponent(table, value, isSelected, hasFocused, row, column);
				if (comp instanceof JLabel) {
					JLabel label = (JLabel) comp;
					label.setPreferredSize(new Dimension(100, 30));
					label.setIcon(getSortIcon(table, column));
					label.setHorizontalAlignment(SwingConstants.CENTER);
					label.setBackground(MaterialColors.YELLOW_300);
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
		});

		entradas.setPreferredScrollableViewportSize(new Dimension(800, 600));
		entradas.setFillsViewportHeight(true);
		entradas.setAutoCreateRowSorter(true);
		entradas.getTableHeader().setReorderingAllowed(false);
		entradas.setRowHeight(30);

		entradas.getColumnModel().getColumn(0).setCellRenderer(new CellRenderer());
		entradas.getColumnModel().getColumn(1).setCellRenderer(new CellRenderer());
		entradas.getColumnModel().getColumn(2).setCellRenderer(new CellRenderer());
		entradas.getColumnModel().getColumn(3).setCellRenderer(new CellRenderer());

		entradas.getColumnModel().getColumn(1).setCellEditor(dataEditor);
		entradas.getColumnModel().getColumn(2).setCellEditor(phoneEditor);

		entradas.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(tipo_membro));

		JScrollPane jsEntradas = new JScrollPane(entradas);
		panel_11.add(jsEntradas);

		jsSaidas = new JScrollPane(saidas);
		panel_11.add(jsSaidas);

		entradas.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {
				entradas.scrollRectToVisible(entradas.getCellRect(entradas.getRowCount() - 1, 0, true));
			}

		});

		entradas.getInputMap().put(KeyStroke.getKeyStroke("DELETE"), "deleteRow");
		entradas.getActionMap().put("deleteRow", new DeleteAction());

		entradas.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent mouseEvent) {
				JTable table = (JTable) mouseEvent.getSource();
				Point point = mouseEvent.getPoint();
				int column = table.columnAtPoint(point);
				int rowAtPoint = table.rowAtPoint(point);
				if (rowAtPoint != -1) {
					int row = table.convertRowIndexToModel(rowAtPoint);
					if (mouseEvent.getClickCount() == 2 && !table.isCellEditable(row, column)
							&& table.getSelectedRow() != -1) {
						abrir(modelMembro.getMembro(row));
					}
				}
			}
		});

		pInferior = new JPanel(new BorderLayout());
		add(pInferior, BorderLayout.SOUTH);
		panel2 = new JPanel(new GridLayout(2, 1));

		inicializarBotoes();

		inicializarPanelAdd();

		inicializarMenus();

		modelMembro.atualizarTextFieldsNumeros();

	}

	private void inicializarMenus() {
		JMenuItem delete = new JMenuItem("Apagar");
		delete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				removerMembros();
			}
		});

		JMenuItem info = new JMenuItem("Informações");
		info.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				abrir(modelMembro.getMembro(entradas.convertRowIndexToModel(entradas.getSelectedRow())));

			}
		});

		JMenuItem atualizar = new JMenuItem("Atualizar Tabela");
		atualizar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				TableModelMembro.getInstance().fireTableDataChanged();
			}
		});

		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.addPopupMenuListener(new PopupMenuListener() {

			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						int rowAtPointOriginal = entradas
								.rowAtPoint(SwingUtilities.convertPoint(popupMenu, new Point(0, 0), entradas));
						if (rowAtPointOriginal > -1) {
							int rowAtPoint = entradas.convertRowIndexToModel(rowAtPointOriginal);
							if (rowAtPoint > -1) {
								int[] rows = convertRowsIndextoModel();
								if (rows.length <= 1) {
									info.setVisible(true);
									entradas.setRowSelectionInterval(rowAtPointOriginal, rowAtPointOriginal);
									delete.setVisible(true);
								}
							} else {
								info.setVisible(false);
								delete.setVisible(true);
							}
						} else {
							info.setVisible(false);
							delete.setVisible(false);
							atualizar.setVisible(true);
						}
					}
				});
			}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

			}

			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {

			}
		});

		popupMenu.add(info);
		popupMenu.add(delete);
		popupMenu.add(atualizar);

		popupMenu.setPopupSize(350, 150);

		entradas.setComponentPopupMenu(popupMenu);

	}

	public void inicializarBotoes() {
		pInferior.add(panel2, BorderLayout.WEST);
		JButton bSair = new JButton("SAIR");
		bSair.setBackground(new Color(247, 247, 255));
		bSair.setForeground(MaterialColors.LIGHT_BLUE_400);
		personalizarBotao(bSair);
		bSair.addActionListener(new SairAction());
		panel2.add(bSair);

		JPanel panel4 = new JPanel(new GridLayout(2, 1));
		pInferior.add(panel4, BorderLayout.EAST);

		bAdd = new JButton("ADICIONAR");
		bAdd.setForeground(MaterialColors.WHITE);
		bAdd.setBackground(MaterialColors.LIGHT_GREEN_500);
		personalizarBotao(bAdd);
		bAdd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new MembroDetail().open();
			}
		});
		panel4.add(bAdd);
	}

	private void inicializarPanelAdd() {

		JPanel both = new JPanel(new GridLayout(0, 5));

		pInferior.add(both, BorderLayout.CENTER);

		panel_total = new JPanel();
		both.add(panel_total);
		JLabel lblTotal = new JLabel("Total: ");
		panel_total.add(lblTotal);
		jtfTotal = new JTextField(String.valueOf(modelMembro.getRowCount()));
		panel_total.add(jtfTotal);
		jtfTotal.setEditable(false);

		panel = new JPanel();
		both.add(panel);

		lTotalEnt = new JLabel("Total de Entradas: ");
		panel.add(lTotalEnt);

		jft_totalsaidas = new JTextField("0");
		jft_totalsaidas.setEditable(false);
		panel.add(jft_totalsaidas);

		panel_1 = new JPanel();
		both.add(panel_1);

		lTotalsaidas = new JLabel("Total de Sa\u00EDdas: ");
		panel_1.add(lTotalsaidas);

		jft_totalSaidas = new JTextField("0");
		jft_totalSaidas.setEditable(false);
		panel_1.add(jft_totalSaidas);

		panel_2 = new JPanel();
		both.add(panel_2);

		lTotalDizimos = new JLabel("Total de D\u00EDzimos: ");
		panel_2.add(lTotalDizimos);

		jft_totalDizimos = new JTextField("0");
		jft_totalDizimos.setEditable(false);
		panel_2.add(jft_totalDizimos);

		panel_3 = new JPanel();
		both.add(panel_3);

		lTotalOfertas = new JLabel("Total Ofertas: ");
		panel_3.add(lTotalOfertas);

		jft_TotalOfertas = new JTextField("0");
		jft_TotalOfertas.setEditable(false);
		panel_3.add(jft_TotalOfertas);
	}

	public void personalizarBotao(JButton jb) {
		jb.setFont(new Font("Roboto", Font.PLAIN, 15));
		MaterialUIMovement.add(jb, MaterialColors.GRAY_300, 5, 1000 / 30);
	}

	public int[] convertRowsIndextoModel() {
		int[] rows = entradas.getSelectedRows();
		for (int i = 0; i < rows.length; i++) {
			rows[i] = entradas.convertRowIndexToModel(rows[i]);
		}
		return rows;
	}

	public void removerMembros() {
		int[] rows = convertRowsIndextoModel();
		if (rows.length > 0) {
			int ok = JOptionPane.showConfirmDialog(this, "Tem certeza que quer apagar o(s) membro(s) selecionado(s)?",
					"APAGAR", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE,
					new ImageIcon(getClass().getResource("/DAD_SS.jpg")));
			if (ok == JOptionPane.OK_OPTION) {
				modelMembro.removerMembro(rows);
			}
		}
	}

	public JTable getMembros() {
		return entradas;
	}

	public void abrir(Membro membro) {
		new MembroDetail(membro).open();
	}

	public JTextField getJtfTotal() {
		return jtfTotal;
	}

	public JTextField getJft_congregados() {
		return jft_totalDizimos;
	}

	public JTextField getJft_lideranca() {
		return jft_TotalOfertas;
	}

	public JTextField getJft_membros_ativos() {
		return jft_totalsaidas;
	}

	public JTextField getJft_membros_nominais() {
		return jft_totalSaidas;
	}

	private class DeleteAction extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 5018711156829284772L;

		@Override
		public void actionPerformed(ActionEvent e) {
			removerMembros();
		}
	}

	public static FinancasPanel getInstance() {
		if (INSTANCE == null)
			INSTANCE = new FinancasPanel();
		return INSTANCE;
	}

	public JButton getbAdd() {
		return bAdd;
	}

	public JTable newTable(String descricao) {
		JTable table = new JTable(TableModelMembro.getInstance()) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 776311897765510270L;

			@Override
			public boolean isCellEditable(int data, int columns) {
				return false;
			}
		};

		RowFilter<TableModelMembro, Object> rf = null;
		TableRowSorter<TableModelMembro> sorter = new TableRowSorter<TableModelMembro>(TableModelMembro.getInstance());
		List<RowFilter<TableModelMembro, Object>> filters = new ArrayList<RowFilter<TableModelMembro, Object>>(5);
		table.setRowSorter(sorter);
		switch (descricao) {
		case "Todos":
			filters.add(RowFilter.regexFilter(Tipo_Membro.MEMBRO_ATIVO.getDescricao(), 3));
			filters.add(RowFilter.regexFilter(Tipo_Membro.MEMBRO_NOMINAL.getDescricao(), 3));
			filters.add(RowFilter.regexFilter(Tipo_Membro.CONGREGADO.getDescricao(), 3));
			filters.add(RowFilter.regexFilter(Tipo_Membro.LIDERANCA.getDescricao(), 3));
			break;
		case "Ativos":
			filters.add(RowFilter.regexFilter(Tipo_Membro.MEMBRO_ATIVO.getDescricao(), 3));
			filters.add(RowFilter.regexFilter(Tipo_Membro.LIDERANCA.getDescricao(), 3));
			break;
		case "Nominais":
			filters.add(RowFilter.regexFilter(Tipo_Membro.MEMBRO_NOMINAL.getDescricao(), 3));
			break;
		case "Líderes":
			filters.add(RowFilter.regexFilter(Tipo_Membro.LIDERANCA.getDescricao(), 3));
			break;
		case "Batizados":
			filters.add(RowFilter.regexFilter(Tipo_Membro.MEMBRO_ATIVO.getDescricao(), 3));
			filters.add(RowFilter.regexFilter(Tipo_Membro.MEMBRO_NOMINAL.getDescricao(), 3));
			filters.add(RowFilter.regexFilter(Tipo_Membro.LIDERANCA.getDescricao(), 3));
			break;
		case "Congregados":
			filters.add(RowFilter.regexFilter(Tipo_Membro.CONGREGADO.getDescricao(), 3));
			break;
		default:
			break;
		}

		rf = RowFilter.orFilter(filters);
		sorter.setRowFilter(rf);

		return table;
	}

}
