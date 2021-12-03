package dad.fam_com_cristo.types;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;

import dad.fam_com_cristo.table.conexao.ConexaoFinancas;
import dad.fam_com_cristo.types.enumerados.Tipo_Transacao;
import dad.recursos.DataPesquisavel;
import dad.recursos.Log;
import dad.recursos.Money;

public class Transacao implements Comparable<Transacao> {

	/**
	 * Variável usada para controle do próximo id a ser atribuído a um novo item.
	 */
	public static int countID = 0;
	private int id;
	private Money value;
	private Tipo_Transacao tipo;
	private String descricao;
	private DataPesquisavel data;
	private Money total;
	private static Connection con;
	private static PreparedStatement pst;

	public Transacao(BigDecimal value, Tipo_Transacao tipo, String descricao, LocalDate data, BigDecimal total) {
		init(value, tipo, descricao, data, total);
	}

	/**
	 * @param value
	 * @param tipo
	 * @param descricao
	 * @param data
	 * @param total
	 */
	private void init(BigDecimal value, Tipo_Transacao tipo, String descricao, LocalDate data, BigDecimal total) {
		con = new ConexaoFinancas().getConnection();
		id = ++countID;
		this.value = new Money(value);
		this.tipo = tipo;
		this.descricao = descricao;
		this.data = new DataPesquisavel(data);
		this.total = new Money(total);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public BigDecimal getValue() {
		return value.getValue();
	}
	
	public Money getValueMoney() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value.setValue(value);
	}

	public Tipo_Transacao getTipo() {
		return tipo;
	}

	public void setTipo(Tipo_Transacao tipo) {
		this.tipo = tipo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public LocalDate getData() {
		return data.getData();
	}
	
	public DataPesquisavel getDataPesquisavel() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data.setData(data);;
	}

	public BigDecimal getTotal() {
		return total.getValue();
	}
	
	public Money getTotalMoney() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total.setValue(total);
	}

	/**
	 * Adiciona transacao na base de dados.
	 */
	public void adicionarNaBaseDeDados() {
		try {
			pst = con.prepareStatement("insert into financas(ID,Data,Valor,Tipo,Descricao,Total) values (?,?,?,?,?,?)");
			pst.setInt(1, getId());
			pst.setDate(2, Date.valueOf(getData()));
			pst.setBigDecimal(3, getValue());
			pst.setString(4, getTipo().getDescricao());
			pst.setString(5, getDescricao());
			pst.setBigDecimal(6, getTotal());
			pst.execute();
		} catch (Exception e) {
			Log.getInstance().printLog("Erro ao adicionar transação na base de dados! - " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Remove a transacao da base de dados.
	 */
	public void removerBaseDeDados() {
		try {
			pst = con.prepareStatement("delete from financas where ID=?");
			pst.setInt(1, getId());
			pst.execute();
		} catch (Exception e) {
			Log.getInstance().printLog("Erro ao adicionar transacao na base de dados! - " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Compara de acordo com a data, para ordenar por data
	 */
	@Override
	public int compareTo(Transacao o) {
		int r = this.getData().compareTo(o.getData());
		if (r == 0)
			return Integer.valueOf(this.getId()).compareTo(Integer.valueOf(o.getId()));
		else
			return r;
	}

	@Override
	public String toString() {
		return "Transacao [id=" + id + ", value=" + value + ", tipo=" + tipo + ", descricao=" + descricao + ", data="
				+ data + ", total=" + total + "]";
	}
	
	

}
