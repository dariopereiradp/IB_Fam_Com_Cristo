package dad.fam_com_cristo.table;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
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
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableRowSorter;
import javax.swing.text.MaskFormatter;

import dad.fam_com_cristo.Membro;
import dad.fam_com_cristo.Tipo_Membro;
import dad.fam_com_cristo.Tipo_Transacao;
import dad.fam_com_cristo.gui.MembroDetail;
import dad.recursos.CellRenderer;
import dad.recursos.SairAction;
import dad.recursos.Utils;
import com.toedter.calendar.JDateChooser;
import java.util.Locale;

/**
 * Classe que representa as tabelas de Entradas e Saidas no DataGui
 * 
 * @author Dário Pereira
 *
 */
public class FinancasPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5202224261982802705L;
	private static FinancasPanel INSTANCE;
	private JTable financas;
	private TableModelFinancas modelFinancas;
	private JPanel pInferior, panel2;
	private JTextField jtfTotal;
	private JButton bAdd;
	private String[] columnToolTips = { "Código de identificação da transação (por ordem sequencial)",
			"Valor numérico da transação", "Data em que ocorreu a transação",
			"Descrção da transação (dízimo, oferta, etc...)" };
	private JPanel panel_total;
	private JPanel panel;
	private JLabel lTotalEnt;
	private JTextField jft_totalEntradas;
	private JPanel panel_1;
	private JLabel lTotalsaidas;
	private JTextField jft_totalSaidas;
	private JPanel panel_4;
	private JPanel panel_2;
	private JLabel lblValor;
	private JFormattedTextField jtfValor;
	private JLabel lblTipo;
	private JComboBox<Tipo_Transacao> comboTipo;
	private JLabel lblDescricao;
	private JTextField jtfDescricao;
	private JLabel lblData;
	private JDateChooser data;
	private JButton btnAdd;

	public FinancasPanel() {
		super();
		INSTANCE = this;
		setLayout(new BorderLayout());
		modelFinancas = TableModelFinancas.getInstance();

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

		JComboBox<Tipo_Transacao> tipo_transacao = new JComboBox<Tipo_Transacao>();
		tipo_transacao.setBounds(370, 255, 191, 25);
		tipo_transacao.setModel(new DefaultComboBoxModel<Tipo_Transacao>(Tipo_Transacao.values()));

		panel_4 = new JPanel();
		add(panel_4, BorderLayout.CENTER);
		panel_4.setLayout(new BorderLayout(0, 0));

		financas = new Table(modelFinancas, columnToolTips, true);
		
		financas.setPreferredScrollableViewportSize(new Dimension(800, 600));

		financas.getColumnModel().getColumn(0).setCellRenderer(new CellRenderer());
		financas.getColumnModel().getColumn(1).setCellRenderer(new CellRenderer());
		financas.getColumnModel().getColumn(2).setCellRenderer(new CellRenderer());
		financas.getColumnModel().getColumn(3).setCellRenderer(new CellRenderer());

		financas.getColumnModel().getColumn(1).setCellEditor(dataEditor);

		financas.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(tipo_transacao));

		JScrollPane jsFinancas = new JScrollPane(financas);
		panel_4.add(jsFinancas, BorderLayout.CENTER);

		financas.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {
				financas.scrollRectToVisible(financas.getCellRect(financas.getRowCount() - 1, 0, true));
			}

		});

		financas.getInputMap().put(KeyStroke.getKeyStroke("DELETE"), "deleteRow");
		financas.getActionMap().put("deleteRow", new DeleteAction());

		pInferior = new JPanel(new BorderLayout());
		add(pInferior, BorderLayout.SOUTH);
		
		panel2 = new JPanel(new BorderLayout());

		inicializarBotoes();

		inicializarPanelAdd();

		inicializarMenus();

		modelFinancas.atualizarTextFieldsNumeros();

	}

	private void inicializarMenus() {
		JMenuItem delete = new JMenuItem("Apagar");
		delete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				removerMembros();
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
						int rowAtPointOriginal = financas
								.rowAtPoint(SwingUtilities.convertPoint(popupMenu, new Point(0, 0), financas));
						if (rowAtPointOriginal > -1) {
							int rowAtPoint = financas.convertRowIndexToModel(rowAtPointOriginal);
							if (rowAtPoint > -1) {
								int[] rows = convertRowsIndextoModel();
								if (rows.length <= 1) {
//									info.setVisible(true);
									financas.setRowSelectionInterval(rowAtPointOriginal, rowAtPointOriginal);
									delete.setVisible(true);
								}
							} else {
//								info.setVisible(false);
								delete.setVisible(true);
							}
						} else {
//							info.setVisible(false);
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

//		popupMenu.add(info);
		popupMenu.add(delete);
		popupMenu.add(atualizar);

		popupMenu.setPopupSize(350, 150);

		financas.setComponentPopupMenu(popupMenu);

	}

	public void inicializarBotoes() {
		pInferior.add(panel2, BorderLayout.WEST);
		JButton bSair = new JButton("SAIR");
		Utils.personalizarBotao(bSair);
		bSair.addActionListener(new SairAction());
		panel2.add(bSair, BorderLayout.CENTER);

	}

	private void inicializarPanelAdd() {

		JPanel both = new JPanel(new GridLayout(0, 3));

		pInferior.add(both, BorderLayout.CENTER);

		panel_total = new JPanel();
		both.add(panel_total);
		JLabel lblTotal = new JLabel("Total: ");
		panel_total.add(lblTotal);
		jtfTotal = new JTextField(String.valueOf(modelFinancas.getRowCount()));
		panel_total.add(jtfTotal);
		jtfTotal.setEditable(false);

		panel = new JPanel();
		both.add(panel);

		lTotalEnt = new JLabel("Total de Entradas: ");
		panel.add(lTotalEnt);

		jft_totalEntradas = new JTextField("0");
		jft_totalEntradas.setEditable(false);
		panel.add(jft_totalEntradas);

		panel_1 = new JPanel();
		both.add(panel_1);

		lTotalsaidas = new JLabel("Total de Sa\u00EDdas: ");
		panel_1.add(lTotalsaidas);

		jft_totalSaidas = new JTextField("0");
		jft_totalSaidas.setEditable(false);
		panel_1.add(jft_totalSaidas);
		
		panel_2 = new JPanel();
		pInferior.add(panel_2, BorderLayout.NORTH);
		
		lblValor = new JLabel("Valor: ");
		panel_2.add(lblValor);
		
		jtfValor = new JFormattedTextField();
		panel_2.add(jtfValor);
		
		lblTipo = new JLabel("Tipo: ");
		panel_2.add(lblTipo);
		
		comboTipo = new JComboBox<Tipo_Transacao>();
		comboTipo.setModel(new DefaultComboBoxModel<Tipo_Transacao>(Tipo_Transacao.values()));
		comboTipo.setSelectedIndex(0);
		panel_2.add(comboTipo);
		
		lblDescricao = new JLabel("Descri\u00E7\u00E3o: ");
		panel_2.add(lblDescricao);
		
		jtfDescricao = new JTextField();
		panel_2.add(jtfDescricao);
		jtfDescricao.setColumns(10);
		
		lblData = new JLabel("Data: ");
		panel_2.add(lblData);
		
		data = new JDateChooser();
		data.setLocale(new Locale("pt", "BR"));
		data.setDateFormatString("dd/MM/yyyy");
		panel_2.add(data);
		
		btnAdd = new JButton("Adicionar");
		panel_2.add(btnAdd);
	}

	public int[] convertRowsIndextoModel() {
		int[] rows = financas.getSelectedRows();
		for (int i = 0; i < rows.length; i++) {
			rows[i] = financas.convertRowIndexToModel(rows[i]);
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
				modelFinancas.removerTransacao(rows);
			}
		}
	}

	public JTable getMembros() {
		return financas;
	}

	public void abrir(Membro membro) {
		new MembroDetail(membro).open();
	}

	public JTextField getJtfTotal() {
		return jtfTotal;
	}

	public JTextField getJtfEntradas() {
		return jft_totalEntradas;
	}

	public JTextField getJtfSaidas() {
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
