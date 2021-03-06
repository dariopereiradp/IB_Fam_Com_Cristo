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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

import dad.fam_com_cristo.gui.MembroDetail;
import dad.fam_com_cristo.table.cells.CellRenderer;
import dad.fam_com_cristo.table.cells.DataCellEditor;
import dad.fam_com_cristo.table.models.TableModelMembro;
import dad.fam_com_cristo.types.Membro;
import dad.fam_com_cristo.types.enumerados.Tipo_Membro;
import dad.recursos.SairAction;
import dad.recursos.Utils;
import mdlaf.utils.MaterialImageFactory;
import mdlaf.utils.icons.MaterialIconFont;

/**
 * Classe que representa a tabela de Membros na DataGui.
 * 
 * @author D�rio Pereira
 *
 */
public class MembroPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5202224261982802705L;
	private static MembroPanel INSTANCE;
	private JTable membros;
	private TableModelMembro modelMembro;
	private JPanel pInferior, panel2;
	private JTextField jtfTotal;
	private JButton bAdd;
	private String[] columnToolTips = { "Nome do membro", "Data de nascimento do membro",
			"Telefone de contato do membro",
			"Tipo de membro" };
	private JPanel panel_total;
	private JPanel panel;
	private JLabel lblNmeroDeMembros;
	private JTextField jft_membros_ativos;
	private JPanel panel_1;
	private JLabel lblMembrosNominais;
	private JTextField jft_membros_nominais;
	private JPanel panel_2;
	private JLabel lblCongregados;
	private JTextField jft_congregados;
	private JPanel panel_3;
	private JLabel lblLiderana;
	private JTextField jft_lideranca;
	private JPanel panel_4;
	private JLabel lblExmembros;
	private JTextField jft_ex_membros;
	private JPanel panel_5;
	private JLabel lblHomens;
	private JTextField jft_Homens;
	private JPanel panel_6;
	private JLabel lblMulheres;
	private JTextField jft_Mulheres;
	private JPanel panel_7;
	private JLabel Casados;
	private JTextField jftCasados;
	private JPanel panel_8;
	private JLabel lblAdultos;
	private JTextField jft_adultos;
	private JPanel panel_9;
	private JLabel lblAdolescentes;
	private JTextField jft_adolescentes;
	private JPanel panel_10;
	private JLabel lblCrianas;
	private JTextField jft_criancas;

	public MembroPanel() {
		super();
		INSTANCE = this;
		setLayout(new BorderLayout());
		modelMembro = TableModelMembro.getInstance();
		recreate();
	}

	/**
	 * Usado para garantir que tudo muda ao mudar o tema (Dark/Light)
	 */
	public void recreate() {
		membros = new Table(modelMembro, columnToolTips, true);
		membros.setPreferredScrollableViewportSize(new Dimension(800, 600));

		membros.getColumnModel().getColumn(0).setCellRenderer(new CellRenderer());
		membros.getColumnModel().getColumn(1).setCellRenderer(new CellRenderer());
		membros.getColumnModel().getColumn(2).setCellRenderer(new CellRenderer());
		membros.getColumnModel().getColumn(3).setCellRenderer(new CellRenderer());

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

		DefaultCellEditor tipoCell = new DefaultCellEditor(tipo_membro);
		tipoCell.setClickCountToStart(2);

		membros.getColumnModel().getColumn(1).setCellEditor(new DataCellEditor());
		membros.getColumnModel().getColumn(2).setCellEditor(phoneEditor);
		membros.getColumnModel().getColumn(3).setCellEditor(tipoCell);

		JScrollPane jsMembros = new JScrollPane(membros);
		add(jsMembros, BorderLayout.CENTER);

		membros.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {
				membros.scrollRectToVisible(membros.getCellRect(membros.getRowCount() - 1, 0, true));
			}

		});

		pInferior = new JPanel(new BorderLayout());
		add(pInferior, BorderLayout.SOUTH);
		panel2 = new JPanel(new GridLayout(2, 1));

		inicializarBotoes();

		inicializarPanelAdd();

		inicializarMenus();

		membros.getInputMap().put(KeyStroke.getKeyStroke("DELETE"), "deleteRow");
		membros.getActionMap().put("deleteRow", new DeleteAction());

		membros.addMouseListener(new MouseAdapter() {
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

		KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
		membros.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(enter, "solve");
		membros.getActionMap().put("solve", new AbstractAction() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -833616209546223519L;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (membros.getSelectedRows().length == 1)
					abrir(modelMembro.getMembro(membros.convertRowIndexToModel(membros.getSelectedRow())));

			}
		});
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

		JMenuItem info = new JMenuItem("Informa��es");
		info.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				abrir(modelMembro.getMembro(membros.convertRowIndexToModel(membros.getSelectedRow())));

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
						int rowAtPointOriginal = membros
								.rowAtPoint(SwingUtilities.convertPoint(popupMenu, new Point(0, 0), membros));
						if (rowAtPointOriginal > -1) {
							int rowAtPoint = membros.convertRowIndexToModel(rowAtPointOriginal);
							if (rowAtPoint > -1) {
								int[] rows = convertRowsIndextoModel();
								if (rows.length <= 1) {
									info.setVisible(true);
									membros.setRowSelectionInterval(rowAtPointOriginal, rowAtPointOriginal);
									delete.setVisible(true);
								} else {
									info.setVisible(false);
									delete.setVisible(true);
								}
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

		popupMenu.setPopupSize(200, 100);

		membros.setComponentPopupMenu(popupMenu);

	}

	private void inicializarBotoes() {
		pInferior.add(panel2, BorderLayout.WEST);
		JButton bSair = new JButton("Sair");
		bSair.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.EXIT_TO_APP,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		Utils.personalizarBotao(bSair);
		bSair.addActionListener(new SairAction());
		panel2.add(bSair);

		JPanel panel4 = new JPanel(new GridLayout(2, 1));
		pInferior.add(panel4, BorderLayout.EAST);

		bAdd = new JButton("Adicionar");
		bAdd.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.PERSON_ADD,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		Utils.personalizarBotao(bAdd);
		bAdd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new MembroDetail().open();
			}
		});
		panel4.add(bAdd);
	}

	private void inicializarPanelAdd() {

		JPanel both = new JPanel(new GridLayout(0, 6));

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

		lblNmeroDeMembros = new JLabel("Membros ativos: ");
		panel.add(lblNmeroDeMembros);

		jft_membros_ativos = new JTextField("0");
		jft_membros_ativos.setEditable(false);
		panel.add(jft_membros_ativos);

		panel_1 = new JPanel();
		both.add(panel_1);

		lblMembrosNominais = new JLabel("Membros nominais: ");
		panel_1.add(lblMembrosNominais);

		jft_membros_nominais = new JTextField("0");
		jft_membros_nominais.setEditable(false);
		panel_1.add(jft_membros_nominais);

		panel_2 = new JPanel();
		both.add(panel_2);

		lblCongregados = new JLabel("Congregados: ");
		panel_2.add(lblCongregados);

		jft_congregados = new JTextField("0");
		jft_congregados.setEditable(false);
		panel_2.add(jft_congregados);

		panel_3 = new JPanel();
		both.add(panel_3);

		lblLiderana = new JLabel("Lideran\u00E7a: ");
		panel_3.add(lblLiderana);

		jft_lideranca = new JTextField("0");
		jft_lideranca.setEditable(false);
		panel_3.add(jft_lideranca);

		panel_4 = new JPanel();
		both.add(panel_4);

		lblExmembros = new JLabel("Ex-membros: ");
		panel_4.add(lblExmembros);

		jft_ex_membros = new JTextField("0");
		jft_ex_membros.setEditable(false);
		panel_4.add(jft_ex_membros);

		panel_5 = new JPanel();
		both.add(panel_5);

		lblHomens = new JLabel("Homens: ");
		panel_5.add(lblHomens);

		jft_Homens = new JTextField("0");
		jft_Homens.setEditable(false);
		panel_5.add(jft_Homens);

		panel_6 = new JPanel();
		both.add(panel_6);

		lblMulheres = new JLabel("Mulheres: ");
		panel_6.add(lblMulheres);

		jft_Mulheres = new JTextField("0");
		jft_Mulheres.setEditable(false);
		panel_6.add(jft_Mulheres);

		panel_7 = new JPanel();
		both.add(panel_7);

		Casados = new JLabel("Casados: ");
		panel_7.add(Casados);

		jftCasados = new JTextField("0");
		jftCasados.setEditable(false);
		panel_7.add(jftCasados);

		panel_8 = new JPanel();
		both.add(panel_8);

		lblAdultos = new JLabel("Adultos (18+): ");
		panel_8.add(lblAdultos);

		jft_adultos = new JTextField("0");
		jft_adultos.setEditable(false);
		panel_8.add(jft_adultos);

		panel_9 = new JPanel();
		both.add(panel_9);

		lblAdolescentes = new JLabel("Adolescentes (13-17): ");
		panel_9.add(lblAdolescentes);

		jft_adolescentes = new JTextField("0");
		jft_adolescentes.setEditable(false);
		panel_9.add(jft_adolescentes);

		panel_10 = new JPanel();
		both.add(panel_10);

		lblCrianas = new JLabel("Crian\u00E7as (-12)");
		panel_10.add(lblCrianas);

		jft_criancas = new JTextField("0");
		jft_criancas.setEditable(false);
		panel_10.add(jft_criancas);
	}

	/**
	 * Converte as posi��es selecionadas para indices do modelo
	 * 
	 * @return um array contendo as posi��es selecionadas em indices do modelo
	 */
	private int[] convertRowsIndextoModel() {
		int[] rows = membros.getSelectedRows();
		for (int i = 0; i < rows.length; i++) {
			rows[i] = membros.convertRowIndexToModel(rows[i]);
		}
		return rows;
	}

	/**
	 * Apaga um membro, caso se confirme
	 */
	private void removerMembros() {
		int[] rows = convertRowsIndextoModel();
		if (rows.length > 0) {
			int ok = JOptionPane.showConfirmDialog(this, "Tem certeza que quer apagar o(s) membro(s) selecionado(s)?",
					"APAGAR", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE,
					new ImageIcon(getClass().getResource("/FC_SS.jpg")));
			if (ok == JOptionPane.OK_OPTION) {
				modelMembro.removerMembro(rows);
			}
		}
	}

	public JTable getMembros() {
		return membros;
	}

	private void abrir(Membro membro) {
		new MembroDetail(membro).open();
	}

	public JTextField getJtfTotal() {
		return jtfTotal;
	}

	public JTextField getJft_congregados() {
		return jft_congregados;
	}

	public JTextField getJft_ex_membros() {
		return jft_ex_membros;
	}

	public JTextField getJft_lideranca() {
		return jft_lideranca;
	}

	public JTextField getJft_membros_ativos() {
		return jft_membros_ativos;
	}

	public JTextField getJft_membros_nominais() {
		return jft_membros_nominais;
	}

	public JTextField getJft_Homens() {
		return jft_Homens;
	}

	public JTextField getJft_Mulheres() {
		return jft_Mulheres;
	}

	public JTextField getJftCasados() {
		return jftCasados;
	}

	public JTextField getJft_adolescentes() {
		return jft_adolescentes;
	}

	public JTextField getJft_adultos() {
		return jft_adultos;
	}

	public JTextField getJft_criancas() {
		return jft_criancas;
	}

	/**
	 * ActionListener para apagar um membro
	 * 
	 * @author D�rio Pereira
	 *
	 */
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

	/**
	 * Devole uma tabela filtrada com aquilo que se pretende imprimir
	 * 
	 * @param descricao tipo de tabela que se pretende imprimir
	 * @return a tabela pronta para imprimir
	 */
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
		case "L�deres":
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

	public static MembroPanel getInstance() {
		if (INSTANCE == null)
			new MembroPanel();
		return INSTANCE;
	}
}
