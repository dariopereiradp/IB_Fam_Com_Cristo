package dad.fam_com_cristo.types.enumerados;

/**
 * Enumerado que representa as op��es mutuamente exclusivas, Sim ou N�o
 * @author D�rio Pereira
 *
 */
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
	
	/**
	 * Converte uma String no enumerado correspondente, se huver
	 * 
	 * @param descricao descri��o exata do enumerado {@link #getDescricao()}
	 * @return o enumerado correspondente, se houver; caso contr�rio, lan�a uma
	 *         IllegalArgumentException
	 */
	public static Sim_Nao getEnum(String descricao){
		for(Sim_Nao sim_nao : values())
            if(sim_nao.getDescricao().equals(descricao)) return sim_nao;
        throw new IllegalArgumentException("A descri��o dada n�o corresponde a nenhum enumerado!");
	}
	
}
