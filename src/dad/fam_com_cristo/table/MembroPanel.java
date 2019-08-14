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
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
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
import javax.swing.text.MaskFormatter;

import com.toedter.calendar.JDateChooser;

import dad.fam_com_cristo.Membro;
import dad.fam_com_cristo.gui.MembroDetail;
import dad.recursos.CellRenderer;
import dad.recursos.CellRendererNoImage;
import dad.recursos.CpfValidator;
import dad.recursos.Log;
import dad.recursos.SairAction;
import mdlaf.animation.MaterialUIMovement;
import mdlaf.utils.MaterialColors;

public class MembroPanel extends JPanel {

	private static MembroPanel INSTANCE;
	private JTable membros;
	private TableModelMembro modelMembro;
	private JPanel panelAdd, pInferior, panel2, panel3;
	private JTextField nome;
	private JFormattedTextField cpf, telefone;
	private MaskFormatter mascaraCpf, maskPhone;
	private JTextField jtfTotal;
	private JDateChooser date_nasc;
	private JButton bAdd;

	/**
	 * 
	 */
	private static final long serialVersionUID = -5439324224974968781L;
	private String[] columnToolTips = { "CPF do cliente", "Nome do cliente", "Data de Nascimento do Cliente",
			"Telefone do cliente", "N�mero de Empr�stimos que o cliente fez" };

