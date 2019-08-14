package dad.fam_com_cristo;

public enum Sim_Nao {

	NAO ("N�o"),
	SIM ("Sim");
	
	private String descricao;
	
	Sim_Nao(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}

	@Override
	public String toString() {
		return descricao;
	}
	
}
