package dad.fam_com_cristo.types;

/**
 * Enumerado que representa as opções mutuamente exclusivas, Sim ou Não
 * @author Dário Pereira
 *
 */
public enum Sim_Nao {

	NAO ("Não"),
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
	
	public static Sim_Nao getEnum(String descricao){
		for(Sim_Nao sim_nao : values())
            if(sim_nao.getDescricao().equals(descricao)) return sim_nao;
        throw new IllegalArgumentException("A descrição dada não corresponde a nenhum enumerado!");
	}
	
}
