package dad.fam_com_cristo.types;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import dad.fam_com_cristo.table.conexao.ConexaoLogin;
import dad.fam_com_cristo.types.enumerados.Tipo_Funcionario;
import dad.recursos.Log;

/**
 * Classe que representa os funcionários (pessoas que têm login para entrar no
 * programa).
 * 
 * @author Dário Pereira
 *
 */
public class Funcionario {

	private final String nome;
	/**
	 * Número de vezes que esse funcionário fez login.
	 */
	private int num_acessos;
	private LocalDateTime data_ultimo_acesso;
	/**
	 * Data em que o funcionário foi registrado no programa.
	 */
	private LocalDateTime data_criacao;
	private Tipo_Funcionario type;
	private Connection con;
	private PreparedStatement pst;

	public Funcionario(Tipo_Funcionario type, String nome, int num_acessos, LocalDateTime data_ultimo_acesso, LocalDateTime data_criacao) {
		super();
		this.type = type;
		this.nome = nome;
		this.num_acessos = num_acessos;
		this.data_ultimo_acesso = data_ultimo_acesso;
		this.data_criacao = data_criacao;
		con = ConexaoLogin.getConnection();
	}

	public String getNome() {
		return nome;
	}

	public int getNum_acessos() {
		return num_acessos;
	}

	public void setNum_acessos(int num_acessos) {
		this.num_acessos = num_acessos;
	}

	public LocalDateTime getData_ultimo_acesso() {
		return data_ultimo_acesso;
	}

	public void setData_ultimo_acesso(LocalDateTime data_ultimo_acesso) {
		this.data_ultimo_acesso = data_ultimo_acesso;
	}

	public LocalDateTime getData_criacao() {
		return data_criacao;
	}

	public void setData_criacao(LocalDateTime data_criacao) {
		this.data_criacao = data_criacao;
	}
	
	public Tipo_Funcionario getType() {
		return type;
	}
	
	public void setType(Tipo_Funcionario type) {
		this.type = type;
		try {
			pst = con.prepareStatement(
					"update logins set Tipo = ? where nome = ?");
			pst.setString(1, type.getDescricao());
			pst.setString(2, getNome());
			pst.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			Log.getInstance().printLog("Errp ao atualizar tipo de funcionário: " + e.getMessage());
		}
	}
	
	public void registerLogin() throws SQLException {
		pst = con.prepareStatement(
				"update logins set Num_acessos = Num_acessos + 1,Ultimo_Acesso=? where nome = ?");
		pst.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
		pst.setString(2, getNome());
		pst.execute();
	}

}
