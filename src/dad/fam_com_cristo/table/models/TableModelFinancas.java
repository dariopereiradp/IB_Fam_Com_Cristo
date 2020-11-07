package dad.fam_com_cristo.table.models;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import dad.fam_com_cristo.gui.DataGui;
import dad.fam_com_cristo.table.FinancasPanel;
import dad.fam_com_cristo.table.command.AtualizaTransacao;
import dad.fam_com_cristo.table.command.Command;
import dad.fam_com_cristo.table.conexao.ConexaoFinancas;
import dad.fam_com_cristo.types.Transacao;
import dad.fam_com_cristo.types.enumerados.Tipo_Transacao;
import dad.recursos.DataPesquisavel;
import dad.recursos.Log;
import dad.recursos.Money;
import dad.recursos.UndoManager;

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
	private String[] colunas = { "Data", "Valor", "Tipo", "Descrição", "Sub-total" };
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
			pst = con.prepareStatement("select * from financas order by Data");
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
			transacoes.sort(null);
			recalcularSubTotais(0, transacoes.size());
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
		DataGui.getInstance().getMenuAnular().setText("Anular (Ctrl+Z)"
				.concat(!undoManager.getUndoName().equals("") ? " - (" + undoManager.getUndoName() + ")" : ""));
		DataGui.getInstance().getMenuRefazer().setEnabled(undoManager.isRedoAvailable());
		DataGui.getInstance().getMenuRefazer().setText("Refazer (Ctrl+Y)"
				.concat(!undoManager.getRedoName().equals("") ? " - (" + undoManager.getRedoName() + ")" : ""));
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
	 * Adiciona uma transacao à base de dados, atualizando, se necessário, os demais
	 * campos
	 * 
	 * @param transacao - transacao que se pretende adicionar.
	 */
	public void addTransacao(Transacao transacao) {
		undoManager.execute(new AddTransacao(transacao));
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return transacoes.get(rowIndex).getDataPesquisavel();
		case 1:
			return transacoes.get(rowIndex).getValueMoney();
		case 2:
			return transacoes.get(rowIndex).getTipo();
		case 3:
			return transacoes.get(rowIndex).getDescricao();
		case 4:
			return transacoes.get(rowIndex).getTotalMoney();
		default:
			return transacoes.get(rowIndex);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Class getColumnClass(int column) {
		switch (column) {
		case 0:
			return DataPesquisavel.class;
		case 1:
			return Money.class;
		case 2:
			return Tipo_Transacao.class;
		case 4:
			return Money.class;
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
				case 0:
					LocalDate data = (LocalDate) valor;
					if (!data.isEqual(transacao.getData())) {
						LocalDate oldData = transacao.getData();
						transacao.setData(data);
						transacoes.sort(null);
						int rowIndex1 = transacoes.indexOf(transacao);
						transacao.setData(oldData);
						transacoes.sort(null);

						undoManager.execute(new AtualizaTransacao("Data", transacao, valor));
						int init = rowIndex >= rowIndex1 ? rowIndex1 : rowIndex;
						int end = rowIndex >= rowIndex1 ? rowIndex + 1 : rowIndex1 + 1;
						recalcularSubTotais(init, end);
					}
					break;
				case 1:
					if (transacao.getValue().compareTo((BigDecimal) valor) != 0
							&& ((BigDecimal) valor).compareTo(new BigDecimal("0")) != 0) {
						undoManager.execute(new AtualizaTransacao("Valor", transacao, valor));
						recalcularSubTotais(rowIndex, transacoes.size());
					}
					break;
				case 2:
					Tipo_Transacao tipo = ((Tipo_Transacao) valor);
					if (transacao.getTipo() != tipo) {
						undoManager.execute(new AtualizaTransacao("Tipo", transacao, valor));
						recalcularSubTotais(rowIndex, transacoes.size());
					}
					break;
				case 3:
					String descricao = (String) valor;
					if (!transacao.getDescricao().equals(descricao))
						undoManager.execute(new AtualizaTransacao("Descricao", transacao, valor));
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
	private void insertTransacao(Transacao transacao, int row) {
		transacao.adicionarNaBaseDeDados();
		transacoes.add(row, transacao);

	}

	/**
	 * Remove os membros que têm os indexes passados no array rows.
	 * 
	 * @param rows - array que contém os indexes dos membros para apagar.
	 */
	public void removerTransacao(int[] rows) {
		undoManager.execute(new RemoverTransacoes(rows));
	}

	/**
	 * Classe que representa um comando para remover uma ou várias transaçõess
	 * 
	 * @author Dário Pereira
	 *
	 */
	private class RemoverTransacoes implements Command {

		private int[] rows;
		private ArrayList<Transacao> remover = new ArrayList<>();

		public RemoverTransacoes(int[] rows) {
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
				transacoes.sort(null);
				recalcularSubTotais(rows[0], transacoes.size());
				fireTableDataChanged();
				atualizarTextFieldsNumeros();
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
			transacoes.sort(null);
			recalcularSubTotais(rows[0], transacoes.size());
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
	 * Recalcula os sub-totais das transações entre os indices e atualiza na base de
	 * dados
	 * 
	 * @param beginIndex - inclusive
	 * @param endIndex   - exclusive
	 */
	public void recalcularSubTotais(int beginIndex, int endIndex) {
		BigDecimal totalAnterior;
		if (beginIndex == 0)
			totalAnterior = new BigDecimal("0");
		else
			totalAnterior = transacoes.get(beginIndex - 1).getTotal();
		for (int i = beginIndex; i < endIndex; i++) {
			Transacao t = transacoes.get(i);
			BigDecimal oldTotal = t.getTotal();
			BigDecimal total;
			if (t.getTipo() == Tipo_Transacao.ENTRADA)
				total = totalAnterior.add(t.getValue());
			else
				total = totalAnterior.subtract(t.getValue());
			totalAnterior = total;
			if (!total.equals(oldTotal)) {
				t.setTotal(total);
				try {
					pst = con.prepareStatement("update financas set " + "Total" + "=? where ID=" + t.getId());
					pst.setBigDecimal(1, total);
					pst.execute();
				} catch (SQLException e) {
					Log.getInstance().printLog(getClass() + " - " + e.getMessage());
					e.printStackTrace();
				}
			}
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
		int index = 0;

		public AddTransacao(Transacao transacao) {
			this.transacao = transacao;
		}

		@Override
		public void execute() {
			try {
				transacao.adicionarNaBaseDeDados();
				transacoes.add(transacao);
				transacoes.sort(null);
				fireTableDataChanged();
				index = transacoes.indexOf(transacao);
				recalcularSubTotais(index, transacoes.size());
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
			transacoes.sort(null);
			recalcularSubTotais(index, transacoes.size());
			fireTableDataChanged();
		}

		@Override
		public void redo() {
			execute();
		}

		@Override
		public String getName() {
			return "Adicionar Transação";
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

	/**
	 * 
	 * @return o total de entradas
	 */
	public BigDecimal getTotal_Entradas() {
		BigDecimal n = new BigDecimal("0.0");
		for (Transacao t : transacoes) {
			if (t.getTipo() == Tipo_Transacao.ENTRADA)
				n = n.add(t.getValue());
		}
		return n;
	}

	/**
	 * 
	 * @return o total de saídas
	 */
	public BigDecimal getTotal_Saidas() {
		BigDecimal n = new BigDecimal("0.0");
		for (Transacao t : transacoes) {
			if (t.getTipo() == Tipo_Transacao.SAIDA)
				n = n.add(t.getValue());
		}
		return n;
	}

	/**
	 * 
	 * @param init
	 * @param end
	 * @return o total de entradas no entre o período indicado (inclusive)
	 */
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

	/**
	 * 
	 * @param init
	 * @param end
	 * @return o total de saídas no período indicado (inclusive)
	 */
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

	/**
	 * 
	 * @param init
	 * @param end
	 * @return o balanço do período indicado (entradas - saídas)
	 */
	public BigDecimal getTotalPeriod(LocalDate init, LocalDate end) {
		return getTotalEntradas(init, end).subtract(getTotalSaidas(init, end));
	}

	/**
	 * 
	 * @param ano
	 * @return lista com o total de entradas para cada mes do ano indicado
	 */
	public List<BigDecimal> getTotalEntradasPorMes(int ano) {
		List<BigDecimal> list = new ArrayList<>(12);
		for (int i = 1; i <= 12; i++) {
			LocalDate init = LocalDate.of(ano, i, 1);
			LocalDate end = init.with(TemporalAdjusters.lastDayOfMonth());
			list.add(getTotalEntradas(init, end));
		}
		return list;
	}

	/**
	 * 
	 * @param ano
	 * @return lista com o total de saídas por mes do ano indicado
	 */
	public List<BigDecimal> getTotalSaidasPorMes(int ano) {
		List<BigDecimal> list = new ArrayList<>(12);
		for (int i = 1; i <= 12; i++) {
			LocalDate init = LocalDate.of(ano, i, 1);
			LocalDate end = init.with(TemporalAdjusters.lastDayOfMonth());
			list.add(getTotalSaidas(init, end));
		}
		return list;
	}

	/**
	 * 
	 * @param ano
	 * @return lista com o balanço de cada mes do ano indicado (entradas-saidas)
	 */
	public List<BigDecimal> getSubTotalPorMes(int ano) {
		List<BigDecimal> list = new ArrayList<>(12);
		for (int i = 1; i <= 12; i++) {
			LocalDate init = LocalDate.of(ano, i, 1);
			LocalDate end = init.with(TemporalAdjusters.lastDayOfMonth());
			list.add(getTotalPeriod(init, end));
		}
		return list;
	}

	/**
	 * 
	 * @return o número de transações de entradas realizado
	 */
	private int getNum_Entradas() {
		int n = 0;
		for (Transacao t : transacoes) {
			if (t.getTipo() == Tipo_Transacao.ENTRADA)
				n++;
		}
		return n;
	}

	/**
	 * 
	 * @return o número de transações de saída realizado
	 */
	private int getNum_Saidas() {
		int n = 0;
		for (Transacao t : transacoes) {
			if (t.getTipo() == Tipo_Transacao.SAIDA)
				n++;
		}
		return n;
	}

	/**
	 * 
	 * @param init
	 * @param end
	 * @return número de transações de entrada no período indicado
	 */
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

	/**
	 * 
	 * @param init
	 * @param end
	 * @return número de transações de saída no período indicado
	 */
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

	/**
	 * 
	 * @return a data da transação mais antiga
	 */
	public LocalDate getOldestDate() {
		if (transacoes.size() > 0)
			return transacoes.get(0).getData();
		else
			return LocalDate.now();
	}

	public static TableModelFinancas getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new TableModelFinancas();
		}
		return INSTANCE;
	}
}
