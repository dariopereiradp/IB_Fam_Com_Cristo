package dad.fam_com_cristo;

import java.util.Date;

/**
 * Classe que representa os funcion�rios (pessoas que t�m login para entrar no programa).
 * @author D�rio Pereira
 *
 */
public class Funcionario {

	private String nome;
	/**
	 * N�mero de vezes que esse funcion�rio fez login.
	 */
	private int num_acessos;
	private Date data_ultimo_acesso;
	/**
	 * Data em que o funcion�rio foi registrado no programa.
	 */
	private Date data_criacao;

	public Funcionario(String nome, int num_acessos, Date data_ultimo_acesso, Date data_criacao) {
		super();
		this.nome = nome;
		this.num_acessos = num_acessos;
		this.data_ultimo_acesso = data_ultimo_acesso;
		this.data_criacao = data_criacao;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getNum_acessos() {
		return num_acessos;
	}

	public void setNum_acessos(int num_acessos) {
		this.num_acessos = num_acessos;
	}

	public Date getData_ultimo_acesso() {
		return data_ultimo_acesso;
	}

	public void setData_ultimo_acesso(Date data_ultimo_acesso) {
		this.data_ultimo_acesso = data_ultimo_acesso;
	}

	public Date getData_criacao() {
		return data_criacao;
	}

	public void setData_criacao(Date data_criacao) {
		this.data_criacao = data_criacao;
	}
	
	/**
	 * Regista um novo login feito por esse funcion�rio.
	 */
	public void novoAcesso() {
		num_acessos++;
		data_ultimo_acesso = new Date();
	}

}
