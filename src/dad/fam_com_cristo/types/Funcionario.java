package dad.fam_com_cristo.types;

import java.time.LocalDateTime;

/**
 * Classe que representa os funcionários (pessoas que têm login para entrar no
 * programa).
 * 
 * @author Dário Pereira
 *
 */
public class Funcionario {

	private String nome;
	/**
	 * Número de vezes que esse funcionário fez login.
	 */
	private int num_acessos;
	private LocalDateTime data_ultimo_acesso;
	/**
	 * Data em que o funcionário foi registrado no programa.
	 */
	private LocalDateTime data_criacao;

	public Funcionario(String nome, int num_acessos, LocalDateTime data_ultimo_acesso, LocalDateTime data_criacao) {
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

}
