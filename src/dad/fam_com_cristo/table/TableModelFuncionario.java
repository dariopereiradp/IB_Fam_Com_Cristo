package dad.fam_com_cristo.table;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import dad.fam_com_cristo.gui.ChangePassword;
import dad.fam_com_cristo.table.conexao.ConexaoLogin;
import dad.fam_com_cristo.types.Funcionario;
import dad.recursos.Log;

/**
 * Classe que representa o TableModel para os funcion�rios.
 * 
 * @author D�rio Pereira
 *
 */
public class TableModelFuncionario extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3247984074345998765L;
	private static TableModelFuncionario INSTANCE;
	private ArrayList<Funcionario> funcionarios;
	private String[] colunas = { "Nome (Login)", "Senha", "N�mero de Acessos", "Data do �ltimo Acesso",
			"Data de Cria��o" };
	private Connection con;
	private PreparedStatement pst;
	private ResultSet rs;

	private TableModelFuncionario() {
		INSTANCE = this;
	}

	/**
	 * Faz upload da base de dados e cria o ArrayList com os funcion�rios que
	 * existirem na base de dados Logins.
	 */
	public void uploadDataBase() {
		funcionarios = new ArrayList<>();
		try {
			con = ConexaoLogin.getConnection();
			pst = con.prepareStatement("select * from logins");
			rs = pst.executeQuery();
			if (rs.next()) {
				do {
					String nome = rs.getString(1);
					int num_acessos = rs.getInt(3);
					Date data_ultimo_acesso = rs.getTimestamp(4);
					Date data_criacao = rs.getTimestamp(5);
					Funcionario func = new Funcionario(nome, num_acessos, data_ultimo_acesso, data_criacao);
					funcionarios.add(func);
				} while (rs.next());
			}
			fireTableDataChanged();
			Log.getInstance().printLog("Base de dados funcionarios carregada com sucesso!");
		} catch (Exception e) {
			Log.getInstance().printLog(
					"Erro ao carregar a base de dados dos Funcion�rios: " + e.getMessage() + "\n" + getClass());
			e.printStackTrace();
		}
	}

	@Override
	public int getRowCount() {
		return funcionarios.size();
	}

	@Override
	public int getColumnCount() {
		return colunas.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return colunas[columnIndex];
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return funcionarios.get(rowIndex).getNome();
		case 1:
			return "********";
		case 2:
			return funcionarios.get(rowIndex).getNum_acessos();
		case 3:
			return new SimpleDateFormat("dd/MM/yyyy '�s' HH'h'mm'm'ss's'")
					.format(funcionarios.get(rowIndex).getData_ultimo_acesso());
		case 4:
			return new SimpleDateFormat("dd/MM/yyyy '�s' HH'h'mm'm'ss's'")
					.format(funcionarios.get(rowIndex).getData_criacao());
		default:
			return funcionarios.get(rowIndex);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Class getColumnClass(int column) {
		if (column == 2) {
			return Integer.class;
		}
		return String.class;
	}

	public ArrayList<Funcionario> getFuncionarios() {
		return funcionarios;
	}

	/**
	 * Adiciona um funcion�rio.
	 * @param func - funcion�rio a adicionar
	 */
	public void addFuncionario(Funcionario func) {
		funcionarios.add(func);
	}

	public Funcionario getFuncionario(int rowIndex) {
		return funcionarios.get(rowIndex);
	}

	/**
	 * Remove os funcion�rios que est�o nas posi��es indicadas pelo array rows.
	 * @param rows - array que cont�m as posi��es dos funcion�rios a remover.
	 */
	public void removeFuncionarios(int[] rows) {
		ArrayList<Funcionario> toDelete = new ArrayList<>();
		for (int i = 0; i < rows.length; i++) {
			Funcionario func = funcionarios.get(rows[i]);
			if (!func.getNome().equals("admin"))
				apagar(func, toDelete);
		}
		funcionarios.removeAll(toDelete);
	}

	/**
	 * Remove um funcion�rio da base de dados.
	 * @param func - funcion�rios a remover
	 * @param toDelete
	 */
	private void apagar(Funcionario func, ArrayList<Funcionario> toDelete) {
		try {
			con = ConexaoLogin.getConnection();
			pst = con.prepareStatement("delete from logins where Nome=?");
			pst.setString(1, func.getNome());
			pst.execute();
			toDelete.add(func);
			fireTableDataChanged();
		} catch (SQLException e1) {
			Log.getInstance().printLog("Erro ao apagar o funcion�rio! - " + e1.getMessage());
			e1.printStackTrace();
		}

	}

	/**
	 * @param table - tabela que se pretende converter os indexes
	 * @return um array com todos os indexes do modelo dos funcion�rios que est�o
	 *         selecionados.
	 */
	public int[] convertRowsIndextoModel(JTable table) {
		int[] rows = table.getSelectedRows();
		for (int i = 0; i < rows.length; i++) {
			rows[i] = table.convertRowIndexToModel(rows[i]);
		}
		return rows;
	}

	/**
	 * Devolve uma nova tabela de Funcionarios
	 * 
	 * @return - a nova tabela
	 */
	public JTable getSmallTable() {
		JTable small = new Table(this, colunas, false);

		small.setPreferredScrollableViewportSize(new Dimension(1100, 350));

		small.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -3355270436133061350L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean selected,
					boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, selected, hasFocus, row, column);
				if (!getFuncionario(small.convertRowIndexToModel(row)).getNome().equals("admin"))
					this.setIcon(new ImageIcon(getClass().getResource("/edit.png")));
				else
					this.setIcon(null);
				return this;
			}
		});

		small.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {
				small.scrollRectToVisible(small.getCellRect(small.getRowCount() - 1, 0, true));
			}

		});

		small.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent mouseEvent) {
				JTable table = (JTable) mouseEvent.getSource();
				Point point = mouseEvent.getPoint();
				int column = table.columnAtPoint(point);
				int rowAtPoint = table.rowAtPoint(point);
				if (rowAtPoint != -1) {
					int row = table.convertRowIndexToModel(rowAtPoint);
					if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
						if (!getFuncionario(row).getNome().equals("admin") && column == 1)
							new ChangePassword(getFuncionario(row).getNome(), true)
									.open();
						;

					}
				}
			}

		});

		JMenuItem mudarPassword = new JMenuItem("Alterar senha");
		mudarPassword.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new ChangePassword(TableModelFuncionario.getInstance()
						.getFuncionario(small.convertRowIndexToModel(small.getSelectedRow())).getNome(), true).open();
			}
		});

		JMenuItem apagarItem = new JMenuItem("Apagar");
		apagarItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int[] rows = TableModelFuncionario.getInstance().convertRowsIndextoModel(small);
				if (rows.length > 0) {
					int ok = JOptionPane.showConfirmDialog(small,
							"Tem certeza que quer apagar o(s) funcion�rio(s) selecionado(s)?\nATEN��O: ESSA A��O N�O PODE SER ANULADA!",
							"APAGAR", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE,
							new ImageIcon(getClass().getResource("/DAD_SS.jpg")));
					if (ok == JOptionPane.OK_OPTION) {
						TableModelFuncionario.getInstance().removeFuncionarios(rows);
					}
				}
			}
		});

		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.addPopupMenuListener(new PopupMenuListener() {

			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						int rowAtPointOriginal = small
								.rowAtPoint(SwingUtilities.convertPoint(popupMenu, new Point(0, 0), small));
						if (rowAtPointOriginal > -0) {
							int rowAtPoint = small.convertRowIndexToModel(rowAtPointOriginal);
							if (rowAtPoint > 0) {
								popupMenu.setVisible(true);
								int[] rows = TableModelFuncionario.getInstance().convertRowsIndextoModel(small);
								if (rows.length == 1) {
									mudarPassword.setVisible(true);
									small.setRowSelectionInterval(rowAtPointOriginal, rowAtPointOriginal);
								} else {
									mudarPassword.setVisible(false);
								}
								apagarItem.setVisible(true);
							}
						} else {
							popupMenu.setVisible(false);
							mudarPassword.setVisible(false);
							apagarItem.setVisible(false);
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

		popupMenu.add(mudarPassword);
		popupMenu.add(apagarItem);
		popupMenu.setPopupSize(100, 60);
		small.setComponentPopupMenu(popupMenu);

		small.getInputMap().put(KeyStroke.getKeyStroke("DELETE"), "deleteRow");
		small.getActionMap().put("deleteRow", new AbstractAction() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				int[] rows = TableModelFuncionario.getInstance().convertRowsIndextoModel(small);
				if (rows.length > 0) {
					if (!(rows.length == 1 && rows[0] == 0)) {
						int ok = JOptionPane.showConfirmDialog(small,
								"Tem certeza que quer apagar o(s) funcion�rio(s) selecionado(s)?\nATEN��O: ESSA A��O N�O PODE SER ANULADA!",
								"APAGAR", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE,
								new ImageIcon(getClass().getResource("/DAD_SS.jpg")));
						if (ok == JOptionPane.OK_OPTION) {
							TableModelFuncionario.getInstance().removeFuncionarios(rows);
						}
					}
				}
			}
		});

		return small;
	}
	
	public static TableModelFuncionario getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new TableModelFuncionario();
		}
		return INSTANCE;
	}
}
