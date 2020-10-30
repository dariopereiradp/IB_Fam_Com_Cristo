package dad.fam_com_cristo.table;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import dad.fam_com_cristo.gui.DataGui;
import dad.fam_com_cristo.table.command.AtualizaTransacao;
import dad.fam_com_cristo.table.command.Command;
import dad.fam_com_cristo.table.command.CompositeCommand;
import dad.fam_com_cristo.table.conexao.ConexaoFinancas;
import dad.fam_com_cristo.types.Tipo_Transacao;
import dad.fam_com_cristo.types.Transacao;
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
	private String[] colunas = { "Nº Transação", "Data", "Valor", "Tipo", "Descrição", "Sub-total" };
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
			con = new ConexaoFinancas().getConnection();
			pst = con.prepareStatement("select * from financas order by ID");
			rs = pst.executeQuery();
			if (rs.next()) {
				do {
					int id = rs.getInt(1);
					LocalDate data = rs.getDate(2).toLocalDate();
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
			return transacoes.get(rowIndex).getData();
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
		case 1:
			return LocalDate.class;
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
					LocalDate data = (LocalDate) valor;
					if (!data.isEqual(transacao.getData())) {
						undoManager.execute(new AtualizaTransacao(this, "Data", transacao, valor));
					}
					break;
				case 2:
					if (transacao.getValue().compareTo((BigDecimal) valor) != 0
							&& ((BigDecimal) valor).compareTo(new BigDecimal("0")) != 0) {
						BigDecimal diferenca = transacao.getValue().subtract((BigDecimal) valor);
						diferenca = transacao.getTotal().subtract(diferenca);
						undoManager.execute(new CompositeCommand("Atualizar valor",
								new AtualizaTransacao(this, "Valor", transacao, valor),
								new AtualizaTransacao(this, "Total", transacao, diferenca)));
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
	 * @return o salto total (Entradas - Saidas)
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

	public BigDecimal getTotalEntradas(LocalDate init, LocalDate end) {
		if (init == null && end == null)
			return getTotal_Entradas();
		else {
			BigDecimal n = new BigDecimal("0.0");
			if (init == null) {
				for (Transacao t : transacoes) {
					if (t.getTipo() == Tipo_Transacao.ENTRADA
							&& (t.getData().isBefore(end) || t.getData().isEqual(end)))
						n = n.add(t.getValue());
				}
			} else if (end == null) {
				for (Transacao t : transacoes) {
					if (t.getTipo() == Tipo_Transacao.ENTRADA
							&& (t.getData().isAfter(init) || t.getData().isEqual(init)))
						n = n.add(t.getValue());
				}
			} else {
				for (Transacao t : transacoes) {
					if (t.getTipo() == Tipo_Transacao.ENTRADA
							&& (t.getData().isAfter(init) || t.getData().isEqual(init))
							&& (t.getData().isBefore(end) || t.getData().isEqual(end)))
						n = n.add(t.getValue());
				}
			}
			return n;
		}
	}

	public BigDecimal getTotalSaidas(LocalDate init, LocalDate end) {
		if (init == null && end == null)
			return getTotal_Saidas();
		else {
			BigDecimal n = new BigDecimal("0.0");
			if (init == null) {
				for (Transacao t : transacoes) {
					if (t.getTipo() == Tipo_Transacao.SAIDA && (t.getData().isBefore(end) || t.getData().isEqual(end)))
						n = n.add(t.getValue());
				}
			} else if (end == null) {
				for (Transacao t : transacoes) {
					if (t.getTipo() == Tipo_Transacao.SAIDA && (t.getData().isAfter(init) || t.getData().isEqual(init)))
						n = n.add(t.getValue());
				}
			} else {
				for (Transacao t : transacoes) {
					if (t.getTipo() == Tipo_Transacao.SAIDA && (t.getData().isAfter(init) || t.getData().isEqual(init))
							&& (t.getData().isBefore(end) || t.getData().isEqual(end)))
						n = n.add(t.getValue());
				}
			}
			return n;
		}
	}

	public BigDecimal getTotalPeriod(LocalDate init, LocalDate end) {
		return getTotalEntradas(init, end).subtract(getTotalSaidas(init, end));
	}

	public List<BigDecimal> getTotalEntradasPorMes(int ano) {
		List<BigDecimal> list = new ArrayList<>(12);
		for (int i = 1; i <= 12; i++) {
			LocalDate init = LocalDate.of(ano, i, 1);
			LocalDate end = init.with(TemporalAdjusters.lastDayOfMonth());
			list.add(getTotalEntradas(init, end));
		}
		return list;
	}

	public List<BigDecimal> getTotalSaidasPorMes(int ano) {
		List<BigDecimal> list = new ArrayList<>(12);
		for (int i = 1; i <= 12; i++) {
			LocalDate init = LocalDate.of(ano, i, 1);
			LocalDate end = init.with(TemporalAdjusters.lastDayOfMonth());
			list.add(getTotalSaidas(init, end));
		}
		return list;
	}

	public List<BigDecimal> getSubTotalPorMes(int ano) {
		List<BigDecimal> list = new ArrayList<>(12);
		for (int i = 1; i <= 12; i++) {
			LocalDate init = LocalDate.of(ano, i, 1);
			LocalDate end = init.with(TemporalAdjusters.lastDayOfMonth());
			list.add(getTotalPeriod(init, end));
		}
		return list;
	}

	private int getNum_Entradas() {
		int n = 0;
		for (Transacao t : transacoes) {
			if (t.getTipo() == Tipo_Transacao.ENTRADA)
				n++;
		}
		return n;
	}

	private int getNum_Saidas() {
		int n = 0;
		for (Transacao t : transacoes) {
			if (t.getTipo() == Tipo_Transacao.SAIDA)
				n++;
		}
		return n;
	}

	public int getNumEntradasPorPeriodo(LocalDate init, LocalDate end) {
		if (init == null && end == null)
			return getNum_Entradas();
		else {
			int n = 0;
			if (init == null) {
				for (Transacao t : transacoes) {
					if (t.getTipo() == Tipo_Transacao.ENTRADA
							&& (t.getData().isBefore(end) || t.getData().isEqual(end)))
						n++;
				}
			} else if (end == null) {
				for (Transacao t : transacoes) {
					if (t.getTipo() == Tipo_Transacao.ENTRADA
							&& (t.getData().isAfter(init) || t.getData().isEqual(init)))
						n++;
				}
			} else {
				for (Transacao t : transacoes) {
					if (t.getTipo() == Tipo_Transacao.ENTRADA
							&& (t.getData().isAfter(init) || t.getData().isEqual(init))
							&& (t.getData().isBefore(end) || t.getData().isEqual(end)))
						n++;
				}
			}
			return n;
		}
	}

	public int getNumSaidasPorPeriodo(LocalDate init, LocalDate end) {
		if (init == null && end == null)
			return getNum_Saidas();
		else {
			int n = 0;
			if (init == null) {
				for (Transacao t : transacoes) {
					if (t.getTipo() == Tipo_Transacao.SAIDA && (t.getData().isBefore(end) || t.getData().isEqual(end)))
						n++;
				}
			} else if (end == null) {
				for (Transacao t : transacoes) {
					if (t.getTipo() == Tipo_Transacao.SAIDA && (t.getData().isAfter(init) || t.getData().isEqual(init)))
						n++;
				}
			} else {
				for (Transacao t : transacoes) {
					if (t.getTipo() == Tipo_Transacao.SAIDA && (t.getData().isAfter(init) || t.getData().isEqual(init))
							&& (t.getData().isBefore(end) || t.getData().isEqual(end)))
						n++;
				}
			}
			return n;
		}
	}

	/**
	 * Ordena as transacoes por data
	 */
	public TableModelFinancas ordenar() {
		transacoes.sort(null);
		fireTableDataChanged();
		Log.getInstance().printLog("Transações ordenadas com sucesso!");
		return this;

	}

	@SuppressWarnings("unchecked")
	public LocalDate getOldestDate() {
		ArrayList<Transacao> sorted = (ArrayList<Transacao>) transacoes.clone();

		sorted.sort((o1, o2) -> o1.getData().compareTo(o2.getData()));
		return sorted.get(0).getData();
	}

	public static TableModelFinancas getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new TableModelFinancas();
		}
		return INSTANCE;
	}
}
