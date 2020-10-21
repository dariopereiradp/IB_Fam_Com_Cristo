package dad.fam_com_cristo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;

import dad.recursos.ConexaoFinancas;
import dad.recursos.Log;

public class Transacao {
	
	/**
	 * Variável usada para controle do próximo id a ser atribuído a um novo
	 * item.
	 */
	public static int countID = 0;
	private int id;
	private double value;
	private Tipo_Transacao tipo;
	private String descricao;
	private Date data;
	private double total;
	private static Connection con;
	private static PreparedStatement pst;
	
	public Transacao(double value, Tipo_Transacao tipo, String descricao, Date data, double total) {
		con = ConexaoFinancas.getConnection();
		id = ++countID;
		this.value = value;
		this.tipo = tipo;
		this.descricao = descricao;
		this.data = data;
		this.total = total;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
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

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}
	
	/**
	 * Adiciona transacao na base de dados.
	 */
	public void adicionarNaBaseDeDados() {
		try {
			pst = con.prepareStatement(
					"insert into financas(ID,Data,Valor,Tipo,Descricao,Total) values (?,?,?,?,?,?)");
			pst.setInt(1, getId());
			String data = new SimpleDateFormat("yyyy-M-d").format(getData());
			pst.setDate(2, java.sql.Date.valueOf(data));
			pst.setDouble(3, getValue());
			pst.setString(4, getTipo().getDescricao());
			pst.setString(5, getDescricao());
			pst.setDouble(6, getTotal());
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
			Log.getInstance().printLog("Erro ao adicionar membro na base de dados! - " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	
	

}
