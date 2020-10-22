package dad.fam_com_cristo.table;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.table.AbstractTableModel;

import dad.fam_com_cristo.Tipo_Transacao;
import dad.fam_com_cristo.Transacao;
import dad.recursos.Command;
import dad.recursos.ConexaoFinancas;
import dad.recursos.Log;

/**
 * Classe que representa um comando para atualizar as informações de uma transacao,
 * implementando as funções de undo e redo e guardando na base de dados.
 * 
 * @author Dário Pereira
 *
 */
public class AtualizaTransacao implements Command {

	private Connection con;
	private PreparedStatement pst;
	private Transacao transacao;
	private String coluna;
	private Object valor;
	private Object old;
	private AbstractTableModel table;
	private boolean data_string;

	public AtualizaTransacao(AbstractTableModel table, String coluna, Transacao transacao, Object valor) {
		this.table = table;
		this.coluna = coluna;
		this.transacao = transacao;
		this.valor = valor;
		con = ConexaoFinancas.getConnection();
		inicializar();
		data_string = false;
	}

	public AtualizaTransacao(AbstractTableModel table, String coluna, Transacao transacao, Object valor, boolean data_string) {
		this.table = table;
		this.coluna = coluna;
		this.transacao = transacao;
		this.valor = valor;
		con = ConexaoFinancas.getConnection();
		inicializar();
		this.data_string = data_string;
	}

	private void inicializar() {
		switch (coluna) {
		case "Valor":
			old = transacao.getValue();
			break;
		case "Data":
			old = transacao.getData();
			break;
		case "Tipo":
			old = transacao.getTipo();
			break;
		case "Descricao":
			old = transacao.getDescricao();
			break;
		case "Total":
			old = transacao.getTotal();
			break;
		default:
			break;
		}

	}

	@Override
	public void execute() {
		try {
			pst = con.prepareStatement("update financas set " + coluna + "=? where ID=" + transacao.getId());
			switch (coluna) {
			case "Valor":
//				BigDecimal value = new BigDecimal(Utils.getInstance().getNumberFormatCurrency().parse((String) valor).toString());
				pst.setBigDecimal(1, (BigDecimal) valor);
				transacao.setValue((BigDecimal) valor);
				break;
			case "Data":
				if (data_string) {
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date dataTransacao = dateFormat.parse((String) valor);
					transacao.setData(dataTransacao);
					String data = new SimpleDateFormat("yyyy-M-d").format(dataTransacao);
					pst.setDate(1, java.sql.Date.valueOf(data));
				} else {
					Date dataTransacao = (Date) valor;
					transacao.setData(dataTransacao);
					String data = new SimpleDateFormat("yyyy-M-d").format(dataTransacao);
					pst.setDate(1, java.sql.Date.valueOf(data));
				}
				break;
			case "Tipo":
				Tipo_Transacao tipoTransacao = (Tipo_Transacao) valor;
				pst.setString(1, tipoTransacao.getDescricao());
				transacao.setTipo(tipoTransacao);
				break;
			case "Descricao":
				pst.setString(1, (String) valor);
				transacao.setDescricao((String) valor);
				break;
			case "Total":
//				BigDecimal valueTotal = new BigDecimal(Utils.getInstance().getNumberFormatCurrency().parse((String) valor).toString());
				pst.setBigDecimal(1, (BigDecimal) valor);
				transacao.setTotal((BigDecimal) valor);
				break;
			
			default:
				break;
			}
//			pst.setInt(2, membro.getId());
			pst.execute();
			table.fireTableDataChanged();
		} catch (Exception e) {
			Log.getInstance().printLog("Erro ao atualizar " + coluna);
			e.printStackTrace();
		}

	}

	@Override
	public void undo() {
		try {
			pst = con.prepareStatement("update financas set " + coluna + "=? where ID=?");
			switch (coluna) {
			case "Valor":
				pst.setBigDecimal(1, (BigDecimal) old);
				transacao.setValue((BigDecimal) old);
				break;
			case "Data":
				String data = new SimpleDateFormat("yyyy-M-d").format((Date) old);
				pst.setDate(1, java.sql.Date.valueOf(data));
				transacao.setData((Date) old);
				break;
			case "Tipo":
				Tipo_Transacao tipoTransacao = (Tipo_Transacao) old;
				pst.setString(1, tipoTransacao.getDescricao());
				transacao.setTipo(tipoTransacao);
				break;
			case "Descricao":
				pst.setString(1, (String) old);
				transacao.setDescricao((String) old);
				break;
			case "Total":
				pst.setBigDecimal(1, (BigDecimal) old);
				transacao.setTotal((BigDecimal) old);
				break;
			default:
				break;
			}
			pst.setInt(2, transacao.getId());
			pst.execute();
			table.fireTableDataChanged();
		} catch (Exception e) {
			Log.getInstance().printLog("Erro ao anular a ação!\n" + e.getMessage());
			e.printStackTrace();
		}

	}

	@Override
	public void redo() {
		execute();
	}

	@Override
	public String getName() {
		return "Editar " + coluna;
	}
}

