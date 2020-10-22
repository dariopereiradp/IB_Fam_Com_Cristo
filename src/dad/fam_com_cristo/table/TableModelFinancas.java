package dad.fam_com_cristo.table;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.table.AbstractTableModel;

import dad.fam_com_cristo.Tipo_Transacao;
import dad.fam_com_cristo.Transacao;
import dad.fam_com_cristo.gui.DataGui;
import dad.recursos.Command;
import dad.recursos.ConexaoFinancas;
import dad.recursos.Log;
import dad.recursos.UndoManager;
import dad.recursos.Utils;

/**
 * Classe que representa o TableModel para as financas
 * 
 * @author Dário Pereira
 *
 */
public class TableModelFinancas extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3247984074345998765L;
	private static TableModelFinancas INSTANCE;
	private ArrayList<Transacao> transacoes;
	private String[] colunas = { "ID", "Data", "Valor", "Tipo", "Descrição", "Sub-total" };
	private Connection con;
	private PreparedStatement pst;
	private ResultSet rs;
	private UndoManager undoManager;

	private TableModelFinancas() {
		INSTANCE = this;
		undoManager = new UndoManager();
	}

	/**
	 * Faz upload da base de dados e cria o ArrayList com os clientes que existirem
	 * na base de dados Users.
	 */
	public void uploadDataBase() {
		transacoes = new ArrayList<>();
		int maior = 0;
		try {
			con = ConexaoFinancas.getConnection();
			pst = con.prepareStatement("select * from financas order by ID");
			rs = pst.executeQuery();
			if (rs.next()) {
				do {
					int id = rs.getInt(1);
					Date data = rs.getDate(2);
					BigDecimal valor = rs.getBigDecimal(3);
					Tipo_Transacao tipo = Tipo_Transacao.getEnum(rs.getString(4));
					String descricao = rs.getString(5);
					BigDecimal total = rs.getBigDecimal(6);
					Transacao transacao = new Transacao(valor, tipo, descricao, data, total);
					transacao.setId(id);
					if (id > maior)
						maior = id;
					transacoes.add(transacao);
				} while (rs.next());
			}
			Transacao.countID = maior;
			fireTableDataChanged();
			Log.getInstance().printLog("Base de dados Financas carregada com sucesso!");
		} catch (Exception e) {
			Log.getInstance()
					.printLog("Erro ao carregar a base de dados das Finanças: " + e.getMessage() + "\n" + getClass());
			e.printStackTrace();
		}
	}

	public UndoManager getUndoManager() {
		return undoManager;
	}

	/**
	 * Configura os listeners para mudar o estado dos menus undo e redo.
	 */
	public void addListeners() {
		undoManager.addPropertyChangeListener(e -> updateItems());
		updateItems();
	}

	/**
	 * Atualiza a disponibilidade e o texto dos menus Undo e Redo
	 */
	public void updateItems() {
		DataGui.getInstance().getMenuAnular().setEnabled(undoManager.isUndoAvailable());
		DataGui.getInstance().getMenuAnular().setText("Anular (Ctrl+Z) - (" + undoManager.getUndoName() + ")");
		DataGui.getInstance().getMenuRefazer().setEnabled(undoManager.isRedoAvailable());
		DataGui.getInstance().getMenuRefazer().setText("Refazer (Ctrl+Y) - (" + undoManager.getRedoName() + ")");
	}

	@Override
	public int getRowCount() {
		return transacoes.size();
	}

	@Override
	public int getColumnCount() {
		return colunas.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return colunas[columnIndex];
	}

	public ArrayList<Transacao> getTransacoes() {
		return transacoes;
	}

	/**
	 * Adiciona uma transacao à base de dados.
	 * 
	 * @param transacao - transacao que se pretende adicionar.
	 */
	public void addTransacao(Transacao transacao) {
		undoManager.execute(new AddTransacao(transacao));
		fireTableDataChanged();
	}

	public Transacao getTranscao(int rowIndex) {
		return transacoes.get(rowIndex);
	}

	/**
	 * 
	 * @param transacao transacao que se pretende descobrir em que linha está
	 * @return a linha em que a transacao está, se ele existir na tabela. <br>
	 *         -1 se a transacao não existir na tabela.
	 */
	public int getRow(Transacao transacao) {
		for (int i = 0; i < transacoes.size(); i++) {
			if (transacoes.get(i).getId() == transacao.getId())
				return i;
		}
		return -1;
	}

	/**
	 * Remove os membros que têm os indexes passados no array rows.
	 * 
	 * @param rows - array que contém os indexes dos membros para apagar.
	 */
	public void removerTransacao(int[] rows) {
		undoManager.execute(new RemoverTransacao(rows));
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return transacoes.get(rowIndex).getId();
		case 1:
			return new SimpleDateFormat("dd/MM/yyyy").format(transacoes.get(rowIndex).getData());
		case 2:
			return Utils.getInstance().getNumberFormatCurrency().format(transacoes.get(rowIndex).getValue());
		case 3:
			return transacoes.get(rowIndex).getTipo();
		case 4:
			return transacoes.get(rowIndex).getDescricao();
		case 5:
			return Utils.getInstance().getNumberFormatCurrency().format(transacoes.get(rowIndex).getTotal());
		default:
			return transacoes.get(rowIndex);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Class getColumnClass(int column) {
		switch (column) {
		case 0:
			return Integer.class;
		case 2:
			return BigDecimal.class;
		case 3:
			return Tipo_Transacao.class;
		case 5:
			return BigDecimal.class;
		default:
			return String.class;
		}
	}

	@Override
	public void setValueAt(Object valor, int rowIndex, int columnIndex) {
		try {
			if (valor != null) {
				if ((String.valueOf(valor).trim().equals("")))
					valor = "-";
				Transacao transacao = transacoes.get(rowIndex);
				switch (columnIndex) {
				case 1:
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date data = dateFormat.parse((String) valor);
					if (!dateFormat.format(transacao.getData()).equals(dateFormat.format(data)))
						undoManager.execute(new AtualizaTransacao(this, "Data", transacao, valor, true));
					break;
				case 2:
					if (transacao.getValue().compareTo((BigDecimal) valor) != 0) {
						undoManager.execute(new AtualizaTransacao(this, "Valor", transacao, valor));
					}
					break;
				case 3:
					if (transacao.getTipo() != (Tipo_Transacao) valor) {
						undoManager.execute(new AtualizaTransacao(this, "Tipo", transacao, valor));
					}
					break;
				case 4:
					String descricao = (String) valor;
					if (!transacao.getDescricao().equals(descricao))
						undoManager.execute(new AtualizaTransacao(this, "Descricao", transacao, valor));
				default:
					transacoes.get(rowIndex);
					break;
				}
				fireTableDataChanged();
				atualizarTextFieldsNumeros();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.getInstance().printLog("Erro no setValue()\n" + e.getMessage() + "\n" + getClass());
		}
	}

	/**
	 * Método para inserir uma transacao na base de dados, na posição pretendida.
	 * <br>
	 * Útil para o redo
	 * 
	 * @param transacao - transacao que se pretende inserir.
	 * @param row       - linha em que se pretende inserir a transacao.
	 */
	public void insertTransacao(Transacao transacao, int row) {
		transacao.adicionarNaBaseDeDados();
		transacoes.add(row, transacao);

	}

	/**
	 * Classe que representa um comando para remover uma ou várias transaçõess
	 * 
	 * @author Dário Pereira
	 *
	 */
	private class RemoverTransacao implements Command {

		private int[] rows;
		private ArrayList<Transacao> remover = new ArrayList<>();

		public RemoverTransacao(int[] rows) {
			this.rows = rows;
		}

		@Override
		public void execute() {
			try {
				for (int i = 0; i < rows.length; i++) {
					transacoes.get(rows[i]).removerBaseDeDados();
					remover.add(transacoes.get(rows[i]));
				}
				transacoes.removeAll(remover);
				fireTableDataChanged();
				atualizarTextFieldsNumeros();
				Log.getInstance().printLog("Transação(ões) apagada(s) com sucesso!");
			} catch (Exception e) {
				Log.getInstance().printLog("Erro ao apagar a(s) transação(ões)\n" + e.getMessage());
				e.printStackTrace();
			}
		}

		@Override
		public void undo() {
			for (int i = 0; i < rows.length; i++) {
				insertTransacao(remover.get(i), rows[i]);
			}
			atualizarTextFieldsNumeros();
			fireTableDataChanged();
		}

		@Override
		public void redo() {
			execute();
		}

		@Override
		public String getName() {
			return "Remover Transação";
		}
	}

	/**
	 * Classe que representa um comando para adicionar uma transcação.
	 * 
	 * @author Dário Pereira
	 *
	 */
	private class AddTransacao implements Command {

		private Transacao transacao;

		public AddTransacao(Transacao transacao) {
			this.transacao = transacao;
		}

		@Override
		public void execute() {
			try {
				transacao.adicionarNaBaseDeDados();
				transacoes.add(transacao);
				atualizarTextFieldsNumeros();
			} catch (Exception e) {
				Log.getInstance().printLog("Erro ao adicionar transação! " + e.getMessage());
				e.printStackTrace();
			}
		}

		@Override
		public void undo() {
			transacao.removerBaseDeDados();
			transacoes.remove(transacao);
			atualizarTextFieldsNumeros();
			fireTableDataChanged();
		}

		@Override
		public void redo() {
			execute();
			fireTableDataChanged();
		}

		@Override
		public String getName() {
			return "Adicionar Membro";
		}
	}

	/**
	 * Atualiza as estatísticas das finanças
	 */
	public void atualizarTextFieldsNumeros() {
		FinancasPanel.getInstance().getJtfTotal().setValue(getTotal());
		FinancasPanel.getInstance().getJtfEntradas().setValue(getTotal_Entradas());
		FinancasPanel.getInstance().getJtfSaidas().setValue(getTotal_Saidas());
	}

	/**
	 * 
	 * @return o número total de transações
	 */
	public BigDecimal getTotal() {
		return getTotal_Entradas().subtract(getTotal_Saidas());
	}

	public BigDecimal getTotal_Entradas() {
		BigDecimal n = new BigDecimal("0.0");
		for (Transacao t : transacoes) {
			if (t.getTipo() == Tipo_Transacao.ENTRADA)
				n = n.add(t.getValue());
		}
		return n;
	}

	public BigDecimal getTotal_Saidas() {
		BigDecimal n = new BigDecimal("0.0");
		for (Transacao t : transacoes) {
			if (t.getTipo() == Tipo_Transacao.SAIDA)
				n = n.add(t.getValue());
		}
		return n;
	}

//	/**
//	 * Ordena a tabela de membros por ordem alfabética
//	 */
//	public void ordenar() {
//		transacoes.sort(null);
//		fireTableDataChanged();
//		Log.getInstance().printLog("Membros ordenados com sucesso!");

//	}

	public static TableModelFinancas getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new TableModelFinancas();
		}
		return INSTANCE;
	}
}
