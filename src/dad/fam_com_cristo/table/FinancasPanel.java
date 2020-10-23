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
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
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

import com.toedter.calendar.JDateChooser;

import dad.fam_com_cristo.Tipo_Transacao;
import dad.fam_com_cristo.Transacao;
import dad.fam_com_cristo.gui.themes.DateChooser;
import dad.fam_com_cristo.gui.themes.Table;
import dad.recursos.CellRenderer;
import dad.recursos.CellRendererNoImage;
import dad.recursos.CurrencyCell;
import dad.recursos.SairAction;
import dad.recursos.Utils;
import mdlaf.utils.MaterialImageFactory;
import mdlaf.utils.icons.MaterialIconFont;

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
	private JFormattedTextField jtfTotal;
	private String[] columnToolTips = { "Código de identificação da transação (por ordem sequencial)",
			"Data em que ocorreu a transação", "Valor numérico da transação", "Tipo da transação: entrada ou saída",
			"Descrção da transação (dízimo, oferta, etc...)", "Valor total na data da transação" };
	private JPanel panel_total;
	private JPanel panel;
	private JLabel lTotalEnt;
	private JFormattedTextField jft_totalEntradas;
	private JPanel panel_1;
	private JLabel lTotalsaidas;
	private JFormattedTextField jft_totalSaidas;
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

		recreate();

	}

	/**
	 * 
	 */
	public void recreate() {
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

		JComboBox<Tipo_Transacao> tipo_transacao = new JComboBox<Tipo_Transacao>();
		tipo_transacao.setBounds(370, 255, 191, 25);
		tipo_transacao.setModel(new DefaultComboBoxModel<Tipo_Transacao>(Tipo_Transacao.values()));

		panel_4 = new JPanel();
		add(panel_4, BorderLayout.CENTER);
		panel_4.setLayout(new BorderLayout(0, 0));

		financas = new Table(modelFinancas, columnToolTips, new boolean[] { false, true, true, true, true, false });

		financas.setPreferredScrollableViewportSize(new Dimension(800, 600));

		financas.getColumnModel().getColumn(0).setCellRenderer(new CellRendererNoImage());
		financas.getColumnModel().getColumn(1).setCellRenderer(new CellRenderer());
		financas.getColumnModel().getColumn(2).setCellRenderer(new CellRenderer());
		financas.getColumnModel().getColumn(3).setCellRenderer(new CellRenderer());
		financas.getColumnModel().getColumn(4).setCellRenderer(new CellRenderer());
		financas.getColumnModel().getColumn(5).setCellRenderer(new CellRendererNoImage());

		financas.getColumnModel().getColumn(1).setCellEditor(dataEditor);
		financas.getColumnModel().getColumn(2).setCellEditor(new CurrencyCell());

		financas.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(tipo_transacao));

		financas.getColumnModel().getColumn(0).setMinWidth(80);
		financas.getColumnModel().getColumn(1).setPreferredWidth(160);
		financas.getColumnModel().getColumn(2).setPreferredWidth(160);
		financas.getColumnModel().getColumn(3).setPreferredWidth(160);
		financas.getColumnModel().getColumn(1).setMinWidth(160);
		financas.getColumnModel().getColumn(2).setMinWidth(160);
		financas.getColumnModel().getColumn(3).setMinWidth(160);
		financas.getColumnModel().getColumn(4).setPreferredWidth(500);
		financas.getColumnModel().getColumn(4).setMinWidth(450);
		financas.getColumnModel().getColumn(5).setMinWidth(90);

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

		inicializarPanelAdd();
		
		inicializarBotoes();

		inicializarMenus();

		modelFinancas.atualizarTextFieldsNumeros();
	}

	private void inicializarMenus() {
		JMenuItem delete = new JMenuItem("Apagar");
		delete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				removerTranscacoes();
			}
		});

		JMenuItem atualizar = new JMenuItem("Atualizar Tabela");
		atualizar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				TableModelFinancas.getInstance().fireTableDataChanged();
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
									financas.setRowSelectionInterval(rowAtPointOriginal, rowAtPointOriginal);
									delete.setVisible(true);
								}
							} else {
								delete.setVisible(true);
							}
						} else {
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

		popupMenu.add(delete);
		popupMenu.add(atualizar);

		popupMenu.setPopupSize(200, 60);

		financas.setComponentPopupMenu(popupMenu);

	}

	public void inicializarBotoes() {
		pInferior.add(panel2, BorderLayout.WEST);
		JButton bSair = new JButton("Sair");
		bSair.setIcon(MaterialImageFactory.getInstance().getImage(
                MaterialIconFont.EXIT_TO_APP,
                Utils.getInstance().getCurrentTheme().getColorIcons()));
		Utils.personalizarBotao(bSair);
		bSair.addActionListener(new SairAction());
		panel2.add(bSair, BorderLayout.CENTER);
		
		btnAdd = new JButton("Adicionar");
		btnAdd.setIcon(MaterialImageFactory.getInstance().getImage(
                MaterialIconFont.ADD_SHOPPING_CART,
                Utils.getInstance().getCurrentTheme().getColorIcons()));
		Utils.personalizarBotao(btnAdd);
		panel_2.add(btnAdd);
		
		btnAdd.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				adicionarTransacao();
				
			}
		});

	}

	public void adicionarTransacao() {
		BigDecimal valor = Utils.getInstance().getNumberFromFormat(jtfValor.getText());
		
		if(valor.compareTo(new BigDecimal("0")) == 0) {
			JOptionPane.showMessageDialog(null, "Introduza um valor maior que R$ 0,00!",
					"Salvar", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/FC_SS.jpg")));
		} else {
			Tipo_Transacao tipo = (Tipo_Transacao) comboTipo.getSelectedItem(); 
			String descricao = jtfDescricao.getText();
			Date dataTransacao = data.getDate();
			
			if(descricao.trim().equals(""))
				descricao = "-";
			
			BigDecimal total = modelFinancas.getTotal();
			if(tipo.equals(Tipo_Transacao.ENTRADA))
				total = modelFinancas.getTotal().add(valor);
			else
				total = modelFinancas.getTotal().subtract(valor);
			Transacao transacao = new Transacao(valor, tipo, descricao, dataTransacao, total);
			modelFinancas.addTransacao(transacao);
			
			jtfValor.setValue(0);
			jtfDescricao.setText("");
		}
		
	}

	private void inicializarPanelAdd() {

		JPanel both = new JPanel(new GridLayout(0, 3));

		pInferior.add(both, BorderLayout.CENTER);

		panel_total = new JPanel();
		both.add(panel_total);
		
		JLabel lblTotal = new JLabel("Total: ");
		panel_total.add(lblTotal);
		
		jtfTotal = Utils.getInstance().getNewCurrencyTextField();
		panel_total.add(jtfTotal);
		jtfTotal.setEditable(false);

		panel = new JPanel();
		both.add(panel);

		lTotalEnt = new JLabel("Total de Entradas: ");
		panel.add(lTotalEnt);

		jft_totalEntradas = Utils.getInstance().getNewCurrencyTextField();
		jft_totalEntradas.setEditable(false);
		panel.add(jft_totalEntradas);

		panel_1 = new JPanel();
		both.add(panel_1);

		lTotalsaidas = new JLabel("Total de Sa\u00EDdas: ");
		panel_1.add(lTotalsaidas);

		jft_totalSaidas = Utils.getInstance().getNewCurrencyTextField();
		jft_totalSaidas.setEditable(false);
		panel_1.add(jft_totalSaidas);

		GridLayout grid = new GridLayout(1, 9);
		grid.setHgap(20);
		panel_2 = new JPanel(grid);
		pInferior.add(panel_2, BorderLayout.NORTH);

		lblValor = new JLabel("Valor: ");
		panel_2.add(lblValor);
		
        jtfValor = Utils.getInstance().getNewCurrencyTextField();
        jtfValor.setValue(0);
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

		data = new DateChooser();
		panel_2.add(data);

	}

	public int[] convertRowsIndextoModel() {
		int[] rows = financas.getSelectedRows();
		for (int i = 0; i < rows.length; i++) {
			rows[i] = financas.convertRowIndexToModel(rows[i]);
		}
		return rows;
	}

	public void removerTranscacoes() {
		int[] rows = convertRowsIndextoModel();
		if (rows.length > 0) {
			int ok = JOptionPane.showConfirmDialog(this,
					"Tem certeza que quer apagar a(s) transação(ões) selecionada(s)?", "APAGAR",
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE,
					new ImageIcon(getClass().getResource("/DAD_SS.jpg")));
			if (ok == JOptionPane.OK_OPTION) {
				modelFinancas.removerTransacao(rows);
			}
		}
	}

	public JTable getTransacoes() {
		return financas;
	}

	public JFormattedTextField getJtfTotal() {
		return jtfTotal;
	}

	public JFormattedTextField getJtfEntradas() {
		return jft_totalEntradas;
	}

	public JFormattedTextField getJtfSaidas() {
		return jft_totalSaidas;
	}
	
	public JButton getBtnAdd() {
		return btnAdd;
	}

	private class DeleteAction extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 5018711156829284772L;

		@Override
		public void actionPerformed(ActionEvent e) {
			removerTranscacoes();
		}
	}

	public JTable newTable(String descricao) {
		JTable table = new JTable(TableModelFinancas.getInstance()) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 776311897765510270L;

			@Override
			public boolean isCellEditable(int data, int columns) {
				return false;
			}
		};

		RowFilter<TableModelFinancas, Object> rf = null;
		TableRowSorter<TableModelFinancas> sorter = new TableRowSorter<TableModelFinancas>(
				TableModelFinancas.getInstance());
		List<RowFilter<TableModelFinancas, Object>> filters = new ArrayList<RowFilter<TableModelFinancas, Object>>(5);
		table.setRowSorter(sorter);
		switch (descricao) {
		case "Entradas":
			filters.add(RowFilter.regexFilter(Tipo_Transacao.ENTRADA.getDescricao(), 3));
			break;
		case "Saídas":
			filters.add(RowFilter.regexFilter(Tipo_Transacao.SAIDA.getDescricao(), 3));
			break;
		default:
			break;
		}

		rf = RowFilter.orFilter(filters);
		sorter.setRowFilter(rf);

		return table;
	}

	public static FinancasPanel getInstance() {
		if (INSTANCE == null)
			INSTANCE = new FinancasPanel();
		return INSTANCE;
	}

}