	public MembroPanel() {
		super();
		INSTANCE = this;
		setLayout(new BorderLayout());
		modelMembro = TableModelMembro.getInstance();
		membros = new JTable(modelMembro) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 776311897765510270L;

			@Override
			public boolean isCellEditable(int data, int columns) {
				if (columns == 0 || columns == 4)
					return false;
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
		TableCellRenderer tcr = membros.getTableHeader().getDefaultRenderer();
		membros.getTableHeader().setDefaultRenderer(new TableCellRenderer() {

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

		membros.setPreferredScrollableViewportSize(new Dimension(800, 600));
		membros.setFillsViewportHeight(true);
		membros.setAutoCreateRowSorter(true);
		membros.getTableHeader().setReorderingAllowed(false);
		membros.setRowHeight(30);

		membros.getColumnModel().getColumn(0).setCellRenderer(new CellRendererNoImage());
		membros.getColumnModel().getColumn(1).setCellRenderer(new CellRenderer());
		membros.getColumnModel().getColumn(2).setCellRenderer(new CellRenderer());
		membros.getColumnModel().getColumn(3).setCellRenderer(new CellRenderer());
		membros.getColumnModel().getColumn(4).setCellRenderer(new CellRendererNoImage());

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
		
		membros.getColumnModel().getColumn(2).setCellEditor(dataEditor);
		// users.getColumnModel().getColumn(2).setCellEditor(new
		// JDateChooserCellEditor());
		
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
		membros.getColumnModel().getColumn(3).setCellEditor(phoneEditor);

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

		JScrollPane jsLivros = new JScrollPane(membros);
		add(jsLivros, BorderLayout.CENTER);

		membros.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {
				membros.scrollRectToVisible(membros.getCellRect(membros.getRowCount() - 1, 0, true));
			}

		});

		pInferior = new JPanel(new BorderLayout());
		add(pInferior, BorderLayout.SOUTH);
		panel2 = new JPanel(new GridLayout(2, 1));
		panel3 = new JPanel();
		JLabel total = new JLabel("Total: ");
		jtfTotal = new JTextField(String.valueOf(modelMembro.getRowCount()));
		jtfTotal.setEditable(false);
		panel3.add(total);
		panel3.add(jtfTotal);
		panel2.add(panel3);

		inicializarBotoes();

		inicializarPanelAdd();

		JMenuItem delete = new JMenuItem("Apagar");
		delete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				removerUsers();
			}
		});

		JMenuItem info = new JMenuItem("Informa��es");
		info.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				abrir(modelMembro.getUser(membros.convertRowIndexToModel(membros.getSelectedRow())));

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

		membros.setComponentPopupMenu(popupMenu);

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
						abrir(modelMembro.getUser(row));
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
					abrir(modelMembro.getUser(membros.convertRowIndexToModel(membros.getSelectedRow())));

			}
		});

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
		bAdd.setEnabled(false);
		bAdd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				adicionarUser();

			}
		});
		panel4.add(bAdd);
	}

	private void inicializarPanelAdd() {
		panelAdd = new JPanel(new GridLayout(1, 5));

		JPanel panelNome = new JPanel(new BorderLayout());
		JLabel lNome = new JLabel("Nome: ");
		lNome.setFont(new Font("Roboto", Font.BOLD, 15));
		panelNome.add(lNome, BorderLayout.WEST);

		nome = new JTextField();
		nome.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					adicionarUser();
			}
		});
		panelNome.add(nome, BorderLayout.CENTER);

		panelAdd.add(panelNome);

		JPanel panelDataNascimento = new JPanel(new BorderLayout());
		JLabel lDateNasc = new JLabel("Data de Nascimento: ");
		lDateNasc.setFont(new Font("Roboto", Font.BOLD, 15));
		panelDataNascimento.add(lDateNasc, BorderLayout.WEST);

		date_nasc = new JDateChooser();
		date_nasc.setLocale(new Locale("pt", "BR"));
		date_nasc.setDateFormatString("dd/MM/yyyy");
		date_nasc.setMaxSelectableDate(new Date());
		date_nasc.setDate(new Date());

		date_nasc.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					adicionarUser();
			}
		});
		panelDataNascimento.add(date_nasc, BorderLayout.CENTER);
		panelAdd.add(panelDataNascimento);

		JPanel last = new JPanel();

		try {
			mascaraCpf = new MaskFormatter("###.###.###-##");
			mascaraCpf.setCommitsOnValidEdit(true);
			cpf = new JFormattedTextField(mascaraCpf);
		} catch (ParseException e1) {
			cpf = new JFormattedTextField();
			e1.printStackTrace();
		}

		cpf.setFont(new Font("Arial", Font.PLAIN, 15));
		cpf.setBounds(90, 162, 181, 20);
		cpf.setColumns(10);

		cpf.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent e) {
				validar();
				super.focusLost(e);
			}

		});

		JLabel lCpf = new JLabel("CPF: ");
		lCpf.setFont(new Font("Roboto", Font.BOLD, 15));

		last.add(lCpf);
		last.add(cpf);

		try {
			maskPhone = new MaskFormatter("(##) # ####-####");
			maskPhone.setCommitsOnValidEdit(true);
			telefone = new JFormattedTextField(maskPhone);
		} catch (ParseException e1) {
			telefone = new JFormattedTextField();
			e1.printStackTrace();
		}

		telefone.setFont(new Font("Arial", Font.PLAIN, 15));
		telefone.setBounds(120, 162, 181, 20);
		telefone.setColumns(12);

		JLabel lPhone = new JLabel("Telefone: ");
		lPhone.setFont(new Font("Roboto", Font.BOLD, 15));

		last.add(lPhone);
		last.add(telefone);

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					adicionarUser();
			}
		});

		JPanel both = new JPanel(new GridLayout(2, 1));
		both.add(last);
		both.add(panelAdd);

		pInferior.add(both, BorderLayout.CENTER);
	}

	public boolean validar() {
		String cpfString;
		cpfString = cpf.getText();
		cpfString = cpfString.replace(".", "").replace("-", "");
		if (!cpfString.trim().equals("")) {
			if (CpfValidator.isCPF(cpfString)) {
				if (Membro.existe(cpfString)) {
					bAdd.setEnabled(false);
					String nomeUser = Membro.getUser(cpfString).getNome();
					JOptionPane.showMessageDialog(this, "J� existe um usu�rio registado com esse CPF! - " + nomeUser,
							"Erro", JOptionPane.ERROR_MESSAGE, new ImageIcon(getClass().getResource("/DAD_SS.jpg")));
					return false;
				} else {
					bAdd.setEnabled(true);
					return true;
				}
			} else {
				bAdd.setEnabled(false);
				JOptionPane.showMessageDialog(this, "N�mero de CPF inv�lido!", "Erro", JOptionPane.ERROR_MESSAGE,
						new ImageIcon(getClass().getResource("/DAD_SS.jpg")));
				return false;
			}
		}
		return false;
	}

	public void personalizarBotao(JButton jb) {
		jb.setFont(new Font("Roboto", Font.PLAIN, 15));
		MaterialUIMovement.add(jb, MaterialColors.GRAY_300, 5, 1000 / 30);
	}

	public void adicionarUser() {
		if (nome.getText().trim().equals(""))
			JOptionPane.showMessageDialog(this, "Deve inserir um nome para o cliente!", "ADICIONAR",
					JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/DAD_SS.jpg")));
		else {
			if (validar()) {
				TableModelMembro.getInstance().addUser(
						new Membro(nome.getText(), date_nasc.getDate(),
								telefone.getText().replace("-", "").replace("(", "").replace(")", "").replace(" ", ""),
								0, false));
				Log.getInstance().printLog("Cliente adicionado com sucesso!");
			} else
				JOptionPane.showMessageDialog(this, "CPF inv�lido!", "ADICIONAR", JOptionPane.INFORMATION_MESSAGE,
						new ImageIcon(getClass().getResource("/DAD_SS.jpg")));
		}

	}

	public int[] convertRowsIndextoModel() {
		int[] rows = membros.getSelectedRows();
		for (int i = 0; i < rows.length; i++) {
			rows[i] = membros.convertRowIndexToModel(rows[i]);
		}
		return rows;
	}

	public void clearTextFields() {
		nome.setText("");
		date_nasc.cleanup();
		cpf.setText("");
		telefone.setText("");
	}

	public void removerUsers() {
		int[] rows = convertRowsIndextoModel();
		if (rows.length > 0) {
			int ok = JOptionPane.showConfirmDialog(this, "Tem certeza que quer apagar o(s) cliente(s) selecionado(s)?",
					"APAGAR", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE,
					new ImageIcon(getClass().getResource("/DAD_SS.jpg")));
			if (ok == JOptionPane.OK_OPTION) {
				modelMembro.removeUser(rows);
			}
		}
	}

	public JTable getUsers() {
		return membros;
	}

	public void abrir(Membro user) {
		new MembroDetail(user).open();
	}

	public JTextField getJtfTotal() {
		return jtfTotal;
	}

	private class DeleteAction extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 5018711156829284772L;

		@Override
		public void actionPerformed(ActionEvent e) {
			removerUsers();
		}
	}

	public static MembroPanel getInstance() {
		if (INSTANCE == null)
			INSTANCE = new MembroPanel();
		return INSTANCE;
	}

	public JButton getbAdd() {
		return bAdd;
	}

}
